package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.FigurePApplet;

public class ComputedRealProperty implements IRealPropertyValue {
	Property property;
	IValue fun;
	Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	float value;
	private FigurePApplet fpa;

	public ComputedRealProperty(Property prop, IValue fun, FigurePApplet fpa){
		this.property = prop;
		this.fun = fun;
		this.fpa = fpa;
	}
	
	public Property getProperty(){
		return property;
	}
	
	public synchronized float getValue() {
		Result<IValue> res;
		float old = value;
		synchronized(fpa){
			if(fun instanceof RascalFunction)
				res = ((RascalFunction) fun).call(argTypes, argVals);
			else
				res = ((OverloadedFunctionResult) fun).call(argTypes, argVals);
		}
		if(res.getType().isIntegerType())
			value = ((IInteger) res.getValue()).intValue();
		else if(res.getType().isRealType())
			value = ((IReal) res.getValue()).floatValue();
		else
			value = ((INumber) res.getValue()).toReal().floatValue();
		if(value != old)
			fpa.setComputedValueChanged();
		return value;
	}

	public boolean isCallBack() {
		return true;
	}

}
