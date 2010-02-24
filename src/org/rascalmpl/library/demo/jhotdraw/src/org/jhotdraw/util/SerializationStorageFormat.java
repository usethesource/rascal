/*
 * @(#)SerializationStorageFormat.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.io.*;
import org.jhotdraw.framework.Drawing;

/**
 * A SerializationStorageFormat is a straight-forward file format to store and restore
 * Drawings. It uses Java's serialization mechanism to store Drawings. The SerializationStorageFormat
 * has the file extension "ser" (e.g. my_picasso.ser).
 *
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public class SerializationStorageFormat extends StandardStorageFormat {

	/**
	 * Create a SerialzationStorageFormat for storing and restoring Drawings.
	 */
	public SerializationStorageFormat() {
		super();
	}

	/**
	 * Factory method to create the file extension recognized by the FileFilter for this
	 * SerializationStorageFormat. The SerializationStorageFormat has the file extension "ser"
	 * (e.g. my_picasso.ser).
	 *
	 * @return new file extension
	 */
	protected String createFileExtension() {
		return "ser";
	}

	/**
	 * Factory method to create a file description for the file type when displaying the
	 * associated FileFilter.
	 *
	 * @return new file description
	 */
	public String createFileDescription() {
		return "Serialization (" + getFileExtension() + ")";
	}
	
	/**
	 * Store a Drawing under a given name. The name should be valid with regard to the FileFilter
	 * that means, it should already contain the appropriate file extension.
	 *
	 * @param fileName file name of the Drawing under which it should be stored
	 * @param saveDrawing drawing to be saved
	 */
	public String store(String fileName, Drawing saveDrawing) throws IOException {
		FileOutputStream stream = new FileOutputStream(adjustFileName(fileName));
		ObjectOutput output = new ObjectOutputStream(stream);
		output.writeObject(saveDrawing);
		output.close();
		return adjustFileName(fileName);
	}

	/**
	 * Restore a Drawing from a file with a given name. The name must be should with regard to the
	 * FileFilter that means, it should have the appropriate file extension.
	 *
	 * @param fileName of the file in which the Drawing has been saved
	 * @return restored Drawing
	 */
	public Drawing restore(String fileName) throws IOException {
		try {
			FileInputStream stream = new FileInputStream(fileName);
			ObjectInput input = new ObjectInputStream(stream);
			return (Drawing)input.readObject();
		}
		catch (ClassNotFoundException exception) {
			throw new IOException("Could not restore drawing '" + fileName +"': class not found!");
		}
	}
}