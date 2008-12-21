package org.meta_environment.rascal.interpreter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.RegExp.Lexical;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;

class RegExpPatternValue implements PatternValue {
	String RegExpAsString;
	Character modifier = null;
	List<org.meta_environment.rascal.ast.QualifiedName> patternVars;
	Matcher matcher = null;
	
	RegExpPatternValue(String s){
		RegExpAsString = s;
		modifier = null;
		patternVars = null;
	}
	
	RegExpPatternValue(String s, Character mod, List<org.meta_environment.rascal.ast.QualifiedName> names){
		RegExpAsString = s;
		modifier = mod;
		patternVars = names;
	}
	
	public String toString(){
		return "RegExpPatternValue(" + RegExpAsString + ", " + modifier + ", " + patternVars + ")";
	}
	
	public boolean match(IValue subj, Evaluator ev){
		if(!subj.getType().isStringType()){
			return false;
			/* TODO: this constraint is too harsh in matching context.
			 * throw new RascalTypeError("Subject in regular expression match should have type string and not " + subj.getType());
			 */
		}
		String s = ((IString) subj).getValue();
		try {
			Pattern pat = Pattern.compile(RegExpAsString);
			matcher = pat.matcher(s);
			if(matcher.matches()){
				Map<org.meta_environment.rascal.ast.QualifiedName,String> map = getBindings();
				for(org.meta_environment.rascal.ast.QualifiedName name : map.keySet()){
					EvalResult res = ev.env.getVariable(name);
					/* TODO: This cannot be satisfied in a generator since there are repated assignments
					 * to the variables in the regexp.
					if((res != null) && (res.value != null)){
						throw new RascalException(ev.vf, "variable " + name + " in regular expression has already a value (" + res.value + ")");
					}
					*/
					ev.assignVariable(name, ev.result(ev.vf.string(map.get(name))));
				}
				return true;
			}
			return false;
		} catch (PatternSyntaxException e){
			throw new RascalTypeError(e.getMessage());
		}
	}
	
	private Map<org.meta_environment.rascal.ast.QualifiedName,String> getBindings(){
		Map<org.meta_environment.rascal.ast.QualifiedName,String> map = new HashMap<org.meta_environment.rascal.ast.QualifiedName,String>();
		int k = 1;
		for(org.meta_environment.rascal.ast.QualifiedName nm : patternVars){
			map.put(nm, matcher.group(k));
			k++;
		}
		return map;
	}
}

public class RegExpPatternEvaluator extends NullASTVisitor<PatternValue> {
	
	public boolean isRegExpPattern(org.meta_environment.rascal.ast.Expression pat){
		if(pat.isLiteral() && pat.getLiteral().isRegExp()){
			org.meta_environment.rascal.ast.Literal lit = ((Literal) pat).getLiteral();
			if(lit.isRegExp()){
				return true;
			}
		}
		return false;
	}	
	
	public PatternValue visitExpressionLiteral(Literal x) {
		//System.err.println("visitExpressionLiteral: " + x.getLiteral());
		return x.getLiteral().accept(this);
	}
	
	public PatternValue visitLiteralRegExp(RegExp x) {
		//System.err.println("visitLiteralRegExp: " + x.getRegExpLiteral());
		return x.getRegExpLiteral().accept(this);
	}
	
	@Override
	public PatternValue visitRegExpLexical(Lexical x) {
		//System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpPatternValue(x.getString());
	}
	
	@Override
	public PatternValue visitRegExpLiteralLexical(
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
		
		//TODO take escaped \< characters into account.
		Pattern replacePat = Pattern.compile("<([a-zA-Z0-9]+):([^>]*)>");
		Matcher m = replacePat.matcher(subjectPat);
		
		String resultRegExp = "";
		List<org.meta_environment.rascal.ast.QualifiedName> names = new ArrayList<org.meta_environment.rascal.ast.QualifiedName>();

		while(m.find()){
			String varName = m.group(1);
			//System.err.println("varName = " + varName);
			//TODO: below is a correct but very expensive way of building a Qualified name:
			// a complete parse and tree construction are done for the text of the variable as it appears in the
			// regular expression.
			// A better way would be to build a template for the resulting parse tree and to insert the text
			// of the variable name in it.
			Parser parser = Parser.getInstance();
			ASTFactory factory = new ASTFactory();
			ASTBuilder builder = new ASTBuilder(factory);
			try {
				INode tree = parser.parse(new ByteArrayInputStream((varName + ";").getBytes()));
				Command cmd = builder.buildCommand(tree);
				org.meta_environment.rascal.ast.QualifiedName name = cmd.getStatement().getExpression().getQualifiedName();
				//System.err.println("regexp name = " + name);
				names.add(name);
			} catch (Exception e) {
				throw new RascalBug("Cannot convert string " + varName + " to a name");
			}
			resultRegExp += subjectPat.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subjectPat.substring(start, end);
		//System.err.println("resultRegExp: " + resultRegExp);
		return new RegExpPatternValue(resultRegExp, modifier, names);
	}
}
