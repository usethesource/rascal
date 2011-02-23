package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;

public abstract class Label extends org.rascalmpl.ast.Label {

	public Label(INode __param1) {
		super(__param1);
	}

	static public class Empty extends org.rascalmpl.ast.Label.Empty {

		public Empty(INode __param1) {
			super(__param1);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Label.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Label> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Default extends org.rascalmpl.ast.Label.Default {

		public Default(INode __param1, Name __param2) {
			super(__param1, __param2);
		}


	}
}
