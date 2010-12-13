package org.rascalmpl.semantics.dynamic;

import java.lang.Object;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Formal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;

public abstract class Formals extends org.rascalmpl.ast.Formals {

	public Formals(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Formals.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Formals> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Default extends org.rascalmpl.ast.Formals.Default {

		public Default(INode __param1, List<Formal> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			List<Formal> list = this.getFormals();
			Object[] typesAndNames = new Object[list.size() * 2];

			for (int formal = 0, index = 0; formal < list.size(); formal++, index++) {
				Formal f = list.get(formal);
				Type type = f.__evaluate(__eval);

				if (type == null) {
					throw new UndeclaredTypeError(f.getType().toString(), f);
				}
				typesAndNames[index++] = type;
				typesAndNames[index] = org.rascalmpl.interpreter.utils.Names.name(f.getName());
			}

			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(typesAndNames);

		}

	}
}