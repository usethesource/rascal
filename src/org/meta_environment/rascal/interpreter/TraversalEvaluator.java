package org.meta_environment.rascal.interpreter;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

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
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Case;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Replacement;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.control_exceptions.Failure;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.RewriteRule;
import org.meta_environment.rascal.interpreter.matching.IBooleanResult;
import org.meta_environment.rascal.interpreter.matching.IMatchingResult;
import org.meta_environment.rascal.interpreter.matching.LiteralPattern;
import org.meta_environment.rascal.interpreter.matching.RegExpPatternValue;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.SymbolAdapter;


// TODO: this class is still too tightly coupled with evaluator
public class TraversalEvaluator {
	public enum DIRECTION  {BottomUp, TopDown}	// Parameters for traversing trees
	public enum FIXEDPOINT {Yes, No}
	public enum PROGRESS   {Continuing, Breaking}
	
	private final Evaluator eval;
	private final IValueFactory vf;
	private final TypeFactory tf = TypeFactory.getInstance();

	public TraversalEvaluator(IValueFactory vf, Evaluator eval) {
		this.vf = vf;
		this.eval = eval;
	}
	
	/*
	 * StringReplacement represents a single replacement in the subject string.
	 */

	public class StringReplacement {
		int start;
		int end;
		String replacement;

		public StringReplacement(int start, int end, String repl){
			this.start = start;
			this.end = end;
			replacement = repl;
		}

		@Override
		public String toString(){
			return "StringReplacement(" + start + ", " + end + ", " + replacement + ")";
		}
	}
	
	/*
	 * TraverseResult contains the value returned by a traversal
	 * and a changed flag that indicates whether the value itself or
	 * any of its children has been changed during the traversal.
	 */

	// TODO: can this be put in the result hierarchy?
	public class TraverseResult {
		public boolean matched;   // Some rule matched;
		public Result<IValue> value; 		// Result<IValue> of the 
		public boolean changed;   // Original subject has been changed

		public TraverseResult(boolean someMatch, Result<IValue> value){
			this.matched = someMatch;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(Result<IValue> value){
			this.matched = false;
			this.value = value;
			this.changed = false;
		}

		public TraverseResult(Result<IValue> value, boolean changed){
			this.matched = true;
			this.value   = value;
			this.changed = changed;
		}
		public TraverseResult(boolean someMatch, Result<IValue> value, boolean changed){
			this.matched = someMatch;
			this.value   = value;
			this.changed = changed;
		}
	}

	/*
	 * CaseOrRule is the union of a Case or a Rule and allows the sharing of
	 * traversal code for both.
	 */
	public class CasesOrRules {
		private java.util.List<Case> cases;
		private java.util.List<RewriteRule> rules;

