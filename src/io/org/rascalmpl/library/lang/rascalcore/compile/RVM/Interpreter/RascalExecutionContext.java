package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Stack;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.CallTraceObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.CoverageFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.DebugFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.NullFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.ProfileFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.RVMTrackingObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

/**
 * Provides all context information that is needed during the execution of a compiled Rascal program
 * and contains:
 * - PathConfig
 * - class loaders
 * - I/O streams
 * - execution flags
 * - state variables and caches needed by RascalPrimitives
 */
public class RascalExecutionContext implements IRascalMonitor {
	private IDEServices ideServices;
	private final PrintWriter stderr;
	private final Configuration config;
	private final PrintWriter stdout;
	
	private final IValueFactory vf;
	
	private final boolean debug;
	private final boolean debugRVM;
	private final boolean testsuite;
	private final boolean profile;
	private final boolean trace;
	private boolean coverage;
    private boolean jvm;
    private boolean verbose;
    
	private IFrameObserver frameObserver;
	private final IMap symbol_definitions;
	
	private String currentModuleName;
	private RVMCore rvm;
	
	private final IMap moduleTags;
	private Map<IValue, IValue> moduleVariables;
	
	private final TypeReifier reifier;
	
	// Caches
	private static Cache<String, Function> companionDefaultFunctionCache;
	private static Cache<String, Function> companionFieldDefaultFunctionCache;
//	private Cache<String,  Class<IGTD<IConstructor, ITree, ISourceLocation>>> parserCache;
	private static Cache<String, IValue> parsedModuleCache;
	private static Cache<Type, IConstructor> typeToSymbolCache;
	private static Cache<IValue, Type> symbolToTypeCache;
	private static Cache<IString, DescendantDescriptor> descendantDescriptorCache;
	private static Cache<Type, Type> sharedTypeConstantCache;
	
	private static final String PATH_TO_LINKED_PARSERGENERATOR = "lang/rascal/grammar/ParserGenerator.rvmx";
    private static final String PATH_TO_LINKED_KERNEL = "lang/rascal/boot/Kernel.rvmx";
    private static final String PATH_TO_LINKED_RASCALEXTRACTION = "experiments/Compiler/RascalExtraction/RascalExtraction.rvmx";
    private static final String PATH_TO_LINKED_QUESTIONCOMPILER = "experiments/tutor3/QuestionCompiler.rvmx";
    private static final String PATH_TO_LINKED_WEBSERVER = "util/Webserver.rvmx";
    
	static {
		createCaches(true);
	}
	
	// State for RascalPrimitive
	
	private final ParsingTools parsingTools; 
	Stack<String> indentStack = new Stack<String>();
	
	StringBuilder templateBuilder = null;
	private final Stack<StringBuilder> templateBuilderStack = new Stack<StringBuilder>();
	private final ISourceLocation bootDir;
    private final PathConfig pcfg;
	
	public RascalExecutionContext(
			PathConfig pcfg, 
			PrintWriter stdout,
			PrintWriter stderr, 
			IMap moduleTags, 
			IMap symbol_definitions, 
			IFrameObserver frameObserver, 
			IDEServices ideServices,
			boolean coverage, 
			boolean debug, 
			boolean debugRVM, 
			boolean jvm, 
			boolean profile, 
			boolean testsuite, 
			boolean trace, 
			boolean verbose
	){
		
	  this.vf = pcfg.getValueFactory();
	  this.pcfg = pcfg;
	  this.bootDir = pcfg.getBoot();
	  if(bootDir != null && !(bootDir.getScheme().equals("boot") ||  
	                          //bootDir.getScheme().equals("compressed+boot") || 
	                          URIResolverRegistry.getInstance().isDirectory(bootDir))){
	    throw new RuntimeException("bootDir should be a directory, given " + bootDir);
	  }
	  
	  this.moduleTags = moduleTags;
	  this.symbol_definitions = symbol_definitions == null ? vf.mapWriter().done() : symbol_definitions;
	  this.debug = debug;
	  this.debugRVM = debugRVM;
	  this.testsuite = testsuite;

	  this.profile = profile;
	  this.coverage = coverage;
	  this.jvm = jvm;
	  this.trace = trace;
	  this.verbose = verbose;

	  currentModuleName = "UNDEFINED";

	  reifier = new TypeReifier(vf);

	  this.ideServices = ideServices == null ? new BasicIDEServices(stderr) : ideServices;
	  this.stdout = stdout;
	  this.stderr = stderr;
	  config = new Configuration();
	  config.setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
	  
	  if(frameObserver == null){
	    if(profile){
	      setFrameObserver(new ProfileFrameObserver(this));
	    } else if(coverage){
	      setFrameObserver(new CoverageFrameObserver(this));
	    } else if(debug){
	      setFrameObserver(new DebugFrameObserver(this));
	    } else if(trace){
	      setFrameObserver(new CallTraceObserver(this));
	    } else if(debugRVM){
	      setFrameObserver(new RVMTrackingObserver(this));
	    } else {
	      setFrameObserver(NullFrameObserver.getInstance());
	    }
	  } else {
	    setFrameObserver(frameObserver);
	  }

	  parsingTools = new ParsingTools(vf);
	}
	
