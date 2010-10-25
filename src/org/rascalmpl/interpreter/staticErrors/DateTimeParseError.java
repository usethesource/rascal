package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class DateTimeParseError extends StaticError {

	private static final long serialVersionUID = 837136613054853491L;

	public DateTimeParseError(String parseString, ISourceLocation loc) {
		super("Unable to parse datetime string: " + parseString, loc);
	}
	
}