		@SuppressWarnings("unchecked")
		public CasesOrRules(java.util.List<?> casesOrRules){
			if(casesOrRules.get(0) instanceof Case){
				this.cases = (java.util.List<Case>) casesOrRules;
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

		public java.util.List<Case> getCases(){
			return cases;
		}
		public java.util.List<RewriteRule> getRules(){
			return rules;
		}
	}

	public TraverseResult traverse(Result<IValue> subject, CasesOrRules casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint) {
		//System.err.println("traverse: subject=" + subject + ", casesOrRules=" + casesOrRules);
		do {
			TraverseResult tr = traverseOnce(subject, casesOrRules, direction, progress);
			if(fixedpoint == FIXEDPOINT.Yes){
				if (!tr.changed) {
					return tr;
				}
				subject = tr.value;
			} else {
				return tr;
			}
		} while (true);
	}

	private TraverseResult traverseOnce(Result<IValue> subject, CasesOrRules casesOrRules, 
			DIRECTION direction, 
			PROGRESS progress){
		Type subjectType = subject.getType();
		boolean matched = false;
		boolean changed = false;
		Result<IValue> result = subject;

		//System.err.println("traverseOnce: " + subject + ", type=" + subject.getType());
		if(subjectType.isStringType()){
			return traverseString(subject, casesOrRules);
		}

		if(direction == DIRECTION.TopDown){
			TraverseResult tr = traverseTop(subject, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			if((progress == PROGRESS.Breaking) && changed){
				return tr;
			}
			subject = tr.value;
		}

		if(subjectType.isAbstractDataType()){
			IConstructor cons = (IConstructor)subject.getValue();
			if(cons.arity() == 0){
				result = subject;
			} else {
				IValue args[] = new IValue[cons.arity()];

				for(int i = 0; i < cons.arity(); i++){
					IValue child = cons.get(i);
//					Type childType = cons.getConstructorType().getFieldType(i);
					TraverseResult tr = traverseOnce(ResultFactory.makeResult(child.getType(), child, eval), casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					args[i] = tr.value.getValue();
				}
				IConstructor rcons = vf.constructor(cons.getConstructorType(), args);
				result = applyRules(makeResult(subjectType, rcons.setAnnotations(cons.getAnnotations()), eval));
			}
		} else
			if(subjectType.isNodeType()){
				INode node = (INode)subject.getValue();
				if(node.arity() == 0){
					result = subject;
				} else {
					IValue args[] = new IValue[node.arity()];

					for(int i = 0; i < node.arity(); i++){
						IValue child = node.get(i);
						TraverseResult tr = traverseOnce(ResultFactory.makeResult(child.getType(), child, eval), casesOrRules, direction, progress);
						matched |= tr.matched;
						changed |= tr.changed;
						args[i] = tr.value.getValue();
					}
					result = applyRules(makeResult(tf.nodeType(), vf.node(node.getName(), args).setAnnotations(node.getAnnotations()), eval));
				}
			} else
				if(subjectType.isListType()){
					IList list = (IList) subject.getValue();
					int len = list.length();
					if(len > 0){
						IListWriter w = list.getType().writer(vf);
						Type elemType = list.getType().getElementType();
						
						for(int i = 0; i < len; i++){
							IValue elem = list.get(i);
							TraverseResult tr = traverseOnce(ResultFactory.makeResult(elemType, elem, eval), casesOrRules, direction, progress);
							matched |= tr.matched;
							changed |= tr.changed;
							w.append(tr.value.getValue());
						}
						result = makeResult(subjectType, w.done(), eval);
					} else {
						result = subject;
					}
				} else 
					if(subjectType.isSetType()){
						ISet set = (ISet) subject.getValue();
						if(!set.isEmpty()){
							ISetWriter w = set.getType().writer(vf);
							Type elemType = set.getType().getElementType();
							
							for (IValue v : set){
								TraverseResult tr = traverseOnce(ResultFactory.makeResult(elemType, v, eval), casesOrRules, direction, progress);
								matched |= tr.matched;
								changed |= tr.changed;
								w.insert(tr.value.getValue());
							}
							result = makeResult(subjectType, w.done(), eval);
						} else {
							result = subject;
						}
					} else
						if (subjectType.isMapType()) {
							IMap map = (IMap) subject.getValue();
							if(!map.isEmpty()){
								IMapWriter w = map.getType().writer(vf);
								Iterator<Entry<IValue,IValue>> iter = map.entryIterator();
								Type keyType = map.getKeyType();
								Type valueType = map.getValueType();

								while (iter.hasNext()) {
									Entry<IValue,IValue> entry = iter.next();
									TraverseResult tr = traverseOnce(ResultFactory.makeResult(keyType, entry.getKey(), eval), casesOrRules, direction, progress);
									matched |= tr.matched;
									changed |= tr.changed;
									IValue newKey = tr.value.getValue();
									tr = traverseOnce(ResultFactory.makeResult(valueType, entry.getValue(), eval), casesOrRules, direction, progress);
									matched |= tr.matched;
									changed |= tr.changed;
									IValue newValue = tr.value.getValue();
									w.put(newKey, newValue);
								}
								result = makeResult(subjectType, w.done(), eval);
							} else {
								result = subject;
							}
						} else
							if(subjectType.isTupleType()){
								ITuple tuple = (ITuple) subject.getValue();
								int arity = tuple.arity();
								IValue args[] = new IValue[arity];
								for(int i = 0; i < arity; i++){
									Type fieldType = subjectType.getFieldType(i);
									TraverseResult tr = traverseOnce(ResultFactory.makeResult(fieldType, tuple.get(i), eval), casesOrRules, direction, progress);
									matched |= tr.matched;
									changed |= tr.changed;
									args[i] = tr.value.getValue();
								}
								result = makeResult(subjectType, vf.tuple(args), eval);
							} else {
								result = subject;
							}

		if(direction == DIRECTION.BottomUp){
			
			if((progress == PROGRESS.Breaking) && changed){
				return new TraverseResult(matched, result, changed);
			}

			TraverseResult tr = traverseTop(result, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			return new TraverseResult(matched, tr.value, changed);
		}
		return new TraverseResult(matched,result,changed);
	}

	/**
	 * Replace an old subject by a new one as result of an insert statement.
	 */
	private TraverseResult replacement(Result<IValue> oldSubject, Result<IValue> newSubject){
		if(newSubject.getType().equivalent((oldSubject.getType())))
			return new TraverseResult(true, newSubject, true);
		throw new UnexpectedTypeError(oldSubject.getType(), newSubject.getType(), eval.getCurrentAST());
	}

	/**
	 * Loop over all cases or rules.
	 */

	public TraverseResult applyCasesOrRules(Result<IValue> subject, CasesOrRules casesOrRules) {
		if(casesOrRules.hasCases()){
			for (Case cs : casesOrRules.getCases()) {
				eval.setCurrentAST(cs);
				if (cs.isDefault()) {
					cs.getStatement().accept(eval);
					return new TraverseResult(true,subject);
				}

				TraverseResult tr = applyOneRule(subject, cs.getPatternWithAction());
				if(tr.matched){
					return tr;
				}
			}
		} else {
			//System.err.println("hasRules");

			for(RewriteRule rule : casesOrRules.getRules()){
				eval.setCurrentAST(rule.getRule());
				Environment oldEnv = eval.getCurrentEnvt();
				eval.setCurrentEnvt(rule.getEnvironment());
				try {
					TraverseResult tr = applyOneRule(subject, rule.getRule());
					if(tr.matched){
						return tr;
					}
				}
				finally {
					eval.setCurrentEnvt(oldEnv);
				}
			}
		}
		//System.err.println("applyCasesorRules does not match");
		return new TraverseResult(subject);
	}

	/*
	 * traverseTop: traverse the outermost symbol of the subject.
	 */

	public TraverseResult traverseTop(Result<IValue> subject, CasesOrRules casesOrRules) {
		//System.err.println("traversTop(" + subject + ")");
		try {
			return applyCasesOrRules(subject, casesOrRules);	
		} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e) {

			return replacement(subject, e.getValue());
		}
	}
	
	/*
	 * traverString implements a visit of a string subject and applies the set of cases
	 * for all substrings of the subject. At the end, all replacements are applied and the modified
	 * subject is returned.
	 */

	// TODO: decouple visiting of strings from case statement
	public TraverseResult traverseString(Result<IValue> subject, CasesOrRules casesOrRules){
		String subjectString = ((IString) subject.getValue()).getValue();
		int len = subjectString.length();
		java.util.List<StringReplacement> replacements = new ArrayList<StringReplacement>();
		boolean matched = false;
		boolean changed = false;
		int cursor = 0;

		Case cs = (Case) singleCase(casesOrRules);

//		PatternEvaluator re = new PatternEvaluator(vf, this, getCurrentEnvt());
		if(cs != null && cs.isPatternWithAction() && cs.getPatternWithAction().getPattern().isLiteral() && cs.getPatternWithAction().getPattern().getLiteral().isRegExp()){
			/*
			 * In the frequently occurring case that there is one case with a regexp as pattern,
			 * we can delegate all the work to the regexp matcher.
			 */
			org.meta_environment.rascal.ast.PatternWithAction rule = cs.getPatternWithAction();

			Expression patexp = rule.getPattern();
			IMatchingResult mp = patexp.accept(eval.makePatternEvaluator(patexp));
			mp.initMatch(subject);
			Environment old = eval.getCurrentEnvt();
			try {
				while(mp.hasNext()){
					if(mp.next()){
						try {
							if(rule.isReplacing()){
								Replacement repl = rule.getReplacement();
								boolean trueConditions = true;
								if(repl.isConditional()){
									for(Expression cond : repl.getConditions()){
										Result<IValue> res = cond.accept(eval);
										if(!res.isTrue()){         // TODO: How about alternatives?
											trueConditions = false;
											break;
										}
									}
								}
								if(trueConditions){
									throw new org.meta_environment.rascal.interpreter.control_exceptions.Insert(repl.getReplacementExpression().accept(eval), mp);
								}

							} else {
								rule.getStatement().accept(eval);
							}
						} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
							changed = true;
							IValue repl = e.getValue().getValue();
							if(repl.getType().isStringType()){
								int start = ((RegExpPatternValue) mp).getStart();
								int end = ((RegExpPatternValue) mp).getEnd();
								replacements.add(new StringReplacement(start, end, ((IString)repl).getValue()));
							} else {
								throw new UnexpectedTypeError(tf.stringType(),repl.getType(), rule);
							}
						} catch (Failure e){
							//System.err.println("failure occurred");
						}
					}
				}
			} finally {
				eval.unwind(old);
			}
		} else {
			/*
			 * In all other cases we generate subsequent substrings subject[0,len], subject[1,len] ...
			 * and try to match all the cases.
			 * Performance issue: we create a lot of garbage by producing all these substrings.
			 */

			while(cursor < len){
				//System.err.println("cursor = " + cursor);
				try {
					IString substring = vf.string(subjectString.substring(cursor, len));
					Result<IValue> subresult  = ResultFactory.makeResult(tf.stringType(), substring, eval);
					TraverseResult tr = applyCasesOrRules(subresult, casesOrRules);
					matched |= tr.matched;
					changed |= tr.changed;
					//System.err.println("matched=" + matched + ", changed=" + changed);
					cursor++;
				} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
					IValue repl = e.getValue().getValue();
					if(repl.getType().isStringType()){
						int start;
						int end;
						IBooleanResult lastPattern = e.getMatchPattern();
						if(lastPattern == null)
							throw new ImplementationError("no last pattern known");
						if(lastPattern instanceof RegExpPatternValue){
							start = ((RegExpPatternValue)lastPattern).getStart();
							end = ((RegExpPatternValue)lastPattern).getEnd();
						} else if(lastPattern instanceof LiteralPattern){
							start = 0;
							end = ((IString)repl).getValue().length();
						} else {
							throw new SyntaxError("Illegal pattern " + lastPattern + " in string visit", eval.getCurrentAST().getLocation());
						}

						replacements.add(new StringReplacement(cursor + start, cursor + end, ((IString)repl).getValue()));
						matched = changed = true;
						cursor += end;
					} else {
						throw new UnexpectedTypeError(tf.stringType(),repl.getType(), eval.getCurrentAST());
					}
				}
			}
		}

		if(!changed){
			return new TraverseResult(matched, subject, changed);
		}
		/*
		 * The replacements are now known. Create a version of the subject with all replacement applied.
		 */
		StringBuffer res = new StringBuffer();
		cursor = 0;
		for(StringReplacement sr : replacements){
			for( ;cursor < sr.start; cursor++){
				res.append(subjectString.charAt(cursor));
			}
			cursor = sr.end;
			res.append(sr.replacement);
		}
		for( ; cursor < len; cursor++){
			res.append(subjectString.charAt(cursor));
		}

		return new TraverseResult(matched, ResultFactory.makeResult(tf.stringType(), vf.string(res.toString()), eval), changed);
	}

	/*
	 * applyOneRule: try to apply one rule to the subject.
	 */

	private TraverseResult applyOneRule(Result<IValue> subject,
			org.meta_environment.rascal.ast.PatternWithAction rule) {

		//System.err.println("applyOneRule: subject=" + subject + ", type=" + subject.getType() + ", rule=" + rule);

		if (rule.isArbitrary()){
			if(eval.matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
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
			Replacement repl = rule.getReplacement();
			java.util.List<Expression> conditions = repl.isConditional() ? repl.getConditions() : new ArrayList<Expression>();
			if(eval.matchEvalAndReplace(subject, rule.getPattern(), conditions, repl.getReplacementExpression())){
				return new TraverseResult(true, subject);
			}
		} else {
			throw new ImplementationError("Impossible case in rule");
		}
		return new TraverseResult(subject);
	}
	
	public Result<IValue> applyRules(Result<IValue> v, IEvaluatorContext ctx) {
		//System.err.println("applyRules(" + v + ")");
		// we search using the run-time type of a value
		
		Type typeToSearchFor = v.getValue().getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) v.getValue()).getConstructorType();
		}

