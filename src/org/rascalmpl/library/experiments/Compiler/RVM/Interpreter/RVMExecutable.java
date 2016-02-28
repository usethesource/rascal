package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 * RVMExecutable contains all data needed for executing an RVM program.
 *
 * RVMExecutable is serialized by FSTRVMExecutableSerializer; make sure that
 * all fields declared here are synced with the serializer.
 */
public class RVMExecutable implements Serializable{

	private static final long serialVersionUID = -8966920880207428792L;
   
	
	// transient fields
	transient static IValueFactory vf;
	transient static TypeStore store;
	//transient static TypeSerializer typeserializer;
	
	// Serializable fields
	
	private String module_name;
	private IMap moduleTags;
	private IMap symbol_definitions;
	
	private ArrayList<Function> functionStore;
	private  Map<String, Integer> functionMap;
	
	// Constructors
	private ArrayList<Type> constructorStore;
	private Map<String, Integer> constructorMap;
	
	// Function overloading
	private ArrayList<OverloadedFunction> overloadedStore;
	private Map<String, Integer> resolver;
	
	private ArrayList<String> initializers;
	private ArrayList<String> testsuites;
	private String uid_module_init;
	private String uid_module_main;
	private String uid_module_main_testsuite;
	
	private byte[] jvmByteCode;
	private String fullyQualifiedDottedName;
	
	public RVMExecutable(
			final String module_name,
			final IMap moduleTags,
			
			final IMap symbol_definitions,
			final Map<String, Integer> functionMap,
			final ArrayList<Function> functionStore,
			
			final Map<String, Integer> constructorMap,
			final ArrayList<Type> constructorStore,
	
			final Map<String, Integer> resolver,
			final ArrayList<OverloadedFunction> overloadedStore,
			
			ArrayList<String> initializers,
			ArrayList<String> testsuites,
			String uid_module_init,
			String uid_module_main,
			String uid_module_main_testsuite,
			TypeStore ts,
			IValueFactory vfactory, 
			boolean jvm
			){
		
		this.module_name = module_name;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		
		this.functionMap = functionMap;
		this.functionStore = functionStore;
		
		this.constructorMap = constructorMap;
		this.constructorStore = constructorStore;

		this.resolver = resolver;
		this.overloadedStore = overloadedStore;
		
		this.initializers = initializers;
		this.testsuites = testsuites;
		
		this.uid_module_init = uid_module_init;
		this.uid_module_main = uid_module_main;
		this.uid_module_main_testsuite = uid_module_main_testsuite;
		
		vf = vfactory;
		store = ts;
		if(jvm){
			buildRunnerByteCode(false, false);
		}
	}
	
	public String getModuleName() {
		return module_name;
	}

	public IMap getModuleTags() {
		return moduleTags;
	}

	IMap getSymbolDefinitions() {
		return symbol_definitions;
	}

	ArrayList<Function> getFunctionStore() {
		return functionStore;
	}

	Map<String, Integer> getFunctionMap() {
		return functionMap;
	}

	ArrayList<Type> getConstructorStore() {
		return constructorStore;
	}

	Map<String, Integer> getConstructorMap() {
		return constructorMap;
	}
	
	ArrayList<OverloadedFunction> getOverloadedStore() {
		return overloadedStore;
	}
	
	Map<String, Integer> getResolver() {
		return resolver;
	}
	
	ArrayList<String> getInitializers() {
		return initializers;
	}

	ArrayList<String> getTestSuites() {
		return testsuites;
	}

	String getUidModuleInit() {
		return uid_module_init;
	}

	String getUidModuleMain() {
		return uid_module_main;
	}

	String getUidModuleMainTestsuite() {
		return uid_module_main_testsuite;
	}

	byte[] getJvmByteCode() {
		return jvmByteCode;
	}

	void setJvmByteCode(byte[] jvmByteCode) {
		this.jvmByteCode = jvmByteCode;
	}

	String getFullyQualifiedDottedName() {
		return fullyQualifiedDottedName;
	}

	void setFullyQualifiedDottedName(String fullyQualifiedDottedName) {
		this.fullyQualifiedDottedName = fullyQualifiedDottedName;
	}

