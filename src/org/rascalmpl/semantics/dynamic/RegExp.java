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

import java.util.Arrays;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.semantics.dynamic.RegExpLiteral.InterpolationElement;

public abstract class RegExp extends org.rascalmpl.ast.RegExp {

	static public class Lexical extends org.rascalmpl.ast.RegExp.Lexical {
		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			List<InterpolationElement> elems = Arrays.<InterpolationElement>asList(new RegExpLiteral.StaticInterpolationElement(getString()));
			return new RegExpPatternValue(eval, this, elems, java.util.Collections.<String> emptyList());
		}
	}

	public RegExp(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
