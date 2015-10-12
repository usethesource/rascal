package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.SoftReference;
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
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.values.uptr.RascalValueFactory;

import de.ruedigermoeller.serialization.FSTBasicObjectSerializer;
import de.ruedigermoeller.serialization.FSTClazzInfo;
import de.ruedigermoeller.serialization.FSTClazzInfo.FSTFieldInfo;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

/**
 * Function contains all data needed for a single RVM function
 *
 * Function is serialized by FSTFunctionSerializer, make sure that
 * all fields declared here are synced with the serializer.
 */

public class Function implements Serializable {
	private static final long serialVersionUID = -1741144671553091111L;
	
	String name;
	Type ftype;
	int scopeId;
	String funIn;
	int scopeIn = -1;
	int nformals;
	int nlocals;
	boolean isDefault;
	int maxstack;
	public CodeBlock codeblock;
	public IValue[] constantStore;			
	public Type[] typeConstantStore;
	boolean concreteArg = false;
	int abstractFingerprint = 0;
	int concreteFingerprint = 0;

	int[] froms;
	int[] tos;
	public int[] types;
	int[] handlers;
	int[] fromSPs;
	int lastHandler = -1;

	public Integer funId; // USED in dynRun to find the function, in the JVM version only.

	public String[] fromLabels;
	public String[] toLabels;
    public String[] handlerLabels;
    public int[] fromSPsCorrected;
	
	public int continuationPoints = 0;
	
	boolean isCoroutine = false;
	int[] refs;

	boolean isVarArgs = false;

	ISourceLocation src;			
	IMap localNames;
	
	// transient fields 
	transient static IValueFactory vf;
	transient SoftReference<MemoizationCache<IValue>> memoization;
	
	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
	}
	
	public Function(final String name, final Type ftype, final String funIn, final int nformals, final int nlocals, boolean isDefault, final IMap localNames, 
			 final int maxstack, boolean concreteArg, int abstractFingerprint,
			int concreteFingerprint, final CodeBlock codeblock, final ISourceLocation src, int ctpt){
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
		this.continuationPoints = ctpt ;
	}
	
	Function(final String name, final Type ftype, final String funIn, final int nformals, final int nlocals, boolean isDefault, final IMap localNames, 
			 final int maxstack, boolean concreteArg, int abstractFingerprint,
			int concreteFingerprint, final CodeBlock codeblock, final ISourceLocation src, int scopeIn, IValue[] constantStore, Type[] typeConstantStore,
			int[] froms, int[] tos, int[] types, int[] handlers, int[] fromSPs, int lastHandler, int scopeId,
			boolean isCoroutine, int[] refs, boolean isVarArgs, int ctpt){
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
		this.scopeIn = scopeIn;
		this.constantStore = constantStore;
		this.typeConstantStore = typeConstantStore;
		this.froms = froms;
		this.tos = tos;
		this.types = types;
		this.handlers = handlers;
		this.fromSPs = fromSPs;
		this.lastHandler = lastHandler;
		this.scopeId = scopeId;
		this.isCoroutine = isCoroutine;
		this.refs = refs;
		this.isVarArgs = isVarArgs;
		this.continuationPoints = ctpt ;
	}
	
	public void  finalize(final Map<String, Integer> codeMap, final Map<String, Integer> constructorMap, final Map<String, Integer> resolver){
		if(constructorMap == null){
			System.out.println("finalize: null");
		}
		codeblock.done(name, codeMap, constructorMap, resolver);
		this.scopeId = codeblock.getFunctionIndex(name);
		if(funIn.length() != 0) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();
	}
	
	public void attachExceptionTable(final IList exceptions, final RVMLoader rascalLinker) {
			froms = new int[exceptions.length()];
			tos = new int[exceptions.length()];
			types = new int[exceptions.length()];
			handlers = new int[exceptions.length()];
			fromSPs = new int[exceptions.length()];
			fromSPsCorrected = new int[exceptions.length()];

			fromLabels = new String[exceptions.length()];
			toLabels = new String[exceptions.length()];
			handlerLabels = new String[exceptions.length()];
					
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
				fromSPsCorrected[i] = fromSP + nlocals;
				fromLabels[i] = from;
				toLabels[i] = to;
				handlerLabels[i] = handler;			

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
		sb.append("FUNCTION ").append(name).append(" ->> ").append(ftype).append("\n");
		for(int i = 0; i < constantStore.length; i++){
			sb.append("\t constant "). append(i).append(": "). append(constantStore[i]).append("\n");
		}
		for(int i = 0; i < typeConstantStore.length; i++){
			sb.append("\t type constant "). append(i).append(": "). append(typeConstantStore[i]).append("\n");
		}
//		codeblock.toString() ;
		sb.append(codeblock.toString());
		return sb.toString();
	}
	
}

/**
 * FSTFunctionSerializer: serializer for Function objects
 *
 */
class FSTFunctionSerializer extends FSTBasicObjectSerializer {
	
