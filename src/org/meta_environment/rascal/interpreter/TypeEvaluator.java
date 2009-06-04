package org.meta_environment.rascal.interpreter;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ast.Formal;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Parameters;
import org.meta_environment.rascal.ast.TypeArg;
import org.meta_environment.rascal.ast.TypeVar;
import org.meta_environment.rascal.ast.BasicType.Bool;
import org.meta_environment.rascal.ast.BasicType.Int;
import org.meta_environment.rascal.ast.BasicType.Loc;
import org.meta_environment.rascal.ast.BasicType.Node;
import org.meta_environment.rascal.ast.BasicType.Real;
import org.meta_environment.rascal.ast.BasicType.String;
import org.meta_environment.rascal.ast.BasicType.Value;
import org.meta_environment.rascal.ast.BasicType.Void;
import org.meta_environment.rascal.ast.Formal.TypeName;
import org.meta_environment.rascal.ast.FunctionType.TypeArguments;
import org.meta_environment.rascal.ast.Parameters.VarArgs;
import org.meta_environment.rascal.ast.Signature.NoThrows;
import org.meta_environment.rascal.ast.Signature.WithThrows;
import org.meta_environment.rascal.ast.StructuredType.List;
import org.meta_environment.rascal.ast.StructuredType.Map;
import org.meta_environment.rascal.ast.StructuredType.Relation;
import org.meta_environment.rascal.ast.StructuredType.Set;
import org.meta_environment.rascal.ast.StructuredType.Tuple;
import org.meta_environment.rascal.ast.Type.Ambiguity;
import org.meta_environment.rascal.ast.Type.Basic;
import org.meta_environment.rascal.ast.Type.Bracket;
import org.meta_environment.rascal.ast.Type.Function;
import org.meta_environment.rascal.ast.Type.Selector;
import org.meta_environment.rascal.ast.Type.Structured;
import org.meta_environment.rascal.ast.Type.Symbol;
import org.meta_environment.rascal.ast.Type.User;
import org.meta_environment.rascal.ast.Type.Variable;
import org.meta_environment.rascal.ast.TypeArg.Default;
import org.meta_environment.rascal.ast.TypeArg.Named;
import org.meta_environment.rascal.ast.UserType.Name;
import org.meta_environment.rascal.ast.UserType.Parametric;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.Lambda;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousFunctionReferenceError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredTypeError;


public class TypeEvaluator {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static final TypeEvaluator sInstance = new TypeEvaluator();
	private Environment env;
	private final Visitor visitor = new Visitor();

	private TypeEvaluator() {
		super();
	}

	public static TypeEvaluator getInstance() {
		return sInstance;
	}

	public Type eval(org.meta_environment.rascal.ast.Type type, Environment env) {
		this.env = env;
		return type.accept(visitor);
	}

	public Type eval(org.meta_environment.rascal.ast.Type type) {
		return eval(type, null);
	}


	public Type eval(Parameters parameters) {
		return eval(parameters, null);
	}

	public Type eval(org.meta_environment.rascal.ast.Parameters parameters, Environment env) {
		this.env = env;
		return parameters.accept(visitor);
	}
	
	
	public Type eval(org.meta_environment.rascal.ast.Formal formal) {
		return eval(formal, null);
	}

	public Type eval(org.meta_environment.rascal.ast.Formal formal, Environment env) {
		this.env = env;
		return formal.accept(visitor);
	}

	private class Visitor extends NullASTVisitor<Type> {
		@Override
		public Type visitTypeBasic(Basic x) {
			return x.getBasic().accept(this);
		}

		@Override
		public Type visitFormalTypeName(TypeName x) {
			return x.getType().accept(this);
		}

		@Override
		public Type visitFunctionDeclarationDefault(
				org.meta_environment.rascal.ast.FunctionDeclaration.Default x) {
			return x.getSignature().accept(this);
		}

		@Override
		public Type visitParametersDefault(
				org.meta_environment.rascal.ast.Parameters.Default x) {
			return x.getFormals().accept(this);
		}

