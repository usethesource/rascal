package org.meta_environment.rascal.interpreter.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FromCurrentWorkingDirectoryLoader extends AbstractModuleLoader {
	@Override
	protected InputStream getInputStream(String name) throws IOException {
		File f = new File(name);
		
		if (!f.exists()) {
			throw new FileNotFoundException("File not found: " + name);
		}
		
		return new FileInputStream(f);
	}
	
	@Override
	protected OutputStream getOutputStream(String name) throws IOException {
		return new FileOutputStream(name);
	}
}
