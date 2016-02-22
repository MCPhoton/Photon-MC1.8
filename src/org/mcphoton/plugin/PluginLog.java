/*
 * Copyright (C) 2015 ElectronWill
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcphoton.plugin;

import com.electronwill.text.ModifiableCharSequence;
import org.mcphoton.core.Photon;
import org.mcphoton.util.Log;

/**
 *
 * @author ElectronWill
 */
public final class PluginLog extends Log {

	/**
	 * The plugin's name.
	 */
	private final String plugin;

	public PluginLog(String plugin) {
		this.plugin = plugin;
	}

	@Override
	public void info(CharSequence msg) {
		Photon.log.formatted("(--)", plugin, msg);
	}

	@Override
	public void warning(CharSequence msg) {
		Photon.log.formatted("(WW)", plugin, msg);
	}

	@Override
	public void debug(CharSequence msg) {
		Photon.log.formatted("(++)", plugin, msg);
	}

	@Override
	public void error(CharSequence msg) {
		Photon.log.formatted("(!!)", plugin, msg);
	}

	@Override
	public void error(Throwable t) {
		Photon.log.errorFrom(t, plugin);
	}

	@Override
	public void error(Throwable t, CharSequence msg) {
		Photon.log.errorFrom(t, plugin, msg);
	}

	@Override
	public void raw(ModifiableCharSequence msg) {
		Photon.log.raw(msg);
	}

}
