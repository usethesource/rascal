package org.meta_environment.rascal.interpreter.env;

import org.meta_environment.rascal.ast.PatternAction;

public class RewriteRule {
	private Environment env;
	private PatternAction rule;

	public RewriteRule(PatternAction rule, Environment env) {
		this.rule = rule;
		this.env = env;
	}
	
	public PatternAction getRule() {
		return rule;
	}
	
	public Environment getEnvironment() {
		return env;
	}
}
