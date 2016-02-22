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
package org.mcphoton.item;

import org.mcphoton.world.BlockInfos;

/**
 * Interface for items that efficiently break some type(s) of blocks.
 *
 * @author ElectronWill
 */
public interface Tool {

	/**
	 * Returns how much time (in milliseconds) is needed to break this block with this tool.
	 *
	 *
	 * @param block the block hurt by this tool
	 * @return time required, in milliseconds.
	 */
	int getBreakTime(BlockInfos block);

}