	private String javaCompilerPathAsString(IList javaCompilerPath) {
        StringBuilder b = new StringBuilder();
        
        for (IValue elem : javaCompilerPath) {
            ISourceLocation loc = (ISourceLocation) elem;
            
            if (b.length() != 0) {
                b.append(File.pathSeparatorChar);
            }
           
            assert loc.getScheme().equals("file");
            String path = loc.getPath();
            if (path.startsWith("/") && path.contains(":\\")) {
                // a windows path should drop the leading /
                path = path.substring(1);
            }
            b.append(path);
        }
        
        return b.toString();
    }

    public static ISourceLocation getLocation(ISourceLocation givenBootDir, String desiredPath) {
	  IValueFactory vfac = ValueFactoryFactory.getValueFactory();
	  try {
	    if(givenBootDir == null){
	      return vfac.sourceLocation("boot"/*"compressed+boot"*/, "", desiredPath);
	    }

	    String scheme = givenBootDir.getScheme();
//	    if(!scheme.startsWith("compressed+")){
//	      scheme = "compressed+" + scheme;
//	    }
	   
	    String basePath = givenBootDir.getPath();
	    if(!basePath.endsWith("/")){
	      basePath += "/";
	    }
	    return vfac.sourceLocation(scheme, givenBootDir.getAuthority(), basePath + desiredPath);
	  } catch (URISyntaxException e) {
	    throw new RuntimeException("Cannot create location for " + desiredPath);
	  }
	}
	
	public static ISourceLocation getKernel(ISourceLocation givenBootDir) {
      return getLocation(givenBootDir, PATH_TO_LINKED_KERNEL);
    }
	
	public static ISourceLocation getParserGenerator(ISourceLocation givenBootDir) {
      return getLocation(givenBootDir, PATH_TO_LINKED_PARSERGENERATOR);
    }
	
	public static ISourceLocation getQuestionCompiler(ISourceLocation givenBootDir) {
      return getLocation(givenBootDir, PATH_TO_LINKED_QUESTIONCOMPILER);
    }
	
	public static ISourceLocation getWebserver(ISourceLocation givenBootDir) {
	    return getLocation(givenBootDir, PATH_TO_LINKED_WEBSERVER);
	}

	public ISourceLocation getBoot() {
	  return  getLocation(bootDir, "");
	}
	
	public PathConfig getPathConfig(){
	    return pcfg;
	}
	
	public IMap getModuleTags(){
	    return moduleTags;
	}
	
	public  IMap getModuleTagsCurrentModule(){
	    IString mname = vf.string(currentModuleName);
	    if(moduleTags == null){
	        return vf.mapWriter().done();
	    }
	    IValue v = moduleTags.get(mname);
	    if(v == null){
	        return vf.mapWriter().done();
	    }
	    IMap m = (IMap) v;
	    return m == null ? vf.mapWriter().done() : (IMap) m.get(mname);
	}
	
	public ISourceLocation getKernel() {
	  return getLocation(bootDir, PATH_TO_LINKED_KERNEL);
	}
	
