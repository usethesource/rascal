/*
 * @(#)SVGStorageFormat.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import org.jhotdraw.framework.Drawing;
import org.jhotdraw.util.StandardStorageFormat;


/**
 * The SVGStorageFormat can save drawings in SVG 1.0.  At this time, it cannot load
 * SVG drawings.
 *
 * TODO: Refactor this and the other storage formats.  There is too much duplication.
 * 
 * @version <$CURRENT_VERSION$>
 * @author mtnygard
 */
public class SVGStorageFormat extends StandardStorageFormat {
	
	/**
	 * Return the file extension recognized by the FileFilter for this
	 * StandardStorageFormat. 
	 *
	 * @return the file extension
	 */
	protected String createFileExtension() {
		return "svg";
	}

	/**
	 * Factory method to create a file description for the file type when displaying the
	 * associated FileFilter.
	 *
	 * @return the file description
	 */
	public String createFileDescription() {
		return "Scalable Vector Graphics (svg)";
	}


	/**
	 * @see org.jhotdraw.util.StorageFormat#isRestoreFormat()
	 */
	public boolean isRestoreFormat() {
		return false;
	}

	/**
	 * @see org.jhotdraw.util.StorageFormat#isStoreFormat()
	 */
	public boolean isStoreFormat() {
		return true;
	}

  /**
	 * Store a Drawing as SVG under a given name.
	 *
	 * @param fileName file name of the Drawing under which it should be stored
	 * @param saveDrawing drawing to be saved
	 * @return file name with correct file extension
   * @see org.jhotdraw.util.StorageFormat#store(java.lang.String, org.jhotdraw.framework.Drawing)
   */
  public String store(String fileName, Drawing saveDrawing) throws IOException {
		// Get a DOMImplementation
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document
		Document document = domImpl.createDocument(null, "svg", null);
	
		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
	
		// Ask the test to render into the SVG Graphics2D implementation
		saveDrawing.draw(svgGenerator);
	
		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		fileName = adjustFileName(fileName);
		FileOutputStream fos = new FileOutputStream(fileName);
		Writer out = new OutputStreamWriter(fos, "UTF-8");
		
		
		svgGenerator.stream(out, true);
		return fileName;
  }

  /**
   * @see org.jhotdraw.util.StorageFormat#restore(java.lang.String)
   */
  public Drawing restore(String fileName) throws IOException {
    throw new IOException("Not implemented");
  }
}
