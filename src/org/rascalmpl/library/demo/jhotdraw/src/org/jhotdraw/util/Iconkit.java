/*
 * @(#)Iconkit.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

/**
 * The Iconkit class supports the sharing of images. It maintains
 * a map of image names and their corresponding images.
 *
 * Iconkit also supports to load a collection of images in
 * synchronized way.
 * The resolution of a path name to an image is delegated to the DrawingEditor.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld031.htm>Singleton</a></b><br>
 * The Iconkit is a singleton.
 * <hr>
 *
 * @version <$CURRENT_VERSION$>
 */
public class Iconkit {
	private Map                 fMap;
	private List                fRegisteredImages;
	private Component           fComponent;
	private final static int    ID = 123;
	private static Iconkit      fgIconkit = null;
	private static boolean      fgDebug = false;

	/**
	 * Constructs an Iconkit that uses the given editor to
	 * resolve image path names.
	 */
	public Iconkit(Component component) {
		fMap = new Hashtable(53);
		fRegisteredImages = CollectionsFactory.current().createList(10);
		fComponent = component;
		fgIconkit = this;
	}

	/**
	 * Gets the single instance
	 */
	public static Iconkit instance() {
		return fgIconkit;
	}

	/**
	 * Loads all registered images.
	 * @see #registerImage
	 */
	public void loadRegisteredImages(Component component) {
		if (fRegisteredImages.size() == 0)
			return;

		MediaTracker tracker = new MediaTracker(component);

		// register images with MediaTracker
		Iterator iter = fRegisteredImages.iterator();
		while (iter.hasNext()) {
			String fileName = (String)iter.next();
			if (basicGetImage(fileName) == null) {
				tracker.addImage(loadImage(fileName), ID);
			}
		}
		fRegisteredImages.clear();

		// block until all images are loaded
		try {
			tracker.waitForAll();
		}
		catch (Exception e) {
			// ignore: do nothing
		}
	}

	/**
	 * Registers an image that is then loaded together with
	 * the other registered images by loadRegisteredImages.
	 * @see #loadRegisteredImages
	 */
	public void registerImage(String fileName) {
		fRegisteredImages.add(fileName);
	}

	/**
	 * Registers and loads an image.
	 */
	public Image registerAndLoadImage(Component component, String fileName) {
		registerImage(fileName);
		loadRegisteredImages(component);
		return getImage(fileName);
	}

	/**
	 * Loads an image with the given name.
	 */
	public Image loadImage(String filename) {
		if (fMap.containsKey(filename)) {
			return (Image) fMap.get(filename);
		}
		Image image = loadImageResource(filename);
		if (image != null) {
			fMap.put(filename, image);
		}
		return image;
	}

	public Image loadImage(String filename, boolean waitForLoad) {
		Image image = loadImage(filename);
		if (image!=null && waitForLoad) {
			ImageIcon icon = new ImageIcon(image);
			image = icon.getImage(); //this forces the wait to happen
		}
		return image;
	}

	public Image loadImageResource(String resourcename) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		try {
			URL url = getClass().getResource(resourcename);
			if (fgDebug) {
				System.out.println(resourcename);
			}
			return toolkit.createImage((ImageProducer) url.getContent());
		}
		catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets the image with the given name. If the image
	 * can't be found it tries it again after loading
	 * all the registered images.
	 */
	public Image getImage(String filename) {
		Image image = basicGetImage(filename);
		if (image != null) {
			return image;
		}
		// load registered images and try again
		loadRegisteredImages(fComponent);
		// try again
		return basicGetImage(filename);
	}

	private Image basicGetImage(String filename) {
		if (fMap.containsKey(filename)) {
			return (Image) fMap.get(filename);
		}
		return null;
	}
}
