/*
 * @(#)StandardStorageFormat.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import org.jhotdraw.framework.Drawing;

/**
 * A StandardStorageFormat is an internal file format to store and restore
 * Drawings. It uses its own descriptive syntax ands write classes and attributes
 * as plain text in a text file. The StandardStorageFormat has the file extension
 * "draw" (e.g. my_picasso.draw).
 *
 * @author Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class StandardStorageFormat implements StorageFormat {

	/**
	 * FileFilter for a javax.swing.JFileChooser which recognizes files with the
	 * extension "draw"
	 */
	private FileFilter myFileFilter;

	/**
	 * File extension
	 */
	private String myFileExtension;

	/**
	 * Description of the file type when displaying the FileFilter
	 */
	private String myFileDescription;

	/**
	 * Create a StandardStorageFormat for storing and restoring Drawings.
	 */
	public StandardStorageFormat() {
		setFileExtension(createFileExtension());
		setFileDescription(createFileDescription());
		setFileFilter(createFileFilter());
	}

	/**
	 * Factory method to create the file extension recognized by the FileFilter for this
	 * StandardStorageFormat. The StandardStorageFormat has the file extension "draw"
	 * (e.g. my_picasso.draw).
	 *
	 * @return new file extension
	 */
	protected String createFileExtension() {
		return myFileExtension = "draw";
	}

	/**
	 * Set the file extension for the storage format
	 *
	 * @param newFileExtension extension
	 */
	public void setFileExtension(String newFileExtension) {
		myFileExtension = newFileExtension;
	}

	/**
	 * Return the file extension for the storage format
	 *
	 * @return file extension
	 */
	public String getFileExtension() {
		return myFileExtension;
	}

	/**
	 * Factory method to create a file description for the file type when displaying the
	 * associated FileFilter.
	 *
	 * @return new file description
	 */
	public String createFileDescription() {
		return "Internal Format (" + getFileExtension() + ")";
	}

	/**
	 * Set the file description for the file type of the storage format
	 *
	 * @param newFileDescription description of the file type
	 */
	public void setFileDescription(String newFileDescription) {
		myFileDescription = newFileDescription;
	}

	/**
	 * Return the file description for the file type of the storage format
	 *
	 * @return description of the file type
	 */
	public String getFileDescription() {
		return myFileDescription;
	}

	/**
	 * Factory method to create a FileFilter that accepts file with the appropriate
	 * file exention used by a javax.swing.JFileChooser. Subclasses can override this
	 * method to provide their own file filters.
	 *
	 * @return FileFilter for this StorageFormat
	 */
	protected FileFilter createFileFilter() {
		return new FileFilter() {
			public boolean accept(File checkFile) {
				// still display directories for navigation
				if (checkFile.isDirectory()) {
					return true;
				}
				else {
					return checkFile.getName().endsWith("." + getFileExtension());
				}
			}

			public String getDescription() {
				return getFileDescription();
			}
		};
	}

	/**
	 * Set the FileFilter used to identify Drawing files with the correct file
	 * extension for this StorageFormat.
	 *
	 * @param newFileFilter FileFilter for this StorageFormat
	 */
	public void setFileFilter(FileFilter newFileFilter) {
		myFileFilter = newFileFilter;
	}

	/**
	 * Return the FileFilter used to identify Drawing files with the correct file
	 * extension for this StorageFormat.
	 *
	 * @return FileFilter for this StorageFormat
	 */
	public FileFilter getFileFilter() {
		return myFileFilter;
	}

	/**
	 * @see org.jhotdraw.util.StorageFormat#isRestoreFormat()
	 */
	public boolean isRestoreFormat() {
		return true;
	}

	/**
	 * @see org.jhotdraw.util.StorageFormat#isStoreFormat()
	 */
	public boolean isStoreFormat() {
		return true;
	}

	/**
	 * Store a Drawing under a given name. If the file name does not have the correct
	 * file extension, then the file extension is added.
	 *
	 * @param fileName file name of the Drawing under which it should be stored
	 * @param saveDrawing drawing to be saved
	 * @return file name with correct file extension
	 */
	public String store(String fileName, Drawing saveDrawing) throws IOException {
		FileOutputStream stream = new FileOutputStream(adjustFileName(fileName));
		StorableOutput output = new StorableOutput(stream);
		output.writeStorable(saveDrawing);
		output.close();
		return adjustFileName(fileName);
	}

	/**
	 * Restore a Drawing from a file with a given name.
	 *
	 * @param fileName of the file in which the Drawing has been saved
	 * @return restored Drawing
	 */
	public Drawing restore(String fileName) throws IOException {
		if (!hasCorrectFileExtension(fileName)) {
			return null;
		}
		else {
			FileInputStream stream = new FileInputStream(fileName);
			StorableInput input = new StorableInput(stream);
			return (Drawing)input.readStorable();
		}
	}

	/**
	 * Test, whether two StorageFormats are the same. They are the same if they both support the
	 * same file extension.
	 *
	 * @return true, if both StorageFormats have the same file extension, false otherwise
	 */
	public boolean equals(Object compareObject) {
		if (compareObject instanceof StandardStorageFormat) {
			return getFileExtension().equals(((StandardStorageFormat)compareObject).getFileExtension());
		}
		else {
			return false;
		}
	}

	/**
	 * Adjust a file name to have the correct file extension.
	 *
	 * @param testFileName file name to be tested for a correct file extension
	 * @return testFileName + file extension if necessary
	 */
	protected String adjustFileName(String testFileName) {
		if (!hasCorrectFileExtension(testFileName)) {
			return testFileName + "." + getFileExtension();
		}
		else {
			return testFileName;
		}
	}

	/**
	 * Test whether the file name has the correct file extension
	 *
	 * @return true, if the file has the correct extension, false otherwise
	 */
	protected boolean hasCorrectFileExtension(String testFileName) {
		return testFileName.endsWith("." + getFileExtension());
	}
}