package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class Start extends org.rascalmpl.ast.Start {

	public Start(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Start.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Start> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Present extends org.rascalmpl.ast.Start.Present {

		public Present(INode __param1) {
			super(__param1);
		}


	}

	static public class Absent extends org.rascalmpl.ast.Start.Absent {

		public Absent(INode __param1) {
			super(__param1);
		}


	}
}
