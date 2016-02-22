package org.mcphoton.core;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.mcphoton.Difficulty;
import org.mcphoton.Gamemode;
import org.mcphoton.command.CommandExecutor;
import org.mcphoton.command.impl.DebugCommand;
import org.mcphoton.command.impl.ManCommand;
import org.mcphoton.command.impl.StopCommand;
import org.mcphoton.core.listeners.PlayerMoveListener;
import org.mcphoton.entity.impl.OnlinePlayer;
import org.mcphoton.event.Events;
import org.mcphoton.event.ListenOrder;
import org.mcphoton.event.impl.PlayerMoveEvent;
import org.mcphoton.messaging.TextChatMessage;
import org.mcphoton.network.PacketsRegisterer;
import org.mcphoton.network.PhotonPacketSender;
import org.mcphoton.network.ReceiverThread;
import org.mcphoton.plugin.GlobalPluginsManager;
import org.mcphoton.plugin.PhotonPluginsManager;
import org.mcphoton.plugin.Plugin;
import org.mcphoton.util.Location;
import org.mcphoton.world.OverWorld;
import org.mcphoton.world.World;
import com.electronwill.collections.Bag;
import com.electronwill.concurrent.IntConstant;
import com.electronwill.concurrent.SynchronizedBag;
import com.electronwill.streams.ByteArrayOutputStream;
import com.electronwill.text.ModifiableCharSequence;
import com.electronwill.text.StringUtils;

public final class Photon {
	
	// Global constants:
	public static final File baseDir = new File(".");
	public static final File worldsDir = new File(baseDir, "worlds");
	public static final File pluginsDir = new File(baseDir, "plugins");
	public static final File logsDir = new File(baseDir, "logs");
	public static final File logFile = new File(baseDir, "server.log");
	public static final File propsFile = new File(baseDir, "server.properties");
	// final File securityFile = new File(baseDir, "security.yml");
	public static final PhotonLog log;
	
	// Private values:
	private static final IntConstant port = new IntConstant();
	private static final Bag<OnlinePlayer> players = new SynchronizedBag<>();
	private static volatile int maxPlayers;
	private static volatile int viewDistance;
	private static volatile TextChatMessage description;
	private static volatile String logoBase64;
	private static volatile Location spawn;
	private static volatile boolean debug;
	private static volatile boolean registrationOpen = false;
	
	// Threads:
	private static ConsoleThread consoleThread;
	private static ReceiverThread receiverThread;
	private static ScheduledExecutorService executorService;
	
	// Package-private values:
	static volatile boolean stopped = false;
	
	public static void main(String[] args) {
		String pres = "Photon server v" + coreVersion() + " for Minecraft " + gameVersion();
		ModifiableCharSequence bar = new ModifiableCharSequence(pres.length());
		for (int i = 0; i < pres.length(); i++) {
			bar.append("=");
		}
		System.out.println(bar);
		System.out.println(pres);
		System.out.println(bar);
		
		log.info("Preparing the JVM for the worst (we don't know what could happen ^^)...");
		setupJVM();
		
		log.info("Configuring the server with \"server.properties\"...");
		try {
			configureServer();
		} catch (Exception e) {
			System.out.println("Unable to configure the server");
			e.printStackTrace();
		}
		
		log.info("Loading network packets...");
		try {
			registrationOpen = true;
			PacketsRegisterer.registerAll();
			registrationOpen = false;
		} catch (Throwable t) {
			log.error(t, "Unable to register all the packets");
		}
		
		log.info("Loading internal event listeners");
		Events.register(PlayerMoveEvent.class, new PlayerMoveListener(), ListenOrder.VERY_LAST);
		
		log.info("Loading and enabling plugins...");
		loadPlugins();
		
		log.info("Loading photon's commands...");
		loadCommands();
		
		log.info("Starting the working threads..");
		consoleThread = new ConsoleThread();
		consoleThread.start();
		PhotonPacketSender.start();
		try {
			InetSocketAddress bindAddress = new InetSocketAddress(port.get());
			receiverThread = ReceiverThread.createInstance(bindAddress);
			receiverThread.start();
		} catch (Exception e) {
			log.error(e, "Unable to start the ReceiverThread");
		}
	}
	
