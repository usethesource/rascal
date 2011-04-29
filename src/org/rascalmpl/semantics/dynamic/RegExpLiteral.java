/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;

public abstract class RegExpLiteral extends org.rascalmpl.ast.RegExpLiteral {

	static public class Lexical extends org.rascalmpl.ast.RegExpLiteral.Lexical {

		public Lexical(IConstructor __param1, String __param2) {
			super(__param1, __param2);
		}

		private String getValueAsString(String varName, IEvaluatorContext ctx) {
			Environment env = ctx.getCurrentEnvt();
			Result<IValue> res = env.getVariable(varName);
			if (res != null && res.getValue() != null) {
				if (res.getType().isStringType())
					return ((IString) res.getValue()).getValue();

				return res.getValue().toString();
			}

			throw new UninitializedVariableError(varName, ctx.getCurrentAST());
		}

		/*
		 * Interpolate all occurrences of <X> by the value of X
		 */
		private String interpolate(String re, IEvaluatorContext ctx) {
			Pattern replacePat = java.util.regex.Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
			Matcher m = replacePat.matcher(re);
			StringBuffer result = new StringBuffer();
			int start = 0;
			while (m.find()) {
				// TODO: escape special chars?
				result.append(re.substring(start, m.start(0))).append(getValueAsString(m.group(1), ctx)); 
				start = m.end(0);
			}
			result.append(re.substring(start, re.length()));

			return result.toString();
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval) {

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
							interpolate(m.group(2), __eval)).append(")");
				} else { /* case (2): <X> */
					int varIndex = patternVars.indexOf(varName);
					if (varIndex >= 0) {
						/* Generate reference to previous occurrence */
						resultRegExp.append("(?:\\").append(1 + varIndex)
								.append(")");
					} else {
						resultRegExp.append(getValueAsString(varName,__eval)); // TODO:
						// escape
						// special
						// chars?
					}
				}
				start = m.end(0);
			}
			resultRegExp.append(subjectPat.substring(start, end));
			return new RegExpPatternValue(this, resultRegExp
					.toString(), patternVars);

		}
	}

	public RegExpLiteral(IConstructor __param1) {
		super(__param1);
	}
}
