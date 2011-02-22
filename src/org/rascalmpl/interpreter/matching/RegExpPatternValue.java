package org.rascalmpl.interpreter.matching;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

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

public class RegExpPatternValue extends AbstractMatchingResult  {
	private String RegExpAsString;				// The regexp represented as string
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
	
	public RegExpPatternValue(IEvaluatorContext ctx,  AbstractAST x, String s, List<String> patternVars) {
		super(ctx, x);
		RegExpAsString = removeRascalSpecificEscapes(s);
		this.patternVars = patternVars;
		initialized = false;
	}
	
	private String removeRascalSpecificEscapes(String s) {
		StringBuilder b = new StringBuilder(s.length());
		char[] chars = s.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\\' && i + 1 < chars.length) {
				switch(chars[++i]) {
				case '>' : b.append('>'); continue;
				case '<' : b.append('<'); continue;
				default: // leave the other escapes as-is
					b.append('\\');
					b.append(chars[i]);
				}
			}
			else {
				b.append(chars[i]);
			}
		}
		
		return b.toString();
	}

	@Override
	public Type getType(Environment ev) {
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
	public java.util.List<String> getVariables(){
		return patternVars;
	}
	
	@Override
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + patternVars + ")";
	}

	@Override
	public AbstractAST getAST() {
		return ctx.getCurrentAST();
	}
}
