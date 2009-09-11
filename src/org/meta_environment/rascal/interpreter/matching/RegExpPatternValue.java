package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

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
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;

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
	
	public RegExpPatternValue(IValueFactory vf, IEvaluatorContext ctx, String s, List<String> patternVars) {
		super(vf, ctx);
		RegExpAsString = s;
		this.patternVars = patternVars;
		initialized = false;
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
			for (int nVar = 0; nVar < patternVars.size(); nVar++){
				java.lang.String name = patternVars.get(nVar);				
				java.lang.String binding = matcher.group(1+nVar);
				ctx.getCurrentEnvt().storeVariable(name, makeResult(tf.stringType(), vf.string(binding), ctx));
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
			matcher = pat.matcher(subject);
			IString empty = vf.string("");
			
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
	public java.util.List<String> getVariables(){
		return patternVars;
	}
	
	@Override
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + patternVars + ")";
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO implement
		throw new NotYetImplemented(ctx.getCurrentAST());
	}

	@Override
	public AbstractAST getAST() {
		return ctx.getCurrentAST();
	}
}