	public ISourceLocation getParserGenerator(){
	  return getLocation(bootDir, PATH_TO_LINKED_PARSERGENERATOR);
	}
	
	public ISourceLocation getRascalExtraction(){
	  return getLocation(bootDir, PATH_TO_LINKED_RASCALEXTRACTION);
	}
	
	public ISourceLocation getQuestionCompiler() {
      return getLocation(bootDir, PATH_TO_LINKED_QUESTIONCOMPILER);
    }
    
	// Cache related methods
	
	private static void createCaches(boolean enabled){
		sharedTypeConstantCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
				//.maximumSize(enabled ? 10000 : 0)
				.build();
		typeToSymbolCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
				//.maximumSize(enabled ? 1000 : 0)
				.build();
		symbolToTypeCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
				//.maximumSize(enabled ? 10000 : 0)
				.build();
		descendantDescriptorCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
				//.maximumSize(enabled ? 500000 : 0)
				.build();
		companionDefaultFunctionCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
//				.maximumSize(enabled ? 300 : 0)
				.build();
		companionFieldDefaultFunctionCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
//				.maximumSize(enabled ? 300 : 0)
				.build();
//		parserCache = Caffeine.newBuilder()
////				.weakKeys()
//			    .weakValues()
////			    .recordStats()
//				.maximumSize(enabled ? 30 : 0)
//				.build();
		
		parsedModuleCache = Caffeine.newBuilder()
				.weakKeys()
			    .weakValues()
			    //.recordStats()
//				.maximumSize(enabled ? 100 : 0)
				.build();
	}
	
	public void clearCaches(){
		createCaches(true);
	}
	
	public void noCaches(){
		createCaches(false);
	}
	
	public void printCacheStat(String name, Cache<?,?> cache){
		CacheStats s = cache.stats();
		System.out.printf(
				"%35s: %6d (EV) %6d (HC) %5.1f (HR) %6d (LC) %6d (LFC) %5.1f (LFR) %6d (LSC) %6d (MC) %5.1f (MR) %6d (RC) %10.1f (ALP) \n",
				//"%35s: %12.0f (ALP) %f (HR) %d (RC)\n",
				name,
				
				s.evictionCount(),
				s.hitCount(),
				s.hitRate(),
				s.loadCount(),
				s.loadFailureCount(),
				s.loadFailureRate(),
				s.loadSuccessCount(),
				s.missCount(),
				s.missRate(),
				s.requestCount(),
				s.averageLoadPenalty());	
	}
	
	public void printCacheStats(){
		printCacheStat("sharedTypeConstantCache", sharedTypeConstantCache);
		printCacheStat("typeToSymbolCache", typeToSymbolCache);
		printCacheStat("symbolToTypeCache", symbolToTypeCache);
		printCacheStat("descendantDescriptorCache", descendantDescriptorCache);
		printCacheStat("companionDefaultFunctionCache", companionDefaultFunctionCache);
		printCacheStat("companionFieldDefaultFunctionCache", companionFieldDefaultFunctionCache);
//		printCacheStat("parserCache", parserCache);
		printCacheStat("parsedModuleCache", parsedModuleCache);
		System.out.println("");
	}

	public ParsingTools getParsingTools(){
		return parsingTools;
	}
	
	public Cache<String, IValue> getParsedModuleCache() {
		return parsedModuleCache;
	}
	
	public IConstructor typeToSymbol(final Type t){
		return typeToSymbolCache.get(t, k -> RascalPrimitive.$type2symbol(t));
	}
	
	 public Type symbolToType(IConstructor v, final IMap definitions) {
	     return symbolToTypeCache.get(v, k -> reifier.symbolToType(v, definitions));
	 }
	
	Cache<IString, DescendantDescriptor> getDescendantDescriptorCache() {
		return descendantDescriptorCache;
	}
	
	public Function getCompanionDefaultsFunction(String name, Type ftype){
		String key = name + ftype;
		
		//return  rvm.getCompanionDefaultsFunction(name, ftype);
		
		Function result = companionDefaultFunctionCache.get(key, k -> rvm.getCompanionDefaultsFunction(name, ftype));
		//System.err.println("RascalExecutionContext.getCompanionDefaultsFunction: " + key + " => " + result.name);
		return result;
	}
	
