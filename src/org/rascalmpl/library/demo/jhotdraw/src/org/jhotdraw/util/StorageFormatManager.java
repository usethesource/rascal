/*
 * @(#)StorageFormatManager.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
 
package org.jhotdraw.util;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.util.List;
import java.util.Iterator;

/**
 * The StorageFormatManager is a contains StorageFormats.
 * It is not a Singleton because it could be necessary to deal with different
 * format managers, e.g. one for importing Drawings, one for exporting Drawings.
 * If one StorageFormat matches the file extension of the Drawing file, then this
 * StorageFormat can be used to store or restore the Drawing.
 *
 * @see StorageFormat
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class StorageFormatManager {

	/**
	 * List containing all registered storage formats
	 */
	private List myStorageFormats;
	
	/**
	 * Default storage format that should be selected in a javax.swing.JFileChooser
	 */
	private StorageFormat myDefaultStorageFormat;
	
	/**
	 * Create a new StorageFormatManager.
	 */
	public StorageFormatManager() {
		myStorageFormats = CollectionsFactory.current().createList();
	}
	
	/**
	 * Add a StorageFormat that should be supported by this StorageFormatManager.
	 *
	 * @param newStorageFormat new StorageFormat to be supported
	 */
	public void addStorageFormat(StorageFormat newStorageFormat) {
		myStorageFormats.add(newStorageFormat);
	}

	/**
	 * Remove a StorageFormat that should no longer be supported by this StorageFormatManager.
	 * The StorageFormat is excluded in when search for a StorageFormat.
	 *
	 * @param oldStorageFormat old StorageFormat no longer to be supported
	 */
	public void removeStorageFormat(StorageFormat oldStorageFormat) {
		myStorageFormats.remove(oldStorageFormat);
	}
	
	/**
	 * Test, whether a StorageFormat is supported by this StorageFormat
	 */
	public boolean containsStorageFormat(StorageFormat checkStorageFormat){
		return myStorageFormats.contains(checkStorageFormat);
	}
	
	/**
	 * Set a StorageFormat as the default storage format which is selected in a
	 * javax.swing.JFileChooser. The default storage format must be already
	 * added with addStorageFormat. Setting the default storage format to null
	 * does not automatically remove the StorageFormat from the list of
	 * supported StorageFormats.
	 *
	 * @param newDefaultStorageFormat StorageFormat that should be selected in a JFileChooser
	 */
	public void setDefaultStorageFormat(StorageFormat newDefaultStorageFormat) {
		myDefaultStorageFormat = newDefaultStorageFormat;
	}
	
	/**
	 * Return the StorageFormat which is used as selected file format in a javax.swing.JFileChooser
	 *
	 * @return default storage format
	 */
	public StorageFormat getDefaultStorageFormat() {
		return myDefaultStorageFormat;
	}
	
	/**
	 * Register all FileFilters supported by StorageFormats
	 *
	 * @param fileChooser javax.swing.JFileChooser to which FileFilters are added
	 */
	public void registerFileFilters(JFileChooser fileChooser) {
		if (fileChooser.getDialogType() == JFileChooser.OPEN_DIALOG) {
			// behavior for open dialogs
			StorageFormat sf;
			for (Iterator e = myStorageFormats.iterator(); e.hasNext();) {
				sf = (StorageFormat) e.next();
				if (sf.isRestoreFormat()) {
					fileChooser.addChoosableFileFilter(sf.getFileFilter());
				}
			}

		// set a current activated file filter if a default storage Format has been defined
			sf = getDefaultStorageFormat();
			if (sf != null && sf.isRestoreFormat()) {
				fileChooser.setFileFilter(sf.getFileFilter());
			}
		}
		else if (fileChooser.getDialogType() == JFileChooser.SAVE_DIALOG) {
			// behavior for save dialogs
			StorageFormat sf;
			for (Iterator e = myStorageFormats.iterator(); e.hasNext();) {
				sf = (StorageFormat) e.next();
				if (sf.isStoreFormat()) {
					fileChooser.addChoosableFileFilter(sf.getFileFilter());
				}
			}

			// set a current activated file filter if a default storage Format has been defined
			sf = getDefaultStorageFormat();
			if (sf != null && sf.isStoreFormat()) {
				fileChooser.setFileFilter(sf.getFileFilter());
			}
		}
		else {
			// old behavior
			StorageFormat sf;
			for (Iterator e = myStorageFormats.iterator(); e.hasNext();) {
				sf = (StorageFormat) e.next();
				fileChooser.addChoosableFileFilter(sf.getFileFilter());
			}

			// set a current activated file filter if a default storage Format has been defined
			sf = getDefaultStorageFormat();
			if (sf != null) {
				fileChooser.setFileFilter(sf.getFileFilter());
			}
		}
	}

	/**
	 * Find a StorageFormat that can be used according to a FileFilter to store a Drawing
	 * in a file or restore it from a file respectively.
	 *
	 * @param findFileFilter FileFilter used to identify a StorageFormat
	 * @return StorageFormat, if a matching file extension could be found, false otherwise
	 */
	public StorageFormat findStorageFormat(FileFilter findFileFilter) {
		Iterator formatsIterator = myStorageFormats.iterator();
		StorageFormat currentStorageFormat = null;
		while (formatsIterator.hasNext()) {
			currentStorageFormat = (StorageFormat)formatsIterator.next();
			if (currentStorageFormat.getFileFilter().equals(findFileFilter)) {
				return currentStorageFormat;
			}
		}
		
		return null;
	}

	/**
	 * Find a StorageFormat that can be used according to a file object to store a
	 * Drawing in a file or restore it from a file respectively.
	 *
	 * @param file a File object to be matched
	 * @return StorageFormat, if a matching file extension could be found, <code>null</code>
	 * otherwise
	 */
	public StorageFormat findStorageFormat(File file) {
		Iterator formatsIterator = myStorageFormats.iterator();
		StorageFormat currentStorageFormat;
		while (formatsIterator.hasNext()) {
			currentStorageFormat = (StorageFormat) formatsIterator.next();
			if (currentStorageFormat.getFileFilter().accept(file)) {
				return currentStorageFormat;
			}
		}
		return null;
	}
}