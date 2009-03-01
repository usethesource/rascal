package org.meta_environment.rascal.interpreter.load;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FromCurrentWorkingDirectoryLoader extends AbstractModuleLoader {
	@Override
	protected InputStream getStream(String name) throws IOException {
		return new FileInputStream(name);
	}
}
