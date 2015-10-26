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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class TuplePattern extends AbstractMatchingResult {
	private List<IMatchingResult> children;
	private ITuple treeSubject;
	private final TypeFactory tf = TypeFactory.getInstance();
	private int nextChild;
	private Type type;
	
	public TuplePattern(IEvaluatorContext ctx, Expression x, List<IMatchingResult> list){
		super(ctx, x);
		
		this.children = list;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		hasNext = false;
		
		if (!subject.getValue().getType().isTuple()) {
			return;
		}
		treeSubject = (ITuple) subject.getValue();

		if (treeSubject.arity() != children.size()){
			return;
		}
		
		hasNext = true;
		
		for (int i = 0; i < children.size(); i += 1){
			IValue childValue = treeSubject.get(i);
			IMatchingResult child = children.get(i);
			child.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
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
			Type fieldTypes[] = new Type[children.size()];
			for(int i = 0; i < children.size(); i++){
				fieldTypes[i] = children.get(i).getType(env, patternVars);
				patternVars = merge(patternVars, children.get(i).getVariables());
			}
			type = tf.tupleType(fieldTypes);
		}
		return type;
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();
		for (int i = 0; i < children.size(); i += 1) {
			res.addAll(children.get(i).getVariables());
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
						IValue childValue = treeSubject.get(i);
						IMatchingResult tailChild = children.get(i);
						tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
					}
				}
			}
		}
	    hasNext = false;
	    return false;
	}
	
	public List<IMatchingResult> getChildren(){
		return children;
	}
	
	@Override
	public String toString(){
		StringBuilder res = new StringBuilder();
		res.append("<");
		String sep = "";
		for (IBooleanResult mp : children){
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		res.append(">");
		
		return res.toString();
	}
}