		java.util.List<RewriteRule> rules = ctx.getHeap().getRules(typeToSearchFor);
		
		if (rules.size() > 0) {
			TraverseResult tr = traverseTop(v, new CasesOrRules(rules));
			return tr.value;
		}

		return v;
	}

	Result<IValue> applyRules(Result<IValue> v) {
		//System.err.println("applyRules(" + v + ")");
		// we search using the run-time type of a value
		Type typeToSearchFor = v.getValue().getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) v.getValue()).getConstructorType();
		}

		java.util.List<RewriteRule> rules = eval.getHeap().getRules(typeToSearchFor);
		if(rules.isEmpty()){
			// weird side-effect but it works
			declareConcreteSyntaxType(v.getValue());
			return v;
		}

		TraverseResult tr = traverseTop(v, new CasesOrRules(rules));
		/* innermost is achieved by repeated applications of applyRules
		 * when intermediate results are produced.
		 */

		// weird side-effect but it works
		declareConcreteSyntaxType(tr.value.getValue());

		return tr.value;
	}

	void declareConcreteSyntaxType(IValue value) {
		// if somebody constructs a sort, then this implicitly declares
		// a corresponding Rascal type.
		Type type = value.getType();

		if (type == Factory.Symbol) {
			IConstructor symbol = (IConstructor) value;

			if (symbol.getConstructorType() == Factory.Symbol_Sort) {
				Environment root = eval.getCurrentEnvt().getRoot();
				root.concreteSyntaxType(new SymbolAdapter(symbol).getName(), 
						(IConstructor) Factory.Symbol_Cf.make(vf, value));
			}
		}

	}

	/*
	 *  singleCase returns a single case or rules if casesOrRueles has length 1 and null otherwise.
	 */

	private Object singleCase(CasesOrRules casesOrRules){
		if(casesOrRules.length() == 1){
			if(casesOrRules.hasCases()){
				return casesOrRules.getCases().get(0);
			}

			return casesOrRules.getRules().get(0);
		}
		return null;
	}


}


