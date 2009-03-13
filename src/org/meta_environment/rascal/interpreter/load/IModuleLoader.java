package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;

import org.meta_environment.rascal.ast.Module;


public interface IModuleLoader {
	/**
	 * Load a module with the given (qualified) module name. The 
	 * package separator is '::'. If a module can not be loaded by
	 * a loader, it must throw a ModuleLoadException such that the
	 * next loader can be tried.
	 * 
	 * @param name fully qualified name of the module to be loaded
	 * @return an AST of a module
	 * @throws IOException 
	 */
	public Module loadModule(String name) throws IOException;
}
