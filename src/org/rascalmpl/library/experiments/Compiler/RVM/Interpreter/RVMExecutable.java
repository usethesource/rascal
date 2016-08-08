package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.VersionInfo;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RSFExecutableWriter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMExecutableReader;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMExecutableWriter;
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
 * RVMExecutable is serialized by write/read methods defined in this class; make sure that
 * all **non-static** fields declared here are synced with the serializer.
 */
public class RVMExecutable  {
	
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
	
	public void writeRVM(ISourceLocation rvmExecutable) throws IOException{		
		OutputStream fileOut;
		
		TypeStore typeStore = //RascalValueFactory.getStore(); 
							 new TypeStore(RascalValueFactory.getStore());

		ISourceLocation compOut = rvmExecutable;
		fileOut = URIResolverRegistry.getInstance().getOutputStream(compOut, false);
		RVMExecutableWriter out = new RVMExecutableWriter(fileOut);
		long before = Timing.getCpuTime();
		this.write(out);
		out.close();
		System.out.println("RVMExecutable.writeRVM: " + compOut.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
	}
	
	public static RVMExecutable readRVM(ISourceLocation rvmExecutable) throws IOException {
		RVMExecutable executable = null;
		
		vf = ValueFactoryFactory.getValueFactory();
		TypeStore typeStore = // RascalValueFactory.getStore();
							  new TypeStore(RascalValueFactory.getStore());
	
		RVMExecutableReader in = null;
		try {
			ISourceLocation compIn = rvmExecutable;
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new RVMExecutableReader(fileIn);
			long before = Timing.getCpuTime();
			executable = read(in);
			in.close();
			in = null;
			System.out.println("RVMExecutable.readRVM: " + compIn.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} 
		finally {
			if(in != null){
				in.close();
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
	
	public void write(RVMExecutableWriter out)	throws IOException {

		int n;
		
		// Write standard header
		
		out.writeJString(RVMExecutable.RASCAL_MAGIC);
		out.writeJString(VersionInfo.RASCAL_VERSION);
		out.writeJString(VersionInfo.RASCAL_RUNTIME_VERSION);
		out.writeJString(VersionInfo.RASCAL_COMPILER_VERSION);
		
		// String[] errors
		
		out.writeValue(getErrors());
		
		if(!isValid()){
			return;
		}
		
		// public String module_name;

		out.writeJString(getModuleName());

		// public IMap moduleTags;
	
		out.writeValue(getModuleTags());

		// public IMap symbol_definitions;

		out.writeValue(getSymbolDefinitions());

		// public ArrayList<Function> functionStore;
		// public Map<String, Integer> functionMap;

		out.writeFunctionStore(getFunctionStore());

		// public ArrayList<Type> constructorStore;

		out.writeConstructorStore(getConstructorStore());

		// public Map<String, Integer> constructorMap;

		out.writeMapStringInt(getConstructorMap());

		// public ArrayList<OverloadedFunction> overloadedStore;

		out.writeArrayOverloadedFunctions(getOverloadedStore());

		// public Map<String, Integer> resolver;

		out.writeMapStringInt(getResolver());

		// ArrayList<String> initializers;

		out.writeArrayListString(getInitializers());

		// ArrayList<String> testsuites;

		out.writeArrayListString(getTestSuites());

		// public String uid_module_init;

		out.writeJString(getUidModuleInit());

		// public String uid_module_main;

		out.writeJString(getUidModuleMain());

		// public String uid_module_main_testsuite;

		out.writeJString(getUidModuleMainTestsuite());
		
		// public byte[] jvmByteCode;
		
		out.writeByteArray(getJvmByteCode());
		
		// public String fullyQualifiedDottedName;
		
		out.writeJString(getFullyQualifiedDottedName());
	}
	
	public static RVMExecutable read(RVMExecutableReader in) throws IOException{
		int n;

		// Read and check standard header

		// Check magic

		String rascal_magic = in.readJString();

		if(!rascal_magic.equals(RVMExecutable.RASCAL_MAGIC)){
			throw new RuntimeException("Cannot read incompatible Rascal executable");
		}

		// Check RASCAL_VERSION

		String rascal_version = in.readJString();

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

		String rascal_runtime_version = in.readJString();
		try {
			sv = new SemVer(rascal_runtime_version);
		} catch(Exception e){
			throw new RuntimeException("Invalid value for RASCAL_RUNTIME_VERSION in Rascal executable");
		}
		if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_RUNTIME_VERSION)){
			throw new RuntimeException("RASCAL_RUNTIME_VERSION " + rascal_runtime_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_RUNTIME_VERSION);
		}

		// Check RASCAL_COMPILER_VERSION

		String rascal_compiler_version = in.readJString();
		try {
			sv = new SemVer(rascal_runtime_version);
		} catch(Exception e){
			throw new RuntimeException("Invalid value for RASCAL_COMPILER_VERSION in Rascal executable");
		}
		if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_COMPILER_VERSION)){
			throw new RuntimeException("RASCAL_COMPILER_VERSION " + rascal_compiler_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_COMPILER_VERSION);
		}
		//			System.err.println("RascalShell: Rascal: " + VersionInfo.RASCAL_VERSION + "; Runtime: " + VersionInfo.RASCAL_RUNTIME_VERSION + "; Compiler: " + VersionInfo.RASCAL_COMPILER_VERSION);
		//			System.err.println("Executable : Rascal: " + rascal_version + "; Runtime: " + rascal_runtime_version + "; Compiler: " + rascal_compiler_version);

		// String[] errors

		ISet errors = (ISet) in.readValue();

		if(errors.size() > 0){
			return new RVMExecutable(errors);
		}

		// public String name;

		String module_name = in.readJString();

		// public IMap moduleTags;

		IMap moduleTags = (IMap) in.readValue();

		// public IMap symbol_definitions;

		IMap symbol_definitions = (IMap) in.readValue();

		// public ArrayList<Function> functionStore;
		// public  Map<String, Integer> functionMap;

		Function[] functionStore = in.readFunctionStore();

		n = functionStore.length;
		HashMap<String, Integer> functionMap = new HashMap<String, Integer>(n);
		for(int i = 0; i < n; i++){
			functionMap.put(functionStore[i].getName(), i);
		}

		// public ArrayList<Type> constructorStore;

		n = (Integer) in.readInt();
		ArrayList<Type> constructorStore = new ArrayList<Type>(n);

		for(int i = 0; i < n; i++){
			constructorStore.add(i, in.readType());
		}

		// public Map<String, Integer> constructorMap;

		Map<String, Integer> constructorMap = (Map<String, Integer>) in.readMapStringInt();

		// public OverloadedFunction[] overloadedStore;

		OverloadedFunction[] overloadedStore = (OverloadedFunction[]) in.readArrayOverloadedFunctions();

		// public Map<String, Integer> resolver;

		HashMap<String, Integer> resolver = (HashMap<String, Integer>) in.readMapStringInt();

		// ArrayList<String> initializers;

		ArrayList<String> initializers = in.readArrayListString();

		// ArrayList<String> testsuites;

		ArrayList<String> testsuites = in.readArrayListString();

		// public String uid_module_init;

		String uid_module_init = in.readJString();

		// public String uid_module_main;

		String uid_module_main = in.readJString();

		// public String uid_module_main_testsuite;

		String uid_module_main_testsuite = in.readJString();

		// public byte[] jvmByteCode;

		byte[] jvmByteCode = (byte[]) in.readByteArray();

		// public String fullyQualifiedDottedName;

		String fullyQualifiedDottedName = in.readJString();

		RVMExecutable ex = new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
				constructorMap, constructorStore, resolver, overloadedStore, initializers, testsuites, 
				uid_module_init, uid_module_main, uid_module_main_testsuite, store, vf, false);
		ex.setJvmByteCode(jvmByteCode);
		ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);

