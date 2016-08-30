package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.DefaultTestResultListener;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.load.URIContributor;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.CallTraceObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.CoverageFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.DebugFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.NullFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.ProfileFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.RVMTrackingObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

/**
 * Provides all context information that is needed during the execution of a compiled Rascal program
 * and contains:
 * - I/O streams
 * - class loaders
 * - RascalSearchPath
 * - execution flags
 * - state variables need by RascalPrimitives
 *
 */
public class RascalExecutionContext implements IRascalMonitor {

	private IRascalMonitor monitor;
	private final PrintWriter stderr;
	private final Configuration config;
	private final List<ClassLoader> classLoaders;
	private final PrintWriter stdout;
	
	private final IValueFactory vf;
	private final TypeStore typeStore;
	private final boolean debug;
	private final boolean debugRVM;
	private final boolean testsuite;
	private final boolean profile;
	private final boolean trace;
	private final ITestResultListener testResultListener;
	private IFrameObserver frameObserver;
	private final IMap symbol_definitions;
	private RascalSearchPath rascalSearchPath;
	
	private String currentModuleName;
	private RVMCore rvm;
	private boolean coverage;
	private boolean jvm;
    private boolean verbose;
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
	
	private static final String PATH_TO_LINKED_PARSERGENERATOR = "lang/rascal/grammar/ParserGenerator.rvm.ser.gz";
    private static final String PATH_TO_LINKED_KERNEL = "lang/rascal/boot/Kernel.rvm.ser.gz";
	
	static {
		createCaches(true);
	}
	
	// State for RascalPrimitive
	
	private final ParsingTools parsingTools; 
	Stack<String> indentStack = new Stack<String>();
	
	StringBuilder templateBuilder = null;
	private final Stack<StringBuilder> templateBuilderStack = new Stack<StringBuilder>();
	private IListWriter test_results;
	private final ISourceLocation bootDir;
	
