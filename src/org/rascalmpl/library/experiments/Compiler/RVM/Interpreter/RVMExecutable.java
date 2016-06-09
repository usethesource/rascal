package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.VersionInfo;
import org.rascalmpl.library.util.SemVer;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

/**
 * RVMExecutable contains all data needed for executing an RVM program.
 *
 * RVMExecutable is serialized by FSTRVMExecutableSerializer; make sure that
 * all **non-static** fields declared here are synced with the serializer.
 */
public class RVMExecutable implements Serializable{
	
	static private final FSTSerializableType serializableType;
	static private final FSTSerializableIValue serializableIValue;
	static private final FSTRVMExecutableSerializer rvmExecutableSerializer;
	static private final FSTFunctionSerializer functionSerializer;
	static private final FSTOverloadedFunctionSerializer overloadedFunctionSerializer;
	static private final FSTCodeBlockSerializer codeblockSerializer;
	
	static {
		// set up FST serialization in gredients that will be reused across read/write calls

		// PDB Types
		serializableType = new FSTSerializableType();

		// PDB values
		serializableIValue =  new FSTSerializableIValue();
		
		// Specific serializers
		rvmExecutableSerializer = new FSTRVMExecutableSerializer();

		functionSerializer = new FSTFunctionSerializer();

		overloadedFunctionSerializer = new FSTOverloadedFunctionSerializer();

		codeblockSerializer = new FSTCodeBlockSerializer();
	} 
	
	/**
	 * Create an FSTConfiguration depending on the used extension: ".json" triggers the JSON reader/writer.
	 * Note: the JSON version is somewhat larger and slower but is usefull for recovery during bootstrapping incidents.
	 * @param source or desination of executable
	 * @return an initialized FSTConfiguration
	 */
	private static FSTConfiguration makeFSTConfig(ISourceLocation path){
		FSTConfiguration config = path.getURI().getPath().contains(".json") ?
				FSTConfiguration.createJsonConfiguration() : FSTConfiguration.createDefaultConfiguration(); 
		config.registerSerializer(FSTSerializableType.class, serializableType, false);
		config.registerSerializer(FSTSerializableIValue.class, serializableIValue, false);
		config.registerSerializer(RVMExecutable.class, rvmExecutableSerializer, false);
		config.registerSerializer(Function.class, functionSerializer, false);
		config.registerSerializer(OverloadedFunction.class, overloadedFunctionSerializer, false);
		config.registerSerializer(CodeBlock.class, codeblockSerializer, false);
		config.registerClass(OverloadedFunction.class);
		return config;
	}

	private static final long serialVersionUID = -8966920880207428792L;
	static final String RASCAL_MAGIC = "Rascal Vincit Omnia";
	
	// transient fields
	static IValueFactory vf;
	static TypeStore store;
	
	// Serializable fields
	
	private ISet errors;
	private String module_name;
	private IMap moduleTags;
	private IMap symbol_definitions;
	
	private Function[] functionStore;
	private  Map<String, Integer> functionMap;
	
	// Constructors
	private ArrayList<Type> constructorStore;
	private Map<String, Integer> constructorMap;
	
	// Function overloading
	private OverloadedFunction[] overloadedStore;
	private Map<String, Integer> resolver;
	
	private ArrayList<String> initializers;
	private ArrayList<String> testsuites;
	private String uid_module_init;
	private String uid_module_main;
	private String uid_module_main_testsuite;
	
	private byte[] jvmByteCode;
	private String fullyQualifiedDottedName;
	
	public RVMExecutable(ISet errors){
		this.errors = errors;
	}
	
