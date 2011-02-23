package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Statement;

public abstract class LanguageAction extends org.rascalmpl.ast.LanguageAction {

	public LanguageAction(INode __param1) {
		super(__param1);
	}

	static public class Build extends org.rascalmpl.ast.LanguageAction.Build {

		public Build(INode __param1, Expression __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.LanguageAction.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.LanguageAction> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Action extends org.rascalmpl.ast.LanguageAction.Action {

		public Action(INode __param1, List<Statement> __param2) {
			super(__param1, __param2);
		}


	}
}
