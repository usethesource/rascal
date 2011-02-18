package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Type;

public abstract class Alternative extends org.rascalmpl.ast.Alternative {

	public Alternative(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Alternative.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Alternative> __param2) {
			super(__param1, __param2);
		}


	}

	static public class NamedType extends org.rascalmpl.ast.Alternative.NamedType {

		public NamedType(INode __param1, Name __param2, Type __param3) {
			super(__param1, __param2, __param3);
		}


	}
}
