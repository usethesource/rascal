package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;

public class ModuleLoadError extends StaticError {
	public ModuleLoadError(String name, String cause, AbstractAST ast) {
		super("Could not load module " + name + (cause != null ? (": " + cause) : ""), ast);
	}

	public ModuleLoadError(String name, String cause, ISourceLocation errorLocation) {
		super("Could not load module " + name + (cause != null ? (": " + cause) : ""), errorLocation);
	}

	private static final long serialVersionUID = -2382848293435609203L;

}
