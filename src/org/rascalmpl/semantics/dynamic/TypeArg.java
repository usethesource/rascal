package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;

public abstract class TypeArg extends org.rascalmpl.ast.TypeArg {

	public TypeArg(INode __param1) {
		super(__param1);
	}

	static public class Named extends org.rascalmpl.ast.TypeArg.Named {

		public Named(INode __param1, org.rascalmpl.ast.Type __param2, Name __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return this.getType().__evaluate(__eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.TypeArg.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.TypeArg> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Default extends org.rascalmpl.ast.TypeArg.Default {

		public Default(INode __param1, org.rascalmpl.ast.Type __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return this.getType().__evaluate(__eval);

		}

	}
}