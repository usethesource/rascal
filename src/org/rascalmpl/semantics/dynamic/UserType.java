package org.rascalmpl.semantics.dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;

public abstract class UserType extends org.rascalmpl.ast.UserType {

	static public class Name extends org.rascalmpl.ast.UserType.Name {

		public Name(INode __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment __eval) {
			Environment theEnv = __eval.getHeap().getEnvironmentForName(
					getName(), __eval);
			String name = org.rascalmpl.interpreter.utils.Names.typeName(this
					.getName());

			if (theEnv != null) {
				Type type = theEnv.lookupAlias(name);

				if (type != null) {
					return type;
				}

				Type tree = theEnv.lookupAbstractDataType(name);

				if (tree != null) {
					return tree;
				}

				Type symbol = theEnv.lookupConcreteSyntaxType(name);

				if (symbol != null) {
					return symbol;
				}
			}

			throw new UndeclaredTypeError(name, this);

		}

	}

	static public class Parametric extends
			org.rascalmpl.ast.UserType.Parametric {

		public Parametric(INode __param1, QualifiedName __param2,
				List<org.rascalmpl.ast.Type> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval) {
			String name;
			Type type = null;
			Environment theEnv = __eval.getHeap().getEnvironmentForName(
					this.getName(), __eval);

			name = org.rascalmpl.interpreter.utils.Names.typeName(this
					.getName());

			if (theEnv != null) {
				type = theEnv.lookupAlias(name);

				if (type == null) {
					type = theEnv.lookupAbstractDataType(name);
				}
			}

			if (type != null) {
				Map<Type, Type> bindings = new HashMap<Type, Type>();
				Type[] params = new Type[this.getParameters().size()];

				int i = 0;
				for (org.rascalmpl.ast.Type param : this.getParameters()) {
					params[i++] = param.typeOf(__eval);
				}

				// __eval has side-effects that we might need?
				type.getTypeParameters().match(TF.tupleType(params), bindings);

				// Note that instantiation use type variables from the current
				// context, not the declaring context
				Type outerInstance = type.instantiate(__eval.getTypeBindings());
				return outerInstance.instantiate(bindings);
			}

			throw new UndeclaredTypeError(name, this);

		}

	}

	public UserType(INode __param1) {
		super(__param1);
	}
}
