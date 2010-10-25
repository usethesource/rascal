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
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;

public class TypeEvaluator {
	private final static TypeFactory tf = TypeFactory.getInstance();
	
	private final Visitor visitor = new Visitor();
	private final Environment env;

	private GlobalEnvironment heap;
	
	public TypeEvaluator(Environment env, GlobalEnvironment heap) {
		super();
		
		this.env = env;
		this.heap = heap;
	}
	
	public Type eval(org.rascalmpl.ast.Type type) {
		return type.accept(visitor);
	}

	public Type eval(org.rascalmpl.ast.Parameters parameters) {
		return parameters.accept(visitor);
	}

	public Type eval(org.rascalmpl.ast.Formal formal) {
		return formal.accept(visitor);
	}
	
	public Type eval(Signature signature) {
		return signature.accept(visitor);
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
				org.rascalmpl.ast.FunctionDeclaration.Default x) {
			return x.getSignature().accept(this);
		}

		@Override
		public Type visitParametersDefault(
				org.rascalmpl.ast.Parameters.Default x) {
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
				org.rascalmpl.ast.DataTypeSelector.Selector x) {
			Type adt;
			QualifiedName sort = x.getSort();
			java.lang.String name = Names.typeName(sort);
			
			if (Names.isQualified(sort)) {
				ModuleEnvironment mod = heap.getModule(Names.moduleName(sort));
				
				if (mod == null) {
					throw new UndeclaredModuleError(Names.moduleName(sort), sort);
				}
				
				adt = mod.lookupAbstractDataType(name);
			}
			else {
				adt = env.lookupAbstractDataType(name);
			}
			
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
			return RascalTypeFactory.getInstance().functionType(x.getType().accept(this), x.getParameters().accept(this));
		}

		@Override
		public Type visitSignatureWithThrows(WithThrows x) {
			return RascalTypeFactory.getInstance().functionType(x.getType().accept(this), x.getParameters().accept(this));
		}

		@Override
		public Type visitFormalsDefault(
				org.rascalmpl.ast.Formals.Default x) {
			java.util.List<Formal> list = x.getFormals();
			Object[] typesAndNames = new Object[list.size() * 2];

			for (int formal = 0, index = 0; formal < list.size(); formal++, index++) {
				Formal f = list.get(formal);
				Type type = f.accept(this);
				
				if (type == null) {
					throw new UndeclaredTypeError(f.getType().toString(), f);
				}
				typesAndNames[index++] = type;
				typesAndNames[index] = Names.name(f.getName());
			}

			return tf.tupleType(typesAndNames);
		}

		@Override
		public Type visitTypeFunction(Function x) {
			return x.getFunction().accept(this);
		}
		
