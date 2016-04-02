package com.electronwill.streams;

import java.io.IOException;

/**
 * Something that can be written to a ProtocolOutputStream.
 *
 * @author TheElectronWill
 */
public interface Writeable {
	
	/**
	 * Writes this object to a ProtocolOutputStream.
	 *
	 * @param out the output stream to write this object to.
	 * @throws java.io.IOException if a problem occurs
	 */
	void writeTo(EasyOutputStream out) throws IOException;
	
}
