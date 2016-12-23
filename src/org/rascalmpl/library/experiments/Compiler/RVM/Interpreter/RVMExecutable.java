package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
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
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.library.experiments.Compiler.VersionInfo;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.CompilerIDs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMWireExtensions;
import org.rascalmpl.library.util.SemVer;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.message.IValueReader;
import org.rascalmpl.value.io.binary.message.IValueWriter;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;
import org.rascalmpl.value.io.binary.util.WindowCacheFactory;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireInputStream;
import org.rascalmpl.value.io.binary.wire.binary.BinaryWireOutputStream;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

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
			final ArrayList<Type> constructorStore,
	
			final Map<String, Integer> resolver,
			final OverloadedFunction[] overloadedStore,
			
			ArrayList<String> initializers,
			String uid_module_init,
			String uid_module_main,
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

	public Function[] getFunctionStore() {
		return functionStore;
	}

	public Map<String, Integer> getFunctionMap() {
		return functionMap;
	}

	public ArrayList<Type> getConstructorStore() {
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
	
	ArrayList<String> getInitializers() {
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
	      w.append(f.executeTest(testResultListener, rex));
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
	
//	public void write(ISourceLocation rvmExecutable) throws IOException{		
//		OutputStream fileOut;
//		
//		TypeStore typeStore = RascalValueFactory.getStore(); //new TypeStore(RascalValueFactory.getStore());
//		
//		FSTSerializableType.initSerialization(vf, typeStore);
//		FSTSerializableIValue.initSerialization(vf, typeStore);
//		
//		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
//		FSTFunctionSerializer.initSerialization(vf, typeStore);
//		FSTCodeBlockSerializer.initSerialization(vf, typeStore);
//
//		ISourceLocation compOut = rvmExecutable;
//		fileOut = URIResolverRegistry.getInstance().getOutputStream(compOut, false);
//		FSTObjectOutput out = new FSTObjectOutput(fileOut, makeFSTConfig(rvmExecutable));
//		//long before = Timing.getCpuTime();
//		out.writeObject(this);
//		out.close();
//		//System.out.println("RVMExecutable.write: " + compOut.getPath() + " [" +  (Timing.getCpuTime() - before)/1000000 + " msec]");
//	}
	
	static byte EXEC_HEADER[] = new byte[] { 'R', 'V','M' };
	static byte[] EXEC_VERSION = new byte[] {1, 0, 0};
	static byte EXEC_COMPRESSION_NONE = 0;
	static byte EXEC_COMPRESSION_GZIP = 1;
	static byte EXEC_COMPRESSION_ZSTD = 2;
	
	
	public void newWrite(ISourceLocation rvmExecutable, int compressionLevel) throws IOException {
	    TypeStore typeStore = new TypeStore(RascalValueFactory.getStore());
	    try(OutputStream out = URIResolverRegistry.getInstance().getOutputStream(rvmExecutable, false)){
	        out.write(EXEC_HEADER);
	        out.write(EXEC_VERSION);
	        out.write(compressionLevel > 0 ? EXEC_COMPRESSION_ZSTD : EXEC_COMPRESSION_NONE);
	        OutputStream cout = out;
	        if(compressionLevel > 0){
	            cout = new ZstdOutputStream(out, compressionLevel);
	        }
	        try(IWireOutputStream iout = new BinaryWireOutputStream(cout, 50_000)){
	            write(iout, typeStore, WindowCacheFactory.getInstance().getTrackLastWrittenReferenceEquality(50_000));
	        }
	    }
	}
	

    private void write(IWireOutputStream out, TypeStore typeStore, TrackLastWritten<Object> lastWritten) throws IOException {
	    
	    out.startMessage(CompilerIDs.Executable.ID);
	    
	    // Write standard header
        out.writeField(CompilerIDs.Executable.RASCAL_MAGIC, RVMExecutable.RASCAL_MAGIC);
        out.writeField(CompilerIDs.Executable.RASCAL_VERSION, VersionInfo.RASCAL_VERSION);
        out.writeField(CompilerIDs.Executable.RASCAL_RUNTIME_VERSION, VersionInfo.RASCAL_RUNTIME_VERSION);
        out.writeField(CompilerIDs.Executable.RASCAL_COMPILER_VERSION, VersionInfo.RASCAL_COMPILER_VERSION);
        
        RVMWireExtensions.nestedOrReference(out, CompilerIDs.Executable.ERRORS, getErrors(), lastWritten,  
            (o, e) -> IValueWriter.write(o, WindowSizes.TINY_WINDOW, e));
                  
        if(!isValid()){
            return;
        }

        out.writeField(CompilerIDs.Executable.MODULE_NAME, getModuleName());

        RVMWireExtensions.nestedOrReference(out, CompilerIDs.Executable.MODULE_TAGS, getModuleTags(), lastWritten,
            (o, v) -> IValueWriter.write(o, WindowSizes.TINY_WINDOW, v));

        RVMWireExtensions.nestedOrReference(out, CompilerIDs.Executable.SYMBOL_DEFINITIONS, getSymbolDefinitions(), lastWritten,
            (o, v) -> IValueWriter.write(o, WindowSizes.NORMAL_WINDOW, v));

        out.writeField(CompilerIDs.Executable.FUNCTION_MAP, getFunctionMap());
        
        out.writeField(CompilerIDs.Executable.CONSTRUCTOR_MAP, getConstructorMap());
        
        out.writeField(CompilerIDs.Executable.RESOLVER, getResolver());
        
        // FUNCTION_MAP, CONSTRUCTOR_MAP and RESOLVER should come before FUNCTION_STORE
        
        out.writeRepeatedNestedField(CompilerIDs.Executable.FUNCTION_STORE, functionStore.length);
        for(Function function : functionStore){
            function.write(out, lastWritten);
        }

        RVMWireExtensions.nestedOrReferenceRepeated(out, CompilerIDs.Executable.CONSTRUCTOR_STORE, constructorStore, lastWritten,
            (o, t) -> IValueWriter.write(o, WindowSizes.SMALL_WINDOW, t));

        out.writeRepeatedNestedField(CompilerIDs.Executable.OVERLOADED_STORE, getOverloadedStore().length);
        for(OverloadedFunction ovl : getOverloadedStore()){
            ovl.write(out, lastWritten);
        }

        out.writeField(CompilerIDs.Executable.INITIALIZERS, 
            getInitializers().toArray(new String[getInitializers().size()]));

        out.writeField(CompilerIDs.Executable.UID_MODULE_INIT, getUidModuleInit());

        out.writeField(CompilerIDs.Executable.UID_MODULE_MAIN, getUidModuleMain());
        
        System.err.println("JVM_BYTE_CODE: " + getJvmByteCode().length + "bytes written");
        out.writeField(CompilerIDs.Executable.JVM_BYTE_CODE, getJvmByteCode());
        
        out.writeField(CompilerIDs.Executable.JVM_BYTE_CODE_SIZE, getJvmByteCode().length);
        
        out.writeField(CompilerIDs.Executable.FULLY_QUALIFIED_DOTTED_NAME, getFullyQualifiedDottedName());
        
        out.endMessage();
    }
	
	public static RVMExecutable newRead(ISourceLocation rvmExecutable, TypeStore typeStore) throws IOException{
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
	                cin = new ZstdInputStream(in);
	            }
	            try(IWireInputStream win = new BinaryWireInputStream(cin)){
	                return read(win, ValueFactoryFactory.getValueFactory(), WindowCacheFactory.getInstance().getTrackLastRead(50_000));
	            }                      
	        } else {
	            vf = ValueFactoryFactory.getValueFactory();
	            FSTSerializableType.initSerialization(vf, typeStore);
	            FSTSerializableIValue.initSerialization(vf, typeStore);
	        
	            FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
	            FSTFunctionSerializer.initSerialization(vf, typeStore);
	            FSTCodeBlockSerializer.initSerialization(vf, typeStore);
	        
	            try (InputStream fileIn = new SequenceInputStream(new ByteArrayInputStream(header), in);
	                    FSTObjectInput fstIn = new FSTObjectInput(fileIn, makeFSTConfig(rvmExecutable))) {
	                return (RVMExecutable) fstIn.readObject(RVMExecutable.class);
	            } catch (ClassNotFoundException c) {
	                throw new IOException("Class not found: " + c.getMessage(), c);
	            } catch (Throwable e) {
	                throw new IOException(e.getMessage(), e);
	            } 
	        }
	    }
	}
	
	private static RVMExecutable read(IWireInputStream in, IValueFactory vf, TrackLastRead<Object> lastRead) throws IOException {
	    
	    System.err.println("Reading Executable");
	    
	    // Serializable fields

	    ISet errors = vf.set();
	    String module_name = "unitialized module_name";
	    
	    IMap emptyIMap = vf.mapWriter().done();
	    IMap moduleTags = emptyIMap;
	    IMap symbol_definitions = emptyIMap;

	    Function[] functionStore = new Function[0];
	    
	    Map<String, Integer> functionMap = null;

	    // Constructors
	    ArrayList<Type> constructorStore = new ArrayList<>();
	    Map<String, Integer> constructorMap = null;

	    // Function overloading
	    OverloadedFunction[] overloadedStore = new OverloadedFunction[0];
	    Map<String, Integer> resolver = null;

	    ArrayList<String> initializers = new ArrayList<>();
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
            System.err.println("Executable.read: field " + in.field());
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
                    errors = RVMWireExtensions.readNestedOrReference(in, vf, lastRead,
                        (i, v) -> (ISet)IValueReader.read(in, v));
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
                    moduleTags = RVMWireExtensions.readNestedOrReference(in, vf, lastRead,
                        (i, v) -> (IMap)IValueReader.read(in, v));
                    break;
                }
                
                case CompilerIDs.Executable.SYMBOL_DEFINITIONS: {
                    symbol_definitions = RVMWireExtensions.readNestedOrReference(in, vf, lastRead,
                        (i, v) -> (IMap)IValueReader.read(in, v));
                    break;
                }
                
                case CompilerIDs.Executable.FUNCTION_MAP: {
                    functionMap  = in.getStringIntegerMap();
                    break;
                }
                
                case CompilerIDs.Executable.CONSTRUCTOR_MAP: {
                    constructorMap = in.getStringIntegerMap();
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
                        Function function = Function.read(in, vf, functionMap, constructorMap, resolver, lastRead);
                        functionStore[i] = function;
                    }
                    break;
                }
                
                case CompilerIDs.Executable.CONSTRUCTOR_STORE: {
                    constructorStore = RVMWireExtensions.readNestedOrReferenceRepeatedList(in, vf, lastRead,
                        IValueReader::readType);
                    break;
                }
                
                case CompilerIDs.Executable.OVERLOADED_STORE: {
                    int n = in.getRepeatedLength();
                    overloadedStore = new OverloadedFunction[n];
                    for(int i = 0; i < n; i++){
                        overloadedStore[i] = OverloadedFunction.read(in, vf, lastRead);
                    }
                    break;
                }
                
                case CompilerIDs.Executable.RESOLVER: {
                    resolver = in.getStringIntegerMap();
                    break;
                }
                
                case CompilerIDs.Executable.INITIALIZERS: {
                    initializers = (ArrayList<String>) Arrays.asList(in.getStrings());
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
                    System.err.println("JVM_BYTE_CODE: " + in.getRepeatedLength() + " repeatedLength");
                    jvmByteCode = in.getBytes();
                    System.err.println("JVM_BYTE_CODE: " + jvmByteCode.length + " bytes read");
                    break;
                }
                
                case CompilerIDs.Executable.JVM_BYTE_CODE_SIZE: {
                    System.err.println("JVM_BYTE_CODE_SIZE: " + in.getInteger() + " bytes");
                    System.err.println("JVM_BYTE_CODE_SIZE: " + in.getRepeatedLength() + " repeatedLength");
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
	        uid_module_main, store, vf, false);
	    ex.setJvmByteCode(jvmByteCode);
	    ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);

	    return ex;
	}

    public static RVMExecutable read(ISourceLocation rvmExecutable, TypeStore typeStore) throws IOException {
		vf = ValueFactoryFactory.getValueFactory();
//		TypeStore typeStore = new TypeStore(RascalValueFactory.getStore());
		
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
	
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);
	
		try (InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(rvmExecutable);
		        FSTObjectInput in = new FSTObjectInput(fileIn, makeFSTConfig(rvmExecutable))) {
		    return (RVMExecutable) in.readObject(RVMExecutable.class);
		} catch (ClassNotFoundException c) {
		    throw new IOException("Class not found: " + c.getMessage(), c);
		} catch (Throwable e) {
		    throw new IOException(e.getMessage(), e);
		} 
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

		// public String uid_module_init;

		out.writeObject(ex.getUidModuleInit());

		// public String uid_module_main;

		out.writeObject(ex.getUidModuleMain());
		
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
				
		// ISet errors
				
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

		Object o = in.readObject();   //transient for boot
		// ArrayList<String> testsuites;

		if(o instanceof ArrayList<?>){
		  o = in.readObject();    // skip: ArrayList<String> testsuites;
		}

		// public String uid_module_init;

		String uid_module_init = (String) o;

		// public String uid_module_main;

		String uid_module_main = (String) in.readObject();

		o = in.readObject();   //transient for boot
		if(o instanceof String){
		  o = in.readObject(); // skip: public String uid_module_main_testsuite;
		}
		
		// public byte[] jvmByteCode;
		
		byte[] jvmByteCode = (byte[]) o;
				
		// public String fullyQualifiedDottedName;
	
		String fullyQualifiedDottedName = (String) in.readObject();

		RVMExecutable ex = new RVMExecutable(module_name, moduleTags, symbol_definitions, functionMap, functionStore, 
								constructorMap, constructorStore, resolver, overloadedStore, initializers, uid_module_init, 
								uid_module_main, store, vf, false);
		ex.setJvmByteCode(jvmByteCode);
		ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);
		
		return ex;
	}
}
