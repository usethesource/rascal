package org.rascalmpl.interpreter;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Formal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.BasicType.Bag;
import org.rascalmpl.ast.BasicType.Bool;
import org.rascalmpl.ast.BasicType.DateTime;
import org.rascalmpl.ast.BasicType.Int;
import org.rascalmpl.ast.BasicType.Lex;
import org.rascalmpl.ast.BasicType.Loc;
import org.rascalmpl.ast.BasicType.Map;
import org.rascalmpl.ast.BasicType.Node;
import org.rascalmpl.ast.BasicType.Num;
import org.rascalmpl.ast.BasicType.Real;
import org.rascalmpl.ast.BasicType.ReifiedAdt;
import org.rascalmpl.ast.BasicType.ReifiedConstructor;
import org.rascalmpl.ast.BasicType.ReifiedFunction;
import org.rascalmpl.ast.BasicType.ReifiedNonTerminal;
import org.rascalmpl.ast.BasicType.ReifiedReifiedType;
import org.rascalmpl.ast.BasicType.ReifiedType;
import org.rascalmpl.ast.BasicType.Relation;
import org.rascalmpl.ast.BasicType.Set;
import org.rascalmpl.ast.BasicType.String;
import org.rascalmpl.ast.BasicType.Tuple;
import org.rascalmpl.ast.BasicType.Value;
import org.rascalmpl.ast.BasicType.Void;
import org.rascalmpl.ast.Formal.TypeName;
import org.rascalmpl.ast.FunctionType.TypeArguments;
import org.rascalmpl.ast.Parameters.VarArgs;
import org.rascalmpl.ast.Signature.NoThrows;
import org.rascalmpl.ast.Signature.WithThrows;
import org.rascalmpl.ast.Type.Ambiguity;
import org.rascalmpl.ast.Type.Basic;
import org.rascalmpl.ast.Type.Bracket;
import org.rascalmpl.ast.Type.Function;
import org.rascalmpl.ast.Type.Selector;
import org.rascalmpl.ast.Type.Structured;
import org.rascalmpl.ast.Type.Symbol;
import org.rascalmpl.ast.Type.User;
import org.rascalmpl.ast.Type.Variable;
import org.rascalmpl.ast.TypeArg.Default;
import org.rascalmpl.ast.TypeArg.Named;
import org.rascalmpl.ast.UserType.Name;
import org.rascalmpl.ast.UserType.Parametric;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.AmbiguousFunctionReferenceError;
import org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError;
import org.rascalmpl.interpreter.staticErrors.PartiallyLabeledFieldsError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;

public class TypeEvaluator {
	private final static org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	
	private final org.rascalmpl.interpreter.TypeEvaluator.Visitor visitor = new org.rascalmpl.interpreter.TypeEvaluator.Visitor();
	private final org.rascalmpl.interpreter.env.Environment env;

	private org.rascalmpl.interpreter.env.GlobalEnvironment heap;
	
	public TypeEvaluator(org.rascalmpl.interpreter.env.Environment env, org.rascalmpl.interpreter.env.GlobalEnvironment heap) {
		super();
		
		this.env = env;
		this.__setHeap(heap);
	}
	
	public org.rascalmpl.interpreter.env.Environment __getEnv() {
		return env;
	}

	public void __setHeap(org.rascalmpl.interpreter.env.GlobalEnvironment heap) {
		this.heap = heap;
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment __getHeap() {
		return heap;
	}

	public static org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	public org.eclipse.imp.pdb.facts.type.Type eval(org.rascalmpl.ast.Type type) {
		return type.__evaluate(this.visitor);
	}

	public org.eclipse.imp.pdb.facts.type.Type eval(org.rascalmpl.ast.Parameters parameters) {
		return parameters.__evaluate(this.visitor);
	}

	public org.eclipse.imp.pdb.facts.type.Type eval(org.rascalmpl.ast.Formal formal) {
		return formal.__evaluate(this.visitor);
	}
	
	public org.eclipse.imp.pdb.facts.type.Type eval(org.rascalmpl.ast.Signature signature) {
		return signature.__evaluate(this.visitor);
	}

	public class Visitor extends org.rascalmpl.ast.NullASTVisitor<org.eclipse.imp.pdb.facts.type.Type> {
		public org.eclipse.imp.pdb.facts.type.Type getArgumentTypes(java.util.List<org.rascalmpl.ast.TypeArg> args) {
			org.eclipse.imp.pdb.facts.type.Type[] fieldTypes = new org.eclipse.imp.pdb.facts.type.Type[args.size()];
			java.lang.String[] fieldLabels = new java.lang.String[args.size()];

			int i = 0;
			boolean allLabeled = true;
			boolean someLabeled = false;
	
			for (org.rascalmpl.ast.TypeArg arg : args) {
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
				throw new org.rascalmpl.interpreter.staticErrors.PartiallyLabeledFieldsError(args.get(0));
			}
			
			if (!allLabeled) {
				return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(fieldTypes);
			}
			
			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(fieldTypes, fieldLabels);
		}

		public org.rascalmpl.interpreter.env.Environment getEnvironmentForName(org.rascalmpl.ast.QualifiedName name) {
			if (org.rascalmpl.interpreter.utils.Names.isQualified(name)) {
				if (TypeEvaluator.this.__getHeap() == null) {
					throw new org.rascalmpl.interpreter.asserts.ImplementationError("no heap to look up module");
				}
				org.rascalmpl.interpreter.env.ModuleEnvironment mod = TypeEvaluator.this.__getHeap().getModule(org.rascalmpl.interpreter.utils.Names.moduleName(name));
				if (mod == null) {
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError(org.rascalmpl.interpreter.utils.Names.moduleName(name), name);
				}
				return mod;
			}
			
			return TypeEvaluator.this.__getEnv();
		}
		
		public org.rascalmpl.interpreter.env.GlobalEnvironment __getHeap() {
			return TypeEvaluator.this.__getHeap();
		}

		public org.rascalmpl.interpreter.env.Environment __getEnv() {
			return TypeEvaluator.this.__getEnv();
		}
	}
}

