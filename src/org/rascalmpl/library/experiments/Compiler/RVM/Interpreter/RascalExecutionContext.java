package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
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
	private final boolean debug;
	private final boolean profile;
	
	RascalExecutionContext(IValueFactory vf, boolean debug, boolean profile, IEvaluatorContext ctx){
		
		this.vf = vf;
		this.debug = debug;
		this.profile = profile;
		
		resolverRegistry = ctx.getResolverRegistry();
		monitor = ctx.getEvaluator().getMonitor();
		stdout = ctx.getEvaluator().getStdOut();
		stderr = ctx.getEvaluator().getStdErr();
		config = ctx.getEvaluator().getConfiguration();
		classLoaders = ctx.getEvaluator().getClassLoaders();
		this.ctx = ctx;
	}

	IValueFactory getValueFactory(){ return vf; }
	boolean getDebug() { return debug; }
	boolean getProfile(){ return profile; }
	URIResolverRegistry getResolverRegistry() { return resolverRegistry; }
	IRascalMonitor getMonitor() {return monitor;}
	PrintWriter getStdErr() { return stderr; }
	PrintWriter getStdOut() { return stdout; }
	Configuration getConfiguration() { return config; }
	List<ClassLoader> getClassLoaders() { return classLoaders; }
	
	IEvaluatorContext getEvaluatorContext() { return ctx; }

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
