package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.library.experiments.Compiler.VersionInfo;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.CompilerIDs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireInputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireOutputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMWireInputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMWireOutputStream;
import org.rascalmpl.library.util.SemVer;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireOutputStream;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

/**
 * RVMExecutable contains all data needed for executing an RVM program.
 *
 * RVMExecutable is serialized by write and read defined here
 */
public class RVMExecutable {
	
	static final String RASCAL_MAGIC = "Rascal Vincit Omnia";
	
	// transient fields
	private static IValueFactory vf;
	private TypeStore typeStore;
	
	// Serializable fields
	
	private ISet errors;
	private String module_name;
	private IMap moduleTags;
	
	private IMap symbol_definitions;
	
	private Function[] functionStore;
	private  Map<String, Integer> functionMap;
	
	// Constructors
	private List<Type> constructorStore;
	private Map<String, Integer> constructorMap;
	
	// Function overloading
	private OverloadedFunction[] overloadedStore;
	private Map<String, Integer> resolver;
	
	private List<String> initializers;
	private String uid_module_init;
	private String uid_module_main;
	
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
			final List<Type> constructorStore,
	
			final Map<String, Integer> resolver,
			final OverloadedFunction[] overloadedStore,
			
			List<String> initializers2,
			String uid_module_init,
			String uid_module_main,
			IValueFactory vfactory,
			boolean jvm
			) throws IOException{
		
		vf = vfactory;
		this.errors = vf.set();
		
		this.module_name = module_name;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		this.typeStore = null;
		
		this.functionMap = functionMap;
		this.functionStore = functionStore;
		
		this.constructorMap = constructorMap;
		this.constructorStore = constructorStore;

		this.resolver = resolver;
		this.overloadedStore = overloadedStore;
		
		this.initializers = initializers2;
		
		this.uid_module_init = uid_module_init;
		this.uid_module_main = uid_module_main;
		
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
	
	public TypeStore getTypeStore() { 
        if(typeStore == null) {
            typeStore = new TypeReifier(vf).buildTypeStore(symbol_definitions);
        }
        return typeStore;
    }

	public Function[] getFunctionStore() {
		return functionStore;
	}

	public Map<String, Integer> getFunctionMap() {
		return functionMap;
	}

	public List<Type> getConstructorStore() {
		return constructorStore;
	}

	public Map<String, Integer> getConstructorMap() {
		return constructorMap;
	}
	
	OverloadedFunction[] getOverloadedStore() {
		return overloadedStore;
	}
	
	Map<String, Integer> getResolver() {
		return resolver;
	}
	
	List<String> getInitializers() {
		return initializers;
	}
	
	public ArrayList<Function> getTests(){
	  ArrayList<Function> tests = new ArrayList<>();
	  if(functionStore != null){
	    for(Function f : functionStore){
	      if(f.isTest){
	        tests.add(f);
	      }
	    }
	  }
	  return tests;
	}
	
	public IList executeTests(ITestResultListener testResultListener, RascalExecutionContext rex){
	  IListWriter w = vf.listWriter();
	  for(Function f : functionStore){
	    if(f.isTest){
	      w.append(f.executeTest(testResultListener, getTypeStore(), rex));
	    }
	  }
	  return w.done();
	}

	String getUidModuleInit() {
		return uid_module_init;
	}

	String getUidModuleMain() {
		return uid_module_main;
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
		        throw new RuntimeException("Function too large: " + Arrays.stream(functionStore).max(c).get(), e);
		    }
		    else {
		        throw e;
		    }
		}
	}
	
	static byte EXEC_HEADER[] = new byte[] { 'R', 'V','M' };
	static byte[] EXEC_VERSION = new byte[] {1, 0, 0};
	static byte EXEC_COMPRESSION_NONE = 0;
	static byte EXEC_COMPRESSION_GZIP = 1;
	static byte EXEC_COMPRESSION_ZSTD = 2;
	
	
	@SuppressWarnings("resource")
    public void write(ISourceLocation rvmExecutable, int compressionLevel) throws IOException {
	    try(OutputStream out = URIResolverRegistry.getInstance().getOutputStream(rvmExecutable, false)){
	        out.write(EXEC_HEADER);
	        out.write(EXEC_VERSION);
	        out.write(compressionLevel > 0 ? EXEC_COMPRESSION_ZSTD : EXEC_COMPRESSION_NONE);
	        OutputStream cout = out;
	        if(compressionLevel > 0){
	            cout = new BufferedOutputStream(new ZstdOutputStream(out, compressionLevel));
	        }
	        try(IRVMWireOutputStream iout = new RVMWireOutputStream(new BinaryWireOutputStream(cout, 50_000), 50_000)){
	            write(iout);
	        }
	    }
	}
	
    private void write(IRVMWireOutputStream out) throws IOException {
	    out.startMessage(CompilerIDs.Executable.ID);
	    
	    // Write standard header
        out.writeField(CompilerIDs.Executable.RASCAL_MAGIC, RVMExecutable.RASCAL_MAGIC);
        out.writeField(CompilerIDs.Executable.RASCAL_VERSION, VersionInfo.RASCAL_VERSION);
        out.writeField(CompilerIDs.Executable.RASCAL_RUNTIME_VERSION, VersionInfo.RASCAL_RUNTIME_VERSION);
        out.writeField(CompilerIDs.Executable.RASCAL_COMPILER_VERSION, VersionInfo.RASCAL_COMPILER_VERSION);
        
        out.writeField(CompilerIDs.Executable.ERRORS, getErrors(), WindowSizes.TINY_WINDOW);
                  
        if(!isValid()){
            return;
        }

        out.writeField(CompilerIDs.Executable.MODULE_NAME, getModuleName());


        out.writeField(CompilerIDs.Executable.MODULE_TAGS, getModuleTags(), WindowSizes.TINY_WINDOW);

        out.writeField(CompilerIDs.Executable.SYMBOL_DEFINITIONS, getSymbolDefinitions(), WindowSizes.NORMAL_WINDOW);

        out.writeFieldStringInt(CompilerIDs.Executable.FUNCTION_MAP, getFunctionMap());
        
        out.writeFieldStringInt(CompilerIDs.Executable.CONSTRUCTOR_MAP, getConstructorMap());
        
        out.writeFieldStringInt(CompilerIDs.Executable.RESOLVER, getResolver());
        
        // FUNCTION_MAP, CONSTRUCTOR_MAP and RESOLVER should come before FUNCTION_STORE
        
        out.writeRepeatedNestedField(CompilerIDs.Executable.FUNCTION_STORE, functionStore.length);
        for(Function function : functionStore){
            function.write(out);
        }

        out.writeField(CompilerIDs.Executable.CONSTRUCTOR_STORE, constructorStore.toArray(new Type[constructorStore.size()]), WindowSizes.SMALL_WINDOW);

        out.writeRepeatedNestedField(CompilerIDs.Executable.OVERLOADED_STORE, getOverloadedStore().length);
        for(OverloadedFunction ovl : getOverloadedStore()){
            ovl.write(out);
        }

        out.writeField(CompilerIDs.Executable.INITIALIZERS, 
            getInitializers().toArray(new String[getInitializers().size()]));

        out.writeField(CompilerIDs.Executable.UID_MODULE_INIT, getUidModuleInit());

        out.writeField(CompilerIDs.Executable.UID_MODULE_MAIN, getUidModuleMain());
        
        out.writeField(CompilerIDs.Executable.JVM_BYTE_CODE, getJvmByteCode());
        
        out.writeField(CompilerIDs.Executable.FULLY_QUALIFIED_DOTTED_NAME, getFullyQualifiedDottedName());
        
        out.endMessage();
    }
	
	@SuppressWarnings("resource")
    public static RVMExecutable read(ISourceLocation rvmExecutable) throws IOException {
	    try(InputStream in = URIResolverRegistry.getInstance().getInputStream(rvmExecutable)){
	        byte[] header = new byte[EXEC_HEADER.length];
	        in.read(header);
	        if(Arrays.equals(header, EXEC_HEADER)){
	            byte[] version = new byte[EXEC_VERSION.length];
	            in.read(version);
	            if(!Arrays.equals(version, EXEC_VERSION)){
	                throw new IOException("Incorrect version");
	            }
	            int compression = in.read();
	            InputStream cin = in;
	            if(compression != EXEC_COMPRESSION_NONE){
	                cin = new BufferedInputStream(new ZstdInputStream(in));
	            }
	            try(IRVMWireInputStream win = new RVMWireInputStream(new BinaryWireInputStream(cin), ValueFactoryFactory.getValueFactory())){
	                return read(win, ValueFactoryFactory.getValueFactory());
	            }                      
	        } else {
	            throw new IOException("RVMExcutable cannot read legacy data format, please recompile");
	        }
	    }
	}
	
	private static RVMExecutable read(IRVMWireInputStream in, IValueFactory vf) throws IOException {
	    ISet errors = vf.set();
	    String module_name = "unitialized module_name";
	    
	    IMap emptyIMap = vf.mapWriter().done();
	    IMap moduleTags = emptyIMap;
	    IMap symbol_definitions = emptyIMap;

	    Function[] functionStore = new Function[0];
	    
	    Map<String, Integer> functionMap = null;

	    // Constructors
	    List<Type> constructorStore = new ArrayList<>();
	    Map<String, Integer> constructorMap = null;

	    // Function overloading
	    OverloadedFunction[] overloadedStore = new OverloadedFunction[0];
	    Map<String, Integer> resolver = null;

	    List<String> initializers = new ArrayList<>();
	    String uid_module_init = "unitialized uid_module_init";
	    String uid_module_main = "unitialized uid_module_main";

	    byte[] jvmByteCode = new byte[0];
	    String fullyQualifiedDottedName = "unitialized fullyQualifiedDottedName";
	    
	    in.next();
        assert in.current() == IWireInputStream.MESSAGE_START;
        if(in.message() != CompilerIDs.Executable.ID){
            throw new IOException("Unexpected message: " + in.message());
        }
        while(in.next() != IWireInputStream.MESSAGE_END){
            switch(in.field()){
                
                case CompilerIDs.Executable.RASCAL_MAGIC: {
                    String rascal_magic = in.getString();
                    if(!rascal_magic.equals(RVMExecutable.RASCAL_MAGIC)){
                        throw new RuntimeException("Cannot read incompatible Rascal executable");
                    }
                    break;
                }
                
                case CompilerIDs.Executable.RASCAL_VERSION: {
                    String rascal_version = in.getString();
                    SemVer sv;
                    try {
                        sv = new SemVer(rascal_version);
                    } catch(Exception e){
                        throw new RuntimeException("Invalid value for RASCAL_VERSION in Rascal executable");
                    }
                    
                    if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_VERSION)){
                        throw new RuntimeException("RASCAL_VERSION " + rascal_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_VERSION);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.RASCAL_RUNTIME_VERSION: {
                    String rascal_runtime_version = in.getString();
                    SemVer sv;
                    try {
                        sv = new SemVer(rascal_runtime_version);
                    } catch(Exception e){
                        throw new RuntimeException("Invalid value for RASCAL_RUNTIME_VERSION in Rascal executable");
                    }
                    if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_RUNTIME_VERSION)){
                        throw new RuntimeException("RASCAL_RUNTIME_VERSION " + rascal_runtime_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_RUNTIME_VERSION);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.RASCAL_COMPILER_VERSION: {
                    String rascal_compiler_version = in.getString();
                    SemVer sv;
                    try {
                        sv = new SemVer(rascal_compiler_version);
                    } catch(Exception e){
                        throw new RuntimeException("Invalid value for RASCAL_COMPILER_VERSION in Rascal executable");
                    }
                    if(!sv.satisfiesVersion("~" + VersionInfo.RASCAL_COMPILER_VERSION)){
                        throw new RuntimeException("RASCAL_COMPILER_VERSION " + rascal_compiler_version + " in Rascal executable incompatible with current version " + VersionInfo.RASCAL_COMPILER_VERSION);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.ERRORS: {
                    errors = in.readIValue();
                    if(errors.size() > 0){
                        System.err.println("Executable.ERRORS: " + errors);
                        return new RVMExecutable(errors);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.MODULE_NAME: {
                    module_name = in.getString();
                    break;
                }
                
                case CompilerIDs.Executable.MODULE_TAGS: {
                    moduleTags = in.readIValue();
                    break;
                }
                
                case CompilerIDs.Executable.SYMBOL_DEFINITIONS: {
                    symbol_definitions = in.readIValue();
                    break;
                }
                
                case CompilerIDs.Executable.FUNCTION_MAP: {
                    functionMap  = in.readStringIntegerMap();
                    break;
                }
                
                case CompilerIDs.Executable.CONSTRUCTOR_MAP: {
                    constructorMap = in.readStringIntegerMap();
                    break;
                }
                
                case CompilerIDs.Executable.FUNCTION_STORE: {
                    int n = in.getRepeatedLength();
                    functionStore = new Function[n];
                    if(functionMap == null){
                        throw new IOException("FUNCTION_MAP should be defined before FUNCTION_STORE");
                    }
                    if(constructorMap == null){
                        throw new IOException("CONSTRUCTOR_MAP should be defined before FUNCTION_STORE");
                    }
                    if(resolver == null){
                        throw new IOException("RESOLVER should be defined before FUNCTION_STORE");
                    }
                    for(int i = 0; i < n; i++){
                        Function function = Function.read(in, functionMap, constructorMap, resolver);
                        functionStore[i] = function;
                    }
                    break;
                }
                
                case CompilerIDs.Executable.CONSTRUCTOR_STORE: {
                    constructorStore = Arrays.asList(in.readTypes());
                    break;
                }
                
                case CompilerIDs.Executable.OVERLOADED_STORE: {
                    int n = in.getRepeatedLength();
                    overloadedStore = new OverloadedFunction[n];
                    for(int i = 0; i < n; i++){
                        overloadedStore[i] = OverloadedFunction.read(in);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.RESOLVER: {
                    resolver = in.readStringIntegerMap();
                    break;
                }
                
                case CompilerIDs.Executable.INITIALIZERS: {
                    initializers = Arrays.asList(in.getStrings());
                    break;
                }
                
                case CompilerIDs.Executable.UID_MODULE_INIT: {
                    uid_module_init = in.getString();
                    break;
                }
                
                case CompilerIDs.Executable.UID_MODULE_MAIN: {
                    uid_module_main = in.getString();
                    break;
                }
                
                case CompilerIDs.Executable.JVM_BYTE_CODE: {
                    jvmByteCode = in.getBytes();
                    break;
                }
                
                case CompilerIDs.Executable.FULLY_QUALIFIED_DOTTED_NAME: {
                    fullyQualifiedDottedName = in.getString();
                    break;
                }
                
                default: {
                    System.err.println("Executable.read, skips " + in.field());
                    // skip field, normally next takes care of it
                    in.skipNestedField();
                }
            }
        }

	    RVMExecutable ex = new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
	        constructorMap, constructorStore, resolver, overloadedStore, initializers, uid_module_init, 
	        uid_module_main, vf, false);
	    ex.setJvmByteCode(jvmByteCode);
	    ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);

	    return ex;
	}
}