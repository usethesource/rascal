package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;

class RegExpResult {
	String RegExpAsString;
	Character modifier = null;
	List<String> patternVars;
	Matcher matcher = null;
	
	RegExpResult(String s){
		RegExpAsString = s;
		modifier = null;
		patternVars = null;
	}
	
	RegExpResult(String s, Character mod, List<String> names){
		RegExpAsString = s;
		modifier = mod;
		patternVars = names;
	}
	
	public String toString(){
		return "RegExpResult(" + RegExpAsString + ", " + modifier + ", " + patternVars + ")";
	}
	
	public boolean matches(String s){
		Pattern pat = Pattern.compile(RegExpAsString);
		matcher = pat.matcher(s);
		return matcher.matches();
	}
	public Map<String,String> getBindings(){
		Map<String,String> map = new HashMap<String,String>();
		int k = 1;
		for(String nm : patternVars){
			map.put(nm, matcher.group(k));
			k++;
		}
		return map;
	}
}

public class RegExpEvaluator extends NullASTVisitor<RegExpResult> {
	
	
	public RegExpResult visitExpressionLiteral(Literal x) {
		System.err.println("visitExpressionLiteral: " + x.getLiteral());
		return x.getLiteral().accept(this);
	}
	
	public RegExpResult visitLiteralRegExp(RegExp x) {
		System.err.println("visitLiteralRegExp: " + x.getRegExpLiteral());
		return x.getRegExpLiteral().accept(this);
	}
	
	@Override
	public RegExpResult visitRegExpLexical(Lexical x) {
		System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpResult(x.getString());
	}
	
	@Override
	public RegExpResult visitRegExpLiteralLexical(
			org.meta_environment.rascal.ast.RegExpLiteral.Lexical x) {
		System.err.println("visitRegExpLiteralLexical: " + x.getString()
				);

		String subject = x.getString();
		Character modifier = null;
		
		int start = 1;
		int end = subject.length()-1;
		if(subject.charAt(end) != '/'){
			modifier = subject.charAt(end);
			end--;
		}
		
		Pattern replacePat = Pattern.compile("<([a-zA-Z0-9]+):([^>]*)>");
		Matcher m = replacePat.matcher(subject);
		
		String resultRegExp = "";
		List<String> names = new ArrayList<String>();

		while(m.find()){
			String varName = m.group(1);
			names.add(varName);
			resultRegExp += subject.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subject.substring(start, end);
		return new RegExpResult(resultRegExp, modifier, names);
	}
	
	@Override
	public RegExpResult visitRegExpModifierLexical(
			org.meta_environment.rascal.ast.RegExpModifier.Lexical x) {
		System.err.println("visitRegExpModifierLexical: " + x.getString());
		return new RegExpResult(x.getString());
	}
	
	@Override
	public RegExpResult visitNamedRegExpLexical(
			org.meta_environment.rascal.ast.NamedRegExp.Lexical x) {
		System.err.println("visitNamedRegExpLexical: " + x.getString());
		return new RegExpResult(x.getString());
	}
	
	@Override
	public RegExpResult visitNamedBackslashLexical(
			org.meta_environment.rascal.ast.NamedBackslash.Lexical x) {
		System.err.println("visitNamedBackslashLexical: " + x.getString());
		return new RegExpResult(x.getString());
	}
	
}
