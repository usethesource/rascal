package org.rascalmpl.interpreter.env;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.Result;

public class KeywordParameter {

	private final String name;
	private final Type type;
	private final Result<IValue> def;

	public KeywordParameter(String name, Type type, Result<IValue> def){
		this.name = name;
		this.type = type;
		this.def = def;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Result<IValue> getDefault() {
		return def;
	}
	
	public IValue getValue(){
		return def.getValue();
	}
}
