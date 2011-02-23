package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Toplevel;

public abstract class Body extends org.rascalmpl.ast.Body {

	public Body(INode __param1) {
		super(__param1);
	}

	static public class Toplevels extends org.rascalmpl.ast.Body.Toplevels {

		public Toplevels(INode __param1, List<Toplevel> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Body.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Body> __param2) {
			super(__param1, __param2);
		}


	}
}
