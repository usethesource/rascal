package org.rascalmpl.interpreter.matching;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ConcreteApplicationPattern extends AbstractMatchingResult {
	private IList subjectArgs;
	private final IMatchingResult tupleMatcher;
	private IConstructor production;
	private final ITuple tupleSubject;
	private final Type myType;

	public ConcreteApplicationPattern(
			IEvaluatorContext ctx, CallOrTree x,
			List<IMatchingResult> list) {
		super(ctx, x);
		
		// retrieve the static value of the production of this pattern
//		this.production = (IConstructor) ctx.getEvaluator().eval(x.getArguments().get(0)).getValue();
		this.production = TreeAdapter.getProduction((IConstructor) getAST().getTree());
		
		// use a tuple pattern to match the children of this pattern
		this.tupleMatcher = new TuplePattern(ctx, x, list);
		
		// this prototype can be used for every subject that comes through initMatch
		this.tupleSubject = ProductionAdapter.isLexical(production) ? new LexicalTreeAsTuple() : new TreeAsTuple();
		
		// save the type of this tree
		this.myType = x._getType();
	}
	
	public List<String> getVariables() {
		return tupleMatcher.getVariables();
	}
	
	private class TreeAsTuple implements ITuple {
		// notice how this class skips the layout nodes...
		
		public int arity() {
			return (subjectArgs.length() + 1) / 2;
		}

		public IValue get(int i) throws IndexOutOfBoundsException {
			IConstructor arg = (IConstructor) subjectArgs.get(i * 2);
//			if (TreeAdapter.isList(arg)) {
//				return TreeAdapter.getArgs(arg);
//			}
			return arg;
		}

		public Type getType() {
			Type[] fields = new Type[arity()];
			for (int i = 0; i < fields.length; i++) {
				fields[i] = get(i).getType();
			}
			return tf.tupleType(fields);
		}

		public boolean isEqual(IValue other) {
			if (!(other instanceof ITuple)) {
				return false;
			}
			
			if (arity() != ((ITuple) other).arity()) {
				return false;
			}
			
			int i = 0;
			for (IValue otherChild : (ITuple) other) {
				if (!get(i++).isEqual(otherChild)) {
					return false;
				}
			}
			
			return true;
		}

		
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {
				int currentIndex = 0;

				public boolean hasNext() {
					return currentIndex < arity();
				}

				public IValue next() {
					return get(currentIndex++);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		public IValue get(String label) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}

		public IValue select(int... fields) throws IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		public IValue select(String... fields) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}

		public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
			throw new UnsupportedOperationException();
		}

		public ITuple set(String label, IValue arg) throws FactTypeUseException {
			throw new UnsupportedOperationException();
		}

		public <T> T accept(IValueVisitor<T> v) throws VisitorException {
			throw new UnsupportedOperationException();
		}
	}
	
	private class LexicalTreeAsTuple extends TreeAsTuple {
		// notice how this class does not skip the layout nodes...
		public int arity() {
			return subjectArgs.length();
		}

		public IValue get(int i) throws IndexOutOfBoundsException {
			IConstructor arg = (IConstructor) subjectArgs.get(i);
//			if (TreeAdapter.isList(arg)) {
//				return TreeAdapter.getArgs(arg);
//			}
			return arg;
		}
	}
	

	@Override
	public void initMatch(Result<IValue> subject) {
		hasNext = false;
		Type subjectType = subject.getValue().getType();
		super.initMatch(subject);

		if(subjectType.isAbstractDataType()){
			IConstructor treeSubject = (IConstructor)subject.getValue();
		
			if (!TreeAdapter.isAppl(treeSubject)) {
				// fail early if the subject is an ambiguity cluster
				hasNext = false;
				return;
			}

			if (!TreeAdapter.getProduction(treeSubject).isEqual(production)) {
				// fail early if the subject's production is not the same
				hasNext = false;
				return;
			}
			
			this.subjectArgs = TreeAdapter.getArgs(treeSubject);
			tupleMatcher.initMatch(ResultFactory.makeResult(tupleSubject.getType(), tupleSubject, ctx));
			
			hasNext = tupleMatcher.hasNext();
		}
	}
	
	@Override
	public boolean hasNext() {
		if (!hasNext) {
			return false;
		}
		
		return tupleMatcher.hasNext();
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if (hasNext) {
			return tupleMatcher.next();
		}
		return false;
	}
	
	@Override
	public Type getType(Environment env) {
		return myType;
	}
}
