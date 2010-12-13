package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;

public abstract class Signature extends org.rascalmpl.ast.Signature {

	public Signature(INode __param1) {
		super(__param1);
	}

	static public class WithThrows extends org.rascalmpl.ast.Signature.WithThrows {

		public WithThrows(INode __param1, org.rascalmpl.ast.Type __param2, FunctionModifiers __param3, Name __param4, Parameters __param5, List<org.rascalmpl.ast.Type> __param6) {
			super(__param1, __param2, __param3, __param4, __param5, __param6);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(this.getType().__evaluate(__eval), this.getParameters().__evaluate(__eval));

		}

	}

	static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {

		public NoThrows(INode __param1, org.rascalmpl.ast.Type __param2, FunctionModifiers __param3, Name __param4, Parameters __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(this.getType().__evaluate(__eval), this.getParameters().__evaluate(__eval));

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Signature.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Signature> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}