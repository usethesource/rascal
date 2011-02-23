package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Header;

public abstract class PreModule extends org.rascalmpl.ast.PreModule {

	public PreModule(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.PreModule.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PreModule> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Default extends org.rascalmpl.ast.PreModule.Default {

		public Default(INode __param1, Header __param2) {
			super(__param1, __param2);
		}


	}
}
