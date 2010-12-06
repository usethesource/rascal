package org.rascalmpl.interpreter;

import org.rascalmpl.values.uptr.Factory;

public class BasicTypeEvaluator extends org.rascalmpl.ast.NullASTVisitor<org.eclipse.imp.pdb.facts.type.Type> {
	private final static org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	private final org.eclipse.imp.pdb.facts.type.Type typeArgument;
	private final org.eclipse.imp.pdb.facts.IValue[] valueArguments; // for adt, constructor and non-terminal representations
	private final org.rascalmpl.interpreter.env.Environment env;
	
	public BasicTypeEvaluator(org.rascalmpl.interpreter.env.Environment env, org.eclipse.imp.pdb.facts.type.Type argumentTypes, org.eclipse.imp.pdb.facts.IValue[] valueArguments) {
		this.env = env;
		this.typeArgument = argumentTypes;
		this.valueArguments = valueArguments;
	}
	
	public org.eclipse.imp.pdb.facts.type.Type __getTypeArgument() {
		return typeArgument;
	}

	public static org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	public org.rascalmpl.interpreter.env.Environment __getEnv() {
		return env;
	}

	public org.eclipse.imp.pdb.facts.IValue[] __getValueArguments() {
		return valueArguments;
	}
}
