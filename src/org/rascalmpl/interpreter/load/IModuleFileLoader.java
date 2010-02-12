package org.rascalmpl.interpreter.load;

import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;

public interface IModuleFileLoader{
	boolean fileExists(String filename);
	InputStream getInputStream(String filename);
	boolean supportsLoadingBinaries();
	boolean tryWriteBinary(String filename, String binaryName, IConstructor tree);
}
