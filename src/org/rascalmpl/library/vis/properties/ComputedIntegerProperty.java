package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.FigurePApplet;

public class ComputedIntegerProperty implements IIntegerPropertyValue {
	Property property;
	String tname;
	IValue fun;
	Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	int value;

	public ComputedIntegerProperty(Property prop, IValue fun, FigurePApplet fpa){
		this.property = prop;
		this.fun = fun;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public synchronized int getValue() {
		Result<IValue> res;
		if(fun instanceof RascalFunction)
			res = ((RascalFunction) fun).call(argTypes, argVals);
		else
			res = ((OverloadedFunctionResult) fun).call(argTypes, argVals);
		
		value = ((IInteger) res.getValue()).intValue();
	
		return value;
	}

	public boolean usesTrigger() {
		return true;
	}

}
