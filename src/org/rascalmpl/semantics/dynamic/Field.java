package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class Field extends org.rascalmpl.ast.Field {

	public Field(INode __param1) {
		super(__param1);
	}

	static public class Index extends org.rascalmpl.ast.Field.Index {

		public Index(INode __param1, IntegerLiteral __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Field.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Field> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Name extends org.rascalmpl.ast.Field.Name {

		public Name(INode __param1, org.rascalmpl.ast.Name __param2) {
			super(__param1, __param2);
		}


	}
}