	public static String coreVersion() {
		return "0.4.1 Alpha (20/12/2015)";
	}
	
	public static String gameVersion() {
		return "1.8.*";
	}
	
	public static int protocolVersion() {
		return 47;
	}
	
	public static boolean isRegistrationPhase() {
		return registrationOpen;
	}
	
	/**
	 * Creates the PhotonLog object.
	 */
	static {
		System.out.println("Creating files if needed...");
		createFiles();
		Console console = System.console();
		PrintWriter writer;
		if (console == null) {
			writer = new PrintWriter(System.out, true);
		} else {
			writer = console.writer();
			System.setOut(new DebugLogPrintStream());// redirects System.out to the log
		}
		PhotonLog l = null;
		try {
			l = new PhotonLog(writer);
		} catch (IOException e) {
			System.err.println("Unable to get the PhotonLog object");
			e.printStackTrace();
		}
		log = l;
		if (console == null)
			log.warning("System.console() is null, so System.out cannot be redirected to the log");
	}
	
	private Photon() {}
	
	/**
	 * Create all missing files or directories.
	 */
	static void createFiles() {
		baseDir.mkdir();
		worldsDir.mkdir();
		pluginsDir.mkdir();
		logsDir.mkdir();
		if (!propsFile.exists()) {
			try {
				Path target = propsFile.toPath();
				InputStream input = Photon.class.getResourceAsStream("/resources/server.properties");
				Files.copy(input, target);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	static void setupJVM() {
		Thread.setDefaultUncaughtExceptionHandler(new PhotonExceptionHandler(log));
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}
	
	static void configureServer() throws FileNotFoundException, IOException {
		Properties defaults = new Properties();
		Properties props = new Properties(defaults);
		try (InputStream defaultsInput = Photon.class.getResourceAsStream("/resources/server.properties");
				Reader defaultsReader = new InputStreamReader(defaultsInput, StandardCharsets.UTF_8);
				Reader fileReader = new FileReader(propsFile)) {
				
			defaults.load(defaultsReader);
			props.load(fileReader);
			
			String portSetting = props.getProperty("port");
			port.init(Integer.parseInt(portSetting));
			
			String maxPlayersSetting = props.getProperty("maxPlayers");
			maxPlayers = Integer.parseInt(maxPlayersSetting);
			
			String descriptionSetting = props.getProperty("description");
			description = TextChatMessage.parse(descriptionSetting);
			
			String logoSetting = props.getProperty("logo");
			setLogo(logoSetting);
			
			String spawnSetting = props.getProperty("spawn");
			List<String> spawnParts = StringUtils.splitString(spawnSetting, ',');
			String worldName = spawnParts.get(0);
			int x = Integer.parseInt(spawnParts.get(1));
			int y = Integer.parseInt(spawnParts.get(2));
			int z = Integer.parseInt(spawnParts.get(3));
			World w = World.get(worldName);
			if (w == null) {
				w = new OverWorld(worldName, Difficulty.NORMAL, Gamemode.CREATIVE);
				World.register(w);
			}
			spawn = new Location(w, x, y, z);
			
			String viewDistanceSetting = props.getProperty("viewDistance");
			viewDistance = Integer.parseInt(viewDistanceSetting);
			
			String debugSetting = props.getProperty("debug");
			debug = Boolean.parseBoolean(debugSetting);
			log.setDebugEnabled(debug);
			
			String executorThreadsSetting = props.getProperty("executorThreads");
			int nThreads = Integer.parseInt(executorThreadsSetting);
			executorService = Executors.newScheduledThreadPool(nThreads);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	static void loadPlugins() {
		PhotonPluginsManager ppm = new PhotonPluginsManager();
		GlobalPluginsManager.register(ppm);
		
		List<Plugin> plugins = GlobalPluginsManager.loadAllFrom(pluginsDir);
		List<Exception> errors = GlobalPluginsManager.getExceptions(true);
		
		log.info("Found " + plugins.size() + " valid plugins");
		if (!errors.isEmpty()) {
			log.info("================================================================");
			log.warning(errors.isEmpty() + " error(s) occured while trying to load the plugins:");
			for (Exception ex : errors) {
				log.error(ex);
			}
			log.info("================================================================");
		}
		
		registrationOpen = true;
		for (Plugin p : plugins) {
			try {
				log.info("Loading plugin " + p.getName() + " v" + p.getVersion());
				p.onLoad();
			} catch (Throwable t) {
				log.error(t, "Unable to load plugin");
			}
		}
		registrationOpen = false;
		
		for (Plugin p : plugins) {
			try {
				log.info("Enabling plugin " + p.getName() + " v" + p.getVersion());
				GlobalPluginsManager.enable(p);
			} catch (Throwable t) {
				log.error(t, "Unable to enable plugin");
			}
		}
	}
	
	static void loadCommands() {
		StopCommand stopCmd = new StopCommand();
		CommandExecutor.register(stopCmd, "exit", "shutdown");
		
		DebugCommand debugCmd = new DebugCommand();
		CommandExecutor.register(debugCmd);
		
		ManCommand manCmd = new ManCommand();
		CommandExecutor.register(manCmd, "help");
	}
	
	public static ScheduledExecutorService executorService() {
		return executorService;
	}
	
	public static void setLogo(String name) throws IOException {
		InputStream in;
		if (name.equals("default")) {
			in = Photon.class.getResourceAsStream("/resources/photon-server-icon.png");
		} else {
			File f = new File(baseDir, name);
			in = new FileInputStream(f);
		}
		try {
			setLogo(in);
		} finally {
			in.close();
		}
	}
	
	@SuppressWarnings("all")
	public static void setLogo(InputStream in) throws IOException {
		ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream(4096);
		int read;
		byte[] buffer = new byte[4096];
		while ((read = in.read(buffer)) > 0) {
			bytesOutput.write(buffer);
		}
		logoBase64 = Base64.getEncoder().encodeToString(bytesOutput.toByteArray());// encodes it in base64
	}
	
	public static boolean isDebugEnabled() {
		return debug;
	}
	
	public static void setDebugEnabled(boolean enabled) {
		Photon.debug = enabled;
	}
	
	public static String getLogoBase64() {
		return logoBase64;
	}
	
	public static TextChatMessage getDescription() {
		return description;
	}
	
	public static Location getSpawn() {
		return spawn;
	}
	
	public static void setSpawn(Location spawn) {
		Photon.spawn = spawn;
	}
	
	public static int getMaxPlayers() {
		return maxPlayers;
	}
	
	public static void setMaxPlayers(int maxPlayers) {
		Photon.maxPlayers = maxPlayers;
	}
	
	public static int getPort() {
		return port.get();
	}
	
	public static int getViewDistance() {
		return viewDistance;
	}
	
	public static int getPlayerCount() {
		return players.size();
	}
	
	public static boolean addPlayer(OnlinePlayer p) {
		if (players.size() >= maxPlayers)
			return false;
		return players.add(p);
	}
	
	public static boolean removePlayer(OnlinePlayer p) {
		return players.remove(p);
	}
	
	public static void forEachPlayer(Consumer<OnlinePlayer> action) {
		synchronized (players) {
			for (int i = 0; i < players.size(); i++) {
				OnlinePlayer player = players.get(i);
				action.accept(player);
			}
		}
	}
	
	public static synchronized void stop() {
		if (stopped)
			throw new IllegalStateException("Server is already stopped");
			
		stopped = true;
		
		// TODO save players infos
		// TODO save world
		
		log.info("Disabling plugins...");
		Iterator<Plugin> enabledPlugins = GlobalPluginsManager.getEnabledPlugins();
		while (enabledPlugins.hasNext()) {
			Plugin p = enabledPlugins.next();
			try {
				log.info("Disabling plugin " + p.getName() + " v" + p.getVersion());
				p.onDisable();
			} catch (Throwable t) {
				log.error(t, "Error occured while disabling plugin");
			}
		}
		
		log.info("Stopping Threads...");
		consoleThread.stop();
		receiverThread.stop();
		PhotonPacketSender.stop();
		
		System.exit(0);
	}
	
	/*
	void configureSecutiry() {
		// TODO
	}*/
}
