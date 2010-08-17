package org.rascalmpl.interpreter.matching;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.Names;

public class NodePattern extends AbstractMatchingResult {
	private final TypeFactory tf = TypeFactory.getInstance();
	private final TuplePattern tuple;
	private INode subject;
	private final NodeWrapperTuple tupleSubject;
	private boolean isGenericNodeType;
	private QualifiedName qName;
	private Type type;
	
	public NodePattern(IEvaluatorContext ctx, Expression x, IMatchingResult matchPattern, QualifiedName name, List<IMatchingResult> list){
		super(ctx, x);
		
		if (matchPattern != null) {
			list.add(0, matchPattern);
			isGenericNodeType = true;
		}
		else if (name != null) {
			IString nameVal = ctx.getValueFactory().string(Names.name(Names.lastName(name)));
			list.add(0, new ValuePattern(ctx, x, ResultFactory.makeResult(tf.stringType(), nameVal, ctx)));
			isGenericNodeType = false;
			qName = name;
		}
		
		this.tuple = new TuplePattern(ctx, x, list);
		this.tupleSubject = new NodeWrapperTuple();
	}
	
	private class NodeWrapperTuple implements ITuple {
		private Type type;
		
		public int arity() {
			return 1 + subject.arity();
		}

		public IValue get(int i) throws IndexOutOfBoundsException {
			if (i == 0) {
				return ctx.getValueFactory().string(subject.getName());
			}
			return subject.get(i - 1);
		}

		public Type getType() {
			if (type == null) {
				Type[] kids = new Type[1 + subject.arity()];
				kids[0] = tf.stringType();
				for (int i = 0; i < subject.arity(); i++) {
					kids[i+1] = subject.get(i).getType();
				}
				type = tf.tupleType(kids);
			}
			return type;
		}
		
		public boolean isEqual(IValue other) {
			if (!other.getType().isTupleType()) {
				return false;
			}
			if (other.getType().getArity() != subject.arity()) {
				return false;
			}
			for (int i = 0; i < arity(); i++) {
				if (!get(i).isEqual(((ITuple)other).get(i))) {
					return false;
				}
			}
			return true;
		}
		
		public Iterator<IValue> iterator() {
			return new Iterator<IValue>() {

				boolean first = true;
				Iterator<IValue> subjectIter = subject.iterator();
				
				public boolean hasNext() {
					return first || subjectIter.hasNext(); 
				}

				public IValue next() {
					if (first) {
						first = false;
						return get(0);
					}
					return subjectIter.next();
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
	
	@Override
	public void initMatch(Result<IValue> subject) {
		if (!subject.getValue().getType().isNodeType()) {
			hasNext = false;
			return;
		}
		this.subject = (INode) subject.getValue();
		tuple.initMatch(ResultFactory.makeResult(tupleSubject.getType(), tupleSubject, ctx));
	}
	
	@Override
	public Type getType(Environment env) {
		if (type == null) {
			type = getConstructorType(env);

			if (type.isConstructorType()) {
				type = getConstructorType(env).getAbstractDataType();
			}
		}
		return type;
	}

	private Type getSignatureType(Environment env) {
		int arity = tuple.getType(env).getArity() - 1;

		Type[] types = new Type[arity];

		for (int i = 1; i < arity + 1; i += 1) {
			types[i - 1] =  tuple.getType(env).getFieldType(i);
		}

		return tf.tupleType(types);
	}
	
	
	public Type getConstructorType(Environment env) {
		 Type signature = getSignatureType(env);

		 if (!isGenericNodeType) {
			 Result<IValue> constructors = env.getVariable(qName);
			 
			 if (constructors != null && constructors instanceof OverloadedFunctionResult) {
				 for (AbstractFunction d : ((OverloadedFunctionResult) constructors).iterable()) {
					 if (d.match(signature)) {
						 return env.getConstructor(d.getReturnType(), Names.name(Names.lastName(qName)), signature);
					 }
				 }
			 }
		 }
	     return tf.nodeType();
	}
	
	@Override
	public java.util.List<String> getVariables() {
		return tuple.getVariables();
	}
	
	@Override
	public boolean hasNext(){
		return tuple.hasNext();
	}
	
	@Override
	public boolean next(){
		return tuple.next();
	}
	
	@Override
	public String toString(){
		return "nodeAsTuple:" + tuple.toString();
	}
}
