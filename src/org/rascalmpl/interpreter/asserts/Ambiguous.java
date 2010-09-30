package org.rascalmpl.interpreter.asserts;

import java.util.regex.Matcher;

import org.eclipse.imp.pdb.facts.IConstructor;


public final class Ambiguous extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public Ambiguous(IConstructor tree) {
		super("Unexpected ambiguity: " + getValueString(tree));
	}

	private static String getValueString(IConstructor tree) {
		String val = tree.toString();
		val = val.replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		val = val.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		val = val.replaceAll("<", Matcher.quoteReplacement("\\<"));
		val = val.replaceAll(">", Matcher.quoteReplacement("\\>"));
		return "\"" + val + "\"";
	}
}
