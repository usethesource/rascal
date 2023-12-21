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

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.semantics.dynamic.RegExpLiteral;
import org.rascalmpl.semantics.dynamic.RegExpLiteral.InterpolationElement;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

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
	
	private boolean iWroteItMySelf;
	
	
//	private static HashMap<String,Matcher> matcherCache = 
//		new HashMap<String,Matcher>();
	
	public RegExpPatternValue(IEvaluatorContext ctx, AbstractAST x, java.util.List<RegExpLiteral.InterpolationElement> regexp, List<String> patternVars) {
		super(ctx, x);
		
		this.regexp = regexp;
		this.patternVars = patternVars;
		initialized = false;
		this.iWroteItMySelf = false;
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
		
		Type runType = subject.getValue().getType();
		
		if (runType.isSubtypeOf(tf.stringType())) {
			this.subject = ((IString) subject.getValue()).getValue();
		}
		else {
			hasNext = false;
			return;
		}
		
		initialized = firstMatch = hasNext = true;
		
		try {
			String RegExpAsString = interpolate(ctx);
			this.pat = Pattern.compile(RegExpAsString, Pattern.UNICODE_CHARACTER_CLASS);
			matcher = this.pat.matcher(((IString) subject.getValue()).getValue());
			IString empty = ctx.getValueFactory().string("");
			
			// Initialize all pattern variables to ""
			for (String name : patternVars) {
				ctx.getCurrentEnvt().declareAndStoreInferredInnerScopeVariable(name, ResultFactory.makeResult(tf.stringType(), empty, ctx));
			}
		} catch (PatternSyntaxException e) {
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
		if (firstMatch){
			firstMatch = false;
		}
		
		try {
			return findMatch();
		}
		catch (IndexOutOfBoundsException e) {
			throw new ImplementationError("Unexpected error in mapping to Java regex:" + interpolate(ctx), ctx.getCurrentAST().getLocation());
		}
	}
	
	@Override
	public List<IVarPattern> getVariables(){
		List<IVarPattern> res = new LinkedList<>();
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
	
	public boolean bindingInstance() {
		return this.iWroteItMySelf;
	}
}
