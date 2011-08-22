package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.swt.ICallbackEnv;

public class HandlerValue extends PropertyValue<IValue>  {
	
	IValue fun;
	IValue value;

	
	public HandlerValue(IValue fun){
		this.fun = fun;
	}
	
	public IValue execute(ICallbackEnv env,Type[] types,IValue[] args){
		value =env.executeRascalCallBack(fun, types, args).getValue();
		return value;
	}

	@Override
	public IValue getValue() {
		return value;
	}

}
