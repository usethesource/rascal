package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IModuleFileLoader {
	public InputStream getSourceInputStream(String filename) throws IOException;
	public OutputStream getBinaryOutputStream(String filename) throws IOException;
}
