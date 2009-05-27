package org.meta_environment.rascal.interpreter.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FromCurrentWorkingDirectoryLoader implements IModuleFileLoader {
	public InputStream getInputStream(String name) throws IOException {
		File f = new File(name);
		
		if (!f.exists()) {
			throw new FileNotFoundException("File not found: " + name);
		}
		
		return new FileInputStream(f);
	}
}
