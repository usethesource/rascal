package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class Mapping_Expression extends org.rascalmpl.ast.Mapping_Expression {

	public Mapping_Expression(INode __param1) {
		super(__param1);
	}

	static public class Default extends org.rascalmpl.ast.Mapping_Expression.Default {

		public Default(INode __param1, Expression __param2, Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Mapping_Expression.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Mapping_Expression> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}