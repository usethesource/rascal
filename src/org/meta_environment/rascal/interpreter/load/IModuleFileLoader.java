package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;
import java.io.InputStream;

public interface IModuleFileLoader {
	public InputStream getInputStream(String filename) throws IOException;
}
