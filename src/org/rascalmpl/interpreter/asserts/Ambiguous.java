package org.rascalmpl.interpreter.asserts;


public final class Ambiguous extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public Ambiguous(String message) {
		super("Unexpected ambiguity: " + message);
	}
}
