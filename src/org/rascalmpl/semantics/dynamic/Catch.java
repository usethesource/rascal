package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Statement;

public abstract class Catch extends org.rascalmpl.ast.Catch {

	public Catch(INode __param1) {
		super(__param1);
	}

	static public class Binding extends org.rascalmpl.ast.Catch.Binding {

		public Binding(INode __param1, Expression __param2, Statement __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Default extends org.rascalmpl.ast.Catch.Default {

		public Default(INode __param1, Statement __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Catch.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Catch> __param2) {
			super(__param1, __param2);
		}


	}
}
