package org.rascalmpl.interpreter;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.Expression.Ambiguity;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.callbacks.IConstructorDeclared;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.ModuleLoadError;
import org.rascalmpl.interpreter.staticErrors.ModuleNameMismatchError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;
import org.rascalmpl.interpreter.staticErrors.UnguardedInsertError;
import org.rascalmpl.interpreter.staticErrors.UnguardedReturnError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.strategy.StrategyContextStack;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.library.lang.rascal.syntax.MetaRascalRascal;
import org.rascalmpl.library.lang.rascal.syntax.ObjectRascalRascal;
import org.rascalmpl.library.lang.rascal.syntax.RascalRascal;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.IParserInfo;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.RascalActionExecutor;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.uri.CWDURIResolver;
import org.rascalmpl.uri.ClassResourceInputOutput;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.HomeURIResolver;
import org.rascalmpl.uri.HttpURIResolver;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;

public class Evaluator extends NullASTVisitor<Result<IValue>> implements IEvaluator<Result<IValue>> {
	private IValueFactory vf;
	private static final TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	protected Environment currentEnvt;
	private StrategyContextStack strategyContextStack;

	private final GlobalEnvironment heap;
	private boolean interrupt = false;

	private final JavaBridge javaBridge;

	private AbstractAST currentAST; // used in runtime errormessages

	private static boolean doProfiling = false;
	private Profiler profiler;

	private final TypeDeclarationEvaluator typeDeclarator;
	protected IEvaluator<IMatchingResult> patternEvaluator;

	private final List<ClassLoader> classLoaders;
	private final ModuleEnvironment rootScope;
	private boolean concreteListsShouldBeSpliced;

	private final PrintWriter stderr;
	private final PrintWriter stdout;

	private ITestResultListener testReporter;
	/**
	 * To avoid null pointer exceptions, avoid passing this directly to other classes, use
	 * the result of getMonitor() instead. 
	 */
	private IRascalMonitor monitor;
	

	private Stack<Accumulator> accumulators = new Stack<Accumulator>();
	private Stack<Integer> indentStack = new Stack<Integer>();
	private final RascalURIResolver rascalPathResolver;
	private static final ASTBuilder builder = new ASTBuilder();

	private final URIResolverRegistry resolverRegistry;

	private HashSet<IConstructorDeclared> constructorDeclaredListeners;

	public Evaluator(IValueFactory f, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap) {
		this(f, stderr, stdout, scope, heap, new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new RascalURIResolver(new URIResolverRegistry()));
	}

