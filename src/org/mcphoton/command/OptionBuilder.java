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
 * Utility class for easily building complex (or simple, it's as you want ^^) Option objects.
 *
 * @author ElectronWill
 */
public final class OptionBuilder {

	/**
	 * Option's description.
	 */
	private String description;

	/**
	 * True if this option is required.
	 */
	private boolean required;

	/**
	 *
	 * True if this option can take multiple parameters.
	 */
	private boolean takeParams;

	/**
	 * True if this option needs a parameter.
	 */
	private boolean needsParam;

	/**
	 * Sets the option's description.
	 *
	 * @param description
	 * @return
	 */
	public OptionBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Makes the option required.
	 *
	 * @return
	 */
	public OptionBuilder required() {
		this.required = true;
		return this;
	}

	/**
	 * Sets if the option is required or not.
	 *
	 * @param required
	 * @return
	 */
	public OptionBuilder required(boolean required) {
		this.required = required;
		return this;
	}

	/**
	 * Makes the option need only one parameter.
	 *
	 * @return
	 */
	public OptionBuilder withOneParameter() {
		this.needsParam = true;
		this.takeParams = false;
		return this;
	}

	/**
	 * Makes the option able to takes multiple parameters. It will need to have at least one
	 * parameter.
	 *
	 * @return
	 */
	public OptionBuilder withMultipleParameters() {
		this.takeParams = true;
		this.needsParam = true;
		return this;
	}

	/**
	 * Clears this OptionBuilder: removes the description, the need for a parameter, the ability to
	 * handle multiple parameters and the required status. A subsequent {@link #build()} call would
	 * not work because of the lack of an option's description.
	 *
	 * @return
	 */
	public OptionBuilder clear() {
		description = null;
		needsParam = false;
		required = false;
		takeParams = false;
		return this;
	}

	/**
	 * Builds an Option object with the current settings.
	 *
	 * @return
	 */
	public Option build() {
		if (description == null) {
			throw new NullPointerException("OPTION BUILDER ERROR - Option's description cannot be null!");
		}
		return new Option(description, required, needsParam, takeParams);
	}

}
