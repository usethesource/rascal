package org.rascalmpl.interpreter.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.TypeEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatchError;
import org.rascalmpl.interpreter.staticErrors.RedeclaredFunctionError;

/**
 * This class holds different functions that have the same name. Each function has different patterns as formal parameters.
 * The implementation should quickly decide which pattern is most likely to match, then use the pattern matcher to bind actual
 * values to the formal parameters. 
 * 
 * Note that the semantics of this class depend heavily on the patterns being mutually exclusive!
 * 
 * Note that ***shadowing*** rules may not have been implemented by this class at the moment.
 */
public class OverloadedFunctionResult extends Result<IValue> implements IExternalValue {
	private final static TypeFactory TF = TypeFactory.getInstance();
	
	private final Hashtable<Type, List<AbstractFunction>> candidates = new Hashtable<Type, List<AbstractFunction>>();
	private final String name;

	public OverloadedFunctionResult(String name, Type type, List<AbstractFunction> candidates, IEvaluatorContext ctx) {
		super(type, null, ctx);
		
		if (candidates.size() <= 0) {
			throw new ImplementationError("at least need one function");
		}
		
		for (AbstractFunction candidate : candidates) {
			List<Expression> formals = ((FunctionDeclaration) candidate.getAst()).getSignature().getParameters().getFormals().getFormals();
			if (formals.size() > 0) { // with args, we store the function under the type of the first argument (for speed!)
				Expression first = formals.get(0);
				Type storeUnder = null;
				TypeEvaluator te = new TypeEvaluator(ctx.getCurrentEnvt(), ctx.getHeap());
				if (first instanceof Expression.CallOrTree) { // a constructor!
					storeUnder = te.eval(first);
				}
				else { // something else than a constructor
					
				}
			}
			else { // no args candidates are stored under the void type
				List<AbstractFunction> l = this.candidates.get(TF.voidType());
				if (l == null) {
					l = new LinkedList<AbstractFunction>();
					this.candidates.put(TF.voidType(), l);
				}
				l.add(candidate);
			}
		}
		this.name = name;
	}
	
	public OverloadedFunctionResult(AbstractFunction function) {
		super(function.getType(), null, function.getEval());
		this.candidates = new ArrayList<AbstractFunction>(1);
		this.candidates.add(function);
		this.name = function.getName();
	}
	
	public List<AbstractFunction> getCandidates() {
		return Collections.unmodifiableList(candidates);
	}

	@Override
	public IValue getValue() {
		return this;
	}

	public int size() {
		return candidates.size();
	}
	
	private Type lub(List<AbstractFunction> candidates) {
		Type lub = TF.voidType();
		
		for (AbstractFunction l : candidates) {
			lub = lub.lub(l.getType());
		}
		
		return lub;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Type tuple = getTypeFactory().tupleType(argTypes);
		
		for (AbstractFunction candidate : candidates) {
			// TODO: if match would accept an array of argTypes, the tuple type would not need to be constructed
			if (candidate.match(tuple)) {
				try {
					return candidate.call(argTypes, argValues);
				}
				catch (MatchFailed m) {
					// this is perfectly normal
				}
			}
		}
		
		throw new ArgumentsMismatchError(name, candidates, argTypes, ctx.getCurrentAST());
	}
	
	public OverloadedFunctionResult join(OverloadedFunctionResult other) {
		List<AbstractFunction> joined = new ArrayList<AbstractFunction>(other.candidates.size() + candidates.size());
		joined.addAll(candidates);
		
		for (AbstractFunction cand : other.candidates) {
			if (!joined.contains(cand)) {
				joined.add(cand);
			}
		}
		
		return new OverloadedFunctionResult(name, lub(joined), joined, ctx);
	}
	
	public OverloadedFunctionResult add(AbstractFunction candidate) {
		for (AbstractFunction other : iterable()) {
			if (!other.equals((Object) candidate) && candidate.isAmbiguous(other)) {
				throw new RedeclaredFunctionError(candidate.getHeader(), other.getHeader(), candidate.getAst());
			}
		}
		
		List<AbstractFunction> joined = new ArrayList<AbstractFunction>(candidates.size() + 1);
		joined.addAll(candidates);
		
		if (!joined.contains(candidate)) {
			joined.add(candidate);
		}
		return new OverloadedFunctionResult(name, lub(joined), joined, ctx);
	}

	public Iterable<AbstractFunction> iterable() {
		return new Iterable<AbstractFunction>() {
			public Iterator<AbstractFunction> iterator() {
				return candidates.iterator();
			}
		};
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult other = (OverloadedFunctionResult) obj;
			return candidates.containsAll(other.candidates)
			&& other.candidates.containsAll(candidates);
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (AbstractFunction l : iterable()) {
			b.append(l.toString());
			b.append(' ');
		}
		
		return b.toString();
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	public boolean isEqual(IValue other) {
		if (other instanceof OverloadedFunctionResult) {
			return candidates.equals(((OverloadedFunctionResult) other).candidates);
		}
		return false;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(
			Result<V> that) {
		return that.equalToOverloadedFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToOverloadedFunction(
			OverloadedFunctionResult that) {
		return ResultFactory.bool(candidates.equals(that.candidates), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that) {
		return that.compareOverloadedFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> compareOverloadedFunction(
			OverloadedFunctionResult that) {
		if (that == this) {
			return ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(0), ctx);
		}
		
		if (candidates.size() > that.candidates.size()) {
			return  ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(1), ctx);
		}
		
		if (candidates.size() < that.candidates.size()) {
			 ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(-1), ctx);
		}
		
		for (AbstractFunction f : candidates) {
			for (AbstractFunction g : that.candidates) {
				Result<U> result = f.compare(g);
				
				if (((IInteger) result.getValue()).intValue() != 0) {
					return result;
				}
			}
		}
		
		return ResultFactory.makeResult(TF.integerType(), getValueFactory().integer(0), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeOverloadedFunction(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue> Result<U> composeOverloadedFunction(OverloadedFunctionResult that) {
		List<AbstractFunction> newAlternatives = new ArrayList<AbstractFunction>(candidates.size());
		
		for (AbstractFunction f : candidates) {
			for (AbstractFunction g : that.candidates) {
				if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
					newAlternatives.add(new ComposedFunctionResult(f, g, ctx));
				}
			}
		}
		
		if (newAlternatives.size() == 0) {
			return undefinedError("composition", that);
		}
		
		return (Result<U>) new OverloadedFunctionResult(name, getType(), newAlternatives, ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue> Result<U> composeFunction(AbstractFunction g) {
		List<AbstractFunction> newAlternatives = new ArrayList<AbstractFunction>(candidates.size());

		for (AbstractFunction f : candidates) {
			if (getTypeFactory().tupleType(f.getReturnType()).isSubtypeOf(g.getFunctionType().getArgumentTypes())) {
				newAlternatives.add(new ComposedFunctionResult(f, g, ctx));
			}
		}
		
		if (newAlternatives.size() == 0) {
			return undefinedError("composition", g);
		}
		
		return (Result<U>) new OverloadedFunctionResult(name, getType(), newAlternatives, ctx);
	}
	
}
