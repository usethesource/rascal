package org.rascalmpl.checker;

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.IURIOutputStreamResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class StaticChecker {
	private final Evaluator eval;
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	public static final String TYPECHECKER = "typecheckTree";
	private boolean checkerEnabled; 
	private boolean initialized;
	private boolean loaded;
	
	public StaticChecker(PrintWriter stderr, PrintWriter stdout) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***static-checker***", heap));
		eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		checkerEnabled = false;
		initialized = false;
		loaded = false;
	}
	
	private IValue eval(String cmd) {
		try {
			return eval.eval(cmd, URI.create("checker:///")).getValue();
		} catch (SyntaxError se) {
			throw new ImplementationError("syntax error in static checker modules", se);
		}
	}

	public synchronized void load() {
//		eval("import rascal::checker::Check;");
//		eval("import rascal::checker::Import;");
		eval("import rascal::scoping::ResolveNames;");
		loaded = true;
	}

	public void init() {
		initialized = true;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	private IConstructor resolveImports(IConstructor moduleParseTree) {
		ISet imports = (ISet) eval.call("importedModules", moduleParseTree);
		
		eval.getStdErr().println("imports: " + imports);
		
		IMapWriter mw = VF.mapWriter(TypeFactory.getInstance().stringType(), TypeFactory.getInstance().sourceLocationType());
		
		for (IValue i : imports) {
			URI uri = URI.create("rascal:///" + ((IString) i).getValue());
			mw.put(i, VF.sourceLocation(uri));
		}
		
		eval.getStdErr().println("locations: " + mw.done());
		
		return (IConstructor) eval.call("linkImportedModules", moduleParseTree, mw.done());
	}
	
	public synchronized IConstructor checkModule(IConstructor moduleParseTree) {
		IConstructor res = moduleParseTree;
//		res = resolveImports(res);
		if (checkerEnabled) res = (IConstructor) eval.call("resolveTree", res);
		return res;
	}

	public void disableChecker() {
		checkerEnabled = false;
	}

	public void enableChecker() {
		if (!loaded) load();
		checkerEnabled = true;
	}
	
	public boolean isCheckerEnabled() {
		return checkerEnabled;
	}

	public void addRascalSearchPath(URI uri) {
		eval.addRascalSearchPath(uri);
	}
	
	public void registerInputResolver(IURIInputStreamResolver resolver) {
		eval.getResolverRegistry().registerInput(resolver);
	}
	
	public void registerOutputResolver(IURIOutputStreamResolver resolver) {
		eval.getResolverRegistry().registerOutput(resolver);
	}

	public URIResolverRegistry getResolverRegistry() {
		return eval.getResolverRegistry();
	}

	public void addClassLoader(ClassLoader classLoader) {
		eval.addClassLoader(classLoader);
	}
}