	public RascalExecutionContext(
			IValueFactory vf, 
			ISourceLocation bootDir,
			PrintWriter stdout, 
			PrintWriter stderr, 
			IMap moduleTags, 
			IMap symbol_definitions, 
			TypeStore typeStore,
			boolean debug, 
			boolean debugRVM, 
			boolean testsuite, 
			boolean profile, 
			boolean trace, 
			boolean coverage, 
			boolean jvm, 
			boolean verbose,
			ITestResultListener testResultListener, 
			IFrameObserver frameObserver,
			RascalSearchPath rascalSearchPath
	){
		
	  this.vf = vf;
	  this.bootDir = bootDir;
	  if(bootDir != null && !URIResolverRegistry.getInstance().isDirectory(bootDir)){
	    throw new RuntimeException("bootDir should be a directory, given " + bootDir);
	  }
	  
	  this.moduleTags = moduleTags;
	  this.symbol_definitions = symbol_definitions;
	  this.typeStore = typeStore == null ? RascalValueFactory.getStore() /*new TypeStore()*/ : typeStore;
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

	  if(rascalSearchPath == null){
	    this.rascalSearchPath = new RascalSearchPath();
	    addRascalSearchPath(URIUtil.rootLocation("test-modules"));
	    addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
	  } else {
	    this.rascalSearchPath = rascalSearchPath;
	  }

	  monitor = new ConsoleRascalMonitor(); //ctx.getEvaluator().getMonitor();
	  this.stdout = stdout;
	  this.stderr = stderr;
	  config = new Configuration();
	  this.classLoaders = new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader()));
	  this.testResultListener = (testResultListener == null) ? (ITestResultListener) new DefaultTestResultListener(stderr, verbose)
	      : testResultListener;

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
	
	public static ISourceLocation getLocation(ISourceLocation givenBootDir, String desiredPath) {
	  IValueFactory vfac = ValueFactoryFactory.getValueFactory();
	  try {
	    if(givenBootDir == null){
	      return vfac.sourceLocation("compressed+boot", "", desiredPath);
	    }

	    String scheme = givenBootDir.getScheme();
	    if(!scheme.startsWith("compressed+")){
	      scheme = "compressed+" + scheme;
	    }
	   
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

	public ISourceLocation getBoot() {
	  return  getLocation(bootDir, "");
	}
	
	public ISourceLocation getKernel() {
	  return getLocation(bootDir, PATH_TO_LINKED_KERNEL);
	}
	
	public ISourceLocation getParserGenerator(){
	  return getLocation(bootDir, PATH_TO_LINKED_PARSERGENERATOR);
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
	
//	public Cache<String,  Class<IGTD<IConstructor, ITree, ISourceLocation>>> getParserCache(){
//		return parserCache;
//	}
	
	public Cache<String, IValue> getParsedModuleCache() {
		return parsedModuleCache;
	}
	
	public static IConstructor typeToSymbol(final Type t){
		return typeToSymbolCache.get(t, k -> RascalPrimitive.$type2symbol(t));
	}
	
	public Type symbolToType(final IConstructor sym, IMap definitions){
		IValue[] key = new IValue[] { sym, definitions};
		return symbolToTypeCache.get(sym, k -> { return reifier.symbolToType(sym, definitions); });
	}
	
	public Type valueToType(final IConstructor sym){
		if (sym.getType() instanceof ReifiedType){
			IMap definitions = (IMap) sym.get("definitions");
			reifier.declareAbstractDataTypes(definitions, getTypeStore());
			return symbolToType((IConstructor) sym.get("symbol"), definitions);
		}
		throw new IllegalArgumentException(sym + " is not a reified type");
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
		return typeStore; 
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
	
	public void addClassLoader(ClassLoader loader) {
		// later loaders have precedence
		classLoaders.add(0, loader);
	}
	
	List<ClassLoader> getClassLoaders() { return classLoaders; }
	
	IRascalMonitor getMonitor() {return monitor;}
	
	void setMonitor(IRascalMonitor monitor) {
		this.monitor = monitor;
	}
	
	public PrintWriter getStdErr() { return stderr; }
	
	public PrintWriter getStdOut() { return stdout; }
	
	Configuration getConfiguration() { return config; }
	
	ITestResultListener getTestResultListener() { return testResultListener; }
	
	public String getCurrentModuleName(){ return currentModuleName; }
	
	public void setCurrentModuleName(String moduleName) { currentModuleName = moduleName; }
	
	public Stack<String> getIndentStack() { return indentStack; }
	
	StringBuilder getTemplateBuilder() { return templateBuilder; }
	
	void setTemplateBuilder(StringBuilder sb) { templateBuilder = sb; }
	
	Stack<StringBuilder> getTemplateBuilderStack() { return  templateBuilderStack; }
	
	IListWriter getTestResults() { return test_results; }
	
	void setTestResults(IListWriter writer) { test_results = writer; }
	
	boolean bootstrapParser(String moduleName){
		if(moduleTags != null){
			IMap tags = (IMap) moduleTags.get(vf.string(moduleName));
			if(tags != null)
				return tags.get(vf.string("bootstrapParser")) != null;
		}
		return false;
	}
	
	public int endJob(boolean succeeded) {
		if (monitor != null)
			return monitor.endJob(succeeded);
		return 0;
	}
	
	public void event(int inc) {
		if (monitor != null)
			monitor.event(inc);
	}
	
	public void event(String name, int inc) {
		if (monitor != null)
			monitor.event(name, inc);
	}

	public void event(String name) {
		if (monitor != null)
			monitor.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
		if (monitor != null){
			monitor.startJob(name, workShare, totalWork);
		} else {
			stdout.println(name);
			stdout.flush();
		}
	}
	
	public void startJob(String name, int totalWork) {
		if (monitor != null)
			monitor.startJob(name, totalWork);
	}
	
	public void startJob(String name) {
		if (monitor != null){
			monitor.startJob(name);
		} else {
			stdout.println(name);
			stdout.flush();
		}
	}
		
	public void todo(int work) {
		if (monitor != null)
			monitor.todo(work);
	}
	
	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		stdout.println("Warning: " + message);
		stdout.flush();
	}

	public RascalSearchPath getRascalSearchPath() { 
		return rascalSearchPath; 
	}
	
	private void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
		rascalSearchPath.addPathContributor(contrib);
	}
	
	private void addRascalSearchPath(final ISourceLocation uri) {
		rascalSearchPath.addPathContributor(new URIContributor(uri));
	}
	/**
	 * Source location resolvers map user defined schemes to primitive schemes
	 */
	private final HashMap<String, ICallableValue> sourceResolvers = new HashMap<String, ICallableValue>();
	
	/**
	 * Register a source resolver for a specific scheme. Will overwrite the previously
	 * registered source resolver for this scheme.
	 * 
	 * @param scheme   intended be a scheme name without + or :
	 * @param function a Rascal function of type `loc (loc)`
	 */
	public void registerSourceResolver(String scheme, ICallableValue function) {
		sourceResolvers.put(scheme, function);
	}
	
	public ISourceLocation resolveSourceLocation(ISourceLocation loc) {
		String scheme = loc.getScheme();
		int pos;
		
		ICallableValue resolver = sourceResolvers.get(scheme);
		if (resolver == null) {
			for (char sep : new char[] {'+',':'}) {
				pos = scheme.indexOf(sep);
				if (pos != -1) {
					scheme = scheme.substring(0, pos);
				}
			}

			resolver = sourceResolvers.get(scheme);
			if (resolver == null) {
				return loc;
			}
		}
		
		Type[] argTypes = new Type[] { TypeFactory.getInstance().sourceLocationType() };
		IValue[] argValues = new IValue[] { loc };
		
		return (ISourceLocation) resolver.call(argTypes, argValues, null).getValue();
	}
	
//	void registerCommonSchemes(){
//		addRascalSearchPath(URIUtil.rootLocation("test-modules"));
//		addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
//		addRascalSearchPath(URIUtil.rootLocation("courses"));
//	}
}
