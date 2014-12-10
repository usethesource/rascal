package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.uri.URIResolverRegistry;

/**
 * Provide all context information that is needed during the execution of a compiled Rascal program
 *
 */
public class RascalExecutionContext {

	private final URIResolverRegistry resolverRegistry;
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
	private RascalURIResolver rascalURIResolver;
	
	private String currentModuleName;
	
	RascalExecutionContext(IValueFactory vf, IMap symbol_definitions, boolean debug, boolean profile, boolean trackCalls, IEvaluatorContext ctx, ITestResultListener testResultListener){
		
		this.vf = vf;
		this.symbol_definitions = symbol_definitions;
		this.typeStore = new TypeStore();
		this.debug = debug;
		this.profile = profile;
		this.trackCalls = trackCalls;
		
		currentModuleName = "UNDEFINED";
		
		resolverRegistry = ctx.getResolverRegistry();
		rascalURIResolver = new RascalURIResolver(resolverRegistry);
		rascalURIResolver.addPathContributor(StandardLibraryContributor.getInstance());
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
	
	public TypeStore getTypeStore() { return typeStore; }
	
	boolean getDebug() { return debug; }
	
	boolean getProfile(){ return profile; }
	
	boolean getTrackCalls() { return trackCalls; }
	
	public URIResolverRegistry getResolverRegistry() { return resolverRegistry; }
	
	public RascalURIResolver getRascalResolver() { return rascalURIResolver; }
	
	IRascalMonitor getMonitor() {return monitor;}
	
	public PrintWriter getStdErr() { return stderr; }
	
	public PrintWriter getStdOut() { return stdout; }
	
	Configuration getConfiguration() { return config; }
	
	List<ClassLoader> getClassLoaders() { return classLoaders; }
	
	IEvaluatorContext getEvaluatorContext() { return ctx; }
	
	ITestResultListener getTestResultListener() { return testResultListener; }
	
	public String getCurrentModuleName(){ return currentModuleName; }
	
	void setCurrentModuleName(String moduleName) { currentModuleName = moduleName; }
	
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
