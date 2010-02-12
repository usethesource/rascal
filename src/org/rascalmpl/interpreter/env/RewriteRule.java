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
