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
package org.mcphoton.command;

/**
 * An AdvancedCommand's option.
 *
 * @author ElectronWill
 */
public class Option {

	/**
	 * Option's description.
	 */
	private final String description;

	/**
	 * True if this option is required.
	 */
	private final boolean required;

	/**
	 *
	 * True if this option can take multiple parameters.
	 */
	private final boolean takeParams;

	/**
	 * True if this option needs a parameter.
	 */
	private final boolean needsParam;

	public Option(String description) {
		this.description = description;
		this.required = false;
		this.takeParams = false;
		this.needsParam = false;
	}

	public Option(String description, boolean required) {
		this.description = description;
		this.required = required;
		this.takeParams = false;
		this.needsParam = false;
	}

	public Option(String description, boolean required, boolean needsParam) {
		this.description = description;
		this.required = required;
		this.takeParams = false;
		this.needsParam = needsParam;
	}

	public Option(String description, boolean required, boolean needsParam, boolean multipleParam) {
		this.description = description;
		this.required = required;
		this.needsParam = needsParam;
		this.takeParams = multipleParam;
	}

	/**
	 * Checks if this option needs at least parameter.
	 *
	 * @return
	 */
	public boolean needsParameter() {
		return needsParam;
	}

	/**
	 * Checks if this option can take multiple parameters.
	 *
	 * @return
	 */
	public boolean takesMultipleParameters() {
		return takeParams;
	}

	/**
	 * Checks if this option is required.
	 *
	 * @return true if it is required, false if it is optional.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Returns the option's description.
	 *
	 * @return the description
	 */
	public String getDesctiption() {
		return description;
	}

}
