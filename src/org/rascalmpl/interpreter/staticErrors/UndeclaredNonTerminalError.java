package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class UndeclaredNonTerminalError extends StaticError {
	private static final long serialVersionUID = 4249563329694796160L;

	public UndeclaredNonTerminalError(String name, ISourceLocation loc) {
		super("Undeclared non-terminal: " + name, loc);
	}

	public UndeclaredNonTerminalError(String name, ISourceLocation loc, Throwable cause) {
		super("Undeclared non-terminal: " + name, loc, cause);
	}
}
