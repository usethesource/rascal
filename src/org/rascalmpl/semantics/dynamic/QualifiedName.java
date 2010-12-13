package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {

	public QualifiedName(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.QualifiedName.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.QualifiedName> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Default extends org.rascalmpl.ast.QualifiedName.Default {

		public Default(INode __param1, List<Name> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}