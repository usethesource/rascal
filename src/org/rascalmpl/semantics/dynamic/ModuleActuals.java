package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Type;

public abstract class ModuleActuals extends org.rascalmpl.ast.ModuleActuals {

	public ModuleActuals(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.ModuleActuals.Default {

		public Default(INode __param1, List<Type> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.ModuleActuals.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ModuleActuals> __param2) {
			super(__param1, __param2);
		}


	}
}
