package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class MidStringChars extends org.rascalmpl.ast.MidStringChars {

	public MidStringChars(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.MidStringChars.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.MidStringChars> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Lexical extends org.rascalmpl.ast.MidStringChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}


	}
}