	public RVMExecutable(
			final String module_name,
			final IMap moduleTags,
			
			final IMap symbol_definitions,
			final Map<String, Integer> functionMap,
			final Function[] functionStore,
			
			final Map<String, Integer> constructorMap,
			final ArrayList<Type> constructorStore,
	
			final Map<String, Integer> resolver,
			final OverloadedFunction[] overloadedStore,
			
			ArrayList<String> initializers,
			ArrayList<String> testsuites,
			String uid_module_init,
			String uid_module_main,
			String uid_module_main_testsuite,
			TypeStore ts,
			IValueFactory vfactory, 
			boolean jvm
			) throws IOException{
		
		vf = vfactory;
		store = ts;
		
		this.errors = vf.set();
		
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
		
		if(jvm){
			generateClassFile(false);
			clearForJVM();
		}
	}
	
	void clearForJVM(){
		for(Function f : functionStore){
			f.clearForJVM();
		}
	}
	
	public Boolean isValid(){
		return errors.size() == 0;
	}
	
	public ISet getErrors(){
		return errors;
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

	public Function[] getFunctionStore() {
		return functionStore;
	}

	public Map<String, Integer> getFunctionMap() {
		return functionMap;
	}

	ArrayList<Type> getConstructorStore() {
		return constructorStore;
	}

	Map<String, Integer> getConstructorMap() {
		return constructorMap;
	}
	
	OverloadedFunction[] getOverloadedStore() {
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
	
	private String getGeneratedPackageName(){
		String packageName = ""; //"org.rascalmpl.library";
		
		int n = module_name.lastIndexOf("::");
		
		if(n > 2){
			if(!packageName.isEmpty()){
				packageName +=  ".";
			}
			packageName +=  module_name.substring(0,  n).replaceAll("::", ".");
		}
		return packageName;
	}
	
	private String getGeneratedClassName(){
		String className;
		int n = module_name.lastIndexOf("::");
		
		if(n > 2){
			className = module_name.substring(n + 2);
		} else {
			className = module_name;
		}
		
		className += "$Compiled";
		return className;
	}
	
	String getGeneratedClassQualifiedName(){
		String packageName = getGeneratedPackageName();
		String className = getGeneratedClassName();
		return packageName + (packageName.isEmpty() ? "" : ".") + className;
	}

	void generateClassFile(boolean debug) {
		try {			
			BytecodeGenerator codeEmittor = new BytecodeGenerator(functionStore, overloadedStore, functionMap, constructorMap, resolver);
	
			codeEmittor.buildClass(getGeneratedPackageName(), getGeneratedClassName(), debug) ;

			jvmByteCode = codeEmittor.finalizeCode();
			fullyQualifiedDottedName = codeEmittor.finalName().replace('/', '.') ;
			
			if(debug){
				codeEmittor.dumpClass();
			}
			
		} catch (Exception e) {
		    if (e.getMessage() != null && e.getMessage().startsWith("Method code too large")) {
		        // ASM does not provide an indication of _which_ method is too large, so let's find out:
		        Comparator<Function> c = ((x, y) -> x.codeblock.finalCode.length - y.codeblock.finalCode.length); 
		        throw new RuntimeException("Function too large: " + Arrays.stream(functionStore).max(c).get());
		    }
		    else {
		        throw e;
		    }
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
		FSTObjectOutput out = new FSTObjectOutput(fileOut, makeFSTConfig(rvmExecutable));
		long before = Timing.getCpuTime();
		out.writeObject(this);
		out.close();
		System.out.println("RVMExecutable.write: " + compOut.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
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
			in = new FSTObjectInput(fileIn, makeFSTConfig(rvmExecutable));
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
		//executable.rvmProgramLoc = rvmExecutable;
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
}
	
class FSTRVMExecutableSerializer extends FSTBasicObjectSerializer {

	private static IValueFactory vf;
	private static TypeStore store;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		vf = vfactory;
		store = ts;
		store.extendStore(RascalValueFactory.getStore());
	}

	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite,
			FSTClazzInfo arg2, FSTFieldInfo arg3, int arg4)
					throws IOException {

		int n;
		RVMExecutable ex = (RVMExecutable) toWrite;
		
		// Write standard header
		
		out.writeObject(RVMExecutable.RASCAL_MAGIC);
		out.writeObject(VersionInfo.RASCAL_VERSION);
		out.writeObject(VersionInfo.RASCAL_RUNTIME_VERSION);
		out.writeObject(VersionInfo.RASCAL_COMPILER_VERSION);
		
		// String[] errors
		
		out.writeObject(new FSTSerializableIValue(ex.getErrors()));
		
		if(!ex.isValid()){
			return;
		}
		
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
		
		// Read and check standard header
		
		// Check magic
		
		String rascal_magic =  (String) in.readObject();
		
		if(!rascal_magic.equals(RVMExecutable.RASCAL_MAGIC)){
			throw new RuntimeException("Cannot read incompatible Rascal executable");
		}
		
		// Check RASCAL_VERSION
		
		String rascal_version = (String) in.readObject();
		
		SemVer sv;
		try {
			sv = new SemVer(rascal_version);
		} catch(Exception e){
			throw new RuntimeException("Invalid value for RASCAL_VERSION in Rascal executable");
		}
		
		if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_VERSION)){
			throw new RuntimeException("RASCAL_VERSION " + rascal_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_VERSION);
		}
		
		// Check RASCAL_RUNTIME_VERSION
		
		String rascal_runtime_version = (String) in.readObject();
		try {
			sv = new SemVer(rascal_runtime_version);
		} catch(Exception e){
			throw new RuntimeException("Invalid value for RASCAL_RUNTIME_VERSION in Rascal executable");
		}
		if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_RUNTIME_VERSION)){
			throw new RuntimeException("RASCAL_RUNTIME_VERSION " + rascal_runtime_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_RUNTIME_VERSION);
		}
		
