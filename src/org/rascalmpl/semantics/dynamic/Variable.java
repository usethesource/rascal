package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Name;

public abstract class Variable extends org.rascalmpl.ast.Variable {

	public Variable(INode __param1) {
		super(__param1);
	}

	static public class UnInitialized extends org.rascalmpl.ast.Variable.UnInitialized {

		public UnInitialized(INode __param1, Name __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Variable.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Variable> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Initialized extends org.rascalmpl.ast.Variable.Initialized {

		public Initialized(INode __param1, Name __param2, Expression __param3) {
			super(__param1, __param2, __param3);
		}


	}
}
