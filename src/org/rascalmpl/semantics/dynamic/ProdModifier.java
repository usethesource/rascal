package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Assoc;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class ProdModifier extends org.rascalmpl.ast.ProdModifier {

	public ProdModifier(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.ProdModifier.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ProdModifier> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Associativity extends org.rascalmpl.ast.ProdModifier.Associativity {

		public Associativity(INode __param1, Assoc __param2) {
			super(__param1, __param2);
		}


	}

	static public class Tag extends org.rascalmpl.ast.ProdModifier.Tag {

		public Tag(INode __param1, org.rascalmpl.ast.Tag __param2) {
			super(__param1, __param2);
		}


	}

	static public class Bracket extends org.rascalmpl.ast.ProdModifier.Bracket {

		public Bracket(INode __param1) {
			super(__param1);
		}


	}

	static public class Lexical extends org.rascalmpl.ast.ProdModifier.Lexical {

		public Lexical(INode __param1) {
			super(__param1);
		}


	}
}
