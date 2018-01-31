package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

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
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.IRascalValueFactory;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.binary.util.ByteBufferInputStream;
import io.usethesource.vallang.io.binary.util.ByteBufferOutputStream;
import io.usethesource.vallang.io.binary.util.DelayedCompressionOutputStream;
import io.usethesource.vallang.io.binary.util.DelayedZstdOutputStream;
import io.usethesource.vallang.io.binary.util.DirectZstdInputStream;
import io.usethesource.vallang.io.binary.util.FileChannelDirectInputStream;
import io.usethesource.vallang.io.binary.util.FileChannelDirectOutputStream;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireInputStream;
import io.usethesource.vallang.io.binary.wire.binary.BinaryWireInputStream;
import io.usethesource.vallang.io.binary.wire.binary.BinaryWireOutputStream;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

/**
 * RVMExecutable contains all data needed for executing an RVM program.
 *
 * RVMExecutable is serialized by write and read defined here
 */
public class RVMExecutable {
	
	static final String RASCAL_MAGIC = "Rascal Vincit Omnia";
	static boolean validating = true;
	
	// transient fields
	private final IValueFactory vf;
	private TypeStore typeStore;
	
	// Serializable fields
	
	private final ISet errors;
	private final String module_name;
	private final IMap moduleTags;
	
	private final IMap symbol_definitions;
	
	private final Function[] functionStore;
	private final Map<String, Integer> functionMap;
	
	// Constructors
	private final Type[] constructorStore;
	private final Map<String, Integer> constructorMap;
	
	// Function overloading
	private final OverloadedFunction[] overloadedStore;
	
	private final List<String> initializers;
	private final String uid_module_init;
	private final String uid_module_main;
	
	private byte[] jvmByteCode;
	private String fullyQualifiedDottedName;
	
	public RVMExecutable(ISet errors){
		this.vf = IRascalValueFactory.getInstance();
		this.errors = errors;
        
        this.module_name = null;
        this.moduleTags = null;
        this.symbol_definitions = null;
        this.typeStore = null;
        
        this.functionMap = null;
        this.functionStore = null;
        
        this.constructorMap = null;
        this.constructorStore = null;

        this.overloadedStore = null;
        
        this.initializers = null;
        
        this.uid_module_init = null;
        this.uid_module_main = null;
	}
	
	public RVMExecutable(
			final String module_name,
			final IMap moduleTags,
			
			final IMap symbol_definitions,
			final Map<String, Integer> functionMap,
			final Function[] functionStore,
			
			final Map<String, Integer> constructorMap,
			final Type[] constructorStore,
	
			final OverloadedFunction[] overloadedStore,
			List<String> initializers2,
			
			String uid_module_init,
			String uid_module_main,
			IValueFactory vfactory,
			boolean jvm,
			final Map<String,String> classRenamings
			) throws IOException{
	
//	    System.err.println("RVMExecutable " + module_name);
//	    System.err.println("functionStore:    " + functionStore.length);
//	    System.err.println("constructorStore: " + constructorStore.length);
//	    System.err.println("overloadedStore:  " + overloadedStore.length);
	    
		this.vf = vfactory;
		this.errors = vf.set();
		
		this.module_name = module_name;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		this.typeStore = null;
		
		this.functionMap = functionMap;
		this.functionStore = functionStore;
		
		this.constructorMap = constructorMap;
		this.constructorStore = constructorStore;

		this.overloadedStore = overloadedStore;
		
		this.initializers = initializers2;
		
		this.uid_module_init = uid_module_init;
		this.uid_module_main = uid_module_main;
		
		if(validating){
		    validate();
		    fids2objects();
		}
		
		if(jvm){
			generateClassFile(false, classRenamings);
//			System.err.println("jvmByteCode:      " + jvmByteCode.length);
			removeCodeBlocks();
		}
	}
	
