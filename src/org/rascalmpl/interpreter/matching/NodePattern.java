/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordArgument;
import org.rascalmpl.ast.KeywordArguments;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatch;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class NodePattern extends AbstractMatchingResult {
	private final TypeFactory tf = TypeFactory.getInstance();
	private Type type;
	private final Type constructorType;
	private QualifiedName qName;
	private List<IMatchingResult> children;
	private List<IMatchingResult> orgChildren;
	private INode subject;
	private int nextChild;
	private IMatchingResult namePattern;
	private final int positionalArity;
	private int subjectPositionalArity;
	private LinkedList<String> keywordParameterNames;
	private LinkedList<IMatchingResult> orgKeywordChildren;
	
	public NodePattern(IEvaluatorContext ctx, Expression x, IMatchingResult matchPattern, QualifiedName name, Type constructorType, List<IMatchingResult> list){
		super(ctx, x);
		this.constructorType = constructorType;
		this.orgChildren = list;
		positionalArity = orgChildren.size();
		if (matchPattern != null) {
			namePattern = matchPattern;
		}
		else if (name != null) {
			qName = name;
		}
		KeywordArguments keywordArgs = x.getKeywordArguments();
		this.orgKeywordChildren = new LinkedList<IMatchingResult>();
		this.keywordParameterNames = new LinkedList<String>();
		this.orgKeywordChildren = new LinkedList<IMatchingResult>();
		if(keywordArgs.isDefault()){
				for(KeywordArgument kwa : keywordArgs.getKeywordArgumentList()){
					IMatchingResult mr = kwa.getExpression().buildMatcher(ctx.getEvaluator());
					keywordParameterNames.add(Names.name(kwa.getName()));
					orgKeywordChildren.add(mr);
				}
		}
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		hasNext = false;
		if(subject.isVoid()) 
			throw new UninitializedPatternMatch("Uninitialized pattern match: trying to match a value of the type 'void'", ctx.getCurrentAST());

		if (!subject.getValue().getType().isNodeType()) {
			return;
		}
		
		if (subject.getType().isSubtypeOf(Factory.Tree) && TreeAdapter.isAppl((IConstructor) subject.getValue())) {
		  this.subject = new TreeAsNode((IConstructor) subject.getValue());
		}
		else {
		  this.subject = (INode) subject.getValue();
		}
		
		String sname = this.subject.getName();
		if(qName != null){
			if(!((org.rascalmpl.semantics.dynamic.QualifiedName.Default) qName).lastName().equals(sname)){
				return;
			}
		} else {
			IString nameVal = ctx.getValueFactory().string(sname);

			namePattern.initMatch(ResultFactory.makeResult(tf.stringType(), nameVal, ctx));
			if(!(namePattern.hasNext() && namePattern.next())){
				return; // TODO What if the name has alternatives?
			}	
		}
		
		// Determine type compatibility between pattern and subject
		
		Type patternType = getType(ctx.getCurrentEnvt(), null);
		
		Type subjectType = this.subject.getType();
		subjectPositionalArity = 0;
		
		if (subjectType.isAbstractDataType()) {
			 subjectType = ((IConstructor) this.subject).getConstructorType();
			if(subjectType.hasDefaults()){
				subjectPositionalArity = subjectType.getPositionalArity();
				
				if (positionalArity != subjectPositionalArity  ||
					positionalArity + keywordParameterNames.size() > this.subject.arity()){
					return;
				}
			} else {
				if(this.subject.arity() != positionalArity){
					return;
				}
			}
		} else {
			if (this.subject.arity() != positionalArity){
				return;
			}
		}
		
		if (patternType.comparable(subjectType)) {
			hasNext = true;
		} else {
			return;
		}
		
		children = new ArrayList<IMatchingResult>();
		
		int kwpositions[] = new int[this.subject.arity()];
		
		for(int i = 0; i < kwpositions.length; i++){
			kwpositions[i] = -1;
		}
		if(keywordParameterNames != null){
			for(int i = 0; i < keywordParameterNames.size(); i++){
				String kwname = keywordParameterNames.get(i);
				int pos = subjectType.getFieldIndex(kwname);
				kwpositions[pos] = i;
			}
		}
	
		for (int i = 0; i < this.subject.arity(); i += 1){
			IValue childValue = this.subject.get(i);
			IMatchingResult child;
			if(i < positionalArity){
				child = orgChildren.get(i);
			} else if(kwpositions[i] >= 0){
				child = orgKeywordChildren.get(kwpositions[i]);
			} else {
				child = new QualifiedNamePattern(ctx);
			} 
			child.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
			children.add(child);
			hasNext = child.hasNext();
			if (!hasNext) {
				break; // saves time!
			}
		}
		nextChild = 0;
	}

	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		if (type == null) {
			type = getConstructorType(env);

			if (type != null && type.isConstructorType()) {
				type = getConstructorType(env).getAbstractDataType();
			}
			
			if (type == null) {
			  type = TypeFactory.getInstance().nodeType();
			}
		}
		return type;
	}

	public Type getConstructorType(Environment env) {
		 return constructorType;
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();
		for (int i = 0; i < orgChildren.size(); i += 1) {
			res.addAll(orgChildren.get(i).getVariables());
		}
		for (int i = 0; i < orgKeywordChildren.size(); i += 1) {
			res.addAll(orgKeywordChildren.get(i).getVariables());
		}
		
		return res;
	}

	@Override
	public boolean next(){
		checkInitialized();

		if (!hasNext) {
			return false;
		}

		if (children.size() == 0) {
			hasNext = false;
			return true;
		}
		
		while (nextChild >= 0) {
			IMatchingResult nextPattern = children.get(nextChild);

			if (nextPattern.hasNext() && nextPattern.next()) {
				if (nextChild == children.size() - 1) {
					// We need to make sure if there are no
					// more possible matches for any of the tuple's fields, 
					// then the next call to hasNext() will definitely returns false.
					hasNext = false;
					for (int i = nextChild; i >= 0; i--) {
						hasNext |= children.get(i).hasNext();
					}
					return true;
				}
				
				nextChild++;
			}
			else {
				nextChild--;

				if (nextChild >= 0) {
					for (int i = nextChild + 1; i < children.size(); i++) {
						IValue childValue = subject.get(i);
						IMatchingResult tailChild = children.get(i);
						tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
					}
				}
			}
		}
	    hasNext = false;
	    return false;
	}
	
	@Override
	public String toString(){
		int n = orgChildren.size();
		if(n == 1){
			return Names.fullName(qName) + "()";
		}
		StringBuilder res = new StringBuilder(Names.fullName(qName));
		res.append("(");
		String sep = "";
		
		for (int i = 0; i < orgChildren.size(); i++){
			IBooleanResult mp = orgChildren.get(i);
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		for(int i = 0; i < orgKeywordChildren.size(); i++){
			IBooleanResult mp = orgKeywordChildren.get(i);
			res.append(sep);
			sep = ", ";
			res.append(keywordParameterNames.get(i));
			res.append("=");
			res.append(mp.toString());
		}
		res.append(")");
		
		return res.toString();
	}
	
	private class TreeAsNode implements INode {
	  private final String name;
    private final IList args;

    public TreeAsNode(IConstructor tree) {
	    this.name = TreeAdapter.getConstructorName(tree);
	    this.args = TreeAdapter.isContextFree(tree) ? TreeAdapter.getASTArgs(tree) : TreeAdapter.getArgs(tree);
    }
    
    @Override
    public Type getType() {
      return TypeFactory.getInstance().nodeType();
    }

    @Override
    public <T> T accept(IValueVisitor<T> v) throws VisitorException {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEqual(IValue other) {
      throw new UnsupportedOperationException();
    }

    @Override
    public IValue get(int i) throws IndexOutOfBoundsException {
      // TODO: this should deal with regular expressions in the "right" way, such as skipping 
      // over optionals and alternatives.
      return args.get(i);
    }

    @Override
    public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
      throw new UnsupportedOperationException();
    }

    @Override
    public int arity() {
      return args.length();
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Iterable<IValue> getChildren() {
      return args;
    }

    @Override
    public Iterator<IValue> iterator() {
      return args.iterator();
    }

    @Override
    public IValue getAnnotation(String label) throws FactTypeUseException {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode setAnnotation(String label, IValue newValue) throws FactTypeUseException {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAnnotation(String label) throws FactTypeUseException {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAnnotations() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, IValue> getAnnotations() {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode setAnnotations(Map<String, IValue> annotations) {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode joinAnnotations(Map<String, IValue> annotations) {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode removeAnnotation(String key) {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode removeAnnotations() {
      throw new UnsupportedOperationException();
    }

    @Override
    public INode replace(int first, int second, int end, IList repl) throws FactTypeUseException,
        IndexOutOfBoundsException {
      throw new UnsupportedOperationException();
    }
	}
}


