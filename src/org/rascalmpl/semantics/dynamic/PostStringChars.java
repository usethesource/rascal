package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class PostStringChars extends org.rascalmpl.ast.PostStringChars {

	public PostStringChars(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.PostStringChars.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PostStringChars> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Lexical extends org.rascalmpl.ast.PostStringChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}


	}
}
