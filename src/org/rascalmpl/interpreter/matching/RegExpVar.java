package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class RegExpVar implements IVarPattern {
	private String name;
	private static final Type stringType = TypeFactory.getInstance().stringType();

	public RegExpVar(String name){
		this.name = name;
		
	}
	@Override
	public boolean isVarIntroducing() {
		return true;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Type getType() {
		return stringType;
	}

}
