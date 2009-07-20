package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;


public class RegExpPatternEvaluator extends NullASTVisitor<MatchPattern> {
	private boolean debug = false;
	private final Environment env;
	private final IValueFactory vf;
	private Evaluator eval;
	
	public RegExpPatternEvaluator(IValueFactory vf, Evaluator eval, Environment env) {
		this.env = env;
		this.eval = eval;
		this.vf = vf;
	}
	
	public boolean isRegExpPattern(org.meta_environment.rascal.ast.Expression pat){
		if(pat.isLiteral() && pat.getLiteral().isRegExp()){
			org.meta_environment.rascal.ast.Literal lit = ((Literal) pat).getLiteral();
			if(lit.isRegExp()){
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public MatchPattern visitExpressionLiteral(Literal x) {
		if(debug)System.err.println("visitExpressionLiteral: " + x.getLiteral());
		return x.getLiteral().accept(this);
	}
	
	@Override
	public MatchPattern visitLiteralRegExp(RegExp x) {
		if(debug)System.err.println("visitLiteralRegExp: " + x.getRegExpLiteral());
		return x.getRegExpLiteral().accept(this);
	}
	
	@Override
	public MatchPattern visitRegExpLexical(Lexical x) {
		if(debug)System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpPatternValue(vf, new EvaluatorContext(eval, x), x.getString(), env);
	}
	
	@Override
	public MatchPattern visitRegExpLiteralLexical(
			org.meta_environment.rascal.ast.RegExpLiteral.Lexical x) {
		if(debug)System.err.println("visitRegExpLiteralLexical: " + x.getString());

		String subjectPat = x.getString();
		Character modifier = null;
		
		if(subjectPat.charAt(0) != '/'){
			throw new SyntaxError("Malformed Regular expression: " + subjectPat, x.getLocation());
		}
		
		int start = 1;
		int end = subjectPat.length()-1;
		if(subjectPat.charAt(end) != '/'){
			modifier = Character.valueOf(subjectPat.charAt(end));
			end--;
		}
		if(subjectPat.charAt(end) != '/'){
			throw new SyntaxError("Regular expression does not end with /", x.getLocation());
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
			if(names.contains(varName))
				throw new RedeclaredVariableError(varName, x);
			names.add(varName);
			resultRegExp += subjectPat.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subjectPat.substring(start, end);
		/*
		 * Replace in the final regexp all occurrences of \< by <
		 */
		resultRegExp = resultRegExp.replaceAll("(\\\\<)", "<");
		if(debug)System.err.println("resultRegExp: " + resultRegExp);
		return new RegExpPatternValue(vf, x, resultRegExp, modifier, names, env);
	}
}
