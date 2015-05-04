package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.values.uptr.Factory;

import de.ruedigermoeller.serialization.FSTBasicObjectSerializer;
import de.ruedigermoeller.serialization.FSTClazzInfo;
import de.ruedigermoeller.serialization.FSTClazzInfo.FSTFieldInfo;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;


/**
 * FSTSerializableType: a wrapper and serializer for Rascal Types.
 *
 */
public class FSTSerializableType extends FSTBasicObjectSerializer implements Serializable {
	
	private static final long serialVersionUID = 5122014003014853428L;
	
	private static transient TypeStore store;
	private static transient TypeFactory tf;
	private static transient RascalTypeFactory rtf;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		store = ts;
		store.extendStore(Factory.getStore());
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
		//System.out.println("FSTSerializableType.writeType: " + t);
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
				out.writeObject("real");
				return null;
			}

			@Override
			public Void visitInteger(Type type) throws IOException {
				out.writeObject("int");
				return null;
			}

			@Override
			public Void visitRational(Type type) throws IOException {
				out.writeObject("rat");
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				out.writeObject("list");
				writeType(out, type.getElementType());
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				String keyLabel = type.getKeyLabel();
				String valLabel = type.getValueLabel();
				
				if(keyLabel == null && valLabel == null){
					out.writeObject("map");
				} else {
					out.writeObject("map_named_fields");
					out.writeObject(keyLabel);
					out.writeObject(valLabel);
				}
				writeType(out, type.getKeyType());
				writeType(out, type.getValueType());
				return null;
			}

			@Override
			public Void visitNumber(Type type) throws IOException {
				out.writeObject("number");
				return null;
			}

			@Override
			public Void visitAlias(Type type) throws IOException {
				
				//System.out.println("writeType, alias: " + type.getName() + ", " + type.getAliased());
				out.writeObject("alias");
				out.writeObject(type.getName());
				writeType(out, type.getAliased());
				writeType(out, type.getTypeParameters());
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				out.writeObject("set");
				writeType(out, type.getElementType());
				return null;
			}

			@Override
			public Void visitSourceLocation(Type type) throws IOException {
				out.writeObject("loc");
				return null;
			}

			@Override
			public Void visitString(Type type) throws IOException {
				out.writeObject("str");
				return null;
			}

			@Override
			public Void visitNode(Type type) throws IOException {
				out.writeObject("node");
				return null;
			}

			@Override
			public Void visitConstructor(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				if(fieldNames == null){
					out.writeObject("constructor");
				} else {
					out.writeObject("constructor_named_fields");
					out.writeObject(fieldNames);
				}
				out.writeObject(type.getName());
				int arity = type.getArity();
				out.writeObject(arity);
				
				//System.out.println("writeType, constructor " + type.getName() + ", " + type.getAbstractDataType());System.out.flush();
				writeType(out, type.getAbstractDataType());
				Type elemType = type.getFieldTypes();
				for(int i = 0; i < arity; i++){
					writeType(out, elemType.getFieldType(i));
					//System.out.println("writeType, constructor elemType[" + i + "] = " + elemType.getFieldType(i));System.out.flush();
				}
				
				return null;
			}

			@Override
			public Void visitAbstractData(Type type) throws IOException {
				out.writeObject("adt");
				out.writeObject(type.getName());
				Type typeParameters = type.getTypeParameters();
				writeType(out, typeParameters);
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				
				if(fieldNames == null){
					out.writeObject("tuple");
				} else {
					out.writeObject("tuple_named_fields");
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
				out.writeObject("value");
				return null;
			}

			@Override
			public Void visitVoid(Type type) throws IOException {
				out.writeObject("void");
				return null;
			}

			@Override
			public Void visitBool(Type type) throws IOException {
				out.writeObject("bool");
				return null;
			}

			@Override
			public Void visitParameter(Type type) throws IOException {
				out.writeObject("parameter");
				out.writeObject(type.getName());
				writeType(out, type.getBound());
				return null;
			}

			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					FunctionType ft = (FunctionType) type;
					out.writeObject("function");
					writeType(out, ft.getReturnType());
					writeType(out, ft.getArgumentTypes());
					writeType(out, ft.getKeywordParameterTypes());
				} else if(type instanceof ReifiedType){
					//System.out.println("writeType: " + type);
					ReifiedType rt = (ReifiedType) type;
					out.writeObject("reified");
					Type elemType = rt.getTypeParameters();  // TODO ok?
					writeType(out, elemType);
				} else if(type instanceof OverloadedFunctionType){
					out.writeObject("overloaded");
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					out.writeObject(alternatives.size());
					for(FunctionType ft : alternatives){
						writeType(out, ft);
					}
				} else if(type instanceof NonTerminalType){
					out.writeObject("nonterminal");
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
				out.writeObject("datetime");
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
		//System.out.println("FSTSerializableType.instantiate: " + t);
		this.type = t;
		return new FSTSerializableType(t);
	}
		
	private Type readType(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object xx = in.readObject();
		if(xx instanceof FSTSerializableType){
			return ((FSTSerializableType) xx).getType();
		}
		String start = (String) xx;
		String [] fieldNames = null;
		String keyLabel = null;
		String valLabel = null;
		
		switch(start){
		case "real":	return tf.realType();
		
		case "int":		return tf.integerType();
		
		case "rat":		return tf.rationalType();
		
		case "list":	Type elemType = readType(in);
						return tf.listType(elemType);
		
		case "map_named_fields":
						keyLabel  = (String) in.readObject();
						valLabel  = (String) in.readObject();
						// fall through to "map" case
						
		case "map":		Type keyType = readType(in);
						Type valType = readType(in);
						if(keyLabel == null){
							return tf.mapType(keyType, valType);
						}
						return tf.mapType(keyType, keyLabel, valType, valLabel);
						
		case "number":	return tf.numberType();
		
		case "alias":	String name = (String) in.readObject();
						Type aliasedType = readType(in);
						Type typeParameters = readType(in);
						//System.out.println("readType,alias, " + name + ", " + aliasedType);
						return tf.aliasType(store, name, aliasedType, typeParameters);
		
		case "set":		elemType = readType(in);
						return tf.setType(elemType);
						
		case "loc":		return tf.sourceLocationType();
		
		case "str":		return tf.stringType();
		
		case "node":	return tf.nodeType();
		
		case "constructor_named_fields":
						fieldNames = (String[]) in.readObject();
						// fall through to "constructor" case
			
		case "constructor": 	
						name = (String) in.readObject();
						int arity = (Integer) in.readObject();
						Type adtType = readType(in);
						
						Type declaredAdt = store.lookupAbstractDataType(name);
						
						if(declaredAdt != null){
							adtType = declaredAdt;
						}
						//System.out.println("readType, constructor " + name + ", " + adtType);System.out.flush();
						
						Type fieldTypes[] = new Type[arity];

						for(int i = 0; i < arity; i++){
							fieldTypes[i] = readType(in);
							//System.out.println("readType, constructor, fieldType[" + i + "] = " + fieldTypes[i]);System.out.flush();
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
						
		case "adt":		name = (String) in.readObject();
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
						
		case "tuple_named_fields":
						fieldNames = (String[]) in.readObject();
						// fall through to "tuple" case
						
		case "tuple":	arity = (Integer) in.readObject();
						Type[] elemTypes = new Type[arity];
						for(int i = 0; i < arity; i++){
							elemTypes[i] = readType(in);
						}
						
						if(fieldNames != null){
							return tf.tupleType(elemTypes, fieldNames);
						}
						return tf.tupleType(elemTypes);
						
		case "value":	return tf.valueType();
		
		case "void":	return tf.voidType();
		
		case "bool":	return tf.boolType();
		
		case "function":
						Type returnType = readType(in);
						Type argumentTypes =  readType(in);
						Type keywordParameterTypes = readType(in);
						return rtf.functionType(returnType, argumentTypes, keywordParameterTypes);
						
		case "reified":
						elemType = readType(in);
						elemType = elemType.getFieldType(0);
						res = rtf.reifiedType(elemType);
						//System.out.println("readType: " + res);
						return res;
						
		case "overloaded":
						int n = (Integer) in.readObject();
						Set<FunctionType> alternatives = new HashSet<FunctionType>(n);
						for(int i = 0; i < n; i++){
							alternatives.add((FunctionType)in.readObject());
						}
						return rtf.overloadedFunctionType(alternatives);
						

		case "nonterminal":
						IConstructor nt = (IConstructor) ((FSTSerializableIValue)in.readObject()).getValue();
						return rtf.nonTerminalType(nt);
						
		case "parameter":
						name = (String) in.readObject();
						Type bound = readType(in);
						return tf.parameterType(name, bound);
						
		case "datetime":	
						return tf.dateTimeType();
		
		}
		throw new RuntimeException("readType: unhandled case " + start);
	}

}
