package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredAnnotationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.ast.AbstractAST;

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
	protected <U extends IValue> Result<U> inSet(SetResult s, AbstractAST ast) {
		return s.elementOf(this, ast);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> notInSet(SetResult s, AbstractAST ast) {
		return s.notElementOf(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> inRelation(RelationResult s, AbstractAST ast) {
		return s.elementOf(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInRelation(RelationResult s, AbstractAST ast) {
		return s.notElementOf(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> inList(ListResult s, AbstractAST ast) {
		return s.elementOf(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> notInList(ListResult s, AbstractAST ast) {
		return s.notElementOf(this, ast);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> addSet(SetResult s, AbstractAST ast) {
		return s.addElement(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> subtractSet(SetResult s, AbstractAST ast) {
		return s.removeElement(this, ast);
	}

	@Override
	protected <U extends IValue> Result<U> addList(ListResult s, AbstractAST ast) {
		return s.appendElement(this, ast);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult s, AbstractAST ast) {
		return s.removeElement(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> setAnnotation(
			String annoName, Result<V> anno, Environment env, AbstractAST ast) {
				Type annoType = env.getAnnotationType(getType(), annoName);
			
				if (annoType == null) {
					throw new UndeclaredAnnotationError(annoName, getType(), ast);
				}
				if (!anno.getType().isSubtypeOf(annoType)){
					throw new UnexpectedTypeError(annoType, anno.getType(), ast);
				}
			
				IValue annotatedBase = ((IConstructor)getValue()).setAnnotation(annoName, anno.getValue());
				
				// TODO: applyRuels?
				return makeResult(getType(), annotatedBase);
			}

	@Override
	public <U extends IValue> Result<U> getAnnotation(String annoName, Environment env, AbstractAST ast) {
		Type annoType = env.getAnnotationType(getType(), annoName);
	
		if (annoType == null) {
			throw new UndeclaredAnnotationError(annoName, getType(), ast);
		}
	
		IValue annoValue = ((IConstructor) getValue()).getAnnotation(annoName);
		if (annoValue == null) {
			throw RuntimeExceptionFactory.noSuchAnnotation(annoName, ast);
		}
		// TODO: applyRules?
		return makeResult(annoType, annoValue);
	}
	

	@Override
	protected <U extends IValue> Result<U> equalToValue(ValueResult that, AbstractAST ast) {
		return that.equalityBoolean(this);
	}

	
	protected static int compareIValues(IValue left, IValue right, AbstractAST ast) {
		Result<IValue> leftResult = makeResult(left.getType(), left);
		Result<IValue> rightResult = makeResult(right.getType(), right);
		Result<IValue> resultResult = leftResult.compare(rightResult, ast);
		// compare always returns IntegerResult so we can cast its value.
		return ((IInteger)resultResult.getValue()).intValue();
	}

	// FIXME: ast should not be passed at this level
	private static SortedSet<IValue> sortedSet(Iterator<IValue> iter, final AbstractAST ast) {
		Comparator<IValue> comparator = new Comparator<IValue>() {
			public int compare(IValue o1, IValue o2) {
				return compareIValues(o1, o2, ast);
			}
		};
		SortedSet<IValue> set = new TreeSet<IValue>(comparator);
		while (iter.hasNext()) {
			IValue value = iter.next();
			set.add(value);
		}
		return set;
	}

	protected static int compareISets(ISet left, ISet right, AbstractAST ast) {
		int compare = new Integer(left.size()).compareTo(right.size());
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
		
		SortedSet<IValue> leftSet = sortedSet(left.iterator(), ast);
		SortedSet<IValue> rightSet = sortedSet(right.iterator(), ast);
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

	public ElementResult(Type type, T value) {
		super(type, value);
	}
	
	public ElementResult(Type type, T value, Iterator<Result<IValue>> iter) {
		super(type, value, iter);
	}
	
	protected <V extends IValue> int comparisonInts(Result<V> that, AbstractAST ast) {
		return ((IInteger)compare(that, ast).getValue()).intValue();
	}

	protected <U extends IValue> Result<U> equalityBoolean(ElementResult that) {
		// Do not delegate to comparison here, since it takes runtime types into account
		return bool(that.getValue().isEqual(this.getValue()));
	}

	protected <U extends IValue, V extends IValue> Result<U> nonEqualityBoolean(ElementResult<V> that) {
		return bool(!that.getValue().isEqual(this.getValue()));
	}

}