	public Function getCompanionFieldDefaultFunction(Type adtType, String fieldName){
		//return rvm.getCompanionFieldDefaultFunction(adtType, fieldName);
		
		String key = adtType.toString() + fieldName;
		Function result = companionFieldDefaultFunctionCache.get(key, k -> rvm.getCompanionFieldDefaultFunction(adtType, fieldName));
		return result;
	}
	
	public static Type shareTypeConstant(Type t){
		return sharedTypeConstantCache.get(t, k -> k);
	}
	
	public IValueFactory getValueFactory(){ return vf; }
	
	public IMap getSymbolDefinitions() { return symbol_definitions; }
	
	public TypeStore getTypeStore() { 
	    if(rvm != null){
	        return rvm.getTypeStore();
	    }
	    return new TypeReifier(vf).buildTypeStore(symbol_definitions);
	}
	
	boolean getDebug() { return debug; }
	
	IFrameObserver getFrameObserver(){
		return frameObserver;
	}
	
	void setFrameObserver(IFrameObserver observer){
		frameObserver = observer;
	}
	
	boolean getDebugRVM() { return debugRVM; }
	
	boolean getTestSuite() { return testsuite; }
	
	boolean getProfile(){ return profile; }
	
	boolean getCoverage(){ return coverage; }
	
	boolean getJVM() { return jvm; }
	
	boolean getTrace() { return trace; }
	
	boolean getVerbose() { return verbose; }
	
	public RVMCore getRVM(){ return rvm; }
	
	protected void setRVM(RVMCore rvmCore){ 
		this.rvm = rvmCore;
		if(frameObserver != null){
			frameObserver.setRVM(rvmCore);
		}
	}
	
	public Map<IValue, IValue> getModuleVariables(){
		return moduleVariables;
	}
	
	void setModuleVariables(Map<IValue, IValue> moduleVariables){
		this.moduleVariables = moduleVariables;
	}
	
	IRascalMonitor getMonitor() {
	    return ideServices;
	}
	
	IDEServices getIDEServices(){
	  return ideServices;
	}
	
	public PrintWriter getStdErr() { return stderr; }
	
	public PrintWriter getStdOut() { return stdout; }
	
	Configuration getConfiguration() { return config; }
	
	public String getFullModuleName(){ return currentModuleName; }
	
	public String getFullModuleNameAsPath() { return currentModuleName.replaceAll("::",  "/") + ".rsc"; }
	
	public void setFullModuleName(String moduleName) { currentModuleName = moduleName; }
	
	public Stack<String> getIndentStack() { return indentStack; }
	
	StringBuilder getTemplateBuilder() { return templateBuilder; }
	
	void setTemplateBuilder(StringBuilder sb) { templateBuilder = sb; }
	
	Stack<StringBuilder> getTemplateBuilderStack() { return  templateBuilderStack; }
	
	boolean bootstrapParser(String moduleName){
		if(moduleTags != null){
			IMap tags = (IMap) moduleTags.get(vf.string(moduleName));
			if(tags != null)
				return tags.get(vf.string("bootstrapParser")) != null;
		}
		return false;
	}
	
	public int endJob(boolean succeeded) {
		return ideServices.endJob(succeeded);
	}
	
	public void event(int inc) {
		 ideServices.event(inc);
	}
	
	public void event(String name, int inc) {
		 ideServices.event(name, inc);
	}

	public void event(String name) {
	  ideServices.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
	  ideServices.startJob(name, workShare, totalWork);
	}
	
	public void startJob(String name, int totalWork) {
	  ideServices.startJob(name, totalWork);
	}
	
	public void startJob(String name) {
	  ideServices.startJob(name);
	}
		
	public void todo(int work) {
	  ideServices.todo(work);
	}
	
	@Override
	public boolean isCanceled() {
	  return ideServices.isCanceled();
	}

	@Override
	public void warning(String message, ISourceLocation src) {
	  ideServices.warning(message,  src);;
	}
}
