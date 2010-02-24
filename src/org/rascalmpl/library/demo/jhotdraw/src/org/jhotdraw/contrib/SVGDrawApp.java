package org.jhotdraw.contrib;

import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.samples.javadraw.JavaDrawApp;
import org.jhotdraw.util.StorageFormatManager;

/**
 * @author mtnygard
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SVGDrawApp extends JavaDrawApp {
	public static void main(String[] args) {
		SVGDrawApp window = new SVGDrawApp();
		window.open();
	}
	
	public SVGDrawApp() {
		super("JHotDraw");
	}

	/**
	 * Factory method which create a new instance of this
	 * application.
	 *
	 * @return	newly created application
	 */
	protected DrawApplication createApplication() {
		return new SVGDrawApp();
	}

	/**
	 * Factory method to create a StorageFormatManager for supported storage formats.
	 * Different applications might want to use different storage formats and can return
	 * their own format manager by overriding this method.
	 * 
	 * TODO: Read storage formats from a config file.
	 */
	public StorageFormatManager createStorageFormatManager() {
		StorageFormatManager storageFormatManager = new StorageFormatManager();
		SVGStorageFormat format = new SVGStorageFormat();
		storageFormatManager.addStorageFormat(format);
		storageFormatManager.setDefaultStorageFormat(format);
		return storageFormatManager;
	}
}
