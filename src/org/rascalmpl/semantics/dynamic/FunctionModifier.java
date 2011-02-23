package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class FunctionModifier extends org.rascalmpl.ast.FunctionModifier {

	public FunctionModifier(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.FunctionModifier.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.FunctionModifier> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Java extends org.rascalmpl.ast.FunctionModifier.Java {

		public Java(INode __param1) {
			super(__param1);
		}


	}
}
