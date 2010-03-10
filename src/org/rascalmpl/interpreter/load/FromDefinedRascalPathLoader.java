package org.rascalmpl.interpreter.load;

import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;

public class FromDefinedRascalPathLoader implements IModuleFileLoader {
	private List<FromDirectoryLoader> loaders;
	
	public FromDefinedRascalPathLoader() {
		String property = System.getProperty("rascal.path");
		
		if (property != null) {
			loaders = new LinkedList<FromDirectoryLoader>();

			for (String path : property.split(":")) {
				loaders.add(new FromDirectoryLoader(path));
			}
		}
	}

	public boolean fileExists(String filename) {
		if (loaders != null) {
			for (FromDirectoryLoader loader : loaders) {
				if (loader.fileExists(filename)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public InputStream getInputStream(String filename) {
		if (loaders != null) {
			for (FromDirectoryLoader loader : loaders) {
				if (loader.fileExists(filename)) {
					return loader.getInputStream(filename);
				}
			}
		}
		
		return null;
	}

	public boolean supportsLoadingBinaries() {
		return true;
	}

	public boolean tryWriteBinary(String filename, String binaryName,
			IConstructor tree) {
		if (loaders != null) {
			for (FromDirectoryLoader loader : loaders) {
				if (loader.fileExists(filename)) {
					return loader.tryWriteBinary(filename, binaryName, tree);
				}
			}
		}
		
		return false;
	}

	public URI getURI(String filename) {
		for (FromDirectoryLoader loader : loaders) {
			if (loader.fileExists(filename)) {
				return loader.getURI(filename);
			}
		}
		return null;
	}

}