		@Override
		public Type visitFunctionTypeTypeArguments(TypeArguments x) {
			Type returnType = x.getType().accept(this);
			Type argTypes = getArgumentTypes(x.getArguments());
			return RascalTypeFactory.getInstance().functionType(returnType, argTypes);
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
		public Type visitBasicTypeNum(Num x) {
			return tf.numberType();
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
		public Type visitBasicTypeAmbiguity(
				org.rascalmpl.ast.BasicType.Ambiguity x) {
			throw new ImplementationError("Ambiguity detected in BasicType", x.getLocation());
		}
		
		@Override
		public Type visitBasicTypeBag(Bag x) {
			throw new NonWellformedTypeError("bag should have one type argument, like bag[value].", x);
		}
		
		@Override
		public Type visitBasicTypeLex(Lex x) {
			throw new NonWellformedTypeError("lex should have one type argument, like lex[Id].", x);
		}
		
		@Override
		public Type visitBasicTypeList(
				org.rascalmpl.ast.BasicType.List x) {
			throw new NonWellformedTypeError("list should have one type argument, like list[value].", x);
		}
		
		@Override
		public Type visitBasicTypeMap(Map x) {
			throw new NonWellformedTypeError("map should have at two type arguments, like map[value,value].", x);
		}
		
		@Override
		public Type visitBasicTypeReifiedType(ReifiedType x) {
			throw new NonWellformedTypeError("type should have at one type argument, like type[value].", x);
		}
		
		@Override
		public Type visitBasicTypeRelation(Relation x) {
			throw new NonWellformedTypeError("rel should have at least one type argument, like rel[value,value].", x);
		}
		
		@Override
		public Type visitBasicTypeSet(Set x) {
			throw new NonWellformedTypeError("set should have one type argument, like set[value].", x);
		}
		
		@Override
		public Type visitBasicTypeTuple(Tuple x) {
			throw new NonWellformedTypeError("tuple should have type arguments, like tuple[value,value].", x);
		}

		@Override
		public Type visitBasicTypeDateTime(DateTime x) {
			return tf.dateTimeType();
		}
		
		@Override
		public Type visitTypeStructured(Structured x) {
			return x.getStructured().accept(this);
		}

		private Type getArgumentTypes(java.util.List<TypeArg> args) {
			Type[] fieldTypes = new Type[args.size()];
			java.lang.String[] fieldLabels = new java.lang.String[args.size()];

			int i = 0;
			boolean allLabeled = true;
			boolean someLabeled = false;
	
			for (TypeArg arg : args) {
				fieldTypes[i] = arg.getType().accept(this);

				if (arg.isNamed()) {
					fieldLabels[i] = Names.name(arg.getName());
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
				return tf.tupleType(fieldTypes);
			}
			
			return tf.tupleType(fieldTypes, fieldLabels);
		}

		@Override
		public Type visitStructuredTypeDefault(
				org.rascalmpl.ast.StructuredType.Default x) {
		   return x.getBasicType().accept(new BasicTypeEvaluator(env, getArgumentTypes(x.getArguments()), null));
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
		public Type visitBasicTypeReifiedAdt(ReifiedAdt x) {
			throw new NonWellformedTypeError("a reified adt should be one of adt(str name), adt(str name, list[type[&T]] parameters), adt(str name, list[Constructor] constructors).", x);
		}
		
		@Override
		public Type visitBasicTypeReifiedConstructor(ReifiedConstructor x) {
			throw new NonWellformedTypeError("a reified constructor declaration should look like contructor(str name, type[&T1] arg1, ...)", x);
		}
		
		@Override
		public Type visitBasicTypeReifiedNonTerminal(ReifiedNonTerminal x) {
			throw new NonWellformedTypeError("a reified non-terminal type should look like non-terminal(Symbol symbol, x)", x);
		}
		
		@Override
		public Type visitBasicTypeReifiedReifiedType(ReifiedReifiedType x) {
			throw new NonWellformedTypeError("a reified reified type should look like reified(type[&T] arg)", x);
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
				return param.instantiate(env.getTypeBindings());
			}
			return param;
		}

		@Override
		public Type visitTypeUser(User x) {
			return x.getUser().accept(this);
		}
		
		private Environment getEnvironmentForName(QualifiedName name) {
			if (Names.isQualified(name)) {
				if (heap == null) {
					throw new ImplementationError("no heap to look up module");
				}
				ModuleEnvironment mod = heap.getModule(Names.moduleName(name));
				if (mod == null) {
					throw new UndeclaredModuleError(Names.moduleName(name), name);
				}
				return mod;
			}
			
			return env;
		}
		
		@Override
		public Type visitUserTypeParametric(Parametric x) {
			java.lang.String name;
			Type type = null;
			Environment theEnv = getEnvironmentForName(x.getName());

			name = Names.typeName(x.getName());

			if (theEnv != null) {
				type = theEnv.lookupAlias(name);

				if (type == null) {
					type = theEnv.lookupAbstractDataType(name);
				}
			}

			if (type != null) {
				java.util.Map<Type, Type> bindings = new HashMap<Type,Type>();
				Type[] params = new Type[x.getParameters().size()];

				int i = 0;
				for (org.rascalmpl.ast.Type param : x.getParameters()) {
					params[i++] = param.accept(this);
				}

				// this has side-effects that we might need?
				type.getTypeParameters().match(tf.tupleType(params), bindings);
				
				// Note that instantiation use type variables from the current context, not the declaring context
				Type outerInstance = type.instantiate(env.getTypeBindings());
				return outerInstance.instantiate(bindings);
			}
			
			throw new UndeclaredTypeError(name, x);
		}

		@Override
		public Type visitUserTypeName(Name x) {
			Environment theEnv = getEnvironmentForName(x.getName());
			java.lang.String name = Names.typeName(x.getName());

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
			
			throw new UndeclaredTypeError(name, x);
		}

		@Override
		public Type visitTypeAmbiguity(Ambiguity x) {
			throw new ImplementationError("Ambiguous type: " + x);
		}
		
		@Override
		public Type visitTypeSymbol(Symbol x) {
			return RascalTypeFactory.getInstance().nonTerminalType(x);
		}
	}
}

class BasicTypeEvaluator extends NullASTVisitor<Type> {
	private final static TypeFactory tf = TypeFactory.getInstance();
	private final Type typeArgument;
	private final IValue[] valueArguments; // for adt, constructor and non-terminal representations
	private final Environment env;
	
	public BasicTypeEvaluator(Environment env, Type argumentTypes, IValue[] valueArguments) {
		this.env = env;
		this.typeArgument = argumentTypes;
		this.valueArguments = valueArguments;
	}
	
	@Override
	public Type visitBasicTypeBag(Bag x) {
		throw new NotYetImplemented(x);
	}
	
	@Override
	public Type visitBasicTypeList(
			org.rascalmpl.ast.BasicType.List x) {
		if (typeArgument.getArity() == 1) {
			return tf.listType(typeArgument.getFieldType(0));
		}
		throw new NonWellformedTypeError("list should have exactly one type argument, like list[value]", x);
	}
	
	@Override
	public Type visitBasicTypeTuple(
			org.rascalmpl.ast.BasicType.Tuple x) {
		return typeArgument;
	}
	
	@Override
	public Type visitBasicTypeInt(Int x) {
		if (typeArgument.getArity() == 0) {
			return tf.integerType();
		}
		throw new NonWellformedTypeError("int cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeNum(Num x) {
		if (typeArgument.getArity() == 0) {
			return tf.numberType();
		}
		throw new NonWellformedTypeError("num cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeAmbiguity(
			org.rascalmpl.ast.BasicType.Ambiguity x) {
		throw new ImplementationError("Detected ambiguity in BasicType", x.getLocation());
	}
	
	@Override
	public Type visitBasicTypeBool(Bool x) {
		if (typeArgument.getArity() == 0) {
			return tf.boolType();
		}
		throw new NonWellformedTypeError("bool cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeLex(Lex x) {
		throw new NotYetImplemented(x);
	}
	
	@Override
	public Type visitBasicTypeLoc(Loc x) {
		if (typeArgument.getArity() == 0) {
			return tf.sourceLocationType();
		}
		throw new NonWellformedTypeError("loc cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeMap(Map x) {
		if (typeArgument.getArity() == 2) {
			return tf.mapTypeFromTuple(typeArgument);
		}
		throw new NonWellformedTypeError("map should have exactly two type arguments, like map[value,value]", x);
	}
	
	@Override
	public Type visitBasicTypeNode(Node x) {
		if (typeArgument.getArity() == 0) {
			return tf.nodeType();
		}
		throw new NonWellformedTypeError("node cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeReal(Real x) {
		if (typeArgument.getArity() == 0) {
			return tf.realType();
		}
		throw new NonWellformedTypeError("real cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeReifiedType(ReifiedType x) {
		if (typeArgument.getArity() == 1) {
			return RascalTypeFactory.getInstance().reifiedType(typeArgument.getFieldType(0));
		}
		throw new NonWellformedTypeError("type should have exactly one type argument, like type[value]", x);
	}
	
	@Override
	public Type visitBasicTypeRelation(Relation x) {
		return tf.relTypeFromTuple(typeArgument);
	}
	
	@Override
	public Type visitBasicTypeSet(Set x) {
		if (typeArgument.getArity() == 1) {
			return tf.setType(typeArgument.getFieldType(0));
		}
		throw new NonWellformedTypeError("set should have exactly one type argument, like set[value]", x);
	}
	
	@Override
	public Type visitBasicTypeString(String x) {
		if (typeArgument.getArity() == 0) {
			return tf.stringType();
		}
		throw new NonWellformedTypeError("string cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeValue(Value x) {
		if (typeArgument.getArity() == 0) {
			return tf.valueType();
		}
		throw new NonWellformedTypeError("value cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeVoid(Void x) {
		if (typeArgument.getArity() == 0) {
			return tf.voidType();
		}
		throw new NonWellformedTypeError("void cannot have type arguments.", x);
	}
	
	@Override
	public Type visitBasicTypeReifiedAdt(ReifiedAdt x) {
		java.lang.String name;
		
		if (valueArguments == null) {
			throw new ImplementationError("missing value arguments to construct adt type");
		}
		
		if (valueArguments.length >= 1) {
			if (valueArguments[0].getType().isStringType()) {
				name = ((IString) valueArguments[0]).getValue();
				Type adt = env.lookupAbstractDataType(name);
				
				if (adt == null) {
					// TODO this should be a dynamic error, not a static one
					throw new UndeclaredTypeError(name, x);
				}
			}
			else {
				throw new NonWellformedTypeError("a reified adt should have a name as first argument, like adt(str name)", x);
			}
			
			if (valueArguments.length == 1) {
				return tf.abstractDataType(env.getStore(), name);
			}
			
			if (valueArguments.length == 2) {
				if (valueArguments[1].getType().isListType()) {
					IList list = (IList) valueArguments[1];
					Type[] args = new Type[list.length()];
					int i = 0;
					for (IValue arg : list) {
						Type argType = arg.getType();

						if (argType instanceof org.rascalmpl.interpreter.types.ReifiedType) {
							args[i++] = argType.getTypeParameters().getFieldType(0);
						}
						else {
							throw new NonWellformedTypeError("type parameters of an adt should be reified types, as in adt(str name, list[type[value]] parameters", x);
						}
					}
					
					return tf.abstractDataType(env.getStore(), name, args);
				}
			}
		}
		
		
		throw new NonWellformedTypeError("a reified adt should be one of adt(str name), adt(str name, list[type[value]] parameters).", x);
	}
	
	@Override
	public Type visitBasicTypeReifiedConstructor(ReifiedConstructor x) {
		throw new ImplementationError("Did not expect to handle constructors in type evaluator");
	}
	
	@Override
	public Type visitBasicTypeReifiedNonTerminal(ReifiedNonTerminal x) {
		if (valueArguments == null) {
			throw new ImplementationError("missing value arguments to construct non-terminal type");
		}
		
		if (valueArguments.length == 1) {
			if (valueArguments[0].getType() == Factory.Symbol) {
				return RascalTypeFactory.getInstance().reifiedType(RascalTypeFactory.getInstance().nonTerminalType((IConstructor) valueArguments[0]));
			}
		}
		
		throw new NonWellformedTypeError("a reified non-terminal type should look like non-terminal(Symbol symbol, x)", x);
	}
	
	@Override
	public Type visitBasicTypeReifiedFunction(ReifiedFunction x) {
		if (typeArgument.getArity() < 1) {
			throw new ImplementationError("a reified function type has at least a return type, as in fun(int).");
		}

		throw new NotYetImplemented(x);
	}
	
	@Override
	public Type visitBasicTypeReifiedReifiedType(ReifiedReifiedType x) {
		if (valueArguments == null) {
			throw new ImplementationError("missing value arguments to construct non-terminal type");
		}
		
		if (valueArguments.length == 1) {
			if (valueArguments[0].getType() instanceof org.rascalmpl.interpreter.types.ReifiedType) {
				return RascalTypeFactory.getInstance().reifiedType(Typeifier.toType((IConstructor) valueArguments[0]));
			}
		}
		
		throw new NonWellformedTypeError("a reified reified type should look like reified(type[&T] arg)", x);
	}
	
	@Override
	public Type visitBasicTypeDateTime(DateTime x) {
		if (typeArgument.getArity() == 0) {
			return tf.dateTimeType();
		}
		throw new NonWellformedTypeError("datetime cannot have type arguments.", x);
	}
}
