package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;


public class Function implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -1741144671553091111L;
	
	String name;
	Type ftype;
	int scopeId;
	private String funIn;
	int scopeIn = -1;
	int nformals;
	int nlocals;
	boolean isDefault;
	int maxstack;
	CodeBlock codeblock;
	IValue[] constantStore;			
	Type[] typeConstantStore;
	boolean concreteArg = false;
	int abstractFingerprint = 0;
	int concreteFingerprint = 0;

	int[] froms;
	int[] tos;
	int[] types;
	int[] handlers;
	int[] fromSPs;
	int lastHandler = -1;

	boolean isCoroutine = false;
	int[] refs;

	boolean isVarArgs = false;

	ISourceLocation src;			
	IMap localNames;
	
	// transient fields 
	transient private static TypeStore store;
	transient private static TypeSerializer typeserializer;
	transient static IValueFactory vf;
	
	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		store = ts;
		typeserializer = new TypeSerializer(store);
		vf = vfactory;
	}
	
	private void writeObject(java.io.ObjectOutputStream stream)
			throws IOException {
		// String name;
		stream.writeObject(name);
		
		// Type ftype;
		typeserializer.writeType(stream, ftype);
		
		// int scopeId;
		stream.writeObject(scopeId);
		
		// private String funIn;
		stream.writeObject(funIn);
		
		// int scopeIn = -1;
		stream.writeObject(scopeIn);
		
		// int nformals;
		stream.writeObject(nformals);
		
		// int nlocals;
		stream.writeObject(nlocals);
		
		// boolean isDefault;
		stream.writeObject(isDefault);
		
		// int maxstack;
		stream.writeObject(maxstack);
		
		// CodeBlock codeblock;
		stream.writeObject(codeblock);
		
		// IValue[] constantStore;
		int n = constantStore.length;
		stream.writeObject(n);
		
		for(int i = 0; i < n; i++){
			stream.writeObject(new SerializableRascalValue<IValue>(constantStore[i]));
		}
		
		// Type[] typeConstantStore;
		n = typeConstantStore.length;
		stream.writeObject(n);
		
		for(int i = 0; i < n; i++){
			typeserializer.writeType(stream, typeConstantStore[i]);
		}
		
		// boolean concreteArg = false;
		stream.writeObject(concreteArg);
		
		// int abstractFingerprint = 0;
		stream.writeObject(abstractFingerprint);
		
		// int concreteFingerprint = 0;
		stream.writeObject(concreteFingerprint);
		
		// int[] froms;
		stream.writeObject(froms);
		
		// int[] tos;
		stream.writeObject(tos);
		
		// int[] types;
		stream.writeObject(types);
		
		// int[] handlers;
		stream.writeObject(handlers);
		
		// int[] fromSPs;
		stream.writeObject(fromSPs);
		
		// int lastHandler = -1;
		stream.writeObject(lastHandler);

		// oolean isCoroutine = false;
		stream.writeObject(isCoroutine);
		
		// int[] refs;
		stream.writeObject(refs);

		// boolean isVarArgs = false;
		stream.writeObject(isVarArgs);

		// ISourceLocation src;
		stream.writeObject(new SerializableRascalValue<ISourceLocation>(src));
		
		// IMap localNames;
		stream.writeObject(new SerializableRascalValue<IMap>(localNames));
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		
		// String name;
		name = (String) stream.readObject();
		
		// Type ftype;
		ftype = typeserializer.readType(stream);
		
		// int scopeId;
		scopeId = (Integer) stream.readObject();
		
		// private String funIn;
		funIn = (String) stream.readObject();
		
		// int scopeIn = -1;
		scopeIn = (Integer) stream.readObject();
		
		// int nformals;
		nformals = (Integer) stream.readObject();
		
		// int nlocals;
		nlocals = (Integer) stream.readObject();
		
		// boolean isDefault;
		isDefault = (Boolean) stream.readObject();
		
		// int maxstack;
		maxstack = (Integer) stream.readObject();
		
		// CodeBlock codeblock;
		codeblock = (CodeBlock) stream.readObject();
		
		// IValue[] constantStore;
		int n = (Integer) stream.readObject();
		constantStore = new IValue[n];
		
		for(int i = 0; i < n; i++){
			constantStore[i] = ((SerializableRascalValue<IValue>) stream.readObject()).getValue();
		}
		
		// Type[] typeConstantStore;
		n = (Integer) stream.readObject();
		typeConstantStore = new Type[n];
		
		for(int i = 0; i < n; i++){
			typeConstantStore[i] = typeserializer.readType(stream);
		}
		
		// boolean concreteArg = false;
		concreteArg = (Boolean) stream.readObject();
		
		// int abstractFingerprint = 0;
		abstractFingerprint = (Integer) stream.readObject();
		
		// int concreteFingerprint = 0;
		concreteFingerprint = (Integer) stream.readObject();

		// int[] froms;
		froms = (int[]) stream.readObject();
		
		// int[] tos;
		tos = (int[]) stream.readObject();
		
		// int[] types;
		types = (int[]) stream.readObject();
		
		// int[] handlers;
		handlers = (int[]) stream.readObject();
		
		// int[] fromSPs;
		fromSPs = (int[]) stream.readObject();
		
		// int lastHandler = -1;
		lastHandler = (Integer) stream.readObject();

		// boolean isCoroutine = false;
		isCoroutine = (Boolean) stream.readObject();
		
		// int[] refs;
		refs = (int[]) stream.readObject();

		// boolean isVarArgs = false;
		isVarArgs = (Boolean)stream.readObject();

		// ISourceLocation src;
		src = ((SerializableRascalValue<ISourceLocation>) stream.readObject()).getValue();
		
		// IMap localNames;
		localNames = ((SerializableRascalValue<IMap>) stream.readObject()).getValue();
	}
	
	public Function(final String name, final Type ftype, final String funIn, final int nformals, final int nlocals, boolean isDefault, final IMap localNames, 
			 final int maxstack, boolean concreteArg, int abstractFingerprint,
			int concreteFingerprint, final CodeBlock codeblock, final ISourceLocation src){
		this.name = name;
		this.ftype = ftype;
		this.funIn = funIn;
		this.nformals = nformals;
		this.nlocals = nlocals;
		this.isDefault = isDefault;
		this.localNames = localNames;
		this.maxstack = maxstack;
		this.concreteArg = concreteArg;
		this.abstractFingerprint = abstractFingerprint;
		this.concreteFingerprint = concreteFingerprint;
		this.codeblock = codeblock;
		this.src = src;
	}
	
	public void  finalize(final Map<String, Integer> codeMap, final Map<String, Integer> constructorMap, final Map<String, Integer> resolver, final boolean listing){
		if(constructorMap == null){
			System.out.println("finalize: null");
		}
		codeblock.done(name, codeMap, constructorMap, resolver, listing);
		this.scopeId = codeblock.getFunctionIndex(name);
		if(funIn.length() != 0) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();
	}
	
	public void attachExceptionTable(final IList exceptions, final RascalLinker rascalLinker) {
		froms = new int[exceptions.length()];
		tos = new int[exceptions.length()];
		types = new int[exceptions.length()];
		handlers = new int[exceptions.length()];
		fromSPs = new int[exceptions.length()];
		
		int i = 0;
		for(IValue entry : exceptions) {
			ITuple tuple = (ITuple) entry;
			String from = ((IString) tuple.get(0)).getValue();
			String to = ((IString) tuple.get(1)).getValue();
			Type type = rascalLinker.symbolToType((IConstructor) tuple.get(2));
			String handler = ((IString) tuple.get(3)).getValue();
			int fromSP =  ((IInteger) tuple.get(4)).intValue();
			
			froms[i] = codeblock.getLabelPC(from);
			tos[i] = codeblock.getLabelPC(to);
			types[i] = codeblock.getTypeConstantIndex(type);
			handlers[i] = codeblock.getLabelPC(handler);	
			fromSPs[i] = fromSP;
			i++;
		}
	}
	
	public int getHandler(final int pc, final Type type) {
		int i = 0;
		lastHandler = -1;
		for(int from : froms) {
			if(pc >= from) {
				if(pc < tos[i]) {
					// In the range...
					if(type.isSubtypeOf(codeblock.getConstantType(types[i]))) {
						lastHandler = i;
						return handlers[i];
					}
				}
			}
			i++;
		}
		return -1;
	}
	
	public int getFromSP(){
		return nlocals + fromSPs[lastHandler];
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrintableName(){
		int from = name.lastIndexOf("/")+1;
		int to = name.indexOf("(", from);
		if(to < 0){
			to = name.length();
		}
		return name.substring(from, to);
	}
	
	public String getQualifiedName(){
		return name.substring(0, name.indexOf("("));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FUNCTION ").append(name).append(" ").append(ftype);
		for(int i = 0; i < constantStore.length; i++){
			sb.append("constant "). append(i).append(": "). append(constantStore[i]);
		}
		for(int i = 0; i < typeConstantStore.length; i++){
			sb.append("type constant "). append(i).append(": "). append(typeConstantStore[i]);
		}
		sb.append(codeblock);
		return sb.toString();
	}
	
}
