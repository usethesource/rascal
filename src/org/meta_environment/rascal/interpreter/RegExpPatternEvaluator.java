package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;
import org.meta_environment.rascal.interpreter.env.EvalResult;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;

class RegExpPatternValue implements MatchPattern {
	private String RegExpAsString;				// The regexp represented as string
	//private Character modifier;				// Optional modifier following the pattern
	private Pattern pat;						// The Pattern resulting from compiling the regexp

	private List<String> patternVars;			// The variables occurring in the regexp
	private HashMap<String, String> boundBeforeConstruction = new HashMap<String, String>();
												// The variable (and their value) that were already bound 
												// when the  pattern was constructed
	private Matcher matcher;					// The actual regexp matcher
	String subject;								// Subject string to be matched
	Evaluator ev;
	private boolean initialized = false;		// Has matcher been initialized?
	private boolean firstMatch;				// Is this the first match?
	private boolean hasNext;					// Are there more matches?
	
	private int start;							// start of last match in current subject
	private int end;							// end of last match in current subject
	
	RegExpPatternValue(String s){
		RegExpAsString = s;
	//	modifier = null;
		patternVars = null;
		initialized = false;
	}
	
	RegExpPatternValue(String s, Character mod, List<String> names){
		RegExpAsString = (mod == null) ? s : "(?" + mod + ")" + s;
	//	modifier = mod;
		patternVars = names;
		initialized = false;
		for(String name : names){
			EvalResult res = GlobalEnvironment.getInstance().getVariable(name);
			if((res != null) && (res.value != null)){
				if(!res.type.isStringType()){
					throw new RascalTypeError("Name " + name + " should have type string but has type " + res.type);
				}
				boundBeforeConstruction.put(name, ((IString)res.value).getValue());
				//System.err.println("bound before construction: " + name + ", " + res.value);
			}
		}
	}
	
	public Type getType(Evaluator ev) {
		return ev.tf.stringType();
	}

	public void initMatch(IValue subject, Evaluator ev) {
		if(!subject.getType().isStringType()){
			hasNext = false;
			return;
		}
		this.subject = ((IString) subject).getValue();
		this.ev = ev;
		initialized = firstMatch = hasNext = true;
	
		try {
			pat = Pattern.compile(RegExpAsString);
		} catch (PatternSyntaxException e){
			throw new RascalTypeError(e.getMessage());
		}
	}
	
	public boolean hasNext() {
		return initialized && (firstMatch || hasNext);
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
			for(String name : bindings.keySet()){
				String valBefore = boundBeforeConstruction.get(name);
				if(true){ // TODO: ??? valBefore == null){
					GlobalEnvironment.getInstance().top().storeVariable(name, ev.result(ev.vf.string(bindings.get(name))));
				} else {					
					if(!valBefore.equals(bindings.get(name))){
						matches = false;
						break;
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
		return findMatch();
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
	
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + patternVars + ")";
	}
}

public class RegExpPatternEvaluator extends NullASTVisitor<MatchPattern> {
	
	public boolean isRegExpPattern(org.meta_environment.rascal.ast.Expression pat){
		if(pat.isLiteral() && pat.getLiteral().isRegExp()){
			org.meta_environment.rascal.ast.Literal lit = ((Literal) pat).getLiteral();
			if(lit.isRegExp()){
				return true;
			}
		}
		return false;
	}	
	
	public MatchPattern visitExpressionLiteral(Literal x) {
		//System.err.println("visitExpressionLiteral: " + x.getLiteral());
		return x.getLiteral().accept(this);
	}
	
	public MatchPattern visitLiteralRegExp(RegExp x) {
		//System.err.println("visitLiteralRegExp: " + x.getRegExpLiteral());
		return x.getRegExpLiteral().accept(this);
	}
	
	@Override
	public MatchPattern visitRegExpLexical(Lexical x) {
		//System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpPatternValue(x.getString());
	}
	
	@Override
	public MatchPattern visitRegExpLiteralLexical(
			org.meta_environment.rascal.ast.RegExpLiteral.Lexical x) {
		//System.err.println("visitRegExpLiteralLexical: " + x.getString());

		String subjectPat = x.getString();
		Character modifier = null;
		
		if(subjectPat.charAt(0) != '/'){
			throw new RascalTypeError("Malformed Regular expression: " + subjectPat);
		}
		
		int start = 1;
		int end = subjectPat.length()-1;
		if(subjectPat.charAt(end) != '/'){
			modifier = subjectPat.charAt(end);
			end--;
		}
		if(subjectPat.charAt(end) != '/'){
			throw new RascalTypeError("Regular expression does not end with /");
		}
		
		/*
		 * Find all pattern variables. Take escaped \< characters into account.
		 */
		Pattern replacePat = Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)\\s*:\\s*([^>]*)>");
		Matcher m = replacePat.matcher(subjectPat);
		
		String resultRegExp = "";
		List<String> names = new ArrayList<String>();

		while(m.find()){
			String varName = m.group(1);
			names.add(varName);
			resultRegExp += subjectPat.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subjectPat.substring(start, end);
		/*
		 * Replace in the final regexp all occurrences of \< by <
		 */
		resultRegExp = resultRegExp.replaceAll("(\\\\<)", "<");
		//System.err.println("resultRegExp: " + resultRegExp);
		return new RegExpPatternValue(resultRegExp, modifier, names);
	}
}
