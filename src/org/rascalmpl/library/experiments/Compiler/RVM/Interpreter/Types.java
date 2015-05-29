package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class Types {
	
	private final IValueFactory vf;
	private final TypeFactory tf = TypeFactory.getInstance();
	
	public Types(IValueFactory vf) {
		this.vf = vf;
	}
	
	public IValue typeToValue(Type t, RascalExecutionContext rex) {
		
		TypeStore store = new TypeStore();
		IMap definitions = rex.getSymbolDefinitions();
		TypeReifier tr = new TypeReifier(vf);
		tr.declareAbstractDataTypes(definitions, store);
		
		IConstructor symbol = typeToSymbol(t, store);
		
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		bindings.put(RascalValueFactory.TypeParam, t);
		RascalValueFactory.Type.instantiate(bindings);
		
		IValue result = vf.constructor(RascalValueFactory.Type_Reified.instantiate(bindings), symbol, definitions);
		
		return result;
	}
	
	public Type symbolToType(IConstructor symbol, TypeStore store) {
		Type cons = symbol.getConstructorType();
		
		if (cons == RascalValueFactory.Symbol_Int) {
			return tf.integerType();
		}
		else if (cons == RascalValueFactory.Symbol_Real) {
			return tf.realType();
		}
		else if (cons == RascalValueFactory.Symbol_Rat) {
			return tf.rationalType();
		}
		else if (cons == RascalValueFactory.Symbol_Bool) {
			return tf.boolType();
		}
		else if (cons == RascalValueFactory.Symbol_Datetime) {
			return tf.dateTimeType();
		}
		else if (cons == RascalValueFactory.Symbol_Num) {
			return tf.numberType();
		}
		else if (cons == RascalValueFactory.Symbol_Loc) {
			return tf.sourceLocationType();
		}
		else if (cons == RascalValueFactory.Symbol_Adt) {
			return adtToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Alias){
			return aliasToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Bag) {
			throw new CompilerError("bags are not implemented yet");
		}
		else if (cons == RascalValueFactory.Symbol_Cons) {
			return consToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Func) {
			return funcToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Label) {
			return symbolToType((IConstructor) symbol.get("symbol"), store);
		}
		else if (cons == RascalValueFactory.Symbol_Map) {
			return mapToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Node) {
			return tf.nodeType();
		}
		else if (cons == RascalValueFactory.Symbol_Parameter) {
			return tf.parameterType(((IString) symbol.get("name")).getValue(), symbolToType((IConstructor) symbol.get("bound"), store));
		}
		else if (cons == RascalValueFactory.Symbol_BoundParameter) {
			return tf.parameterType(((IString) symbol.get("name")).getValue(), symbolToType((IConstructor) symbol.get("bound"), store));
		}
		else if (cons == RascalValueFactory.Symbol_ReifiedType) {
			return RascalTypeFactory.getInstance().reifiedType(symbolToType((IConstructor) symbol.get("symbol"), store));
		}
		else if (cons == RascalValueFactory.Symbol_Rel) {
			return tf.relTypeFromTuple(symbolsToTupleType((IList) symbol.get("symbols"), store));
		}
		else if (cons == RascalValueFactory.Symbol_ListRel) {
			return tf.lrelTypeFromTuple(symbolsToTupleType((IList) symbol.get("symbols"), store));
		}
		else if (cons == RascalValueFactory.Symbol_Set) {
			return tf.setType(symbolToType((IConstructor) symbol.get("symbol"), store));
		}
		else if (cons == RascalValueFactory.Symbol_List) {
			return tf.listType(symbolToType((IConstructor) symbol.get("symbol"), store));
		}
		else if (cons == RascalValueFactory.Symbol_Str) {
			return tf.stringType();
		}
		else if (cons == RascalValueFactory.Symbol_Tuple) {
			return tupleToType(symbol, store);
		}
		else if (cons == RascalValueFactory.Symbol_Void) {
			return tf.voidType();
		}
		else if (cons == RascalValueFactory.Symbol_Value) {
			return tf.valueType();
		}
		else {
			// We assume the other types are one of the non-terminal symbols
			return RascalTypeFactory.getInstance().nonTerminalType(symbol);
		}
	}

	private Type tupleToType(IConstructor symbol, TypeStore store) {
		return symbolsToTupleType((IList) symbol.get("symbols"), store);
	}

	private Type symbolsToTupleType(IList symbols, TypeStore store) {
		boolean allLabels = true;
		Type[] types = new Type[symbols.length()];
		String[] labels = new String[symbols.length()];
		
		for (int i = 0; i < symbols.length(); i++) {
			IConstructor elem = (IConstructor) symbols.get(i);
			if (elem.getConstructorType() == RascalValueFactory.Symbol_Label) {
				labels[i] = ((IString) elem.get("name")).getValue();
				elem = (IConstructor) elem.get("symbol");
			}
			else {
				allLabels = false;
			}
			
			types[i] = symbolToType(elem, store);
		}
		
		if (allLabels) {
			return tf.tupleType(types, labels);
		}
		else {
			return tf.tupleType(types);
		}
	}
	
	private Type mapToType(IConstructor symbol, TypeStore store) {
		IConstructor from = (IConstructor) symbol.get("from");
		IConstructor to = (IConstructor) symbol.get("to");
		String fromLabel = null;
		String toLabel = null;
		
		if (SymbolAdapter.isLabel(from)) {
			fromLabel = SymbolAdapter.getLabel(from);
			from = (IConstructor) from.get("symbol");
		}
		if (SymbolAdapter.isLabel(to)) {
			toLabel = SymbolAdapter.getLabel(to);
			to = (IConstructor) to.get("symbol");
		}
		if (fromLabel != null && toLabel != null) {
			return tf.mapType(symbolToType(from, store), fromLabel, symbolToType(to, store), toLabel);
		}
		else {
			return tf.mapType(symbolToType(from, store), symbolToType(to, store));
		}
	}

	private Type funcToType(IConstructor symbol, TypeStore store) {
		Type returnType = symbolToType((IConstructor) symbol.get("ret"), store);
		Type parameters = symbolsToTupleType((IList) symbol.get("parameters"), store);
		// TODO: function types shouls also reify keyword parameters
		return RascalTypeFactory.getInstance().functionType(returnType, parameters, tf.voidType());
	}

	private Type consToType(IConstructor symbol, TypeStore store) {
		Type adt = symbolToType((IConstructor) symbol.get("adt"), store);
		IList parameters = (IList) symbol.get("parameters");
		String name = ((IString) symbol.get("name")).getValue();
		Type tupleType = symbolsToTupleType(parameters, store);
		Type t = tf.constructorFromTuple(store, adt, name, tupleType);
		return t;
		
	}

	private Type aliasToType(IConstructor symbol, TypeStore store) {
		String name = ((IString) symbol.get("name")).getValue();
		Type aliased = symbolToType((IConstructor) symbol.get("aliased"), store);
		IList parameters = (IList) symbol.get("parameters");
		
		if (parameters.isEmpty()) {
			return tf.aliasType(store, name, aliased);
		}
		else {
			return tf.aliasTypeFromTuple(store, name, aliased, symbolsToTupleType(parameters, store));
		}
	}

	private Type adtToType(IConstructor symbol, TypeStore store) {
		String name = ((IString) symbol.get("name")).getValue();
		Type adt = store.lookupAbstractDataType(name);
		
		if (adt == null) {
			Type params = symbolsToTupleType((IList) symbol.get("parameters"), store);
			if (params.isBottom() || params.getArity() == 0) {
				adt = tf.abstractDataType(store, name);
			}
			else {
				adt = tf.abstractDataTypeFromTuple(store, name, params);
			}
		}
		
		return adt;
	}

	public IConstructor typeToSymbol(Type type, final TypeStore typeStore) {
		
		return (IConstructor) type.accept(new ITypeVisitor<IValue, RuntimeException>() {
			
			private Map<Type,IValue> cache = new HashMap<Type, IValue>();
			
			@Override
			public IValue visitReal(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Real);
			}

			@Override
			public IValue visitInteger(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Int);
			}

			@Override
			public IValue visitRational(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Rat);
			}

			@Override
			public IValue visitList(Type type) {
				if(type.isListRelation()) {
					IListWriter w = vf.listWriter();

					if (type.hasFieldNames()) {
						for (int i = 0; i < type.getArity(); i++) {
							w.append(vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getFieldName(i)), type.getFieldType(i).accept(this)));
						}
					} else {
						if (type.getFieldTypes().isBottom()) {
							return vf.constructor(RascalValueFactory.Symbol_List, vf.constructor(RascalValueFactory.Symbol_Void));
						}
				  
						for (Type f : type.getFieldTypes()) {
							w.append(f.accept(this));
						}
					}
				
					return vf.constructor(RascalValueFactory.Symbol_ListRel, w.done());
				}
				return vf.constructor(RascalValueFactory.Symbol_List, type.getElementType().accept(this));
			}

			@Override
			public IValue visitMap(Type type) {
				if (type.hasFieldNames()) {
					return vf.constructor(RascalValueFactory.Symbol_Map, vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getKeyLabel()), type.getKeyType().accept(this)), vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getValueLabel()), type.getValueType().accept(this)));
				} else {
					return vf.constructor(RascalValueFactory.Symbol_Map, type.getKeyType().accept(this), type.getValueType().accept(this));
				}
			}

			@Override
			public IValue visitNumber(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Num);
			}

			@Override
			public IValue visitAlias(Type type) {
				IListWriter w = vf.listWriter();
				Type params = type.getTypeParameters();
				
				if (params.getArity() > 0) {
					for (Type t : params) {
						w.append(t.accept(this));
					}
				}
				
				return vf.constructor(RascalValueFactory.Symbol_Alias, vf.string(type.getName()), w.done(), type.getAliased().accept(this));
			}

			@Override
			public IValue visitSet(Type type) {
				if(type.isRelation()) {
					IListWriter w = vf.listWriter();

					if (type.hasFieldNames()) {
						for (int i = 0; i < type.getArity(); i++) {
							w.append(vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getFieldName(i)), type.getFieldType(i).accept(this)));
						}
					} else {
						if (type.getFieldTypes().isBottom()) {
							return vf.constructor(RascalValueFactory.Symbol_Set, vf.constructor(RascalValueFactory.Symbol_Void));
						}
						for (Type f : type.getFieldTypes()) {
							w.append(f.accept(this));
						}
					}
				
					return vf.constructor(RascalValueFactory.Symbol_Rel, w.done());
				}
				return vf.constructor(RascalValueFactory.Symbol_Set, type.getElementType().accept(this));
			}

			@Override
			public IValue visitSourceLocation(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Loc);
			}

			@Override
			public IValue visitString(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Str);
			}

			@Override
			public IValue visitNode(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Node);
			}
			
			@Override
			public IValue visitConstructor(Type type) {
				IValue adt = cache.get(type.getAbstractDataType());
				
				if (adt == null) {
					visitAbstractData(type.getAbstractDataType());
				}
				
				IValue result = cache.get(type);
				if (result == null) {
					IListWriter w = vf.listWriter();

					if (type.hasFieldNames()) {
						for (int i = 0; i < type.getArity(); i++) {
							w.append(vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getFieldName(i)), type.getFieldType(i).accept(this)));
						}
					}
					else {
						for (Type field : type.getFieldTypes()) {
							w.append(field.accept(this));
						}
					}
					result = vf.constructor(RascalValueFactory.Symbol_Cons, vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getName()), adt), w.done());
					cache.put(type, result);
				}
				
				return result;
			}
			
			@Override
			public IValue visitAbstractData(Type type) {
				IValue sym = cache.get(type);
				
				if (sym == null) {
					IListWriter w = vf.listWriter();
					Type params = type.getTypeParameters();
					if (params.getArity() > 0) {
						for (Type param : params) {
							w.append(param.accept(this));
						}
					}
					
					sym = vf.constructor(RascalValueFactory.Symbol_Adt, vf.string(type.getName()), w.done());
					cache.put(type, sym);			

					// make sure to find the type by the uninstantiated adt
					Type adt = typeStore.lookupAbstractDataType(type.getName());
					for (Type cons : typeStore.lookupAlternatives(adt)) {
						cons.accept(this);
					}
				}
				
				return sym;
			}

			@Override
			public IValue visitTuple(Type type) {
				IListWriter w = vf.listWriter();
				
				if (type.hasFieldNames()) {
					for (int i = 0; i < type.getArity(); i++) {
						w.append(vf.constructor(RascalValueFactory.Symbol_Label, vf.string(type.getFieldName(i)), type.getFieldType(i).accept(this)));
					}
				}
				else {
					for (Type f : type) {
						w.append(f.accept(this));
					}
				}

				return vf.constructor(RascalValueFactory.Symbol_Tuple, w.done());
			}

			@Override
			public IValue visitValue(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Value);
			}

			@Override
			public IValue visitVoid(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Void);
			}

			@Override
			public IValue visitBool(Type boolType) {
				return vf.constructor(RascalValueFactory.Symbol_Bool);
			}

			@Override
			public IValue visitParameter(Type parameterType) {
				return vf.constructor(RascalValueFactory.Symbol_BoundParameter, vf.string(parameterType.getName()), parameterType.getBound().accept(this));
			}

			@Override
			public IValue visitExternal(Type externalType) {
				if (externalType instanceof NonTerminalType) {
					return visitNonTerminalType((NonTerminalType) externalType);
				}
				else if (externalType instanceof ReifiedType) {
					return visitReifiedType((ReifiedType) externalType);
				}
				else if (externalType instanceof FunctionType) {
					return visitFunctionType((FunctionType) externalType);
				}
				
				throw new CompilerError("unable to reify " + externalType);
			}

			private IValue visitFunctionType(FunctionType externalType) {
				IListWriter w = vf.listWriter();
				for (Type arg : externalType.getArgumentTypes()) {
					w.append(arg.accept(this));
				}
				
				return vf.constructor(RascalValueFactory.Symbol_Func, externalType.getReturnType().accept(this), w.done());
			}

			private IValue visitReifiedType(ReifiedType externalType) {
				return vf.constructor(RascalValueFactory.Symbol_ReifiedType, externalType.getTypeParameters().getFieldType(0).accept(this));
			}

			private IValue visitNonTerminalType(NonTerminalType externalType) {
				return externalType.getSymbol();
			}

			@Override
			public IValue visitDateTime(Type type) {
				return vf.constructor(RascalValueFactory.Symbol_Datetime);
			}

		});	
	}

}
