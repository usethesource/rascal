package org.meta_environment.rascal.interpreter.env;

import org.meta_environment.rascal.ast.PatternWithAction;

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
