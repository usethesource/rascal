package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

// TODO: add non-linear matching, and add string interpolation into the patterns.

public class RegExpPatternValue extends AbstractMatchingResult  {
	private AbstractAST ast;					// The AST for this regexp
	private String RegExpAsString;				// The regexp represented as string
	//private Character modifier;				// Optional modifier following the pattern
	private Pattern pat;						// The Pattern resulting from compiling the regexp

	private List<String> patternVars;			// The variables occurring in the regexp
	private HashSet<String> boundBeforeConstruction = new HashSet<String>();
												// The variable (and their value) that were already bound 
												// when the  pattern was constructed
	private Matcher matcher;					// The actual regexp matcher
	String subject;								// Subject string to be matched
	private boolean initialized = false;		// Has matcher been initialized?
	private boolean firstMatch;				// Is this the first match?
	private boolean hasNext;					// Are there more matches?
	
	private int start;							// start of last match in current subject
	private int end;							// end of last match in current subject
	private boolean firstTime;
	
	
	public RegExpPatternValue(IValueFactory vf, IEvaluatorContext ctx, String s){
		super(vf, ctx);
		RegExpAsString = s;
	//	modifier = null;
		patternVars = null;
		initialized = false;
		firstTime = true;
	}
	
	public RegExpPatternValue(IValueFactory vf, IEvaluatorContext ctx, AbstractAST ast, String s, Character mod, List<String> names) {
		super(vf, ctx);
		this.ast = ast;
		RegExpAsString = (mod == null) ? s : "(?" + mod + ")" + s;
		patternVars = names;
		initialized = false;
		firstTime = true;
	}
	
	@Override
	public Type getType(Environment ev) {
		return tf.stringType();
	}

	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		
		if(!subject.getType().isSubtypeOf(tf.stringType())) {
			hasNext = false;
			return;
		}
		this.subject = ((IString) subject.getValue()).getValue();
		initialized = firstMatch = hasNext = true;
	
		try {
			pat = Pattern.compile(RegExpAsString);
		} catch (PatternSyntaxException e){
			ISourceLocation loc = ast.getLocation();
			throw new SyntaxError(e.getMessage(), loc);
		}
		
		// do NOT reinit firstTime here!
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
			boolean matches = true;
			Map<String,String> bindings = getBindings();
			for (String name : bindings.keySet()) {
				if (firstTime) { 
					Result<IValue> val = ctx.getCurrentEnvt().getVariable(name);
					if (val != null && val.getValue() != null) {
						boundBeforeConstruction.add(name);
						
						if (!val.getType().isSubtypeOf(tf.stringType())) {
							throw new UnexpectedTypeError(tf.stringType(), val.getType(), ctx.getCurrentAST());
						}
						
						if(!val.equals(ResultFactory.makeResult(tf.stringType(), vf.string(bindings.get(name)), ctx), ctx).isTrue()) {
							matches = false;
							break;
						}
					}
					else {
						if (val != null) {
							if (!val.getType().isSubtypeOf(tf.stringType())) {
								throw new UnexpectedTypeError(tf.stringType(), val.getType(), ctx.getCurrentAST());
							}
						}
						
						ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), vf.string(bindings.get(name)), ctx));
					}
				}
				else {
					if (boundBeforeConstruction.contains(name)) {
						Result<IValue> val = ctx.getCurrentEnvt().getVariable(name);
						if (val == null) {
							throw new ImplementationError("??? we just said that it was bound before construction");
						}

						if (!val.getType().isSubtypeOf(tf.stringType())) {
							throw new UnexpectedTypeError(tf.stringType(), val.getType(), ctx.getCurrentAST());
						}

						if(!val.equals(ResultFactory.makeResult(tf.stringType(), vf.string(bindings.get(name)), ctx), ctx).isTrue()){
							matches = false;
							break;
						}
					}
					else {
						/*
						 * Note that regular expressions cannot be non-linear, e.g. duplicate occurrences 
						 * of variables are not allowed. Otherwise we would have to check here for the
						 * previous local value of the variable.
						 */
						ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), vf.string(bindings.get(name)), ctx));			
					}
				}
			}
			
			if(matches){
				start = matcher.start();
				end = matcher.end();
				return true;
			}
		}
		hasNext = false;
		start = end = -1;
		return false;
	}
	
	public boolean next(){
		if(firstMatch){
			firstMatch = false;
			matcher = pat.matcher(subject);
		}
		try {
			return findMatch();
		}
		finally {
			firstTime = false;
		}
	}
	
	public java.util.List<String> getVariables(){
		return patternVars;
	}
	
	private Map<String,String> getBindings(){
		Map<String,String> bindings = new HashMap<String,String>();
		int k = 1;
		for(String nm : patternVars){
			bindings.put(nm, matcher.group(k));
			k++;
		}
		return bindings;
	}
	
	@Override
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + patternVars + ")";
	}

	public IValue toIValue(Environment env) {
		// TODO implement
		throw new NotYetImplemented(ast);
	}

	public AbstractAST getAST() {
		return ast;
	}
}
