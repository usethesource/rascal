/*******************************************************************************
  * Copyright (c) 2009-2013 CWI
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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;

public abstract class RegExpLiteral extends org.rascalmpl.ast.RegExpLiteral {

	
	static public class Lexical extends org.rascalmpl.ast.RegExpLiteral.Lexical {

		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		/*
		 * Interpolate all occurrences of <X> by the value of X
		 */
		private void interpolate(String re, List<InterpolationElement> elems, List<String> vars) {
			Pattern replacePat = java.util.regex.Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
			Matcher m = replacePat.matcher(re);
			int start = 0;
			while (m.find()) {
				elems.add(new StaticInterpolationElement(addGroups(re.substring(start, m.start(0)), vars)));
				elems.add(new NamedInterpolationElement(m.group(1)));
				
				start = m.end(0);
			}
			elems.add(new StaticInterpolationElement(re.substring(start, re.length())));
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			String subjectPat = this.getString();
			List<InterpolationElement> resultRegExp = new LinkedList<InterpolationElement>();

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

			if (modifiers.length() > 0) {
				resultRegExp.add(new StaticInterpolationElement("(?" + modifiers + ")"));
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

				resultRegExp.add(new StaticInterpolationElement(addGroups(subjectPat.substring(start, m.start(0)), patternVars))); // add
				// regexp
				// before
				// <
				// ...
				// >

				if (m.end(2) > -1) { /* case (1): <X:regexp> */

					if (patternVars.contains(varName)) {
						throw new RedeclaredVariable(varName, this);
					}
					patternVars.add(varName);
					resultRegExp.add(new StaticInterpolationElement("("));
					interpolate(m.group(2), resultRegExp, patternVars);
					resultRegExp.add(new StaticInterpolationElement(")"));
				} else { /* case (2): <X> */
					int varIndex = patternVars.indexOf(varName);
					if (varIndex >= 0) {
						/* Generate reference to previous occurrence */
						resultRegExp.add(new StaticInterpolationElement("(?:\\" + (1 + varIndex) + ")"));
					} else {
						resultRegExp.add(new NamedInterpolationElement(varName));
					}
				}
				start = m.end(0);
			}
			
			resultRegExp.add(new StaticInterpolationElement(addGroups(subjectPat.substring(start, end), patternVars)));
			return new RegExpPatternValue(eval, this, resultRegExp, patternVars);
		}

		private String addGroups(String s, List<String> patternVars) {
			int i = -1;
			while ((i = s.indexOf("(", i + 1)) != -1) {
				if (i == 0) { // start of string
					if (s.length() == 1 || s.charAt(i + 1) != '?') {
						patternVars.add("_" + i);
					}
				}
				else if (i == s.length() - 1) { // end of string
					if (s.charAt(i - 1) != '\\') {
						patternVars.add("_" + i);
					}
				}
				else { // middle of string
					if (s.charAt(i - 1) != '\\' && s.charAt(i + 1) != '?') {
						patternVars.add("_" + i);
					}
				}
			}
			
			return s;
		}
	}

	public RegExpLiteral(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
	
	static public interface InterpolationElement {
		String getString(IEvaluatorContext env);
	}
	
	static public class NamedInterpolationElement implements InterpolationElement {
		private final String name;

		public NamedInterpolationElement(String name) {
			this.name = name;
		}
		
		private String escape(IValue val) {
			String name;
			if (val.getType().isString()) {
				name = ((IString) val).getValue();
			}
			else {
				name = val.toString();
			}
			StringBuilder b = new StringBuilder();
			
			for (int i = 0; i < name.length(); i++) {
				char ch = name.charAt(i);
				if ("^.|?*+()[\\".indexOf(ch) != -1) {
					b.append('\\');
				}
				b.append(ch);
			}
			
			return b.toString();
		}

		@Override
		public String getString(IEvaluatorContext env) {
			Result<IValue> variable = env.getCurrentEnvt().getVariable(name);
			
			if (variable == null) {
				throw new UndeclaredVariable(name, env.getCurrentAST());
			}
			
			IValue value = variable.getValue();
			if (value == null) {
				throw new UninitializedVariable(name, env.getCurrentAST());
			}
			
			return escape(value);
		}
		
		@Override
		public String toString() {
			return "(?" + name + ")";
		}
	}
	
	static public class StaticInterpolationElement implements InterpolationElement {
		private String elem;

		public StaticInterpolationElement(String elem) {
			this.elem = removeRascalSpecificEscapes(elem);
		}
		
		@Override
		public String getString(IEvaluatorContext env) {
			return elem;
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
		public String toString() {
			return elem;
		}
	}
	
}
