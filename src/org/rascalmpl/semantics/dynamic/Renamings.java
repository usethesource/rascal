package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Renaming;

public abstract class Renamings extends org.rascalmpl.ast.Renamings {

	public Renamings(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Renamings.Default {

		public Default(INode __param1, List<Renaming> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Renamings.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Renamings> __param2) {
			super(__param1, __param2);
		}


	}
}
