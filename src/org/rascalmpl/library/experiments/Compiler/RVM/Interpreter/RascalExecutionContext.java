package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.load.URIContributor;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.uri.CWDURIResolver;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.CompressedStreamResolver;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.HomeURIResolver;
import org.rascalmpl.uri.HttpURIResolver;
import org.rascalmpl.uri.HttpsURIResolver;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.TempURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

/**
 * Provide all context information that is needed during the execution of a compiled Rascal program
 *
 */
public class RascalExecutionContext {

	private final IRascalMonitor monitor;
	private final PrintWriter stderr;
	private final Configuration config;
	private final List<ClassLoader> classLoaders;
	private final PrintWriter stdout;
	private final IEvaluatorContext ctx;
	private final IValueFactory vf;
	private final TypeStore typeStore;
	private final boolean debug;
	private final boolean profile;
	private final boolean trackCalls;
	private final ITestResultListener testResultListener;
	private final IMap symbol_definitions;
	private RascalSearchPath rascalSearchPath;
	
	private String currentModuleName;
	//private ILocationCollector locationReporter;
	private RVM rvm;
	private boolean coverage;
	private final IMap moduleTags;
	
	RascalExecutionContext(IValueFactory vf, IMap moduleTags, IMap symbol_definitions, TypeStore typeStore, boolean debug, boolean profile, boolean trackCalls, boolean coverage, IEvaluatorContext ctx, ITestResultListener testResultListener){
		
		this.vf = vf;
		this.moduleTags = moduleTags;
		this.symbol_definitions = symbol_definitions;
		this.typeStore = typeStore;
		this.debug = debug;
		this.profile = profile;
		this.coverage = coverage;
		this.trackCalls = trackCalls;
		
		currentModuleName = "UNDEFINED";
		
		// TODO: Search path initialization: compare with Evaluator!
		rascalSearchPath = new RascalSearchPath();
		URIResolverRegistry resolverRegistry = rascalSearchPath.getRegistry();
		rascalSearchPath.addPathContributor(StandardLibraryContributor.getInstance());
		
		// register some schemes
		FileURIResolver files = new FileURIResolver();
		resolverRegistry.registerInputOutput(files);

		HttpURIResolver http = new HttpURIResolver();
		resolverRegistry.registerInput(http);

		//added
		HttpsURIResolver https = new HttpsURIResolver();
		resolverRegistry.registerInput(https);

		CWDURIResolver cwd = new CWDURIResolver();
		resolverRegistry.registerLogical(cwd);

		ClassResourceInput library = new ClassResourceInput("std", getClass(), "/org/rascalmpl/library");
		resolverRegistry.registerInput(library);

		ClassResourceInput testdata = new ClassResourceInput("testdata", getClass(), "/org/rascalmpl/test/data");
		resolverRegistry.registerInput(testdata);

		ClassResourceInput benchmarkdata = new ClassResourceInput("benchmarks", getClass(), "/org/rascalmpl/benchmark");
		resolverRegistry.registerInput(benchmarkdata);

		resolverRegistry.registerInput(new JarURIResolver());

		resolverRegistry.registerLogical(new HomeURIResolver());
		resolverRegistry.registerInputOutput(new TempURIResolver());

		resolverRegistry.registerInputOutput(new CompressedStreamResolver(resolverRegistry));
		
		FileURIResolver testModuleResolver = new TempURIResolver() {		// Code borrowed from Evaluator
		    @Override
		    public String scheme() {
		      return "test-modules";
		    }
		    
		    @Override
		    protected String getPath(ISourceLocation uri) {
		      String path = uri.getPath();
		      path = path.startsWith("/") ? "/test-modules" + path : "/test-modules/" + path;
		      return System.getProperty("java.io.tmpdir") + path;
		    }
		  };
		rascalSearchPath.getRegistry().registerInputOutput(testModuleResolver);
		rascalSearchPath.addPathContributor(new URIContributor(URIUtil.rootLocation("test-modules")));
		
		monitor = ctx.getEvaluator().getMonitor();
		stdout = ctx.getEvaluator().getStdOut();
		stderr = ctx.getEvaluator().getStdErr();
		config = ctx.getEvaluator().getConfiguration();
		classLoaders = ctx.getEvaluator().getClassLoaders();
		this.testResultListener = testResultListener;
		this.ctx = ctx;
	}

	IValueFactory getValueFactory(){ return vf; }
	
	public IMap getSymbolDefinitions() { return symbol_definitions; }
	
	public TypeStore getTypeStore() { 
		return typeStore; 
	}
	
	boolean getDebug() { return debug; }
	
	boolean getProfile(){ return profile; }
	
	boolean getCoverage(){ return coverage; }
	
	boolean getTrackCalls() { return trackCalls; }
	
	public RVM getRVM(){ return rvm; }
	
	void setRVM(RVM rvm){ this.rvm = rvm; }
	
	public RascalSearchPath getRascalSearchPath() { return rascalSearchPath; }
	
	IRascalMonitor getMonitor() {return monitor;}
	
	public PrintWriter getStdErr() { return stderr; }
	
	public PrintWriter getStdOut() { return stdout; }
	
	Configuration getConfiguration() { return config; }
	
	List<ClassLoader> getClassLoaders() { return classLoaders; }
	
	IEvaluatorContext getEvaluatorContext() { return ctx; }
	
	ITestResultListener getTestResultListener() { return testResultListener; }
	
	public String getCurrentModuleName(){ return currentModuleName; }
	
	void setCurrentModuleName(String moduleName) { currentModuleName = moduleName; }
	
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
		if (monitor != null)
			monitor.startJob(name, workShare, totalWork);
	}
	
	public void startJob(String name, int totalWork) {
		if (monitor != null)
			monitor.startJob(name, totalWork);
	}
	
	public void startJob(String name) {
		if (monitor != null)
			monitor.startJob(name);
	}
		
	public void todo(int work) {
		if (monitor != null)
			monitor.todo(work);
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
		String scheme = loc.getURI().getScheme();
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
}
