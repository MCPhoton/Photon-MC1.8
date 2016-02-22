package org.mcphoton.messaging;

import java.util.List;
import java.util.Map;
import com.electronwill.text.ModifiableCharSequence;

/**
 * A textual chat message. This class also provides a way to parse a message that uses '§'-codes, and to create such a
 * message.
 * 
 * @author ElectronWill
 */
public class TextChatMessage extends ChatMessage {
	
	/**
	 * Parses a "legacy string" that contains color and style codes. Each code consists of 2 characters: the '§'
	 * character and another character. The second character definds the color/style to apply.
	 */
	public static TextChatMessage parse(CharSequence csq) {
		TextChatMessage main = null;// the main part of the msg
		TextChatMessage current = null;// the part we're currently working on
		ModifiableCharSequence mcs = new ModifiableCharSequence();
		for (int i = 0; i < csq.length(); i++) {
			char c = csq.charAt(i);
			if (c == '§' && i + 1 < csq.length()) {
				
				if (main == null) {
					main = new TextChatMessage(mcs.toString());
					current = main;
				} else {
					current.setText(mcs.toString());
					if (current != main)
						main.addExtra(current);
					current = new TextChatMessage();
				}
				mcs = new ModifiableCharSequence();
				
				char c2 = csq.charAt(++i);
				switch (c2) {
					case '0':
						current.setColor(Color.BLACK);
						break;
					case '1':
						current.setColor(Color.DARK_BLUE);
						break;
					case '2':
						current.setColor(Color.DARK_GREEN);
						break;
					case '3':
						current.setColor(Color.DARK_AQUA);
						break;
					case '4':
						current.setColor(Color.DARK_RED);
						break;
					case '5':
						current.setColor(Color.DARK_PURPLE);
						break;
					case '6':
						current.setColor(Color.GOLD);
						break;
					case '7':
						current.setColor(Color.GREY);
						break;
					case '8':
						current.setColor(Color.DARK_GREY);
						break;
					case '9':
						current.setColor(Color.BLUE);
						break;
					case 'a':
						current.setColor(Color.GREEN);
						break;
					case 'b':
						current.setColor(Color.AQUA);
						break;
					case 'c':
						current.setColor(Color.RED);
						break;
					case 'd':
						current.setColor(Color.LIGHT_PURPLE);
						break;
					case 'e':
						current.setColor(Color.YELLOW);
						break;
					case 'f':
						current.setColor(Color.WHITE);
						break;
					case 'k':
						current.setObfuscated(true);
						break;
					case 'l':
						current.setBold(true);
						break;
					case 'm':
						current.setStrikethrough(true);
						break;
					case 'n':
						current.setUnderlined(true);
						break;
					case 'o':
						current.setItalic(true);
						break;
					case 'r':
						current.setColor(Color.WHITE);
						current.setObfuscated(false);
						current.setBold(false);
						current.setStrikethrough(false);
						current.setUnderlined(false);
						current.setItalic(false);
						break;
					default:// unknown code
						mcs.append(c).append(c2);
				}
			} else {
				mcs.append(c);
			}
		}
		if (main == null) {
			main = new TextChatMessage(mcs.toString());
		} else {
			current.setText(mcs.toString());
			if (current != main)
				main.addExtra(current);
		}
		return main;
	}
	
	public TextChatMessage() {}
	
	public TextChatMessage(Map<String, Object> map) {
		super(map);
	}
	
	public TextChatMessage(String text) {
		map.put("text", text);
	}
	
	public String getText() {
		return (String) map.get("text");
	}
	
	public void setText(String text) {
		map.put("text", text);
	}
	
	/**
	 * Returns the "legacy string" which represents this TextChatMessage with color and style codes. Each code consists
	 * of 2 characters: the '§' character and another character. The second character definds the color/style to apply.
	 */
	public String toLegacyString() {
		ModifiableCharSequence mcs = new ModifiableCharSequence();
		if (isBold())
			mcs.append("§l");
		if (isObfuscated())
			mcs.append("§k");
		if (isStrikethrough())
			mcs.append("§m");
		if (isUnderlined())
			mcs.append("§n");
		if (isItalic())
			mcs.append("§o");
			
		mcs.append(getText());
		
		List<Object> extras = getExtras();
		if (extras != null) {
			for (Object extra : extras) {
				if (extra instanceof TextChatMessage) {
					TextChatMessage extraMessage = (TextChatMessage) extra;
					if ((extraMessage.isBoldSet() && !extraMessage.isBold() && this.isBold())
							|| (extraMessage.isObfuscatedSet() && !extraMessage.isObfuscated() && this.isObfuscated())
							|| (extraMessage.isStrikethroughSet() && !extraMessage.isStrikethrough() && this.isStrikethrough())
							|| (extraMessage.isUnderlinedSet() && !extraMessage.isUnderlined() && this.isUnderlined())
							|| (extraMessage.isItalicSet() && !extraMessage.isItalic() && this.isItalic())) {
						mcs.append("§r");
					}
					mcs.append(extraMessage.toLegacyString());
				} else {
					mcs.append(extra.toString());
				}
			}
		}
		return mcs.toString();
	}
	
	/**
	 * Returns a string which represents this TextChatMessage with console codes, to use it in the Terminal (console).
	 * Each code consists of a special character sequence. When such a sequence is read by the Terminal (console), it
	 * creates color/style.
	 */
	public String toConsoleString() {
		ModifiableCharSequence mcs = new ModifiableCharSequence();
		if (isBold())
			mcs.append("\u001B[1m");
		// obfuscated does not exist
		if (isStrikethrough())
			mcs.append("\u001B[9m");
		if (isUnderlined())
			mcs.append("\u001B[4m");
		if (isItalic())
			mcs.append("\u001B[3m");
			
		mcs.append(getText());
		
		List<Object> extras = getExtras();
		if (extras != null) {
			for (Object extra : extras) {
				if (extra instanceof TextChatMessage) {
					TextChatMessage extraMessage = (TextChatMessage) extra;
					if ((extraMessage.isBoldSet() && !extraMessage.isBold() && this.isBold())
							|| (extraMessage.isObfuscatedSet() && !extraMessage.isObfuscated() && this.isObfuscated())
							|| (extraMessage.isStrikethroughSet() && !extraMessage.isStrikethrough() && this.isStrikethrough())
							|| (extraMessage.isUnderlinedSet() && !extraMessage.isUnderlined() && this.isUnderlined())
							|| (extraMessage.isItalicSet() && !extraMessage.isItalic() && this.isItalic())) {
						mcs.append("\u001B[0m");
					}
					mcs.append(extraMessage.toConsoleString());
				} else {
					mcs.append(extra.toString());
				}
			}
		}
		mcs.append("\u001B[0m");
		return mcs.toString();
	}
	
}
