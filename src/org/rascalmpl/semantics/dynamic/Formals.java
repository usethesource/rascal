package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;
import org.rascalmpl.interpreter.utils.Names;

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
			Object[] typesAndNames = new Object[list.size() * 2];

			for (int formal = 0, index = 0; formal < list.size(); formal++, index++) {
				org.rascalmpl.ast.Expression f = list.get(formal);
				Type type = f.typeOf(env);

				if (type == null) {
					throw new UndeclaredTypeError(f.getType().toString(), f);
				}
				typesAndNames[index++] = type;
				typesAndNames[index] = Names.name(f.getName());
			}

			return TypeFactory.getInstance().tupleType(typesAndNames);
		}

	}
}
