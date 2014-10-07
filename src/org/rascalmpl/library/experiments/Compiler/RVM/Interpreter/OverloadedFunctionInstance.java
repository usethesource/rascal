package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class OverloadedFunctionInstance implements /*ICallableValue,*/ IExternalValue {
	final int[] functions;
	final int[] constructors;
	final Frame env;
	
	private Type type;
	private List<Function> functionStore;
	private List<Type> constructorStore;
	
	final RVM rvm;
	
	public OverloadedFunctionInstance(int[] functions, int[] constructors, Frame env, 
										List<Function> functionStore, List<Type> constructorStore, RVM rvm) {
		this.functions = functions;
		this.constructors = constructors;
		this.env = env;
		this.functionStore = functionStore;
		this.constructorStore = constructorStore;
		this.rvm = rvm;
	}
	
	/**
	 * Assumption: scopeIn != -1  
	 */
	public static OverloadedFunctionInstance computeOverloadedFunctionInstance(int[] functions, int[] constructors, Frame cf, int scopeIn,
			                                                                     List<Function> functionStore, List<Type> constructorStore, RVM rvm) {
		for(Frame env = cf; env != null; env = env.previousScope) {
			if (env.scopeId == scopeIn) {
				return new OverloadedFunctionInstance(functions, constructors, env, functionStore, constructorStore, rvm);
			}
		}
		throw new CompilerError("Could not find a matching scope when computing a nested overloaded function instance: " + scopeIn);
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
			types.add((FunctionType) RascalTypeFactory.getInstance().functionType(type.getAbstractDataType(), type.getFieldTypes(), TypeFactory.getInstance().voidType()));
		}
		this.type = RascalTypeFactory.getInstance().overloadedFunctionType(types);
		return this.type;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return this == other;
	}

	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public int getArity() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean hasVarArgs() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean hasKeywordArgs() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
//		// TODO: 
//		return null;
//	}
//
//	@Override
//	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
//		return this.call(null, argTypes, argValues, keyArgValues);
//	}
//
//	@Override
//	public ICallableValue cloneInto(Environment env) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isStatic() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public IEvaluator<Result<IValue>> getEval() {
//		return rvm.getEvaluatorContext().getEvaluator();
//	}

	@Override
  public boolean mayHaveKeywordParameters() {
    return false;
  }
  
  @Override
  public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
    throw new IllegalOperationException(
        "Cannot be viewed as with keyword parameters", getType());
  }
}
