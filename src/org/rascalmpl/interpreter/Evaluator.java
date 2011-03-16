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
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
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
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;
import org.rascalmpl.interpreter.staticErrors.UnguardedInsertError;
import org.rascalmpl.interpreter.staticErrors.UnguardedReturnError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.strategy.StrategyContextStack;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.library.rascal.syntax.MetaRascalRascal;
import org.rascalmpl.library.rascal.syntax.ObjectRascalRascal;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.IParserInfo;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.RascalActionExecutor;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.uri.CWDURIResolver;
import org.rascalmpl.uri.ClassResourceInputOutput;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.HomeURIResolver;
import org.rascalmpl.uri.HttpURIResolver;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.Factory;


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
	private IEvaluator<IMatchingResult> patternEvaluator;

	private final List<ClassLoader> classLoaders;
	private final ModuleEnvironment rootScope;
	private boolean concreteListsShouldBeSpliced;
	private final Parser parser;

	private final PrintWriter stderr;
	private final PrintWriter stdout;

	private ITestResultListener testReporter;
	private Stack<Accumulator> accumulators = new Stack<Accumulator>();
	private Stack<Integer> indentStack = new Stack<Integer>();
	private final RascalURIResolver rascalPathResolver;
	private static final ASTBuilder builder = new ASTBuilder();

	private final URIResolverRegistry resolverRegistry;

	private HashSet<IConstructorDeclared> constructorDeclaredListeners;

	public Evaluator(IValueFactory f, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap) {
		this(f, stderr, stdout, scope, heap, new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new RascalURIResolver(new URIResolverRegistry()));
	}

	public Evaluator(IValueFactory f, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap, List<ClassLoader> classLoaders, RascalURIResolver rascalURIResolver) {
		this.__setVf(f);
		this.__setPatternEvaluator(new PatternEvaluator(this));
		this.strategyContextStack = new StrategyContextStack();
		this.heap = heap;
		this.typeDeclarator = new TypeDeclarationEvaluator(this);
		this.currentEnvt = scope;
		this.rootScope = scope;
		this.__getHeap().addModule(scope);
		this.classLoaders = classLoaders;
		this.javaBridge = new JavaBridge(this.classLoaders, this.__getVf());
		this.rascalPathResolver = rascalURIResolver;
		this.parser = new Parser();
		this.stderr = stderr;
		this.stdout = stdout;
		this.resolverRegistry = rascalPathResolver.getRegistry();
		this.constructorDeclaredListeners = new HashSet<IConstructorDeclared>();

		this.updateProperties();

		if (stderr == null) {
			throw new NullPointerException();
		}
		if (stdout == null) {
			throw new NullPointerException();
		}

		this.rascalPathResolver.addPathContributor(new IRascalSearchPathContributor() {
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
		this.resolverRegistry.registerInputOutput(files);

		HttpURIResolver http = new HttpURIResolver();
		this.resolverRegistry.registerInput(http);

		CWDURIResolver cwd = new CWDURIResolver();
		this.resolverRegistry.registerInputOutput(cwd);

		ClassResourceInputOutput library = new ClassResourceInputOutput(this.resolverRegistry, "std", this.getClass(), "/org/rascalmpl/library");
		this.resolverRegistry.registerInputOutput(library);

		ClassResourceInputOutput testdata = new ClassResourceInputOutput(this.resolverRegistry, "testdata", this.getClass(), "/org/rascalmpl/test/data");
		this.resolverRegistry.registerInput(testdata);

		this.resolverRegistry.registerInput(new JarURIResolver(this.getClass()));

		this.resolverRegistry.registerInputOutput(this.rascalPathResolver);

		HomeURIResolver home = new HomeURIResolver();
		this.resolverRegistry.registerInputOutput(home);
	}

	public void registerConstructorDeclaredListener(IConstructorDeclared iml) {
		this.constructorDeclaredListeners.add(iml);
	}
	
	public void notifyConstructorDeclaredListeners() {
		for (IConstructorDeclared iml : this.constructorDeclaredListeners) iml.handleConstructorDeclaredEvent();
		this.constructorDeclaredListeners.clear();
	}
	
	public List<ClassLoader> getClassLoaders() {
		return Collections.unmodifiableList(classLoaders);
	}

	public Parser __getParser() {
		return parser;
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

	public void __setConcreteListsShouldBeSpliced(boolean concreteListsShouldBeSpliced) {
		this.concreteListsShouldBeSpliced = concreteListsShouldBeSpliced;
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

	public void __setAccumulators(Stack<Accumulator> accumulators) {
		this.accumulators = accumulators;
	}

	public Stack<Accumulator> __getAccumulators() {
		return accumulators;
	}

	public void __setPatternEvaluator(IEvaluator<IMatchingResult> patternEvaluator) {
		this.patternEvaluator = patternEvaluator;
	}

	public IEvaluator<IMatchingResult> __getPatternEvaluator() {
		return patternEvaluator;
	}

	public void __setVf(IValueFactory vf) {
		this.vf = vf;
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
		this.__setInterrupt(true);
	}

	public boolean isInterrupted() {
		return this.__getInterrupt();
	}

	public PrintWriter getStdErr() {
		return this.stderr;
	}

	public void setTestResultListener(ITestResultListener l) {
		this.testReporter = l;
	}

	public JavaBridge getJavaBridge() {
		return this.__getJavaBridge();
	}

	public URIResolverRegistry getResolverRegistry() {
		return this.resolverRegistry;
	}

	public RascalURIResolver getRascalResolver() {
		return this.rascalPathResolver;
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
	public IValue call(String name, IValue... args) {
		QualifiedName qualifiedName = org.rascalmpl.interpreter.utils.Names.toQualifiedName(name);
		OverloadedFunctionResult func = (OverloadedFunctionResult) this.getCurrentEnvt().getVariable(qualifiedName);

		Type[] types = new Type[args.length];

		if (func == null) {
			throw new ImplementationError("Function " + name + " is unknown");
		}

		int i = 0;
		for (IValue v : args) {
			types[i++] = v.getType();
		}

		return func.call(types, args).getValue();
	}

	/**
	 * Parse an object string using the imported SDF modules from the current
	 * context.
	 */
	public IConstructor parseObject(IConstructor startSort, URI input) {
		try {
			System.err.println("Generating a parser");
			IGTD parser = this.getObjectParser(this.__getVf().sourceLocation(input));
			String name = "";
			if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
				name = "start__";
				startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
			}
			if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
				name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
			}

			this.__setInterrupt(false);
			IActionExecutor exec = new RascalActionExecutor(this, (IParserInfo) parser);
			return parser.parse(name, input, this.resolverRegistry.getInputStream(input), exec);
		} catch (IOException e) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.io(this.__getVf().string(e.getMessage()), this.getCurrentAST(), this.getStackTrace());
		}
	}

	public IConstructor parseObject(IConstructor startSort, String input) {
		URI inputURI = java.net.URI.create("file://-");
		IGTD parser = this.getObjectParser(this.__getVf().sourceLocation(inputURI));
		String name = "";
		if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
			name = "start__";
			startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
		}
		if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
			name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
		}
		this.__setInterrupt(false);
		IActionExecutor exec = new RascalActionExecutor(this, (IParserInfo) parser);
		return parser.parse(name, inputURI, input, exec);
	}
	
	public IConstructor parseObject(IConstructor startSort, String input, ISourceLocation loc) {
		IGTD parser = this.getObjectParser(loc);
		String name = "";
		if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
			name = "start__";
			startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
		}
		if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
			name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
		}
		this.__setInterrupt(false);
		IActionExecutor exec = new RascalActionExecutor(this, (IParserInfo) parser);
		return parser.parse(name, loc.getURI(), input, exec);
	}

	private IGTD getObjectParser(ISourceLocation loc) {
		return this.getObjectParser((ModuleEnvironment) this.getCurrentEnvt().getRoot(), loc);
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

		ParserGenerator pg = this.getParserGenerator();
		ISet productions = currentModule.getProductions();
		Class<IGTD> parser = this.getHeap().getObjectParser(currentModule.getName(), productions);

		if (parser == null) {
			String parserName;
			if (this.__getRootScope() == currentModule) {
				parserName = "__Shell__";
			} else {
				parserName = currentModule.getName().replaceAll("::", ".");
			}

			parser = pg.getParser(loc, parserName, productions);
			this.getHeap().storeObjectParser(currentModule.getName(), productions, parser);
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
		ParserGenerator pg = this.getParserGenerator();
		ISourceLocation loc = this.__getVf().sourceLocation(input);
		IGTD objectParser = this.getObjectParser(env, loc);
		ISet productions = env.getProductions();
		Class<IGTD> parser = this.getHeap().getRascalParser(env.getName(), productions);

		if (parser == null) {
			String parserName;
			if (this.__getRootScope() == env) {
				parserName = "__Shell__";
			} else {
				parserName = env.getName().replaceAll("::", ".");
			}

			parser = pg.getRascalParser(loc, parserName, productions, objectParser);
			this.getHeap().storeRascalParser(env.getName(), productions, parser);
		}

		try {
			return parser.newInstance();
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		}
	}

	public ParserGenerator getParserGenerator() {
		if (this.parserGenerator == null) {
			this.parserGenerator = new ParserGenerator(this.getStdErr(), this.classLoaders, this.getValueFactory());
		}
		return this.parserGenerator;
	}

	public void setCurrentAST(AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public AbstractAST getCurrentAST() {
		return this.currentAST;
	}

	public void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
		this.rascalPathResolver.addPathContributor(contrib);
	}

	public void addRascalSearchPath(final URI uri) {
		this.rascalPathResolver.addPathContributor(new IRascalSearchPathContributor() {
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
		this.classLoaders.add(0, loader);
	}

	public String getStackTrace() {
		StringBuilder b = new StringBuilder(1024*1024);
		Environment env = this.currentEnvt;
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
	public Result<IValue> eval(Statement stat) {
		this.__setInterrupt(false);
		try {
			if (Evaluator.doProfiling) {
				this.profiler = new Profiler(this);
				this.profiler.start();

			}
			this.currentAST = stat;
			try {
				return stat.interpret(this);
			} finally {
				if (Evaluator.doProfiling) {
					if (this.profiler != null) {
						this.profiler.pleaseStop();
						this.profiler.report();
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
	 * Evaluate an expression
	 * 
	 * @param expr
	 * @return
	 */
	public Result<IValue> eval(Expression expr) {
		this.__setInterrupt(false);
		this.currentAST = expr;
		if (Evaluator.doProfiling) {
			this.profiler = new Profiler(this);
			this.profiler.start();

		}
		try {
			Result<IValue> r = expr.interpret(this);
			if (r != null) {
				return r;
			}
		} finally {
			if (Evaluator.doProfiling) {
				if (this.profiler != null) {
					this.profiler.pleaseStop();
					this.profiler.report();
				}
			}
		}

		throw new NotYetImplemented(expr.toString());
	}

	/**
	 * Parse and evaluate a command in the current execution environment
	 * 
	 * @param command
	 * @return
	 */
	public Result<IValue> eval(String command, URI location) {
		this.__setInterrupt(false);
		IConstructor tree;

		IActionExecutor actionExecutor = new RascalActionExecutor(this, this.__getParser().getInfo());

		if (!command.contains("`")) {
			tree = this.__getParser().parseCommand(location, command, actionExecutor);
		} else {
			IGTD rp = this.getRascalParser(this.getCurrentModuleEnvironment(), location);
			tree = rp.parse("start__$Command", location, command, actionExecutor);
		}

		Command stat = builder.buildCommand(tree);
		if (stat == null) {
			throw new ImplementationError("Disambiguation failed: it removed all alternatives");
		}

		return this.eval(stat);
	}

	public IConstructor parseCommand(String command, URI location) {
		this.__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, this.__getParser().getInfo());

		if (!command.contains("`")) {
			return this.__getParser().parseCommand(location, command, actionExecutor);
		}

		IGTD rp = this.getRascalParser(this.getCurrentModuleEnvironment(), location);
		return rp.parse("start__$Command", location, command, actionExecutor);
	}

	public Result<IValue> eval(Command command) {
		this.__setInterrupt(false);
		if (Evaluator.doProfiling) {
			this.profiler = new Profiler(this);
			this.profiler.start();

		}
		try {
			return command.interpret(this);
		} finally {
			if (Evaluator.doProfiling) {
				if (this.profiler != null) {
					this.profiler.pleaseStop();
					this.profiler.report();
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
	public Result<IValue> eval(Declaration declaration) {
		this.__setInterrupt(false);
		this.currentAST = declaration;
		Result<IValue> r = declaration.interpret(this);
		if (r != null) {
			return r;
		}

		throw new NotYetImplemented(declaration.toString());
	}

	/**
	 * Evaluate an import
	 * 
	 * @param imp
	 * @return
	 */
	public Result<IValue> eval(Import imp) {
		this.__setInterrupt(false);
		this.currentAST = imp;
		Result<IValue> r = imp.interpret(this);
		if (r != null) {
			return r;
		}

		throw new ImplementationError("Not yet implemented: " + imp.getTree());
	}

	public void doImport(String string) {
		this.eval("import " + string + ";", java.net.URI.create("import:///"));
	}

	public void reloadModules(Set<String> names, URI errorLocation) {
		Set<String> onHeap = new HashSet<String>();
		
		for (String mod : names) {
			if (this.__getHeap().existsModule(mod)) {
				onHeap.add(mod);
				this.__getHeap().removeModule(this.__getHeap().getModule(mod));
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
	
	private void reloadModule(String name, URI errorLocation) {	
		ModuleEnvironment env = new ModuleEnvironment(name, getHeap());
		this.__getHeap().addModule(env);

		try {
			Module module = this.loadModule(name, env);

			if (module != null) {
				if (!this.getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(this.getModuleName(module), name, this.__getVf().sourceLocation(errorLocation));
				}
				this.__getHeap().setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				module.interpret(this);
			}
		} catch (StaticError e) {
			this.__getHeap().removeModule(env);
			throw e;
		} catch (Throw e) {
			this.__getHeap().removeModule(env);
			throw e;
		} catch (IOException e) {
			this.__getHeap().removeModule(env);
			throw new ModuleLoadError(name, e.getMessage(), this.__getVf().sourceLocation(errorLocation));
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
	
	/**
	 * Return an evaluation result that is already in normal form, i.e., all
	 * potential rules have already been applied to it.
	 */
	public Result<IValue> normalizedResult(Type t, IValue v) {
		Map<Type, Type> bindings = this.getCurrentEnvt().getTypeBindings();
		Type instance;

		if (bindings.size() > 0) {
			instance = t.instantiate(bindings);
		} else {
			instance = t;
		}

		if (v != null) {
			this.checkType(v.getType(), instance);
		}
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(instance, v, this);
	}

	public void unwind(Environment old) {
		// TODO why not just replace the current env with the old one??
//		while (this.getCurrentEnvt() != old) {
//			this.setCurrentEnvt(this.getCurrentEnvt().getParent());
//			this.getCurrentEnvt();
//		}
		setCurrentEnvt(old);
	}

	public void pushEnv() {
		Environment env = new Environment(this.getCurrentEnvt(), this.getCurrentEnvt().getName());
		this.setCurrentEnvt(env);
	}

	public Environment pushEnv(Statement s) {
		/* use the same name as the current envt */
		Environment env = new Environment(this.getCurrentEnvt(), s.getLocation(), this.getCurrentEnvt().getName());
		this.setCurrentEnvt(env);
		return env;
	}

	private void checkType(Type given, Type expected) {
		if (expected instanceof FunctionType) {
			return;
		}
		if (!given.isSubtypeOf(expected)) {
			throw new UnexpectedTypeError(expected, given, this.getCurrentAST());
		}
	}

	public boolean mayOccurIn(Type small, Type large) {
		return this.mayOccurIn(small, large, new HashSet<Type>());
	}

	boolean mayOccurIn(Type small, Type large, Set<Type> seen) {
		// TODO: this should probably be a visitor as well

		if (small.isVoidType())
			return true;
		if (large.isVoidType())
			return false;
		if (small.isValueType())
			return true;
		if (small.isSubtypeOf(large))
			return true;
		if (large.isListType() || large.isSetType())
			return this.mayOccurIn(small, large.getElementType(), seen);
		if (large.isMapType())
			return this.mayOccurIn(small, large.getKeyType(), seen) || this.mayOccurIn(small, large.getValueType(), seen);
		if (large.isTupleType()) {
			for (int i = 0; i < large.getArity(); i++) {
				if (this.mayOccurIn(small, large.getFieldType(i), seen))
					return true;
			}
			return false;
		}

		if (large instanceof NonTerminalType && small instanceof NonTerminalType) {
			// TODO: Until we have more precise info about the types in the
			// concrete syntax
			// we just return true here.
			return true;
		}

		if (large.isConstructorType()) {

			for (int i = 0; i < large.getArity(); i++) {
				if (this.mayOccurIn(small, large.getFieldType(i), seen))
					return true;
			}
			return false;
		}
		if (large.isAbstractDataType()) {
			if (small.isNodeType() && !small.isAbstractDataType())
				return true;
			if (small.isConstructorType() && small.getAbstractDataType().equivalent(large.getAbstractDataType()))
				return true;
			seen.add(large);
			for (Type alt : this.getCurrentEnvt().lookupAlternatives(large)) {
				if (alt.isConstructorType()) {
					for (int i = 0; i < alt.getArity(); i++) {
						Type fType = alt.getFieldType(i);
						if (seen.add(fType) && this.mayOccurIn(small, fType, seen))
							return true;
					}
				} else
					throw new ImplementationError("ADT");

			}
			return false;
		}
		return small.isSubtypeOf(large);
	}

	// Ambiguity ...................................................

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
		ModuleEnvironment module = this.__getHeap().getModule(name);
		if (module == null) {
			throw new UndeclaredModuleError(name, x);
		}
		this.getCurrentModuleEnvironment().addImport(name, module);
	}

	public ModuleEnvironment getCurrentModuleEnvironment() {
		if (!(this.currentEnvt instanceof ModuleEnvironment)) {
			throw new ImplementationError("Current env should be a module environment");
		}
		return ((ModuleEnvironment) this.currentEnvt);
	}

	public String getUnescapedModuleName(Import x) {
		return org.rascalmpl.interpreter.utils.Names.fullName(x.getModule().getName());
	}

	public void loadParseTreeModule(AbstractAST x) {
		String parseTreeModName = "ParseTree";
		if (!this.__getHeap().existsModule(parseTreeModName)) {
			this.evalRascalModule(x, parseTreeModName);
		}
		this.addImportToCurrentModule(x, parseTreeModName);
	}

	/**
	 * Parse a module. Practical for implementing IDE features or features that
	 * use Rascal to implement Rascal. Parsing a module currently has the side
	 * effect of declaring non-terminal types in the given environment.
	 */
	public IConstructor parseModule(URI location, ModuleEnvironment env) throws IOException {
		char[] data;

		InputStream inputStream = null;
		try {
			inputStream = this.resolverRegistry.getInputStream(location);
			data = this.readModule(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		URI resolved = this.rascalPathResolver.resolve(location);
		if (resolved != null) {
			location = resolved;
		}

		return this.parseModule(data, location, env);
	}

	public Module preParseModule(URI location, ISourceLocation cause) {
		char[] data;

		InputStream inputStream = null;
		try {
			inputStream = this.resolverRegistry.getInputStream(location);
			data = this.readModule(inputStream);
		} catch (IOException e) {
			throw new ModuleLoadError(location.toString(), e.getMessage(), cause);
		} 
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new ModuleLoadError(location.toString(), e.getMessage(), cause);
				}
			}
		}

		URI resolved = this.rascalPathResolver.resolve(location);
		if (resolved != null) {
			location = resolved;
		}
		
		this.__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, this.__getParser().getInfo());

		IConstructor prefix = this.__getParser().preParseModule(location, data, actionExecutor);
		return builder.buildModule((IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));
	}
	
	public IConstructor parseModule(char[] data, URI location, ModuleEnvironment env) {
		this.__setInterrupt(false);
		IActionExecutor actionExecutor = new RascalActionExecutor(this, this.__getParser().getInfo());

		IConstructor prefix = this.__getParser().preParseModule(location, data, actionExecutor);
		
		Module preModule = builder.buildModule((IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));
		String name = getModuleName(preModule);
		
		if (env == null) {
			env = this.__getHeap().getModule(name);
			if (env == null) {
				env = new ModuleEnvironment(name, heap);
				heap.addModule(env);
			}
			env.setBootstrap(needBootstrapParser(preModule));
		}
		
		// take care of imports and declare syntax
		preModule.declareSyntax(this, true);

		ISet prods = env.getProductions();
		if (this.needBootstrapParser(preModule)) {
			return new MetaRascalRascal().parse(Parser.START_MODULE, location, data, actionExecutor);
		}
		else if (prods.isEmpty() || !containsBackTick(data, preModule.getBody().getLocation().getOffset())) {
			return this.__getParser().parseModule(location, data, actionExecutor);
		}
		else {
			return this.getRascalParser(env, location).parse(Parser.START_MODULE, location, data, actionExecutor);
		}
	}

	public static boolean containsBackTick(char[] data, int offset) {
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

	private char[] readModule(InputStream inputStream) throws IOException {
		char[] buffer = new char[8192];
		CharArrayWriter writer = new CharArrayWriter();
		InputStreamReader reader = new InputStreamReader(inputStream);

		int bytesRead;
		while ((bytesRead = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, bytesRead);
		}

		return writer.toCharArray();
	}

	private Module loadModule(String name, ModuleEnvironment env) throws IOException {
		try {
			IConstructor tree = this.parseModule(java.net.URI.create("rascal:///" + name), env);
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
		ModuleEnvironment env = this.getCurrentModuleEnvironment();
		try {
			Module module = this.loadModule(name, env);

			if (module != null) {
				if (!this.getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(this.getModuleName(module), name, x);
				}
				this.__getHeap().setModuleURI(name, module.getLocation().getURI());
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
		ModuleEnvironment env = this.__getHeap().getModule(name);
		if (env == null) {
			env = new ModuleEnvironment(name, this.__getHeap());
			this.__getHeap().addModule(env);
		}
		try {
			Module module = this.loadModule(name, env);

			if (module != null) {
				if (!this.getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(this.getModuleName(module), name, x);
				}
				this.__getHeap().setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				module.declareSyntax(this, true);
				module.interpret(this);
				return module;
			}
		} catch (StaticError e) {
			this.__getHeap().removeModule(env);
			throw e;
		} catch (Throw e) {
			this.__getHeap().removeModule(env);
			throw e;
		} catch (IOException e) {
			this.__getHeap().removeModule(env);
			throw new ModuleLoadError(name, e.getMessage(), x);
		}

		this.__getHeap().removeModule(env);
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

	// Expressions -----------------------------------------------------------

	public Result<IValue> evalBooleanExpression(Expression x) {
		IBooleanResult mp = this.makeBooleanResult(x);
		mp.init();
		while (mp.hasNext()) {
			if (this.__getInterrupt())
				throw new InterruptException(this.getStackTrace());
			if (mp.next()) {
				return org.rascalmpl.interpreter.result.ResultFactory.bool(true, this);
			}
		}
		return org.rascalmpl.interpreter.result.ResultFactory.bool(false, this);
	}

	

	public boolean matchAndEval(Result<IValue> subject, Expression pat, Statement stat) {
		boolean debug = false;
		Environment old = this.getCurrentEnvt();
		this.pushEnv();

		try {
			IMatchingResult mp = pat.buildMatcher((PatternEvaluator) this.__getPatternEvaluator());
			mp.initMatch(subject);

			while (mp.hasNext()) {
				this.pushEnv();
				if (this.__getInterrupt())
					throw new InterruptException(this.getStackTrace());

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
			this.unwind(old);
		}
		return false;
	}

	boolean matchEvalAndReplace(Result<IValue> subject, Expression pat, List<Expression> conditions, Expression replacementExpr) {
		Environment old = this.getCurrentEnvt();
		try {
			IMatchingResult mp = pat.buildMatcher((PatternEvaluator) this.__getPatternEvaluator());
			mp.initMatch(subject);

			while (mp.hasNext()) {
				if (this.__getInterrupt())
					throw new InterruptException(this.getStackTrace());
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
			this.unwind(old);
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
		Environment env = this.currentEnvt;
		while (env != null) {
			stack.add(0, env);
			env = env.getCallerScope();
		}
		return stack;
	}

	public Environment getCurrentEnvt() {
		return this.currentEnvt;
	}

	public void setCurrentEnvt(Environment env) {
		this.currentEnvt = env;
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this;
	}

	public GlobalEnvironment getHeap() {
		return this.__getHeap();
	}

	public boolean runTests() {
		final boolean[] allOk = new boolean[] { true };
		final ITestResultListener l = this.testReporter != null ? this.testReporter : new DefaultTestResultListener(this.getStdOut());

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

	public IValueFactory getValueFactory() {
		return this.__getVf();
	}

	public void setIValueFactory(IValueFactory factory) {
		this.__setVf(factory);
	}

	public IStrategyContext getStrategyContext() {
		return this.strategyContextStack.getCurrentContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		this.strategyContextStack.pushContext(strategyContext);
	}

	public void popStrategyContext() {
		this.strategyContextStack.popContext();
	}

	public void setAccumulators(Accumulator accu) {
		this.__getAccumulators().push(accu);
	}

	public Stack<Accumulator> getAccumulators() {
		return this.__getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		this.__setAccumulators(accumulators);
	}

	public void appendToString(IValue value, StringBuilder b) {
		if (value.getType() == Factory.Tree) {
			b.append(org.rascalmpl.values.uptr.TreeAdapter.yield((IConstructor) value));
		} else if (value.getType().isStringType()) {
			b.append(((IString) value).getValue());
		} else {
			b.append(value.toString());
		}
	}

	

}
