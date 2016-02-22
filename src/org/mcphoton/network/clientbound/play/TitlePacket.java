/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.network.clientbound.play;

import java.io.IOException;
import org.mcphoton.messaging.ChatMessage;
import org.mcphoton.network.SendablePacket;
import com.electronwill.streams.EasyOutputStream;
import com.electronwill.streams.Writeable;

/**
 *
 * @author ElectronWill
 */
public final class TitlePacket extends SendablePacket {
	
	public Action action;
	
	public TitlePacket(Action action) {
		this.action = action;
	}
	
	@Override
	public void writeTo(com.electronwill.streams.EasyOutputStream dest) throws IOException {
		action.writeTo(dest);
	}
	
	@Override
	public int id() {
		return 0x45;
	}
	
	@Override
	public int maxDataSize() {
		return Integer.MAX_VALUE;
	}
	
	public static abstract class Action implements Writeable {
		
		@Override
		public abstract void writeTo(EasyOutputStream out) throws IOException;
	}
	
	public static final class SetTitleAction extends Action {
		
		public ChatMessage title;
		
		public SetTitleAction(ChatMessage title) {
			this.title = title;
		}
		
		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(0);
			dest.writeString(title.toString());
		}
		
	}
	
	public static final class SetSubtitleAction extends Action {
		
		public ChatMessage subTitle;
		
		public SetSubtitleAction(ChatMessage subTitle) {
			this.subTitle = subTitle;
		}
		
		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(1);
			dest.writeString(subTitle.toString());
		}
		
	}
	
	public static final class SetTimesAndDisplayAction extends Action {
		
		public int fadeInTime, stayTime, fadeOutTime;
		
		public SetTimesAndDisplayAction(int fadeInTime, int stayTime, int fadeOutTime) {
			this.fadeInTime = fadeInTime;
			this.stayTime = stayTime;
			this.fadeOutTime = fadeOutTime;
		}
		
		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(2);
			dest.writeInt(fadeInTime);
			dest.writeInt(stayTime);
			dest.writeInt(fadeOutTime);
		}
	}
	
	public static final class HideAction extends Action {
		
		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(3);
		}
		
	}
	
	public static final class ResetAction extends Action {
		
		@Override
		public void writeTo(EasyOutputStream dest) throws IOException {
			dest.writeVarInt(4);
		}
		
	}
	
}
