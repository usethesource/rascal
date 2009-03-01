package org.meta_environment.rascal.interpreter.load;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FromResourceLoader extends AbstractModuleLoader {

	private String sourceFolder;
	private Class<?> clazz;

	/**
	 * Creates a loader that will find modules in the resources
	 * of the given class
	 * 
	 * @param clazz
	 */
	public FromResourceLoader(Class<?> clazz) {
		this(clazz, "");
	}
	
	/**
	 * Creates a loader that will find modules in the resources
	 * of the given class. The given sourceFolder is where the search
	 * for modules in the class's resources will start.
	 * 
	 * @param clazz
	 */
	public FromResourceLoader(Class<?> clazz, String sourceFolder) {
		this.clazz = clazz;
		
		if (!sourceFolder.startsWith("/")) {
			sourceFolder = "/" + sourceFolder;
		}
		if (!sourceFolder.endsWith("/")) {
			sourceFolder = sourceFolder + "/";
		}
		this.sourceFolder = sourceFolder;
	}

	@Override
	protected InputStream getStream(String name) throws IOException {
		URL url = clazz.getResource(sourceFolder + name);

		if (url == null) {
			throw new FileNotFoundException(name);
		}
		
		return url.openStream();
	}
}
