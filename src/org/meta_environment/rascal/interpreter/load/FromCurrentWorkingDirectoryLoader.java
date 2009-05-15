package org.meta_environment.rascal.interpreter.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FromCurrentWorkingDirectoryLoader implements IModuleFileLoader {
	public InputStream getSourceInputStream(String name) throws IOException {
		File f = new File(name);
		
		if (!f.exists()) {
			throw new FileNotFoundException("File not found: " + name);
		}
		
		return new FileInputStream(f);
	}
	
	public OutputStream getBinaryOutputStream(String name) throws IOException {
		return new FileOutputStream(name);
	}
}
