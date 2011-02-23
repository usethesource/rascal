package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.TypeVar;

public abstract class ModuleParameters extends org.rascalmpl.ast.ModuleParameters {

	public ModuleParameters(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.ModuleParameters.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ModuleParameters> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Default extends org.rascalmpl.ast.ModuleParameters.Default {

		public Default(INode __param1, List<TypeVar> __param2) {
			super(__param1, __param2);
		}


	}
}
