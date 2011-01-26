package org.rascalmpl.interpreter;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.PartiallyLabeledFieldsError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;

public class TypeEvaluator {
	private final static TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();

	private final org.rascalmpl.interpreter.TypeEvaluator.Visitor visitor = new org.rascalmpl.interpreter.TypeEvaluator.Visitor();
	private final Environment env;

	private GlobalEnvironment heap;

	public TypeEvaluator(Environment env, GlobalEnvironment heap) {
		super();

		this.env = env;
		this.__setHeap(heap);
	}

	public Environment __getEnv() {
		return env;
	}

	public void __setHeap(GlobalEnvironment heap) {
		this.heap = heap;
	}

	public GlobalEnvironment __getHeap() {
		return heap;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	public Type eval(org.rascalmpl.ast.Type type) {
		return type.__evaluate(this.visitor);
	}

	public Type eval(Parameters parameters) {
		return parameters.__evaluate(this.visitor);
	}

	public Type eval(Expression formal) {
		return formal.__evaluate(this.visitor);
	}

	public Type eval(Signature signature) {
		return signature.__evaluate(this.visitor);
	}

	public class Visitor extends NullASTVisitor<Type> {
		public Type getArgumentTypes(List<TypeArg> args) {
			Type[] fieldTypes = new Type[args.size()];
			String[] fieldLabels = new String[args.size()];

			int i = 0;
			boolean allLabeled = true;
			boolean someLabeled = false;

			for (TypeArg arg : args) {
				fieldTypes[i] = arg.getType().__evaluate(this);

				if (arg.isNamed()) {
					fieldLabels[i] = org.rascalmpl.interpreter.utils.Names.name(arg.getName());
					someLabeled = true;
				} else {
					fieldLabels[i] = null;
					allLabeled = false;
				}
				i++;
			}

			if (someLabeled && !allLabeled) {
				// TODO: this ast is not the root of the cause
				throw new PartiallyLabeledFieldsError(args.get(0));
			}

			if (!allLabeled) {
				return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(fieldTypes);
			}

			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(fieldTypes, fieldLabels);
		}

		public Environment getEnvironmentForName(QualifiedName name) {
			if (org.rascalmpl.interpreter.utils.Names.isQualified(name)) {
				if (TypeEvaluator.this.__getHeap() == null) {
					throw new ImplementationError("no heap to look up module");
				}
				ModuleEnvironment mod = TypeEvaluator.this.__getHeap().getModule(org.rascalmpl.interpreter.utils.Names.moduleName(name));
				if (mod == null) {
					throw new UndeclaredModuleError(org.rascalmpl.interpreter.utils.Names.moduleName(name), name);
				}
				return mod;
			}

			return TypeEvaluator.this.__getEnv();
		}

		public GlobalEnvironment __getHeap() {
			return TypeEvaluator.this.__getHeap();
		}

		public Environment __getEnv() {
			return TypeEvaluator.this.__getEnv();
		}
	}
}
