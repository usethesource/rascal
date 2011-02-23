package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;

public abstract class RegExp extends org.rascalmpl.ast.RegExp {

	public RegExp(INode __param1) {
		super(__param1);
	}

	static public class Lexical extends org.rascalmpl.ast.RegExp.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			if (__eval.__getDebug())
				System.err.println("visitRegExpLexical: " + this.getString());
			return new RegExpPatternValue(__eval.__getCtx(), this, this.getString(), java.util.Collections.<String> emptyList());

		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.RegExp.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.RegExp> __param2) {
			super(__param1, __param2);
		}


	}
}
