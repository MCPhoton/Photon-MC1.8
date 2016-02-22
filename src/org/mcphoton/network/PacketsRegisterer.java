package org.mcphoton.network;

import static org.mcphoton.network.ConnectionState.INIT;
import static org.mcphoton.network.ConnectionState.LOGIN;
import static org.mcphoton.network.ConnectionState.PLAY;
import static org.mcphoton.network.ConnectionState.STATUS;
import org.mcphoton.network.serverbound.HandshakePacket;
import org.mcphoton.network.serverbound.login.EncryptionResponsePacket;
import org.mcphoton.network.serverbound.login.LoginStartPacket;
import org.mcphoton.network.serverbound.play.AnimationPacket;
import org.mcphoton.network.serverbound.play.ChatMessagePacket;
import org.mcphoton.network.serverbound.play.ClickWindowPacket;
import org.mcphoton.network.serverbound.play.ClientSettingsPacket;
import org.mcphoton.network.serverbound.play.ClientStatusPacket;
import org.mcphoton.network.serverbound.play.CloseWindowPacket;
import org.mcphoton.network.serverbound.play.ConfirmTransactionPacket;
import org.mcphoton.network.serverbound.play.CreativeInventoryActionPacket;
import org.mcphoton.network.serverbound.play.EnchantItemPacket;
import org.mcphoton.network.serverbound.play.EntityActionPacket;
import org.mcphoton.network.serverbound.play.HeldItemChangePacket;
import org.mcphoton.network.serverbound.play.KeepAlivePacket;
import org.mcphoton.network.serverbound.play.PlayerAbilitiesPacket;
import org.mcphoton.network.serverbound.play.PlayerBlockPlacementPacket;
import org.mcphoton.network.serverbound.play.PlayerDiggingPacket;
import org.mcphoton.network.serverbound.play.PlayerGroundPacket;
import org.mcphoton.network.serverbound.play.PlayerLookPacket;
import org.mcphoton.network.serverbound.play.PlayerPositionAndLookPacket;
import org.mcphoton.network.serverbound.play.PlayerPositionPacket;
import org.mcphoton.network.serverbound.play.PluginMessagePacket;
import org.mcphoton.network.serverbound.play.ResourcePackStatusPacket;
import org.mcphoton.network.serverbound.play.SpectatePacket;
import org.mcphoton.network.serverbound.play.SteerVehiclePacket;
import org.mcphoton.network.serverbound.play.TabCompletePacket;
import org.mcphoton.network.serverbound.play.UpdateSignPacket;
import org.mcphoton.network.serverbound.play.UseEntityPacket;
import org.mcphoton.network.serverbound.status.PingPacket;
import org.mcphoton.network.serverbound.status.RequestPacket;

/**
 * Registers all the Vanilla Minecraft serverbound packets.
 * 
 * @author ElectronWill
 * 		
 */
public final class PacketsRegisterer {
	
	private PacketsRegisterer() {}
	
	private static volatile boolean registered = false;
	
	/**
	 * Registers all the packets of Minecraft 1.8
	 * 
	 * @throws NoSuchMethodException
	 */
	public static void registerAll() throws NoSuchMethodException {
		if (registered)
			throw new IllegalStateException("Game packets can only be registered once!");
			
		// Handshake packet:
		ReceivablePacket.register(HandshakePacket.class, INIT, 0);
		
		// Login packets:
		ReceivablePacket.register(LoginStartPacket.class, LOGIN, 0);
		ReceivablePacket.register(EncryptionResponsePacket.class, LOGIN, 1);
		
		// Status packets:
		ReceivablePacket.register(RequestPacket.class, STATUS, 0);
		ReceivablePacket.register(PingPacket.class, STATUS, 1);
		
		// Play packets:
		ReceivablePacket.register(KeepAlivePacket.class, PLAY, 0);
		ReceivablePacket.register(ChatMessagePacket.class, PLAY, 1);
		ReceivablePacket.register(UseEntityPacket.class, PLAY, 2);
		ReceivablePacket.register(PlayerGroundPacket.class, PLAY, 3);
		ReceivablePacket.register(PlayerPositionPacket.class, PLAY, 4);
		ReceivablePacket.register(PlayerLookPacket.class, PLAY, 5);
		ReceivablePacket.register(PlayerPositionAndLookPacket.class, PLAY, 6);
		ReceivablePacket.register(PlayerDiggingPacket.class, PLAY, 7);
		ReceivablePacket.register(PlayerBlockPlacementPacket.class, PLAY, 8);
		ReceivablePacket.register(HeldItemChangePacket.class, PLAY, 9);
		ReceivablePacket.register(AnimationPacket.class, PLAY, 10);
		ReceivablePacket.register(EntityActionPacket.class, PLAY, 11);
		ReceivablePacket.register(SteerVehiclePacket.class, PLAY, 12);
		ReceivablePacket.register(CloseWindowPacket.class, PLAY, 13);
		ReceivablePacket.register(ClickWindowPacket.class, PLAY, 14);
		ReceivablePacket.register(ConfirmTransactionPacket.class, PLAY, 15);
		ReceivablePacket.register(CreativeInventoryActionPacket.class, PLAY, 16);
		ReceivablePacket.register(EnchantItemPacket.class, PLAY, 17);
		ReceivablePacket.register(UpdateSignPacket.class, PLAY, 18);
		ReceivablePacket.register(PlayerAbilitiesPacket.class, PLAY, 19);
		ReceivablePacket.register(TabCompletePacket.class, PLAY, 20);
		ReceivablePacket.register(ClientSettingsPacket.class, PLAY, 21);
		ReceivablePacket.register(ClientStatusPacket.class, PLAY, 22);
		ReceivablePacket.register(PluginMessagePacket.class, PLAY, 23);
		ReceivablePacket.register(SpectatePacket.class, PLAY, 24);
		ReceivablePacket.register(ResourcePackStatusPacket.class, PLAY, 25);
	}
	
}