		// Check RASCAL_COMPILER_VERSION
		
		String rascal_compiler_version = (String) in.readObject();
		try {
			sv = new SemVer(rascal_runtime_version);
		} catch(Exception e){
			throw new RuntimeException("Invalid value for RASCAL_COMPILER_VERSION in Rascal executable");
		}
		if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_COMPILER_VERSION)){
			throw new RuntimeException("RASCAL_COMPILER_VERSION " + rascal_compiler_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_COMPILER_VERSION);
		}
//		System.err.println("RascalShell: Rascal: " + VersionInfo.RASCAL_VERSION + "; Runtime: " + VersionInfo.RASCAL_RUNTIME_VERSION + "; Compiler: " + VersionInfo.RASCAL_COMPILER_VERSION);
//		System.err.println("Executable : Rascal: " + rascal_version + "; Runtime: " + rascal_runtime_version + "; Compiler: " + rascal_compiler_version);
				
		// String[] errors
				
		ISet errors = (ISet) in.readObject();
				
		if(errors.size() > 0){
			return new RVMExecutable(errors);
		}
				
		// public String name;

		String module_name = (String) in.readObject();

		// public IMap moduleTags;

		IMap moduleTags = (IMap) in.readObject();

		// public IMap symbol_definitions;

		IMap symbol_definitions = (IMap) in.readObject();

		// public ArrayList<Function> functionStore;
		// public  Map<String, Integer> functionMap;

		Function[] functionStore = (Function[]) in.readObject();

		n = functionStore.length;
		HashMap<String, Integer> functionMap = new HashMap<String, Integer>(n);
		for(int i = 0; i < n; i++){
			functionMap.put(functionStore[i].getName(), i);
		}

		// public ArrayList<Type> constructorStore;

		n = (Integer) in.readObject();
		ArrayList<Type> constructorStore = new ArrayList<Type>(n);

		for(int i = 0; i < n; i++){
			constructorStore.add(i, (Type) in.readObject());
		}

		// public Map<String, Integer> constructorMap;

		Map<String, Integer> constructorMap = (Map<String, Integer>) in.readObject();

		// public OverloadedFunction[] overloadedStore;

		OverloadedFunction[] overloadedStore = (OverloadedFunction[]) in.readObject();

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
