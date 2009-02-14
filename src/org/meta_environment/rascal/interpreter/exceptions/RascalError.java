package org.meta_environment.rascal.interpreter.exceptions;

import org.eclipse.imp.pdb.facts.ISourceRange;
import org.meta_environment.rascal.ast.AbstractAST;

public class RascalError extends RuntimeException {
	private static final long serialVersionUID = 3715516757858886671L;

	private final ISourceRange range;
	private final String path;

	public RascalError(String message) {
		super(message);
		this.range = null;
		this.path = null;
	}

	public RascalError(String message, AbstractAST node) {
		super(message);
		range = node.getSourceRange();
		path = node.getSourcePath();
	}

	public RascalError(String message, Throwable cause) {
		super(message, cause);
		range = null;
		path = null;
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();

		if (hasRange()) {
			if (range.getStartLine() != range.getEndLine()) {
				message += " from line " + range.getStartLine() + ", column "
						+ range.getStartColumn() + " to line "
						+ range.getEndLine() + "," + " column "
						+ range.getEndColumn();
			} else {
				message += " at line " + range.getStartLine() + ", column "
						+ range.getStartColumn() + " to "
						+ range.getEndColumn();
			}
		}

		if (hasPath()) {
			message += " in " + path;
		}

		return message;
	}

	public boolean hasRange() {
		return range != null;
	}

	public boolean hasPath() {
		return path != null && !path.equals("-");

	}

}
