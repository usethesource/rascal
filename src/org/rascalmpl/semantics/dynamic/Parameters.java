package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Formals;
import org.rascalmpl.interpreter.env.Environment;

public abstract class Parameters extends org.rascalmpl.ast.Parameters {
	private static TypeFactory TF = TypeFactory.getInstance();

	public Parameters(INode __param1) {
		super(__param1);
	}

	static public class VarArgs extends org.rascalmpl.ast.Parameters.VarArgs {

		public VarArgs(INode __param1, Formals __param2) {
			super(__param1, __param2);
		}


		@Override
		public Type typeOf(Environment env) {
			Type formals = getFormals().typeOf(env);
			int arity = formals.getArity();

			if (arity == 0) {
				return TF.tupleType(TF.listType(TF.valueType()));
			}

			Type[] types = new Type[arity];
			int i;

			for (i = 0; i < arity - 1; i++) {
				types[i] = formals.getFieldType(i);
			}

			types[i] = TF.listType(formals.getFieldType(i));

			return TF.tupleType(types);
		}

	}

	static public class Default extends org.rascalmpl.ast.Parameters.Default {
		public Default(INode __param1, Formals __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env) {
			return this.getFormals().typeOf(env);
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Parameters.Ambiguity {
		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Parameters> __param2) {
			super(__param1, __param2);
		}
	}
}
