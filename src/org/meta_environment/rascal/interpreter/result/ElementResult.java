package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredAnnotationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public class ElementResult<T extends IValue> extends Result<T> {

	@Override
	protected <U extends IValue> Result<U> inSet(SetResult s, EvaluatorContext ctx) {
		return s.elementOf(this, ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> notInSet(SetResult s, EvaluatorContext ctx) {
		return s.notElementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> inRelation(RelationResult s, EvaluatorContext ctx) {
		return s.elementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInRelation(RelationResult s, EvaluatorContext ctx) {
		return s.notElementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> inList(ListResult s, EvaluatorContext ctx) {
		return s.elementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInList(ListResult s, EvaluatorContext ctx) {
		return s.notElementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> inMap(MapResult s, EvaluatorContext ctx) {
		return s.elementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInMap(MapResult s, EvaluatorContext ctx) {
		return s.notElementOf(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s, EvaluatorContext ctx) {
		return s.addElement(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s, EvaluatorContext ctx) {
		return s.removeElement(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> addList(ListResult s, EvaluatorContext ctx) {
		return s.appendElement(this, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult s, EvaluatorContext ctx) {
		return s.removeElement(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> setAnnotation(
			String annoName, Result<V> anno, Environment env, EvaluatorContext ctx) {
				Type annoType = env.getAnnotationType(getType(), annoName);
			
				if (annoType == null) {
					throw new UndeclaredAnnotationError(annoName, getType(), ctx.getCurrentAST());
				}
				if (!anno.getType().isSubtypeOf(annoType)){
					throw new UnexpectedTypeError(annoType, anno.getType(), ctx.getCurrentAST());
				}
			
				IValue annotatedBase = ((IConstructor)getValue()).setAnnotation(annoName, anno.getValue());
				
				// TODO: applyRuels?
				return makeResult(getType(), annotatedBase, ctx);
			}

	@Override
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env, EvaluatorContext ctx) {
		Type annoType = env.getAnnotationType(getType(), annoName);
	
		if (annoType == null) {
			throw new UndeclaredAnnotationError(annoName, getType(), ctx.getCurrentAST());
		}
	
		IValue annoValue = ((IConstructor) getValue()).getAnnotation(annoName);
		if (annoValue == null) {
			throw RuntimeExceptionFactory.noSuchAnnotation(annoName, ctx.getCurrentAST(), null);
		}
		// TODO: applyRules?
		return makeResult(annoType, annoValue, ctx);
	}
	

	@Override
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, EvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}

	
	protected static int compareIValues(IValue left, IValue right, EvaluatorContext ctx) {
		Result<IValue> leftResult = makeResult(left.getType(), left, ctx);
		Result<IValue> rightResult = makeResult(right.getType(), right, ctx);
		Result<IValue> resultResult = leftResult.compare(rightResult, ctx);
		// compare always returns IntegerResult so we can cast its value.
		return ((IInteger)resultResult.getValue()).intValue();
	}

	// FIXME: ast should not be passed at this level
	private static SortedSet<IValue> sortedSet(Iterator<IValue> iter, final EvaluatorContext ctx) {
		Comparator<IValue> comparator = new Comparator<IValue>() {
			public int compare(IValue o1, IValue o2) {
				return compareIValues(o1, o2, ctx);
			}
		};
		SortedSet<IValue> set = new TreeSet<IValue>(comparator);
		while (iter.hasNext()) {
			IValue value = iter.next();
			set.add(value);
		}
		return set;
	}

	protected static int compareISets(ISet left, ISet right, EvaluatorContext ctx) {
		int compare = Integer.valueOf(left.size()).compareTo(Integer.valueOf(right.size()));
		if (compare != 0) {
			return compare;
		}
		
		// Sets are of equal size from here on
		if (left.isEqual(right)) {
			return 0;
		}
		if (left.isSubsetOf(right)) {
			return -1;
		}
		if (right.isSubsetOf(left)) {
			return 1;
		}
		
		SortedSet<IValue> leftSet = sortedSet(left.iterator(), ctx);
		SortedSet<IValue> rightSet = sortedSet(right.iterator(), ctx);
		Comparator<? super IValue> comparator = leftSet.comparator();
	
		while (!leftSet.isEmpty()) {
			compare = comparator.compare(leftSet.last(), rightSet.last());
			if (compare != 0) {
				return compare;
			}
			leftSet = leftSet.headSet(leftSet.last());
			rightSet = rightSet.headSet(rightSet.last());
		}
		return 0;
	}

	public ElementResult(Type type, T value, EvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ElementResult(Type type, T value, Iterator<Result<IValue>> iter, EvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	protected <V extends IValue> int comparisonInts(Result<V> that, EvaluatorContext ctx) {
		return ((IInteger)compare(that, ctx).getValue()).intValue();
	}

	@SuppressWarnings("unchecked")
	protected <U extends IValue, V extends IValue> Result<U> equalityBoolean(ElementResult that, EvaluatorContext ctx) {
		// Do not delegate to comparison here, since it takes runtime types into account
		return bool(((IInteger)compare(that, ctx).getValue()).intValue() == 0);
	}

	protected <U extends IValue, V extends IValue> Result<U> nonEqualityBoolean(ElementResult<V> that) {
		return bool(!that.getValue().isEqual(this.getValue()));
	}

}
