package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;

public abstract class FunctionType extends org.rascalmpl.ast.FunctionType {

	public FunctionType(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.FunctionType.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.FunctionType> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class TypeArguments extends org.rascalmpl.ast.FunctionType.TypeArguments {

		public TypeArguments(INode __param1, org.rascalmpl.ast.Type __param2, List<TypeArg> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			Type returnType = this.getType().__evaluate(__eval);
			Type argTypes = __eval.getArgumentTypes(this.getArguments());
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(returnType, argTypes);

		}

	}
}