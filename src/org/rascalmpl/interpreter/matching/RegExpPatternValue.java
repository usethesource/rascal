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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.semantics.dynamic.RegExpLiteral;
import org.rascalmpl.semantics.dynamic.RegExpLiteral.InterpolationElement;

public class RegExpPatternValue extends AbstractMatchingResult  {
	private final List<InterpolationElement> regexp;
	private Pattern pat;						// The Pattern resulting from compiling the regexp
	
	private List<String> patternVars;			// The variables occurring in the regexp
	private Matcher matcher;					// The actual regexp matcher
	String subject;								// Subject string to be matched
	private boolean initialized = false;		// Has matcher been initialized?
	private boolean firstMatch;				    // Is this the first match?
	private boolean hasNext;					// Are there more matches?
	
	private int start;							// start of last match in current subject
	private int end;							// end of last match in current subject
	
	
	
//	private static HashMap<String,Matcher> matcherCache = 
//		new HashMap<String,Matcher>();
	
	public RegExpPatternValue(IEvaluatorContext ctx, AbstractAST x, java.util.List<RegExpLiteral.InterpolationElement> regexp, List<String> patternVars) {
		super(ctx, x);
		
		this.regexp = regexp;
		this.patternVars = patternVars;
		initialized = false;
	}
	
	private String interpolate(IEvaluatorContext env) {
		StringBuilder b = new StringBuilder();
		
		for (RegExpLiteral.InterpolationElement elem : regexp) {
			b.append(elem.getString(env));
		}
		
		return b.toString();
	}
	
	

	@Override
	public Type getType(Environment ev, HashMap<String,IVarPattern> patternVars) {
		return tf.stringType();
	}

	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		
		if(!subject.getValue().getType().isSubtypeOf(tf.stringType())) {
			hasNext = false;
			return;
		}
		this.subject = ((IString) subject.getValue()).getValue();
		initialized = firstMatch = hasNext = true;
	
		try {
			String RegExpAsString = interpolate(ctx);
			pat = Pattern.compile(RegExpAsString);
		} catch (PatternSyntaxException e){
			throw new SyntaxError(e.getMessage(), ctx.getCurrentAST().getLocation());
		}
	}
	
	@Override
	public boolean hasNext() {
		return initialized && (firstMatch || hasNext);
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env) {
		return subjectType.equivalent(tf.stringType());
	}
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}
	
	private boolean findMatch(){
		while(matcher.find()){
			for (int nVar = 0; nVar < patternVars.size(); nVar++){
				java.lang.String binding = matcher.group(1+nVar);
				if(binding != null){
					java.lang.String name = patternVars.get(nVar);
					ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), ctx.getValueFactory().string(binding), ctx));
				}
			}
			start = matcher.start(0);
			end = matcher.end(0);
			return true;
		}
		hasNext = false;
		return false;
	}
	
	@Override
	public boolean next(){
		if(firstMatch){
			firstMatch = false;
// TODO Commented out caching code since it does not seem to help;
//			matcher = matcherCache.get(RegExpAsString);
//			if(matcher == null){
				matcher = pat.matcher(subject);
//				matcherCache.put(RegExpAsString, matcher);
//			} else
//				matcher.reset(subject);
			IString empty = ctx.getValueFactory().string("");
			
			// Initialize all pattern variables to ""
			for(String name : patternVars){
				if(!ctx.getCurrentEnvt().declareVariable(tf.stringType(), name))
					throw new RedeclaredVariableError(name, ctx.getCurrentAST());
				ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), empty, ctx));
			}
		}
		return findMatch();
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		List<IVarPattern> res = new LinkedList<IVarPattern>();
		for(String name : patternVars){
			res.add(new RegExpVar(name));
		}
		return res;
	}
	
	@Override
	public String toString(){
		return "RegExpPatternValue(" + regexp + ", " + patternVars + ")";
	}

	@Override
	public AbstractAST getAST() {
		return ctx.getCurrentAST();
	}
}
