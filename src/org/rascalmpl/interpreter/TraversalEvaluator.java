package org.rascalmpl.interpreter;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

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
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Replacement;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.RewriteRule;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;


// TODO: this class is still too tightly coupled with evaluator
public class TraversalEvaluator {
	public enum DIRECTION  {BottomUp, TopDown}	// Parameters for traversing trees
	public enum FIXEDPOINT {Yes, No}
	public enum PROGRESS   {Continuing, Breaking}
	
	private final Evaluator eval;
	private static final TypeFactory tf = TypeFactory.getInstance();

	public TraversalEvaluator(Evaluator eval) {
		this.eval = eval;
	}
	
//	/*
//	 * TraverseResult contains the value returned by a traversal
//	 * and a changed flag that indicates whether the value itself or
//	 * any of its children has been changed during the traversal.
//	 */
//
//	// TODO: can this be put in the result hierarchy?
//	public class TraverseResult {
//		public final boolean matched;   // Some rule matched;
//		public final IValue value; 		// Result<IValue> of the 
//		public final boolean changed;   // Original subject has been changed
//
//		public TraverseResult(boolean someMatch, IValue value){
//			this.matched = someMatch;
//			this.value = value;
//			this.changed = false;
//		}
//
//		public TraverseResult(IValue value){
//			this.matched = false;
//			this.value = value;
//			this.changed = false;
//		}
//
//		public TraverseResult(IValue value, boolean changed){
//			this.matched = true;
//			this.value   = value;
//			this.changed = changed;
//		}
//		public TraverseResult(boolean someMatch, IValue value, boolean changed){
//			this.matched = someMatch;
//			this.value   = value;
//			this.changed = changed;
//		}
//	}

	/*
	 * CaseOrRule is the union of a Case or a Rule and allows the sharing of
	 * traversal code for both.
	 */
	public class CasesOrRules {
		private java.util.List<Case> cases;
		private java.util.List<RewriteRule> rules;
		private boolean allConcretePatternCases = true;

		@SuppressWarnings("unchecked")
		public CasesOrRules(java.util.List<?> casesOrRules){
			if(casesOrRules.get(0) instanceof Case){
				this.cases = (java.util.List<Case>) casesOrRules;
				for(Case c : cases){
					Type pt = c.getPatternWithAction().getPattern()._getType();
					
					if(pt == null || !(pt instanceof NonTerminalType)){
						allConcretePatternCases = false;
						break;
					}
				}
			} else {
				rules = (java.util.List<RewriteRule>)casesOrRules;
			}
		}

		public boolean hasRules(){
			return rules != null;
		}

		public boolean hasCases(){
			return cases != null;
		}

		public int length(){
			return (cases != null) ? cases.size() : rules.size();
		}
		
		public boolean hasAllConcretePatternCases(){
			return allConcretePatternCases;
		}

		public java.util.List<Case> getCases(){
			return cases;
		}
		public java.util.List<RewriteRule> getRules(){
			return rules;
		}
	}

	public TraverseResult traverse(IValue subject, CasesOrRules casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint) {
		//System.err.println("traverse: subject=" + subject + ", casesOrRules=" + casesOrRules);
		do {
			if (eval.isInterrupted()) throw new InterruptException(eval.getStackTrace());
			TraverseResult tr = traverseOnce(subject, casesOrRules, direction, progress);
			
			// TODO: what's going on? Will this go all the way back in the tree?
			if(fixedpoint == FIXEDPOINT.Yes){
				if (!tr.changed) {
					return tr;
				}
				subject = tr.value;
				TraverseResultFactory.freeTraverseResult(tr);
			} else {
				return tr;
			}
		} while (true);
	}

