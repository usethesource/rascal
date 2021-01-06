/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.matching.TypedVariablePattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.ArgumentMismatch;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFunction;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.utils.Cases.CaseBlock;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;


// TODO: this class is still too tightly coupled with evaluator
public class TraversalEvaluator {
	public enum DIRECTION  {BottomUp, TopDown}	// Parameters for traversing trees
	public enum FIXEDPOINT {Yes, No}
	public enum PROGRESS   {Continuing, Breaking}
	
	private final IEvaluator<Result<IValue>> eval;
	private static final TypeFactory tf = TypeFactory.getInstance();
	private final List<IValue> traversalContext;
	
	public TraversalEvaluator(IEvaluator<Result<IValue>> eval) {
		this.eval = eval;
		this.traversalContext = new Vector<IValue>();
	}
	
	public IList getContext() {
		IListWriter lw = eval.getValueFactory().listWriter();
		for (IValue v : this.traversalContext)
			lw.append(v);
		return lw.done().reverse();
	}

	public static class CaseBlockList {
		private java.util.List<CaseBlock> cases;
		private boolean allConcretePatternCases = true;
		private boolean hasRegexp = false;

		public CaseBlockList(java.util.List<CaseBlock> cases){
			this.cases =  cases;
			
			for (CaseBlock c : cases) {
				allConcretePatternCases &= c.allConcrete;
				hasRegexp |= c.hasRegExp;
			}
		}

		public boolean hasRegexp() {
			return hasRegexp;
		}
		
		public int length(){
			return cases.size();
		}
		
		public boolean hasAllConcretePatternCases(){
			return allConcretePatternCases;
		}

		public java.util.List<CaseBlock> getCases(){
			return cases;
		}
	}

	public IValue traverse(IValue subject, CaseBlockList casesOrRules, DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint) {
		return traverseOnce(subject, casesOrRules, direction, progress, fixedpoint, new TraverseResult());
	}

