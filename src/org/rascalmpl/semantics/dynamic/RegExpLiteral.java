package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;

public abstract class RegExpLiteral extends org.rascalmpl.ast.RegExpLiteral {

	static public class Lexical extends org.rascalmpl.ast.RegExpLiteral.Lexical {

		public Lexical(ISourceLocation __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			if (__eval.__getDebug()) {
				System.err.println("visitRegExpLiteralLexical: "
						+ this.getString());
			}

			String subjectPat = this.getString();

			if (subjectPat.charAt(0) != '/') {
				throw new SyntaxError("Malformed Regular expression: "
						+ subjectPat, this.getLocation());
			}

			int start = 1;
			int end = subjectPat.length() - 1;

			while (end > 0 && subjectPat.charAt(end) != '/') {
				end--;
			}
			String modifiers = subjectPat.substring(end + 1);

			if (subjectPat.charAt(end) != '/') {
				throw new SyntaxError("Regular expression does not end with /",
						this.getLocation());
			}

			// The resulting regexp that we are constructing
			StringBuffer resultRegExp = new StringBuffer();

			if (modifiers.length() > 0) {
				resultRegExp.append("(?").append(modifiers).append(")");
			}

			/*
			 * Find all pattern variables. There are two cases: (1) <X:regexp> -
			 * a true pattern variable that will match regexp. Introduces a new
			 * local variable. - regexp may contain references to variables <V>
			 * in the surrounding scope (but not to pattern variables!) These
			 * values are interpolated in regexp (2) <X> - if this did not occur
			 * earlier in the pattern, we do a string interpolation of the
			 * current value of X. - otherwise this should have been introduced
			 * before by a pattern variable and we ensure at match time that
			 * both values are the same (non-linear pattern). We take escaped \<
			 * characters into account.
			 */

			String Name = "[a-zA-Z0-9]+";
			String NR1 = "[^\\\\<>]";
			String NR2 = "(?:\\\\[\\\\<>])";
			String NR3 = "(?:\\\\)";
			String NR4 = "(?:<" + Name + ">)";

			String NamedRegexp = "(?:" + NR1 + "|" + NR2 + "|" + NR3 + "|"
					+ NR4 + ")";

			String RE = "(?:(?<!\\\\)|(?<=\\\\\\\\))<(" + Name
					+ ")(?:\\s*:\\s*(" + NamedRegexp + "*))?" + ">";
			// | |
			// group 1 2
			// variable name regular expression to be matched

			Pattern replacePat = java.util.regex.Pattern.compile(RE);

			Matcher m = replacePat.matcher(subjectPat);

			// List of variable introductions
			List<String> patternVars = new ArrayList<String>();

			while (m.find()) {
				String varName = m.group(1);

				resultRegExp.append(subjectPat.substring(start, m.start(0))); // add
				// regexp
				// before
				// <
				// ...
				// >

				if (m.end(2) > -1) { /* case (1): <X:regexp> */

					if (patternVars.contains(varName)) {
						throw new RedeclaredVariableError(varName, this);
					}
					patternVars.add(varName);
					resultRegExp.append("(").append(
							__eval.interpolate(m.group(2))).append(")");
				} else { /* case (2): <X> */
					int varIndex = patternVars.indexOf(varName);
					if (varIndex >= 0) {
						/* Generate reference to previous occurrence */
						resultRegExp.append("(?:\\").append(1 + varIndex)
								.append(")");
					} else {
						resultRegExp.append(__eval.getValueAsString(varName)); // TODO:
						// escape
						// special
						// chars?
					}
				}
				start = m.end(0);
			}
			resultRegExp.append(subjectPat.substring(start, end));
			return new RegExpPatternValue(__eval.__getCtx(), this, resultRegExp
					.toString(), patternVars);

		}
	}

	public RegExpLiteral(ISourceLocation __param1) {
		super(__param1);
	}
}
