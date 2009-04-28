package org.meta_environment.rascal.interpreter.env;

import org.meta_environment.rascal.ast.Rule;

public class RewriteRule {
	private Environment env;
	private Rule rule;

	public RewriteRule(Rule rule, Environment env) {
		this.rule = rule;
		this.env = env;
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public Environment getEnvironment() {
		return env;
	}
}
