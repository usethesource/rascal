package org.rascalmpl.interpreter;

import static org.rascalmpl.interpreter.result.ResultFactory.bool;
import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;
import static org.rascalmpl.interpreter.result.ResultFactory.nothing;
import static org.rascalmpl.interpreter.utils.Utils.unescape;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.Bound;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Catch;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.Label;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.ShellCommand;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Strategy;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.ast.Assignable.Constructor;
import org.rascalmpl.ast.Assignable.FieldAccess;
import org.rascalmpl.ast.Command.Shell;
import org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral;
import org.rascalmpl.ast.DateTimeLiteral.DateLiteral;
import org.rascalmpl.ast.DateTimeLiteral.TimeLiteral;
import org.rascalmpl.ast.Declaration.Alias;
import org.rascalmpl.ast.Declaration.Annotation;
import org.rascalmpl.ast.Declaration.Data;
import org.rascalmpl.ast.Declaration.DataAbstract;
import org.rascalmpl.ast.Declaration.Function;
import org.rascalmpl.ast.Declaration.Rule;
import org.rascalmpl.ast.Declaration.Tag;
import org.rascalmpl.ast.Declaration.Test;
import org.rascalmpl.ast.Declaration.Variable;
import org.rascalmpl.ast.Declaration.View;
import org.rascalmpl.ast.Expression.Addition;
import org.rascalmpl.ast.Expression.All;
import org.rascalmpl.ast.Expression.Ambiguity;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.Any;
import org.rascalmpl.ast.Expression.Bracket;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.Composition;
import org.rascalmpl.ast.Expression.Comprehension;
import org.rascalmpl.ast.Expression.Division;
import org.rascalmpl.ast.Expression.Equivalence;
import org.rascalmpl.ast.Expression.FieldProject;
import org.rascalmpl.ast.Expression.FieldUpdate;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.Guarded;
import org.rascalmpl.ast.Expression.IfDefinedOtherwise;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
import org.rascalmpl.ast.Expression.Intersection;
import org.rascalmpl.ast.Expression.IsDefined;
import org.rascalmpl.ast.Expression.It;
import org.rascalmpl.ast.Expression.Join;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.Expression.Lexical;
import org.rascalmpl.ast.Expression.List;
import org.rascalmpl.ast.Expression.Literal;
import org.rascalmpl.ast.Expression.Match;
import org.rascalmpl.ast.Expression.Modulo;
import org.rascalmpl.ast.Expression.Negation;
import org.rascalmpl.ast.Expression.Negative;
import org.rascalmpl.ast.Expression.NoMatch;
import org.rascalmpl.ast.Expression.NonEmptyBlock;
import org.rascalmpl.ast.Expression.NotIn;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.Product;
import org.rascalmpl.ast.Expression.Range;
import org.rascalmpl.ast.Expression.Reducer;
import org.rascalmpl.ast.Expression.ReifiedType;
import org.rascalmpl.ast.Expression.ReifyType;
import org.rascalmpl.ast.Expression.Set;
import org.rascalmpl.ast.Expression.StepRange;
import org.rascalmpl.ast.Expression.Subscript;
import org.rascalmpl.ast.Expression.Subtraction;
import org.rascalmpl.ast.Expression.TransitiveClosure;
import org.rascalmpl.ast.Expression.TransitiveReflexiveClosure;
import org.rascalmpl.ast.Expression.Tuple;
import org.rascalmpl.ast.Expression.TypedVariable;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.FunctionDeclaration.Abstract;
import org.rascalmpl.ast.Header.Parameters;
import org.rascalmpl.ast.Import.Syntax;
import org.rascalmpl.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.rascalmpl.ast.IntegerLiteral.HexIntegerLiteral;
import org.rascalmpl.ast.IntegerLiteral.OctalIntegerLiteral;
import org.rascalmpl.ast.Literal.Boolean;
import org.rascalmpl.ast.Literal.Integer;
import org.rascalmpl.ast.Literal.Location;
import org.rascalmpl.ast.Literal.Real;
import org.rascalmpl.ast.Literal.RegExp;
import org.rascalmpl.ast.LocalVariableDeclaration.Default;
import org.rascalmpl.ast.PathTail.Mid;
import org.rascalmpl.ast.PatternWithAction.Arbitrary;
import org.rascalmpl.ast.PatternWithAction.Replacing;
import org.rascalmpl.ast.ProtocolPart.Interpolated;
import org.rascalmpl.ast.ProtocolPart.NonInterpolated;
import org.rascalmpl.ast.ProtocolTail.Post;
import org.rascalmpl.ast.ShellCommand.Edit;
import org.rascalmpl.ast.ShellCommand.Help;
import org.rascalmpl.ast.ShellCommand.ListDeclarations;
import org.rascalmpl.ast.ShellCommand.Quit;
import org.rascalmpl.ast.ShellCommand.Unimport;
import org.rascalmpl.ast.Statement.Append;
import org.rascalmpl.ast.Statement.Assert;
import org.rascalmpl.ast.Statement.AssertWithMessage;
import org.rascalmpl.ast.Statement.Assignment;
import org.rascalmpl.ast.Statement.Break;
import org.rascalmpl.ast.Statement.Continue;
import org.rascalmpl.ast.Statement.DoWhile;
import org.rascalmpl.ast.Statement.EmptyStatement;
import org.rascalmpl.ast.Statement.Fail;
import org.rascalmpl.ast.Statement.For;
import org.rascalmpl.ast.Statement.GlobalDirective;
import org.rascalmpl.ast.Statement.IfThen;
import org.rascalmpl.ast.Statement.IfThenElse;
import org.rascalmpl.ast.Statement.Insert;
import org.rascalmpl.ast.Statement.Solve;
import org.rascalmpl.ast.Statement.Switch;
import org.rascalmpl.ast.Statement.Throw;
import org.rascalmpl.ast.Statement.Try;
import org.rascalmpl.ast.Statement.TryFinally;
import org.rascalmpl.ast.Statement.VariableDeclaration;
import org.rascalmpl.ast.Statement.While;
import org.rascalmpl.ast.Test.Labeled;
import org.rascalmpl.ast.Test.Unlabeled;
import org.rascalmpl.ast.Toplevel.GivenVisibility;
import org.rascalmpl.ast.Visit.DefaultStrategy;
import org.rascalmpl.ast.Visit.GivenStrategy;
import org.rascalmpl.interpreter.TraversalEvaluator.DIRECTION;
import org.rascalmpl.interpreter.TraversalEvaluator.FIXEDPOINT;
import org.rascalmpl.interpreter.TraversalEvaluator.PROGRESS;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.env.RewriteRule;
import org.rascalmpl.interpreter.load.FromDefinedSdfSearchPathPathContributor;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.ISdfSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalURIResolver;
import org.rascalmpl.interpreter.load.SDFSearchPath;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.BoolResult;
import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.AmbiguousConcretePattern;
import org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop;
import org.rascalmpl.interpreter.staticErrors.DateTimeParseError;
import org.rascalmpl.interpreter.staticErrors.ItOutsideOfReducer;
import org.rascalmpl.interpreter.staticErrors.JavaMethodLinkError;
import org.rascalmpl.interpreter.staticErrors.MissingModifierError;
import org.rascalmpl.interpreter.staticErrors.ModuleLoadError;
import org.rascalmpl.interpreter.staticErrors.ModuleNameMismatchError;
import org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;
import org.rascalmpl.interpreter.staticErrors.UnguardedInsertError;
import org.rascalmpl.interpreter.staticErrors.UnguardedReturnError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.strategy.StrategyContextStack;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.utils.Utils;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.RascalParser;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.uri.CWDURIResolver;
import org.rascalmpl.uri.ClassResourceInputStreamResolver;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.HttpURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.errors.SubjectAdapter;
import org.rascalmpl.values.errors.SummaryAdapter;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ParsetreeAdapter;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Evaluator extends NullASTVisitor<Result<IValue>> implements IEvaluator<Result<IValue>> {
	private IValueFactory vf;
	private static final TypeFactory tf = TypeFactory.getInstance();
	protected Environment currentEnvt;
	private StrategyContextStack strategyContextStack;

	protected final GlobalEnvironment heap;

	private final JavaBridge javaBridge;

	private AbstractAST currentAST; 	// used in runtime errormessages

	private static boolean doProfiling = false;
	private Profiler profiler;
	
	private boolean saveParsedModules = false;

	private final TypeDeclarationEvaluator typeDeclarator;
	protected IEvaluator<IMatchingResult> patternEvaluator;

	private final java.util.List<ClassLoader> classLoaders;
	protected final ModuleEnvironment rootScope;
	private boolean concreteListsShouldBeSpliced;
	private final RascalParser parser;

	private PrintWriter stderr;
	private PrintWriter stdout;

	private Stack<Accumulator> accumulators = new Stack<Accumulator>();
	private final RascalURIResolver resolver;
	private final SDFSearchPath sdf;
	private final ASTBuilder builder;

	public Evaluator(IValueFactory f, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap) {
		this.vf = f;
		this.patternEvaluator = new PatternEvaluator(this);
		this.strategyContextStack = new StrategyContextStack();
		this.heap = heap;
		this.typeDeclarator = new TypeDeclarationEvaluator(this);
		this.currentEnvt = scope;
		this.rootScope = scope;
		this.heap.addModule(scope);
		this.classLoaders = new ArrayList<ClassLoader>();
		this.javaBridge = new JavaBridge(stderr, classLoaders, vf);
		this.resolver = new RascalURIResolver();
		this.sdf = new SDFSearchPath();
		this.parser = new RascalParser();
		this.stderr = stderr;
		this.stdout = stdout;
		this.builder = new ASTBuilder(new ASTFactory());

		updateProperties();
		
		if (stderr == null) {
			throw new NullPointerException();
		}
		if (stdout == null) {
			throw new NullPointerException();
		}

		resolver.addPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(java.util.List<URI> l) {
				l.add(URI.create("cwd:///"));
				l.add(URI.create("stdlib:///org/rascalmpl/library"));
				l.add(URI.create("stdlib:///"));
				l.add(URI.create("stdlib:///org/rascalmpl/test/data"));

				String property = System.getProperty("rascal.path");

				if (property != null) {
					for (String path : property.split(":")) {
						l.add(URI.create("file://" + path));
					}
				}
			}
			@Override
			public String toString() {
				return "[current wd and stdlib]";
			}
		});

		// add current wd and sdf-library to search path for SDF modules
		sdf.addSdfSearchPathContributor(new ISdfSearchPathContributor() {
			public java.util.List<String> contributePaths() {
				java.util.List<String> result = new ArrayList<String>();
				result.add(new File("/Users/jurgenv/Sources/Rascal/rascal/src/org/rascalmpl/library").getAbsolutePath());
//				result.add(new File("/Users/mhills/Projects/rascal/build/rascal/src/org/rascalmpl/library").getAbsolutePath());
				result.add(Configuration.getSdfLibraryPathProperty());
				result.add(new File(System.getProperty("user.dir"), "src/org/rascalmpl/test/data").getAbsolutePath());
				return result;
			}
		});

		// adds folders using -Drascal.sdf.path=/colon-separated/path
		sdf.addSdfSearchPathContributor(new FromDefinedSdfSearchPathPathContributor());

		// load Java classes from the current jar (for the standard library)
		classLoaders.add(getClass().getClassLoader());

		// register some schemes
		URIResolverRegistry registry = URIResolverRegistry.getInstance();
		FileURIResolver files = new FileURIResolver(); 
		registry.registerInput(files.scheme(), files);
		registry.registerOutput(files.scheme(), files);

		HttpURIResolver http = new HttpURIResolver();
		registry.registerInput(http.scheme(), http);

		CWDURIResolver cwd = new CWDURIResolver();
		registry.registerInput(cwd.scheme(), cwd);
		registry.registerOutput(cwd.scheme(), cwd);
		
		ClassResourceInputStreamResolver library = new ClassResourceInputStreamResolver("stdlib", this.getClass());
		registry.registerInput(library.scheme(), library);

		registry.registerInput(resolver.scheme(), resolver);
		registry.registerOutput(resolver.scheme(), resolver);
	}
	
	public SDFSearchPath getSDFSearchPath() {
		return sdf;
	}
	
	public JavaBridge getJavaBridge(){
		return javaBridge;
	}

	public IValue call(String name, IValue...args) {
		QualifiedName qualifiedName = Names.toQualifiedName(name);
		OverloadedFunctionResult func = (OverloadedFunctionResult) getCurrentEnvt().getVariable(qualifiedName);
		
		
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
	 * Parse an object string using the imported SDF modules from the current context.
	 */
	public IConstructor parseObject(IConstructor startSort, URI input) {
		try {
			return filterStart(startSort, parser.parseStream(sdf.getSdfSearchPath(), ((ModuleEnvironment) getCurrentEnvt().getRoot()).getSDFImports(), URIResolverRegistry.getInstance().getInputStream(input)));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), getCurrentAST(), getStackTrace());
		} catch (SyntaxError e) {
			throw RuntimeExceptionFactory.parseError(e.getLocation(), getCurrentAST(), getStackTrace());
		}
	}
	
	/**
	 * Parse an object string using the imported SDF modules from the current context.
	 */
	public IConstructor parseObject(IConstructor startSort, java.lang.String input) {
		try {
			return filterStart(startSort, parser.parseString(sdf.getSdfSearchPath(), ((ModuleEnvironment) getCurrentEnvt().getRoot()).getSDFImports(), input));
		} catch (IOException e) {
			throw new ImplementationError("unexpected io exception", e);
		} catch (SyntaxError e) {
			throw RuntimeExceptionFactory.parseError(e.getLocation(), getCurrentAST(), getStackTrace());
		}
	}
	
	public IValue parseObjectExperimental(IConstructor startSort, java.lang.String sentence) {
		IGLL parser = getObjectParser();
		return parser.parse(startSort, sentence);
	}

	private IGLL getObjectParser() {
		// TODO: refactor pg to field
		try {
			ParserGenerator pg = getParserGenerator();
			ModuleEnvironment currentModule = (ModuleEnvironment) getCurrentEnvt().getRoot();
			
			if (heap.existsModule(currentModule.getName())) {
				// TODO: add support for modular grammars (this only uses the syntax definition of the current module
				IConstructor moduleTree = parseModule(URI.create("rascal:///" + currentModule.getName()), currentModule);
				return pg.getParser(getCurrentAST().getLocation(), currentModule.getName().replaceAll("::", "."), moduleTree);
			}
			
			throw RuntimeExceptionFactory.io(getValueFactory().string("can not parse yet from outside the module where the syntax is defined"), getCurrentAST(), getStackTrace());
		} catch (IOException e) {
			throw new ImplementationError("unexpected io exception", e);
		}
	}

	private ParserGenerator getParserGenerator() {
		if (parserGenerator == null) {
			parserGenerator = new ParserGenerator(stdout, classLoaders, getValueFactory());
		}
		return parserGenerator;
	}

	private IConstructor filterStart(IConstructor startSort, IConstructor ptree) {
		ptree = ParsetreeAdapter.addPositionInformation(ptree, URI.create("stdin:///"));
		IConstructor tree = (IConstructor) TreeAdapter.getArgs(ParsetreeAdapter.getTop(ptree)).get(1);

		if (TreeAdapter.isAppl(tree)) {
			IConstructor prod = TreeAdapter.getProduction(tree);
			IConstructor rhs = ProductionAdapter.getRhs(prod);

			if (!rhs.isEqual(startSort)) {
				throw RuntimeExceptionFactory.parseError(TreeAdapter.getLocation(tree), null, null);
			}

			return tree;
		}
		else if (TreeAdapter.isAmb(tree)) {
			for (IValue alt : TreeAdapter.getAlternatives(tree)) {
				IConstructor prod = TreeAdapter.getProduction((IConstructor) alt);
				IConstructor rhs = ProductionAdapter.getRhs(prod);

				if (rhs.isEqual(startSort)) {
					return (IConstructor) alt;
				}
			}

			throw RuntimeExceptionFactory.parseError(TreeAdapter.getLocation(tree), null, null);
		}
		
		throw new ImplementationError("unexpected tree type: " + tree.getType());
	}

	private void checkPoint(Environment env) {
		env.checkPoint();
	}

	private void rollback(Environment env) {
		env.rollback();
	}

	private void commit(Environment env) {
		env.commit();
	}

	public void setCurrentAST(AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public AbstractAST getCurrentAST() {
		return currentAST;
	}

	public void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
		resolver.addPathContributor(contrib);
	}
	
	public void addRascalSearchPath(final URI uri) {
		resolver.addPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(java.util.List<URI> path) {
				path.add(0, uri);
			}
			
			@Override
			public String toString() {
				return uri.toString();
			}
		});
	}
	
	public void addSdfSearchPathContributor(ISdfSearchPathContributor contrib) {
		sdf.addSdfSearchPathContributor(contrib);
	}

	public void addClassLoader(ClassLoader loader) {
		// later loaders have precedence
		classLoaders.add(0, loader);
	}

	public String getStackTrace() {
		StringBuilder b = new StringBuilder();
		Environment env = currentEnvt;
		while (env != null) {
			ISourceLocation loc = env.getLocation();
			String name = env.getName();
			if (name != null && loc != null) {
				URI uri = loc.getURI();
				b.append('\t');
				b.append(uri.getRawPath()+ ":" + loc.getBeginLine() + "," + loc.getBeginColumn() + ": " + name);
				b.append('\n');
			}
			env = env.getParent();
		}
		return b.toString();
	}

	/**
	 * Evaluate a statement
	 * @param stat
	 * @return
	 */
	public Result<IValue> eval(Statement stat) {
		try {
			if(doProfiling){
				profiler = new Profiler(this);
				profiler.start();

			}
			currentAST = stat;
			try {
				return stat.accept(this);
			}
			finally {
				if(doProfiling){
					profiler.pleaseStop();
					profiler.report();
				}
			}
		} catch (Return e){
			throw new UnguardedReturnError(stat);
		}
		catch (Failure e){
			throw new UnguardedFailError(stat);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Insert e){
			throw new UnguardedInsertError(stat);
		}
	}

	/**
	 * Evaluate an expression
	 * @param expr
	 * @return
	 */
	public Result<IValue> eval(Expression expr) {
		currentAST = expr;
		Result<IValue> r = expr.accept(this);
		if(r != null){
			return r;
		}

		throw new NotYetImplemented(expr.toString());
	}

	private java.util.Set<String> getSDFImports() {
		return ((ModuleEnvironment) getCurrentEnvt().getRoot()).getSDFImports();
	}
	
	/**
	 * Parse and evaluate a command in the current execution environment
	 * @param command
	 * @return
	 */
	public Result<IValue> eval(String command, URI location) {
		try {
			IConstructor tree = parser.parseCommand(getSDFImports(), sdf.getSdfSearchPath(), location, command);
			
			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw parseError(tree, location);
			}
			
			Command stat = builder.buildCommand(tree);
			
			if (stat == null) {
				throw new ImplementationError("Disambiguation failed: it removed all alternatives");
			}
			
			return eval(stat);
		} catch (IOException e) {
			throw new ImplementationError("something weird happened", e);
		}
	}
	
	public IConstructor parseCommand(String command, URI location) {
		IConstructor tree;
		try {
			tree = parser.parseCommand(getSDFImports(), sdf.getSdfSearchPath(), location, command);
			
			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw parseError(tree, location);
			}
			
			return tree;
		} catch (IOException e) {
			throw new ImplementationError("something weird happened", e);
		}
	}

	public Result<IValue> eval(Command command) {
		return command.accept(this);
	}
	
