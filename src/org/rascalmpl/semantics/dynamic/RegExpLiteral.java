package org.rascalmpl.semantics.dynamic;

public abstract class RegExpLiteral extends org.rascalmpl.ast.RegExpLiteral {


public RegExpLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.RegExpLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.RegExpLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Lexical extends org.rascalmpl.ast.RegExpLiteral.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		if(__eval.__getDebug())System.err.println("visitRegExpLiteralLexical: " + this.getString());

		java.lang.String subjectPat = this.getString();

		if(subjectPat.charAt(0) != '/'){
			throw new org.rascalmpl.interpreter.staticErrors.SyntaxError("Malformed Regular expression: " + subjectPat, this.getLocation());
		}

		int start = 1;
		int end = subjectPat.length()-1;

		while(end > 0 && subjectPat.charAt(end) != '/'){
			end--;
		}
		java.lang.String modifiers = subjectPat.substring(end+1);

		if(subjectPat.charAt(end) != '/'){
			throw new org.rascalmpl.interpreter.staticErrors.SyntaxError("Regular expression does not end with /", this.getLocation());
		}

		// The resulting regexp that we are constructing
		java.lang.StringBuffer resultRegExp = new java.lang.StringBuffer();

		if(modifiers.length() > 0)
			resultRegExp.append("(?").append(modifiers).append(")");

		/*
		 * Find all pattern variables. There are two cases:
		 * (1) <X:regexp>
		 *     - a true pattern variable that will match regexp. Introduces a new local variable.
		 *     - regexp may contain references to variables <V> in the surrounding scope (but not to
		 *       pattern variables!) These values are interpolated in regexp
		 * (2) <X>
		 *     - if this did not occur earlier in the pattern, we do a string interpolation of the current value of X.
		 *     - otherwise this should have been introduced before by a pattern variable and we ensure at match time
		 *       that both values are the same (non-linear pattern).
		 * We take escaped \< characters into account.
		 */

		java.lang.String Name = "[a-zA-Z0-9]+";
		java.lang.String NR1 = "[^\\\\<>]";
		java.lang.String NR2 = "(?:\\\\[\\\\<>])";
		java.lang.String NR3 = "(?:\\\\)";
		java.lang.String NR4 = "(?:<" + Name + ">)";

		java.lang.String NamedRegexp = "(?:" + NR1 + "|" + NR2 + "|" + NR3 + "|" + NR4 + ")";

		java.lang.String RE = "(?:(?<!\\\\)|(?<=\\\\\\\\))<(" + Name + ")(?:\\s*:\\s*(" + NamedRegexp + "*))?" + ">";
		//                               |                         |
		//                       group   1                         2
		//                               variable name             regular expression to be matched

		java.util.regex.Pattern replacePat = java.util.regex.Pattern.compile(RE);

		java.util.regex.Matcher m = replacePat.matcher(subjectPat);

		// List of variable introductions
		java.util.List<java.lang.String> patternVars = new java.util.ArrayList<java.lang.String>();

		while(m.find()){
			java.lang.String varName = m.group(1);

			resultRegExp.append(subjectPat.substring(start, m.start(0))); // add regexp before < ... > 

			if (m.end(2) > -1){       /* case (1): <X:regexp> */

				if(patternVars.contains(varName))
					throw new org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError(varName, this);
				patternVars.add(varName);
				resultRegExp.append("(").append(__eval.interpolate(m.group(2))).append(")");
			} else {                   /* case (2): <X> */
				int varIndex = patternVars.indexOf(varName);
				if(varIndex >= 0){
					/* Generate reference to previous occurrence */
					resultRegExp.append("(?:\\").append(1+varIndex).append(")");
				} else {	
					resultRegExp.append(__eval.getValueAsString(varName)); // TODO: escape special chars?
				} 
			}
			start = m.end(0);
		}
		resultRegExp.append(subjectPat.substring(start, end));
		return new org.rascalmpl.interpreter.matching.RegExpPatternValue(__eval.__getCtx(), this, resultRegExp.toString(), patternVars);
	
}

}
}