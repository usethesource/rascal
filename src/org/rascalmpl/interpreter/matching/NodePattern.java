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

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordArgument_Expression;
import org.rascalmpl.ast.KeywordArguments_Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatch;
import org.rascalmpl.interpreter.utils.Cases;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class NodePattern extends AbstractMatchingResult {
	private final TypeFactory tf = TypeFactory.getInstance();
	private Type type;
	private final Type patternConstructorType;
	private QualifiedName qName;
	private List<IMatchingResult> patternChildren;
	private List<IMatchingResult> patternOriginalChildren;
	private INode subject;
	private int nextChild;
	private IMatchingResult namePattern;
	private int patternPositionalArity;   	// Arity of pattern including only positional arguments
	private final int patternTotalArity;  	// Arity of pattern including keyword arguments
	private int subjectPositionalArity;		// Arity of subject including only positional arguments
	private int subjectTotalArity; 			// Arity of subject including keyword arguments
	private LinkedList<String> patternKeywordParameterNames;
	private LinkedList<IMatchingResult> patternOriginalKeywordChildren;
	private final boolean matchUPTR;
	
	public NodePattern(IEvaluatorContext ctx, Expression x, IMatchingResult matchPattern, QualifiedName name, Type constructorType, List<IMatchingResult> list){
		super(ctx, x);
		this.patternConstructorType = constructorType;
		this.patternOriginalChildren = list;
		patternPositionalArity = patternOriginalChildren.size();
		if (matchPattern != null) {
			namePattern = matchPattern;
			matchUPTR = false;
		}
		else if (name != null) {
			qName = name;
			matchUPTR = Cases.IUPTR_NAMES.contains(Names.fullName(qName));
		}
		else {
			matchUPTR = false;
		}
		KeywordArguments_Expression keywordArgs = x.getKeywordArguments();
		this.patternOriginalKeywordChildren = new LinkedList<IMatchingResult>();
		this.patternKeywordParameterNames = new LinkedList<String>();
		this.patternOriginalKeywordChildren = new LinkedList<IMatchingResult>();
		if(keywordArgs.isDefault()){
				for(KeywordArgument_Expression kwa : keywordArgs.getKeywordArgumentList()){
					IMatchingResult mr = kwa.getExpression().buildMatcher(ctx.getEvaluator());
					patternKeywordParameterNames.add(Names.name(kwa.getName()));
					patternOriginalKeywordChildren.add(mr);
				}
				patternTotalArity = patternPositionalArity + patternKeywordParameterNames.size();
		} else {
			patternTotalArity = patternPositionalArity;
		}
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		boolean nodeSubject = false;
		super.initMatch(subject);
		hasNext = false;
		if(subject.isVoid()) 
			throw new UninitializedPatternMatch("Uninitialized pattern match: trying to match a value of the type 'void'", ctx.getCurrentAST());

		if (!subject.getValue().getType().isNode()) {
			return;
		}
		
		if (!matchUPTR && subject.getType().isSubtypeOf(Factory.Tree) && TreeAdapter.isAppl((IConstructor) subject.getValue())) {
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
		subjectPositionalArity = subjectTotalArity = this.subject.arity();
		
		if(patternTotalArity > subjectTotalArity)
			return;
		if (subjectType.isAbstractData()) {
			subjectType = ((IConstructor) this.subject).getConstructorType();
			if(subjectType.hasKeywordArguments()){
				subjectPositionalArity = subjectType.getPositionalArity();
				subjectTotalArity = subjectType.getArity();
				if (patternPositionalArity != subjectPositionalArity || patternTotalArity > subjectTotalArity){
					return;
				}
			} else {
				if(patternTotalArity != subjectTotalArity){
					return;
				}
			}
		} else if(subjectType.isNode()){
			nodeSubject = true;
			 INode node = ((INode) this.subject);
			 if(node.hasKeywordArguments()){
				 subjectPositionalArity = node.positionalArity();
				 subjectTotalArity = node.arity();
					if (patternPositionalArity != subjectPositionalArity || patternTotalArity > subjectTotalArity){
						return;
					}
			 } else {
					if(patternTotalArity != subjectTotalArity){
						return;
					}
			 }
		} else {
			if (patternTotalArity != subjectTotalArity){
				return;
			}
		}
		
		if (patternType.comparable(subjectType)) {
			hasNext = true;
		} else {
			return;
		}
		
		patternChildren = new ArrayList<IMatchingResult>();
		
		int kwpositions[] = new int[subjectTotalArity];
		
		for(int i = 0; i < kwpositions.length; i++){
			kwpositions[i] = -1;
		}
	
		if(patternKeywordParameterNames != null){
			for(int i = 0; i < patternKeywordParameterNames.size(); i++){
				String kwname = patternKeywordParameterNames.get(i);
				int pos = nodeSubject ? ((INode) this.subject).getKeywordIndex(kwname)
						              : subjectType.getFieldIndex(kwname);
				if(pos >= 0){
					kwpositions[pos] = i;
				} else {
					hasNext = false;
					return;
				}
			}
		}
	
		for (int i = 0; i < subjectTotalArity; i += 1){
			IValue subjectChild = this.subject.get(i);
			IMatchingResult patternChild;
			if(i < patternPositionalArity){
				patternChild = patternOriginalChildren.get(i);
			} else if(kwpositions[i] >= 0){
				patternChild = patternOriginalKeywordChildren.get(kwpositions[i]);
			} else {
				patternChild = new QualifiedNamePattern(ctx);
			} 
			patternChild.initMatch(ResultFactory.makeResult(subjectChild.getType(), subjectChild, ctx));
			patternChildren.add(patternChild);
			hasNext = patternChild.hasNext();
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

			if (type != null && type.isConstructor()) {
				type = getConstructorType(env).getAbstractDataType();
			}
			
			if (type == null) {
			  type = TypeFactory.getInstance().nodeType();
			}
		}
		return type;
	}

	public Type getConstructorType(Environment env) {
		 return patternConstructorType;
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();
		for (int i = 0; i < patternOriginalChildren.size(); i += 1) {
			res.addAll(patternOriginalChildren.get(i).getVariables());
		}
		for (int i = 0; i < patternOriginalKeywordChildren.size(); i += 1) {
			res.addAll(patternOriginalKeywordChildren.get(i).getVariables());
		}
		
		return res;
	}

	@Override
	public boolean next(){
		checkInitialized();

		if (!hasNext) {
			return false;
		}

		if (patternChildren.size() == 0) {
			hasNext = false;
			return true;
		}
		
		while (nextChild >= 0) {
			IMatchingResult nextPattern = patternChildren.get(nextChild);

			if (nextPattern.hasNext() && nextPattern.next()) {
				if (nextChild == patternChildren.size() - 1) {
					// We need to make sure if there are no
					// more possible matches for any of the tuple's fields, 
					// then the next call to hasNext() will definitely returns false.
					hasNext = false;
					for (int i = nextChild; i >= 0; i--) {
						IMatchingResult child = patternChildren.get(i);
            hasNext |= child.hasNext();
						if (patternConstructorType != null && !patternConstructorType.isNode() && hasNext) {
						  // This code should disappear as soon as we have a type checker. 
						  // Since constructors give us specific type contexts, an inferred type
						  // for a child pattern variable should get this specific type. A type
						  // checker would have inferred that already, but now we just push
						  // the information down to all children of the constructor.
						  child.updateType(patternConstructorType.getFieldType(i++));
						}
					}
					
					return true;
				}
				
				nextChild++;
			}
			else {
				nextChild--;

				if (nextChild >= 0) {
					for (int i = nextChild + 1; i < patternChildren.size(); i++) {
						IValue childValue = subject.get(i);
						IMatchingResult tailChild = patternChildren.get(i);
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
		int n = patternOriginalChildren.size();
		if(n == 1){
			return Names.fullName(qName) + "()";
		}
		StringBuilder res = new StringBuilder(Names.fullName(qName));
		res.append("(");
		String sep = "";
		
		for (int i = 0; i < patternOriginalChildren.size(); i++){
			IBooleanResult mp = patternOriginalChildren.get(i);
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		for(int i = 0; i < patternOriginalKeywordChildren.size(); i++){
			IBooleanResult mp = patternOriginalKeywordChildren.get(i);
			res.append(sep);
			sep = ", ";
			res.append(patternKeywordParameterNames.get(i));
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
    public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
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
    public INode replace(int first, int second, int end, IList repl) throws FactTypeUseException,
        IndexOutOfBoundsException {
      throw new UnsupportedOperationException();
    }

	@Override
	public IValue getKeywordArgumentValue(String name) {
		 throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasKeywordArguments() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getKeywordArgumentNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getKeywordIndex(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int positionalArity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends INode> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}		
	}
}


