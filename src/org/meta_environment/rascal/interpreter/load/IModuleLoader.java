package org.meta_environment.rascal.interpreter.load;

import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.errors.ModuleLoadException;

public interface IModuleLoader {
	/**
	 * Load a module with the given (qualified) module name. The 
	 * package separator is '::'. If a module can not be loaded by
	 * a loader, it must throw a ModuleLoadException such that the
	 * next loader can be tried.
	 * 
	 * @param name fully qualified name of the module to be loaded
	 * @return an AST of a module
	 * @throws ModuleLoadException when a module can not be loaded
	 */
	public Module loadModule(String name) throws ModuleLoadException;
}