	void buildRunnerByteCode(boolean profile, boolean debug) {
		try {
			// TODO; in the future create multiple classes with the same name as a Rascal module
			
			String className;
			String packageName = "org.rascalmpl.library";
			
			int n = module_name.lastIndexOf("::");
			
			if(n > 2){
				className = module_name.substring(n + 2);
				packageName +=  "." + module_name.substring(0,  n).replaceAll("::", ".");
			} else {
				className = module_name;
			}

			BytecodeGenerator codeEmittor = new BytecodeGenerator(functionStore, overloadedStore, functionMap, constructorMap, resolver);
	
			codeEmittor.buildClass(packageName,className,debug) ;

			jvmByteCode = codeEmittor.finalizeCode();
			fullyQualifiedDottedName = codeEmittor.finalName().replace('/', '.') ;
			System.err.println("buildRunnerByteCode: " + jvmByteCode.length + " bytes");
			// TODO: REMOVE for debug purposes only
			codeEmittor.dumpClass("/tmp/" + className + ".class");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(ISourceLocation rvmExecutable) throws IOException{		
		OutputStream fileOut;
		
		TypeStore typeStore = RascalValueFactory.getStore(); //new TypeStore(RascalValueFactory.getStore());
		
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
		
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);

		ISourceLocation compOut = rvmExecutable;
		fileOut = URIResolverRegistry.getInstance().getOutputStream(compOut, false);
		FSTObjectOutput out = new FSTObjectOutput(fileOut, RVMLoader.conf);
		long before = Timing.getCpuTime();
		out.writeObject(this);
		out.close();
		System.out.println("Writing: " + compOut.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
	}
	
	public static RVMExecutable read(ISourceLocation rvmExecutable) throws IOException {
		RVMExecutable executable = null;
		
		vf = ValueFactoryFactory.getValueFactory();
		TypeStore typeStore = RascalValueFactory.getStore(); //new TypeStore(RascalValueFactory.getStore());
		
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
	
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);
	
		FSTObjectInput in = null;
		try {
			ISourceLocation compIn = rvmExecutable;
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new FSTObjectInput(fileIn, RVMLoader.conf);
			long before = Timing.getCpuTime();
			executable = (RVMExecutable) in.readObject(RVMExecutable.class);
			in.close();
			in = null;
			System.out.println("Reading: " + compIn.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
		} catch (ClassNotFoundException c) {
			throw new IOException("Class not found: " + c.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} 
		finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return executable;
	}
	
	public NameCompleter completePartialIdentifier(NameCompleter completer, String partialIdentifier) {
		if (partialIdentifier == null || partialIdentifier.isEmpty()) {
			throw new IllegalArgumentException("The behavior with empty string is undefined.");
		}
		if (partialIdentifier.startsWith("\\")) {
			partialIdentifier = partialIdentifier.substring(1);
		}

		for(Function fun : functionStore){
			completer.add(fun.name, partialIdentifier);
		}
		
		for(Type type : constructorStore){
			completer.add(type.getName(), partialIdentifier);
		}
		
//		for(IValue modVar : moduleVariables.keySet()){
//			completer.add(modVar.toString(), partialIdentifier);
//		}

		return completer;
	}
	
	public NameCompleter completePartialModuleIdentifier(NameCompleter completer, String partialIdentifier) {
		if (partialIdentifier == null || partialIdentifier.isEmpty()) {
			throw new IllegalArgumentException("The behavior with empty string is undefined.");
		}
		if (partialIdentifier.startsWith("\\")) {
			partialIdentifier = partialIdentifier.substring(1);
		}
		
		

		return completer;
	}
	
	public boolean comparable(RVMExecutable other){
		
		//boolean nameOk = this.getModuleName().equals(other.getModuleName());
		boolean symbol_definitionsOk = true;
		
		IMap defs1 = this.symbol_definitions;
		IMap defs2 = other.symbol_definitions;
		
		if(defs1.size() != defs2.size()){
			System.err.println("symbol_definitions: different size" + defs1.size() + " vs " + defs2.size());
			symbol_definitionsOk = false;
		}
		
		for(IValue key : defs1){
			if(!defs2.containsKey(key)){
				System.err.println("symbol_definitions: key " + key + " not in defs2");
				symbol_definitionsOk = false;
			} else if(!defs1.get(key).equals(defs2.get(key))){
				System.err.println("symbol_definitions: different values for key " + key + ": " + defs1.get(key) + " vs " + defs2.get(key));
				IConstructor choice1 = (IConstructor) defs1.get(key);
				IConstructor choice2 = (IConstructor) defs2.get(key);
				
				IValue adt1 = choice1.get("def");
				IValue adt2 = choice2.get("def");
				
				boolean a1 = adt1.equals(adt2);
				boolean a2 = adt1 == adt2;
				
				ISet alts1 =  (ISet) choice1.get("alternatives");
				ISet alts2 =  (ISet) choice1.get("alternatives");
				
				boolean b = alts1.equals(alts2);
				boolean c = choice1.equals(choice2);
				
				System.err.println("symbol_definitions: a1=" + a1 + ", a2=" + a2 + ", b=" + b + ", c=" + c);
				
				symbol_definitionsOk = false;
			}
		}
		
		boolean functionMapOk = this.functionMap.equals(other.functionMap);
	
		boolean constructorMapOk = this.constructorMap.equals(other.constructorMap);
		boolean resolverOk = this.resolver.equals(other.resolver);
		boolean initializersOk = this.initializers.equals(other.initializers);
		boolean testsuitesOk = this.testsuites.equals(other.testsuites);
		boolean uidsOk = this.uid_module_init.equals(other.uid_module_init) &&
						 this.uid_module_main.equals(other.uid_module_main) &&
						 this.uid_module_main_testsuite.equals(other.uid_module_main_testsuite);
		
		boolean constructorStoreOk = true;
		ArrayList<Type> cs = this.constructorStore;
		ArrayList<Type> cs2 = other.constructorStore;
		
		if(cs.size() != cs2.size()){
			System.err.println("constructorStore: " + cs.size() + " vs " + cs2.size());
			constructorStoreOk = false;
		}
		
		for(int i = 0; i < Math.min(cs.size(),cs2.size()); i++){
			if(!cs.get(i).equals(cs2.get(i))){
				System.err.println(i + ": " + cs.get(i) + " vs " + cs2.get(i));
				constructorStoreOk = false;
			}
		}
		
		boolean overloadedStoreOk = true;
		
		ArrayList<OverloadedFunction> ols = this.overloadedStore;
		ArrayList<OverloadedFunction> ols2 = other.overloadedStore;
		
		if(ols.size() !=  ols2.size()){
			System.err.println("overloadedStores: " + ols.size() + " vs " + ols2.size());
			overloadedStoreOk = false;
		}
		
		for(int i = 0; i < Math.min(ols.size(),ols2.size()); i++){
			if(!ols.get(i).comparable(ols2.get(i))){
				System.err.println(i + ": " + ols.get(i) + " vs " + ols2.get(i));
				overloadedStoreOk = false;
			}
		}
		
//		System.out.println("Checking original and copy of RVMExecutable:");
//		System.out.println("\tname:                " + nameOk);
//		System.out.println("\tsymbol_definitions: " + symbol_definitionsOk 	+ " [" + symbol_definitions.size() + "]");
//		System.out.println("\tfunctionMap:        " + functionMapOk 		+ " [" + functionMap.size() + "]");
//		System.out.println("\tconstructorStore:   " + constructorStoreOk 	+ " [" + constructorStore.size() + "]");
//		System.out.println("\tconstructorMap:     " + constructorMapOk 		+ " [" + constructorStore.size() + "]");
//		System.out.println("\tresolver:           " + resolverOk 			+ " [" + resolver.size() + "]");
//		System.out.println("\tinitializers:       " + initializersOk 		+ " [" + initializers.size() + "]");
//		System.out.println("\ttestsuites:         " + testsuitesOk 			+ " [" + testsuites.size() + "]");
//		System.out.println("\tuids:               " + uidsOk);
//		System.out.println("\toverloadedStore:    " + overloadedStoreOk 	+ " [" + overloadedStore.size() + "]");
		
		return symbol_definitionsOk && functionMapOk && constructorStoreOk && constructorMapOk && 
			   resolverOk && overloadedStoreOk && initializersOk && testsuitesOk && uidsOk;
	}
}
	
class FSTRVMExecutableSerializer extends FSTBasicObjectSerializer {

	private static IValueFactory vf;
	private static TypeStore store;
	//private static TypeReifier tr;
	//private static TypeSerializer typeserializer;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
		//tr = new TypeReifier(vf);
		//typeserializer = new TypeSerializer(ts);
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo arg2, FSTFieldInfo arg3, int arg4)
					throws IOException {

		int n;
		RVMExecutable ex = (RVMExecutable) toWrite;

		// public String module_name;

		out.writeObject(ex.getModuleName());

		// public IMap moduleTags;
	
		out.writeObject(new FSTSerializableIValue(ex.getModuleTags()));

		// public IMap symbol_definitions;

		out.writeObject(new FSTSerializableIValue(ex.getSymbolDefinitions()));

		// public ArrayList<Function> functionStore;
		// public Map<String, Integer> functionMap;

		out.writeObject(ex.getFunctionStore());

		// public ArrayList<Type> constructorStore;

		n = ex.getConstructorStore().size();
		out.writeObject(n);

		for(int i = 0; i < n; i++){
			out.writeObject(new FSTSerializableType(ex.getConstructorStore().get(i)));
		}

		// public Map<String, Integer> constructorMap;

		out.writeObject(ex.getConstructorMap());

		// public ArrayList<OverloadedFunction> overloadedStore;

		out.writeObject(ex.getOverloadedStore());

		// public Map<String, Integer> resolver;

		out.writeObject(ex.getResolver());

		// ArrayList<String> initializers;

		out.writeObject(ex.getInitializers());

		// ArrayList<String> testsuites;

		out.writeObject(ex.getTestSuites());

		// public String uid_module_init;

		out.writeObject(ex.getUidModuleInit());

		// public String uid_module_main;

		out.writeObject(ex.getUidModuleMain());

		// public String uid_module_main_testsuite;

		out.writeObject(ex.getUidModuleMainTestsuite());
		
		// public byte[] jvmByteCode;
		
		if(ex.getJvmByteCode() == null) 
			System.err.println("byte code is null"); 
		else 
			System.err.println("Writing byte code: " + ex.getJvmByteCode().length + " bytes");
		
		out.writeObject(ex.getJvmByteCode());
		
		// public String fullyQualifiedDottedName;
		
		out.writeObject(ex.getFullyQualifiedDottedName());
	}


	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
	}

	@SuppressWarnings("unchecked")
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws ClassNotFoundException, IOException 
	{
		int n;

		// public String name;

		String module_name = (String) in.readObject();

		// public IMap moduleTags;

		IMap moduleTags = (IMap) in.readObject();

		// public IMap symbol_definitions;

		IMap symbol_definitions = (IMap) in.readObject();

		// public ArrayList<Function> functionStore;
		// public  Map<String, Integer> functionMap;

		ArrayList<Function> functionStore = (ArrayList<Function>) in.readObject();

		n = functionStore.size();
		HashMap<String, Integer> functionMap = new HashMap<String, Integer>(n);
		for(int i = 0; i < n; i++){
			functionMap.put(functionStore.get(i).getName(), i);
		}

		// public ArrayList<Type> constructorStore;

		n = (Integer) in.readObject();
		ArrayList<Type> constructorStore = new ArrayList<Type>(n);

		for(int i = 0; i < n; i++){
			constructorStore.add(i, (Type) in.readObject());
		}

		// public Map<String, Integer> constructorMap;

		Map<String, Integer> constructorMap = (Map<String, Integer>) in.readObject();

		// public ArrayList<OverloadedFunction> overloadedStore;

		ArrayList<OverloadedFunction> overloadedStore = (ArrayList<OverloadedFunction>) in.readObject();

		// public Map<String, Integer> resolver;

		HashMap<String, Integer> resolver = (HashMap<String, Integer>) in.readObject();

		// ArrayList<String> initializers;

		ArrayList<String> initializers = (ArrayList<String>) in.readObject();

		// ArrayList<String> testsuites;

		ArrayList<String> testsuites = (ArrayList<String>) in.readObject();

		// public String uid_module_init;

		String uid_module_init = (String) in.readObject();

		// public String uid_module_main;

		String uid_module_main = (String) in.readObject();

		// public String uid_module_main_testsuite;

		String uid_module_main_testsuite = (String) in.readObject();
		
		// public byte[] jvmByteCode;
		
		byte[] jvmByteCode = (byte[]) in.readObject();
		if(jvmByteCode == null)	
			System.err.println("byte code is null");
		else 
			System.err.println("Reading byte code: " + jvmByteCode.length + " bytes");
				
		// public String fullyQualifiedDottedName;
	
		String fullyQualifiedDottedName = (String) in.readObject();

		RVMExecutable ex = new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
								constructorMap, constructorStore, resolver, overloadedStore, initializers, testsuites, 
								uid_module_init, uid_module_main, uid_module_main_testsuite, store, vf, false);
		ex.setJvmByteCode(jvmByteCode);
		ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);
		
		return ex;
	
	}
}
