package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class ElementResult<T extends IValue> extends Result<T> {
	public ElementResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, ctx);
	}
	
	public ElementResult(Type type, T value, Iterator<Result<IValue>> iter, IEvaluatorContext ctx) {
		super(type, value, iter, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.insertElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> inSet(SetResult s) {
		return s.elementOf(this);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> notInSet(SetResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> inRelation(RelationResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInRelation(RelationResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> inList(ListResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInList(ListResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> inMap(MapResult s) {
		return s.elementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInMap(MapResult s) {
		return s.notElementOf(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s) {
		return s.addElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s) {
		return s.removeElement(this);
	}

	@Override
	protected <U extends IValue> Result<U> addList(ListResult s) {
		return s.appendElement(this);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult s) {
		return s.removeElement(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> addRelation(RelationResult that) {
		if (that.getValue().getElementType().isVoidType()) {
			return makeResult(getTypeFactory().setType(this.getType()), that.getValue().insert(this.getValue()), ctx);
		}
		return super.addRelation(that);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> setAnnotation(String annoName, Result<V> anno, Environment env) {
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
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env) {
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
	protected <U extends IValue> Result<U> equalToValue(ValueResult that) {
		return that.equalityBoolean(this);
	}

	
	protected static int compareIValues(IValue left, IValue right, IEvaluatorContext ctx) {
		Result<IValue> leftResult = makeResult(TypeFactory.getInstance().valueType(), left, ctx);
		Result<IValue> rightResult = makeResult(TypeFactory.getInstance().valueType(), right, ctx);
		Result<IValue> resultResult = leftResult.compare(rightResult);
		// compare always returns IntegerResult so we can cast its value.
		return ((IInteger)resultResult.getValue()).intValue();
	}

	// FIXME: ast should not be passed at this level
	private static SortedSet<IValue> sortedSet(Iterator<IValue> iter, final IEvaluatorContext ctx) {
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

	protected static int compareISets(ISet left, ISet right, IEvaluatorContext ctx) {
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
	
	protected <V extends IValue> int comparisonInts(Result<V> that) {
		return ((IInteger)compare(that).getValue()).intValue();
	}

	protected <U extends IValue, V extends IValue> Result<U> equalityBoolean(ElementResult<V> that) {
		// Do not delegate to comparison here, since it takes runtime types into account
		return bool((((IInteger)compare(that).getValue()).intValue() == 0), ctx);
	}

	protected <U extends IValue, V extends IValue> Result<U> nonEqualityBoolean(ElementResult<V> that) {
		return bool((!that.getValue().isEqual(this.getValue())), ctx);
	}

}