//	protected void evalSDFModule(Default x) {
//		// TODO: find out what this is for
//		if (currentEnvt == rootScope) {
//			parser.addSdfImportForImportDefault(x);
//		}
////		super.evalSDFModule(x);
//	}
	
//	private IConstructor parseModule(String contents, ModuleEnvironment env) throws IOException{
//		URI uri = URI.create("stdin:///");
//		java.util.List<String> sdfSearchPath = sdf.getSdfSearchPath();
//		java.util.Set<String> sdfImports = parser.getSdfImports(sdfSearchPath, uri, contents.getBytes());
//
//		IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, uri, contents.getBytes(), env);
//		
//		if(tree.getConstructorType() == Factory.ParseTree_Summary){
//			throw parseError(tree, uri);
//		}
//		
//		return tree;
//	}

	
	/**
	 * Evaluate a declaration
	 * @param declaration
	 * @return
	 */
	public Result<IValue> eval(Declaration declaration) {
		currentAST = declaration;
		Result<IValue> r = declaration.accept(this);
		if(r != null){
			return r;
		}

		throw new NotYetImplemented(declaration.toString());
	}

	/**
	 * Evaluate an import
	 * @param imp
	 * @return
	 */
	public Result<IValue> eval(org.rascalmpl.ast.Import imp) {
		currentAST = imp;
		Result<IValue> r = imp.accept(this);
		if(r != null){
			return r;
		}

		throw new ImplementationError("Not yet implemented: " + imp.getTree());
	}
	
	public void doImport(String string) {
		eval("import " + string + ";", URI.create("import:///"));
	}


	public void reloadModule(String name) {
		try {
			if (!heap.existsModule(name)) {
				return; // ignore modules we don't know about
			}

			ModuleEnvironment mod = heap.resetModule(name);

			Module module = loadModule(name, mod);

			if (module != null) {
				if (!getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(getModuleName(module), name, getCurrentAST());
				}
				module.accept(this);
			}
		}
		catch (IOException e) {
			throw new ModuleLoadError(name, e.getMessage(), getCurrentAST());
		}
	}
	
	/* First a number of general utility methods */

	/*
	 * Return an evaluation result that is already in normal form,
	 * i.e., all potential rules have already been applied to it.
	 */

	Result<IValue> normalizedResult(Type t, IValue v){
		Map<Type, Type> bindings = getCurrentEnvt().getTypeBindings();
		Type instance;

		if (bindings.size() > 0) {
			instance = t.instantiate(getCurrentEnvt().getStore(), bindings);
		}
		else {
			instance = t;
		}

		if (v != null) {
			checkType(v.getType(), instance);
		}
		return makeResult(instance, v, this);
	}

	public void unwind(Environment old) {
		// TODO why not just replace the current env with the old one??
		while (getCurrentEnvt() != old) {
			setCurrentEnvt(getCurrentEnvt().getParent());
			getCurrentEnvt();
		}
	}

	public void pushEnv() {
		Environment env = new Environment(getCurrentEnvt(), getCurrentEnvt().getName());
		setCurrentEnvt(env);
	}

	Environment pushEnv(Statement s) {
		/* use the same name as the current envt */
		Environment env = new Environment(getCurrentEnvt(), s.getLocation(), getCurrentEnvt().getName());
		setCurrentEnvt(env);
		return env;
	}


	private void checkType(Type given, Type expected) {
		if (expected instanceof FunctionType) {
			return;
		}
		if (!given.isSubtypeOf(expected)){
			throw new UnexpectedTypeError(expected, given, getCurrentAST());
		}
	}

	public boolean mayOccurIn(Type small, Type large) {
		return mayOccurIn(small, large, new HashSet<Type>());
	}

	boolean mayOccurIn(Type small, Type large, java.util.Set<Type> seen){
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
			return mayOccurIn(small,large.getElementType(), seen);
		if(large.isMapType())
			return mayOccurIn(small, large.getKeyType(), seen) ||
			mayOccurIn(small, large.getValueType(), seen);
		if(large.isTupleType()){
			for(int i = 0; i < large.getArity(); i++){
				if(mayOccurIn(small, large.getFieldType(i), seen))
					return true;
			}
			return false;
		}

		if(large instanceof NonTerminalType && small instanceof NonTerminalType){
			//TODO: Until we have more precise info about the types in the concrete syntax
			// we just return true here.
			return true;
		}

		if(large.isConstructorType()){

			for(int i = 0; i < large.getArity(); i++){
				if(mayOccurIn(small, large.getFieldType(i), seen))
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
			for(Type alt : getCurrentEnvt().lookupAlternatives(large)){				
				if(alt.isConstructorType()){
					for(int i = 0; i < alt.getArity(); i++){
						Type fType = alt.getFieldType(i);
						if(seen.add(fType) && mayOccurIn(small, fType, seen))
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

	@Override
	public Result<IValue> visitExpressionAmbiguity(Ambiguity x) {
		// TODO: assuming that that is the only reason for an expression to be ambiguous
		// we might also check if this is an "appl" constructor...

		//			System.err.println("Env: " + currentEnvt);
		//			int i = 0;
		//			for (Expression exp: x.getAlternatives()) {
		//				System.err.println("Alt " + i++ + ": " + exp.getTree());
		//			}
		throw new AmbiguousConcretePattern(x);
	}



	@Override
	public Result<IValue> visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		throw new Ambiguous(x.toString());
	}

	// Commands 
	@Override
	public Result<IValue> visitCommandShell(Shell x) {
		return x.getCommand().accept(this);
	}

	@Override
	public Result<IValue> visitCommandDeclaration(org.rascalmpl.ast.Command.Declaration x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public Result<IValue> visitCommandStatement(
			org.rascalmpl.ast.Command.Statement x) {
		setCurrentAST(x.getStatement());
		return eval(x.getStatement());
	}
	
	@Override
	public Result<IValue> visitCommandExpression(org.rascalmpl.ast.Command.Expression x) {
		setCurrentAST(x.getExpression());
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result<IValue> visitCommandImport(org.rascalmpl.ast.Command.Import x) {
		return x.getImported().accept(this);
	}
	
	@Override
	public Result<IValue> visitShellCommandHelp(Help x) {
		printHelpMessage(stdout);
		return ResultFactory.nothing();
	}
	
	@Override
	public Result<IValue> visitShellCommandUnimport(Unimport x) {
		((ModuleEnvironment) getCurrentEnvt().getRoot()).unImport(x.getName().toString());
		return ResultFactory.nothing();
	}

	protected void printHelpMessage(PrintWriter out) {
		out.println("Welcome to the Rascal command shell.");
		out.println();
		out.println("Shell commands:");
		out.println(":help                      Prints this message");
		out.println(":quit or EOF               Quits the shell");
		out.println(":declarations              Lists all visible rules, functions and variables");
		out.println(":set <option> <expression> Sets an option");
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

	@Override
	public Result<IValue> visitShellCommandQuit(Quit x) {
		throw new QuitException();
	}

	@Override
	public Result<IValue> visitShellCommandEdit(Edit x) {
		return ResultFactory.nothing();
	}

	@Override
	public Result<IValue> visitShellCommandTest(ShellCommand.Test x) {
		return ResultFactory.bool(runTests(), this);
	}

	@Override
	public Result<IValue> visitShellCommandListDeclarations(ListDeclarations x) {
		printVisibleDeclaredObjects(stdout);
		return ResultFactory.nothing();
	}

	protected void printVisibleDeclaredObjects(PrintWriter out) {
		java.util.List<Entry<String, OverloadedFunctionResult>> functions = getCurrentEnvt().getAllFunctions();
		Collections.sort(functions, new Comparator<Entry<String, OverloadedFunctionResult>>() {
			public int compare(Entry<String, OverloadedFunctionResult> o1,
					Entry<String, OverloadedFunctionResult> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		if (functions.size() != 0) {
			out.println("Functions:");

			for (Entry<String, OverloadedFunctionResult> cand : functions) {
				for (AbstractFunction func : cand.getValue().iterable()) {
					out.print('\t');
					out.println(func.getHeader());
				}
			}
		}
		

		java.util.List<RewriteRule> rules = getHeap().getRules();
		if (rules.size() != 0) {
			out.println("Rules:");
			for (RewriteRule rule : rules) {
				out.print('\t');
				out.println(rule.getRule().getPattern().toString());
			}
		}
		
		Map<String, Result<IValue>> variables = getCurrentEnvt().getVariables();
		if (variables.size() != 0) {
			out.println("Variables:");
			for (String name : variables.keySet()) {
				out.print('\t');
				Result<IValue> value = variables.get(name);
				out.println(value.getType() + " " + name + " = " + value.getValue());
			}
		}
		
		out.flush();
	}
	
	// Modules -------------------------------------------------------------

	@Override
	public Result<IValue> visitImportDefault(
			org.rascalmpl.ast.Import.Default x) {
		// TODO support for full complexity of import declarations
		String name = getUnescapedModuleName(x);

		//		TODO If a SDF module and a Rascal module are located in the same directory thing doesn't always do what you want.
		if (isSDFModule(name)) {
			if (!heap.existsModule(name)) {
				heap.addModule(new ModuleEnvironment(name));
				addImportToCurrentModule(x, name);
			}
			else {
				heap.resetModule(name);
			}
			evalSDFModule(name, x);
			return nothing();
		}
		
		if (!heap.existsModule(name)) {
			// deal with a fresh module that needs initialization
			heap.addModule(new ModuleEnvironment(name));
			evalRascalModule(x, name);
			addImportToCurrentModule(x, name);
		}
		else if (getCurrentEnvt() == rootScope) {
			// in the root scope we treat an import as a "reload"
			heap.resetModule(name);
			evalRascalModule(x, name);
			addImportToCurrentModule(x, name);
		}
		else {
			// otherwise simply add the current imported name to the imports of the current module
			addImportToCurrentModule(x, name);
		}
		
		return nothing();
	}
	
	@Override
	public Result<IValue> visitImportSyntax(Syntax x) {
		typeDeclarator.declareSyntaxType(x.getSyntax().getUser(), getCurrentEnvt());
		return nothing();
	}

	protected void evalSDFModule(String name, 
			org.rascalmpl.ast.Import.Default x) {
		loadParseTreeModule(x);
		getCurrentModuleEnvironment().addSDFImport(getUnescapedModuleName(x));
		ModuleEnvironment mod = heap.getModule(name);
		if (mod == null) {
			mod = new ModuleEnvironment(name);
			heap.addModule(mod);
		}
		
		try {
			parser.generateModuleParser(sdf.getSdfSearchPath(), getCurrentModuleEnvironment().getSDFImports(), mod);
		} catch (IOException e) {
			RuntimeExceptionFactory.io(vf.string("IO exception while importing module " + x), x, getStackTrace());
		}
	}

	private void addImportToCurrentModule(
			org.rascalmpl.ast.Import.Default x, String name) {
		ModuleEnvironment module = heap.getModule(name);
		if (module == null) {
			throw new UndeclaredModuleError(name, x);
		}
		getCurrentModuleEnvironment().addImport(name, module);
	}

	private ModuleEnvironment getCurrentModuleEnvironment() {
		if (!(currentEnvt instanceof ModuleEnvironment)) {
			throw new ImplementationError("Current env should be a module environment");
		}
		return ((ModuleEnvironment) currentEnvt);
	}

	private String getUnescapedModuleName(
			org.rascalmpl.ast.Import.Default x) {
		return Names.fullName(x.getModule().getName());
	}

	private void loadParseTreeModule(
			org.rascalmpl.ast.Import.Default x) {
		String parseTreeModName = "ParseTree";
		if (!heap.existsModule(parseTreeModName)) {
			evalRascalModule(x, parseTreeModName);
		}
		addImportToCurrentModule(x, parseTreeModName);
	}

	private boolean isSDFModule(String name) {
		return sdf.isSdfModule(name);
	}

	private IConstructor tryLoadBinary(String name){
		InputStream inputStream = null;
		
		try {
			inputStream = resolver.getBinaryInputStream(URI.create("rascal:///" + name));
			if(inputStream == null) {
				return null;
			}

			PBFReader pbfReader = new PBFReader();
			return (IConstructor) pbfReader.read(ValueFactoryFactory.getValueFactory(), inputStream);
		}
		catch (IOException e) {
			return null;
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (IOException ioex){
				throw new ImplementationError(ioex.getMessage(), ioex);
			}
		}
	}
	
	private void writeBinary(String name, IConstructor tree) throws IOException {
		OutputStream outputStream = null;
		PBFWriter pbfWriter = new PBFWriter();
		
		try{
			outputStream = new BufferedOutputStream(resolver.getBinaryOutputStream(URI.create("rascal:///" + name)));
			pbfWriter.write(tree, outputStream);
		}
		finally{
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}
	
	/**
	 * Parse a module. Practical for implementing IDE features or features that use Rascal to implement Rascal.
	 * Parsing a module currently has the side effect of declaring non-terminal types in the given environment.
	 */
	public IConstructor parseModule(URI location, ModuleEnvironment env) throws IOException {
		byte[] data;
		
		InputStream inputStream = null;
		try {
			inputStream = URIResolverRegistry.getInstance().getInputStream(location);
			data = readModule(inputStream);
		}
		finally{
			if(inputStream != null){
				inputStream.close();
			}
		}

		URI resolved = resolver.resolve(location);
		if (resolved != null) {
			location = resolved;
		}
		
		return parseModule(data, location, env);
	}
	
	public IConstructor parseModule(byte[] data, URI location, ModuleEnvironment env) throws IOException {
		
		java.util.List<String> sdfSearchPath = sdf.getSdfSearchPath();
		java.util.Set<String> sdfImports;

		sdfImports = parser.getSdfImports(sdfSearchPath, location, data);

		IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, location, data, env);
		if(tree.getConstructorType() == Factory.ParseTree_Summary){
			throw parseError(tree, location);
		}
		return ParsetreeAdapter.addPositionInformation(tree, location); 
	}
	
	private byte[] readModule(InputStream inputStream) throws IOException{
		byte[] buffer = new byte[8192];
		
		ByteArrayOutputStream inputStringData = new ByteArrayOutputStream();
		
		int bytesRead;
		while((bytesRead = inputStream.read(buffer)) != -1){
			inputStringData.write(buffer, 0, bytesRead);
		}
		
		return inputStringData.toByteArray();
	}
	
	protected SyntaxError parseError(IConstructor tree, URI location){
		SummaryAdapter summary = new SummaryAdapter(tree);
		SubjectAdapter subject = summary.getInitialSubject();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		
		if (subject != null) {
			ISourceLocation loc = vf.sourceLocation(location, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());
			return new SyntaxError(subject.getDescription(), loc);
		}
		
		return new SyntaxError("unknown location, maybe you used a keyword as an identifier)", vf.sourceLocation(location, 0,1,1,1,0,1));
	}
	
	
	
	private Module loadModule(String name, ModuleEnvironment env) throws IOException {
		if(isSDFModule(name)){
			return null;
		}
		
		try{
			IConstructor tree = null;
			
			if (!saveParsedModules) {
				tree = tryLoadBinary(name);
			}
			
			if (tree == null) {
				tree = parseModule(URI.create("rascal:///" + name), env);
			}
			
			if (saveParsedModules) {
				writeBinary(name, tree);
			}
			
			ASTBuilder astBuilder = new ASTBuilder(new ASTFactory());
			Module moduleAst = astBuilder.buildModule(tree);
			
			if (moduleAst == null) {
				throw new ImplementationError("After this, all ambiguous ast's have been filtered in " + name, astBuilder.getLastSuccessLocation());
			}
			return moduleAst;
		}catch (FactTypeUseException e){
			throw new ImplementationError("Unexpected PDB typecheck exception", e);
		}
	}
	
	protected Module evalRascalModule(AbstractAST x,
			String name) {
		ModuleEnvironment env = heap.getModule(name);
		if (env == null) {
			env = new ModuleEnvironment(name);
			heap.addModule(env);
		}
		try {
			Module module = loadModule(name, env);
	
			if (module != null) {
				if (!getModuleName(module).equals(name)) {
					throw new ModuleNameMismatchError(getModuleName(module), name, x);
				}
				module.accept(this);
				return module;
			}
		}
		catch (StaticError e) {
			heap.removeModule(env);
			throw e;
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			heap.removeModule(env);
			throw e;
		} 
		catch (IOException e) {
			throw new ModuleLoadError(name, e.getMessage(), x);
		}

		throw new ImplementationError("Unexpected error while parsing module " + name + " and building an AST for it ", x.getLocation());
	}

	@Override 
	public Result<IValue> visitModuleDefault(
			org.rascalmpl.ast.Module.Default x) {
		String name = getModuleName(x);

		ModuleEnvironment env = heap.getModule(name);

		if (env == null) {
			env = new ModuleEnvironment(name);
			heap.addModule(env);
		}

		if (!env.isInitialized()) {
			Environment oldEnv = getCurrentEnvt();
			setCurrentEnvt(env); // such that declarations end up in the module scope
			try {
				x.getHeader().accept(this);

				java.util.List<Toplevel> decls = x.getBody().getToplevels();
				typeDeclarator.evaluateSyntaxDefinitions(x.getHeader().getImports(), getCurrentEnvt());
				typeDeclarator.evaluateDeclarations(decls, getCurrentEnvt());

				for (Toplevel l : decls) {
					l.accept(this);
				}

				// only after everything was successful mark the module initialized
				env.setInitialized();
			}
			finally {
				setCurrentEnvt(oldEnv);
			}
		}
		return ResultFactory.nothing();
	}

	protected String getModuleName(
			Module module) {
		String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	@Override
	public Result<IValue> visitHeaderDefault(
			org.rascalmpl.ast.Header.Default x) {
		visitImports(x.getImports());
		return ResultFactory.nothing();
	}

	@Override
	public Result<IValue> visitDeclarationAlias(Alias x) {
		typeDeclarator.declareAlias(x, getCurrentEnvt());
		return ResultFactory.nothing();
	}

	@Override
	public Result<IValue> visitDeclarationData(Data x) {
		typeDeclarator.declareConstructor(x, getCurrentEnvt());
		return nothing();
	}

	@Override
	public Result<IValue> visitDeclarationDataAbstract(DataAbstract x) {
		typeDeclarator.declareAbstractADT(x, getCurrentEnvt());
		return nothing();
	}

	private void visitImports(java.util.List<Import> imports) {
		for (Import i : imports) {
			i.accept(this);
		}
	}

	@Override
	public Result<IValue> visitHeaderParameters(Parameters x) {
		visitImports(x.getImports());
		return ResultFactory.nothing();
	}

	@Override
	public Result<IValue> visitToplevelGivenVisibility(GivenVisibility x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public Result<IValue> visitDeclarationFunction(Function x) {
		return x.getFunctionDeclaration().accept(this);
	}

	@Override
	public Result<IValue> visitDeclarationVariable(Variable x) {
		Result<IValue> r = nothing();
		setCurrentAST(x);

		for (org.rascalmpl.ast.Variable var : x.getVariables()) {
			Type declaredType = new TypeEvaluator(getCurrentModuleEnvironment(), heap).eval(x.getType());

			if (var.isInitialized()) {  
				Result<IValue> v = var.getInitial().accept(this);

				if (!getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new RedeclaredVariableError(var.getName().toString(), var);
				}

				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(new TypeStore(), bindings);
					r = makeResult(declaredType, v.getValue(), this);
					getCurrentModuleEnvironment().storeVariable(var.getName(), r);
				} else {
					throw new UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
			else {
				throw new UninitializedVariableError(Names.name(var.getName()), var);
			}
		}

		r.setPublic(x.getVisibility().isPublic());
		return r;
	}

	@Override
	public Result<IValue> visitDeclarationAnnotation(Annotation x) {
		Type annoType = new TypeEvaluator(getCurrentModuleEnvironment(), heap).eval(x.getAnnoType());
		String name = Names.name(x.getName());

		Type onType = new TypeEvaluator(getCurrentModuleEnvironment(), heap).eval(x.getOnType());
		getCurrentModuleEnvironment().declareAnnotation(onType, name, annoType);	

		return ResultFactory.nothing();
	}


	private Type evalType(org.rascalmpl.ast.Type type) {
		return new TypeEvaluator(getCurrentEnvt(), heap).eval(type);
	}

	@Override
	public Result<IValue> visitDeclarationView(View x) {
		// TODO implement
		throw new NotYetImplemented("Views");
	}

	@Override
	public Result<IValue> visitDeclarationRule(Rule x) {
		return x.getPatternAction().accept(this);
	}

	@Override
	public Result<IValue> visitPatternWithActionArbitrary(Arbitrary x) {
		IMatchingResult pv = x.getPattern().accept(patternEvaluator);
		
		Type pt = pv.getType(getCurrentEnvt());
		
		if (pv instanceof NodePattern) {
			pt = ((NodePattern) pv).getConstructorType(getCurrentEnvt());
		}
		
		// TODO store rules for concrete syntax on production rule and
		// create Lambda's for production rules to speed up matching and
		// rewrite rule look up
		if (pt instanceof NonTerminalType) {
			pt = Factory.Tree_Appl;
		}
		
		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
			throw new UnexpectedTypeError(tf.nodeType(), pt, x);
		
		heap.storeRule(pt, x, getCurrentModuleEnvironment());
		return ResultFactory.nothing();
	}

	@Override
	public Result<IValue> visitPatternWithActionReplacing(Replacing x) {
		IMatchingResult pv = x.getPattern().accept(patternEvaluator);
		Type pt = pv.getType(getCurrentEnvt());

		if (pv instanceof NodePattern) {
			pt = ((NodePattern) pv).getConstructorType(getCurrentEnvt());
		}
		
		if (pt instanceof NonTerminalType) {
			pt = Factory.Tree_Appl;
		}

		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
			throw new UnexpectedTypeError(tf.nodeType(), pt, x);
		
		heap.storeRule(pt, x, getCurrentModuleEnvironment());
		return ResultFactory.nothing();
	}


	@Override
	public Result<IValue> visitDeclarationTest(Test x) {
		return x.getTest().accept(this);
	}

	@Override
	public Result<IValue> visitTestLabeled(Labeled x) {
		getCurrentModuleEnvironment().addTest(x);
		return nothing();
	}

	@Override
	public Result<IValue> visitTestUnlabeled(Unlabeled x) {
		getCurrentModuleEnvironment().addTest(x);
		return nothing();
	}

	@Override
	public Result<IValue> visitDeclarationTag(Tag x) {
		throw new NotYetImplemented("tags");
	}

	// Variable Declarations -----------------------------------------------

	@Override
	public Result<IValue> visitLocalVariableDeclarationDefault(Default x) {
		// TODO deal with dynamic variables
		return x.getDeclarator().accept(this);
	}

	@Override
	public Result<IValue> visitDeclaratorDefault(
			org.rascalmpl.ast.Declarator.Default x) {
		Result<IValue> r = ResultFactory.nothing();

		for (org.rascalmpl.ast.Variable var : x.getVariables()) {
			String varAsString = var.getName().toString();

			if (var.isInitialized()) {  // variable declaration without initialization
				// first evaluate the initialization, in case the left hand side will shadow something
				// that is used on the right hand side.
				Result<IValue> v = var.getInitial().accept(this);

				Type declaredType = evalType(x.getType());

				if (!getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new RedeclaredVariableError(varAsString, var);
				}

				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(getCurrentEnvt().getStore(), bindings);
					// Was: r = makeResult(declaredType, applyRules(v.getValue()));
					r = makeResult(declaredType, v.getValue(), this);
					getCurrentEnvt().storeVariable(var.getName(), r);
				} else {
					throw new UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
			else {
				Type declaredType = evalType(x.getType());

				if (!getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new RedeclaredVariableError(varAsString, var);
				}
			}
		}

		return r;
	}
	
	@Override
	public Result<IValue> visitExpressionCallOrTree(CallOrTree x){
		setCurrentAST(x);

		Result<IValue> function = x.getExpression().accept(this);
		
		java.util.List<Expression> args = x.getArguments();

		IValue[] actuals = new IValue[args.size()];
		Type[] types = new Type[args.size()];

		for(int i = 0; i < args.size(); i++){
			Result<IValue> resultElem = args.get(i).accept(this);
			types[i] = resultElem.getType();
			actuals[i] = resultElem.getValue();
		}
		
		Result<IValue> res = function.call(types, actuals);
		
		// we need to update the strategy context when the function is of type Strategy
		IStrategyContext strategyContext = getStrategyContext();
		if(strategyContext != null){
			if(function.getValue() instanceof AbstractFunction){
				AbstractFunction f = (AbstractFunction) function.getValue();
				if(f.isTypePreserving()){
					strategyContext.update(actuals[0], res.getValue());
				}
			}else if(function.getValue() instanceof OverloadedFunctionResult){
				OverloadedFunctionResult fun = (OverloadedFunctionResult) function.getValue();
				
				for(AbstractFunction f: fun.iterable()){
					if(f.isTypePreserving()){
						strategyContext.update(actuals[0], res.getValue());
					}
				}
			}
		}
		return res;
	}

	private boolean hasJavaModifier(FunctionDeclaration func) {
		java.util.List<FunctionModifier> mods = func.getSignature().getModifiers().getModifiers();
		for (FunctionModifier m : mods) {
			if (m.isJava()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Result<IValue> visitFunctionBodyDefault(
			org.rascalmpl.ast.FunctionBody.Default x) {
		Result<IValue> result = nothing();

		for (Statement statement : x.getStatements()) {
			setCurrentAST(statement);
			result = statement.accept(this);
		}

		return result;
	}

	// Statements ---------------------------------------------------------

	@Override
	public Result<IValue> visitStatementAssert(Assert x) {
		Result<IValue> r = x.getExpression().accept(this);
		if (!r.getType().equals(tf.boolType())) {
			throw new UnexpectedTypeError(tf.boolType(), r.getType(), x);	
		}

		if(r.getValue().isEqual(vf.bool(false))) {
			throw RuntimeExceptionFactory.assertionFailed(x, getStackTrace());
		}
		return r;	
	}

	@Override
	public Result<IValue> visitStatementAssertWithMessage(AssertWithMessage x) {
		Result<IValue> r = x.getExpression().accept(this);
		if (!r.getType().equals(tf.boolType())) {
			throw new UnexpectedTypeError(tf.boolType(),r.getType(), x);	
		}
		if(r.getValue().isEqual(vf.bool(false))){
			Result<IValue> msgValue = x.getMessage().accept(this);
			IString msg = vf.string(unescape(msgValue.getValue().toString(), x, getCurrentEnvt()));
			throw RuntimeExceptionFactory.assertionFailed(msg, getCurrentAST(), getStackTrace());
		}
		return r;	
	}

	@Override
	public Result<IValue> visitStatementVariableDeclaration(VariableDeclaration x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public Result<IValue> visitStatementExpression(Statement.Expression x) {
		Environment old = getCurrentEnvt();

		try {
			pushEnv();
			return x.getExpression().accept(this);
		}
		finally {
			unwind(old);
		}
	}

	@Override
	public Result<IValue> visitStatementFunctionDeclaration(
			org.rascalmpl.ast.Statement.FunctionDeclaration x) {
		return x.getFunctionDeclaration().accept(this);
	}


	@Override
	public Result<IValue> visitExpressionSubscript(Subscript x) {		
		Result<IValue> expr = x.getExpression().accept(this);
		int nSubs = x.getSubscripts().size();
		Result<?> subscripts[] = new Result<?>[nSubs];
		for (int i = 0; i < nSubs; i++) {
			Expression subsExpr = x.getSubscripts().get(i);
			subscripts[i] = isWildCard(subsExpr.toString()) ? null : subsExpr.accept(this);
		}
		return expr.subscript(subscripts);
	}

	@Override
	public Result<IValue> visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		Result<IValue> expr = x.getExpression().accept(this);
		String field = x.getField().toString();


		return expr.fieldAccess(field, getCurrentEnvt().getStore());
	}

	private boolean isWildCard(String fieldName){
		return fieldName.equals("_");
	}

	@Override
	public Result<IValue> visitExpressionFieldProject(FieldProject x) {
		// TODO: move to result classes
		Result<IValue>  base = x.getExpression().accept(this);

		Type baseType = base.getType();
		if (!baseType.isTupleType() && !baseType.isRelationType() && !baseType.isMapType()) {
			throw new UnsupportedOperationError("projection", baseType, x);
		}

		java.util.List<Field> fields = x.getFields();
		int nFields = fields.size();
		int selectedFields[] = new int[nFields];

		for(int i = 0 ; i < nFields; i++){
			Field f = fields.get(i);
			if (f.isIndex()) {
				selectedFields[i] = ((IInteger) f.getFieldIndex().accept(this).getValue()).intValue();
			} 
			else {
				String fieldName = Names.name(f.getFieldName());
				try {
					selectedFields[i] = baseType.getFieldIndex(fieldName);
				} catch (UndeclaredFieldException e){
					throw new UndeclaredFieldError(fieldName, baseType, x);
				}
			}

			if (selectedFields[i] < 0 || selectedFields[i] > baseType.getArity()) {
				throw RuntimeExceptionFactory.indexOutOfBounds(vf.integer(i), getCurrentAST(), getStackTrace());
			}
		}

		return base.fieldSelect(selectedFields);
	}

	@Override
	public Result<IValue> visitStatementEmptyStatement(EmptyStatement x) {
		return ResultFactory.nothing();
	}


	@Override
	public Result<IValue> visitStatementFail(Fail x) {
		if (!x.getTarget().isEmpty()) {
			throw new Failure(x.getTarget().getName().toString());
		}

		throw new Failure();
	}

	@Override
	public Result<IValue> visitStatementReturn(
			org.rascalmpl.ast.Statement.Return x) {
		throw new Return(x.getStatement().accept(this), x.getStatement().getLocation());
	}

	@Override
	public Result<IValue> visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		return new Statement.NonEmptyBlock(x.getTree(), new Label.Empty(x.getTree()), x.getStatements()).accept(this);
	}

	@Override
	public Result<IValue> visitStatementAppend(Append x) {
		Accumulator target = null;
		if (accumulators.empty()) {
			throw new AppendWithoutLoop(x);
		}
		if (!x.getDataTarget().isEmpty()) {
			String label = Names.name(x.getDataTarget().getLabel());
			for (Accumulator accu: accumulators) {
				if (accu.hasLabel(label)) {
					target = accu;
					break;
				}
			}
			if (target == null) {
				throw new AppendWithoutLoop(x); // TODO: better error message
			}
		}
		else {
			target = accumulators.peek();
		}
		Result<IValue> result = x.getStatement().accept(this);
		target.append(result);
		return result;
	}

	@Override
	public Result<IValue> visitStatementBreak(Break x) {
		throw new NotYetImplemented(x.toString()); // TODO
	}


	@Override
	public Result<IValue> visitStatementContinue(Continue x) {
		throw new NotYetImplemented(x.toString()); // TODO
	}

	@Override
	public Result<IValue> visitStatementGlobalDirective(GlobalDirective x) {
		throw new NotYetImplemented(x.toString()); // TODO
	}

	@Override
	public Result<IValue> visitStatementThrow(Throw x) {
		throw new org.rascalmpl.interpreter.control_exceptions.Throw(x.getStatement().accept(this).getValue(), getCurrentAST(), getStackTrace());
	}

	@Override
	public Result<IValue> visitStatementTry(Try x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), null);
	}

	@Override
	public Result<IValue> visitStatementTryFinally(TryFinally x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), x.getFinallyBody());
	}

	private Result<IValue> evalStatementTry(Statement body, java.util.List<Catch> handlers, Statement finallyBody){
		Result<IValue> res = nothing();

		try {
			res = body.accept(this);
		} catch (org.rascalmpl.interpreter.control_exceptions.Throw e){
			IValue eValue = e.getException();

			for (Catch c : handlers){
				if(c.isDefault()){
					res = c.getBody().accept(this);
					break;
				} 

				// TODO: Throw should contain Result<IValue> instead of IValue
				if(matchAndEval(makeResult(eValue.getType(), eValue, this), c.getPattern(), c.getBody())){
					break;
				}
			}
		}
		finally {
			if (finallyBody != null) {
				finallyBody.accept(this);
			}
		}
		return res;
	}

	@Override
	public Result<IValue> visitStatementVisit(
			org.rascalmpl.ast.Statement.Visit x) {
		return x.getVisit().accept(this);
	}

	@Override
	public Result<IValue> visitStatementInsert(Insert x) {
		throw new org.rascalmpl.interpreter.control_exceptions.Insert(x.getStatement().accept(this));
	}

	@Override
	public Result<IValue> visitStatementAssignment(Assignment x) {
		Result<IValue> right = x.getStatement().accept(this);
		return x.getAssignable().accept(new AssignableEvaluator(getCurrentEnvt(), x.getOperator(), right, this));
	}

	@Override
	public Result<IValue> visitStatementNonEmptyBlock(
			org.rascalmpl.ast.Statement.NonEmptyBlock x) {
		Result<IValue> r = nothing();
		Environment old = getCurrentEnvt();

		pushEnv(x);
		try {
			for (Statement stat : x.getStatements()) {
				setCurrentAST(stat);
				r = stat.accept(this);
			}
		}
		finally {
			unwind(old);
		}
		return r;
	}

	@Override
	public Result<IValue> visitAssignableVariable(
			org.rascalmpl.ast.Assignable.Variable x) {
		return getCurrentEnvt().getVariable(x.getQualifiedName());
	}

	@Override
	public Result<IValue> visitAssignableFieldAccess(FieldAccess x) {
		Result<IValue> receiver = x.getReceiver().accept(this);
		String label = x.getField().toString();

		Type receiverType = receiver.getType();
		if (receiverType.isTupleType()) {
			// the run-time tuple may not have labels, the static type can have labels,
			// so we use the static type here. 
			int index = receiverType.getFieldIndex(label);
			IValue result = ((ITuple) receiver.getValue()).get(index);
			Type type = receiverType.getFieldType(index);
			return makeResult(type, result, this);
		}
		else if (receiverType.isConstructorType() || receiverType.isAbstractDataType()) {
			IConstructor cons = (IConstructor) receiver.getValue();
			Type node = cons.getConstructorType();

			if (!receiverType.hasField(label, getCurrentEnvt().getStore())) {
				throw new UndeclaredFieldError(label, receiverType, x);
			}

			if (!node.hasField(label)) {
				throw RuntimeExceptionFactory.noSuchField(label, x,getStackTrace());
			}

			int index = node.getFieldIndex(label);
			return makeResult(node.getFieldType(index), cons.get(index), this);
		}
		else if (receiverType.isSourceLocationType()) {
			return receiver.fieldAccess(label, new TypeStore());
		}
		else {
			throw new UndeclaredFieldError(label, receiverType, x);
		}
	}

	@Override
	public Result<IValue> visitAssignableAnnotation(
			org.rascalmpl.ast.Assignable.Annotation x) {
		Result<IValue> receiver = x.getReceiver().accept(this);
		String label = x.getAnnotation().toString();

		if (!getCurrentEnvt().declaresAnnotation(receiver.getType(), label)) {
			throw new UndeclaredAnnotationError(label, receiver.getType(), x);
		}

		Type type = getCurrentEnvt().getAnnotationType(receiver.getType(), label);
		IValue value = ((IConstructor) receiver.getValue()).getAnnotation(label);

		return makeResult(type, value, this);
	}

	@Override
	public Result<IValue> visitAssignableConstructor(Constructor x) {
		throw new ImplementationError("Constructor assignable does not represent a value");
	}

	@Override
	public Result<IValue> visitAssignableIfDefinedOrDefault(
			org.rascalmpl.ast.Assignable.IfDefinedOrDefault x) {
		throw new ImplementationError("ifdefined assignable does not represent a value");
	}

	@Override
	public Result<IValue> visitAssignableSubscript(
			org.rascalmpl.ast.Assignable.Subscript x) {
		Result<IValue> receiver = x.getReceiver().accept(this);
		Result<IValue> subscript = x.getSubscript().accept(this);

		if (receiver.getType().isListType()) {
			if (subscript.getType().isIntegerType()) {
				IList list = (IList) receiver.getValue();
				IValue result = list.get(((IInteger) subscript.getValue()).intValue());
				Type type = receiver.getType().getElementType();
				return normalizedResult(type, result);
			}

			throw new UnexpectedTypeError(tf.integerType(), subscript.getType(), x);
		}
		else if (receiver.getType().isMapType()) {
			Type keyType = receiver.getType().getKeyType();

			if (receiver.hasInferredType() || subscript.getType().isSubtypeOf(keyType)) {
				IValue result = ((IMap) receiver.getValue()).get(subscript.getValue());
				
				if (result == null) {
					throw RuntimeExceptionFactory.noSuchKey(subscript.getValue(), x, getStackTrace());
				}
				Type type = receiver.getType().getValueType();
				return makeResult(type, result, this);
			}

			throw new UnexpectedTypeError(keyType, subscript.getType(), x.getSubscript());
		}
		// TODO implement other subscripts
		throw new UnsupportedOperationError("subscript", receiver.getType(), x);
	}

	@Override
	public Result<IValue> visitAssignableTuple(
			org.rascalmpl.ast.Assignable.Tuple x) {
		throw new ImplementationError("Tuple in assignable does not represent a value:" + x);
	}

	@Override
	public Result<IValue> visitAssignableAmbiguity(
			org.rascalmpl.ast.Assignable.Ambiguity x) {
		throw new Ambiguous(x.toString());
	}

	@Override
	public Result<IValue> visitFunctionDeclarationDefault(
			org.rascalmpl.ast.FunctionDeclaration.Default x) {
		AbstractFunction lambda;
		boolean varArgs = x.getSignature().getParameters().isVarArgs();

		if (hasJavaModifier(x)) {
			throw new JavaMethodLinkError("may not use java modifier with a function that has a body", null,  x);
		}
		else {
			if (!x.getBody().isDefault()) {
				throw new MissingModifierError("java", x);
			}

			lambda = new RascalFunction(this, x, varArgs, getCurrentEnvt(), accumulators);
		}

		getCurrentEnvt().storeFunction(lambda.getName(), lambda);

		lambda.setPublic(x.getVisibility().isPublic());
		return lambda;
	}

	@Override
	public Result<IValue> visitFunctionDeclarationAbstract(Abstract x) {
		boolean varArgs = x.getSignature().getParameters().isVarArgs();

		if (!hasJavaModifier(x)) {
			throw new MissingModifierError("java", x);
		}

		AbstractFunction lambda = new JavaMethod(this, x, varArgs, getCurrentEnvt(), javaBridge);
		String name = Names.name(x.getSignature().getName());
		getCurrentEnvt().storeFunction(name, lambda);

		lambda.setPublic(x.getVisibility().isPublic());
		return lambda;
	}

	@Override
	public Result<IValue> visitStatementIfThenElse(IfThenElse x) {
		Statement body = x.getThenStatement();
		java.util.List<Expression> generators = x.getConditions();
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();

		int i = 0;
		try {
			gens[0] = makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();
			while(i >= 0 && i < size) {		
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						setCurrentAST(body);
						return body.accept(this);
					}

					i++;
					gens[i] = makeBooleanResult(generators.get(i));
					gens[i].init();
					olds[i] = getCurrentEnvt();
					pushEnv();
				} else {
					unwind(olds[i]);
					pushEnv();
					i--;
				}
			}
		} finally {
			unwind(old);
		}

		Statement elsePart = x.getElseStatement();
		setCurrentAST(elsePart);
		return elsePart.accept(this);
	}




	@Override
	public Result<IValue> visitStatementIfThen(IfThen x) {
		Statement body = x.getThenStatement();
		java.util.List<Expression> generators = x.getConditions();
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();

		int i = 0;
		try {
			gens[0] = makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();

			while(i >= 0 && i < size) {		
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						setCurrentAST(body);
						return body.accept(this);
					}

					i++;
					gens[i] = makeBooleanResult(generators.get(i));
					gens[i].init();
					olds[i] = getCurrentEnvt();
					pushEnv();
				} else {
					unwind(olds[i]);
					pushEnv();
					i--;
				}
			}
		} finally {
			unwind(old);
		}
		return nothing();
	}

	@Override
	public Result<IValue> visitStatementWhile(While x) {
		Statement body = x.getBody();
		java.util.List<Expression> generators = x.getConditions();
		
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();

		String label = null;
		if (!x.getLabel().isEmpty()) {
			label = Names.name(x.getLabel().getName());
		}
		accumulators.push(new Accumulator(vf, label));

		// a while statement is different from a for statement, the body of the while can influence the
		// variables that are used to test the condition of the loop
		// while does not iterate over all possible matches, rather it produces every time the first match
		// that makes the condition true
		
		loop: while (true) {
			int i = 0;
			try {
				gens[0] = makeBooleanResult(generators.get(0));
				gens[0].init();
				olds[0] = getCurrentEnvt();
				pushEnv();

				while(i >= 0 && i < size) {		
					if(gens[i].hasNext() && gens[i].next()){
						if(i == size - 1){
							body.accept(this);
							continue loop;
						}

						i++;
						gens[i] = makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = getCurrentEnvt();
						pushEnv();
					} else {
						unwind(olds[i]);
						pushEnv();
						i--;
					}
				}
			} finally {
				unwind(old);
			}
			IValue value = accumulators.pop().done();
			return makeResult(value.getType(), value, this);
		}
	}

	@Override
	public Result<IValue> visitStatementDoWhile(DoWhile x) {
		Statement body = x.getBody();
		Expression generator = x.getCondition();
		IBooleanResult gen;
		Environment old = getCurrentEnvt();
		String label = null;
		if (!x.getLabel().isEmpty()) {
			label = Names.name(x.getLabel().getName());
		}
		accumulators.push(new Accumulator(vf, label));

		
		while (true) {
			try {
				body.accept(this);

				gen = makeBooleanResult(generator);
				gen.init();
				if(!(gen.hasNext() && gen.next())) {
					IValue value = accumulators.pop().done();
					return makeResult(value.getType(), value, this);
				}
			} finally {
				unwind(old);
			}
		}
	}

	@Override
	public Result<IValue> visitExpressionMatch(Match x) {
		return evalBooleanExpression(x);
	}

	@Override
	public Result<IValue> visitExpressionNoMatch(NoMatch x) {
		return evalBooleanExpression(x);
	}

	// ----- General method for matching --------------------------------------------------

	public IBooleanResult makeBooleanResult(org.rascalmpl.ast.Expression pat){
		if (pat instanceof Expression.Ambiguity) {
			// TODO: wrong exception here.
			throw new AmbiguousConcretePattern(pat);
		}

		BooleanEvaluator pe = new BooleanEvaluator(this);
		return pat.accept(pe);
	}

	// Expressions -----------------------------------------------------------

	@Override
	public Result<IValue> visitExpressionReifiedType(ReifiedType x) {
		BasicType basic = x.getBasicType();
		java.util.List<Expression> args = x.getArguments();
		Type[] fieldTypes = new Type[args.size()];
		IValue[] fieldValues = new IValue[args.size()];

		int i = 0;
		boolean valued = false;
		for (Expression a : args) {
			Result<IValue> argResult = a.accept(this);
			Type argType = argResult.getType();

			if (argType instanceof org.rascalmpl.interpreter.types.ReifiedType) {
				fieldTypes[i] = argType.getTypeParameters().getFieldType(0);
				i++;
			}
			else {
				valued = true;
				fieldValues[i] = argResult.getValue();
				i++;
			}
		}

		Type type = basic.accept(new BasicTypeEvaluator(getCurrentEnvt(), valued ? null : tf.tupleType(fieldTypes), valued ? fieldValues : null));

		return type.accept(new TypeReifier(this, vf));
	}

	@Override
	public Result<IValue> visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public Result<IValue> visitLiteralRegExp(RegExp x) {
		throw new SyntaxError("regular expression. They are only allowed in a pattern (left of <- and := or in a case statement).", x.getLocation());
	}
	
	@Override
	public Result<IValue> visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}

	@Override
	public Result<IValue> visitIntegerLiteralOctalIntegerLiteral(
			OctalIntegerLiteral x) {
		return x.getOctal().accept(this);
	}
	
	@Override
	public Result<IValue> visitIntegerLiteralHexIntegerLiteral(
			HexIntegerLiteral x) {
		return x.getHex().accept(this);
	}
	
	@Override
	public Result<IValue> visitOctalIntegerLiteralLexical(
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical x) {
		return makeResult(tf.integerType(), vf.integer(new BigInteger(x.getString(), 8).toString()), this);
	}

	@Override
	public Result<IValue> visitHexIntegerLiteralLexical(
			org.rascalmpl.ast.HexIntegerLiteral.Lexical x) {
		String chars = x.getString();
		String hex = chars.substring(2, chars.length());
		return makeResult(tf.integerType(), vf.integer(new BigInteger(hex, 16).toString()), this);
	}
	
	@Override
	public Result<IValue> visitLiteralReal(Real x) {
		String str = x.getRealLiteral().toString();
		if (str.toLowerCase().endsWith("d")) {
			str = str.substring(0, str.length() - 1);
		}
		return makeResult(tf.realType(), vf.real(str), this);
	}

	@Override
	public Result<IValue> visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		return makeResult(tf.boolType(), vf.bool(str.equals("true")), this);
	}

	@Override
	public Result<IValue> visitLiteralString(
			org.rascalmpl.ast.Literal.String x) {
		StringLiteral lit = x.getStringLiteral();

		StringBuilder result = new StringBuilder();

		// To prevent infinite recursion detect non-interpolated strings
		// first. TODO: design flaw?
		if (lit.isNonInterpolated()) {
			String str = Utils.unescape(((StringConstant.Lexical)lit.getConstant()).getString());
			result.append(str);
		}
		else {
			Statement stat = StringTemplateConverter.convert(lit);
			Result<IValue> value = stat.accept(this);
			if (!value.getType().isListType()) {
				throw new ImplementationError("template eval returns non-list");
			}
			IList list = (IList)value.getValue();
			for (IValue elt: list) {
				result.append(toString(elt).getValue());
			}
		}

		return makeResult(tf.stringType(), vf.string(result.toString()), this);
	}

	@Override
	public Result<IValue> visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = ((org.rascalmpl.ast.DecimalIntegerLiteral.Lexical) x.getDecimal()).getString();
		return makeResult(tf.integerType(), vf.integer(str), this);
	}
	
	@Override
	public Result<IValue> visitExpressionQualifiedName(
			org.rascalmpl.ast.Expression.QualifiedName x) {
		QualifiedName name = x.getQualifiedName();
		Result<IValue> variable = getCurrentEnvt().getVariable(name);

		if (variable == null) {
			throw new UndeclaredVariableError(name.toString(), name);
		}

		if (variable.getValue() == null) {
			throw new UninitializedVariableError(name.toString(), name);
		}

		return variable;
	}

	@Override
	public Result<IValue> visitExpressionList(List x) {
		java.util.List<org.rascalmpl.ast.Expression> elements = x
		.getElements();

		Type elementType =  tf.voidType();
		java.util.List<IValue> results = new ArrayList<IValue>();

		// Splicing is true for the complete list; a terrible, terrible hack.
		boolean splicing = concreteListsShouldBeSpliced;
		boolean first = true;
		int skip = 0;

		for (org.rascalmpl.ast.Expression expr : elements) {
			Result<IValue> resultElem = expr.accept(this);
			
			if(resultElem.getType().isVoidType())
				throw new NonVoidTypeRequired(expr);

			if (skip > 0) {
				skip--;
				continue;
			}

			Type resultType = resultElem.getType();
			if (splicing && resultType instanceof NonTerminalType) {
				IConstructor sym = ((NonTerminalType)resultType).getSymbol();

				if (SymbolAdapter.isAnyList(sym)) {
					IConstructor appl = ((IConstructor)resultElem.getValue());
					IList listElems = TreeAdapter.getArgs(appl);
					// Splice elements in list if element types permit this

					if (!listElems.isEmpty()) {
						for(IValue val : listElems){
							elementType = elementType.lub(val.getType());
							results.add(val);
						}
					}
					else {
						// make sure to remove surrounding sep
						if (!first) {
							if (SymbolAdapter.isCf(sym)) {
								IConstructor listSym = SymbolAdapter.getSymbol(sym);
								if (SymbolAdapter.isIterStar(listSym)) {
									results.remove(results.size() - 1);
								}
								else if (SymbolAdapter.isIterStarSep(listSym)) {
									results.remove(results.size() - 1);
									results.remove(results.size() - 1);
									results.remove(results.size() - 1);
								}
							}
							if (SymbolAdapter.isLex(sym)) {
								IConstructor listSym = SymbolAdapter.getSymbol(sym);
								if (SymbolAdapter.isIterStarSep(listSym)) {
									results.remove(results.size() - 1);
									results.remove(results.size() - 1);
								}
							}
						}
						else {
							if (SymbolAdapter.isCf(sym)) {
								IConstructor listSym = SymbolAdapter.getSymbol(sym);
								if (SymbolAdapter.isIterStar(listSym)) {
									skip = 1;
								}
								else if (SymbolAdapter.isIterStarSep(listSym)) {
									skip = 3;
								}
							}
							if (SymbolAdapter.isLex(sym)) {
								IConstructor listSym = SymbolAdapter.getSymbol(sym);
								if (SymbolAdapter.isIterStarSep(listSym)) {
									skip = 2;
								}
							}
						}
					}
				}
				else {
					// Just add it.
					elementType = elementType.lub(resultElem.getType());
					results.add(results.size(), resultElem.getValue());
				}
			}
			else {
				/* = no concrete syntax */ 
				if(resultElem.getType().isListType() &&
						!expr.isList() &&
						elementType.isSubtypeOf(resultElem.getType().getElementType())
				){
					/*
					 * Splice elements in list if element types permit this
					 */
					for(IValue val : ((IList) resultElem.getValue())){
						elementType = elementType.lub(val.getType());
						results.add(val);
					}
				} else {
					elementType = elementType.lub(resultElem.getType());

					results.add(results.size(), resultElem.getValue());
				}
			}


			first = false;
		}
		Type resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		// Was: return makeResult(resultType, applyRules(w.done()));
		return makeResult(resultType, w.done(), this);
	}

	@Override
	public Result<IValue> visitExpressionSet(Set x) {
		java.util.List<org.rascalmpl.ast.Expression> elements = x
		.getElements();

		Type elementType = tf.voidType();
		java.util.List<IValue> results = new ArrayList<IValue>();

		for (org.rascalmpl.ast.Expression expr : elements) {
			Result<IValue> resultElem = expr.accept(this);
			if(resultElem.getType().isVoidType())
				throw new NonVoidTypeRequired(expr);
		
			if(resultElem.getType().isSetType() && !expr.isSet() &&
					elementType.isSubtypeOf(resultElem.getType().getElementType())){
				/*
				 * Splice the elements in the set if element types permit this.
				 */
				for(IValue val : ((ISet) resultElem.getValue())){
					elementType = elementType.lub(val.getType());
					results.add(val);
				}
			} else {
				elementType = elementType.lub(resultElem.getType());
				results.add(results.size(), resultElem.getValue());
			}
		}
		Type resultType = tf.setType(elementType);
		ISetWriter w = resultType.writer(vf);
		w.insertAll(results);
		//Was: return makeResult(resultType, applyRules(w.done()));
		return makeResult(resultType, w.done(), this);
	}

	@Override
	public Result<IValue> visitExpressionMap(
			org.rascalmpl.ast.Expression.Map x) {

		java.util.List<org.rascalmpl.ast.Mapping> mappings = x
		.getMappings();
		Map<IValue,IValue> result = new HashMap<IValue,IValue>();
		Type keyType = tf.voidType();
		Type valueType = tf.voidType();

		for (org.rascalmpl.ast.Mapping mapping : mappings) {
			Result<IValue> keyResult = mapping.getFrom().accept(this);
			Result<IValue> valueResult = mapping.getTo().accept(this);
			
			if(keyResult.getType().isVoidType())
				throw new NonVoidTypeRequired(mapping.getFrom());
			
			if(valueResult.getType().isVoidType())
				throw new NonVoidTypeRequired(mapping.getTo());

			keyType = keyType.lub(keyResult.getType());
			valueType = valueType.lub(valueResult.getType());
			
			IValue keyValue = result.get(keyResult.getValue());
			if(keyValue != null){
				throw RuntimeExceptionFactory.MultipleKey(keyValue, mapping.getFrom(), getStackTrace());
			}

			result.put(keyResult.getValue(), valueResult.getValue());
		}

		Type type = tf.mapType(keyType, valueType);
		IMapWriter w = type.writer(vf);
		w.putAll(result);

		return makeResult(type, w.done(), this);
	}

	//	@Override
	//	public Result<IValue> visitExpressionNonEmptyBlock(NonEmptyBlock x) {
	//		return new org.meta_environment.rascal.interpreter.result.RascalFunction(x, this, (FunctionType) RascalTypeFactory.getInstance().functionType(tf.voidType(), tf.tupleEmpty()), false, x.getStatements(), getCurrentEnvt());
	//	}

	@Override
	public Result<IValue> visitExpressionTuple(Tuple x) {
		java.util.List<org.rascalmpl.ast.Expression> elements = x
		.getElements();

		IValue[] values = new IValue[elements.size()];
		Type[] types = new Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			Result<IValue> resultElem = elements.get(i).accept(this);
			types[i] = resultElem.getType();
			values[i] = resultElem.getValue();
		}

		//return makeResult(tf.tupleType(types), applyRules(vf.tuple(values)));
		return makeResult(tf.tupleType(types), vf.tuple(values), this);
	}

	@Override
	public Result<IValue> visitExpressionGetAnnotation(
			org.rascalmpl.ast.Expression.GetAnnotation x) {
		Result<IValue> base = x.getExpression().accept(this);
		String annoName = Names.name(x.getName());
		return base.getAnnotation(annoName, getCurrentEnvt());
	}

	@Override
	public Result<IValue> visitExpressionSetAnnotation(
			org.rascalmpl.ast.Expression.SetAnnotation x) {
		Result<IValue> base = x.getExpression().accept(this);
		String annoName = Names.name(x.getName());
		Result<IValue> anno = x.getValue().accept(this);
		return base.setAnnotation(annoName, anno, getCurrentEnvt());
	}

	@Override
	public Result<IValue> visitExpressionAddition(Addition x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.add(right);

	}

	@Override
	public Result<IValue> visitExpressionSubtraction(Subtraction x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.subtract(right);

	}

	@Override
	public Result<IValue> visitExpressionNegative(Negative x) {
		Result<IValue> arg = x.getArgument().accept(this);
		return arg.negative();
	}

	@Override
	public Result<IValue> visitExpressionProduct(Product x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.multiply(right);
	}

	@Override
	public Result<IValue> visitExpressionJoin(Join x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.join(right);
	}

	@Override
	public Result<IValue> visitExpressionDivision(Division x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.divide(right);
	}

	@Override
	public Result<IValue> visitExpressionModulo(Modulo x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.modulo(right);
	}

	@Override
	public Result<IValue> visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}

	@Override
	public Result<IValue> visitExpressionIntersection(Intersection x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.intersect(right);
	}

	@Override
	public Result<IValue> visitExpressionOr(Or x) {
		return evalBooleanExpression(x);
	}

	@Override
	public Result<IValue> visitExpressionAnd(And x) {
		return evalBooleanExpression(x);
	}

	private Result<IValue> evalBooleanExpression(Expression x) {
		IBooleanResult mp = makeBooleanResult(x);
		mp.init();
		while(mp.hasNext()){
			if(mp.next()) {
				return ResultFactory.bool(true, this);
			}
		}
		return ResultFactory.bool(false, this);
	}

	@Override
	public Result<IValue> visitExpressionNegation(Negation x) {
		return evalBooleanExpression(x);
	}

	@Override
	public Result<IValue> visitExpressionImplication(Implication x) {
		return evalBooleanExpression(x);
	}

	@Override
	public Result<IValue> visitExpressionEquivalence(Equivalence x) {
		return evalBooleanExpression(x);
	}

	@Override
	public Result<IValue> visitExpressionEquals(
			org.rascalmpl.ast.Expression.Equals x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.equals(right);
	}

	@Override
	public Result<IValue> visitExpressionGuarded(Guarded x) {
		Result<IValue> result = x.getPattern().accept(this);
		Type expected = new TypeEvaluator(getCurrentEnvt(), heap).eval(x.getType());

		// TODO: clean up this hack
		if (expected instanceof NonTerminalType && result.getType().isSubtypeOf(tf.stringType())) {
			try {
				String command = '(' + expected.toString() + ')' + '`' + ((IString) result.getValue()).getValue() + '`';
				IConstructor tree = parser.parseCommand(((ModuleEnvironment) getCurrentEnvt().getRoot()).getSDFImports(), sdf.getSdfSearchPath(), x.getLocation().getURI(), command);

				if (tree.getConstructorType() == Factory.ParseTree_Summary) {
					throw RuntimeExceptionFactory.parseError(x.getPattern().getLocation(), x.getPattern(), getStackTrace());
				}

				tree = ParsetreeAdapter.getTop(tree); // start rule
				tree = (IConstructor) TreeAdapter.getArgs(tree).get(1); // top command expression
				tree = (IConstructor) TreeAdapter.getArgs(tree).get(0); // typed quoted embedded fragment
				tree = (IConstructor) TreeAdapter.getArgs(tree).get(8); // wrapped string between `...`
				return makeResult(expected, tree, this);

			} catch (IOException e) {
				throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), x.getPattern(), getStackTrace());
			}
		}
		if (!result.getType().isSubtypeOf(expected)) {
			throw new UnexpectedTypeError(expected, result.getType(), x.getPattern());
		}

		return makeResult(expected, result.getValue(), this);
	}

	@Override
	public Result<IValue> visitLiteralLocation(Location x) {
		return x.getLocationLiteral().accept(this);
	}

	public org.rascalmpl.interpreter.result.Result<IValue> visitLocationLiteralDefault(org.rascalmpl.ast.LocationLiteral.Default x) {
		Result<IValue> protocolPart = x.getProtocolPart().accept(this);
		Result<IValue> pathPart = x.getPathPart().accept(this);

		String uri = ((IString) protocolPart.getValue()).getValue() + "://" + ((IString) pathPart.getValue()).getValue();

		try {
			URI url = new URI(uri);
			ISourceLocation r = vf.sourceLocation(url);
			return makeResult(tf.sourceLocationType(), r, this);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.malformedURI(uri, x, getStackTrace());
		}
	}

	@Override
	public Result<IValue> visitProtocolPartNonInterpolated(NonInterpolated x) {
		return x.getProtocolChars().accept(this);
	}

	@Override
	public Result<IValue> visitProtocolCharsLexical(
			org.rascalmpl.ast.ProtocolChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(1, str.length() - 3)), this);
	}

	@Override
	public Result<IValue> visitProtocolPartInterpolated(Interpolated x) {
		Result<IValue> pre = x.getPre().accept(this);
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> tail = x.getTail().accept(this);

		String result = ((IString) pre.getValue()).getValue() + toString(expr.getValue()).getValue() + ((IString) tail.getValue()).getValue();

		return makeResult(tf.stringType(), vf.string(result), this);
	}

	@Override
	public Result<IValue> visitProtocolTailMid(
			org.rascalmpl.ast.ProtocolTail.Mid x) {
		Result<IValue> pre = x.getMid().accept(this);
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> tail = x.getTail().accept(this);

		String result = ((IString) pre.getValue()).getValue() + toString(expr.getValue()).getValue() + ((IString) tail.getValue()).getValue();

		return makeResult(tf.stringType(), vf.string(result), this);
	}

	@Override
	public Result<IValue> visitProtocolTailPost(Post x) {
		return x.getPost().accept(this);
	}

	@Override
	public Result<IValue> visitPathPartInterpolated(
			org.rascalmpl.ast.PathPart.Interpolated x) {
		Result<IValue> pre = x.getPre().accept(this);
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> tail = x.getTail().accept(this);

		String preString = ((IString) pre.getValue()).getValue();
		String exprString = toString(expr.getValue()).getValue();
		String tailString = ((IString) tail.getValue()).getValue();
		String result = preString + exprString + tailString;

		return makeResult(tf.stringType(), vf.string(result), this);
	}

	@Override
	public Result<IValue> visitPrePathCharsLexical(
			org.rascalmpl.ast.PrePathChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(0, str.length() - 1)), this);
	}

	@Override
	public Result<IValue> visitPostPathCharsLexical(
			org.rascalmpl.ast.PostPathChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(1, str.length() - 1)), this);
	}

	@Override
	public Result<IValue> visitPathTailPost(
			org.rascalmpl.ast.PathTail.Post x) {
		return x.getPost().accept(this);
	}

	@Override
	public Result<IValue> visitMidPathCharsLexical(
			org.rascalmpl.ast.MidPathChars.Lexical x) {
		String s = x.getString();
		s = s.substring(1, s.length() - 1);
		return makeResult(tf.stringType(), vf.string(s), this);
	}
	
	
	@Override
	public Result<IValue> visitPathTailMid(Mid x) {
		Result<IValue> mid = x.getMid().accept(this);
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> tail = x.getTail().accept(this);

		String midString = ((IString) mid.getValue()).getValue();
		String exprString = toString(expr.getValue()).getValue();
		String tailString = ((IString) tail.getValue()).getValue();
		String result = midString + exprString + tailString;

		return makeResult(tf.stringType(), vf.string(result), this);
	}

	@Override
	public Result<IValue> visitPathPartNonInterpolated(
			org.rascalmpl.ast.PathPart.NonInterpolated x) {
		return x.getPathChars().accept(this);
	}

	@Override
	public Result<IValue> visitPreProtocolCharsLexical(
			org.rascalmpl.ast.PreProtocolChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(1, str.length() - 1)), this);
	}

	@Override
	public Result<IValue> visitPostProtocolCharsLexical(
			org.rascalmpl.ast.PostProtocolChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(1, str.length() - 3)), this);
	}

	@Override
	public Result<IValue> visitPathCharsLexical(
			org.rascalmpl.ast.PathChars.Lexical x) {
		String str = x.getString();
		return makeResult(tf.stringType(), vf.string(str.substring(0, str.length() - 1)), this);
	}

	@Override
	public Result<IValue> visitExpressionClosure(Closure x) {
		Type formals = new TypeEvaluator(getCurrentEnvt(), heap).eval(x.getParameters());
		Type returnType = evalType(x.getType());
		RascalTypeFactory RTF = RascalTypeFactory.getInstance();
		return new org.rascalmpl.interpreter.result.RascalFunction(x, this, (FunctionType) RTF.functionType(returnType, formals), x.getParameters().isVarArgs(), x.getStatements(), getCurrentEnvt(),
					accumulators);
	}

	@Override
	public Result<IValue> visitExpressionVoidClosure(VoidClosure x) {
		Type formals = new TypeEvaluator(getCurrentEnvt(), heap).eval(x.getParameters());
		RascalTypeFactory RTF = RascalTypeFactory.getInstance();
		return new org.rascalmpl.interpreter.result.RascalFunction(x, this, (FunctionType) RTF.functionType(tf.voidType(), formals), x.getParameters().isVarArgs(), x.getStatements(), getCurrentEnvt(),
					accumulators);

	}

	@Override
	public Result<IValue> visitExpressionFieldUpdate(FieldUpdate x) {
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> repl = x.getReplacement().accept(this);
		String name = x.getKey().toString();
		return expr.fieldUpdate(name, repl, getCurrentEnvt().getStore());
	}

	@Override
	public Result<IValue> visitExpressionVisit(Expression.Visit x) {
		return x.getVisit().accept(this);
	}

	@Override
	public Result<IValue> visitExpressionLexical(Lexical x) {
		throw new NotYetImplemented(x);// TODO
	}

	@Override
	public Result<IValue> visitExpressionReifyType(ReifyType x) {
		Type t = new TypeEvaluator(getCurrentEnvt(), heap).eval(x.getType());
		return t.accept(new TypeReifier(this, vf));
	}

	@Override
	public Result<IValue> visitExpressionRange(Range x) {
		//IListWriter w = vf.listWriter(tf.integerType());
		Result<IValue> from = x.getFirst().accept(this);
		Result<IValue> to = x.getLast().accept(this);
		return from.makeRange(to);
	}

	@Override
	public Result<IValue> visitExpressionStepRange(StepRange x) {
		Result<IValue> from = x.getFirst().accept(this);
		Result<IValue> to = x.getLast().accept(this);
		Result<IValue> second = x.getSecond().accept(this);
		return from.makeStepRange(to, second);
	}

	@Override
	public Result<IValue> visitExpressionTypedVariable(TypedVariable x) {
		// TODO: should allow qualified names in TypeVariables?!?
		Result<IValue> result = getCurrentEnvt().getVariable(Names.name(x.getName()));

		if (result != null && result.getValue() != null) {
			return result;
		}

		throw new UninitializedVariableError(Names.name(x.getName()), x);
	}
	
	
	@Override
	public Result<IValue> visitLiteralDateTime(
			org.rascalmpl.ast.Literal.DateTime x) {
		return x.getDateTimeLiteral().accept(this);
	}

	@Override
	public Result<IValue> visitDateTimeLiteralDateAndTimeLiteral(
			DateAndTimeLiteral x) {
		return x.getDateAndTime().accept(this);
	}

	@Override
	public Result<IValue> visitDateTimeLiteralDateLiteral(DateLiteral x) {
		return x.getDate().accept(this);
	}

	@Override
	public Result<IValue> visitDateTimeLiteralTimeLiteral(TimeLiteral x) {
		return x.getTime().accept(this);
	}

	@Override
	public Result<IValue> visitDateAndTimeLexical(
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		// Split into date and time components; of the form $<date>T<time>
		String dtPart = x.getString().substring(1); 
		String datePart = dtPart.substring(0,dtPart.indexOf("T"));
		String timePart = dtPart.substring(dtPart.indexOf("T")+1);
		
		return createVisitedDateTime(datePart, timePart, x);
		
	}

	private Result<IValue> createVisitedDateTime(String datePart, String timePart,
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		String isoDate = datePart;
		if (-1 == datePart.indexOf("-")) {
			isoDate = datePart.substring(0,4) + "-" + datePart.substring(4,6) + "-" + 
			          datePart.substring(6);
		}
		String isoTime = timePart;
		if (-1 == timePart.indexOf(":")) {			
			isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2,4) + ":" +
					  timePart.substring(4);
		}
		String isoDateTime = isoDate + "T" + isoTime;
		try {
			DateTime dateAndTime = ISODateTimeFormat.dateTimeParser().parseDateTime(isoDateTime);
			int hourOffset = dateAndTime.getZone().getOffset(dateAndTime.getMillis())/3600000;
			int minuteOffset = (dateAndTime.getZone().getOffset(dateAndTime.getMillis())/60000) % 60;		
			return ResultFactory.makeResult(tf.dateTimeType(),
					vf.datetime(dateAndTime.getYear(), dateAndTime.getMonthOfYear(), 
							dateAndTime.getDayOfMonth(), dateAndTime.getHourOfDay(), 
							dateAndTime.getMinuteOfHour(), dateAndTime.getSecondOfMinute(),
							dateAndTime.getMillisOfSecond(), hourOffset, minuteOffset), this);
		} catch (IllegalArgumentException iae) {
			throw new DateTimeParseError("$" + datePart + "T" + timePart, x.getLocation());
		}
	}

	@Override
	public Result<IValue> visitJustDateLexical(
			org.rascalmpl.ast.JustDate.Lexical x) {
		// Date is of the form $<date>
		String datePart = x.getString().substring(1); 
		return createVisitedDate(datePart,x); 
	}

	private Result<IValue> createVisitedDate(String datePart,
			org.rascalmpl.ast.JustDate.Lexical x) {
		String isoDate = datePart;
		if (-1 == datePart.indexOf("-")) {
			isoDate = datePart.substring(0,4) + "-" + datePart.substring(4,6) + "-" + 
			          datePart.substring(6);
		}
		try {
			DateTime justDate = ISODateTimeFormat.dateParser().parseDateTime(isoDate);
			return ResultFactory.makeResult(tf.dateTimeType(),
					vf.date(justDate.getYear(), justDate.getMonthOfYear(), 
							justDate.getDayOfMonth()), this);
		} catch (IllegalArgumentException iae) {
			throw new DateTimeParseError("$" + datePart, x.getLocation());
		}			
	}

	@Override
	public Result<IValue> visitJustTimeLexical(
			org.rascalmpl.ast.JustTime.Lexical x) {
		// Time is of the form $T<time>
		String timePart = x.getString().substring(2); 
		return createVisitedTime(timePart,x);
	}

	private Result<IValue> createVisitedTime(String timePart,
			org.rascalmpl.ast.JustTime.Lexical x) {
		String isoTime = timePart;
		if (-1 == timePart.indexOf(":")) {			
			isoTime = timePart.substring(0, 2) + ":" + timePart.substring(2,4) + ":" +
					  timePart.substring(4);
		}
		try {
			DateTime justTime = ISODateTimeFormat.timeParser().parseDateTime(isoTime);
			int hourOffset = justTime.getZone().getOffset(justTime.getMillis())/3600000;
			int minuteOffset = (justTime.getZone().getOffset(justTime.getMillis())/60000) % 60;		
			return ResultFactory.makeResult(tf.dateTimeType(),
					vf.time(justTime.getHourOfDay(), justTime.getMinuteOfHour(), justTime.getSecondOfMinute(),
							justTime.getMillisOfSecond(), hourOffset, minuteOffset), this);
		} catch (IllegalArgumentException iae) {
			throw new DateTimeParseError("$T" + timePart, x.getLocation());
		}						
	}


	boolean matchAndEval(Result<IValue> subject, org.rascalmpl.ast.Expression pat, Statement stat){
		boolean debug = false;
		Environment old = getCurrentEnvt();
		pushEnv();

		try {
			IMatchingResult mp = pat.accept(patternEvaluator);
			mp.initMatch(subject);
			if(debug)System.err.println("matchAndEval: subject=" + subject + ", pat=" + pat);
			while(mp.hasNext()){
				pushEnv();
				if(debug)System.err.println("matchAndEval: mp.hasNext()==true");
				if(mp.next()){
					if(debug)System.err.println("matchAndEval: mp.next()==true");
					try {
						checkPoint(getCurrentEnvt());
						if(debug)System.err.println(stat.toString());
						try {
							stat.accept(this);
						} catch (org.rascalmpl.interpreter.control_exceptions.Insert e){
							// Make sure that the match pattern is set
							if(e.getMatchPattern() == null) {
								e.setMatchPattern(mp);
							}
							throw e;
						}
						commit(getCurrentEnvt());
						return true;
					} catch (Failure e){
						if(debug) System.err.println("failure occurred");
						rollback(getCurrentEnvt());
						// unwind(old); // can not clean up because you don't know how far to roll back
					}
				}
			}
		} finally {
			if(debug)System.err.println("Unwind to old env");
			unwind(old);
		}
		return false;
	}

	boolean matchEvalAndReplace(Result<IValue> subject, 
			org.rascalmpl.ast.Expression pat, 
			java.util.List<Expression> conditions,
			Expression replacementExpr){
		Environment old = getCurrentEnvt();
		try {
			IMatchingResult mp = pat.accept(patternEvaluator);
			mp.initMatch(subject);
			//System.err.println("matchEvalAndReplace: subject=" + subject + ", pat=" + pat + ", conditions=" + conditions);

			while (mp.hasNext()){
				//System.err.println("mp.hasNext()==true; mp=" + mp);

				if(mp.next()){
					try {
						boolean trueConditions = true;
						for(Expression cond : conditions){
							//System.err.println("cond = " + cond);
							if(!cond.accept(this).isTrue()){
								trueConditions = false;
								//System.err.println("false cond = " + cond);
								break;
							}
						}
						if(trueConditions){
							//System.err.println("evaluating replacement expression: " + replacementExpr);
							throw new org.rascalmpl.interpreter.control_exceptions.Insert(replacementExpr.accept(this), mp);		
						}
					} catch (Failure e){
						System.err.println("failure occurred");
					}
				}
			}
		} finally {
			unwind(old);
		}
		return false;
	}

	@Override
	public Result<IValue> visitStatementSwitch(Switch x) {
		Result<IValue> subject = x.getExpression().accept(this);

		for(Case cs : x.getCases()){
			if(cs.isDefault()){
				// TODO: what if the default statement uses a fail statement?
				return cs.getStatement().accept(this);
			}
			org.rascalmpl.ast.PatternWithAction rule = cs.getPatternWithAction();
			if(rule.isArbitrary() && matchAndEval(subject, rule.getPattern(), rule.getStatement())){
				return ResultFactory.nothing();
				/*
			} else if(rule.isGuarded())	{
				org.meta_environment.rascal.ast.Type tp = rule.getType();
				Type t = evalType(tp);
				if(subject.getType().isSubtypeOf(t) && matchAndEval(subject.getValue(), rule.getPattern(), rule.getStatement())){
					return ResultFactory.nothing();
				}
				 */
			} else if(rule.isReplacing()){
				throw new NotYetImplemented(rule);
			}
		}
		return null;
	}

	@Override
	public Result<IValue> visitVisitDefaultStrategy(DefaultStrategy x) {

		Result<IValue> subject = x.getSubject().accept(this);
		java.util.List<Case> cases = x.getCases();
		TraversalEvaluator te = new TraversalEvaluator(this);

		TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), 
				DIRECTION.BottomUp,
				PROGRESS.Continuing,
				FIXEDPOINT.No);
		Type t = tr.value.getType();
		IValue val = tr.value;
		TraverseResultFactory.freeTraverseResult(tr);
		return makeResult(t, val, this);
	}

	@Override
	public Result<IValue> visitVisitGivenStrategy(GivenStrategy x) {
		Result<IValue> subject = x.getSubject().accept(this);

		// TODO: warning switched to static type here, but not sure if that's correct...
		Type subjectType = subject.getType();

		if(subjectType.isConstructorType()){
			subjectType = subjectType.getAbstractDataType();
		}

		java.util.List<Case> cases = x.getCases();
		Strategy s = x.getStrategy();

		DIRECTION direction = DIRECTION.BottomUp;
		PROGRESS progress = PROGRESS.Continuing;
		FIXEDPOINT fixedpoint = FIXEDPOINT.No;

		if(s.isBottomUp()){
			direction = DIRECTION.BottomUp;
		} else if(s.isBottomUpBreak()){
			direction = DIRECTION.BottomUp;
			progress = PROGRESS.Breaking;
		} else if(s.isInnermost()){
			direction = DIRECTION.BottomUp;
			fixedpoint = FIXEDPOINT.Yes;
		} else if(s.isTopDown()){
			direction = DIRECTION.TopDown;
		} else if(s.isTopDownBreak()){
			direction = DIRECTION.TopDown;
			progress = PROGRESS.Breaking;
		} else if(s.isOutermost()){
			direction = DIRECTION.TopDown;
			fixedpoint = FIXEDPOINT.Yes;
		} else {
			throw new ImplementationError("Unknown strategy " + s);
		}

		TraversalEvaluator te = new TraversalEvaluator(this);
		TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), direction, progress, fixedpoint);
		Type t = tr.value.getType();
		IValue val = tr.value;
		TraverseResultFactory.freeTraverseResult(tr);
		return makeResult(t, val, this);
	}

	@Override
	public Result<IValue> visitExpressionNonEquals(
			org.rascalmpl.ast.Expression.NonEquals x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.nonEquals(right);
	}


	@Override
	public Result<IValue> visitExpressionLessThan(LessThan x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.lessThan(right);
	}

	@Override
	public Result<IValue> visitExpressionLessThanOrEq(LessThanOrEq x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.lessThanOrEqual(right);
	}
	@Override
	public Result<IValue> visitExpressionGreaterThan(GreaterThan x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.greaterThan(right);
	}

	@Override
	public Result<IValue> visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.greaterThanOrEqual(right);
	}

	@Override
	public Result<IValue> visitExpressionIfThenElse(
			org.rascalmpl.ast.Expression.IfThenElse x) {
		Result<IValue> cval = x.getCondition().accept(this);

		if (!cval.getType().isBoolType()) {
			throw new UnexpectedTypeError(tf.boolType(), cval.getType(), x);
		}

		if (cval.isTrue()) {
			return x.getThenExp().accept(this);
		}

		return x.getElseExp().accept(this);	
	}


	@Override
	public Result<IValue> visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		try {
			return x.getLhs().accept(this);
		}
		catch (UninitializedVariableError e){
			return x.getRhs().accept(this);
		}
		catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			// TODO For now we accept any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return x.getRhs().accept(this);
		}
	}


	@Override
	public Result<IValue> visitExpressionIsDefined(IsDefined x) {
		try {
			x.getArgument().accept(this); // wait for exception
			return makeResult(tf.boolType(), vf.bool(true), this);

		} catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
			// TODO For now we accept any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return makeResult(tf.boolType(), vf.bool(false), this);
		}
	}


	@Override
	public Result<IValue> visitExpressionIn(In x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return right.in(left);
	}

	@Override
	public Result<IValue> visitExpressionNotIn(NotIn x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return right.notIn(left);
	}

	@Override
	public Result<IValue> visitExpressionComposition(Composition x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.compose(right);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveClosure(TransitiveClosure x) {
		return x.getArgument().accept(this).transitiveClosure();
	}

	@Override
	public Result<IValue> visitExpressionTransitiveReflexiveClosure(TransitiveReflexiveClosure x) {
		return x.getArgument().accept(this).transitiveReflexiveClosure();
	}

	// Comprehensions ----------------------------------------------------

	@Override
	public Result<IValue> visitExpressionComprehension(Comprehension x) {
		return x.getComprehension().accept(this);	
	}

	@Override
	public Result<IValue> visitExpressionEnumerator(
			org.rascalmpl.ast.Expression.Enumerator x) {
		Environment old = getCurrentEnvt();
		try {
			IBooleanResult gen = makeBooleanResult(x);
			gen.init();
			pushEnv();
			if(gen.hasNext() && gen.next()) {
				return bool(true, this);
			}
			return bool(false, this);
		} finally {
			unwind(old);
		}
	}

	/*
	 * ComprehensionWriter provides a uniform framework for writing elements
	 * to a list/set/map during the evaluation of a list/set/map comprehension.
	 */

	private abstract class ComprehensionWriter {
		protected Type elementType1;
		protected Type elementType2;
		protected Type resultType;
		protected java.util.List<org.rascalmpl.ast.Expression> resultExprs;
		protected IWriter writer;
		protected Evaluator ev;

		ComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				Evaluator ev){
			this.ev = ev;
			this.resultExprs = resultExprs;
			this.writer = null;
		}

		public void check(Result<IValue> r, Type t, String kind, org.rascalmpl.ast.Expression expr){
			if(!r.getType().isSubtypeOf(t)){
				throw new UnexpectedTypeError(t, r.getType() ,
						expr);
			}
		}

		public IEvaluatorContext getContext(AbstractAST ast) {
			setCurrentAST(ast);
			return Evaluator.this;
		}

		public abstract void append();


		public abstract Result<IValue> done();
	}

	private class ListComprehensionWriter extends
	ComprehensionWriter {

		private boolean splicing[];
		private Result<IValue> rawElements[];
		
		@SuppressWarnings("unchecked")
		ListComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				Evaluator ev) {
			super(resultExprs, ev);
			splicing = new boolean[resultExprs.size()];
			rawElements = new Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements first, and whether or not to splice them, and evaluate them
			if(writer == null) {
				int k = 0;
				elementType1 = tf.voidType();
				
				for(Expression resExpr : resultExprs){
					rawElements[k] = resExpr.accept(ev);
					Type elementType = rawElements[k].getType();
					
					if (elementType.isListType() && !resExpr.isList()){
						elementType = elementType.getElementType();
						splicing[k] = true;
					} 
					else {
						splicing[k] = false;
					}
					elementType1 = elementType1.lub(elementType);
					k++;
				}
				
				resultType = tf.listType(elementType1);		
				writer = resultType.writer(vf);
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (Expression resExpr : resultExprs) {
					rawElements[k++] = resExpr.accept(ev);
				}
			}
			
			// here we finally add the elements
			int k = 0;
			for (Expression resExpr : resultExprs) {
				if(splicing[k]){
					/*
					 * Splice elements of the value of the result expression in the result list
					 */
					if (!rawElements[k].getType().getElementType().isSubtypeOf(elementType1)) {
						throw new UnexpectedTypeError(elementType1, rawElements[k].getType().getElementType(), resExpr);
					}
					
					for(IValue val : ((IList) rawElements[k].getValue())){
						((IListWriter) writer).append(val);
					}
				} else {
					check(rawElements[k], elementType1, "list", resExpr);
					((IListWriter) writer).append(rawElements[k].getValue());
				}
				k++;
			}
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? makeResult(tf.listType(tf.voidType()), vf.list(), getContext(resultExprs.get(0))) : 
				makeResult(tf.listType(elementType1), writer.done(), getContext(resultExprs.get(0)));
		}
	}

	private class SetComprehensionWriter extends
	ComprehensionWriter {
		private boolean splicing[];
		private Result<IValue> rawElements[];
		
		@SuppressWarnings("unchecked")
		SetComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				Evaluator ev) {
			super(resultExprs, ev);
			splicing = new boolean[resultExprs.size()];
			rawElements = new Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements first, and whether or not to splice them, and evaluate them
			if(writer == null) {
				int k = 0;
				elementType1 = tf.voidType();
				
				for(Expression resExpr : resultExprs){
					rawElements[k] = resExpr.accept(ev);
					Type elementType = rawElements[k].getType();
				
					if (elementType.isSetType() && !resExpr.isSet()){
						elementType = elementType.getElementType();
						splicing[k] = true;
					} 
					else {
						splicing[k] = false;
					}
					elementType1 = elementType1.lub(elementType);
					k++;
				}
				
				resultType = tf.setType(elementType1);		
				writer = resultType.writer(vf);
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (Expression resExpr : resultExprs) {
					rawElements[k++] = resExpr.accept(ev);
				}
			}
			
			// here we finally add the elements
			int k = 0;
			for (Expression resExpr : resultExprs) {
				if(splicing[k]){
					/*
					 * Splice elements of the value of the result expression in the result list
					 */
					if (!rawElements[k].getType().getElementType().isSubtypeOf(elementType1)) {
						throw new UnexpectedTypeError(elementType1, rawElements[k].getType().getElementType(), resExpr);
					}
					
					for(IValue val : ((ISet) rawElements[k].getValue())){
						((ISetWriter) writer).insert(val);
					}
				} else {
					check(rawElements[k], elementType1, "list", resExpr);
					((ISetWriter) writer).insert(rawElements[k].getValue());
				}
				k++;
			}
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? makeResult(tf.setType(tf.voidType()), vf.set(), getContext(resultExprs.get(0))) : 
				makeResult(tf.setType(elementType1), writer.done(), getContext(resultExprs.get(0)));
		}
	}

	private class MapComprehensionWriter extends
	ComprehensionWriter {

		MapComprehensionWriter(
				java.util.List<org.rascalmpl.ast.Expression> resultExprs,
				Evaluator ev) {
			super(resultExprs, ev);
			if(resultExprs.size() != 2)
				throw new ImplementationError("Map comprehensions needs two result expressions");
		}

		@Override
		public void append() {
			Result<IValue> r1 = resultExprs.get(0).accept(ev);
			Result<IValue> r2 = resultExprs.get(1).accept(ev);
			if (writer == null) {
				elementType1 = r1.getType();
				elementType2 = r2.getType();
				resultType = tf.mapType(elementType1, elementType2);
				writer = resultType.writer(vf);
			}
			check(r1, elementType1, "map", resultExprs.get(0));
			check(r2, elementType2, "map", resultExprs.get(1));
			((IMapWriter) writer).put(r1.getValue(), r2.getValue());
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? 
					makeResult(tf.mapType(tf.voidType(), tf.voidType()), vf.map(tf.voidType(), tf.voidType()), getContext(resultExprs.get(0)))
					: makeResult(tf.mapType(elementType1, elementType2), writer.done(), getContext(resultExprs.get(0)));
		}
	}


	private static final Name IT = new Name.Lexical(null, "<it>");
	private ParserGenerator parserGenerator;
	
	@Override
	public Result<IValue> visitExpressionIt(It x) {
		Result<IValue> v = getCurrentEnvt().getVariable(IT);
		if (v == null) {
			throw new ItOutsideOfReducer(x);
		}
		return v;
	}
	
	@Override
	public Result<IValue> visitExpressionReducer(Reducer x) {
		return evalReducer(x.getInit(), x.getResult(), x.getGenerators());
	}
	
	public Result<IValue> evalReducer(Expression init, Expression result, java.util.List<Expression> generators) {
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();
		int i = 0;
		
		Result<IValue> it = init.accept(this);

		try {
			gens[0] = makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();

			while (i >= 0 && i < size) {
				if (gens[i].hasNext() && gens[i].next()) {
					if(i == size - 1){
						getCurrentEnvt().storeVariable(IT, it);
						it = result.accept(this);
						unwind(olds[i]);
						pushEnv();
					} 
					else {
						i++;
						gens[i] = makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = getCurrentEnvt();
						pushEnv();
					}
				} else {
					unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			unwind(old);
		}
		return it;
	
	}

	/*
	 * The common comprehension evaluator
	 */

	private Result<IValue> evalComprehension(java.util.List<Expression> generators, 
			ComprehensionWriter w){
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();

			while (i >= 0 && i < size) {
				if (gens[i].hasNext() && gens[i].next()) {
					if(i == size - 1){
						w.append();
						unwind(olds[i]);
						pushEnv();
					} 
					else {
						i++;
						gens[i] = makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = getCurrentEnvt();
						pushEnv();
					}
				} else {
					unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			unwind(old);
		}
		return w.done();
	}

	@Override
	public Result<IValue> visitComprehensionList(org.rascalmpl.ast.Comprehension.List x) {
		return evalComprehension(
				x.getGenerators(),
				new ListComprehensionWriter(x.getResults(), this));
	}

	@Override
	public Result<IValue> visitComprehensionSet(
			org.rascalmpl.ast.Comprehension.Set x) {
		return evalComprehension(
				x.getGenerators(),
				new SetComprehensionWriter(x.getResults(), this));
	}

	@Override
	public Result<IValue> visitComprehensionMap(
			org.rascalmpl.ast.Comprehension.Map x) {
		java.util.List<Expression> resultExprs = new ArrayList<Expression>();
		resultExprs.add(x.getFrom());
		resultExprs.add(x.getTo());
		return evalComprehension(
				x.getGenerators(),
				new MapComprehensionWriter(resultExprs, this));
	}

	@Override
	public Result<IValue> visitStatementFor(For x) {
		Statement body = x.getBody();
		java.util.List<Expression> generators = x.getGenerators();
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment old = getCurrentEnvt();
		Environment[] olds = new Environment[size];

		Result<IValue> result = makeResult(tf.voidType(), vf.list(), this);

		String label = null;
		if (!x.getLabel().isEmpty()) {
			label = Names.name(x.getLabel().getName());
		}
		accumulators.push(new Accumulator(vf, label));


		// TODO: does this prohibit that the body influences the behavior of the generators??

		int i = 0;
		boolean normalCflow = false;
		try {
			gens[0] = makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();

			while(i >= 0 && i < size) {		
				if(gens[i].hasNext() && gens[i].next()){
					if(i == size - 1){
						// NB: no result handling here.
						body.accept(this);
					} else {
						i++;
						gens[i] = makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = getCurrentEnvt();
						pushEnv();
					}
				} else {
					unwind(olds[i]);
					i--;
					pushEnv();
				}
			}
			// TODO: this is not enough, we must also detect
			// break and return a list result then.
			normalCflow = true;
		} finally {
			IValue value = accumulators.pop().done();
			if (normalCflow) {
				result = makeResult(value.getType(), value, this);
			}
			unwind(old);
		}
		return result;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Result visitExpressionAny(Any x) {
		java.util.List<Expression> generators = x.getGenerators();
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];

		int i = 0;
		gens[0] = makeBooleanResult(generators.get(0));
		gens[0].init();
		while (i >= 0 && i < size) {
			if (gens[i].hasNext() && gens[i].next()) {
				if (i == size - 1) {
					return new BoolResult(tf.boolType(), vf.bool(true), this);
				}

				i++;
				gens[i] = makeBooleanResult(generators.get(i));
				gens[i].init();
			} else {
				i--;
			}
		}
		return new BoolResult(tf.boolType(), vf.bool(false), this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result visitExpressionAll(All x) {
		java.util.List<Expression> producers = x.getGenerators();
		int size = producers.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = makeBooleanResult(producers.get(0));
			gens[0].init();
			olds[0] = getCurrentEnvt();
			pushEnv();

			while (i >= 0 && i < size) {
				if (gens[i].hasNext()) {
					if (!gens[i].next()) {
						return new BoolResult(tf.boolType(), vf.bool(false), this);
					}
					
					if(i == size - 1){
						unwind(olds[i]);
						pushEnv();
					} 
					else {
						i++;
						gens[i] = makeBooleanResult(producers.get(i));
						gens[i].init();
						olds[i] = getCurrentEnvt();
						pushEnv();
					}
				} else {
					unwind(olds[i]);
					i--;
				}
			}
		}
		finally {
			unwind(old);
		}
		
		return new BoolResult(tf.boolType(), vf.bool(true), this);
	}

	// ------------ solve -----------------------------------------

	@Override
	public Result<IValue> visitStatementSolve(Solve x) {
		int size = x.getVariables().size();
		QualifiedName vars[] = new QualifiedName[size];
		IValue currentValue[] = new IValue[size];

		Environment old = getCurrentEnvt();

		try {
			java.util.List<QualifiedName> varList = x.getVariables();

			for (int i = 0; i < size; i++) {
				QualifiedName var = varList.get(i);
				vars[i] = var;
				if (getCurrentEnvt().getVariable(var) == null) {
					throw new UndeclaredVariableError(var.toString(), var);
				}
				if (getCurrentEnvt().getVariable(var).getValue() == null) {
					throw new UninitializedVariableError(var.toString(), var);
				}
				currentValue[i] = getCurrentEnvt().getVariable(var).getValue();
			}

			pushEnv();
			Statement body = x.getBody();

			int max = -1;

			Bound bound= x.getBound();
			if(bound.isDefault()){
				Result<IValue> res = bound.getExpression().accept(this);
				if(!res.getType().isIntegerType()){
					throw new UnexpectedTypeError(tf.integerType(),res.getType(), x);
				}
				max = ((IInteger)res.getValue()).intValue();
				if(max <= 0){
					throw RuntimeExceptionFactory.indexOutOfBounds((IInteger) res.getValue(), getCurrentAST(), getStackTrace());
				}
			}

			Result<IValue> bodyResult = null;

			boolean change = true;
			int iterations = 0;

			while (change && (max == -1 || iterations < max)){
				change = false;
				iterations++;
				bodyResult = body.accept(this);
				for(int i = 0; i < size; i++){
					QualifiedName var = vars[i];
					Result<IValue> v = getCurrentEnvt().getVariable(var);
					if(currentValue[i] == null || !v.getValue().isEqual(currentValue[i])){
						change = true;
						currentValue[i] = v.getValue();
					}
				}
			}
			return bodyResult;
		}
		finally {
			unwind(old);
		}
	}

	public Result<IValue> visitShellCommandSetOption(ShellCommand.SetOption x){
		String name = "rascal.config."+x.getName().toString();
		String value = x.getExpression().accept(this).getValue().toString();

		System.setProperty(name, value);

		updateProperties();

		return ResultFactory.nothing();
	}

	private void updateProperties(){
		String profiling = System.getProperty("rascal.config.profiling");
		if(profiling != null) {
			doProfiling = profiling.equals("true");
		}
		else {
			doProfiling = false;
		}

		String tracing = System.getProperty("rascal.config.tracing");
		if(tracing != null) {
			AbstractFunction.setCallTracing(tracing.equals("true"));
		}
		else {
			AbstractFunction.setCallTracing(false);
		}
		
		String binaryWriting = System.getProperty("rascal.config.saveBinaries");
	    if (binaryWriting != null) {
	    	saveParsedModules = binaryWriting.equals("true");
	    }
	    else {
	    	saveParsedModules = false;
	    }
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

	public Evaluator getEvaluator() {
		return this;
	}

	public GlobalEnvironment getHeap() {
		return heap;
	}

	public boolean runTests(){
		DefaultTestResultListener listener = new DefaultTestResultListener(stderr);
		new TestEvaluator(this, listener).test();
		return listener.allOk();
	}

	public IValueFactory getValueFactory() {
		return vf;
	}

	public void setIValueFactory(
			IValueFactory factory) {
		vf = factory;
	}

	public IStrategyContext getStrategyContext(){
		return strategyContextStack.getCurrentContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext){
		strategyContextStack.pushContext(strategyContext);
	}

	public void popStrategyContext(){
		strategyContextStack.popContext();
	}

	public void setStdErr(PrintWriter printWriter) {
		stderr = printWriter;
	}  
	
	public void setStdOut(PrintWriter printWriter) {
		stdout = printWriter;
	}

	public void setAccumulators(Accumulator accu) {
		accumulators.push(accu);
	}

	public Stack<Accumulator> getAccumulators() {
		return accumulators;
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		this.accumulators = accumulators;
	}
	
	private IString toString(IValue value)
	{
		if (value.getType() == Factory.Tree) {
			return vf.string(TreeAdapter.yield((IConstructor) value));
		}
		if (value.getType().isStringType()) {
			return (IString) value;
		}
		return vf.string(value.toString());
	}


	

	
}
