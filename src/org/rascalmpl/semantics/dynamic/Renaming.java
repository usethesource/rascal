package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;

public abstract class Renaming extends org.rascalmpl.ast.Renaming {

	public Renaming(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Renaming.Default {

		public Default(INode __param1, Name __param2, Name __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Renaming.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Renaming> __param2) {
			super(__param1, __param2);
		}


	}
}