		@Override
		public Type visitTypeBracket(Bracket x) {
			return x.getType().accept(this);
		}
		
		@Override
		public Type visitTypeSelector(Selector x) {
			return x.getSelector().accept(this);
		}
		
		@Override
		public Type visitDataTypeSelectorSelector(
				org.meta_environment.rascal.ast.DataTypeSelector.Selector x) {
			java.lang.String name = Names.name(x.getSort());
			Type adt = env.lookupAbstractDataType(name);
			
			if (adt == null) {
				throw new UndeclaredTypeError(name, x);
			}
			
			java.lang.String constructor = Names.name(x.getProduction());
			java.util.Set<Type> constructors = env.lookupConstructor(adt, constructor);
			
		    if (constructors.size() == 0) {
		    	throw new UndeclaredTypeError(name + "." + constructor, x);
		    }
		    else if (constructors.size() > 1) {
		    	throw new AmbiguousFunctionReferenceError(name + "." + constructor, x);
		    }
		    else {
		    	return constructors.iterator().next();
		    }
		}
		
		@Override
		public Type visitParametersVarArgs(VarArgs x) {
			Type formals = x.getFormals().accept(this);
			int arity = formals.getArity();

			if (arity == 0) {
				// TODO is this sensible or should we restrict the syntax?
				return tf.tupleType(tf.listType(tf.valueType()), "args");
			}
			
			Type[] types = new Type[arity];
			java.lang.String[] labels = new java.lang.String[arity];
			int i;

			for (i = 0; i < arity - 1; i++) {
				types[i] = formals.getFieldType(i);
				labels[i] = formals.getFieldName(i);
			}

			types[i] = tf.listType(formals.getFieldType(i));
			labels[i] = formals.getFieldName(i);

			return tf.tupleType(types, labels);
		}

		@Override
		public Type visitSignatureNoThrows(NoThrows x) {
			return x.getParameters().accept(this);
		}

		@Override
		public Type visitSignatureWithThrows(WithThrows x) {
			return x.getParameters().accept(this);
		}

		@Override
		public Type visitFormalsDefault(
				org.meta_environment.rascal.ast.Formals.Default x) {
			java.util.List<Formal> list = x.getFormals();
			Object[] typesAndNames = new Object[list.size() * 2];

			for (int formal = 0, index = 0; formal < list.size(); formal++, index++) {
				Formal f = list.get(formal);
				Type type = f.accept(this);
				
				if (type == null) {
					throw new UndeclaredTypeError(f.getType().toString(), f);
				}
				typesAndNames[index++] = type;
				typesAndNames[index] = f.getName().toString();
			}

			return tf.tupleType(typesAndNames);
		}

		@Override
		public Type visitTypeFunction(Function x) {
			return Lambda.getClosureType();
		}

		@Override
		public Type visitFunctionTypeTypeArguments(TypeArguments x) {
			return getArgumentTypes(x.getArguments());
		}

		@Override
		public Type visitBasicTypeBool(Bool x) {
			return tf.boolType();
		}

		@Override
		public Type visitBasicTypeReal(Real x) {
			return tf.realType();
		}

		@Override
		public Type visitBasicTypeInt(Int x) {
			return tf.integerType();
		}

		@Override
		public Type visitBasicTypeLoc(Loc x) {
			return tf.sourceLocationType();
		}

		@Override
		public Type visitBasicTypeString(String x) {
			return tf.stringType();
		}

		@Override
		public Type visitBasicTypeNode(Node x) {
			return tf.nodeType();
		}

		@Override
		public Type visitBasicTypeValue(Value x) {
			return tf.valueType();
		}

		@Override
		public Type visitBasicTypeVoid(Void x) {
			return tf.voidType();
		}

		@Override
		public Type visitTypeStructured(Structured x) {
			return x.getStructured().accept(this);
		}

		@Override
		public Type visitStructuredTypeList(List x) {
			return tf.listType(x.getTypeArg().accept(this));
		}

		@Override
		public Type visitStructuredTypeMap(Map x) {
			return tf.mapType(x.getFirst().accept(this), x.getSecond().accept(
					this));
		}

