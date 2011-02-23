package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Prod;
import org.rascalmpl.ast.Start;
import org.rascalmpl.ast.Sym;

public abstract class SyntaxDefinition extends org.rascalmpl.ast.SyntaxDefinition {

	public SyntaxDefinition(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.SyntaxDefinition.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.SyntaxDefinition> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Layout extends org.rascalmpl.ast.SyntaxDefinition.Layout {

		public Layout(INode __param1, Sym __param2, Prod __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Language extends org.rascalmpl.ast.SyntaxDefinition.Language {

		public Language(INode __param1, Start __param2, Sym __param3, Prod __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}
}
