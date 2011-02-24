package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public abstract class ShortChar extends org.rascalmpl.ast.ShortChar {

	static public class Lexical extends org.rascalmpl.ast.ShortChar.Lexical {
		public Lexical(ISourceLocation __param1, String __param2) {
			super(__param1, __param2);
		}
	}

	public ShortChar(ISourceLocation __param1) {
		super(__param1);
	}
}