		@Override
		public Type visitStructuredTypeRelation(Relation x) {
			return getArgumentTypes(x.getArguments());
		}

		private Type getArgumentTypes(java.util.List<TypeArg> args) {
			Type[] fieldTypes = new Type[args.size()];
			java.lang.String[] fieldLabels = new java.lang.String[args.size()];

			int i = 0;
			for (TypeArg arg : args) {
				fieldTypes[i] = arg.getType().accept(this);

				if (arg.isNamed()) {
					fieldLabels[i] = arg.getName().toString();
				} else {
					fieldLabels[i] = "field" + Integer.toString(i);
				}
				i++;
			}

			return tf.relTypeFromTuple(tf.tupleType(fieldTypes, fieldLabels));
		}

		@Override
		public Type visitStructuredTypeSet(Set x) {
			return tf.setType(x.getTypeArg().accept(this));
		}

		@Override
		public Type visitStructuredTypeTuple(Tuple x) {
			java.util.List<TypeArg> args = x.getArguments();
			Type[] fieldTypes = new Type[args.size()];
			java.lang.String[] fieldLabels = new java.lang.String[args.size()];
			
			int i = 0;
			for (TypeArg arg : args) {
				fieldTypes[i] = arg.getType().accept(this);

				if (arg.isNamed()) {
					fieldLabels[i] = Names.name(arg.getName());
				} else {
					fieldLabels[i] = "field" + Integer.toString(i);
				}
				i++;
			}
			return tf.tupleType(fieldTypes, fieldLabels);
		}

		@Override
		public Type visitTypeArgDefault(Default x) {
			return x.getType().accept(this);
		}

		@Override
		public Type visitTypeArgNamed(Named x) {
			return x.getType().accept(this);
		}

		@Override
		public Type visitTypeVariable(Variable x) {
			TypeVar var = x.getTypeVar();
			Type param;

			if (var.isBounded()) {
				param = tf.parameterType(Names.name(var.getName()), var
						.getBound().accept(this));
			} 
			else {
				param = tf.parameterType(Names.name(var.getName()));
			}
			if (env != null) {
				return param.instantiate(env.getStore(), env.getTypeBindings());
			}
			return param;
		}

		@Override
		public Type visitTypeUser(User x) {
			return x.getUser().accept(this);
		}

		@Override
		public Type visitUserTypeParametric(Parametric x) {
			java.lang.String name = Names.name(x.getName());

			if (env != null) {
				Type type = env.lookupAlias(name);
				
				if (type == null) {
					type = env.lookupAbstractDataType(name);
				}
				
				if (type != null) {
					java.util.Map<Type, Type> bindings = new HashMap<Type,Type>();
					Type[] params = new Type[x.getParameters().size()];
					
					int i = 0;
					for (org.meta_environment.rascal.ast.Type param : x.getParameters()) {
						params[i++] = param.accept(this);
					}
					
					type.getTypeParameters().match(tf.tupleType(params), bindings);
					
					type = type.instantiate(new TypeStore(), bindings);
					
					return type.instantiate(env.getStore(), env.getTypeBindings());
				}
			}
			
			throw new UndeclaredTypeError(name, x);
		}

		@Override
		public Type visitUserTypeName(Name x) {
			java.lang.String name = Names.name(x.getName());

			if (env != null) {
				Type type = env.lookupAlias(name);

				if (type != null) {
					return type;
				}
				
				Type tree = env.lookupAbstractDataType(name);

				if (tree != null) {
					return tree;
				}
				
				Type symbol = env.lookupConcreteSyntaxType(name);
				
				if (symbol != null) {
					return symbol;
				}
			}
			
			throw new UndeclaredTypeError(name, x);
		}

		@Override
		public Type visitTypeAmbiguity(Ambiguity x) {
			throw new ImplementationError("Ambiguous type: " + x);
		}
		
		@Override
		public Type visitTypeSymbol(Symbol x) {
			return new ConcreteSyntaxType(x);
		}
	}
}