		return ex;
	}
	
	private static final int EXECUTABLE_MAGIC = 0;
    private static final int EXECUTABLE_RASCAL_VERSION = 1;
    private static final int EXECUTABLE_RASCAL_RUNTIME_VERSION = 2;
    private static final int EXECUTABLE_RASCAL_COMPILER_VERSION = 3;
    private static final int EXECUTABLE_ERRORS = 4;
    private static final int EXECUTABLE_MODULE_NAME = 5;
    private static final int EXECUTABLE_MODULE_TAGS = 6;
    private static final int EXECUTABLE_SYMBOL_DEFINITIONS = 7;
    private static final int EXECUTABLE_FUNCTION_STORE = 8;
    private static final int EXECUTABLE_CONSTRUCTOR_STORE = 9;
    private static final int EXECUTABLE_CONSTRUCTOR_MAP = 10;
    private static final int EXECUTABLE_OVERLOADED_STORE = 11;
    private static final int EXECUTABLE_RESOLVER = 12;
    private static final int EXECUTABLE_INITIALIZERS = 13;
    private static final int EXECUTABLE_TESTSUITES = 14;
    private static final int EXECUTABLE_MODULE_INIT = 15;
    private static final int EXECUTABLE_MODULE_MAIN = 16;
    private static final int EXECUTABLE_MODULE_MAIN_TESTSUITE = 17;
    private static final int EXECUTABLE_JVM_BYTECODE = 18;
    private static final int EXECUTABLE_FULLY_QUALIFIED_DOTTED_NAME = 19;
	
	public void writeRSF(RSFExecutableWriter writer) throws IOException {

	    writer.startMessage(RSFExecutableWriter.EXECUTABLE);
        
        // Write standard header
        
        writer.writeField(EXECUTABLE_MAGIC, RVMExecutable.RASCAL_MAGIC);
        
        writer.writeField(EXECUTABLE_RASCAL_VERSION, VersionInfo.RASCAL_VERSION);
        writer.writeField(EXECUTABLE_RASCAL_RUNTIME_VERSION, VersionInfo.RASCAL_RUNTIME_VERSION);
        writer.writeField(EXECUTABLE_RASCAL_COMPILER_VERSION, VersionInfo.RASCAL_COMPILER_VERSION);
        
        writer.writeField(EXECUTABLE_ERRORS, getErrors());
        
        if(!isValid()){
            writer.endMessage();
            return;
        }

        writer.writeField(EXECUTABLE_MODULE_NAME, getModuleName());
        writer.writeField(EXECUTABLE_MODULE_TAGS, getModuleTags());
        writer.writeField(EXECUTABLE_SYMBOL_DEFINITIONS, getSymbolDefinitions());
        writer.writeField(EXECUTABLE_FUNCTION_STORE, getFunctionStore());
        writer.writeField(EXECUTABLE_CONSTRUCTOR_STORE, getConstructorStore());
        writer.writeField(EXECUTABLE_CONSTRUCTOR_MAP, getConstructorMap());
        writer.writeField(EXECUTABLE_OVERLOADED_STORE, getOverloadedStore());
        writer.writeField(EXECUTABLE_RESOLVER, getResolver());
        writer.writeField(EXECUTABLE_INITIALIZERS, getInitializers());
        writer.writeField(EXECUTABLE_TESTSUITES, getTestSuites());
        writer.writeField(EXECUTABLE_MODULE_INIT, getUidModuleInit());
        writer.writeField(EXECUTABLE_MODULE_MAIN, getUidModuleMain());
        writer.writeField(EXECUTABLE_MODULE_MAIN_TESTSUITE, getUidModuleMainTestsuite());
        writer.writeField(EXECUTABLE_JVM_BYTECODE, getJvmByteCode());
        writer.writeField(EXECUTABLE_FULLY_QUALIFIED_DOTTED_NAME, getFullyQualifiedDottedName());
  
        writer.endMessage();
	}
    
    public static RVMExecutable readRSF(RVMExecutableReader in) throws IOException{
        int n;

        // Read and check standard header

        // Check magic

        String rascal_magic = in.readJString();

        if(!rascal_magic.equals(RVMExecutable.RASCAL_MAGIC)){
            throw new RuntimeException("Cannot read incompatible Rascal executable");
        }

        // Check RASCAL_VERSION

        String rascal_version = in.readJString();

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

        String rascal_runtime_version = in.readJString();
        try {
            sv = new SemVer(rascal_runtime_version);
        } catch(Exception e){
            throw new RuntimeException("Invalid value for RASCAL_RUNTIME_VERSION in Rascal executable");
        }
        if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_RUNTIME_VERSION)){
            throw new RuntimeException("RASCAL_RUNTIME_VERSION " + rascal_runtime_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_RUNTIME_VERSION);
        }

        // Check RASCAL_COMPILER_VERSION

        String rascal_compiler_version = in.readJString();
        try {
            sv = new SemVer(rascal_runtime_version);
        } catch(Exception e){
            throw new RuntimeException("Invalid value for RASCAL_COMPILER_VERSION in Rascal executable");
        }
        if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_COMPILER_VERSION)){
            throw new RuntimeException("RASCAL_COMPILER_VERSION " + rascal_compiler_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_COMPILER_VERSION);
        }
        //          System.err.println("RascalShell: Rascal: " + VersionInfo.RASCAL_VERSION + "; Runtime: " + VersionInfo.RASCAL_RUNTIME_VERSION + "; Compiler: " + VersionInfo.RASCAL_COMPILER_VERSION);
        //          System.err.println("Executable : Rascal: " + rascal_version + "; Runtime: " + rascal_runtime_version + "; Compiler: " + rascal_compiler_version);

        // String[] errors

        ISet errors = (ISet) in.readValue();

        if(errors.size() > 0){
            return new RVMExecutable(errors);
        }

        // public String name;

        String module_name = in.readJString();

        // public IMap moduleTags;

        IMap moduleTags = (IMap) in.readValue();

        // public IMap symbol_definitions;

        IMap symbol_definitions = (IMap) in.readValue();

        // public ArrayList<Function> functionStore;
        // public  Map<String, Integer> functionMap;

        Function[] functionStore = in.readFunctionStore();

        n = functionStore.length;
        HashMap<String, Integer> functionMap = new HashMap<String, Integer>(n);
        for(int i = 0; i < n; i++){
            functionMap.put(functionStore[i].getName(), i);
        }

        // public ArrayList<Type> constructorStore;

        n = (Integer) in.readInt();
        ArrayList<Type> constructorStore = new ArrayList<Type>(n);

        for(int i = 0; i < n; i++){
            constructorStore.add(i, in.readType());
        }

        // public Map<String, Integer> constructorMap;

        Map<String, Integer> constructorMap = (Map<String, Integer>) in.readMapStringInt();

        // public OverloadedFunction[] overloadedStore;

        OverloadedFunction[] overloadedStore = (OverloadedFunction[]) in.readArrayOverloadedFunctions();

        // public Map<String, Integer> resolver;

        HashMap<String, Integer> resolver = (HashMap<String, Integer>) in.readMapStringInt();

        // ArrayList<String> initializers;

        ArrayList<String> initializers = in.readArrayListString();

        // ArrayList<String> testsuites;

        ArrayList<String> testsuites = in.readArrayListString();

        // public String uid_module_init;

        String uid_module_init = in.readJString();

        // public String uid_module_main;

        String uid_module_main = in.readJString();

        // public String uid_module_main_testsuite;

        String uid_module_main_testsuite = in.readJString();

        // public byte[] jvmByteCode;

        byte[] jvmByteCode = (byte[]) in.readByteArray();

        // public String fullyQualifiedDottedName;

        String fullyQualifiedDottedName = in.readJString();

        RVMExecutable ex = new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
                constructorMap, constructorStore, resolver, overloadedStore, initializers, testsuites, 
                uid_module_init, uid_module_main, uid_module_main_testsuite, store, vf, false);
        ex.setJvmByteCode(jvmByteCode);
        ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);

        return ex;
    }
}