	private IValue traverseOnce(IValue subject, CaseBlockList casesOrRules, DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr){
		Type subjectType = subject.getType();
		IValue result = subject;
		this.traversalContext.add(subject);
		
		if (/*casesOrRules.hasRegexp()  && */ subjectType.isString()) {
			result = traverseStringOnce(subject, casesOrRules, tr);
			this.traversalContext.remove(this.traversalContext.size()-1);
			return result;
		}

		boolean hasMatched = false;
		boolean hasChanged = false;
		
		if (direction == DIRECTION.TopDown){
			IValue newTop = traverseTop(subject, casesOrRules, tr);

			if ((progress == PROGRESS.Breaking) && tr.matched) {
				this.traversalContext.remove(this.traversalContext.size()-1);
				return newTop;
			}
			else if (fixedpoint == FIXEDPOINT.Yes && tr.changed) {
				do {
					tr.changed = false;
					newTop = traverseTop(newTop, casesOrRules, tr);
				} while (tr.changed);
				tr.changed = true;
				subject = newTop;
			}
			else {
				subject = newTop;
			}
			
			hasMatched = tr.matched;
			hasChanged = tr.changed;
		}

		if (subjectType.isAbstractData() || RascalType.isNonterminal(subjectType) || RascalType.isReified(subjectType) ){
			result = traverseADTOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else if (subjectType.isNode()){
			result = traverseNodeOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else if(subjectType.isList()){
			result = traverseListOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else if(subjectType.isSet()){
			result = traverseSetOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else if (subjectType.isMap()) {
			result = traverseMapOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else if(subjectType.isTuple()){
			result = traverseTupleOnce(subject, casesOrRules, direction, progress, fixedpoint, tr);
		} else {
			result = subject;
		}
		
		if (direction == DIRECTION.TopDown) {
			tr.matched |= hasMatched;
			tr.changed |= hasChanged;
		}

		if (direction == DIRECTION.BottomUp) {
			if ((progress == PROGRESS.Breaking) && tr.matched) {
				this.traversalContext.remove(this.traversalContext.size()-1);
				return result;
			}

			hasMatched = tr.matched;
			hasChanged = tr.changed;
			tr.matched = false;
			tr.changed = false;
			result = traverseTop(result, casesOrRules, tr);
			
			if (tr.changed && fixedpoint == FIXEDPOINT.Yes) {
				do {
					tr.changed = false;
					tr.matched = false;
					result = traverseTop(result, casesOrRules, tr);
				} while (tr.changed);
				tr.changed = true;
				tr.matched = true;
			}
			
			tr.changed |= hasChanged;
			tr.matched |= hasMatched;
		}
		
		this.traversalContext.remove(this.traversalContext.size()-1);
		return result;
	}

	private IValue traverseStringOnce(IValue subject,
			CaseBlockList casesOrRules, TraverseResult tr) {
		boolean hasMatched = tr.matched;
		boolean hasChanged = tr.changed;
		tr.matched = false;
		tr.changed = false;
		IValue res = traverseString(subject, casesOrRules, tr);
		tr.matched |= hasMatched;
		tr.changed |= hasChanged;
		return res;
	}

	private IValue traverseTupleOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IValue result;
		ITuple tuple = (ITuple) subject;
		int arity = tuple.arity();
		IValue args[] = new IValue[arity];
		boolean hasMatched = false;
		boolean hasChanged = false;
		
		
		for (int i = 0; i < arity; i++){
			tr.changed = false;
			tr.matched = false;
			args[i] = traverseOnce(tuple.get(i), casesOrRules, direction, progress, fixedpoint, tr);
			hasMatched |= tr.matched;
			hasChanged |= tr.changed;
		}
		
		result = eval.getValueFactory().tuple(args);
		tr.changed = hasChanged;
		tr.matched = hasMatched;
		return result;
	}

	private IValue traverseADTOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IConstructor cons = (IConstructor)subject;
		
		Map<String, IValue> kwParams = null;
		if (cons.mayHaveKeywordParameters() && cons.asWithKeywordParameters().hasParameters()) {
			kwParams = new HashMap<>();
		}
		if (cons.arity() == 0 && kwParams == null) {
			return subject; // constants have no children to traverse into
		} 

		if (casesOrRules.hasAllConcretePatternCases() && cons.getType().isSubtypeOf(RascalValueFactory.Tree) && TreeAdapter.isChar((ITree) cons)) {
				return subject; // we dont traverse into the structure of literals and characters
		}

		IValue args[] = null;
		
		if (casesOrRules.hasAllConcretePatternCases() && cons.getType().isSubtypeOf(RascalValueFactory.Tree) && TreeAdapter.isAppl((ITree) cons)){
		    args = new IValue[cons.arity()];
			ITree tree = (ITree)cons;
			
			// Constructor is "appl": we are dealing with a syntax tree
			// - Lexical or literal are returned immediately

			if (TreeAdapter.isLexical(tree)|| TreeAdapter.isLiteral(tree)){
				return subject; // we dont traverse into the structure of literals, lexicals, and characters
			}
			
			// Otherwise:
			// - Copy prod node verbatim to result
			// - Only visit non-layout nodes in argument list
			args[0] = TreeAdapter.getProduction(tree);
			IList list = TreeAdapter.getArgs(tree);
			int len = list.length();

			if (len > 0) {
				IListWriter w = null;
				boolean hasChanged = false;
				boolean hasMatched = false;
				boolean isTop = TreeAdapter.isTop(tree);
				
				if (isTop) {
				    w = eval.getValueFactory().listWriter();
					w.append(list.get(0)); // copy layout before
					tr.changed = false;
					tr.matched = false;
					w.append(traverseOnce(list.get(1), casesOrRules, direction, progress, fixedpoint, tr));
					hasChanged |= tr.changed;
					hasMatched |= tr.matched;
					w.append(list.get(2)); // copy layout after
				} 
				else { 
					for (int i = 0; i < len; i++){
						IValue elem = list.get(i);
						if (i % 2 == 0) { // Recursion to all non-layout elements
							tr.changed = false;
							tr.matched = false;
							IValue newElem = traverseOnce(elem, casesOrRules, direction, progress, fixedpoint, tr);
							
							if (hasChanged) {
							    assert w != null;
							    w.append(newElem);
							}
							else {
							    if (tr.changed) {
							        // first time change
							        w = eval.getValueFactory().listWriter();
							        
							        // insert backlog into list
							        for (int j = 0; j < i; j++) {
							            w.append(list.get(j));
							        }
							        
							        w.append(newElem);
							    }
							    else {
							        // for now do nothing with the unchanged element
							    }
							}
                            
							hasChanged |= tr.changed;
							hasMatched |= tr.matched;
						} else if (hasChanged) { // Just copy layout elements
						   w.append(list.get(i));
						}
					}
				}
				
				tr.changed = hasChanged;
				tr.matched = hasMatched;
				args[1] = hasChanged ? w.done() : list;
			} else {
				args[1] = list;
			}
		} else {
			// Constructor is not "appl", or at least one of the patterns is not a concrete pattern
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (int i = 0; i < cons.arity(); i++){
				IValue child = cons.get(i);
				tr.matched = false;
				tr.changed = false;
				IValue newChild = traverseOnce(child, casesOrRules, direction, progress, fixedpoint, tr);
				
				if (hasChanged) {
				    assert args != null;
                    args[i] = newChild;
				}
				else {
				    if (tr.changed) {
				        // first change
				        args = new IValue[cons.arity()];
				        
				        // insert backlog
				        for (int j = 0; j < i; j++) {
				            args[j] = cons.get(j);
				        }
				        
				        args[i] = newChild;
				    }
				    else {
				        // do nothing with the unchanged element for now
				    }
				}
				
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			
			
			if (kwParams != null) {
				IWithKeywordParameters<? extends INode> consKw = cons.asWithKeywordParameters();
				for (String kwName : consKw.getParameterNames()) {
					IValue val = consKw.getParameter(kwName);
					tr.changed = false;
					tr.matched = false;
					IValue newVal = traverseOnce(val, casesOrRules, direction, progress, fixedpoint, tr);
					kwParams.put(kwName, newVal);
					
					if (!hasChanged && tr.changed) {
					    // first change, insert backlog
					    args = new IValue[cons.arity()];
					    for (int j = 0; j < cons.arity(); j++) {
					        args[j] = cons.get(j);
					    }
					}
					
					hasChanged |= tr.changed;
					hasMatched |= tr.matched;
				}
			}
			
			tr.matched = hasMatched;
			tr.changed = hasChanged;
		}

		if (tr.changed) {
		  IConstructor rcons;
		  
		  try {
		      assert args != null;
		      QualifiedName n = Names.toQualifiedName(cons.getType().getName(), cons.getName(), null);
		      rcons = (IConstructor) eval.call(n, kwParams != null ? kwParams : Collections.<String,IValue>emptyMap(), args);
		  }
		  catch (UndeclaredFunction | UndeclaredModule | ArgumentMismatch e) {
		    // This may happen when visiting data constructors dynamically which are not 
		    // defined in the current scope. For example, when data was serialized and the format
		    // has changed in the meantime, or when a generic function from a library calls visit.
			//
			// We issue a warning, because it is indicative of a bug
		    // and normalizing "rewrite rules" will not trigger at all, but we can gracefully continue 
		    // because we know what the tree looked like before we started visiting.
		    if (kwParams != null) {
		    	rcons = (IConstructor) eval.getValueFactory().constructor(cons.getConstructorType(),  args, kwParams);
		    }
		    else {
		    	rcons = (IConstructor) eval.getValueFactory().constructor(cons.getConstructorType(),  args);
		    }
		  }
		  catch (MatchFailed e) {
			  // this indicates that a nested rewrite has applied to replace a valid child with an invalid child
			  throw new UnexpectedType(cons.getConstructorType().getFieldTypes(), tf.tupleType(args), (AbstractAST) null);
		  }
		  
		  return rcons;
		}
		else {
			return subject;
		}
	}

	private IValue traverseMapOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IMap map = (IMap) subject;
		if(!map.isEmpty()){
			IMapWriter w = eval.getValueFactory().mapWriter();
			Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			while (iter.hasNext()) {
				Entry<IValue,IValue> entry = iter.next();
				tr.changed = false;
				tr.matched = false;
				IValue newKey = traverseOnce(entry.getKey(), casesOrRules, direction, progress, fixedpoint, tr);
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
				tr.changed = false;
				tr.matched = false;
				IValue newValue = traverseOnce(entry.getValue(), casesOrRules, direction, progress, fixedpoint, tr);
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
				w.put(newKey, newValue);
			}
			tr.changed = hasChanged;
			tr.matched = hasMatched;
			return w.done();
		} else {
			return subject;
		}
	}

	private IValue traverseSetOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		ISet set = (ISet) subject;
		if(!set.isEmpty()){
			ISetWriter w = eval.getValueFactory().setWriter();
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (IValue v : set) {
				tr.changed = false;
				tr.matched = false;
				w.insert(traverseOnce(v, casesOrRules, direction, progress, fixedpoint, tr));
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			
			tr.changed = hasChanged;
			tr.matched = hasMatched;
			return w.done();
		} else {
			return subject;
		}
	}

	private IValue traverseListOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IList list = (IList) subject;
		int len = list.length();
		if (len > 0) {
			IListWriter w = null;
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (int i = 0; i < len; i++){
				IValue elem = list.get(i);
				tr.changed = false;
				tr.matched = false;
				IValue newElem = traverseOnce(elem, casesOrRules, direction, progress, fixedpoint, tr);
				
				if (hasChanged) {
				    // continue adding new elements to the new list
				    assert w != null; // it's initialized the first time when tr.changed == true
				    w.append(newElem);
				}
				else { // nothing has changed yet...
				    if (tr.changed) {
				        // first time something changed. insert backlog into the writer
				        w = eval.getValueFactory().listWriter();
				        for (int j = 0; j < i; j++) {
				            w.append(list.get(j));
				        }
				        
				        // append the new element
				        w.append(newElem);
				    }
				    else {
				        // do nothing with the new element because nothing has changed.
				    }
				}
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			
			tr.changed = hasChanged;
			tr.matched = hasMatched;
			
			return tr.changed ? w.done() : list;
		} else {
			return subject;
		}
	}

	private IValue traverseNodeOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IValue result;
		INode node = (INode)subject;
		if (node.arity() == 0 && !(node.mayHaveKeywordParameters() && node.asWithKeywordParameters().hasParameters()) ){
			result = subject;
		} 
		else {
			IValue args[] = null;
			Map<String, IValue> kwParams = null;
			if (node.mayHaveKeywordParameters() && node.asWithKeywordParameters().hasParameters()) {
				kwParams = new HashMap<>();
			}
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (int i = 0; i < node.arity(); i++){
				IValue child = node.get(i);
				tr.changed = false;
				tr.matched = false;
				IValue newChild = traverseOnce(child, casesOrRules, direction, progress, fixedpoint, tr);
				
				if (hasChanged) {
				    // continue adding new elements to the array
				    assert args != null;
				    args[i] = newChild;
				}
				else {
				    if (tr.changed) {
				        // first time change
				        args = new IValue[node.arity()];
				        
				        // add the backlog to the array
				        for (int j = 0; j < i; j++) {
				            args[j] = node.get(j);
				        }
				        
				        args[i] = newChild;
				    }
				    else {
				        // nothing changed so do nothing yet with this child
				    }
				}
				
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			
			if (kwParams != null) {
				IWithKeywordParameters<? extends INode> nodeKw = node.asWithKeywordParameters();
				for (String kwName : nodeKw.getParameterNames()) {
					IValue val = nodeKw.getParameter(kwName);
					tr.changed = false;
					tr.matched = false;
					IValue newVal = traverseOnce(val, casesOrRules, direction, progress, fixedpoint, tr);
					kwParams.put(kwName, newVal);
					hasChanged |= tr.changed;
					hasMatched |= tr.matched;
				}
			}
			
			tr.changed = hasChanged;
			tr.matched = hasMatched;
			
			INode n = null;
			if (kwParams != null) {
			    if (!hasChanged) {
			        return node;
			    }
			    
			    if (args == null) {
			        // first time change
			        args = new IValue[node.arity()];

			        // add the backlog to the array
			        for (int j = 0; j < node.arity(); j++) {
			            args[j] = node.get(j);
			        }
			    }
                
				n = eval.getValueFactory().node(node.getName(), args, kwParams);
			}
			else {
				n = hasChanged ? eval.getValueFactory().node(node.getName(), args) : node;
			}
			
			result = n;
		}
		
		return result;
	}

	private IValue applyCases(IValue subject, CaseBlockList casesOrRules, TraverseResult tr) {
		for (CaseBlock cs : casesOrRules.getCases()) {
			Environment old = eval.getCurrentEnvt();
			AbstractAST prevAst = eval.getCurrentAST();
			
			try {
				eval.pushEnv();
				
				tr.matched = cs.matchAndEval(eval, makeResult(subject.getType(), subject, eval));
				
				if (tr.matched) {
					return subject;
				}
			}
			catch (Failure e) {
				// just continue with the next case
				continue;
			}
			finally {
				eval.unwind(old);
				eval.setCurrentAST(prevAst);
			}
		}
		
		return subject;
	}

	/*
	 * traverseTop: traverse the outermost symbol of the subject.
	 */
	public IValue traverseTop(IValue subject, CaseBlockList casesOrRules, TraverseResult tr) {
		try {
			return applyCases(subject, casesOrRules, tr);	
		} 
		catch (org.rascalmpl.interpreter.control_exceptions.Insert e) {
			tr.changed = true;
			tr.matched = true;
			Result<IValue> toBeInserted = e.getValue();
			
			if (!toBeInserted.getStaticType().isSubtypeOf(e.getStaticType())) {
				throw new UnexpectedType(e.getStaticType(), toBeInserted.getStaticType(), eval.getCurrentAST());
			}
			return e.getValue().getValue();
		}
	}
	
	/*
	 * traverseString implements a visit of a string subject by visiting subsequent substrings 
	 * subject[0,len], subject[1,len] ...and trying to match the cases. If a case matches
	 * the subject cursor is advanced by the length of the match and the matched substring may be replaced.
	 * At the end, the subject string including all replacements is returned.
	 * 
	 * Performance issue: we create a lot of garbage by producing all these substrings.
	 */
	public IValue traverseString(IValue subject, CaseBlockList casesOrRules, TraverseResult tr){
		String subjectString = ((IString) subject).getValue();
		int len = subjectString.length();
		int subjectCursor = 0;
		int subjectCursorForResult = 0;
		StringBuffer replacementString = null; 
		boolean hasMatched = false;
		boolean hasChanged = false;

		if (subjectString.length() == 0) {
		    return traverseTop(subject, casesOrRules, tr);
		}
		
		while (subjectCursor < len){
			//System.err.println("cursor = " + cursor);
			
			try {
				IString substring = eval.getValueFactory().string(subjectString.substring(subjectCursor, len));
				IValue subresult  = substring;
				tr.matched = false;
				tr.changed = false;
				
				// will throw insert or fail
				applyCases(subresult, casesOrRules, tr);
				
				hasMatched |= tr.matched;
				hasChanged |= tr.changed;
				
				subjectCursor++;
			} catch (org.rascalmpl.interpreter.control_exceptions.Insert e) {
				IValue repl = e.getValue().getValue();
				hasChanged = true;
				hasMatched = true;
				
				if (repl.getType().isString()){
					int start;
					int end;
					IBooleanResult lastPattern = e.getMatchPattern();
					if (lastPattern == null) {
						throw new ImplementationError("No last pattern known");
					}
					if (lastPattern instanceof RegExpPatternValue){
						start = ((RegExpPatternValue)lastPattern).getStart();
						end = ((RegExpPatternValue)lastPattern).getEnd();
					} 
					else if (lastPattern instanceof LiteralPattern || lastPattern instanceof TypedVariablePattern){
						start = 0;
						end = subjectString.length();
					} 
					else {
						throw new SyntaxError("Illegal pattern " + lastPattern + " in string visit", eval.getCurrentAST().getLocation());
					}
					
					// Create replacementString when this is the first replacement
					if (replacementString == null) {
						replacementString = new StringBuffer();
					}
					
					// Copy string before the match to the replacement string
					for (; subjectCursorForResult < subjectCursor + start; subjectCursorForResult++){
						replacementString.append(subjectString.charAt(subjectCursorForResult));
					}
					subjectCursorForResult = subjectCursor + end;
					// Copy replacement into replacement string
					replacementString.append(((IString)repl).getValue());

					tr.matched = true;
					tr.changed = true;
					subjectCursor += end;
				} else {
					throw new UnexpectedType(tf.stringType(),repl.getType(), eval.getCurrentAST());
				}
			}
		}
		
		tr.changed |= hasChanged;
		tr.matched |= hasMatched;

		if (!tr.changed) {
			return subject;
		}
		
		// Copy remaining characters of subject string into replacement string
		for (; subjectCursorForResult < len; subjectCursorForResult++){
			replacementString.append(subjectString.charAt(subjectCursorForResult));
		}
		
		return eval.getValueFactory().string(replacementString.toString());
	}
}
