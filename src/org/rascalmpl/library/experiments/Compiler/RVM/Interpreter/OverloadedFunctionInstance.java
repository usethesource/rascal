package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class OverloadedFunctionInstance implements IValue {
	
	final int[] functions;
	final int[] constructors;
	final Frame env;
	
	private Type type;
	private List<Function> functionStore;
	private List<Type> constructorStore;
	
	public OverloadedFunctionInstance(int[] functions, int[] constructors, Frame env, 
										ArrayList<Function> functionStore, ArrayList<Type> constructorStore) {
		this.functions = functions;
		this.constructors = constructors;
		this.env =  env;
		this.functionStore = functionStore;
		this.constructorStore = constructorStore;
	}

	@Override
	public Type getType() {
		if(this.type != null) {
			return this.type;
		}
		Set<FunctionType> types = new HashSet<FunctionType>();
		for(int fun : this.functions) {
			types.add((FunctionType) functionStore.get(fun).ftype);
		}
		for(int constr : this.constructors) {
			Type type = constructorStore.get(constr);
			types.add((FunctionType) RascalTypeFactory.getInstance().functionType(type.getAbstractDataType(), type.getFieldTypes()));
		}
		this.type = RascalTypeFactory.getInstance().overloadedFunctionType(types);
		return this.type;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAnnotatable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		// TODO Auto-generated method stub
		return null;
	}

}