	public Evaluator(IValueFactory vf, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap, List<ClassLoader> classLoaders, RascalURIResolver rascalPathResolver) {
		super();
		
		this.vf = vf;
		this.patternEvaluator = new PatternEvaluator(this);
		this.strategyContextStack = new StrategyContextStack();
		this.heap = heap;
		this.typeDeclarator = new TypeDeclarationEvaluator(this);
		this.currentEnvt = scope;
		this.rootScope = scope;
		heap.addModule(scope);
		this.classLoaders = classLoaders;
		this.javaBridge = new JavaBridge(classLoaders, vf);
		this.rascalPathResolver = rascalPathResolver;
		this.resolverRegistry = rascalPathResolver.getRegistry();
		this.stderr = stderr;
		this.stdout = stdout;
		this.constructorDeclaredListeners = new HashSet<IConstructorDeclared>();

		updateProperties();

		if (stderr == null) {
			throw new NullPointerException();
		}
		if (stdout == null) {
			throw new NullPointerException();
		}

		rascalPathResolver.addPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(List<URI> l) {
				l.add(java.net.URI.create("cwd:///"));
				l.add(java.net.URI.create("std:///"));
				l.add(java.net.URI.create("testdata:///"));

				String property = java.lang.System.getProperty("rascal.path");

				if (property != null) {
					for (String path : property.split(":")) {
						l.add(new File(path).toURI());
					}
				}
			}

			@Override
			public String toString() {
				return "[current wd and stdlib]";
			}
		});

		// register some schemes
		FileURIResolver files = new FileURIResolver();
		resolverRegistry.registerInputOutput(files);

		HttpURIResolver http = new HttpURIResolver();
		resolverRegistry.registerInput(http);

		CWDURIResolver cwd = new CWDURIResolver();
		resolverRegistry.registerInputOutput(cwd);

		ClassResourceInputOutput library = new ClassResourceInputOutput(resolverRegistry, "std", getClass(), "/org/rascalmpl/library");
		resolverRegistry.registerInputOutput(library);

		ClassResourceInputOutput testdata = new ClassResourceInputOutput(resolverRegistry, "testdata", getClass(), "/org/rascalmpl/test/data");
		resolverRegistry.registerInput(testdata);

		resolverRegistry.registerInput(new JarURIResolver(getClass()));

		resolverRegistry.registerInputOutput(rascalPathResolver);

		HomeURIResolver home = new HomeURIResolver();
		resolverRegistry.registerInputOutput(home);
	}

	public synchronized IRascalMonitor setMonitor(IRascalMonitor monitor) {
		if (monitor == this) {
			return monitor;
		}
		
		interrupt = false;
		IRascalMonitor old = monitor;
		this.monitor = monitor;
		return old;
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
	
	public void registerConstructorDeclaredListener(IConstructorDeclared iml) {
		constructorDeclaredListeners.add(iml);
	}
	
	public void notifyConstructorDeclaredListeners() {
		for (IConstructorDeclared iml : constructorDeclaredListeners) iml.handleConstructorDeclaredEvent();
		constructorDeclaredListeners.clear();
	}
	
	public List<ClassLoader> getClassLoaders() {
		return Collections.unmodifiableList(classLoaders);
	}

	public ModuleEnvironment __getRootScope() {
		return rootScope;
	}

	public PrintWriter getStdOut() {
		return stdout;
	}

	public TypeDeclarationEvaluator __getTypeDeclarator() {
		return typeDeclarator;
	}

	public GlobalEnvironment __getHeap() {
		return heap;
	}

	public boolean __getConcreteListsShouldBeSpliced() {
		return concreteListsShouldBeSpliced;
	}

	public void __setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	public boolean __getInterrupt() {
		return interrupt;
	}

	public Stack<Accumulator> __getAccumulators() {
		return accumulators;
	}

	public IEvaluator<IMatchingResult> __getPatternEvaluator() {
		return patternEvaluator;
	}

	public IValueFactory __getVf() {
		return vf;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	public JavaBridge __getJavaBridge() {
		return javaBridge;
	}

	public void interrupt() {
		__setInterrupt(true);
	}

	public boolean isInterrupted() {
		return interrupt;
	}

	public PrintWriter getStdErr() {
		return stderr;
	}

	public void setTestResultListener(ITestResultListener l) {
		testReporter = l;
	}

	public JavaBridge getJavaBridge() {
		return javaBridge;
	}

	public URIResolverRegistry getResolverRegistry() {
		return resolverRegistry;
	}

	public RascalURIResolver getRascalResolver() {
		return rascalPathResolver;
	}
	
	public void indent(int n) {
		indentStack.push(n);
	}
	
	public void unindent() {
		indentStack.pop();
	}
	
	public int getCurrentIndent() {
		return indentStack.peek();
	}

	/**
	 * Call a Rascal function with a number of arguments
	 * 
	 * @return either null if its a void function, or the return value of the
	 *         function.
	 */
	public synchronized IValue call(IRascalMonitor monitor, String name, IValue... args) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			return call(name, args);
		}
		finally {
			setMonitor(old);
		}
	}
	
	private IValue call(String name, IValue... args) {
		QualifiedName qualifiedName = org.rascalmpl.interpreter.utils.Names.toQualifiedName(name);
		OverloadedFunctionResult func = (OverloadedFunctionResult) getCurrentEnvt().getVariable(qualifiedName);

		Type[] types = new Type[args.length];

		if (func == null) {
			throw new ImplementationError("Function " + name + " is unknown");
		}

		int i = 0;
		for (IValue v : args) {
			types[i++] = v.getType();
		}

		return func.call(getMonitor(), types, args).getValue();
	}
	
	private IConstructor parseObject(IConstructor startSort, URI location, char[] input){
		IGTD parser = getObjectParser(vf.sourceLocation(location));
		String name = "";
		if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
			name = "start__";
			startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
		}
		if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
			name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
		}

		__setInterrupt(false);
		IActionExecutor exec = new RascalActionExecutor(this, (IParserInfo) parser);
		return parser.parse(name, location, input, exec);
	}
	
	public synchronized IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, URI location){
		IRascalMonitor old = setMonitor(monitor);
		InputStream inputStream = null;
		try{
			inputStream = resolverRegistry.getInputStream(location);
			char[] input = InputConverter.toChar(inputStream);
			return parseObject(startSort, location, input);
		}catch(IOException ioex){
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.io(vf.string(ioex.getMessage()), getCurrentAST(), getStackTrace());
		}finally{
			setMonitor(old);
			
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException ioex){
					throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.io(vf.string(ioex.getMessage()), getCurrentAST(), getStackTrace());
				}
			}
		}
	}
	
	public synchronized IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, String input){
		IRascalMonitor old = setMonitor(monitor);
		try{
			return parseObject(startSort, URI.create("file://-"), input.toCharArray());
		}finally{
			setMonitor(old);
		}
	}
	
	public synchronized IConstructor parseObject(IRascalMonitor monitor, IConstructor startSort, String input, ISourceLocation loc){
		IRascalMonitor old = setMonitor(monitor);
		try{
			return parseObject(startSort, loc.getURI(), input.toCharArray());
		}finally{
			setMonitor(old);
		}
	}

	private IGTD getObjectParser(ISourceLocation loc){
		return getObjectParser((ModuleEnvironment) getCurrentEnvt().getRoot(), loc);
	}

	private IGTD getObjectParser(ModuleEnvironment currentModule, ISourceLocation loc) {
		if (currentModule.getBootstrap()) {
			return new ObjectRascalRascal();
		}
		
		if (currentModule.hasCachedParser()) {
			String className = currentModule.getCachedParser();
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				return (IGTD)clazz.newInstance();
			} catch (ClassNotFoundException e) {
				throw new ImplementationError("class for cached parser " + className + " could not be found", e);
			} catch (InstantiationException e) {
				throw new ImplementationError("could not instantiate " + className + " to valid IGTD parser", e);
			} catch (IllegalAccessException e) {
				throw new ImplementationError("not allowed to instantiate " + className + " to valid IGTD parser", e);
			}
		}

		ParserGenerator pg = getParserGenerator();
		ISet productions = currentModule.getProductions();
		Class<IGTD> parser = getHeap().getObjectParser(currentModule.getName(), productions);

		if (parser == null) {
			String parserName;
			if (rootScope == currentModule) {
				parserName = "__Shell__";
			} else {
				parserName = currentModule.getName().replaceAll("::", ".");
			}

			parser = pg.getParser(this, loc, parserName, productions);
			getHeap().storeObjectParser(currentModule.getName(), productions, parser);
		}

		try {
			return parser.newInstance();
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		}
	}

	private IGTD getRascalParser(ModuleEnvironment env, URI input) {
		ParserGenerator pg = getParserGenerator();
		ISourceLocation loc = vf.sourceLocation(input);
		IGTD objectParser = getObjectParser(env, loc);
		ISet productions = env.getProductions();
		Class<IGTD> parser = getHeap().getRascalParser(env.getName(), productions);

		if (parser == null) {
			String parserName;
			if (rootScope == env) {
				parserName = "__Shell__";
			} else {
				parserName = env.getName().replaceAll("::", ".");
			}

			parser = pg.getRascalParser(this, loc, parserName, productions, objectParser);
			getHeap().storeRascalParser(env.getName(), productions, parser);
		}

		try {
			return parser.newInstance();
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		}
	}

	public synchronized IConstructor getGrammar(IRascalMonitor monitor, URI uri) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			ParserGenerator pgen = getParserGenerator();
			ModuleEnvironment env = getHeap().getModule(uri.getAuthority());
			return pgen.getGrammar(monitor, env.getProductions());
		}
		finally {
			setMonitor(old);
		}
	}
	
	private ParserGenerator getParserGenerator() {
		startJob("Loading parser generator", 40);
		if(parserGenerator == null){
			parserGenerator = new ParserGenerator(getMonitor(), getStdErr(), classLoaders, getValueFactory());
		}
		endJob(true);
		return parserGenerator;
	}

	public void setCurrentAST(AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public AbstractAST getCurrentAST() {
		return currentAST;
	}

	public void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
		rascalPathResolver.addPathContributor(contrib);
	}

	public void addRascalSearchPath(final URI uri) {
		rascalPathResolver.addPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(List<URI> path) {
				path.add(0, uri);
			}

			@Override
			public String toString() {
				return uri.toString();
			}
		});
	}

	public void addClassLoader(ClassLoader loader) {
		// later loaders have precedence
		classLoaders.add(0, loader);
	}

	public String getStackTrace() {
		StringBuilder b = new StringBuilder(1024*1024);
		Environment env = currentEnvt;
		while (env != null) {
			ISourceLocation loc = env.getLocation();
			String name = env.getName();
			if (name != null && loc != null) {
				URI uri = loc.getURI();
				b.append('\t');
				b.append(uri.getRawPath() + ":" + loc.getBeginLine() + "," + loc.getBeginColumn() + ": " + name);
				b.append('\n');
			} else if (name != null) {
				b.append('\t');
				b.append("somewhere in: " + name);
				b.append('\n');
			}
			env = env.getCallerScope();
		}
		return b.toString();
	}

	/**
	 * Evaluate a statement
	 * 
	 * @param stat
	 * @return
	 */
	public synchronized Result<IValue> eval(Statement stat) {
		__setInterrupt(false);
		try {
			if (Evaluator.doProfiling) {
				profiler = new Profiler(this);
				profiler.start();

			}
			currentAST = stat;
			try {
				return stat.interpret(this);
			} finally {
				if (Evaluator.doProfiling) {
					if (profiler != null) {
						profiler.pleaseStop();
						profiler.report();
					}
				}
			}
		} catch (Return e) {
			throw new UnguardedReturnError(stat);
		} catch (Failure e) {
			throw new UnguardedFailError(stat);
		} catch (Insert e) {
			throw new UnguardedInsertError(stat);
		}
	}

	/**
	 * Parse and evaluate a command in the current execution environment
	 * 
	 * @param command
	 * @return
	 */
	public synchronized Result<IValue> eval(IRascalMonitor monitor, String command, URI location) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			return eval(command, location);
		}
		finally {
			setMonitor(old);
		}
	}

	private Result<IValue> eval(String command, URI location)
			throws ImplementationError {
		__setInterrupt(false);
		IConstructor tree;
		
		IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

		if (!command.contains("`")) {
			tree = Parser.parseCommand(location, command.toCharArray(), actionExecutor);
		} else {
			IGTD rp = getRascalParser(getCurrentModuleEnvironment(), location);
			tree = rp.parse("start__$Command", location, command.toCharArray(), actionExecutor);
		}

		Command stat = builder.buildCommand(tree);
		if (stat == null) {
			throw new ImplementationError("Disambiguation failed: it removed all alternatives");
		}

		return eval(stat);
	}

	public synchronized IConstructor parseCommand(IRascalMonitor monitor, String command, URI location) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			return parseCommand(command, location);
		}
		finally {
			setMonitor(old);
		}
	}
	
	private IConstructor parseCommand(String command, URI location) {
		__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

		if (!command.contains("`")) {
			return Parser.parseCommand(location, command.toCharArray(), actionExecutor);
		}

		IGTD rp = getRascalParser(getCurrentModuleEnvironment(), location);
		return rp.parse("start__$Command", location, command.toCharArray(), actionExecutor);
	}

	public synchronized IConstructor parseCommands(IRascalMonitor monitor, String commands, URI location) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			__setInterrupt(false);
			IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

			if (!commands.contains("`")) {
				return Parser.parseCommands(location, commands.toCharArray(), actionExecutor);
			}

			IGTD rp = getRascalParser(getCurrentModuleEnvironment(), location);
			return rp.parse("start__$Commands", location, commands.toCharArray(), actionExecutor);
		}
		finally {
			setMonitor(old);
		}
	}

	public synchronized Result<IValue> eval(IRascalMonitor monitor, Command command) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			return eval(command);
		}
		finally {
			setMonitor(old);
		}
	}
	
	private Result<IValue> eval(Command command) {
		__setInterrupt(false);
		if (Evaluator.doProfiling) {
			profiler = new Profiler(this);
			profiler.start();

		}
		try {
			return command.interpret(this);
		} finally {
			if (Evaluator.doProfiling) {
				if (profiler != null) {
					profiler.pleaseStop();
					profiler.report();
				}
			}
		}
	}

	/**
	 * Evaluate a declaration
	 * 
	 * @param declaration
	 * @return
	 */
	public synchronized Result<IValue> eval(IRascalMonitor monitor, Declaration declaration) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			__setInterrupt(false);
			currentAST = declaration;
			Result<IValue> r = declaration.interpret(this);
			if (r != null) {
				return r;
			}

			throw new NotYetImplemented(declaration.toString());
		}
		finally {
			setMonitor(old);
		}
	}

	public synchronized void doImport(IRascalMonitor monitor, String string) {
		IRascalMonitor old = setMonitor(monitor);
		interrupt = false;
		try {
			eval("import " + string + ";", java.net.URI.create("import:///"));
		}
		finally {
			setMonitor(old);
		}
	}

	public synchronized void reloadModules(IRascalMonitor monitor, Set<String> names, URI errorLocation) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			Set<String> onHeap = new HashSet<String>();

			for (String mod : names) {
				if (heap.existsModule(mod)) {
					onHeap.add(mod);
					heap.removeModule(heap.getModule(mod));
				}
			}

			for (String mod : onHeap) {
				if (!heap.existsModule(mod)) {
					stderr.print("Reloading module " + mod);
					reloadModule(mod, errorLocation);
				}
			}

			Set<String> depending = getDependingModules(names);
			for (String mod : depending) {
				ModuleEnvironment env = heap.getModule(mod);
				Set<String> todo = new HashSet<String>(env.getImports());
				for (String imp : todo) {
					if (names.contains(imp)) {
						env.unImport(imp);
						ModuleEnvironment imported = heap.getModule(imp);
						if (imported != null) {
							env.addImport(imp, imported);
						}
					}
				}
			}
		}
		finally {
			setMonitor(old);
		}
	}
	
	private void reloadModule(String name, URI errorLocation) {	
		ModuleEnvironment env = new ModuleEnvironment(name, getHeap());
		heap.addModule(env);

		try {
			Module module = loadModule(name, env);

			if (module != null) {
				if (!getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(getModuleName(module), name, vf.sourceLocation(errorLocation));
				}
				heap.setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				module.interpret(this);
			}
		} catch (StaticError e) {
			heap.removeModule(env);
			throw e;
		} catch (Throw e) {
			heap.removeModule(env);
			throw e;
		} catch (IOException e) {
			heap.removeModule(env);
			throw new ModuleLoadError(name, e.getMessage(), vf.sourceLocation(errorLocation));
		}
	}

	/**
	 * transitively compute which modules depend on the given modules
	 * @param names
	 * @return
	 */
	private Set<String> getDependingModules(Set<String> names) {
		Set<String> found = new HashSet<String>();
		LinkedList<String> todo = new LinkedList<String>(names);
		
		while (!todo.isEmpty()) {
			String mod = todo.pop();
			Set<String> dependingModules = heap.getDependingModules(mod);
			dependingModules.removeAll(found);
			found.addAll(dependingModules);
			todo.addAll(dependingModules);
		}
		
		return found;
	}
	
	public void unwind(Environment old) {
		setCurrentEnvt(old);
	}

	public void pushEnv() {
		Environment env = new Environment(getCurrentEnvt(), getCurrentEnvt().getName());
		setCurrentEnvt(env);
	}

	public Environment pushEnv(Statement s) {
		/* use the same name as the current envt */
		Environment env = new Environment(getCurrentEnvt(), s.getLocation(), getCurrentEnvt().getName());
		setCurrentEnvt(env);
		return env;
	}

	
	public void printHelpMessage(PrintWriter out) {
		out.println("Welcome to the Rascal command shell.");
		out.println();
		out.println("Shell commands:");
		out.println(":help                      Prints this message");
		out.println(":quit or EOF               Quits the shell");
		out.println(":declarations              Lists all visible rules, functions and variables");
		out.println(":set <option> <expression> Sets an option");
		out.println("e.g. profiling    true/false");
		out.println("     tracing      true/false");
		out.println(":edit <modulename>         Opens an editor for that module");
		out.println(":modules                   Lists all imported modules");
		out.println(":test                      Runs all unit tests currently loaded");
		out.println(":unimport <modulename>     Undo an import");
		out.println(":undeclare <name>          Undeclares a variable or function introduced in the shell");
		out.println(":history                   Print the command history");
		out.println();
		out.println("Example rascal statements and declarations:");
		out.println("1 + 1;                     Expressions simply print their output and (static) type");
		out.println("int a;                     Declarations allocate a name in the current scope");
		out.println("a = 1;                     Assignments store a value in a (optionally previously declared) variable");
		out.println("int a = 1;                 Declaration with initialization");
		out.println("import IO;                 Importing a module makes its public members available");
		out.println("println(\"Hello World\")     Function calling");
		out.println();
		out.println("Please read the manual for further information");
		out.flush();
	}

	// Modules -------------------------------------------------------------

	public void addImportToCurrentModule(AbstractAST x, String name) {
		ModuleEnvironment module = heap.getModule(name);
		if (module == null) {
			throw new UndeclaredModuleError(name, x);
		}
		getCurrentModuleEnvironment().addImport(name, module);
	}

	public ModuleEnvironment getCurrentModuleEnvironment() {
		if (!(currentEnvt instanceof ModuleEnvironment)) {
			throw new ImplementationError("Current env should be a module environment");
		}
		return ((ModuleEnvironment) currentEnvt);
	}

	public String getUnescapedModuleName(Import x) {
		return org.rascalmpl.interpreter.utils.Names.fullName(x.getModule().getName());
	}

	public Module preParseModule(URI location, ISourceLocation cause) {
		char[] data;

		InputStream inputStream = null;
		try{
			inputStream = resolverRegistry.getInputStream(location);
			data = InputConverter.toChar(inputStream);
		}catch(IOException e){
			throw new ModuleLoadError(location.toString(), e.getMessage(), cause);
		}finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException e){
					throw new ModuleLoadError(location.toString(), e.getMessage(), cause);
				}
			}
		}

		URI resolved = rascalPathResolver.resolve(location);
		if(resolved != null){
			location = resolved;
		}
		
		__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

		IConstructor prefix = Parser.preParseModule(location, data, actionExecutor);
		return builder.buildModule((IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));
	}
	
	/**
	 * Parse a module. Practical for implementing IDE features or features that
	 * use Rascal to implement Rascal. Parsing a module currently has the side
	 * effect of declaring non-terminal types in the given environment.
	 */
	public synchronized IConstructor parseModule(IRascalMonitor monitor, URI location, ModuleEnvironment env) throws IOException {
		IRascalMonitor old = setMonitor(monitor);
		
		try{
			char[] data;
			InputStream inputStream = null;
			try{
				inputStream = resolverRegistry.getInputStream(location);
				data = InputConverter.toChar(inputStream);
			}finally{
				if(inputStream != null){
					inputStream.close();
				}
			}

			URI resolved = rascalPathResolver.resolve(location);
			if(resolved != null){
				location = resolved;
			}

			return parseModule(data, location, env);
		}finally{
			setMonitor(old);
		}
	}
	
	public synchronized IConstructor parseModule(IRascalMonitor monitor, char[] data, URI location, ModuleEnvironment env) {
		IRascalMonitor old = setMonitor(monitor);
		try{
			return parseModule(data, location, env);
		}finally{
			setMonitor(old);
		}
	}

	private IConstructor parseModule(char[] data, URI location, ModuleEnvironment env) {
		__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

		event("Parsing imports and syntax definitions at " + location);
		IConstructor prefix = Parser.preParseModule(location, data, actionExecutor);

		Module preModule = builder.buildModule((IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));
		String name = getModuleName(preModule);

		if (env == null) {
			env = heap.getModule(name);
			if (env == null) {
				env = new ModuleEnvironment(name, heap);
				heap.addModule(env);
			}
			env.setBootstrap(needBootstrapParser(preModule));
		}

		// take care of imports and declare syntax
		env.setSyntaxDefined(false);
		event("Declaring syntax for module " + name);
		preModule.declareSyntax(this, true);

		ISet prods = env.getProductions();
		event("Parsing complete module " + name);
		if (needBootstrapParser(preModule)) {
			return new MetaRascalRascal().parse(Parser.START_MODULE, location, data, actionExecutor);
		}else if (prods.isEmpty() || !containsBackTick(data, preModule.getBody().getLocation().getOffset())) {
			return Parser.parseModule(location, data, actionExecutor);
		}else {
			return getRascalParser(env, location).parse(Parser.START_MODULE, location, data, actionExecutor);
		}
	}
	
	public synchronized IConstructor parseModuleWithErrorTree(IRascalMonitor monitor, char[] data, URI location, ModuleEnvironment env) {
		IRascalMonitor old = setMonitor(monitor);
		try{
			__setInterrupt(false);
			IActionExecutor actionExecutor = new RascalActionExecutor(this, Parser.getInfo());

			event("Parsing imports and syntax definitions at " + location);
			IConstructor prefix = Parser.preParseModule(location, data, actionExecutor);

			Module preModule = builder.buildModule((IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));
			String name = getModuleName(preModule);

			if(env == null){
				env = heap.getModule(name);
				if(env == null){
					env = new ModuleEnvironment(name, heap);
					heap.addModule(env);
				}
				env.setBootstrap(needBootstrapParser(preModule));
			}

			// take care of imports and declare syntax
			env.setSyntaxDefined(false);
			event("Declaring syntax for module " + name);
			preModule.declareSyntax(this, true);

			ISet prods = env.getProductions();
			IGTD parser = null;
			IConstructor result;
			event("Parsing complete module " + name);
			try{
				if(needBootstrapParser(preModule)){
					parser = new MetaRascalRascal();
					result = parser.parse(Parser.START_MODULE, location, data, actionExecutor);
				}else if(prods.isEmpty() || !containsBackTick(data, preModule.getBody().getLocation().getOffset())) {
					parser = new RascalRascal();
					result = parser.parse(Parser.START_MODULE, location, data, actionExecutor);
				}else{
					parser = getRascalParser(env, location);
					result = parser.parse(Parser.START_MODULE, location, data, actionExecutor);
				}
			}catch(SyntaxError se){
				result = parser.buildErrorTree();
				if(result == null) throw se; // Rethrow the exception if building the error tree fails.
			}
			return result;
		}finally{
			setMonitor(old);
		}
	}

	private static boolean containsBackTick(char[] data, int offset) {
		for (int i = data.length - 1; i >= offset; --i) {
			if (data[i] == '`')
				return true;
		}
		return false;
	}

	public boolean needBootstrapParser(Module preModule) {
		for (Tag tag : preModule.getHeader().getTags().getTags()) {
			if (((org.rascalmpl.ast.Name.Lexical) tag.getName()).getString().equals("bootstrapParser")) {
				return true;
			}
		}

		return false;
	}
	
	public String getCachedParser(Module preModule) {
		for (Tag tag : preModule.getHeader().getTags().getTags()) {
			if (((org.rascalmpl.ast.Name.Lexical) tag.getName()).getString().equals("cachedParser")) {
				String tagString = ((org.rascalmpl.ast.TagString.Lexical)tag.getContents()).getString();
				return tagString.substring(1, tagString.length() - 1);
			}
		}
		return null;
	}

	private Module loadModule(String name, ModuleEnvironment env) throws IOException {
		try {
			event("Loading module " + name);
			IConstructor tree = parseModule(this, java.net.URI.create("rascal:///" + name), env);
			ASTBuilder astBuilder = getBuilder();
			Module moduleAst = astBuilder.buildModule(tree);

			if (moduleAst == null) {
				throw new ImplementationError("After this, all ambiguous ast's have been filtered in " + name, astBuilder.getLastSuccessLocation());
			}
			return moduleAst;
		} catch (FactTypeUseException e) {
			throw new ImplementationError("Unexpected PDB typecheck exception", e);
		}
	}
	
	public ASTBuilder getBuilder() {
		return builder;
	}
	
	public Module extendCurrentModule(AbstractAST x, String name) {
		ModuleEnvironment env = getCurrentModuleEnvironment();
		try {
			Module module = loadModule(name, env);

			if (module != null) {
				if (!getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(getModuleName(module), name, x);
				}
				heap.setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				((org.rascalmpl.semantics.dynamic.Module.Default)module).interpretInCurrentEnv(this);
				return module;
			}
		} catch (StaticError e) {
			throw e;
		} catch (Throw e) {
			throw e;
		} catch (IOException e) {
			throw new ModuleLoadError(name, e.getMessage(), x);
		}

		throw new ImplementationError("Unexpected error while parsing module " + name + " and building an AST for it ", x.getLocation());
	}
	
	public Module evalRascalModule(AbstractAST x, String name) {
		ModuleEnvironment env = heap.getModule(name);
		if (env == null) {
			env = new ModuleEnvironment(name, heap);
			heap.addModule(env);
		}
		try {
			Module module = loadModule(name, env);

			if (module != null) {
				if (!getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(getModuleName(module), name, x);
				}
				heap.setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				
				// TODO: is this declare syntax necessary? (again!)
				module.declareSyntax(this, true);
				
				event("Running toplevel declarations in " + name);
				module.interpret(this);
				return module;
			}
		} catch (StaticError e) {
			heap.removeModule(env);
			throw e;
		} catch (Throw e) {
			heap.removeModule(env);
			throw e;
		} catch (IOException e) {
			heap.removeModule(env);
			throw new ModuleLoadError(name, e.getMessage(), x);
		}

		heap.removeModule(env);
		throw new ImplementationError("Unexpected error while parsing module " + name + " and building an AST for it ", x.getLocation());
	}

	public String getModuleName(Module module) {
		String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	public IBooleanResult makeBooleanResult(Expression pat) {
		if (pat instanceof Ambiguity) {
			// TODO: wrong exception here.
			throw new Ambiguous((IConstructor) pat.getTree());
		}

		BooleanEvaluator pe = new BooleanEvaluator(this);
		return pat.buildBooleanBacktracker(pe);
	} 

	public Result<IValue> evalBooleanExpression(Expression x) {
		IBooleanResult mp = makeBooleanResult(x);
		mp.init();
		while (mp.hasNext()) {
			if (interrupt)
				throw new InterruptException(getStackTrace());
			if (mp.next()) {
				return org.rascalmpl.interpreter.result.ResultFactory.bool(true, this);
			}
		}
		return org.rascalmpl.interpreter.result.ResultFactory.bool(false, this);
	}

	public boolean matchAndEval(Result<IValue> subject, Expression pat, Statement stat) {
		boolean debug = false;
		Environment old = getCurrentEnvt();
		pushEnv();

		try {
			IMatchingResult mp = pat.buildMatcher((PatternEvaluator) patternEvaluator);
			mp.initMatch(subject);

			while (mp.hasNext()) {
				pushEnv();
				if (interrupt)
					throw new InterruptException(getStackTrace());

				if (mp.next()) {
					try {
						try {
							stat.interpret(this);
						} catch (Insert e) {
							// Make sure that the match pattern is set
							if (e.getMatchPattern() == null) {
								e.setMatchPattern(mp);
							}
							throw e;
						}
						return true;
					} catch (Failure e) {
						// unwind(old); // can not clean up because you don't
						// know how far to roll back
					}
				}
			}
		} finally {
			if (debug)
				System.err.println("Unwind to old env");
			unwind(old);
		}
		return false;
	}

	boolean matchEvalAndReplace(Result<IValue> subject, Expression pat, List<Expression> conditions, Expression replacementExpr) {
		Environment old = getCurrentEnvt();
		try {
			IMatchingResult mp = pat.buildMatcher((PatternEvaluator) patternEvaluator);
			mp.initMatch(subject);

			while (mp.hasNext()) {
				if (interrupt)
					throw new InterruptException(getStackTrace());
				if (mp.next()) {
					try {
						boolean trueConditions = true;
						for (Expression cond : conditions) {
							if (!cond.interpret(this).isTrue()) {
								trueConditions = false;
								break;
							}
						}
						if (trueConditions) {
							throw new Insert(replacementExpr.interpret(this), mp);
						}
					} catch (Failure e) {
						System.err.println("failure occurred");
					}
				}
			}
		} finally {
			unwind(old);
		}
		return false;
	}

	public static final Name IT = builder.makeLex("Name", null, "<it>");
	private ParserGenerator parserGenerator;
	

	
	public void updateProperties() {
		Evaluator.doProfiling = org.rascalmpl.interpreter.Configuration.getProfilingProperty();

		org.rascalmpl.interpreter.result.AbstractFunction.setCallTracing(org.rascalmpl.interpreter.Configuration.getTracingProperty());
	}

	public Stack<Environment> getCallStack() {
		Stack<Environment> stack = new Stack<Environment>();
		Environment env = currentEnvt;
		while (env != null) {
			stack.add(0, env);
			env = env.getCallerScope();
		}
		return stack;
	}

	public Environment getCurrentEnvt() {
		return currentEnvt;
	}

	public void setCurrentEnvt(Environment env) {
		currentEnvt = env;
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this;
	}

	public GlobalEnvironment getHeap() {
		return __getHeap();
	}

	public boolean runTests(IRascalMonitor monitor) {
		IRascalMonitor old = setMonitor(monitor);
		try {
			final boolean[] allOk = new boolean[] { true };
			final ITestResultListener l = testReporter != null ? testReporter : new DefaultTestResultListener(getStdOut());

			new TestEvaluator(this, new ITestResultListener() {
				public void report(boolean successful, String test, ISourceLocation loc, Throwable t) {
					if (!successful)
						allOk[0] = false;
					l.report(successful, test, loc, t);
				}

				public void report(boolean successful, String test, ISourceLocation loc) {
					if (!successful)
						allOk[0] = false;
					l.report(successful, test, loc);
				}

				public void done() {
					l.done();
				}

				public void start(int count) {
					l.start(count);
				}
			}).test();
			return allOk[0];
		}
		finally {
			setMonitor(old);
		}
	}

	public IValueFactory getValueFactory() {
		return __getVf();
	}

	public IStrategyContext getStrategyContext() {
		return strategyContextStack.getCurrentContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		strategyContextStack.pushContext(strategyContext);
	}

	public void popStrategyContext() {
		strategyContextStack.popContext();
	}

	public void setAccumulators(Accumulator accu) {
		__getAccumulators().push(accu);
	}

	public Stack<Accumulator> getAccumulators() {
		return __getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		this.accumulators = accumulators;
	}

	public IRascalMonitor getMonitor() {
		if (monitor != null)
			return monitor;
		else
			return new NullRascalMonitor();
	}
}