	void removeCodeBlocks(){
		for(Function f : functionStore){
			f.removeCodeBlocks();
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

	public Type[] getConstructorStore() {
		return constructorStore;
	}

	public Map<String, Integer> getConstructorMap() {
		return constructorMap;
	}
	
	OverloadedFunction[] getOverloadedStore() {
		return overloadedStore;
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

	void generateClassFile(boolean debug, Map<String,String> classRenamings) {
		try {			
			BytecodeGenerator codeEmittor = new BytecodeGenerator(functionStore, overloadedStore, functionMap, constructorMap, classRenamings);
	
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
	
	private void validateFunctions(){
        int nfun = functionStore.length;
        if(nfun != functionMap.size()){
            throw new RuntimeException("functionStore and functionMap have different size: " + nfun + " vs " + functionMap.size());
        }
        for(String fname : functionMap.keySet()){
            int n = functionMap.get(fname);
            if(functionStore[n] == null){
                throw new RuntimeException("FunctionStore has null entry for: "+ fname + " at index " + n);
            }
        }
        
        for(Function fn : functionStore){
            if(fn.scopeIn >= 0 && fn.scopeIn >= functionStore.length){
                throw new RuntimeException("Function " + fn.name + ": scopeIn out of range (" + fn.scopeIn + ")");
            }
        }
    }
	
	private void validateConstructors(){
        int ncon = constructorStore.length;
        if(ncon != constructorMap.size()){
            throw new RuntimeException("constructorStore and constructorMap have different size: " + ncon + " vs " + constructorMap.size());
        }
        for(String cname : constructorMap.keySet()){
            int n = constructorMap.get(cname);
            if(constructorStore[n] == null){
                throw new RuntimeException("ConstructorStore has null entry for: "+ cname + " at index " + n);
            }
        }
    }
	
	private void validateOverloading(){
        
        for(OverloadedFunction ovf : overloadedStore){
            int nfun = functionStore.length;
            if(ovf.scopeIn >= 0 && ovf.scopeIn > nfun){
                System.err.println(ovf.name + ": scopeIn out-of-range (" + ovf.scopeIn + ") should be in [0.." + nfun + "]");
            }
            for(int fid : ovf.functions){
                if(fid < 0 || fid >= nfun){
                    throw new RuntimeException("OverloadedFunction " + ovf.name + ": functions contains out-of-range fid (" + fid + ") should be in [0.." + nfun + "]");
                }
            }
            int ncon = constructorStore.length;
            for(int cid : ovf.constructors){
                if(cid < 0 || cid >= ncon){
                    throw new RuntimeException("OverloadedFunction " + ovf.name + ": constructors contains out-of-range cid (" + cid + ") should be in [0.." + ncon + "]");
                }
            }
            if(ovf.filteredFunctions != null){
                for(int[] funs : ovf.filteredFunctions.values()){
                    for(int fid : funs){
                        if(fid < 0 || fid >= nfun){
                            throw new RuntimeException("OverloadedFunction " + ovf.name + ": filteredFunctions contains out-of-range fid (" + fid + ") should be in [0.." + nfun + "]");
                        }
                    }
                }
            }
        }
    }
	
	private void validate(){
	    validateFunctions();
	    validateConstructors();
	    validateOverloading();
	}
	
	private void fids2objects(){
	    for(OverloadedFunction ovl : overloadedStore){
	        ovl.fids2objects(functionStore, constructorStore);
	    }
	}
	
	static byte EXEC_HEADER[] = new byte[] { 'R', 'V','M' };
	static byte[] EXEC_VERSION = new byte[] {1, 0, 0};
	static byte EXEC_COMPRESSION_NONE = 0;
	static byte EXEC_COMPRESSION_GZIP = 1;
	static byte EXEC_COMPRESSION_ZSTD = 2;
	
	
	@SuppressWarnings("resource")
    public void write(ISourceLocation rvmExecutable, int compressionLevel) throws IOException {
	    try(OutputStream out = getOutputStream(rvmExecutable)){
	        out.write(EXEC_HEADER);
	        out.write(EXEC_VERSION);
	        OutputStream cout = out;
	        
	        if (compressionLevel > 0 ) {
	            if (out instanceof ByteBufferOutputStream) {
	                cout = new DelayedZstdOutputStream((ByteBufferOutputStream)out, EXEC_COMPRESSION_ZSTD, compressionLevel);
	            }
	            else {
	                cout = new DelayedCompressionOutputStream(out, EXEC_COMPRESSION_ZSTD, o -> new ZstdOutputStream(o, compressionLevel));
	            }
	        }
	        else {
	            cout.write(EXEC_COMPRESSION_NONE);
	        }
	        
	        try(IRVMWireOutputStream iout = new RVMWireOutputStream(new BinaryWireOutputStream(cout, 50_000), vf, 50_000)){
	            write(iout);
	        }
	    }
	}

    private OutputStream getOutputStream(ISourceLocation loc) throws IOException {
        URIResolverRegistry registry = URIResolverRegistry.getInstance();
        if (registry.supportsWritableFileChannel(loc)) {
            FileChannel chan = registry.getWriteableFileChannel(loc, false);
            if (chan != null) {
                return new FileChannelDirectOutputStream(chan, 10);
            }
        }
        return registry.getOutputStream(loc, false);
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
        
//        out.writeFieldStringInt(CompilerIDs.Executable.RESOLVER, getResolver());
        
        // FUNCTION_MAP, CONSTRUCTOR_MAP should come before FUNCTION_STORE
        
        out.writeRepeatedNestedField(CompilerIDs.Executable.FUNCTION_STORE, functionStore.length);
        for(Function function : functionStore){
            function.write(out);
        }

        out.writeField(CompilerIDs.Executable.CONSTRUCTOR_STORE, constructorStore, WindowSizes.SMALL_WINDOW);

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
	
	public static RVMExecutable read(ISourceLocation rvmExecutable) throws IOException {
	    try(InputStream in = getInputStream(rvmExecutable)){
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
	                if (compression == EXEC_COMPRESSION_ZSTD && in instanceof ByteBufferInputStream && ((ByteBufferInputStream)in).getByteBuffer().isDirect()) {
	                    cin = new DirectZstdInputStream((ByteBufferInputStream) in);
	                }
	                else if (compression == EXEC_COMPRESSION_ZSTD){
	                    cin = new ZstdInputStream(in);
	                }
	                else if (compression == EXEC_COMPRESSION_GZIP) {
	                    cin = new GZIPInputStream(in);
	                }
	            }
	            try(IRVMWireInputStream win = new RVMWireInputStream(new BinaryWireInputStream(cin), ValueFactoryFactory.getValueFactory())){
	                return read(win, ValueFactoryFactory.getValueFactory());
	            }                      
	        } else {
	            throw new IOException("RVMExcutable cannot read legacy data format, please recompile");
	        }
	    }
	}

    private static InputStream getInputStream(ISourceLocation loc) throws IOException {
        URIResolverRegistry registry = URIResolverRegistry.getInstance();
        if (registry.supportsReadableFileChannel(loc)) {
            FileChannel chan = registry.getReadableFileChannel(loc);
            if (chan != null) {
                return new FileChannelDirectInputStream(chan);
            }
        }
        return registry.getInputStream(loc);
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
	    Type[] constructorStore = new Type[0];
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

                    for(int i = 0; i < n; i++){
                        Function function = Function.read(in, functionMap, constructorMap, resolver);
                        functionStore[i] = function;
                    }
                 
                    break;
                }
                
                case CompilerIDs.Executable.CONSTRUCTOR_STORE: {
                    constructorStore = in.readTypes();
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
	        constructorMap, constructorStore, overloadedStore, initializers, uid_module_init, uid_module_main, 
	        vf, false, Collections.emptyMap());
	    ex.setJvmByteCode(jvmByteCode);
	    ex.setFullyQualifiedDottedName(fullyQualifiedDottedName);

	    return ex;
	}
	
	 public static void main(String[] args) throws URISyntaxException  {
	     IValueFactory vf = ValueFactoryFactory.getValueFactory();
	     ISourceLocation rvmx = vf.sourceLocation("home", "", "/git/rascal/bootstrap/phase2/lang/rascal/grammar/ParserGenerator.rvmx");
	     //RVMExecutable.validating = false;
	     try {
	         RVMExecutable exec = read(rvmx);
	     }
	     catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	     }
	 }
}