package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import java.util.Set;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.TypeEvaluator.Visitor;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.AmbiguousFunctionReferenceError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;

public abstract class DataTypeSelector extends org.rascalmpl.ast.DataTypeSelector {

	public DataTypeSelector(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.DataTypeSelector.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.DataTypeSelector> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Selector extends org.rascalmpl.ast.DataTypeSelector.Selector {

		public Selector(INode __param1, QualifiedName __param2, Name __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Type __evaluate(Visitor __eval) {

			Type adt;
			QualifiedName sort = this.getSort();
			String name = org.rascalmpl.interpreter.utils.Names.typeName(sort);

			if (org.rascalmpl.interpreter.utils.Names.isQualified(sort)) {
				ModuleEnvironment mod = __eval.__getHeap().getModule(org.rascalmpl.interpreter.utils.Names.moduleName(sort));

				if (mod == null) {
					throw new UndeclaredModuleError(org.rascalmpl.interpreter.utils.Names.moduleName(sort), sort);
				}

				adt = mod.lookupAbstractDataType(name);
			} else {
				adt = __eval.__getEnv().lookupAbstractDataType(name);
			}

			if (adt == null) {
				throw new UndeclaredTypeError(name, this);
			}

			String constructor = org.rascalmpl.interpreter.utils.Names.name(this.getProduction());
			Set<Type> constructors = __eval.__getEnv().lookupConstructor(adt, constructor);

			if (constructors.size() == 0) {
				throw new UndeclaredTypeError(name + "." + constructor, this);
			} else if (constructors.size() > 1) {
				throw new AmbiguousFunctionReferenceError(name + "." + constructor, this);
			} else {
				return constructors.iterator().next();
			}

		}

	}
}