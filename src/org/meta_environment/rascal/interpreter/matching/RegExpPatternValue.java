package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

public class RegExpPatternValue extends AbstractMatchingResult  {
//	private AbstractAST ast;					// The AST for this regexp
	private String RegExpAsString;				// The regexp represented as string
	private Pattern pat;						// The Pattern resulting from compiling the regexp

	private List<String> patternVars;			// The variables occurring in the regexp
	private Matcher matcher;					// The actual regexp matcher
	String subject;								// Subject string to be matched
	private boolean initialized = false;		// Has matcher been initialized?
	private boolean firstMatch;				// Is this the first match?
	private boolean hasNext;					// Are there more matches?
	
	private int start;							// start of last match in current subject
	private int end;							// end of last match in current subject
	private boolean firstTime;
	private boolean debug = true;
	
	public RegExpPatternValue(IValueFactory vf, IEvaluatorContext ctx, String s, List<String> patternVars) {
		super(vf, ctx);
		RegExpAsString = s;
		this.patternVars = patternVars;
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
			throw new SyntaxError(e.getMessage(), ctx.getCurrentAST().getLocation());
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
			java.util.List<java.lang.String> seen = new ArrayList<java.lang.String>();
			if(debug)System.err.println("# patternVars: " + patternVars.size());
			
			for (int nVar = 0; nVar < patternVars.size(); nVar++){
				java.lang.String name = patternVars.get(nVar);
				if(debug)System.err.println("---- name = " + name + ", nVar = " + nVar);
				if(debug)System.err.println("start=" + matcher.start(nVar) + ", end=" + matcher.end(nVar));
				
				java.lang.String binding = matcher.group(1+nVar);
				if(!seen.contains(name)){ /* first occurrence of var in pattern */
					if(debug)System.err.println("first occ of " + name + ", binding = " + binding);
					if(firstTime && !ctx.getCurrentEnvt().declareVariable(tf.stringType(), name)) {
						throw new RedeclaredVariableError(name, ctx.getCurrentAST());
					}
					ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), vf.string(binding), ctx));
				} else {                  /* repeated occurrence of var in pattern */
					Result<IValue> val = ctx.getCurrentEnvt().getVariable(name);
					if(debug)System.err.println("repeated occ of " + name + ", binding = " + binding);
					if (val != null && val.getValue() != null) {
						if(debug)System.err.println("previous val = " + val.getValue());
						if (!val.getType().isSubtypeOf(tf.stringType())) {
							throw new UnexpectedTypeError(tf.stringType(), val.getType(), ctx.getCurrentAST());
						}
						
						if(!val.equals(ResultFactory.makeResult(tf.stringType(), vf.string(binding), ctx), ctx).isTrue()) {
							matches = false;
							break;
						}
					}
				}
				seen.add(name);
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
	
	@Override
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
	
	@Override
	public java.util.List<String> getVariables(){
		return patternVars;
	}
	
	@Override
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + patternVars + ")";
	}

	public IValue toIValue(Environment env) {
		// TODO implement
		throw new NotYetImplemented(ctx.getCurrentAST());
	}

	public AbstractAST getAST() {
		return ctx.getCurrentAST();
	}
}
