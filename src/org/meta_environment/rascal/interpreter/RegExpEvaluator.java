package org.meta_environment.rascal.interpreter;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;

class RegExpResult {
	String RegExpAsString;
	String modifier = "";
	List<String> patternVars;
	
	RegExpResult(String s){
		RegExpAsString = s;
	}
	
	public String toString(){
		return "RegExpResult(" + RegExpAsString + ")";
	}
}

public class RegExpEvaluator extends NullASTVisitor<RegExpResult> {
	
	private int varCount = 0;
	
	RegExpEvaluator(){
		
	}
	
	
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
		System.err.println("visitRegExpLiteralLexical: " + x.getString() + "; tree: " + x.getTree());
		return new RegExpResult(x.getString());
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