	//private static IValueFactory vf;
	private static TypeStore store;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		//vf = vfactory;
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo clzInfo, FSTFieldInfo arg3, int arg4)
					throws IOException {
		
		Function fun = (Function) toWrite;

		// String name;
		out.writeObject(fun.name);

		// Type ftype;
		out.writeObject(new FSTSerializableType(fun.ftype));

		// int scopeId;
		out.writeObject(fun.scopeId);

		// private String funIn;
		out.writeObject(fun.funIn);

		// int scopeIn = -1;
		out.writeObject(fun.scopeIn);

		// int nformals;
		out.writeObject(fun.nformals);

		// int nlocals;
		out.writeObject(fun.nlocals);

		// boolean isDefault;
		out.writeObject(fun.isDefault);

		// int maxstack;
		out.writeObject(fun.maxstack);

		// CodeBlock codeblock;
		out.writeObject(fun.codeblock);

		// IValue[] constantStore;
		int n = fun.constantStore.length;
		out.writeObject(n);

		for(int i = 0; i < n; i++){
			out.writeObject(new FSTSerializableIValue(fun.constantStore[i]));
		}

		// Type[] typeConstantStore;
		n = fun.typeConstantStore.length;
		out.writeObject(n);

		for(int i = 0; i < n; i++){
			out.writeObject(new FSTSerializableType(fun.typeConstantStore[i]));
		}

		// boolean concreteArg = false;
		out.writeObject(fun.concreteArg);

		// int abstractFingerprint = 0;
		out.writeObject(fun.abstractFingerprint);

		// int concreteFingerprint = 0;
		out.writeObject(fun.concreteFingerprint);

		// int[] froms;
		out.writeObject(fun.froms);

		// int[] tos;
		out.writeObject(fun.tos);

		// int[] types;
		out.writeObject(fun.types);

		// int[] handlers;
		out.writeObject(fun.handlers);

		// int[] fromSPs;
		out.writeObject(fun.fromSPs);

		// int lastHandler = -1;
		out.writeObject(fun.lastHandler);

		// boolean isCoroutine = false;
		out.writeObject(fun.isCoroutine);

		// int[] refs;
		out.writeObject(fun.refs);

		// boolean isVarArgs = false;
		out.writeObject(fun.isVarArgs);

		// ISourceLocation src;
		out.writeObject(new FSTSerializableIValue(fun.src));

		// IMap localNames;
		out.writeObject(new FSTSerializableIValue(fun.localNames));

		// int continuationPoints
		out.writeObject(fun.continuationPoints);
	}
	

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
	}
	
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws ClassNotFoundException, IOException 
	{

		// String name;
		String name = (String) in.readObject();

		// Type ftype;
		Type ftype = (Type) in.readObject();

		// int scopeId;
		Integer scopeId = (Integer) in.readObject();

		// private String funIn;
		String funIn = (String) in.readObject();

		// int scopeIn = -1;
		Integer scopeIn = (Integer) in.readObject();

		// int nformals;
		Integer nformals = (Integer) in.readObject();

		// int nlocals;
		Integer nlocals = (Integer) in.readObject();

		// boolean isDefault;
		Boolean isDefault = (Boolean) in.readObject();

		// int maxstack;
		Integer maxstack = (Integer) in.readObject();

		// CodeBlock codeblock;
		CodeBlock codeblock = (CodeBlock) in.readObject();

		// IValue[] constantStore;
		int n = (Integer) in.readObject();
		IValue[] constantStore = new IValue[n];

		for(int i = 0; i < n; i++){
			constantStore[i] = (IValue) in.readObject();
		}

		// Type[] typeConstantStore;
		n = (Integer) in.readObject();
		Type[] typeConstantStore = new Type[n];

		for(int i = 0; i < n; i++){
			typeConstantStore[i] = (Type) in.readObject();
		}

		// boolean concreteArg = false;
		Boolean concreteArg = (Boolean) in.readObject();

		// int abstractFingerprint = 0;
		Integer abstractFingerprint = (Integer) in.readObject();

		// int concreteFingerprint = 0;
		Integer concreteFingerprint = (Integer) in.readObject();

		// int[] froms;
		int[] froms = (int[]) in.readObject();

		// int[] tos;
		int[] tos = (int[]) in.readObject();

		// int[] types;
		int[] types = (int[]) in.readObject();

		// int[] handlers;
		int[] handlers = (int[]) in.readObject();

		// int[] fromSPs;
		int[] fromSPs = (int[]) in.readObject();

		// int lastHandler = -1;
		Integer lastHandler = (Integer) in.readObject();

		// boolean isCoroutine = false;
		Boolean isCoroutine = (Boolean) in.readObject();

		// int[] refs;
		int[] refs = (int[]) in.readObject();

		// boolean isVarArgs = false;
		Boolean isVarArgs = (Boolean)in.readObject();

		// ISourceLocation src;
		ISourceLocation src = (ISourceLocation) in.readObject();

		// IMap localNames;
		IMap localNames = (IMap) in.readObject();
		
		// int continuationPoints
		Integer continuationPoints = (Integer) in.readObject();
		
		return new Function(name, ftype, funIn, nformals, nlocals, isDefault, localNames, maxstack, concreteArg, abstractFingerprint, concreteFingerprint, 
				codeblock, src, scopeIn, constantStore, typeConstantStore,
				froms, tos, types, handlers, fromSPs, lastHandler, scopeId,
				isCoroutine, refs, isVarArgs, continuationPoints);
	
	}
}
