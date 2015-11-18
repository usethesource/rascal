package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 * FSTSerializableType acts as a serializer and wrapper for Rascal Types.
 * - On writing a FSTSerializableType oject is written
 * - On reading, the wrapped Type is returned.
 */

enum TYPE {REAL, INT, RAT, LIST, MAP, MAP_NAMED_FIELDS, NUMBER, ALIAS, SET, LOC,STR, NODE, CONSTRUCTOR, CONSTRUCTOR_NAMED_FIELDS, 
	ADT, TUPLE, TUPLE_NAMED_FIELDS, VALUE,VOID,BOOL, PARAMETER, FUNCTION, REIFIED, OVERLOADED, NONTERMINAL, DATETIME};

public class FSTSerializableType extends FSTBasicObjectSerializer implements Serializable {
	
	private static final long serialVersionUID = 5122014003014853428L;
	
	private static transient TypeStore store;
	private static transient TypeFactory tf;
	private static transient RascalTypeFactory rtf;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
		tf = TypeFactory.getInstance();
		rtf = RascalTypeFactory.getInstance();
	}

	private transient Type type;
	
	FSTSerializableType(Type t){
		this.type = t;
	}
	
	public FSTSerializableType() {
	}
	
	public Type getType(){
		return type;
	}
	
	private void writeType(final FSTObjectOutput out, Type t) throws IOException{
		out.writeObject(new FSTSerializableType(t));
	}

	@Override
	public void writeObject(final FSTObjectOutput out, Object toWrite,
			FSTClazzInfo arg2, FSTFieldInfo arg3, int arg4)
					throws IOException {
		
		Type t = ((FSTSerializableType) toWrite).getType();
		
		t.accept(new ITypeVisitor<Void,IOException>() {

			@Override
			public Void visitReal(Type type) throws IOException {
				out.writeObject(TYPE.REAL);
				return null;
			}

			@Override
			public Void visitInteger(Type type) throws IOException {
				out.writeObject(TYPE.INT);
				return null;
			}

			@Override
			public Void visitRational(Type type) throws IOException {
				out.writeObject(TYPE.RAT);
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				out.writeObject(TYPE.LIST);
				writeType(out, type.getElementType());
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				String keyLabel = type.getKeyLabel();
				String valLabel = type.getValueLabel();
				
				if(keyLabel == null && valLabel == null){
					out.writeObject(TYPE.MAP);
				} else {
					out.writeObject(TYPE.MAP_NAMED_FIELDS);
					out.writeObject(keyLabel);
					out.writeObject(valLabel);
				}
				writeType(out, type.getKeyType());
				writeType(out, type.getValueType());
				return null;
			}

			@Override
			public Void visitNumber(Type type) throws IOException {
				out.writeObject(TYPE.NUMBER);
				return null;
			}

			@Override
			public Void visitAlias(Type type) throws IOException {
				
				out.writeObject(TYPE.ALIAS);
				out.writeObject(type.getName());
				writeType(out, type.getAliased());
				writeType(out, type.getTypeParameters());
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				out.writeObject(TYPE.SET);
				writeType(out, type.getElementType());
				return null;
			}

			@Override
			public Void visitSourceLocation(Type type) throws IOException {
				out.writeObject(TYPE.LOC);
				return null;
			}

			@Override
			public Void visitString(Type type) throws IOException {
				out.writeObject(TYPE.STR);
				return null;
			}

			@Override
			public Void visitNode(Type type) throws IOException {
				out.writeObject(TYPE.NODE);
				return null;
			}

			@Override
			public Void visitConstructor(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				if(fieldNames == null){
					out.writeObject(TYPE.CONSTRUCTOR);
				} else {
					out.writeObject(TYPE.CONSTRUCTOR_NAMED_FIELDS);
					out.writeObject(fieldNames);
				}
				out.writeObject(type.getName());
				int arity = type.getArity();
				out.writeObject(arity);
				
				writeType(out, type.getAbstractDataType());
				Type elemType = type.getFieldTypes();
				for(int i = 0; i < arity; i++){
					writeType(out, elemType.getFieldType(i));
	
				}
				return null;
			}

			@Override
			public Void visitAbstractData(Type type) throws IOException {
				out.writeObject(TYPE.ADT);
				out.writeObject(type.getName());
				Type typeParameters = type.getTypeParameters();
				writeType(out, typeParameters);
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				
				if(fieldNames == null){
					out.writeObject(TYPE.TUPLE);
				} else {
					out.writeObject(TYPE.TUPLE_NAMED_FIELDS);
					out.writeObject(fieldNames);
				}
				int arity = type.getArity();
				out.writeObject(arity);
				for(int i = 0; i < arity; i++){
					writeType(out, type.getFieldType(i));
				}
				return null;
			}

			@Override
			public Void visitValue(Type type) throws IOException {
				out.writeObject(TYPE.VALUE);
				return null;
			}

			@Override
			public Void visitVoid(Type type) throws IOException {
				out.writeObject(TYPE.VOID);
				return null;
			}

			@Override
			public Void visitBool(Type type) throws IOException {
				out.writeObject(TYPE.BOOL);
				return null;
			}

			@Override
			public Void visitParameter(Type type) throws IOException {
				out.writeObject(TYPE.PARAMETER);
				out.writeObject(type.getName());
				writeType(out, type.getBound());
				return null;
			}

			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					FunctionType ft = (FunctionType) type;
					out.writeObject(TYPE.FUNCTION);
					writeType(out, ft.getReturnType());
					writeType(out, ft.getArgumentTypes());
					writeType(out, ft.getKeywordParameterTypes());
				} else if(type instanceof ReifiedType){
					//System.out.println("writeType: " + type);
					ReifiedType rt = (ReifiedType) type;
					out.writeObject(TYPE.REIFIED);
					Type elemType = rt.getTypeParameters();  // TODO ok?
					writeType(out, elemType);
				} else if(type instanceof OverloadedFunctionType){
					out.writeObject(TYPE.OVERLOADED);
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					out.writeObject(alternatives.size());
					for(FunctionType ft : alternatives){
						writeType(out, ft);
					}
				} else if(type instanceof NonTerminalType){
					out.writeObject(TYPE.NONTERMINAL);
					NonTerminalType nt = (NonTerminalType) type;
					IConstructor cons = nt.getSymbol();
					out.writeObject(new FSTSerializableIValue(cons));
				} else {
					throw new RuntimeException("External type not supported: " + type);
				}
				return null;
			}

			@Override
			public Void visitDateTime(Type type) throws IOException {
				out.writeObject(TYPE.DATETIME);
				return null;
			}

		});
	}
	
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
	}

	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, final FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws ClassNotFoundException, IOException 
	{
		Type t = readType(in);
		return t;
	}
		
	@SuppressWarnings("deprecation")
	private Type readType(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object o = in.readObject();
		if(o instanceof Type){
			return (Type) o;
		}
		TYPE start = (TYPE) o;
		String [] fieldNames = null;
		String keyLabel = null;
		String valLabel = null;
		
		switch(start){
		case REAL:		return tf.realType();
		
		case INT:		return tf.integerType();
		
		case RAT:		return tf.rationalType();
		
		case LIST:		Type elemType = readType(in);
						return tf.listType(elemType);
		
		case MAP_NAMED_FIELDS:
						keyLabel  = (String) in.readObject();
						valLabel  = (String) in.readObject();
						// fall through to "map" case
						
		case MAP:		Type keyType = readType(in);
						Type valType = readType(in);
						if(keyLabel == null){
							return tf.mapType(keyType, valType);
						}
						return tf.mapType(keyType, keyLabel, valType, valLabel);
						
		case NUMBER:	return tf.numberType();
		
		case ALIAS:		String name = (String) in.readObject();
						Type aliasedType = readType(in);
						Type typeParameters = readType(in);
						return tf.aliasType(store, name, aliasedType, typeParameters);
		
		case SET:		elemType = readType(in);
						return tf.setType(elemType);
						
		case LOC:		return tf.sourceLocationType();
		
		case STR:		return tf.stringType();
		
		case NODE:		return tf.nodeType();
		
		case CONSTRUCTOR_NAMED_FIELDS:
						fieldNames = (String[]) in.readObject();
						// fall through to "constructor" case
			
		case CONSTRUCTOR: 	
						name = (String) in.readObject();
						int arity = (Integer) in.readObject();
						Type adtType = readType(in);
						
						Type declaredAdt = store.lookupAbstractDataType(name);
						
						if(declaredAdt != null){
							adtType = declaredAdt;
						}
						
						Type fieldTypes[] = new Type[arity];

						for(int i = 0; i < arity; i++){
							fieldTypes[i] = readType(in);
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
						
						Type res = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
						if(res == null){
							return tf.constructor(store, adtType, name, typeAndNames);
						} else {
							return res;
						}
						
		case ADT:		name = (String) in.readObject();
						typeParameters = readType(in);
						arity = typeParameters.getArity();
						if(arity > 0){
							Type targs[] = new Type[arity];
							for(int i = 0; i < arity; i++){
								targs[i] = typeParameters.getFieldType(i);
							}
							return tf.abstractDataType(store, name, targs);
						}
						return tf.abstractDataType(store, name);
						
		case TUPLE_NAMED_FIELDS:
						fieldNames = (String[]) in.readObject();
						// fall through to "tuple" case
						
		case TUPLE:		arity = (Integer) in.readObject();
						Type[] elemTypes = new Type[arity];
						for(int i = 0; i < arity; i++){
							elemTypes[i] = readType(in);
						}
						
						if(fieldNames != null){
							return tf.tupleType(elemTypes, fieldNames);
						}
						return tf.tupleType(elemTypes);
						
		case VALUE:		return tf.valueType();
		
		case VOID:		return tf.voidType();
		
		case BOOL:		return tf.boolType();
		
		case FUNCTION:	Type returnType = readType(in);
						Type argumentTypes =  readType(in);
						Type keywordParameterTypes = readType(in);
						return rtf.functionType(returnType, argumentTypes, keywordParameterTypes);
						
		case REIFIED:	elemType = readType(in);
						elemType = elemType.getFieldType(0);
						res = rtf.reifiedType(elemType);
						return res;
						
		case OVERLOADED:
						int n = (Integer) in.readObject();
						Set<FunctionType> alternatives = new HashSet<FunctionType>(n);
						for(int i = 0; i < n; i++){
							alternatives.add((FunctionType)in.readObject());
						}
						return rtf.overloadedFunctionType(alternatives);
						

		case NONTERMINAL:
						IConstructor nt = (IConstructor) in.readObject();
						return rtf.nonTerminalType(nt);
						
		case PARAMETER:	name = (String) in.readObject();
						Type bound = readType(in);
						return tf.parameterType(name, bound);
						
		case DATETIME:	return tf.dateTimeType();
		
		}
		throw new RuntimeException("readType: unhandled case " + start);
	}
}
