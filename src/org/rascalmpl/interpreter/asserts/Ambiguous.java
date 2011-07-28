/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.asserts;

import java.util.regex.Matcher;

import org.eclipse.imp.pdb.facts.IConstructor;


public final class Ambiguous extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public Ambiguous(IConstructor tree) {
		super("Unexpected ambiguity at " + tree.getAnnotation("loc") + ": " + getValueString(tree));
		printStackTrace();
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
