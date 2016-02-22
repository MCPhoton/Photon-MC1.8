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

import java.util.Arrays;
import java.util.List;

/**
 * Contains parsed options.
 *
 * @author ElectronWill
 */
public final class ParsedOption extends Option {

	private String param;

	private List<String> params;

	public ParsedOption(Option o) {
		super(o.getDesctiption(), o.isRequired(), o.needsParameter(), o.takesMultipleParameters());
		this.param = null;
		this.params = null;
	}

	public ParsedOption(Option o, String param) {
		super(o.getDesctiption(), o.isRequired(), o.needsParameter(), o.takesMultipleParameters());
		this.param = param;
	}

	public ParsedOption(Option o, List<String> params) {
		super(o.getDesctiption(), o.isRequired(), o.needsParameter(), o.takesMultipleParameters());
		this.params = params;
	}

	public ParsedOption(Option o, String... params) {
		super(o.getDesctiption(), o.isRequired(), o.needsParameter(), o.takesMultipleParameters());
		this.params = Arrays.asList(params);
	}

	public boolean hasMultipleParameters() {
		return params != null;
	}

	public boolean hasSingleParameter() {
		return param != null;
	}

	public String getParameter() {
		return param;
	}

	public List<String> getParametersList() {
		return params;
	}

}
