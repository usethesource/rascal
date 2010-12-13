package org.rascalmpl.interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.rascalmpl.bridge.Rascal;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.Factory;

public class Evaluator extends org.rascalmpl.ast.NullASTVisitor<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> implements org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> {
	private org.eclipse.imp.pdb.facts.IValueFactory vf;
	private static final org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	protected org.rascalmpl.interpreter.env.Environment currentEnvt;
	private org.rascalmpl.interpreter.strategy.StrategyContextStack strategyContextStack;
  
	private final org.rascalmpl.interpreter.env.GlobalEnvironment heap;
	private boolean interrupt = false;

	private final org.rascalmpl.interpreter.utils.JavaBridge javaBridge;

	private org.rascalmpl.ast.AbstractAST currentAST; 	// used in runtime errormessages

	private static boolean doProfiling = false;
	private org.rascalmpl.interpreter.utils.Profiler profiler;
	
	private final org.rascalmpl.interpreter.TypeDeclarationEvaluator typeDeclarator;
	private org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.matching.IMatchingResult> patternEvaluator;

	private final java.util.List<java.lang.ClassLoader> classLoaders;
	private final org.rascalmpl.interpreter.env.ModuleEnvironment rootScope;
	private boolean concreteListsShouldBeSpliced;
	private final org.rascalmpl.parser.Parser parser;

	private final java.io.PrintWriter stderr;
	private final java.io.PrintWriter stdout;

	private org.rascalmpl.interpreter.ITestResultListener testReporter;
	private java.util.Stack<org.rascalmpl.interpreter.Accumulator> accumulators = new java.util.Stack<org.rascalmpl.interpreter.Accumulator>();
	private final org.rascalmpl.interpreter.load.RascalURIResolver rascalPathResolver;
	private final org.rascalmpl.parser.ASTBuilder builder;
	
	private final org.rascalmpl.uri.URIResolverRegistry resolverRegistry;

	public Evaluator(org.eclipse.imp.pdb.facts.IValueFactory f, java.io.PrintWriter stderr, java.io.PrintWriter stdout, org.rascalmpl.interpreter.env.ModuleEnvironment scope, org.rascalmpl.interpreter.env.GlobalEnvironment heap) {
		this(f,stderr,stdout,scope,heap, new java.util.ArrayList<java.lang.ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new org.rascalmpl.interpreter.load.RascalURIResolver(new URIResolverRegistry()));	
	}
	
	public Evaluator(org.eclipse.imp.pdb.facts.IValueFactory f, java.io.PrintWriter stderr, java.io.PrintWriter stdout, org.rascalmpl.interpreter.env.ModuleEnvironment scope, org.rascalmpl.interpreter.env.GlobalEnvironment heap, java.util.List<java.lang.ClassLoader> classLoaders, RascalURIResolver rascalURIResolver) {
		this.__setVf(f);
		this.__setPatternEvaluator(new org.rascalmpl.interpreter.PatternEvaluator(this));
		this.strategyContextStack = new org.rascalmpl.interpreter.strategy.StrategyContextStack();
		this.heap = heap;
		this.typeDeclarator = new org.rascalmpl.interpreter.TypeDeclarationEvaluator(this);
		this.currentEnvt = scope;
		this.rootScope = scope;
		this.__getHeap().addModule(scope);
		this.classLoaders = classLoaders;
		this.javaBridge = new org.rascalmpl.interpreter.utils.JavaBridge(this.classLoaders, this.__getVf());
		this.rascalPathResolver = rascalURIResolver;
		this.parser = new org.rascalmpl.parser.Parser();
		this.stderr = stderr;
		this.stdout = stdout;
		this.builder = new org.rascalmpl.parser.ASTBuilder(org.rascalmpl.ast.ASTFactoryFactory.getASTFactory());
		this.resolverRegistry = rascalPathResolver.getRegistry();

		this.updateProperties();
		
		if (stderr == null) {
			throw new java.lang.NullPointerException();
		}
		if (stdout == null) {
			throw new java.lang.NullPointerException();
		}

		this.rascalPathResolver.addPathContributor(new org.rascalmpl.interpreter.load.IRascalSearchPathContributor() {
			public void contributePaths(java.util.List<java.net.URI> l) {
				l.add(java.net.URI.create("cwd:///"));
				l.add(java.net.URI.create("std:///"));
				l.add(java.net.URI.create("testdata:///"));

				java.lang.String property = java.lang.System.getProperty("rascal.path");

				if (property != null) {
					for (java.lang.String path : property.split(":")) {
						l.add(new java.io.File(path).toURI());
					}
				}
			}
			@Override
			public java.lang.String toString() {
				return "[current wd and stdlib]";
			}
		});

		// register some schemes
		org.rascalmpl.uri.FileURIResolver files = new org.rascalmpl.uri.FileURIResolver(); 
		this.resolverRegistry.registerInputOutput(files);

		org.rascalmpl.uri.HttpURIResolver http = new org.rascalmpl.uri.HttpURIResolver();
		this.resolverRegistry.registerInput(http);
		
		org.rascalmpl.uri.CWDURIResolver cwd = new org.rascalmpl.uri.CWDURIResolver();
		this.resolverRegistry.registerInputOutput(cwd);
		
		org.rascalmpl.uri.ClassResourceInputOutput library = new org.rascalmpl.uri.ClassResourceInputOutput(this.resolverRegistry, "std", this.getClass(), "/org/rascalmpl/library");
		this.resolverRegistry.registerInputOutput(library);
		
		org.rascalmpl.uri.ClassResourceInputOutput testdata = new org.rascalmpl.uri.ClassResourceInputOutput(this.resolverRegistry, "testdata", this.getClass(), "/org/rascalmpl/test/data");
		this.resolverRegistry.registerInput(testdata);
		
		this.resolverRegistry.registerInput(new org.rascalmpl.uri.JarURIResolver(this.getClass()));
		
		this.resolverRegistry.registerInputOutput(this.rascalPathResolver);
		
		org.rascalmpl.uri.HomeURIResolver home = new org.rascalmpl.uri.HomeURIResolver();
		this.resolverRegistry.registerInputOutput(home);
	}  
	
	public List<ClassLoader> getClassLoaders() {
		return Collections.unmodifiableList(classLoaders); 
	}
	
	public org.rascalmpl.parser.Parser __getParser() {
		return parser;
	}

	public org.rascalmpl.interpreter.env.ModuleEnvironment __getRootScope() {
		return rootScope;
	}

	public java.io.PrintWriter getStdOut() {
		return stdout;
	}

