package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.ModuleActuals;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;

public abstract class ImportedModule extends org.rascalmpl.ast.ImportedModule {

	public ImportedModule(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.ImportedModule.Default {

		public Default(INode __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}


	}

	static public class ActualsRenaming extends org.rascalmpl.ast.ImportedModule.ActualsRenaming {

		public ActualsRenaming(INode __param1, QualifiedName __param2, ModuleActuals __param3, org.rascalmpl.ast.Renamings __param4) {
			super(__param1, __param2, __param3, __param4);
		}


	}

	static public class Actuals extends org.rascalmpl.ast.ImportedModule.Actuals {

		public Actuals(INode __param1, QualifiedName __param2, ModuleActuals __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Renamings extends org.rascalmpl.ast.ImportedModule.Renamings {

		public Renamings(INode __param1, QualifiedName __param2, org.rascalmpl.ast.Renamings __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.ImportedModule.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ImportedModule> __param2) {
			super(__param1, __param2);
		}


	}
}
