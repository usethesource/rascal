package org.rascalmpl.interpreter.result;

import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class AbstractPatternDispatchedFunction extends AbstractFunction {
	private final Map<String, List<AbstractFunction>> alternatives;
	private final Type type;
	private final int arity;

	public AbstractPatternDispatchedFunction(IEvaluator<Result<IValue>> eval, Type type, Map<String, List<AbstractFunction>> alternatives) {
		super(null, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(TypeFactory.getInstance().voidType(), TypeFactory.getInstance().voidType()), checkVarArgs(alternatives), null); // ?? I don't know if this will work..
		this.type = type;
		this.alternatives = alternatives;
		this.arity = minArity(alternatives);
	}
	
	@Override
	public boolean isPublic() {
		throw new UnsupportedOperationException();
	}
	
	public Map<String,List<AbstractFunction>> getMap() {
		return alternatives;
	}

	@Override
	public boolean isPatternDispatched() {
		return true;
	}

	/**
	 * We check if the different alternatives for this function have different arities here as well.
	 */
	private static boolean checkVarArgs(Map<String, List<AbstractFunction>> alts) {
		int arity = -1;
		for (List<AbstractFunction> l : alts.values()) {
			for (AbstractFunction f : l) {
				if (arity != -1) {
					if (arity != f.getArity()) { 
						return true;
					}
				}
				else {
					arity = f.getArity();
				}
				
				if (f.hasVarArgs()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static int minArity(Map<String, List<AbstractFunction>> alts) {
		int min = Integer.MAX_VALUE;
		for (List<AbstractFunction> l : alts.values()) {
			for (AbstractFunction f : l) {
				if (f.getArity() < min) {
					min = f.getArity();
				}
			}
		}
		return min;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return equals(other);
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 == null)
			return false;
		if (arg0.getClass() == getClass()) {
			AbstractPatternDispatchedFunction other = (AbstractPatternDispatchedFunction) arg0;
			return other.alternatives.equals(alternatives);
		}
		return false;
	}

	@Override
	/**
	 * In this case we produce the minimum arity of the overloaded alternatives
	 */
	public int getArity() {
		return arity;
	}

	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues) {
		String label = null;
		
		if (argTypes.length == 0) {
			throw new MatchFailed();
		}
		
		if (argTypes[0].isAbstractDataType() || argTypes[0].isConstructorType()) {
			label = ((IConstructor) argValues[0]).getConstructorType().getName();
			List<AbstractFunction> funcs = alternatives.get(label);
			
			if (funcs != null) {
				for (AbstractFunction candidate : funcs) {
					if ((candidate.hasVarArgs() && argValues.length >= candidate.getArity() - 1)
							|| candidate.getArity() == argValues.length) {
						try {
							return candidate.call(argTypes, argValues);
						}
						catch (MatchFailed m) {
							// could happen if pattern dispatched
						}
						catch (Failure e) {
							// could happen if function body throws fail
						}
					}
				}

				throw new MatchFailed();
			}
		}
		
		throw new MatchFailed();
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		return call(null, argTypes, argValues);
	}

	@Override
	public boolean isStatic() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefault() {
		return false;
	}
}