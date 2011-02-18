package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;

public abstract class Case extends org.rascalmpl.ast.Case {

	public Case(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Case.Default {

		public Default(INode __param1, Statement __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Case.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Case> __param2) {
			super(__param1, __param2);
		}


	}

	static public class PatternWithAction extends org.rascalmpl.ast.Case.PatternWithAction {

		public PatternWithAction(INode __param1, org.rascalmpl.ast.PatternWithAction __param2) {
			super(__param1, __param2);
		}


	}
}
