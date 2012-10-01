/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
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
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatchError;
import org.rascalmpl.interpreter.utils.Names;

public class NodePattern extends AbstractMatchingResult {
	private final TypeFactory tf = TypeFactory.getInstance();
	private final TuplePattern tuple;
	private INode subject;
	private final NodeWrapperTuple tupleSubject;
	private QualifiedName qName;
	private Type type;
	private final Type constructorType;
	
	public NodePattern(IEvaluatorContext ctx, Expression x, IMatchingResult matchPattern, QualifiedName name, Type constructorType, List<IMatchingResult> list){
		super(ctx, x);
		this.constructorType = constructorType;
		
		if (matchPattern != null) {
			list.add(0, matchPattern);
		}
		else if (name != null) {
			IString nameVal = ctx.getValueFactory().string(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) name).lastName());
			list.add(0, new ValuePattern(ctx, x, ResultFactory.makeResult(tf.stringType(), nameVal, ctx)));
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
		if(subject.isVoid()) 
			throw new UninitializedPatternMatchError("Uninitialized pattern match: trying to match a value of the type 'void'", ctx.getCurrentAST());
		if (!subject.getValue().getType().isNodeType()) {
			hasNext = false;
			return;
		}
		this.subject = (INode) subject.getValue();
		
		// We should only call initMatch if the node types line up, otherwise the tuple matcher might throw a "static error" exception.
		// The following decision code decides whether it is worth it and safe to call initMatch on the tuple matcher.
		Type patternType = getConstructorType(ctx.getCurrentEnvt());
		if (patternType.isConstructorType()) {
			patternType = patternType.getAbstractDataType();
		}
		Type subjectType = subject.getType();
		if (patternType.comparable(subjectType)) {
			tuple.initMatch(ResultFactory.makeResult(tupleSubject.getType(), tupleSubject, ctx));
			hasNext = tuple.hasNext;
		}
		else {
			hasNext = false;
		}  
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		if (type == null) {
			type = getConstructorType(env);

			if (type.isConstructorType()) {
				type = getConstructorType(env).getAbstractDataType();
			}
		}
		return type;
	}

	public Type getConstructorType(Environment env) {
		 return constructorType;
	}
	
	@Override
	public List<IVarPattern> getVariables() {
		return tuple.getVariables();
	}
	
	@Override
	public boolean hasNext(){
		if (!hasNext) {
			return false;
		}
		return tuple.hasNext();
	}
	
	@Override
	public boolean next() {
		if (hasNext) {
			return tuple.next();
		}
		return false;
	}
	
	@Override
	public String toString(){
		List<IMatchingResult> children = tuple.getChildren();
		int n = children.size();
		if(n == 1){
			return qName + "()";
		}
		StringBuilder res = new StringBuilder(Names.fullName(qName));
		res.append("(");
		String sep = "";
		
		for (int i = 1; i < children.size(); i++){
			IBooleanResult mp = children.get(i);
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		res.append(")");
		
		return res.toString();
	}
}
