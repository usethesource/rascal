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

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.matching.TypedVariablePattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.ArgumentsMismatch;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFunction;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.Cases.CaseBlock;
import org.rascalmpl.values.uptr.TreeAdapter;


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
		
		if (/* casesOrRules.hasRegexp()  && */ subjectType.isString()) {
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

		if (subjectType.isAbstractData()){
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
		
		if (cons.arity() == 0) {
			return subject; // constants have no children to traverse into
		} 

		if (casesOrRules.hasAllConcretePatternCases() && TreeAdapter.isChar(cons)) {
				return subject; // we dont traverse into the structure of literals and characters
		}

		IValue args[] = new IValue[cons.arity()];

		if (casesOrRules.hasAllConcretePatternCases() && TreeAdapter.isAppl(cons)){
			// Constructor is "appl": we are dealing with a syntax tree
			// - Lexical or literal are returned immediately

			if (TreeAdapter.isLexical(cons)|| TreeAdapter.isLiteral(cons)){
				return subject; // we dont traverse into the structure of literals, lexicals, and characters
			}
			
			// Otherwise:
			// - Copy prod node verbatim to result
			// - Only visit non-layout nodes in argument list
			args[0] = cons.get(0);
			IList list = (IList) cons.get(1);
			int len = list.length();

			if (len > 0) {
				IListWriter w = eval.getValueFactory().listWriter(list.getType().getElementType());
				boolean hasChanged = false;
				boolean hasMatched = false;

				for (int i = 0; i < len; i++){
					IValue elem = list.get(i);
					if (i % 2 == 0) { // Recursion to all non-layout elements
						tr.changed = false;
						tr.matched = false;
						w.append(traverseOnce(elem, casesOrRules, direction, progress, fixedpoint, tr));
						hasChanged |= tr.changed;
						hasMatched |= tr.matched;
					} else { // Just copy layout elements
						w.append(list.get(i));
					}
				}
				tr.changed = hasChanged;
				tr.matched = hasMatched;
				args[1] = w.done();
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
				args[i] = traverseOnce(child, casesOrRules, direction, progress, fixedpoint, tr);
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			tr.matched = hasMatched;
			tr.changed = hasChanged;
		}

		if (tr.changed) {
		  IConstructor rcons;
		  
		  try {
		    QualifiedName n = Names.toQualifiedName(cons.getType().getName(), cons.getName(), null);
		    rcons = (IConstructor) eval.call(n, cons.asWithKeywordParameters().getParameters(), args);
		  }
		  catch (UndeclaredFunction | UndeclaredModule | ArgumentsMismatch e) {
		    // This may happen when visiting data constructors dynamically which are not 
		    // defined in the current scope. For example, when data was serialized and the format
		    // has changed in the meantime. We issue a warning, because it is indicative of a bug
		    // and normalizing "rewrite rules" will not trigger at all, but we can gracefully continue 
		    // because we know what the tree looked like before we started visiting.
		    eval.warning("In visit: " + e.getMessage(), eval.getCurrentAST().getLocation());
		    rcons = (IConstructor) eval.getValueFactory().constructor(cons.getConstructorType(),  args, cons.asWithKeywordParameters().getParameters());
		  }
		  
		  if (cons.asAnnotatable().hasAnnotations()) {
		    rcons = rcons.asAnnotatable().setAnnotations(cons.asAnnotatable().getAnnotations());
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
			IMapWriter w = eval.getValueFactory().mapWriter(map.getType());
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
			ISetWriter w = eval.getValueFactory().setWriter(set.getType().getElementType());
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
		if (len > 0){
			IListWriter w = eval.getValueFactory().listWriter(list.getType().getElementType());
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (int i = 0; i < len; i++){
				IValue elem = list.get(i);
				tr.changed = false;
				tr.matched = false;
				w.append(traverseOnce(elem, casesOrRules, direction, progress, fixedpoint, tr));
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

	private IValue traverseNodeOnce(IValue subject, CaseBlockList casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint, TraverseResult tr) {
		IValue result;
		INode node = (INode)subject;
		if (node.arity() == 0){
			result = subject;
		} 
		else {
			IValue args[] = new IValue[node.arity()];
			boolean hasChanged = false;
			boolean hasMatched = false;
			
			for (int i = 0; i < node.arity(); i++){
				IValue child = node.get(i);
				tr.changed = false;
				tr.matched = false;
				args[i] = traverseOnce(child, casesOrRules, direction, progress, fixedpoint, tr);
				hasChanged |= tr.changed;
				hasMatched |= tr.matched;
			}
			
			tr.changed = hasChanged;
			tr.matched = hasMatched;
			
			INode n = eval.getValueFactory().node(node.getName(), args);
			
			if (node.asAnnotatable().hasAnnotations()) {
				n = n.asAnnotatable().setAnnotations(node.asAnnotatable().getAnnotations());
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
			
			if (!toBeInserted.getType().isSubtypeOf(e.getStaticType())) {
				throw new UnexpectedType(e.getStaticType(), toBeInserted.getType(), eval.getCurrentAST());
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
