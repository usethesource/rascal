package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;

class RegExpValue {
	String RegExpAsString;
	Character modifier = null;
	List<Name> patternVars;
	Matcher matcher = null;
	
	RegExpValue(String s){
		RegExpAsString = s;
		modifier = null;
		patternVars = null;
	}
	
	RegExpValue(String s, Character mod, List<Name> names){
		RegExpAsString = s;
		modifier = mod;
		patternVars = names;
	}
	
	public String toString(){
		return "RegExpValue(" + RegExpAsString + ", " + modifier + ", " + patternVars + ")";
	}
	
	public boolean matches(String s){
		try {
			Pattern pat = Pattern.compile(RegExpAsString);
			matcher = pat.matcher(s);
			return matcher.matches();
		} catch (PatternSyntaxException e){
			throw new RascalTypeError(e.getMessage());
		}
	}
	public Map<Name,String> getBindings(){
		Map<Name,String> map = new HashMap<Name,String>();
		int k = 1;
		for(Name nm : patternVars){
			map.put(nm, matcher.group(k));
			k++;
		}
		return map;
	}
}

public class RegExpEvaluator extends NullASTVisitor<RegExpValue> {
	
	public RegExpValue visitExpressionLiteral(Literal x) {
		//System.err.println("visitExpressionLiteral: " + x.getLiteral());
		return x.getLiteral().accept(this);
	}
	
	public RegExpValue visitLiteralRegExp(RegExp x) {
		//System.err.println("visitLiteralRegExp: " + x.getRegExpLiteral());
		return x.getRegExpLiteral().accept(this);
	}
	
	@Override
	public RegExpValue visitRegExpLexical(Lexical x) {
		//System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpValue(x.getString());
	}
	
	@Override
	public RegExpValue visitRegExpLiteralLexical(
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
		
		Pattern replacePat = Pattern.compile("<([a-zA-Z0-9]+):([^>]*)>");
		Matcher m = replacePat.matcher(subjectPat);
		
		String resultRegExp = "";
		List<Name> names = new ArrayList<Name>();

		while(m.find()){
			String varName = m.group(1);
			// TODO empty parse tree is dangerous
			Name name = new ASTFactory().makeNameLexical(null, varName);
			names.add(name);
			resultRegExp += subjectPat.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subjectPat.substring(start, end);
		System.err.println("resultRegExp: " + resultRegExp);
		return new RegExpValue(resultRegExp, modifier, names);
	}
	
	// Following methods are never used.
	
	@Override
	public RegExpValue visitRegExpModifierLexical(
			org.meta_environment.rascal.ast.RegExpModifier.Lexical x) {
		System.err.println("visitRegExpModifierLexical: " + x.getString());
		return new RegExpValue(x.getString());
	}
	
	@Override
	public RegExpValue visitNamedRegExpLexical(
			org.meta_environment.rascal.ast.NamedRegExp.Lexical x) {
		System.err.println("visitNamedRegExpLexical: " + x.getString());
		return new RegExpValue(x.getString());
	}
	
	@Override
	public RegExpValue visitNamedBackslashLexical(
			org.meta_environment.rascal.ast.NamedBackslash.Lexical x) {
		System.err.println("visitNamedBackslashLexical: " + x.getString());
		return new RegExpValue(x.getString());
	}
	
}
