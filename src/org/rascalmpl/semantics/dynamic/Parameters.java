package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Formals;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;

public abstract class Parameters extends org.rascalmpl.ast.Parameters {

	public Parameters(INode __param1) {
		super(__param1);
	}

	static public class VarArgs extends org.rascalmpl.ast.Parameters.VarArgs {

		public VarArgs(INode __param1, Formals __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			Type formals = this.getFormals().__evaluate(__eval);
			int arity = formals.getArity();

			if (arity == 0) {
				// TODO is __eval sensible or should we restrict the syntax?
				return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(
						org.rascalmpl.interpreter.TypeEvaluator.__getTf().listType(org.rascalmpl.interpreter.TypeEvaluator.__getTf().valueType()), "args");
			}

			Type[] types = new Type[arity];
			String[] labels = new String[arity];
			int i;

			for (i = 0; i < arity - 1; i++) {
				types[i] = formals.getFieldType(i);
				labels[i] = formals.getFieldName(i);
			}

			types[i] = org.rascalmpl.interpreter.TypeEvaluator.__getTf().listType(formals.getFieldType(i));
			labels[i] = formals.getFieldName(i);

			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(types, labels);

		}

	}

	static public class Default extends org.rascalmpl.ast.Parameters.Default {

		public Default(INode __param1, Formals __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			return this.getFormals().__evaluate(__eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Parameters.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Parameters> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}