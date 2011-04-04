/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.env;

import org.rascalmpl.ast.PatternWithAction;

public class RewriteRule {
	private Environment env;
	private PatternWithAction rule;

	public RewriteRule(PatternWithAction rule, Environment env) {
		this.rule = rule;
		this.env = env;
	}
	
	public PatternWithAction getRule() {
		return rule;
	}
	
	public Environment getEnvironment() {
		return env;
	}
}