	public org.rascalmpl.interpreter.TypeDeclarationEvaluator __getTypeDeclarator() {
		return typeDeclarator;
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment __getHeap() {
		return heap;
	}

	public void __setConcreteListsShouldBeSpliced(
			boolean concreteListsShouldBeSpliced) {
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

	public void __setAccumulators(java.util.Stack<org.rascalmpl.interpreter.Accumulator> accumulators) {
		this.accumulators = accumulators;
	}

	public java.util.Stack<org.rascalmpl.interpreter.Accumulator> __getAccumulators() {
		return accumulators;
	}

	public void __setPatternEvaluator(org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.matching.IMatchingResult> patternEvaluator) {
		this.patternEvaluator = patternEvaluator;
	}

	public org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.matching.IMatchingResult> __getPatternEvaluator() {
		return patternEvaluator;
	}

	public void __setVf(org.eclipse.imp.pdb.facts.IValueFactory vf) {
		this.vf = vf;
	}

	public org.eclipse.imp.pdb.facts.IValueFactory __getVf() {
		return vf;
	}

	public static org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	public org.rascalmpl.interpreter.utils.JavaBridge __getJavaBridge() {
		return javaBridge;
	}

	public void interrupt() {
		this.__setInterrupt(true);
	}
	
	public boolean isInterrupted() {
		return this.__getInterrupt();
	}
	
	public java.io.PrintWriter getStdErr() {
		return this.stderr;
	}
	
	public void setTestResultListener(org.rascalmpl.interpreter.ITestResultListener l) {
		this.testReporter = l;
	}
	
	public org.rascalmpl.interpreter.utils.JavaBridge getJavaBridge(){
		return this.__getJavaBridge();
	}

	public org.rascalmpl.uri.URIResolverRegistry getResolverRegistry() {
		return this.resolverRegistry;
	}
	
	public org.rascalmpl.interpreter.load.RascalURIResolver getRascalResolver() {
		return this.rascalPathResolver;
	}
	
	/**
	 * Call a Rascal function with a number of arguments
	 * @return either null if its a void function, or the return value of the function.
	 */
	public org.eclipse.imp.pdb.facts.IValue call(java.lang.String name, org.eclipse.imp.pdb.facts.IValue...args) {
		org.rascalmpl.ast.QualifiedName qualifiedName = org.rascalmpl.interpreter.utils.Names.toQualifiedName(name);
		org.rascalmpl.interpreter.result.OverloadedFunctionResult func = (org.rascalmpl.interpreter.result.OverloadedFunctionResult) this.getCurrentEnvt().getVariable(qualifiedName);
		
		
		org.eclipse.imp.pdb.facts.type.Type[] types = new org.eclipse.imp.pdb.facts.type.Type[args.length];
		
		if (func == null) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Function " + name + " is unknown");
		}
		
		int i = 0;
		for (org.eclipse.imp.pdb.facts.IValue v : args) {
			types[i++] = v.getType();
		}
		
		return func.call(types, args).getValue();
	}
	
	/**
	 * Parse an object string using the imported SDF modules from the current context.
	 */
	public org.eclipse.imp.pdb.facts.IConstructor parseObject(org.eclipse.imp.pdb.facts.IConstructor startSort, java.net.URI input) {
		try {
			System.err.println("Generating a parser");
			org.rascalmpl.parser.gtd.IGTD parser = this.getObjectParser(this.__getVf().sourceLocation(input));
			java.lang.String name = "";
			if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
				name = "start__";
				startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
			}
			if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
				name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
			}

			this.__setInterrupt(false);
			org.rascalmpl.parser.IActionExecutor exec = new org.rascalmpl.parser.RascalActionExecutor(this, (org.rascalmpl.parser.IParserInfo) parser);
			return parser.parse(name, input, this.resolverRegistry.getInputStream(input), exec);
		} catch (java.io.IOException e) {
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.io(this.__getVf().string(e.getMessage()), this.getCurrentAST(), this.getStackTrace());
		}
	}
	
	public org.eclipse.imp.pdb.facts.IConstructor parseObject(org.eclipse.imp.pdb.facts.IConstructor startSort, java.lang.String input) {
		java.net.URI inputURI = java.net.URI.create("file://-");
		org.rascalmpl.parser.gtd.IGTD parser = this.getObjectParser(this.__getVf().sourceLocation(inputURI));
		java.lang.String name = "";
		if (org.rascalmpl.values.uptr.SymbolAdapter.isStart(startSort)) {
			name = "start__";
			startSort = org.rascalmpl.values.uptr.SymbolAdapter.getStart(startSort);
		}
		if (org.rascalmpl.values.uptr.SymbolAdapter.isSort(startSort)) {
			name += org.rascalmpl.values.uptr.SymbolAdapter.getName(startSort);
		}
		this.__setInterrupt(false);
		org.rascalmpl.parser.IActionExecutor exec = new org.rascalmpl.parser.RascalActionExecutor(this, (org.rascalmpl.parser.IParserInfo) parser);
		return parser.parse(name, inputURI, input, exec);
	}
	
	private org.rascalmpl.parser.gtd.IGTD getObjectParser(org.eclipse.imp.pdb.facts.ISourceLocation loc) {
		return this.getObjectParser( (org.rascalmpl.interpreter.env.ModuleEnvironment) this.getCurrentEnvt().getRoot(), loc);
	}
	
	private org.rascalmpl.parser.gtd.IGTD getObjectParser(org.rascalmpl.interpreter.env.ModuleEnvironment currentModule, org.eclipse.imp.pdb.facts.ISourceLocation loc) {
		if (currentModule.getBootstrap()) {
			return new org.rascalmpl.library.rascal.syntax.ObjectRascalRascal();
		}
		org.rascalmpl.parser.ParserGenerator pg = this.getParserGenerator();
		org.eclipse.imp.pdb.facts.ISet productions = currentModule.getProductions();
		java.lang.Class<org.rascalmpl.parser.gtd.IGTD> parser = this.getHeap().getObjectParser(currentModule.getName(), productions);

		if (parser == null) {
			java.lang.String parserName;
			if (this.__getRootScope() == currentModule) {
				parserName = "__Shell__";
			}
			else {
				parserName = currentModule.getName().replaceAll("::", ".");
			}

			parser = pg.getParser(loc, parserName, productions);
			this.getHeap().storeObjectParser(currentModule.getName(), productions, parser);
		}
	
		try {
			return parser.newInstance();
		} catch (java.lang.InstantiationException e) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError(e.getMessage(), e);
		} catch (java.lang.IllegalAccessException e) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError(e.getMessage(), e);
		}
	}
	
	private org.rascalmpl.parser.gtd.IGTD getRascalParser(org.rascalmpl.interpreter.env.ModuleEnvironment env, java.net.URI input) {
		org.rascalmpl.parser.ParserGenerator pg = this.getParserGenerator();
		org.eclipse.imp.pdb.facts.ISourceLocation loc = this.__getVf().sourceLocation(input);
		org.rascalmpl.parser.gtd.IGTD objectParser = this.getObjectParser(env, loc);
		org.eclipse.imp.pdb.facts.ISet productions = env.getProductions();
		java.lang.Class<org.rascalmpl.parser.gtd.IGTD> parser = this.getHeap().getRascalParser(env.getName(), productions);

		if (parser == null) {
			java.lang.String parserName;
			if (this.__getRootScope() == env) {
				parserName = "__Shell__";
			}
			else {
				parserName = env.getName().replaceAll("::", ".");
			}
			
			parser = pg.getRascalParser(loc, parserName, productions, objectParser);
			this.getHeap().storeRascalParser(env.getName(), productions, parser);
		}
			
		try {
			return parser.newInstance();
		} catch (java.lang.InstantiationException e) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError(e.getMessage(), e);
		} catch (java.lang.IllegalAccessException e) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError(e.getMessage(), e);
		}
	}
	
	private org.rascalmpl.parser.ParserGenerator getParserGenerator() {
		if (this.parserGenerator == null) {
			this.parserGenerator = new org.rascalmpl.parser.ParserGenerator(this.getStdErr(), this.classLoaders, this.getValueFactory());
		}
		return this.parserGenerator;
	}
	
	private void checkPoint(org.rascalmpl.interpreter.env.Environment env) {
		env.checkPoint();
	}

	private void rollback(org.rascalmpl.interpreter.env.Environment env) {
		env.rollback();
	}

	private void commit(org.rascalmpl.interpreter.env.Environment env) {
		env.commit();
	}

	public void setCurrentAST(org.rascalmpl.ast.AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public org.rascalmpl.ast.AbstractAST getCurrentAST() {
		return this.currentAST;
	}

	public void addRascalSearchPathContributor(org.rascalmpl.interpreter.load.IRascalSearchPathContributor contrib) {
		this.rascalPathResolver.addPathContributor(contrib);
	}
	
	public void addRascalSearchPath(final java.net.URI uri) {
		this.rascalPathResolver.addPathContributor(new org.rascalmpl.interpreter.load.IRascalSearchPathContributor() {
			public void contributePaths(java.util.List<java.net.URI> path) {
				path.add(0, uri);
			}
			
			@Override
			public java.lang.String toString() {
				return uri.toString();
			}
		});
	}
	
	public void addClassLoader(java.lang.ClassLoader loader) {
		// later loaders have precedence
		this.classLoaders.add(0, loader);
	}

	public java.lang.String getStackTrace() {
		java.lang.StringBuilder b = new java.lang.StringBuilder();
		org.rascalmpl.interpreter.env.Environment env = this.currentEnvt;
		while (env != null) {
			org.eclipse.imp.pdb.facts.ISourceLocation loc = env.getLocation();
			java.lang.String name = env.getName();
			if (name != null && loc != null) {
				java.net.URI uri = loc.getURI();
				b.append('\t');
				b.append(uri.getRawPath()+ ":" + loc.getBeginLine() + "," + loc.getBeginColumn() + ": " + name);
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
	 * @param stat
	 * @return
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(org.rascalmpl.ast.Statement stat) {
		this.__setInterrupt(false);
		try {
			if(Evaluator.doProfiling){
				this.profiler = new org.rascalmpl.interpreter.utils.Profiler(this);
				this.profiler.start();

			}
			this.currentAST = stat;
			try {
				return stat.__evaluate(this);
			}
			finally {
				if(Evaluator.doProfiling) {
					if (this.profiler != null) {
						this.profiler.pleaseStop();
						this.profiler.report();
					}
				}
			}
		} 
		catch (org.rascalmpl.interpreter.control_exceptions.Return e){
			throw new org.rascalmpl.interpreter.staticErrors.UnguardedReturnError(stat);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Failure e){
			throw new org.rascalmpl.interpreter.staticErrors.UnguardedFailError(stat);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Insert e){
			throw new org.rascalmpl.interpreter.staticErrors.UnguardedInsertError(stat);
		}
	}

	/**
	 * Evaluate an expression
	 * @param expr
	 * @return
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(org.rascalmpl.ast.Expression expr) {
		this.__setInterrupt(false);
		this.currentAST = expr;
		if(Evaluator.doProfiling){
			this.profiler = new org.rascalmpl.interpreter.utils.Profiler(this);
			this.profiler.start();

		}
		try {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = expr.__evaluate(this);
			if(r != null){
				return r;
			}
		}
		finally {
			if(Evaluator.doProfiling){
				if (this.profiler != null) {
					this.profiler.pleaseStop();
					this.profiler.report();
				}
			}
		}

		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(expr.toString());
	}

	/**
	 * Parse and evaluate a command in the current execution environment
	 * @param command
	 * @return
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(java.lang.String command, java.net.URI location){
		this.__setInterrupt(false);
		org.eclipse.imp.pdb.facts.IConstructor tree;
		
		org.rascalmpl.parser.IActionExecutor actionExecutor = new org.rascalmpl.parser.RascalActionExecutor(this, this.__getParser().getInfo());
		
		if(!command.contains("`")){
			tree = this.__getParser().parseCommand(location, command, actionExecutor);
		}else{
			org.rascalmpl.parser.gtd.IGTD rp = this.getRascalParser(this.getCurrentModuleEnvironment(), location);
			tree = rp.parse("start__$Command", location, command, actionExecutor);
		}
		
		org.rascalmpl.ast.Command stat = this.builder.buildCommand(tree);
		if(stat == null){
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Disambiguation failed: it removed all alternatives");
		}
		
		return this.eval(stat);
	}
	
	public org.eclipse.imp.pdb.facts.IConstructor parseCommand(java.lang.String command, java.net.URI location) {
		this.__setInterrupt(false);
		org.rascalmpl.parser.IActionExecutor actionExecutor = new org.rascalmpl.parser.RascalActionExecutor(this, this.__getParser().getInfo());
		
		if(!command.contains("`")){
			return this.__getParser().parseCommand(location, command, actionExecutor);
		}
		
		org.rascalmpl.parser.gtd.IGTD rp = this.getRascalParser(this.getCurrentModuleEnvironment(), location);
		return rp.parse("start__$Command", location, command, actionExecutor);
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(org.rascalmpl.ast.Command command) {
		this.__setInterrupt(false);
		if (Evaluator.doProfiling){
			this.profiler = new org.rascalmpl.interpreter.utils.Profiler(this);
			this.profiler.start();

		}
		try {
			return command.__evaluate(this);
		}
		finally {
			if(Evaluator.doProfiling){
				if (this.profiler != null) {
					this.profiler.pleaseStop();
					this.profiler.report();
				}
			}
		}
	}
	
	/**
	 * Evaluate a declaration
	 * @param declaration
	 * @return
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(org.rascalmpl.ast.Declaration declaration) {
		this.__setInterrupt(false);
		this.currentAST = declaration;
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = declaration.__evaluate(this);
		if(r != null){
			return r;
		}

		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented(declaration.toString());
	}

	/**
	 * Evaluate an import
	 * @param imp
	 * @return
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> eval(org.rascalmpl.ast.Import imp) {
		this.__setInterrupt(false);
		this.currentAST = imp;
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = imp.__evaluate(this);
		if(r != null){
			return r;
		}

		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Not yet implemented: " + imp.getTree());
	}
	
	public void doImport(java.lang.String string) {
		this.eval("import " + string + ";", java.net.URI.create("import:///"));
	}


	public void reloadModule(java.lang.String name, java.net.URI errorLocation) {
		if (!this.__getHeap().existsModule(name)) {
			return;
		}
		
		this.__getHeap().removeModule(this.__getHeap().getModule(name));
		org.rascalmpl.interpreter.env.ModuleEnvironment env =  new org.rascalmpl.interpreter.env.ModuleEnvironment(name);
		this.__getHeap().addModule(env);

		try {
			org.rascalmpl.ast.Module module = this.loadModule(name, env);
	
			if (module != null) {
				if (!this.getModuleName(module).equals(name)) {
					throw new org.rascalmpl.interpreter.staticErrors.ModuleNameMismatchError(this.getModuleName(module), name, this.__getVf().sourceLocation(errorLocation));
				}
				this.__getHeap().setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				module.__evaluate(this);
			}
		}
		catch (org.rascalmpl.interpreter.staticErrors.StaticError e) {
			this.__getHeap().removeModule(env);
			throw e;
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			this.__getHeap().removeModule(env);
			throw e;
		} 
		catch (java.io.IOException e) {
			this.__getHeap().removeModule(env);
			throw new org.rascalmpl.interpreter.staticErrors.ModuleLoadError(name, e.getMessage(), this.__getVf().sourceLocation(errorLocation));
		}
	}
	
	/* First a number of general utility methods */

	/*
	 * Return an evaluation result that is already in normal form,
	 * i.e., all potential rules have already been applied to it.
	 */

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> normalizedResult(org.eclipse.imp.pdb.facts.type.Type t, org.eclipse.imp.pdb.facts.IValue v){
		java.util.Map<org.eclipse.imp.pdb.facts.type.Type, org.eclipse.imp.pdb.facts.type.Type> bindings = this.getCurrentEnvt().getTypeBindings();
		org.eclipse.imp.pdb.facts.type.Type instance;

		if (bindings.size() > 0) {
			instance = t.instantiate(bindings);
		}
		else {
			instance = t;
		}

		if (v != null) {
			this.checkType(v.getType(), instance);
		}
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(instance, v, this);
	}

	public void unwind(org.rascalmpl.interpreter.env.Environment old) {
		// TODO why not just replace the current env with the old one??
		while (this.getCurrentEnvt() != old) {
			this.setCurrentEnvt(this.getCurrentEnvt().getParent());
			this.getCurrentEnvt();
		}
	}

	public void pushEnv() {
		org.rascalmpl.interpreter.env.Environment env = new org.rascalmpl.interpreter.env.Environment(this.getCurrentEnvt(), this.getCurrentEnvt().getName());
		this.setCurrentEnvt(env);
	}

	public org.rascalmpl.interpreter.env.Environment pushEnv(org.rascalmpl.ast.Statement s) {
		/* use the same name as the current envt */
		org.rascalmpl.interpreter.env.Environment env = new org.rascalmpl.interpreter.env.Environment(this.getCurrentEnvt(), s.getLocation(), this.getCurrentEnvt().getName());
		this.setCurrentEnvt(env);
		return env;
	}


	private void checkType(org.eclipse.imp.pdb.facts.type.Type given, org.eclipse.imp.pdb.facts.type.Type expected) {
		if (expected instanceof org.rascalmpl.interpreter.types.FunctionType) {
			return;
		}
		if (!given.isSubtypeOf(expected)){
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(expected, given, this.getCurrentAST());
		}
	}

	public boolean mayOccurIn(org.eclipse.imp.pdb.facts.type.Type small, org.eclipse.imp.pdb.facts.type.Type large) {
		return this.mayOccurIn(small, large, new java.util.HashSet<org.eclipse.imp.pdb.facts.type.Type>());
	}

	boolean mayOccurIn(org.eclipse.imp.pdb.facts.type.Type small, org.eclipse.imp.pdb.facts.type.Type large, java.util.Set<org.eclipse.imp.pdb.facts.type.Type> seen){
		// TODO: this should probably be a visitor as well

		if(small.isVoidType())
			return true;
		if(large.isVoidType())
			return false;
		if(small.isValueType())
			return true;
		if(small.isSubtypeOf(large))
			return true;
		if(large.isListType() || large.isSetType())
			return this.mayOccurIn(small,large.getElementType(), seen);
		if(large.isMapType())
			return this.mayOccurIn(small, large.getKeyType(), seen) ||
			this.mayOccurIn(small, large.getValueType(), seen);
		if(large.isTupleType()){
			for(int i = 0; i < large.getArity(); i++){
				if(this.mayOccurIn(small, large.getFieldType(i), seen))
					return true;
			}
			return false;
		}

		if(large instanceof org.rascalmpl.interpreter.types.NonTerminalType && small instanceof org.rascalmpl.interpreter.types.NonTerminalType){
			//TODO: Until we have more precise info about the types in the concrete syntax
			// we just return true here.
			return true;
		}

		if(large.isConstructorType()){

			for(int i = 0; i < large.getArity(); i++){
				if(this.mayOccurIn(small, large.getFieldType(i), seen))
					return true;
			}
			return false;
		}
		if(large.isAbstractDataType()){
			if(small.isNodeType() && !small.isAbstractDataType())
				return true;
			if(small.isConstructorType() && small.getAbstractDataType().equivalent(large.getAbstractDataType()))
				return true;
			seen.add(large);
			for(org.eclipse.imp.pdb.facts.type.Type alt : this.getCurrentEnvt().lookupAlternatives(large)){				
				if(alt.isConstructorType()){
					for(int i = 0; i < alt.getArity(); i++){
						org.eclipse.imp.pdb.facts.type.Type fType = alt.getFieldType(i);
						if(seen.add(fType) && this.mayOccurIn(small, fType, seen))
							return true;
					}
				} else
					throw new org.rascalmpl.interpreter.asserts.ImplementationError("ADT");

			}
			return false;
		}
		return small.isSubtypeOf(large);
	}




	// Ambiguity ...................................................

	public void printHelpMessage(java.io.PrintWriter out) {
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

	public void printVisibleDeclaredObjects(java.io.PrintWriter out) {
		java.util.List<java.util.Map.Entry<java.lang.String, org.rascalmpl.interpreter.result.OverloadedFunctionResult>> functions = this.getCurrentEnvt().getAllFunctions();
		java.util.Collections.sort(functions, new java.util.Comparator<java.util.Map.Entry<java.lang.String, org.rascalmpl.interpreter.result.OverloadedFunctionResult>>() {
			public int compare(java.util.Map.Entry<java.lang.String, org.rascalmpl.interpreter.result.OverloadedFunctionResult> o1,
					java.util.Map.Entry<java.lang.String, org.rascalmpl.interpreter.result.OverloadedFunctionResult> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		if (functions.size() != 0) {
			out.println("Functions:");

			for (java.util.Map.Entry<java.lang.String, org.rascalmpl.interpreter.result.OverloadedFunctionResult> cand : functions) {
				for (org.rascalmpl.interpreter.result.AbstractFunction func : cand.getValue().iterable()) {
					out.print('\t');
					out.println(func.getHeader());
				}
			}
		}
		

		java.util.List<org.rascalmpl.interpreter.env.RewriteRule> rules = this.getHeap().getRules();
		if (rules.size() != 0) {
			out.println("Rules:");
			for (org.rascalmpl.interpreter.env.RewriteRule rule : rules) {
				out.print('\t');
				out.println(rule.getRule().getPattern().toString());
			}
		}
		
		java.util.Map<java.lang.String, org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> variables = this.getCurrentEnvt().getVariables();
		if (variables.size() != 0) {
			out.println("Variables:");
			for (java.lang.String name : variables.keySet()) {
				out.print('\t');
				org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> value = variables.get(name);
				out.println(value.getType() + " " + name + " = " + value.getValue());
			}
		}
		
		out.flush();
	}
	
	// Modules -------------------------------------------------------------

	public void addImportToCurrentModule(
			org.rascalmpl.ast.AbstractAST x, java.lang.String name) {
		org.rascalmpl.interpreter.env.ModuleEnvironment module = this.__getHeap().getModule(name);
		if (module == null) {
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError(name, x);
		}
		this.getCurrentModuleEnvironment().addImport(name, module);
	}

	public org.rascalmpl.interpreter.env.ModuleEnvironment getCurrentModuleEnvironment() {
		if (!(this.currentEnvt instanceof org.rascalmpl.interpreter.env.ModuleEnvironment)) {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Current env should be a module environment");
		}
		return ((org.rascalmpl.interpreter.env.ModuleEnvironment) this.currentEnvt);
	}

	public java.lang.String getUnescapedModuleName(
			org.rascalmpl.ast.Import.Default x) {
		return org.rascalmpl.interpreter.utils.Names.fullName(x.getModule().getName());
	}

	public void loadParseTreeModule(
			org.rascalmpl.ast.AbstractAST x) {
		java.lang.String parseTreeModName = "ParseTree";
		if (!this.__getHeap().existsModule(parseTreeModName)) {
			this.evalRascalModule(x, parseTreeModName);
		}
		this.addImportToCurrentModule(x, parseTreeModName);
	}

	
	/**
	 * Parse a module. Practical for implementing IDE features or features that use Rascal to implement Rascal.
	 * Parsing a module currently has the side effect of declaring non-terminal types in the given environment.
	 */
	public org.eclipse.imp.pdb.facts.IConstructor parseModule(java.net.URI location, org.rascalmpl.interpreter.env.ModuleEnvironment env) throws IOException {
		char[] data;
		
		java.io.InputStream inputStream = null;
		try {
			inputStream = this.resolverRegistry.getInputStream(location);
			data = this.readModule(inputStream);
		}
		finally{
			if(inputStream != null){
				inputStream.close();
			}
		}

		java.net.URI resolved = this.rascalPathResolver.resolve(location);
		if (resolved != null) {
			location = resolved;
		}
		
		return this.parseModule(data, location, env);
	}
	
	public org.eclipse.imp.pdb.facts.IConstructor parseModule(char[] data, java.net.URI location, org.rascalmpl.interpreter.env.ModuleEnvironment env) {
		this.__setInterrupt(false);
		org.rascalmpl.parser.IActionExecutor actionExecutor = new org.rascalmpl.parser.RascalActionExecutor(this, this.__getParser().getInfo());
		
		org.eclipse.imp.pdb.facts.IConstructor prefix = this.__getParser().preParseModule(location, data, actionExecutor);
		org.rascalmpl.ast.Module preModule = this.builder.buildModule((org.eclipse.imp.pdb.facts.IConstructor) org.rascalmpl.values.uptr.TreeAdapter.getArgs(prefix).get(1));

		// take care of imports and declare syntax
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> name = preModule.__evaluate(this);
		
		if (env == null) {
			env = this.__getHeap().getModule(((org.eclipse.imp.pdb.facts.IString) name.getValue()).getValue());
		}

		org.eclipse.imp.pdb.facts.ISet prods = env.getProductions();
		if (prods.isEmpty() || !containsBackTick(data)) {
			return this.__getParser().parseModule(location, data, actionExecutor);
		}
		
		org.rascalmpl.parser.gtd.IGTD mp = this.needBootstrapParser(preModule) ? new org.rascalmpl.library.rascal.syntax.MetaRascalRascal() : this.getRascalParser(env, location);
		return mp.parse(Parser.START_MODULE, location, data, actionExecutor);
	}
	
	public static boolean containsBackTick(char[] data){
		for(int i = data.length - 1; i >= 0; --i){
			if(data[i] == '`') return true;
		}
		return false;
	}
	
	public boolean needBootstrapParser(org.rascalmpl.ast.Module preModule) {
		for (org.rascalmpl.ast.Tag tag : preModule.getHeader().getTags().getTags()) {
			if (((org.rascalmpl.ast.Name.Lexical) tag.getName()).getString().equals("bootstrapParser")) {
				return true;
			}
		}
		
		return false;
	}
		
	private char[] readModule(java.io.InputStream inputStream) throws IOException{
		char[] buffer = new char[8192];
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
		java.io.InputStreamReader reader = new java.io.InputStreamReader(inputStream);

		int bytesRead;
		while((bytesRead = reader.read(buffer)) != -1){
			writer.write(buffer, 0, bytesRead);
		}
		
		return writer.toCharArray();
	}
	
	protected org.rascalmpl.interpreter.staticErrors.SyntaxError parseError(org.eclipse.imp.pdb.facts.IConstructor tree, java.net.URI location){
		org.rascalmpl.values.errors.SummaryAdapter summary = new org.rascalmpl.values.errors.SummaryAdapter(tree);
		org.rascalmpl.values.errors.SubjectAdapter subject = summary.getInitialSubject();
		org.eclipse.imp.pdb.facts.IValueFactory vf = org.rascalmpl.values.ValueFactoryFactory.getValueFactory();
		
		if (subject != null) {
			org.eclipse.imp.pdb.facts.ISourceLocation loc = vf.sourceLocation(location, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());
			return new org.rascalmpl.interpreter.staticErrors.SyntaxError(subject.getDescription(), loc);
		}
		
		return new org.rascalmpl.interpreter.staticErrors.SyntaxError("unknown location, maybe you used a keyword as an identifier)", vf.sourceLocation(location, 0,1,1,1,0,1));
	}
	
	
	
	private org.rascalmpl.ast.Module loadModule(java.lang.String name, org.rascalmpl.interpreter.env.ModuleEnvironment env) throws IOException {
		try{
			org.eclipse.imp.pdb.facts.IConstructor tree = this.parseModule(java.net.URI.create("rascal:///" + name), env);
			org.rascalmpl.parser.ASTBuilder astBuilder = new org.rascalmpl.parser.ASTBuilder(org.rascalmpl.ast.ASTFactoryFactory.getASTFactory());
			org.rascalmpl.ast.Module moduleAst = astBuilder.buildModule(tree);
			
			if (moduleAst == null) {
				throw new org.rascalmpl.interpreter.asserts.ImplementationError("After this, all ambiguous ast's have been filtered in " + name, astBuilder.getLastSuccessLocation());
			}
			return moduleAst;
		}catch (org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException e){
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Unexpected PDB typecheck exception", e);
		}
	}
	
	public org.rascalmpl.ast.Module evalRascalModule(org.rascalmpl.ast.AbstractAST x, java.lang.String name) {
		org.rascalmpl.interpreter.env.ModuleEnvironment env = this.__getHeap().getModule(name);
		if (env == null) {
			env = new org.rascalmpl.interpreter.env.ModuleEnvironment(name);
			this.__getHeap().addModule(env);
		}
		try {
			org.rascalmpl.ast.Module module = this.loadModule(name, env);
	
			if (module != null) {
				if (!this.getModuleName(module).equals(name)) {
					throw new org.rascalmpl.interpreter.staticErrors.ModuleNameMismatchError(this.getModuleName(module), name, x);
				}
				this.__getHeap().setModuleURI(name, module.getLocation().getURI());
				env.setInitialized(false);
				module.__evaluate(this);
				return module;
			}
		}
		catch (org.rascalmpl.interpreter.staticErrors.StaticError e) {
			this.__getHeap().removeModule(env);
			throw e;
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			this.__getHeap().removeModule(env);
			throw e;
		} 
		catch (java.io.IOException e) {
			this.__getHeap().removeModule(env);
			throw new org.rascalmpl.interpreter.staticErrors.ModuleLoadError(name, e.getMessage(), x);
		}

		this.__getHeap().removeModule(env);
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("Unexpected error while parsing module " + name + " and building an AST for it ", x.getLocation());
	}

	public java.lang.String getModuleName(
			org.rascalmpl.ast.Module module) {
		java.lang.String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	public void visitImports(java.util.List<org.rascalmpl.ast.Import> imports) {
		for (org.rascalmpl.ast.Import i : imports) {
			i.__evaluate(this);
		}
	}

	public org.eclipse.imp.pdb.facts.type.Type evalType(org.rascalmpl.ast.Type type) {
		return new org.rascalmpl.interpreter.TypeEvaluator(this.getCurrentEnvt(), this.__getHeap()).eval(type);
	}

	public boolean hasJavaModifier(org.rascalmpl.ast.FunctionDeclaration func) {
		java.util.List<org.rascalmpl.ast.FunctionModifier> mods = func.getSignature().getModifiers().getModifiers();
		for (org.rascalmpl.ast.FunctionModifier m : mods) {
			if (m.isJava()) {
				return true;
			}
		}

		return false;
	}

	public boolean isWildCard(java.lang.String fieldName){
		return fieldName.equals("_");
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> evalStatementTry(org.rascalmpl.ast.Statement body, java.util.List<org.rascalmpl.ast.Catch> handlers, org.rascalmpl.ast.Statement finallyBody){
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> res = org.rascalmpl.interpreter.result.ResultFactory.nothing();

		try {
			res = body.__evaluate(this);
		} catch (org.rascalmpl.interpreter.control_exceptions.Throw e){
			org.eclipse.imp.pdb.facts.IValue eValue = e.getException();

			boolean handled = false;
			
			for (org.rascalmpl.ast.Catch c : handlers){
				if(c.isDefault()){
					res = c.getBody().__evaluate(this);
					handled = true;
					break;
				} 

				// TODO: Throw should contain Result<IValue> instead of IValue
				if(this.matchAndEval(org.rascalmpl.interpreter.result.ResultFactory.makeResult(eValue.getType(), eValue, this), c.getPattern(), c.getBody())){
					handled = true;
					break;
				}
			}
			
			if (!handled)
				throw e;
		}
		finally {
			if (finallyBody != null) {
				finallyBody.__evaluate(this);
			}
		}
		return res;
	}

	public org.rascalmpl.interpreter.matching.IBooleanResult makeBooleanResult(org.rascalmpl.ast.Expression pat){
		if (pat instanceof org.rascalmpl.ast.Expression.Ambiguity) {
			// TODO: wrong exception here.
			throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) pat.getTree());
		}

		org.rascalmpl.interpreter.BooleanEvaluator pe = new org.rascalmpl.interpreter.BooleanEvaluator(this);
		return pat.__evaluate(pe);
	}

	// Expressions -----------------------------------------------------------

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> evalBooleanExpression(org.rascalmpl.ast.Expression x) {
		org.rascalmpl.interpreter.matching.IBooleanResult mp = this.makeBooleanResult(x);
		mp.init();
		while(mp.hasNext()){
			if (this.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(this.getStackTrace());
			if(mp.next()) {
				return org.rascalmpl.interpreter.result.ResultFactory.bool(true, this);
			}
		}
		return org.rascalmpl.interpreter.result.ResultFactory.bool(false, this);
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> createVisitedDateTime(java.lang.String datePart, java.lang.String timePart,
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		java.lang.String isoDate = datePart;
		if (-1 == datePart.indexOf("-")) {
			isoDate = datePart.substring(0,4) + "-" + datePart.substring(4,6) + "-" + 
			          datePart.substring(6);
		}
		java.lang.String isoTime = timePart;
		if (-1 == timePart.indexOf(":")) {			
			isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2,4) + ":" +
					  timePart.substring(4);
		}
		java.lang.String isoDateTime = isoDate + "T" + isoTime;
		try {
			org.joda.time.DateTime dateAndTime = org.joda.time.format.ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateTime);
			int hourOffset = dateAndTime.getZone().getOffset(dateAndTime.getMillis())/3600000;
			int minuteOffset = (dateAndTime.getZone().getOffset(dateAndTime.getMillis())/60000) % 60;		
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().dateTimeType(),
					this.__getVf().datetime(dateAndTime.getYear(), dateAndTime.getMonthOfYear(), 
							dateAndTime.getDayOfMonth(), dateAndTime.getHourOfDay(), 
							dateAndTime.getMinuteOfHour(), dateAndTime.getSecondOfMinute(),
							dateAndTime.getMillisOfSecond(), hourOffset, minuteOffset), this);
		} catch (java.lang.IllegalArgumentException iae) {
			throw new org.rascalmpl.interpreter.staticErrors.DateTimeParseError("$" + datePart + "T" + timePart, x.getLocation());
		}
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> createVisitedDate(java.lang.String datePart,
			org.rascalmpl.ast.JustDate.Lexical x) {
		java.lang.String isoDate = datePart;
		if (-1 == datePart.indexOf("-")) {
			isoDate = datePart.substring(0,4) + "-" + datePart.substring(4,6) + "-" + 
			          datePart.substring(6);
		}
		try {
			org.joda.time.DateTime justDate = org.joda.time.format.ISODateTimeFormat.dateParser().parseDateTime(isoDate);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().dateTimeType(),
					this.__getVf().date(justDate.getYear(), justDate.getMonthOfYear(), 
							justDate.getDayOfMonth()), this);
		} catch (java.lang.IllegalArgumentException iae) {
			throw new org.rascalmpl.interpreter.staticErrors.DateTimeParseError("$" + datePart, x.getLocation());
		}			
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> createVisitedTime(java.lang.String timePart,
			org.rascalmpl.ast.JustTime.Lexical x) {
		java.lang.String isoTime = timePart;
		if (-1 == timePart.indexOf(":")) {			
			isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2,4) + ":" +
					  timePart.substring(4);
		}
		try {
			org.joda.time.DateTime justTime = org.joda.time.format.ISODateTimeFormat.timeParser().parseDateTime(isoTime);
			int hourOffset = justTime.getZone().getOffset(justTime.getMillis())/3600000;
			int minuteOffset = (justTime.getZone().getOffset(justTime.getMillis())/60000) % 60;		
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().dateTimeType(),
					this.__getVf().time(justTime.getHourOfDay(), justTime.getMinuteOfHour(), justTime.getSecondOfMinute(),
							justTime.getMillisOfSecond(), hourOffset, minuteOffset), this);
		} catch (java.lang.IllegalArgumentException iae) {
			throw new org.rascalmpl.interpreter.staticErrors.DateTimeParseError("$T" + timePart, x.getLocation());
		}						
	}


	public boolean matchAndEval(org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subject, org.rascalmpl.ast.Expression pat, org.rascalmpl.ast.Statement stat){
		boolean debug = false;
		org.rascalmpl.interpreter.env.Environment old = this.getCurrentEnvt();
		this.pushEnv();

		try {
			org.rascalmpl.interpreter.matching.IMatchingResult mp = pat.__evaluate((org.rascalmpl.interpreter.PatternEvaluator)this.__getPatternEvaluator());
			mp.initMatch(subject);
			if(debug)System.err.println("matchAndEval: subject=" + subject + ", pat=" + pat);
			while(mp.hasNext()){
				this.pushEnv();
				if (this.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(this.getStackTrace());
				if(debug)System.err.println("matchAndEval: mp.hasNext()==true");
				if(mp.next()){
					if(debug)System.err.println("matchAndEval: mp.next()==true");
					try {
						this.checkPoint(this.getCurrentEnvt());
						if(debug)System.err.println(stat.toString());
						try {
							stat.__evaluate(this);
						} catch (org.rascalmpl.interpreter.control_exceptions.Insert e){
							// Make sure that the match pattern is set
							if(e.getMatchPattern() == null) {
								e.setMatchPattern(mp);
							}
							throw e;
						}
						this.commit(this.getCurrentEnvt());
						return true;
					} catch (org.rascalmpl.interpreter.control_exceptions.Failure e){
						if(debug) System.err.println("failure occurred");
						this.rollback(this.getCurrentEnvt());
//						 unwind(old); // can not clean up because you don't know how far to roll back
					}
				}
			}
		} finally {
			if(debug)System.err.println("Unwind to old env");
			this.unwind(old);
		}
		return false;
	}

	boolean matchEvalAndReplace(org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subject, 
			org.rascalmpl.ast.Expression pat, 
			java.util.List<org.rascalmpl.ast.Expression> conditions,
			org.rascalmpl.ast.Expression replacementExpr){
		org.rascalmpl.interpreter.env.Environment old = this.getCurrentEnvt();
		try {
			org.rascalmpl.interpreter.matching.IMatchingResult mp = pat.__evaluate((org.rascalmpl.interpreter.PatternEvaluator)this.__getPatternEvaluator());
			mp.initMatch(subject);

			while (mp.hasNext()){
				if (this.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(this.getStackTrace());
				if(mp.next()){
					try {
						boolean trueConditions = true;
						for(org.rascalmpl.ast.Expression cond : conditions){
							if(!cond.__evaluate(this).isTrue()){
								trueConditions = false;
								break;
							}
						}
						if(trueConditions){
							throw new org.rascalmpl.interpreter.control_exceptions.Insert(replacementExpr.__evaluate(this), mp);		
						}
					} catch (org.rascalmpl.interpreter.control_exceptions.Failure e){
						System.err.println("failure occurred");
					}
				}
			}
		} finally {
			this.unwind(old);
		}
		return false;
	}

	private abstract class ComprehensionWriter {
		protected org.eclipse.imp.pdb.facts.type.Type elementType1;
		protected org.eclipse.imp.pdb.facts.type.Type elementType2;
		protected org.eclipse.imp.pdb.facts.type.Type resultType;
		protected java.util.List<org.rascalmpl.ast.Expression> resultExprs;
		protected org.eclipse.imp.pdb.facts.IWriter writer;
		protected org.rascalmpl.interpreter.Evaluator ev;

		ComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev){
			this.ev = ev;
			this.resultExprs = resultExprs;
			this.writer = null;
		}

		public void check(org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r, org.eclipse.imp.pdb.facts.type.Type t, java.lang.String kind, org.rascalmpl.ast.Expression expr){
			if(!r.getType().isSubtypeOf(t)){
				throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(t, r.getType() ,
						expr);
			}
		}

		public org.rascalmpl.interpreter.IEvaluatorContext getContext(org.rascalmpl.ast.AbstractAST ast) {
			Evaluator.this.setCurrentAST(ast);
			return Evaluator.this;
		}

		public abstract void append();


		public abstract org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> done();
	}

	public class ListComprehensionWriter extends
	org.rascalmpl.interpreter.Evaluator.ComprehensionWriter {

		private boolean splicing[];
		private org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> rawElements[];
		
		@SuppressWarnings("unchecked")
		public
		ListComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			this.splicing = new boolean[resultExprs.size()];
			this.rawElements = new org.rascalmpl.interpreter.result.Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements first, and whether or not to splice them, and evaluate them
			if(this.writer == null) {
				int k = 0;
				this.elementType1 = org.rascalmpl.interpreter.Evaluator.__getTf().voidType();
				
				for(org.rascalmpl.ast.Expression resExpr : this.resultExprs){
					this.rawElements[k] = resExpr.__evaluate(this.ev);
					org.eclipse.imp.pdb.facts.type.Type elementType = this.rawElements[k].getType();
					
					if (elementType.isListType() && !resExpr.isList()){
						elementType = elementType.getElementType();
						this.splicing[k] = true;
					} 
					else {
						this.splicing[k] = false;
					}
					this.elementType1 = this.elementType1.lub(elementType);
					k++;
				}
				
				this.resultType = org.rascalmpl.interpreter.Evaluator.__getTf().listType(this.elementType1);		
				this.writer = this.resultType.writer(Evaluator.this.__getVf());
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (org.rascalmpl.ast.Expression resExpr : this.resultExprs) {
					this.rawElements[k++] = resExpr.__evaluate(this.ev);
				}
			}
			
			// here we finally add the elements
			int k = 0;
			for (org.rascalmpl.ast.Expression resExpr : this.resultExprs) {
				if(this.splicing[k]){
					/*
					 * Splice elements of the value of the result expression in the result list
					 */
					if (!this.rawElements[k].getType().getElementType().isSubtypeOf(this.elementType1)) {
						throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(this.elementType1, this.rawElements[k].getType().getElementType(), resExpr);
					}
					
					for(org.eclipse.imp.pdb.facts.IValue val : ((org.eclipse.imp.pdb.facts.IList) this.rawElements[k].getValue())){
						((org.eclipse.imp.pdb.facts.IListWriter) this.writer).append(val);
					}
				} else {
					this.check(this.rawElements[k], this.elementType1, "list", resExpr);
					((org.eclipse.imp.pdb.facts.IListWriter) this.writer).append(this.rawElements[k].getValue());
				}
				k++;
			}
		}

		@Override
		public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> done() {
			return (this.writer == null) ? org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().listType(org.rascalmpl.interpreter.Evaluator.__getTf().voidType()), Evaluator.this.__getVf().list(), this.getContext(this.resultExprs.get(0))) : 
				org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().listType(this.elementType1), this.writer.done(), this.getContext(this.resultExprs.get(0)));
		}
	}

	public class SetComprehensionWriter extends
	org.rascalmpl.interpreter.Evaluator.ComprehensionWriter {
		private boolean splicing[];
		private org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> rawElements[];
		
		@SuppressWarnings("unchecked")
		public
		SetComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			this.splicing = new boolean[resultExprs.size()];
			this.rawElements = new org.rascalmpl.interpreter.result.Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements first, and whether or not to splice them, and evaluate them
			if(this.writer == null) {
				int k = 0;
				this.elementType1 = org.rascalmpl.interpreter.Evaluator.__getTf().voidType();
				
				for(org.rascalmpl.ast.Expression resExpr : this.resultExprs){
					this.rawElements[k] = resExpr.__evaluate(this.ev);
					org.eclipse.imp.pdb.facts.type.Type elementType = this.rawElements[k].getType();
				
					if (elementType.isSetType() && !resExpr.isSet()){
						elementType = elementType.getElementType();
						this.splicing[k] = true;
					} 
					else {
						this.splicing[k] = false;
					}
					this.elementType1 = this.elementType1.lub(elementType);
					k++;
				}
				
				this.resultType = org.rascalmpl.interpreter.Evaluator.__getTf().setType(this.elementType1);		
				this.writer = this.resultType.writer(Evaluator.this.__getVf());
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (org.rascalmpl.ast.Expression resExpr : this.resultExprs) {
					this.rawElements[k++] = resExpr.__evaluate(this.ev);
				}
			}
			
			// here we finally add the elements
			int k = 0;
			for (org.rascalmpl.ast.Expression resExpr : this.resultExprs) {
				if(this.splicing[k]){
					/*
					 * Splice elements of the value of the result expression in the result list
					 */
					if (!this.rawElements[k].getType().getElementType().isSubtypeOf(this.elementType1)) {
						throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(this.elementType1, this.rawElements[k].getType().getElementType(), resExpr);
					}
					
					for(org.eclipse.imp.pdb.facts.IValue val : ((org.eclipse.imp.pdb.facts.ISet) this.rawElements[k].getValue())){
						((org.eclipse.imp.pdb.facts.ISetWriter) this.writer).insert(val);
					}
				} else {
					this.check(this.rawElements[k], this.elementType1, "list", resExpr);
					((org.eclipse.imp.pdb.facts.ISetWriter) this.writer).insert(this.rawElements[k].getValue());
				}
				k++;
			}
		}

		@Override
		public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> done() {
			return (this.writer == null) ? org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().setType(org.rascalmpl.interpreter.Evaluator.__getTf().voidType()), Evaluator.this.__getVf().set(), this.getContext(this.resultExprs.get(0))) : 
				org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().setType(this.elementType1), this.writer.done(), this.getContext(this.resultExprs.get(0)));
		}
	}

	public class MapComprehensionWriter extends
	org.rascalmpl.interpreter.Evaluator.ComprehensionWriter {

		public MapComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			if(resultExprs.size() != 2)
				throw new org.rascalmpl.interpreter.asserts.ImplementationError("Map comprehensions needs two result expressions");
		}

		@Override
		public void append() {
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r1 = this.resultExprs.get(0).__evaluate(this.ev);
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r2 = this.resultExprs.get(1).__evaluate(this.ev);
			if (this.writer == null) {
				this.elementType1 = r1.getType();
				this.elementType2 = r2.getType();
				this.resultType = org.rascalmpl.interpreter.Evaluator.__getTf().mapType(this.elementType1, this.elementType2);
				this.writer = this.resultType.writer(Evaluator.this.__getVf());
			}
			this.check(r1, this.elementType1, "map", this.resultExprs.get(0));
			this.check(r2, this.elementType2, "map", this.resultExprs.get(1));
			((org.eclipse.imp.pdb.facts.IMapWriter) this.writer).put(r1.getValue(), r2.getValue());
		}

		@Override
		public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> done() {
			return (this.writer == null) ? 
					org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().mapType(org.rascalmpl.interpreter.Evaluator.__getTf().voidType(), org.rascalmpl.interpreter.Evaluator.__getTf().voidType()), Evaluator.this.__getVf().map(org.rascalmpl.interpreter.Evaluator.__getTf().voidType(), org.rascalmpl.interpreter.Evaluator.__getTf().voidType()), this.getContext(this.resultExprs.get(0)))
					: org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().mapType(this.elementType1, this.elementType2), this.writer.done(), this.getContext(this.resultExprs.get(0)));
		}
	}


	public static final org.rascalmpl.ast.Name IT = org.rascalmpl.ast.ASTFactoryFactory.getASTFactory().makeNameLexical(null, "<it>");
	private org.rascalmpl.parser.ParserGenerator parserGenerator;
	
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> evalReducer(org.rascalmpl.ast.Expression init, org.rascalmpl.ast.Expression result, java.util.List<org.rascalmpl.ast.Expression> generators) {
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = this.getCurrentEnvt();
		int i = 0;
		
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> it = init.__evaluate(this);

		try {
			gens[0] = this.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = this.getCurrentEnvt();
			this.pushEnv();

			while (i >= 0 && i < size) {
				if (this.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(this.getStackTrace());
				if (gens[i].hasNext() && gens[i].next()) {
					if(i == size - 1){
						this.getCurrentEnvt().storeVariable(Evaluator.IT, it);
						it = result.__evaluate(this);
						this.unwind(olds[i]);
						this.pushEnv();
					} 
					else {
						i++;
						gens[i] = this.makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = this.getCurrentEnvt();
						this.pushEnv();
					}
				} else {
					this.unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			this.unwind(old);
		}
		return it;
	
	}

	/*
	 * The common comprehension evaluator
	 */

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> evalComprehension(java.util.List<org.rascalmpl.ast.Expression> generators, 
			org.rascalmpl.interpreter.Evaluator.ComprehensionWriter w){
		int size = generators.size();
		org.rascalmpl.interpreter.matching.IBooleanResult[] gens = new org.rascalmpl.interpreter.matching.IBooleanResult[size];
		org.rascalmpl.interpreter.env.Environment[] olds = new org.rascalmpl.interpreter.env.Environment[size];
		org.rascalmpl.interpreter.env.Environment old = this.getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = this.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = this.getCurrentEnvt();
			this.pushEnv();

			while (i >= 0 && i < size) {
				if (this.__getInterrupt()) throw new org.rascalmpl.interpreter.control_exceptions.InterruptException(this.getStackTrace());
				if (gens[i].hasNext() && gens[i].next()) {
					if(i == size - 1){
						w.append();
						this.unwind(olds[i]);
						this.pushEnv();
					} 
					else {
						i++;
						gens[i] = this.makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = this.getCurrentEnvt();
						this.pushEnv();
					}
				} else {
					this.unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			this.unwind(old);
		}
		return w.done();
	}

	public void updateProperties(){
		Evaluator.doProfiling = org.rascalmpl.interpreter.Configuration.getProfilingProperty();

		org.rascalmpl.interpreter.result.AbstractFunction.setCallTracing(org.rascalmpl.interpreter.Configuration.getTracingProperty());
	}

	public java.util.Stack<org.rascalmpl.interpreter.env.Environment> getCallStack() {
		java.util.Stack<org.rascalmpl.interpreter.env.Environment> stack = new java.util.Stack<org.rascalmpl.interpreter.env.Environment>();
		org.rascalmpl.interpreter.env.Environment env = this.currentEnvt;
		while (env != null) {
			stack.add(0, env);
			env = env.getCallerScope();
		}
		return stack;
	}

	public org.rascalmpl.interpreter.env.Environment getCurrentEnvt() {
		return this.currentEnvt;
	}

	public void setCurrentEnvt(org.rascalmpl.interpreter.env.Environment env) {
		this.currentEnvt = env;
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this;
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment getHeap() {
		return this.__getHeap();
	}

	public boolean runTests(){
		final boolean[] allOk = new boolean[] { true };
		final org.rascalmpl.interpreter.ITestResultListener l = this.testReporter != null ? this.testReporter : new org.rascalmpl.interpreter.DefaultTestResultListener(this.getStdOut());
		
		new org.rascalmpl.interpreter.TestEvaluator(this, new org.rascalmpl.interpreter.ITestResultListener() {
			public void report(boolean successful, java.lang.String test, org.eclipse.imp.pdb.facts.ISourceLocation loc, java.lang.Throwable t) {
				if (!successful) allOk[0] = false;
				l.report(successful, test, loc, t);
			}
			
			public void report(boolean successful, java.lang.String test, org.eclipse.imp.pdb.facts.ISourceLocation loc) {
				if (!successful) allOk[0] = false;
				l.report(successful, test, loc);
			}

			public void done() {l.done();}
			public void start(int count) {l.start(count);}
		}).test();
		return allOk[0];
	}

	public org.eclipse.imp.pdb.facts.IValueFactory getValueFactory() {
		return this.__getVf();
	}

	public void setIValueFactory(
			org.eclipse.imp.pdb.facts.IValueFactory factory) {
		this.__setVf(factory);
	}

	public org.rascalmpl.interpreter.strategy.IStrategyContext getStrategyContext(){
		return this.strategyContextStack.getCurrentContext();
	}

	public void pushStrategyContext(org.rascalmpl.interpreter.strategy.IStrategyContext strategyContext){
		this.strategyContextStack.pushContext(strategyContext);
	}

	public void popStrategyContext(){
		this.strategyContextStack.popContext();
	}

	public void setAccumulators(org.rascalmpl.interpreter.Accumulator accu) {
		this.__getAccumulators().push(accu);
	}

	public java.util.Stack<org.rascalmpl.interpreter.Accumulator> getAccumulators() {
		return this.__getAccumulators();
	}

	public void setAccumulators(java.util.Stack<org.rascalmpl.interpreter.Accumulator> accumulators) {
		this.__setAccumulators(accumulators);
	}
	
	public void appendToString(org.eclipse.imp.pdb.facts.IValue value, java.lang.StringBuilder b)
	{
		if (value.getType() == Factory.Tree) {
			b.append(org.rascalmpl.values.uptr.TreeAdapter.yield((org.eclipse.imp.pdb.facts.IConstructor) value));
		}
		else if (value.getType().isStringType()) {
			b.append(((org.eclipse.imp.pdb.facts.IString) value).getValue());
		}
		else {
			b.append(value.toString());
		}
	}

	
	

	
}