	private TraverseResult traverseOnce(IValue subject, CasesOrRules casesOrRules, 
			DIRECTION direction, PROGRESS progress){
		Type subjectType = subject.getType();
		boolean matched = false;
		boolean changed = false;
		IValue result = subject;

		//System.err.println("traverseOnce: " + subject + 
		//		        ",\n              type=" + subject.getType() + ", direction=" + direction + ", progress=" + progress);
		
		if(subjectType.isStringType()){
			return traverseString(subject, casesOrRules);
		}

		if(direction == DIRECTION.TopDown){
			TraverseResult tr = traverseTop(subjectType, subject, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			if((progress == PROGRESS.Breaking) && matched){
				return tr;
			}
			subject = tr.value;
			TraverseResultFactory.freeTraverseResult(tr);
		}

		if(subjectType.isAbstractDataType()){
			IConstructor cons = (IConstructor)subject;
			if(cons.arity() == 0){
				result = subject;
			} else {
				if(casesOrRules.hasAllConcretePatternCases() && TreeAdapter.isChar(cons))
					// Return char nodes as is
					return TraverseResultFactory.makeTraverseResult(subject);
				
				IValue args[] = new IValue[cons.arity()];
				if(casesOrRules.hasAllConcretePatternCases() && TreeAdapter.isAppl(cons)){
					// Constructor is "appl": we are dealing with a syntax tree
					// - Lexical or literal are returned immediately
										
					IConstructor symbol = SymbolAdapter.getRhs(TreeAdapter.getProduction(cons));
					if(SymbolAdapter.isLex(symbol) || SymbolAdapter.isLiteral(symbol)){
						//System.err.println("Layout or Literal found");
						return TraverseResultFactory.makeTraverseResult(subject);
					}
					// Otherwise:
					// - Copy prod node verbatim to result
					// - Only visit non-layout nodes in argument list

					args[0] = cons.get(0);
					IList list = (IList) cons.get(1);
					int len = list.length();
					if(len > 0){
						IListWriter w = list.getType().writer(eval.getValueFactory());
						
						for(int i = 0; i < len; i++){
							IValue elem = list.get(i);
							if(i % 2 == 0){
								// Visit all non-layout elements
								TraverseResult tr = traverseOnce(elem, casesOrRules, direction, progress);
								matched |= tr.matched;
								changed |= tr.changed;
								w.append(tr.value);
								TraverseResultFactory.freeTraverseResult(tr);
							} else
								// Just copy layout elements
								w.append(list.get(i));
						}
						args[1] = w.done();
					} else {
						args[1] = list;
					}
				} else {
					// Constructor is not "appl"
					for(int i = 0; i < cons.arity(); i++){
						IValue child = cons.get(i);
						TraverseResult tr = traverseOnce(child, casesOrRules, direction, progress);
						matched |= tr.matched;
						changed |= tr.changed;
						args[i] = tr.value;
						TraverseResultFactory.freeTraverseResult(tr);
					}
				}
				Type t = cons.getConstructorType();
				IConstructor rcons = eval.getValueFactory().constructor(t, args);
				if(cons.hasAnnotations()) rcons = rcons.setAnnotations(cons.getAnnotations());
				result = applyRules(t, rcons);
			}
		} else if(subjectType.isNodeType()){
			INode node = (INode)subject;
			if(node.arity() == 0){
				result = subject;
			} else {
				IValue args[] = new IValue[node.arity()];

				for(int i = 0; i < node.arity(); i++){
					IValue child = node.get(i);
					TraverseResult tr = traverseOnce(child, casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					args[i] = tr.value;
					TraverseResultFactory.freeTraverseResult(tr);
				}
				INode n = eval.getValueFactory().node(node.getName(), args);
				if(node.hasAnnotations()) n = n.setAnnotations(node.getAnnotations());
				result = applyRules(tf.nodeType(), n);
			}
		} else if(subjectType.isListType()){
			IList list = (IList) subject;
			int len = list.length();
			if(len > 0){
				IListWriter w = list.getType().writer(eval.getValueFactory());
				
				for(int i = 0; i < len; i++){
					IValue elem = list.get(i);
					TraverseResult tr = traverseOnce(elem, casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					w.append(tr.value);
					TraverseResultFactory.freeTraverseResult(tr);
				}
				result = w.done();
			} else {
				result = subject;
			}
		} else if(subjectType.isSetType()){
			ISet set = (ISet) subject;
			if(!set.isEmpty()){
				ISetWriter w = set.getType().writer(eval.getValueFactory());
				
				for (IValue v : set){
					TraverseResult tr = traverseOnce(v, casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					w.insert(tr.value);
					TraverseResultFactory.freeTraverseResult(tr);
				}
				result = w.done();
			} else {
				result = subject;
			}
		} else if (subjectType.isMapType()) {
			IMap map = (IMap) subject;
			if(!map.isEmpty()){
				IMapWriter w = map.getType().writer(eval.getValueFactory());
				Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
				
				while (iter.hasNext()) {
					Entry<IValue,IValue> entry = iter.next();
					TraverseResult tr = traverseOnce(entry.getKey(), casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					IValue newKey = tr.value;
					TraverseResultFactory.freeTraverseResult(tr);
					tr = traverseOnce(entry.getValue(), casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					IValue newValue = tr.value;
					TraverseResultFactory.freeTraverseResult(tr);
					w.put(newKey, newValue);
				}
				result = w.done();
			} else {
				result = subject;
			}
		} else if(subjectType.isTupleType()){
			ITuple tuple = (ITuple) subject;
			int arity = tuple.arity();
			IValue args[] = new IValue[arity];
			for(int i = 0; i < arity; i++){
				TraverseResult tr = traverseOnce(tuple.get(i), casesOrRules, direction, progress);
				matched |= tr.matched;
				changed |= tr.changed;
				args[i] = tr.value;
				TraverseResultFactory.freeTraverseResult(tr);
			}
			result = eval.getValueFactory().tuple(args);
		} else {
			result = subject;
		}

		if(direction == DIRECTION.BottomUp){
			//System.err.println("traverseOnce: bottomup: changed=" + changed);
			if((progress == PROGRESS.Breaking) && changed){
				return TraverseResultFactory.makeTraverseResult(matched, result, changed);
			}

			// TODO: I would expect the FIXEDPOINT stuff to do its work here, not outside the recursion, 
			// such that BottomUp+FixedPoint actually implements Innermost
			
			TraverseResult tr = traverseTop(subjectType, result, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			IValue val = tr.value;
			TraverseResultFactory.freeTraverseResult(tr);
			return TraverseResultFactory.makeTraverseResult(matched, val, changed);
		}
		return TraverseResultFactory.makeTraverseResult(matched,result,changed);
	}

	/**
	 * Replace an old subject by a new one as result of an insert statement.
	 */
	private TraverseResult replacement(Type type, IValue oldSubject, IValue newSubject){
		if(newSubject.getType().equivalent((oldSubject.getType())))
			return TraverseResultFactory.makeTraverseResult(true, newSubject, true);
		throw new UnexpectedTypeError(oldSubject.getType(), newSubject.getType(), eval.getCurrentAST());
	}

	/**
	 * Loop over all cases or rules.
	 */

	public TraverseResult applyCasesOrRules(Type type, IValue subject, CasesOrRules casesOrRules) {
		//System.err.println("applyCasesOrRules: " + subject.getValue());
		if(casesOrRules.hasCases()){
			for (Case cs : casesOrRules.getCases()) {
				Environment old = eval.getCurrentEnvt();
				AbstractAST prevAst = eval.getCurrentAST();
				
				try {
					eval.pushEnv();
					eval.setCurrentAST(cs);
					if (cs.isDefault()) {
						cs.getStatement().accept(eval);
						return new TraverseResult(true,subject);
					}

					TraverseResult tr = applyOneRule(type, subject, cs.getPatternWithAction());
					//System.err.println("applyCasesOrRules: matches");
					if(tr.matched){
						return tr;
					}
					
					TraverseResultFactory.freeTraverseResult(tr);
				}finally {
					eval.unwind(old);
					eval.setCurrentAST(prevAst);
				}
			}
		} else {
			//System.err.println("hasRules");
			for(RewriteRule rule : casesOrRules.getRules()){
				Environment oldEnv = eval.getCurrentEnvt();
				AbstractAST oldAST = eval.getCurrentAST();
				if (eval.isInterrupted()) throw new InterruptException(eval.getStackTrace());
				
				try {
					eval.setCurrentAST(rule.getRule());
					eval.setCurrentEnvt(rule.getEnvironment());
					eval.pushEnv();

					TraverseResult tr = applyOneRule(type, subject, rule.getRule());
					if(tr.matched){
						return tr;
					}
					
					TraverseResultFactory.freeTraverseResult(tr);
				}
				finally {
					eval.setCurrentAST(oldAST);
					eval.setCurrentEnvt(oldEnv);
				}
			}
		}
		//System.err.println("applyCasesorRules does not match");
		return TraverseResultFactory.makeTraverseResult(subject);
	}

	/*
	 * traverseTop: traverse the outermost symbol of the subject.
	 */

	public TraverseResult traverseTop(Type type, IValue subject, CasesOrRules casesOrRules) {
		//System.err.println("traversTop(" + subject + ")");
		try {
			return applyCasesOrRules(type, subject, casesOrRules);	
		} catch (org.rascalmpl.interpreter.control_exceptions.Insert e) {
			//System.err.println("traversTop(" + subject + "): replacement: " + e.getValue());
			return replacement(type, subject, e.getValue().getValue());
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
	public TraverseResult traverseString(IValue subject, CasesOrRules casesOrRules){
		String subjectString = ((IString) subject).getValue();
		int len = subjectString.length();
		boolean matched = false;
		boolean changed = false;
		int subjectCursor = 0;
		int subjectCursorForResult = 0;
		StringBuffer replacementString = null; 

		while(subjectCursor < len){
			//System.err.println("cursor = " + cursor);
			try {
				IString substring = eval.getValueFactory().string(subjectString.substring(subjectCursor, len));
				IValue subresult  = substring;
				TraverseResult tr = applyCasesOrRules(subresult.getType(), subresult, casesOrRules);
				matched |= tr.matched;
				changed |= tr.changed;
				TraverseResultFactory.freeTraverseResult(tr);
				//System.err.println("matched=" + matched + ", changed=" + changed);
				subjectCursor++;
			} catch (org.rascalmpl.interpreter.control_exceptions.Insert e){
				IValue repl = e.getValue().getValue();
				if(repl.getType().isStringType()){
					int start;
					int end;
					IBooleanResult lastPattern = e.getMatchPattern();
					if(lastPattern == null)
						throw new ImplementationError("No last pattern known");
					if(lastPattern instanceof RegExpPatternValue){
						start = ((RegExpPatternValue)lastPattern).getStart();
						end = ((RegExpPatternValue)lastPattern).getEnd();
					} else if(lastPattern instanceof LiteralPattern){
						start = 0;
						end = ((IString)repl).getValue().length();
					} else {
						throw new SyntaxError("Illegal pattern " + lastPattern + " in string visit", eval.getCurrentAST().getLocation());
					}
					
					// Create replacementString when this is the first replacement
					if(replacementString == null)
						replacementString = new StringBuffer();
					
					// Copy string before the match to the replacement string
					for(; subjectCursorForResult < subjectCursor + start; subjectCursorForResult++){
						replacementString.append(subjectString.charAt(subjectCursorForResult));
					}
					subjectCursorForResult = subjectCursor + end;
					// Copy replacement into replacement string
					replacementString.append(((IString)repl).getValue());

					matched = changed = true;
					subjectCursor += end;
				} else {
					throw new UnexpectedTypeError(tf.stringType(),repl.getType(), eval.getCurrentAST());
				}
			}
		}

		if(!changed){
			return TraverseResultFactory.makeTraverseResult(matched, subject, changed);
		}
		
		// Copy remaining characters of subject string into replacement string
		for(; subjectCursorForResult < len; subjectCursorForResult++){
			replacementString.append(subjectString.charAt(subjectCursorForResult));
		}
		return TraverseResultFactory.makeTraverseResult(matched, eval.getValueFactory().string(replacementString.toString()), changed);
	}

	/*
	 * applyOneRule: try to apply one rule to the subject.
	 */

	private TraverseResult applyOneRule(Type type, IValue subject, org.rascalmpl.ast.PatternWithAction rule) {

		//System.err.println("applyOneRule: subject=" + subject + ", type=" + subject.getType() + ", rule=" + rule);
		if (rule.isArbitrary()){
			if(eval.matchAndEval(makeResult(type, subject, eval), rule.getPattern(), rule.getStatement())) {
				return TraverseResultFactory.makeTraverseResult(true, subject);
			}
			/*
		} else if (rule.isGuarded()) {
			org.meta_environment.rascal.ast.Type tp = rule.getType();
			Type type = evalType(tp);
			rule = rule.getRule();
			if (subject.getType().isSubtypeOf(type) && 
				matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
			 */
		} else if (rule.isReplacing()) {
			//System.err.println("applyOneRule: subject=" + subject + ", replacing");
			Replacement repl = rule.getReplacement();
			java.util.List<Expression> conditions = repl.isConditional() ? repl.getConditions() : new ArrayList<Expression>();
			if(eval.matchEvalAndReplace(makeResult(type, subject, eval), rule.getPattern(), conditions, repl.getReplacementExpression())){
				//System.err.println("applyOneRule: matches");
				return TraverseResultFactory.makeTraverseResult(true, subject);
			}
		} else {
			throw new ImplementationError("Impossible case in rule");
		}
		return TraverseResultFactory.makeTraverseResult(subject);
	}
	
	public IValue applyRules(Type type, IValue value){
		Type typeToSearchFor = value.getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) value).getConstructorType();
		}

		java.util.List<RewriteRule> rules = eval.getHeap().getRules(typeToSearchFor);
		
		if (rules.size() > 0) {
			TraverseResult tr = traverseTop(type, value, new CasesOrRules(rules));
			IValue val = tr.value;
			TraverseResultFactory.freeTraverseResult(tr);
			return val;
		}

		return value;
	}
}
