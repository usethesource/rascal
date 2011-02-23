package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;

public abstract class Formals extends org.rascalmpl.ast.Formals {

	public Formals(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Formals.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Formals> __param2) {
			super(__param1, __param2);
		}

	}

	static public class Default extends org.rascalmpl.ast.Formals.Default {

		public Default(INode __param1, List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, __param2);
		}


		@Override
		public Type typeOf(Environment env) {
			List<org.rascalmpl.ast.Expression> list = this.getFormals();
			Type[] types = new Type[list.size()];

			for (int index = 0; index < list.size(); index++) {
				org.rascalmpl.ast.Expression f = list.get(index);
				Type type = f.typeOf(env);

				if (type == null) {
					throw new UndeclaredTypeError(f.getType().toString(), f);
				}
				types[index] = type;
			}

			return TypeFactory.getInstance().tupleType(types);
		}

	}
}
