package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

public class BoolResult extends ValueResult<IBool> {

	public BoolResult(Type type, IBool bool) {
		this(type, bool, null);
	}
		
	public BoolResult(Type type, IBool bool, Iterator<AbstractResult<IValue>> iter) {
		super(type, bool, iter);
	}
	
	public BoolResult(boolean b) {
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b));
	}
	
	public BoolResult(boolean b, Iterator<AbstractResult<IValue>> iter){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b), iter);
	}

}
