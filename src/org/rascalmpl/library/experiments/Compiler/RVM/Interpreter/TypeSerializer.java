package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.types.ReifiedType;

public class TypeSerializer {
	
	private TypeStore store;
	private TypeFactory tf = TypeFactory.getInstance();
	private RascalTypeFactory rtf;
	
	public TypeSerializer(TypeStore ts){
		store = ts;
		rtf = RascalTypeFactory.getInstance();
	}

	public void writeType(final java.io.ObjectOutputStream stream, final Type t) throws IOException{
		//System.out.println("writeType: " + t);System.out.flush();
		t.accept(new ITypeVisitor<Void,IOException>() {

			@Override
			public Void visitReal(Type type) throws IOException {
				stream.writeObject("real");
				return null;
			}

			@Override
			public Void visitInteger(Type type) throws IOException {
				stream.writeObject("int");
				return null;
			}

			@Override
			public Void visitRational(Type type) throws IOException {
				stream.writeObject("rat");
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				stream.writeObject("list");
				writeType(stream, type.getElementType());
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				stream.writeObject("map");
				writeType(stream, type.getKeyType());
				writeType(stream, type.getValueType());
				return null;
			}

			@Override
			public Void visitNumber(Type type) throws IOException {
				stream.writeObject("number");
				return null;
			}

			@Override
			public Void visitAlias(Type type) throws IOException {
				stream.writeObject("alias");
				stream.writeObject(type.getName());
				writeType(stream, type.getAliased());
				writeType(stream, type.getTypeParameters());
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				stream.writeObject("set");
				writeType(stream, type.getElementType());
				return null;
			}

			@Override
			public Void visitSourceLocation(Type type) throws IOException {
				stream.writeObject("loc");
				return null;
			}

			@Override
			public Void visitString(Type type) throws IOException {
				stream.writeObject("str");
				return null;
			}

			@Override
			public Void visitNode(Type type) throws IOException {
				stream.writeObject("node");
				return null;
			}

			@Override
			public Void visitConstructor(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				if(fieldNames == null){
					stream.writeObject("constructor");
				} else {
					stream.writeObject("constructor_named_fields");
					stream.writeObject(fieldNames);
				}
				stream.writeObject(type.getName());
				int arity = type.getArity();
				stream.writeObject(arity);
				writeType(stream, type.getAbstractDataType());
				Type elemType = type.getFieldTypes();
				for(int i = 0; i < arity; i++){
					writeType(stream, elemType.getFieldType(i));
				}
				return null;
			}

			@Override
			public Void visitAbstractData(Type type) throws IOException {
				stream.writeObject("adt");
				stream.writeObject(type.getName());
				Type typeParameters = type.getTypeParameters();
				writeType(stream, typeParameters);
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				
				if(fieldNames == null){
					stream.writeObject("tuple");
				} else {
					stream.writeObject("tuple_named_fields");
					stream.writeObject(fieldNames);
				}
				int arity = type.getArity();
				stream.writeObject(arity);
				for(int i = 0; i < arity; i++){
					writeType(stream, type.getFieldType(i));
				}
				return null;
			}

			@Override
			public Void visitValue(Type type) throws IOException {
				stream.writeObject("value");
				return null;
			}

			@Override
			public Void visitVoid(Type type) throws IOException {
				stream.writeObject("void");
				return null;
			}

			@Override
			public Void visitBool(Type type) throws IOException {
				stream.writeObject("bool");
				return null;
			}

			@Override
			public Void visitParameter(Type type) throws IOException {
				stream.writeObject("parameter");
				stream.writeObject(type.getName());
				writeType(stream, type.getBound());
				return null;
			}

			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					FunctionType ft = (FunctionType) type;
					stream.writeObject("function");
					writeType(stream, ft.getReturnType());
					writeType(stream, ft.getArgumentTypes());
					writeType(stream, ft.getKeywordParameterTypes());
				} else if(type instanceof ReifiedType){
					//System.out.println("writeType: " + type);
					ReifiedType rt = (ReifiedType) type;
					stream.writeObject("reified");
					Type elemType = rt.getTypeParameters();  // TODO ok?
					writeType(stream, elemType);
				} else if(type instanceof OverloadedFunctionType){
					stream.writeObject("overloaded");
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					stream.writeObject(alternatives.size());
					for(FunctionType ft : alternatives){
						writeType(stream, ft);
					}
				} else if(type instanceof NonTerminalType){
					stream.writeObject("nonterminal");
					NonTerminalType nt = (NonTerminalType) type;
					IConstructor cons = nt.getSymbol();
					stream.writeObject(new SerializableRascalValue<IConstructor>(cons));
				} else {
					throw new RuntimeException("External type not supported: " + type);
				}
				return null;
			}

			@Override
			public Void visitDateTime(Type type) throws IOException {
				stream.writeObject("datetime");
				return null;
			}

		});
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Type readType(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException{
		
		String start = (String) stream.readObject();
		String [] fieldNames = null;
		
		//System.out.println("readType: " + start); System.out.flush();
		
		switch(start){
		case "real":	return tf.realType();
		
		case "int":		return tf.integerType();
		
		case "rat":		return tf.rationalType();
		
		case "list":	Type elemType = readType(stream);
						return tf.listType(elemType);
						
		case "map":		Type keyType = readType(stream);
						Type valType = readType(stream);
						return tf.mapType(keyType, valType);
						
		case "number":	return tf.numberType();
		
		case "alias":	String name = (String) stream.readObject();
						Type aliasedType = readType(stream);
						Type typeParameters = readType(stream);
						return tf.aliasType(store, name, aliasedType, typeParameters);
		
		case "set":		elemType = readType(stream);
						return tf.setType(elemType);
						
		case "loc":		return tf.sourceLocationType();
		
		case "str":		return tf.stringType();
		
		case "node":	return tf.nodeType();
		
		case "constructor_named_fields":
						fieldNames = (String[]) stream.readObject();
			
		case "constructor": 	
						name = (String) stream.readObject();
						int arity = (Integer) stream.readObject();
						Type adtType = readType(stream);
						
						Type declaredAdt = store.lookupAbstractDataType(name);
						
						if(declaredAdt != null){
							adtType = declaredAdt;
						}
						
						Type fieldTypes[] = new Type[arity];

						for(int i = 0; i < arity; i++){
							fieldTypes[i] = readType(stream);
						}
						
						if(fieldNames == null){
							Type res = store.lookupConstructor(adtType, name, tf.tupleType(fieldTypes));
							if(res == null) {
								return tf.constructor(store, adtType, name, fieldTypes);
							} else {
								return res;
							}
						}
						Object[] typeAndNames = new Object[2*arity];
						for(int i = 0; i < arity; i++){
							typeAndNames[2 * i] =  fieldTypes[i];
							typeAndNames[2 * i + 1] = fieldNames[i];
						}
						
						Type res = store.lookupConstructor(adtType, name, tf.tupleType(fieldTypes));
						if(res == null){
							return tf.constructor(store, adtType, name, typeAndNames);
						} else {
							return res;
						}
						
		case "adt":		name = (String) stream.readObject();
						typeParameters = readType(stream);
						return tf.abstractDataType(store, name, typeParameters);
						
		case "tuple_named_fields":
						fieldNames = (String[]) stream.readObject();
						// fall through to "tuple" case
						
		case "tuple":	arity = (Integer) stream.readObject();
						Type[] elemTypes = new Type[arity];
						for(int i = 0; i < arity; i++){
							elemTypes[i] = readType(stream);
						}
						
						if(fieldNames != null){
							return tf.tupleType(elemTypes, fieldNames);
						}
						return tf.tupleType(elemTypes);
						
		case "value":	return tf.valueType();
		
		case "void":	return tf.voidType();
		
		case "bool":	return tf.boolType();
		
		case "function":
						Type returnType = readType(stream);
						Type argumentTypes =  readType(stream);
						Type keywordParameterTypes = readType(stream);
						return rtf.functionType(returnType, argumentTypes, keywordParameterTypes);
						
		case "reified":
						elemType = readType(stream);
						elemType = elemType.getFieldType(0);
						res = rtf.reifiedType(elemType);
						//System.out.println("readType: " + res);
						return res;
						
		case "overloaded":
						int n = (Integer) stream.readObject();
						Set<FunctionType> alternatives = new HashSet<FunctionType>(n);
						for(int i = 0; i < n; i++){
							alternatives.add((FunctionType)readType(stream));
						}
						return rtf.overloadedFunctionType(alternatives);
						

		case "nonterminal":
						IConstructor nt = ((SerializableRascalValue<IConstructor>) stream.readObject()).getValue();
						return rtf.nonTerminalType(nt);
						
		case "parameter":
						name = (String) stream.readObject();
						Type bound = readType(stream);
						return tf.parameterType(name, bound);
						
		case "datetime":	
						return tf.dateTimeType();
		
		}
		throw new RuntimeException("readType: unhandled case " + start);
	}
}
