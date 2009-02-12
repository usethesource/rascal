package org.meta_environment.rascal.interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Bound;
import org.meta_environment.rascal.ast.Case;
import org.meta_environment.rascal.ast.Catch;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.Declarator;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Field;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.FunctionModifier;
import org.meta_environment.rascal.ast.Generator;
import org.meta_environment.rascal.ast.Import;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Replacement;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.ast.Toplevel;
import org.meta_environment.rascal.ast.TypeArg;
import org.meta_environment.rascal.ast.TypeVar;
import org.meta_environment.rascal.ast.ValueProducer;
import org.meta_environment.rascal.ast.Variant;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated;
import org.meta_environment.rascal.ast.Declaration.Annotation;
import org.meta_environment.rascal.ast.Declaration.Data;
import org.meta_environment.rascal.ast.Declaration.Function;
import org.meta_environment.rascal.ast.Declaration.Rule;
import org.meta_environment.rascal.ast.Declaration.Tag;
import org.meta_environment.rascal.ast.Declaration.Variable;
import org.meta_environment.rascal.ast.Declaration.View;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.All;
import org.meta_environment.rascal.ast.Expression.Ambiguity;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Any;
import org.meta_environment.rascal.ast.Expression.Area;
import org.meta_environment.rascal.ast.Expression.AreaInFileLocation;
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Closure;
import org.meta_environment.rascal.ast.Expression.ClosureCall;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Division;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.FieldProject;
import org.meta_environment.rascal.ast.Expression.FieldUpdate;
import org.meta_environment.rascal.ast.Expression.FileLocation;
import org.meta_environment.rascal.ast.Expression.FunctionAsValue;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.IfDefined;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.Intersection;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.Lexical;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Match;
import org.meta_environment.rascal.ast.Expression.Modulo;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Negative;
import org.meta_environment.rascal.ast.Expression.NoMatch;
import org.meta_environment.rascal.ast.Expression.NonEmptyBlock;
import org.meta_environment.rascal.ast.Expression.NotIn;
import org.meta_environment.rascal.ast.Expression.OperatorAsValue;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.Product;
import org.meta_environment.rascal.ast.Expression.Range;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.StepRange;
import org.meta_environment.rascal.ast.Expression.Subscript;
import org.meta_environment.rascal.ast.Expression.Subtraction;
import org.meta_environment.rascal.ast.Expression.TransitiveClosure;
import org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.Expression.Visit;
import org.meta_environment.rascal.ast.Expression.VoidClosure;
import org.meta_environment.rascal.ast.Header.Parameters;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.Literal.Real;
import org.meta_environment.rascal.ast.LocalVariableDeclaration.Default;
import org.meta_environment.rascal.ast.Rule.Arbitrary;
import org.meta_environment.rascal.ast.Rule.Guarded;
import org.meta_environment.rascal.ast.Rule.Replacing;
import org.meta_environment.rascal.ast.Statement.Assert;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Block;
import org.meta_environment.rascal.ast.Statement.Break;
import org.meta_environment.rascal.ast.Statement.Continue;
import org.meta_environment.rascal.ast.Statement.DoWhile;
import org.meta_environment.rascal.ast.Statement.EmptyStatement;
import org.meta_environment.rascal.ast.Statement.Fail;
import org.meta_environment.rascal.ast.Statement.For;
import org.meta_environment.rascal.ast.Statement.GlobalDirective;
import org.meta_environment.rascal.ast.Statement.IfThen;
import org.meta_environment.rascal.ast.Statement.IfThenElse;
import org.meta_environment.rascal.ast.Statement.Insert;
import org.meta_environment.rascal.ast.Statement.Solve;
import org.meta_environment.rascal.ast.Statement.Switch;
import org.meta_environment.rascal.ast.Statement.Throw;
import org.meta_environment.rascal.ast.Statement.Try;
import org.meta_environment.rascal.ast.Statement.TryFinally;
import org.meta_environment.rascal.ast.Statement.VariableDeclaration;
import org.meta_environment.rascal.ast.Statement.While;
import org.meta_environment.rascal.ast.Toplevel.DefaultVisibility;
import org.meta_environment.rascal.ast.Toplevel.GivenVisibility;
import org.meta_environment.rascal.ast.Visit.DefaultStrategy;
import org.meta_environment.rascal.ast.Visit.GivenStrategy;
import org.meta_environment.rascal.errors.ErrorAdapter;
import org.meta_environment.rascal.errors.SubjectAdapter;
import org.meta_environment.rascal.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.IterableEvalResult;
import org.meta_environment.rascal.interpreter.env.JavaFunction;
import org.meta_environment.rascal.interpreter.env.Lambda;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.env.RascalFunction;
import org.meta_environment.rascal.interpreter.env.Result;
import org.meta_environment.rascal.interpreter.exceptions.FailureException;
import org.meta_environment.rascal.interpreter.exceptions.InsertException;
import org.meta_environment.rascal.interpreter.exceptions.RascalAssertionException;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.RascalRunTimeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalUndefinedValue;
import org.meta_environment.rascal.interpreter.exceptions.ReturnException;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class Evaluator extends NullASTVisitor<Result> {
	private static final String[] SEARCH_PATH = {
							"src/StandardLibrary/", 
							"src/test/", 
							"src/benchmark/",
							"src/benchmark/BubbleSort/",
							"src/benchmark/Factorial/",
							"src/benchmark/Fibonacci/",
							"src/benchmark/Reverse/",
							"src/benchmark/UnusedProcs/",
							"demo/",
							"demo/Booleans/",
							"demo/Fun/",
							"demo/Graph/",
							"demo/Integers/",
							"demo/JavaFun/",					
							"demo/Lexicals/",
							"demo/Misc/",
							"demo/Pico/",
							"demo/PicoAbstract/",
							"demo/Rascal/"
					};
	public static final String RASCAL_FILE_EXT = ".rsc";
	final IValueFactory vf;
	final TypeFactory tf = TypeFactory.getInstance();
	private final TypeEvaluator te = TypeEvaluator.getInstance();
	private final java.util.ArrayDeque<Environment> callStack;
	private final GlobalEnvironment heap;
	private final java.util.ArrayDeque<ModuleEnvironment> scopeStack;
	
	private final ASTFactory af;
	private final JavaBridge javaBridge;
	
	// TODO: can we remove this?
	protected MatchPattern lastPattern;	// The most recent pattern applied in a match
	                                    	// For the benefit of string matching.

	public Evaluator(IValueFactory f, ASTFactory astFactory, Writer errorWriter, ModuleEnvironment scope) {
		this.vf = f;
		this.af = astFactory;
		this.javaBridge = new JavaBridge(errorWriter);
		this.heap = new GlobalEnvironment();
		this.callStack = new ArrayDeque<Environment>();
		this.callStack.push(scope);
		this.scopeStack = new ArrayDeque<ModuleEnvironment>();
		this.scopeStack.push(scope);
	}
	
	
	private Environment peek() {
		return this.callStack.peek();
	}
	
	/**
	 * Evaluate a statement
	 * @param stat
	 * @return
	 */
	public IValue eval(Statement stat) {
		try {
			Result r = stat.accept(this);
	        if(r != null){
	        	return r.value;
	        } else {
	        	throw new RascalBug("Not yet implemented: " + stat.getTree());
	        }
		} catch (ReturnException e){
			throw new RascalRunTimeError("Unhandled return statement");
		}
		catch (FailureException e){
			throw new RascalRunTimeError("Unhandled fail statement");
		}
		catch (InsertException e){
			throw new RascalRunTimeError("Unhandled insert statement");
		}
	}
	
	/**
	 * Evaluate a declaration
	 * @param declaration
	 * @return
	 */
	public IValue eval(Declaration declaration) {
		Result r = declaration.accept(this);
        if(r != null){
        	return r.value;
        } else {
        	throw new RascalBug("Not yet implemented: " + declaration.getTree());
        }
	}
	
	/**
	 * Evaluate an import
	 * @param imp
	 * @return
	 */
	public IValue eval(org.meta_environment.rascal.ast.Import imp) {
		Result r = imp.accept(this);
        if(r != null){
        	return r.value;
        } else {
        	throw new RascalBug("Not yet implemented: " + imp.getTree());
        }
	}

	/* First a number of general utility methods */
	
	/*
	 * Return an evaluation result that is already in normal form,
	 * i.e., all potential rules have already been applied to it.
	 */
	
	Result normalizedResult(Type t, IValue v){
		Map<Type, Type> bindings = peek().getTypeBindings();
		Type instance;
		
		if (bindings.size() > 0) {
		    instance = t.instantiate(bindings);
		}
		else {
			instance = t;
		}
		
		if (v != null) {
			checkType(v.getType(), instance);
		}
		return new Result(instance, v);
	}
	
	/*
	 * Return an evaluation result that may need normalization.
	 */
	
	Result result(Type t, IValue v) {
		Map<Type, Type> bindings = peek().getTypeBindings();
		Type instance;
		
		if (bindings.size() > 0) {
		    instance = t.instantiate(bindings);
		}
		else {
			instance = t;
		}
		
		if (v != null) {
			checkType(v.getType(), instance);
			// rewrite rules do not change the declared type
			return new Result(instance, applyRules(v));
		}
		return new Result(instance, v);
	}

	Result result(IValue v) {
		Type type = v.getType();
		
		if (type.isRelationType() 
				|| type.isSetType() 
				|| type.isMapType()
				|| type.isListType()) {
			throw new RascalBug("Should not used run-time type for type checking!!!!");
		}
		if(v != null){
			return new Result(type, applyRules(v));
		} else {
			return new Result(null, v);
		}
	}
	
	
	private IValue applyRules(IValue v) {
		
		//System.err.println("applyRules(" + v + ")");
		// we search using the run-time type of a value
		Type typeToSearchFor = v.getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) v).getConstructorType();
		}
		
		java.util.List<org.meta_environment.rascal.ast.Rule> rules = heap.getRules(typeToSearchFor);
		if(rules.isEmpty()){
			return v;
		}
		
		callStack.push(scopeStack.peek());
		try {
			TraverseResult tr = traverse(v, new CasesOrRules(rules), 
					/* bottomup */ true,  
					/* breaking */ false, 
					/* fixedpoint */ false);  
					/* innermost is achieved by repeated applications of applyRules
					 * when intermediate results are produced.
					 */
			return tr.value;
		} finally {
			callStack.pop();
		}
	}


	private void pop() {
		callStack.pop();
	}


	private void push() {
		callStack.push(new Environment(peek()));
	}
	
	private Result result() {
		return new Result(null, null);
	}
	
	private void checkInteger(Result val) {
		checkType(val, tf.integerType());
	}
	
	private void checkReal(Result val) {
		checkType(val, tf.doubleType());
	}
	
	private void checkString(Result val) {
		checkType(val, tf.stringType());
	}
	
	private void checkType(Result val, Type expected) {
		checkType(val.type, expected);
	}
	
	private void checkType(Type given, Type expected) {
		if (expected == org.meta_environment.rascal.interpreter.env.Lambda.getClosureType()) {
			return;
		}
		if (!given.isSubtypeOf(expected)){
			throw new RascalTypeError("Expected " + expected + ", got " + given);
		}
	}
	
	private int intValue(Result val) {
		checkInteger(val);
		return ((IInteger) val.value).getValue();
	}
	
	private double RealValue(Result val) {
		checkReal(val);
		return ((IDouble) val.value).getValue();
	}
	
	private String stringValue(Result val) {
		checkString(val);
		return ((IString) val.value).getValue();
	}
	

	// Ambiguity ...................................................
	
	@Override
	public Result visitExpressionAmbiguity(Ambiguity x) {
		throw new RascalBug("Ambiguous expression: " + x);
	}
	
	@Override
	public Result visitStatementAmbiguity(
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
		throw new RascalBug("Ambiguous statement: " + x);
	}
	
	// Modules -------------------------------------------------------------
	
	@Override
	public Result visitImportDefault(
			org.meta_environment.rascal.ast.Import.Default x) {
		// TODO support for full complexity of import declarations
		String name = x.getModule().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		if (!heap.existsModule(name)) {
			loadModule(x, name).accept(this);
		}
		scopeStack.peek().addImport(name, heap.getModule(name));
		return result();
	}


	private Module loadModule(org.meta_environment.rascal.ast.Import.Default x,
			String name) {
		Parser p = Parser.getInstance();
		ASTBuilder b = new ASTBuilder(af);

		try {
			String fileName = name.replaceAll("::","/") + RASCAL_FILE_EXT;
			fileName = Names.unescape(fileName);
			File file = new File(fileName);

			// TODO: support proper search path for modules
			// TODO: support properly packaged/qualified module names
			// TODO: mind the / at the end of each directory!
			String searchPath[] = SEARCH_PATH;

			for(int i = 0; i < searchPath.length; i++){
				file = new File(searchPath[i] + fileName);
				if(file.exists()){
					break;
				}
			}
			if (!file.exists()) {
				throw new RascalTypeError("Can not find file for module " + name, x);
			}

			IConstructor tree = p.parseFromFile(file);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw new RascalTypeError(parseError(tree, name));
			}

			return b.buildModule(tree);			
		} catch (FactTypeError e) {
			throw new RascalTypeError("Something went wrong during parsing of " + name + ": ", e);
		} catch (FileNotFoundException e) {
			throw new RascalTypeError("Could not import module", e);
		} catch (IOException e) {
			throw new RascalTypeError("Could not import module", e);
		}
	}

	private String parseError(IConstructor tree, String file) {
		ISourceRange range = getErrorRange(new SummaryAdapter(tree));
		
	    return "parse error in " + file + " at line " + range.getEndLine() + ", column " + range.getEndColumn() + "\n";
	}
	
	private ISourceRange getErrorRange(SummaryAdapter summaryAdapter) {
		for (ErrorAdapter error : summaryAdapter) {
			for (SubjectAdapter subject : error) {
				if (subject.isLocalized()) {
					return subject.getRange();
				}
			}
		}
		
		return null;
	}
	
	@Override 
	public Result visitModuleDefault(
			org.meta_environment.rascal.ast.Module.Default x) {
		String name = x.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}

		if (!heap.existsModule(name)) {
			ModuleEnvironment env = heap.addModule(name);
			scopeStack.push(env);
			callStack.push(env); // such that declarations end up in the module scope
			
			try {
				x.getHeader().accept(this);

				java.util.List<Toplevel> decls = x.getBody().getToplevels();
				for (Toplevel l : decls) {
					l.accept(this);
				}
			}
			finally {
				scopeStack.pop();
				callStack.pop();
			}
		}
		
		return result();
	}

	@Override
	public Result visitHeaderDefault(
			org.meta_environment.rascal.ast.Header.Default x) {
		visitImports(x.getImports());
		return result();
	}
	
	private void visitImports(java.util.List<Import> imports) {
		for (Import i : imports) {
			i.accept(this);
		}
	}
	
	@Override
	public Result visitHeaderParameters(Parameters x) {
		visitImports(x.getImports());
		return result();
	}
	
	@Override
	public Result visitToplevelDefaultVisibility(DefaultVisibility x) {
		Result r = x.getDeclaration().accept(this);
		r.setPublic(false);
		return r;
	}

	@Override
	public Result visitToplevelGivenVisibility(GivenVisibility x) {
		Result r = x.getDeclaration().accept(this);
		r.setPublic(x.getVisibility().isPublic());
		return r;
	}
	
	@Override
	public Result visitDeclarationFunction(Function x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	@Override
	public Result visitDeclarationVariable(Variable x) {
		Type declaredType = te.eval(x.getType(), scopeStack.peek());
		Result r = result();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			if (var.isUnInitialized()) {  
				throw new RascalTypeError("Module variable " + var + " is not initialized", var);
			} else {
				Result v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.type, bindings);
					declaredType = declaredType.instantiate(bindings);
					r = normalizedResult(declaredType, v.value);
					scopeStack.peek().storeVariable(var.getName(), r);
				} else {
					throw new RascalTypeError("Variable " + declaredType + " " + var + " incompatible with initial type " + v.type, var);
				}
			}
		}
		
		return r;
	}
	
	@Override
	public Result visitDeclarationAnnotation(Annotation x) {
		Type annoType = te.eval(x.getType(), scopeStack.peek());
		String name = x.getName().toString();
		
		for (org.meta_environment.rascal.ast.Type type : x.getTypes()) {
		  Type onType = te.eval(type, scopeStack.peek());
		  tf.declareAnnotation(onType, name, annoType);	
		  scopeStack.peek().storeAnnotation(onType, name, annoType);
		}
		
		return result();
	}
	
	@Override
	public Result visitDeclarationData(Data x) {
		String name = x.getUser().getName().toString();
		Type sort = tf.abstractDataType(name);
		scopeStack.peek().storeAbstractDataType(sort);
		
		for (Variant var : x.getVariants()) {
			String altName = Names.name(var.getName());
			
		    if (var.isNAryConstructor()) {
		    	java.util.List<TypeArg> args = var.getArguments();
		    	Type[] fields = new Type[args.size()];
		    	String[] labels = new String[args.size()];

		    	for (int i = 0; i < args.size(); i++) {
		    		TypeArg arg = args.get(i);
					fields[i] = te.eval(arg.getType(), scopeStack.peek());
					
					if (arg.hasName()) {
						labels[i] = arg.getName().toString();
					}
					else {
						labels[i] = "arg" + java.lang.Integer.toString(i);
					}
		    	}

		    	Type children = tf.tupleType(fields, labels);
		    	scopeStack.peek().storeConstructor(tf.constructorFromTuple(sort, altName, children));
		    }
		    else if (var.isNillaryConstructor()) {
		    	scopeStack.peek().storeConstructor(tf.constructor(sort, altName, new Object[] { }));
		    }
		}
		
		return result();
	}
	
	@Override
	public Result visitDeclarationAlias(
			org.meta_environment.rascal.ast.Declaration.Alias x) {
		// TODO add support for parameterized types
		String user = x.getUser().getName().toString();
		Type[] params;
		if (x.getUser().isParametric()) {
			java.util.List<org.meta_environment.rascal.ast.Type> formals = x.getUser().getParameters();
			params = new Type[formals.size()];
			int i = 0;
			for (org.meta_environment.rascal.ast.Type formal : formals) {
				if (!formal.isVariable()) {
					throw new RascalTypeError("Declaration of parameterized type with type instance " + formal + " is not allowed", formal);
				}
				TypeVar var = formal.getTypeVar();
				Type bound = var.hasBound() ? evalType(var.getBound()) : tf.valueType();
				params[i++] = tf.parameterType(Names.name(var.getName()), bound);
			}
		}
		else {
			params = new Type[0];
		}
		Type base = evalType(x.getBase());
		Type decl = tf.aliasType(user, base, params);
		scopeStack.peek().storeTypeAlias(decl);
		return result();
	}
	
	private Type evalType(org.meta_environment.rascal.ast.Type type) {
		return te.eval(type, peek());
	}
	
	@Override
	public Result visitDeclarationView(View x) {
		// TODO implement
		throw new RascalBug("views are not yet implemented");
	}
	
	@Override
	public Result visitDeclarationRule(Rule x) {
		return x.getRule().accept(this);
	}
	
	@Override
	public Result visitRuleArbitrary(Arbitrary x) {
		MatchPattern pv = x.getPattern().accept(makePatternEvaluator());
		//System.err.println("visitRule: " + pv.getType(this));
		heap.storeRule(pv.getType(scopeStack.peek()), x);
		return result();
	}


	private AbstractPatternEvaluator makePatternEvaluator() {
		return new AbstractPatternEvaluator(vf, peek(), peek(), this);
	}
	
	@Override
	public Result visitRuleReplacing(Replacing x) {
		MatchPattern pv = x.getPattern().accept(makePatternEvaluator());
		//System.err.println("visitRule: " + pv.getType(this));
		heap.storeRule(pv.getType(scopeStack.peek()), x);
		return result();
	}
	
	@Override
	public Result visitRuleGuarded(Guarded x) {
		//TODO adapt to new scheme
		Result result = x.getRule().getPattern().getPattern().accept(this);
		if (!result.type.isSubtypeOf(evalType(x.getType()))) {
			throw new RascalTypeError("Declared type of rule does not match type of left-hand side", x);
		}
		return x.getRule().accept(this);
	}
	
	@Override
	public Result visitDeclarationTag(Tag x) {
		throw new RascalBug("tags are not yet implemented");
	}
	
	// Variable Declarations -----------------------------------------------

	@Override
	public Result visitLocalVariableDeclarationDefault(Default x) {
		// TODO deal with dynamic variables
		return x.getDeclarator().accept(this);
	}

	@Override
	public Result visitDeclaratorDefault(
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type declaredType = evalType(x.getType());
		Result r = result();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			if (var.isUnInitialized()) {  // variable declaration without initialization
				r = result(declaredType, null);
				peek().storeVariable(var.getName(), r);
			} else {                     // variable declaration with initialization
				Result v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.type, bindings);
					declaredType = declaredType.instantiate(bindings);
					r = result(declaredType, v.value);
					peek().storeVariable(var.getName(), r);
				} else {
					throw new RascalTypeError("Variable " + declaredType + " " + var.getName() + " incompatible with initialization type " + v.type, var);
				}
			}
		}
		return r;
	}
	
	// Function calls and node constructors
	
	@Override
	public Result visitClosureAsFunctionEvaluated(Evaluated x) {
		Expression expr = x.getExpression();
		
		if (expr.isQualifiedName()) {
			
		}
		
		return result(vf.string(Names.name(Names.lastName(expr.getQualifiedName()))));
	}
	
	@Override
	public Result visitExpressionClosureCall(ClosureCall x) {
		Result func = x.getClosure().getExpression().accept(this);
		java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();

		IValue[] actuals = new IValue[args.size()];
		Type[] types = new Type[args.size()];

		for (int i = 0; i < args.size(); i++) {
			Result resultElem = args.get(i).accept(this);
			types[i] = resultElem.type;
			actuals[i] = resultElem.value;
		}

		Type actualTypes = tf.tupleType(types);

		if (func.type == Lambda.getClosureType()) {
			Lambda lambda = (Lambda) func.value;
			try {
				pushCallFrame(lambda.getEnv()); 
				return lambda.call(actuals, actualTypes, peek());
			}
			finally {
				pop();
			}
		}
		else {
			throw new RascalTypeError("Expected a closure, a function or an operator, but got a " + func.type, x);
		}
	}
	
	@Override
	public Result visitExpressionCallOrTree(CallOrTree x) {
		 java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();
		 QualifiedName name = x.getQualifiedName();
		 
		 IValue[] actuals = new IValue[args.size()];
		 Type[] types = new Type[args.size()];

		 for (int i = 0; i < args.size(); i++) {
			 Result resultElem = args.get(i).accept(this);
			 types[i] = resultElem.type;
			 actuals[i] = resultElem.value;
		 }
		 
		 Type signature = tf.tupleType(types);
		 
		 if (isTreeConstructorName(name, signature)) {
			 return constructTree(name, actuals, signature);
		 }
		 else {
			 return call(name, actuals, signature);
		 }
	}
	
	private Result call(QualifiedName name, IValue[] actuals, Type actualTypes) {
		String moduleName = Names.moduleName(name);
		Environment env;
		
		if (moduleName == null) {
			env = peek();
		}
		else {
			env = peek().getImport(moduleName);
			if (env == null) {
				throw new RascalTypeError("unknown module " + moduleName, name);
			}
		}
		Lambda func = env.getFunction(Names.name(Names.lastName(name)), actualTypes);
		
		if (func != null) {
			try {
				pushCallFrame(func.getEnv());
				return func.call(actuals, actualTypes, peek());
			}
			finally {
				pop();
			}
		}
		undefinedFunctionException(name, actualTypes);
		return null;
	}


	private void pushCallFrame(Environment env) {
		callStack.push(new Environment(env));
	}

	private void undefinedFunctionException(QualifiedName name, Type actualTypes) {
		StringBuffer sb = new StringBuffer();
		String sep = "";
		for(int i = 0; i < actualTypes.getArity(); i++){
			sb.append(sep);
			sep = ", ";
			sb.append(actualTypes.getFieldType(i).toString());
		}
		throw new RascalTypeError("No function/constructor " + name + "(" +  sb.toString() + ") is defined", name);
	}

	private boolean isTreeConstructorName(QualifiedName name, Type signature) {
		return peek().isTreeConstructorName(name, signature);
	}
	
	/**
	 * A top-down algorithm is needed to type check a constructor call since the
	 * result type of constructors can be overloaded. We bring down the expected type
	 * of each argument.
	 * @param expected
	 * @param functionName
	 * @param args
	 * @return
	 * 
	 * TODO: code does not deal with import structure, rather data def's are global.
	 */
	private Result constructTree(QualifiedName functionName, IValue[] actuals, Type signature) {
		String sort;
		String cons;
		
		cons = Names.consName(functionName);
		sort = Names.sortName(functionName);

		Type candidate = null;
	
		if (sort != null) {
			Type sortType = peek().getAbstractDataType(sort);
			
			if (sortType != null) {
			  candidate = peek().getConstructor(sortType, cons, signature);
			}
			else {
			  return result(tf.nodeType(), vf.node(cons, actuals));
			}
		}
		
		candidate = peek().getConstructor(cons, signature);
		if (candidate != null) {
			return result(candidate.getAbstractDataType(), candidate.make(vf, actuals));
		}
		
		return result(tf.nodeType(), vf.node(cons, actuals));
	}
	
	@Override
	public Result visitExpressionFunctionAsValue(FunctionAsValue x) {
		return x.getFunction().accept(this);
	}
	
	@Override
	public Result visitFunctionAsValueDefault(
			org.meta_environment.rascal.ast.FunctionAsValue.Default x) {
		Name name = x.getName();
		
		//TODO is this a bug, what if name was overloaded?
		//TODO add support for typed function names
		Lambda func = peek().getFunction(Names.name(name), tf.voidType());
		
		if (func == null) {
			throw new RascalTypeError("Could not find function " + name, x);
		}
		
		return func;
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
	public Result visitFunctionBodyDefault(
			org.meta_environment.rascal.ast.FunctionBody.Default x) {
		Result result = result();
		
		for (Statement statement : x.getStatements()) {
			result = statement.accept(this);
		}
		
		return result;
	}
	
   // Statements ---------------------------------------------------------
	
	@Override
	public Result visitStatementAssert(Assert x) {
		Result r = x.getExpression().accept(this);
		if (!r.type.equals(tf.boolType())) {
			throw new RascalTypeError("Expression in assertion should be bool instead of " + r.type, x);	
		}
		
		if(r.value.isEqual(vf.bool(false))){
			throw new RascalAssertionException(x.getMessage().toString());
		}
		return r;	
	}
	
	@Override
	public Result visitStatementVariableDeclaration(VariableDeclaration x) {
		return x.getDeclaration().accept(this);
	}
	
	@Override
	public Result visitStatementExpression(Statement.Expression x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result visitStatementFunctionDeclaration(
			org.meta_environment.rascal.ast.Statement.FunctionDeclaration x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	Result assignVariable(QualifiedName name, Result right){
		Environment env = getEnv(name);
		Result previous = env.getVariable(name);
		
		if (previous != null) {
			if (right.type.isSubtypeOf(previous.type)) {
				right.type = previous.type;
			} else {
				throw new RascalTypeError("Variable " + name
						+ " has type " + previous.type
						+ "; cannot assign value of type " + right.type, name);
			}
		}
		
		env.storeVariable(name, right);
		return right;
	}


	private Environment getEnv(QualifiedName name) {
		String moduleName = Names.moduleName(name);
		Environment env;
		
		if (moduleName == null) {
			env = peek();
		}
		else {
			env = peek().getImport(moduleName);
		}
		return env;
	}
	
	@Override
	public Result visitExpressionSubscript(Subscript x) {
		
		Result expr = x.getExpression().accept(this);
		Type exprType = expr.type;
		int nSubs = x.getSubscripts().size();
		
		if (exprType.isRelationType()) {
			int relArity = exprType.getArity();
			
			if(nSubs >= relArity){
				throw new RascalTypeError("Too many subscripts (" + nSubs + ") for relation of arity " + relArity, x);
			}
			Result subscriptResult[] = new Result[nSubs];
			Type subscriptType[] = new Type[nSubs];
			boolean subscriptIsSet[] = new boolean[nSubs];
			
			for(int i = 0; i < nSubs; i++){
				subscriptResult[i] = x.getSubscripts().get(i).accept(this);
				subscriptType[i] = subscriptResult[i].type;
			}
			
			boolean yieldSet = (relArity - nSubs) == 1;
			Type resFieldType[] = new Type[relArity - nSubs];
			for (int i = 0; i < relArity; i++) {
				Type relFieldType = exprType.getFieldType(i);
				if(i < nSubs){
					if(subscriptType[i].isSetType() && 
					    subscriptType[i].getElementType().isSubtypeOf(relFieldType)){
						subscriptIsSet[i] = true;
					} else 
					if(subscriptType[i].isSubtypeOf(relFieldType)){
						subscriptIsSet[i] = false;
					} else {
						throw new RascalTypeError("type " + subscriptType[i] + 
								" of subscript #" + i + " incompatible with element type " +
								relFieldType, x);
					}
				} else {
					resFieldType[i - nSubs] = relFieldType;
				}
			}
			Type resultType;
			ISetWriter wset = null;
			IRelationWriter wrel = null;
			
			if(yieldSet){
				resultType = tf.setType(resFieldType[0]);
				wset = resultType.writer(vf);
			} else {
				resultType = tf.relType(resFieldType);
				wrel = resultType.writer(vf);
			}

			for (IValue v : ((IRelation) expr.value)) {
				ITuple tup = (ITuple) v;
				boolean allEqual = true;
				for(int k = 0; k < nSubs; k++){
					if(subscriptIsSet[k] && ((ISet) subscriptResult[k].value).contains(tup.get(k))){
						/* ok */
					} else if (tup.get(k).isEqual(subscriptResult[k].value)){
						/* ok */
					} else {
						allEqual = false;
					}
				}
				
				if (allEqual) {
					IValue args[] = new IValue[relArity - nSubs];
					for (int i = nSubs; i < relArity; i++) {
						args[i - nSubs] = tup.get(i);
					}
					if(yieldSet){
						wset.insert(args[0]);
					} else {
						wrel.insert(vf.tuple(args));
					}
				}
			}
			return normalizedResult(resultType, yieldSet ? wset.done() : wrel.done());
		}
		
		if(nSubs > 1){
			throw new RascalTypeError("Too many subscripts", x);
		}
		
		Result subs = x.getSubscripts().get(0).accept(this);
		Type subsBase = subs.type;
		
		if (exprType.isMapType()
			&& subsBase.isSubtypeOf(exprType.getKeyType())) {
			Type valueType = exprType.getValueType();
			IValue v = ((IMap) expr.value).get(subs.value);
			if(v == null){
				throw new RascalUndefinedValue("No value associated with key " + subs.value);
			}
			return normalizedResult(valueType,v);
		}
		
		if(!subsBase.isIntegerType()){
			throw new RascalTypeError("Subscript should have type integer", x);
		}
		int index = intValue(subs);

		if (exprType.isListType()) {
			Type elementType = exprType.getElementType();
			try {
				IValue element = ((IList) expr.value).get(index);
				return normalizedResult(elementType, element);
			} catch (IndexOutOfBoundsException e) {
				throw new RascalRunTimeError("Subscript out of bounds", e);
			}
		}
		if (exprType.isAbstractDataType()) {
			if(index >= ((IConstructor) expr.value).arity()){
				throw new RascalRunTimeError("Subscript out of bounds");
			}
			
			Type elementType = ((IConstructor) expr.value).getConstructorType().getFieldType(index);
			IValue element = ((IConstructor) expr.value).get(index);
			return normalizedResult(elementType, element);
		}
		if (exprType.isNodeType()) {
			if(index >= ((INode) expr.value).arity()){
				throw new RascalRunTimeError("Subscript out of bounds");
			}
			Type elementType = tf.valueType();
			IValue element = ((INode) expr.value).get(index);
			return normalizedResult(elementType, element);
		}
		if (exprType.isTupleType()) {
			try {
				Type elementType = exprType.getFieldType(index);
				IValue element = ((ITuple) expr.value).get(index);
				return normalizedResult(elementType, element);
			} catch (IndexOutOfBoundsException e){
				throw new RascalRunTimeError("Subscript out of bounds", e);
			}
		}
		throw new RascalBug("Not yet implemented subscript: " + x);
	}

	@Override
	public Result visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		Result expr = x.getExpression().accept(this);
		String field = x.getField().toString();
		
		if (expr.type.isTupleType()) {
			Type tuple = expr.type;
			if (!tuple.hasFieldNames()) {
				throw new RascalTypeError("Tuple does not have field names: " + tuple, x);
			}
			
			return normalizedResult(tuple.getFieldType(field), ((ITuple) expr.value).get(tuple.getFieldIndex(field)));
		}
		else if (expr.type.isRelationType()) {
			Type tuple = expr.type.getFieldTypes();
			
			try {
				ISetWriter w = vf.setWriter(tuple.getFieldType(field));
				for (IValue e : (ISet) expr.value) {
					w.insert(((ITuple) e).get(tuple.getFieldIndex(field)));
				}
				return result(tf.setType(tuple.getFieldType(field)), w.done());
			}
			catch (FactTypeError e) {
				throw new RascalTypeError(e.getMessage(), e);
			}
		}
		else if (expr.type.isAbstractDataType() || expr.type.isConstructorType()) {
			Type node = ((IConstructor) expr.value).getConstructorType();
			
			if (!expr.type.hasField(field)) {
				throw new RascalTypeError(expr.type + " does not have a field named " + field, x);
			}
			
			if (!node.hasField(field)) {
				throw new RascalException(vf, "Field " + field + " accessed on constructor that does not have it." + expr.value.getType());
			}
			
			int index = node.getFieldIndex(field);
			return normalizedResult(node.getFieldType(index),((IConstructor) expr.value).get(index));
		}
		
		throw new RascalTypeError("Field selection is not allowed on " + expr.type, x);
	}
	
	private boolean duplicateIndices(int indices[]){
		for(int i = 0; i < indices.length; i ++){
			for(int j = 0; j < indices.length; j++){
				if(i != j && indices[i] == indices[j]){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Result visitExpressionFieldProject(FieldProject x) {
		Result  base = x.getExpression().accept(this);
		
		java.util.List<Field> fields = x.getFields();
		int nFields = fields.size();
		int selectedFields[] = new int[nFields];
		
		if(base.type.isTupleType()){
			Type fieldTypes[] = new Type[nFields];
			
			for(int i = 0 ; i < nFields; i++){
				Field f = fields.get(i);
				if(f.isIndex()){
					selectedFields[i] = ((IInteger) f.getFieldIndex().accept(this).value).getValue();
				} else {
					String fieldName = f.getFieldName().toString();
					try {
						selectedFields[i] = base.type.getFieldIndex(fieldName);
					} catch (Exception e){
						throw new RascalTypeError("Undefined field " + fieldName + " in projection", x);
					}
				}
				if(selectedFields[i] < 0 || selectedFields[i] > base.type.getArity()){
					throw new RascalTypeError("Index " + selectedFields[i] + " in projection exceeds arity of tuple", x);
				}
				fieldTypes[i] = base.type.getFieldType(selectedFields[i]);
			}
			if(duplicateIndices(selectedFields)){
				throw new RascalTypeError("Duplicate fields in projection", x);
			}
			Type resultType = nFields == 1 ? fieldTypes[0] : tf.tupleType(fieldTypes);
			
			return result(resultType, ((ITuple)base.value).select(selectedFields));				     
		}
		if(base.type.isRelationType()){
			
			Type fieldTypes[] = new Type[nFields];
			
			for(int i = 0 ; i < nFields; i++){
				Field f = fields.get(i);
				if(f.isIndex()){
					selectedFields[i] = ((IInteger) f.getFieldIndex().accept(this).value).getValue();
				} else {
					String fieldName = f.getFieldName().toString();
					try {
						selectedFields[i] = base.type.getFieldIndex(fieldName);
					} catch (Exception e){
						throw new RascalTypeError("Undefined field " + fieldName + " in projection", x);
					}
				}
				if(selectedFields[i] < 0 || selectedFields[i] > base.type.getArity()){
					throw new RascalTypeError("Index " + selectedFields[i] + " in projection exceeds arity of tuple", x);
				}
				fieldTypes[i] = base.type.getFieldType(selectedFields[i]);
			}
			if(duplicateIndices(selectedFields)){
				throw new RascalTypeError("Duplicate fields in projection", x);
			}
			Type resultType = nFields == 1 ? tf.setType(fieldTypes[0]) : tf.relType(fieldTypes);
			
			return result(resultType, ((IRelation)base.value).select(selectedFields));				     
		}
		throw new RascalTypeError("Type " + base.type + " does not allow projection", x);
	}
	
	@Override
	public Result visitStatementEmptyStatement(EmptyStatement x) {
		return result();
	}
	
	@Override
	public Result visitStatementFail(Fail x) {
		if (x.getFail().isWithLabel()) {
			throw new FailureException(x.getFail().getLabel().toString());
		}
		else {
		  throw new FailureException();
		}
	}
	
	@Override
	public Result visitStatementReturn(
			org.meta_environment.rascal.ast.Statement.Return x) {
		org.meta_environment.rascal.ast.Return r = x.getRet();
		
		if (r.isWithExpression()) {
		  throw new ReturnException(x.getRet().getExpression().accept(this));
		}
		else {
			throw new ReturnException(result(tf.voidType(), null));
		}
	}
	
	@Override
	public Result visitStatementBreak(Break x) {
		throw new RascalBug("NYI break" + x); // TODO
	}
	
	@Override
	public Result visitStatementContinue(Continue x) {
		throw new RascalBug("NYI" + x); // TODO
	}
	
	@Override
	public Result visitStatementGlobalDirective(GlobalDirective x) {
		throw new RascalBug("NYI" + x); // TODO
	}
	
	@Override
	public Result visitStatementThrow(Throw x) {
		throw new RascalException(x.getExpression().accept(this).value);
	}
	
	@Override
	public Result visitStatementTry(Try x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), null);
	}
	
	@Override
	public Result visitStatementTryFinally(TryFinally x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), x.getFinallyBody());
	}
	
	private Result evalStatementTry(Statement body, java.util.List<Catch> handlers, Statement finallyBody){
		Result res = result();
		
		try {
			res = body.accept(this);
		} catch (RascalException e){
			
			IValue eValue = e.getException();
			Type eType = eValue.getType();

			for (Catch c : handlers){
				if(c.isDefault()){
					res = c.getBody().accept(this);
					break;
				} 
				
				if (eType.isSubtypeOf(evalType(c.getType()))){
					try {
						push();		
						Name name = c.getName();
						peek().storeVariable(name, normalizedResult(eType, eValue));
						res = c.getBody().accept(this);
						break;
					} 
					finally {
						pop();
					}
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
	public Result visitStatementVisit(
			org.meta_environment.rascal.ast.Statement.Visit x) {
		return x.getVisit().accept(this);
	}
	
	@Override
	public Result visitStatementInsert(Insert x) {
		throw new InsertException(x.getExpression().accept(this));
	}
	
	@Override
	public Result visitStatementAssignment(Assignment x) {
		Result right = x.getExpression().accept(this);
		return x.getAssignable().accept(new AssignableEvaluator(peek(), right, this));
	}
	
	@Override
	public Result visitStatementBlock(Block x) {
		Result r = result();
		try {
			push(); 
			for (Statement stat : x.getStatements()) {
				r = stat.accept(this);
			}
		}
		finally {
			pop();
		}
		return r;
	}
  
	@Override
	public Result visitAssignableVariable(
			org.meta_environment.rascal.ast.Assignable.Variable x) {
		return peek().getVariable(x.getQualifiedName().toString());
	}
	
	@Override
	public Result visitAssignableFieldAccess(FieldAccess x) {
		Result receiver = x.getReceiver().accept(this);
		String label = x.getField().toString();
	
		if (receiver.type.isTupleType()) {
			IValue result = ((ITuple) receiver.value).get(label);
			Type type = ((ITuple) receiver.value).getType().getFieldType(label);
			return normalizedResult(type, result);
		}
		else if (receiver.type.isConstructorType() || receiver.type.isAbstractDataType()) {
			IConstructor cons = (IConstructor) receiver.value;
			Type node = cons.getConstructorType();
			
			if (!receiver.type.hasField(label)) {
				throw new RascalTypeError(receiver.type + " does not have a field named " + label, x);
			}
			
			if (!node.hasField(label)) {
				throw new RascalException(vf, "Field " + label + " accessed on constructor that does not have it." + receiver.value.getType());
			}
			
			int index = node.getFieldIndex(label);
			return normalizedResult(node.getFieldType(index), cons.get(index));
		}
		else {
			throw new RascalTypeError(x.getReceiver() + " has no field named " + label, x);
		}
	}
	
	@Override
	public Result visitAssignableAnnotation(
			org.meta_environment.rascal.ast.Assignable.Annotation x) {
		Result receiver = x.getReceiver().accept(this);
		String label = x.getAnnotation().toString();
		
		if (receiver.type.declaresAnnotation(label)) {
			throw new RascalTypeError("No annotation " + label + " declared for " + receiver.type, x);
		}
		
		// TODO get annotation from local and imported environments
		Type type = tf.getAnnotationType(receiver.type, label);
		IValue value = ((IConstructor) receiver.value).getAnnotation(label);
		
		return normalizedResult(type, value);
	}
	
	@Override
	public Result visitAssignableConstructor(Constructor x) {
		throw new RascalBug("constructor assignable does not represent a value:" + x);
	}
	
	@Override
	public Result visitAssignableIfDefined(
			org.meta_environment.rascal.ast.Assignable.IfDefined x) {
		throw new RascalBug("ifdefined assignable does not represent a value");
	}
	
	@Override
	public Result visitAssignableSubscript(
			org.meta_environment.rascal.ast.Assignable.Subscript x) {
		Result receiver = x.getReceiver().accept(this);
		Result subscript = x.getSubscript().accept(this);
		
		if (receiver.type.isListType() && subscript.type.isIntegerType()) {
			IList list = (IList) receiver.value;
			IValue result = list.get(intValue(subscript));
			Type type = receiver.type.getElementType();
			return normalizedResult(type, result);
		}
		else if (receiver.type.isMapType()) {
			Type keyType = receiver.type.getKeyType();
			
			if (subscript.type.isSubtypeOf(keyType)) {
				IValue result = ((IMap) receiver.value).get(subscript.value);
				Type type = receiver.type.getValueType();
				return normalizedResult(type, result);
			}
		}
		
		// TODO implement other subscripts

		throw new RascalTypeError("Illegal subscript " + x.getSubscript() + " for receiver " + x.getReceiver(), x);
	}
	
	@Override
	public Result visitAssignableTuple(
			org.meta_environment.rascal.ast.Assignable.Tuple x) {
		throw new RascalBug("tuple in assignable does not represent a value:" + x);
	}
	
	@Override
	public Result visitAssignableAmbiguity(
			org.meta_environment.rascal.ast.Assignable.Ambiguity x) {
		throw new RascalBug("ambiguous Assignable: " + x);
	}
	
	@Override
	public Result visitFunctionDeclarationDefault(
			org.meta_environment.rascal.ast.FunctionDeclaration.Default x) {
		Lambda lambda;
		boolean varArgs = x.getSignature().getParameters().isVarArgs();
		
		if (hasJavaModifier(x)) {
			lambda = new JavaFunction(this, x, varArgs, peek(), javaBridge);
		}
		else {
			if (!x.getBody().isDefault()) {
				throw new RascalTypeError("Java function body without java function modifier in: " + x, x);
			}
			
			lambda = new RascalFunction(this, x, varArgs, peek());
		}
		
		String name = Names.name(x.getSignature().getName());
		peek().storeFunction(name, lambda);
		
		return lambda;
	}
	
	@Override
	public Result visitStatementIfThenElse(IfThenElse x) {
		elseBranch: 
			do {
				push(); // For the benefit of variables bound in the condition
				try {
					for (org.meta_environment.rascal.ast.Expression expr : x.getConditions()) {
						Result cval = expr.accept(this);
						if (!cval.type.isBoolType()) {
							throw new RascalTypeError("Condition " + expr + " has type "
									+ cval.type + " but should be bool", x);
						}
						if (cval.value.isEqual(vf.bool(false))) {
							break elseBranch;
						}
						// condition is true: continue
					}
					return x.getThenStatement().accept(this);
				} finally {
					pop();	// Remove any bindings due to condition evaluation.
				}
			} 
			while (false);
		return x.getElseStatement().accept(this);
	}

	@Override
	public Result visitStatementIfThen(IfThen x) {
		push(); // For the benefit of variables bound in the condition
		try {
			for (org.meta_environment.rascal.ast.Expression expr : x.getConditions()) {
				Result cval = expr.accept(this);
				if (!cval.type.isBoolType()) {
					throw new RascalTypeError("Condition " + expr + " has type "
							+ cval.type + " but should be bool", x);
				}
				if (cval.value.isEqual(vf.bool(false))) {
					return result();
				}
			}
			return x.getThenStatement().accept(this);
		}
		finally {
			pop();
		}
	}
	
	@Override
	public Result visitStatementWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		Result statVal = result();
		
		do {
			push();
			try {
				Result cval = expr.accept(this);
				if (!cval.type.isBoolType()) {
					throw new RascalTypeError("Condition " + expr + " has type "
							+ cval.type + " but should be bool", x);
				}
				if (cval.value.isEqual(vf.bool(false))) {
					return statVal;
				}
				statVal = x.getBody().accept(this);
			}
			finally {
				pop();
			}
		} while (true);
	}
		
	@Override
	public Result visitStatementDoWhile(DoWhile x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		do {
			Result result = x.getBody().accept(this);

			push();
			try {
				Result cval = expr.accept(this);
				if (!cval.type.isBoolType()) {
					throw new RascalTypeError("Condition " + expr + " has type "
							+ cval.type + " but should be bool", x);
				}
				if (cval.value.isEqual(vf.bool(false))) {
					return result;
				}
			}
			finally {
				pop();
			}
		} while (true);
	}
	
    @Override
    public Result visitExpressionMatch(Match x) {
    	return new MatchEvaluator(x.getPattern(), x.getExpression(), true, peek(), this).next();
    }
    
    @Override
    public Result visitExpressionNoMatch(NoMatch x) {
    	return new MatchEvaluator(x.getPattern(), x.getExpression(), false, peek(), this).next();
    }
	
	// ----- General method for matching --------------------------------------------------
    
    protected MatchPattern evalPattern(org.meta_environment.rascal.ast.Expression pat){
    	AbstractPatternEvaluator pe = makePatternEvaluator();
		if(pe.isPattern(pat)){
    		return pat.accept(pe);
    	} else {
			RegExpPatternEvaluator re = new RegExpPatternEvaluator(vf, peek());
			if(re.isRegExpPattern(pat)){ 
				return pat.accept(re);
			} else {
				throw new RascalTypeError("pattern expected instead of " + pat, pat);
			}
		}
    }
	
    // TODO remove dead code
//	private boolean matchOne(IValue subj, org.meta_environment.rascal.ast.Expression pat){
//		//System.err.println("matchOne: subj=" + subj + ", pat= " + pat);
//		MatchPattern mp = evalPattern(pat);
//		lastPattern = mp;
//		mp.initMatch(subj, this);
//		return mp.next();
//	}


	// Expressions -----------------------------------------------------------

	@Override
	public Result visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public Result visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}

	@Override
	public Result visitLiteralReal(Real x) {
		String str = x.getRealLiteral().toString();
		return result(vf.dubble(java.lang.Double.parseDouble(str)));
	}

	@Override
	public Result visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		return result(vf.bool(str.equals("true")));
	}

	@Override
	public Result visitLiteralString(
			org.meta_environment.rascal.ast.Literal.String x) {
		String str = x.getStringLiteral().toString();
		return result(vf.string(unescape(str, x)));
	}

	private String unescape(String str, AbstractAST ast) {
		byte[] bytes = str.getBytes();
		StringBuffer result = new StringBuffer();
		
		for (int i = 1; i < bytes.length - 1; i++) {
			char b = (char) bytes[i];
			switch (b) {
			/*
			 * Replace <var> by var's value.
			 */
			case '<':
				StringBuffer var = new StringBuffer();
				char varchar;
				while((varchar = (char) bytes[++i]) != '>'){
					var.append(varchar);
				}
				Result val = peek().getVariable(var.toString());
				String replacement;
				if(val == null || val.value == null) {
					// TODO JURGEN: should we not throw an exception or something? Undefined variables are not allowed.
//					replacement = "**undefined**";	
					throw new RascalUndefinedValue("Undefined variable " + var, ast);
				} else {
					if(val.type.isStringType()){
						replacement = ((IString)val.value).getValue();
					} else {
						replacement = val.value.toString();
					}
				}
//				replacement = replacement.replaceAll("<", "\\\\<"); TODO: maybe we need this after all?
				result.append(replacement);
				continue;
			case '\\':
				switch (bytes[++i]) {
				case '\\':
					b = '\\'; 
					break;
				case 'n':
					b = '\n'; 
					break;
				case '"':
					b = '"'; 
					break;
				case 't':
					b = '\t'; 
					break;
				case 'b':
					b = '\b'; 
					break;
				case 'f':
					b = '\f'; 
					break;
				case 'r':
					b = '\r'; 
					break;
				case '<':
					b = '<'; 
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					b = (char) (bytes[i] - '0');
					if (i < bytes.length - 1 && Character.isDigit(bytes[i+1])) {
						b = (char) (b * 8 + (bytes[++i] - '0'));
						
						if (i < bytes.length - 1 && Character.isDigit(bytes[i+1])) {
							b = (char) (b * 8 + (bytes[++i] - '0'));
						}
					}
					break;
				case 'u':
					// TODO unicode escape
					break;
				default:
				    b = '\\';	
				}
			}
			
			result.append((char) b);
		}
		
		return result.toString();
	}

	@Override
	public Result visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = x.getDecimal().toString();
		return result(vf.integer(java.lang.Integer.parseInt(str)));
	}
	
	@Override
	public Result visitExpressionQualifiedName(
			org.meta_environment.rascal.ast.Expression.QualifiedName x) {
		if (isTreeConstructorName(x.getQualifiedName(), tf.tupleEmpty())) {
			return constructTree(x.getQualifiedName(), new IValue[0], tf.tupleType(new Type[0]));
		}
		else {
			Result result = peek().getVariable(x.getQualifiedName());

			if (result != null && result.value != null) {
				return result;
			} else {
				throw new RascalUndefinedValue("Uninitialized variable: " + x, x);
			}
		}
	}
	
	@Override
	public Result visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType = tf.voidType();
		java.util.List<IValue> results = new ArrayList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			Result resultElem = expr.accept(this);
			if(resultElem.type.isListType() && !expr.isList() &&
					elementType.isSubtypeOf(resultElem.type.getElementType())){
				/*
				 * Splice elements in list if element types permit this
				 */
				for(IValue val : ((IList) resultElem.value)){
					elementType = elementType.lub(val.getType());
					results.add(val);
				}
			} else {
				elementType = elementType.lub(resultElem.type);
				results.add(results.size(), resultElem.value);
			}
		}

		Type resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		return result(resultType, w.done());
	}

	@Override
	public Result visitExpressionSet(Set x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType = tf.voidType();
		java.util.List<IValue> results = new ArrayList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			Result resultElem = expr.accept(this);
			if(resultElem.type.isSetType() && !expr.isSet() &&
			   elementType.isSubtypeOf(resultElem.type.getElementType())){
				/*
				 * Splice the elements in the set if element types permit this.
				 */
				for(IValue val : ((ISet) resultElem.value)){
					elementType = elementType.lub(val.getType());
					results.add(val);
				}
			} else {
				elementType = elementType.lub(resultElem.type);
				results.add(results.size(), resultElem.value);
			}
		}
		Type resultType = tf.setType(elementType);
		ISetWriter w = resultType.writer(vf);
		w.insertAll(results);
		return result(resultType, w.done());
	}

	@Override
	public Result visitExpressionMap(
			org.meta_environment.rascal.ast.Expression.Map x) {

		java.util.List<org.meta_environment.rascal.ast.Mapping> mappings = x
				.getMappings();
		Map<IValue,IValue> result = new HashMap<IValue,IValue>();
		Type keyType = tf.voidType();
		Type valueType = tf.voidType();

		for (org.meta_environment.rascal.ast.Mapping mapping : mappings) {
			Result keyResult = mapping.getFrom().accept(this);
			Result valueResult = mapping.getTo().accept(this);
			
			keyType = keyType.lub(keyResult.type);
			valueType = valueType.lub(valueResult.type);
			
			result.put(keyResult.value, valueResult.value);
		}
		
		Type type = tf.mapType(keyType, valueType);
		IMapWriter w = type.writer(vf);
		w.putAll(result);
		
		return result(type, w.done());
	}
	
	@Override
	public Result visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		return new Lambda(this, tf.voidType(), "", tf.tupleEmpty(), false, x.getStatements(), peek());
	}

	public Result visitExpressionTuple(Tuple x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();

		IValue[] values = new IValue[elements.size()];
		Type[] types = new Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			Result resultElem = elements.get(i).accept(this);
			types[i] = resultElem.type;
			values[i] = resultElem.value;
		}

		return result(tf.tupleType(types), vf.tuple(values));
	}
	
	@Override
	public Result visitExpressionAnnotation(
			org.meta_environment.rascal.ast.Expression.Annotation x) {
		  Result expr = x.getExpression().accept(this);
		String name = x.getName().toString();

		// TODO: get annotations from local and imported environments
		Type annoType = tf.getAnnotationType(expr.type, name);

		if (annoType == null) {
			throw new RascalTypeError("No annotation " + x.getName()
					+ " declared on " + expr.type, x);
		}

		IValue annoValue = ((IConstructor) expr.value).getAnnotation(name);
		
		if (annoValue == null) {
			// TODO: make this a Rascal exception that can be caught by the programmer
			throw new RascalTypeError("This " + expr.type + " does not have a " + name + " annotation set", x);
		}
		return result(annoType, annoValue);
	}
	
	public static void widenArgs(Result left, Result right) {
		TypeFactory tf = TypeFactory.getInstance();
		
		Type leftValType = left.value.getType();
		Type rightValType = right.value.getType();
		if (leftValType.isIntegerType() && rightValType.isDoubleType()) {
			if(left.type.isIntegerType()){
				left.type = tf.doubleType();
			}
			left.value =((IInteger) left.value).toDouble();
		} else if (leftValType.isDoubleType() && rightValType.isIntegerType()) {
			if(right.type.isIntegerType()){
				right.type = tf.doubleType();
			}
			right.value = ((IInteger) right.value).toDouble();
		} 
		/*else if(left.type.isConstructorType()){
			left.type = left.type.getAbstractDataType();
		} else 	if(right.type.isConstructorType()){
			right.type = right.type.getAbstractDataType();
		}
		*/
	}
	
    @Override
	public Result visitExpressionAddition(Addition x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);

		widenArgs(left, right);
		Type resultType = left.type.lub(right.type);
		
		//System.err.println("left=" + left + "; right=" + right + "; resulType=" + resultType);

		// Integer
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).add((IInteger) right.value));
			
		}
		//Real
		if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).add((IDouble) right.value));
			
		}
		//String
		if (left.type.isStringType() && right.type.isStringType()) {
			return result(vf.string(((IString) left.value).getValue()
					+ ((IString) right.value).getValue()));
			
		}
		//List
		if (left.type.isListType()){
				if(right.type.isListType()) {
					return result(resultType, ((IList) left.value)
					.concat((IList) right.value));
				}
				if(right.type.isSubtypeOf(left.type.getElementType())){
					return result(left.type, ((IList)left.value).append(right.value));
				}
		}
		
		if (right.type.isListType()){
			if(left.type.isSubtypeOf(right.type.getElementType())){
				return result(right.type, ((IList)right.value).insert(left.value));
			}
		}
		
		//Relation
		if (left.type.isRelationType() && right.type.isRelationType()) {
				return result(resultType, ((ISet) left.value)
						.union((ISet) right.value));
		}
		
		//Set
		if (left.type.isSetType()){
			if(right.type.isSetType()) {
				return result(resultType, ((ISet) left.value)
				.union((ISet) right.value));
			}
			if(right.type.isSubtypeOf(left.type.getElementType())){
				return result(left.type, ((ISet)left.value).insert(right.value));
			}
		}
	
		if (right.type.isSetType()){
			if(left.type.isSubtypeOf(right.type.getElementType())){
				return result(right.type, ((ISet)right.value).insert(left.value));
			}
		}
		
		//Map
		if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)              //TODO: is this the right operation?
					.join((IMap) right.value));
			
		}
		//Tuple
		if(left.type.isTupleType() && right.type.isTupleType()) {
			Type leftType = left.type;
			Type rightType = right.type;
			
			int leftArity = leftType.getArity();
			int rightArity = rightType.getArity();
			int newArity = leftArity + rightArity;
			
			Type fieldTypes[] = new Type[newArity];
			String fieldNames[] = new String[newArity];
			IValue fieldValues[] = new IValue[newArity];
			
			for(int i = 0; i < leftArity; i++){
				fieldTypes[i] = leftType.getFieldType(i);
				fieldNames[i] = leftType.getFieldName(i);
				fieldValues[i] = ((ITuple) left.value).get(i);
			}
			
			for(int i = 0; i < rightArity; i++){
				fieldTypes[leftArity + i] = rightType.getFieldType(i);
				fieldNames[leftArity + i] = rightType.getFieldName(i);
				fieldValues[leftArity + i] = ((ITuple) right.value).get(i);
			}
			
			//TODO: avoid null fieldnames
			for(int i = 0; i < newArity; i++){
				if(fieldNames[i] == null){
					fieldNames[i] = "f" + String.valueOf(i);
				}
			}
			Type newTupleType = tf.tupleType(fieldTypes, fieldNames);
			return result(newTupleType, vf.tuple(fieldValues));
			
		}
		
		
		throw new RascalTypeError("Operands of + have illegal types: "
					+ left.type + ", " + right.type, x);
	}
    
	public Result visitExpressionSubtraction(Subtraction x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		Type resultType = left.type.lub(right.type);
		
		widenArgs(left, right);

		// Integer
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).subtract((IInteger) right.value));
		}
		
		// Real
		if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).subtract((IDouble) right.value));
		}
		
		// List
		if (left.type.isListType() && right.type.isListType()) {
			IListWriter w = left.type.writer(vf);
			
			IList listLeft = (IList)left.value;
			IList listRight = (IList)right.value;
			
			int lenLeft = listLeft.length();
			int lenRight = listRight.length();

			for(int i = lenLeft-1; i > 0; i--) {
				boolean found = false;
				IValue leftVal = listLeft.get(i);
				for(int j = 0; j < lenRight; j++){
					if(leftVal.isEqual(listRight.get(j))){
						found = true;
						break;
					}
				}
				if(!found){
			        w.insert(leftVal);
				}
			}
			return result(left.type, w.done());
		}
		
		// Set
		if (left.type.isSetType()){
				if(right.type.isSetType()) {
					return result(resultType, ((ISet) left.value)
							.subtract((ISet) right.value));
				}
				if(right.type.isSubtypeOf(left.type.getElementType())){
					return result(left.type, ((ISet)left.value)
							.subtract(vf.set(right.value)));
				}
		}
		
		// Map
		if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)
					.remove((IMap) right.value));
		}
		
		//Relation
		if (left.type.isRelationType() && right.type.isRelationType()) {
			return result(resultType, ((ISet) left.value)
					.subtract((ISet) right.value));
		}
		
		throw new RascalTypeError("Operands of - have illegal types: "
					+ left.type + ", " + right.type, x);
	}
	
	@Override
	public Result visitExpressionNegative(Negative x) {
		Result arg = x.getArgument().accept(this);
		
		if (arg.type.isIntegerType()) {
			return result(vf.integer(- intValue(arg)));
		}
		else if (arg.type.isDoubleType()) {
				return result(vf.dubble(- RealValue(arg)));
		} else {
			throw new RascalTypeError(
					"Operand of unary - should be integer or Real instead of: " + arg.type, x);
		}
	}
	
	@Override
	public Result visitExpressionProduct(Product x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		widenArgs(left, right);

		//Integer
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).multiply((IInteger) right.value));
		} 
		
		//Real 
		else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).multiply((IDouble) right.value));
		}
		
		// List
		else if (left.type.isListType() && right.type.isListType()){
			Type leftElementType = left.type.getElementType();
			Type rightElementType = right.type.getElementType();
			Type resultType = tf.listType(tf.tupleType(leftElementType, rightElementType));
			IListWriter w = resultType.writer(vf);
			
			for(IValue v1 : (IList) left.value){
				for(IValue v2 : (IList) right.value){
					w.append(vf.tuple(v1, v2));	
				}
			}
			return result(resultType, w.done());	
		}
		
		// Relation
		else if(left.type.isRelationType() && right.type.isRelationType()){
			Type leftElementType = left.type.getElementType();
			Type rightElementType = right.type.getElementType();
			
			int leftArity = leftElementType.getArity();
			int rightArity = rightElementType.getArity();
			int newArity = leftArity + rightArity;
			
			Type fieldTypes[] = new Type[newArity];
			String fieldNames[] = new String[newArity];
			
			for(int i = 0; i < leftArity; i++){
				fieldTypes[i] = leftElementType.getFieldType(i);
				fieldNames[i] = leftElementType.getFieldName(i);
			}
			for(int i = 0; i < rightArity; i++){
				fieldTypes[leftArity + i] = rightElementType.getFieldType(i);
				fieldNames[leftArity + i] = rightElementType.getFieldName(i);
			}
			
			// TODO: avoid empty field names
			for(int i = 0; i < newArity; i++){
				if(fieldNames[i] == null){
					fieldNames[i] = "f" + String.valueOf(i);
				}
			}
			
			Type resElementType = tf.tupleType(fieldTypes, fieldNames);
			Type resultType = tf.relTypeFromTuple(resElementType);
			IRelationWriter w = resultType.writer(vf);
			
			for(IValue v1 : (IRelation) left.value){
				IValue elementValues[] = new IValue[newArity];
				
				for(int i = 0; i < leftArity; i++){
					elementValues[i] = ((ITuple) v1).get(i);
				}
				for(IValue v2 : (IRelation) right.value){
					for(int i = 0; i <rightArity; i++){
						elementValues[leftArity + i] = ((ITuple) v2).get(i);
					}
					w.insert(vf.tuple(elementValues));	
				}
			}
			
			return result(resultType, w.done());
		}
		
		//Set
		else if (left.type.isSetType() && right.type.isSetType()){
			Type leftElementType = left.type.getElementType();
			Type rightElementType = right.type.getElementType();
			Type resultType = tf.relType(leftElementType, rightElementType);
			IRelationWriter w = resultType.writer(vf);
			
			for(IValue v1 : (ISet) left.value){
				for(IValue v2 : (ISet) right.value){
					w.insert(vf.tuple(v1, v2));	
				}
			}
			return result(resultType, w.done());	
		}
		else {
			throw new RascalTypeError("Operands of * have illegal types: "
					+ left.type + ", " + right.type, x);
		}
	}
	
	@Override
	public Result visitExpressionDivision(Division x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		widenArgs(left, right);

		//TODO: transform Java arithmetic exceptions into Rascal exceptions
		
		//Integer
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).divide((IInteger) right.value));
		} 
		
		// Real
		else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).divide((IDouble) right.value));
		}
		else {
			throw new RascalTypeError("Operands of / have illegal types: "
					+ left.type + ", " + right.type, x);
		}
	}
	
	@Override
	public Result visitExpressionModulo(Modulo x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).remainder((IInteger) right.value));
		} 
		else {
			throw new RascalTypeError("Operands of % have illegal types: "
					+ left.type + ", " + right.type, x);
		}
	}
	
	@Override
	public Result visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result visitExpressionIntersection(Intersection x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		Type resultType = left.type.lub(right.type);

		//Set
		if (left.type.isSetType() && right.type.isSetType()) {
				return result(resultType, ((ISet) left.value)
				.intersect((ISet) right.value));
		} 
		
		//Map
		else if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)
					.common((IMap) right.value));
		} 
		
		//Relation
		else if (left.type.isRelationType() && right.type.isRelationType()) {
			return result(resultType, ((ISet) left.value)
				.intersect((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of & have illegal types: "
					+ left.type + ", " + right.type, x);
		}
	}

	@Override
	public Result visitExpressionOr(Or x) {
		return new OrEvaluator(x, this).next();
	}

	@Override
	public Result visitExpressionAnd(And x) {
		return new AndEvaluator(x, this).next();
	}

	@Override
	public Result visitExpressionNegation(Negation x) {
		return new NegationEvaluator(x, this).next();
	}
	
	@Override
	public Result visitExpressionImplication(Implication x) {
		return new ImplicationEvaluator(x, this).next();
	}
	
	@Override
	public Result visitExpressionEquivalence(Equivalence x) {
		return new EquivalenceEvaluator(x, this).next();
	}
	
	// TODO factor out into Result or a subclass thereof
	public static boolean equals(Result left, Result right){
		widenArgs(left, right);
		
		if (left.type.comparable(right.type)) {
			return compare(left, right) == 0;
		} else {
			return false;
				//TODO; type error
		}
	}

	@Override
	public Result visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		widenArgs(left, right);
/*		
		if (!left.type.comparable(right.type)) {
			throw new RascalTypeError("Arguments of equals have incomparable types: " + left.type + " and " + right.type, x);
		}
*/
		
		return result(vf.bool(equals(left, right)));
	}
	
	@Override
	public Result visitExpressionOperatorAsValue(OperatorAsValue x) {
		// TODO
		throw new RascalBug("Operator as value not yet implemented:" + x);
	}
	
	@Override
	public Result visitExpressionArea(Area x) {
		Result beginLine = x.getBeginLine().accept(this);
		Result endLine = x.getEndLine().accept(this);
		Result beginColumn = x.getBeginColumn().accept(this);
		Result endColumn = x.getEndColumn().accept(this);
		Result length = x.getLength().accept(this);
		Result offset = x.getOffset().accept(this);
		
		//System.err.println("AreaDefault: " + x );
		
		int iOffset = intValue(offset);
		int iLength = intValue(length);
		int iEndColumn = intValue(endColumn);
		int iBeginColumn = intValue(beginColumn);
		int iEndLine = intValue(endLine);
		int iBeginLine = intValue(beginLine);
	
		ISourceRange r = vf.sourceRange(iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
		return result(tf.sourceRangeType(), r);
	}
	
	@Override
	public Result visitExpressionAreaInFileLocation(AreaInFileLocation x) {
		
	   Result area = x.getAreaExpression().accept(this);
	   
	   if (!area.type.isSubtypeOf(tf.sourceRangeType())) {
		   throw new RascalTypeError("Expected area, got " + area.type, x);
	   }
	   
	   Result file = x.getFilename().accept(this);
	   
	   if (!area.type.isSubtypeOf(tf.sourceRangeType())) {
		   throw new RascalTypeError("Expected area, got " + file.type, x);
	   }
	   
	   checkType(area, tf.sourceRangeType());
	   checkType(file, tf.stringType());
	   
	   ISourceRange range = (ISourceRange) area.value;
	   
	   return result(tf.sourceLocationType(), vf.sourceLocation(stringValue(file), range));
	}
	
	@Override
	public Result visitExpressionClosure(Closure x) {
		Type formals = te.eval(x.getParameters(), peek());
		Type returnType = evalType(x.getType());
		return new Lambda(this, returnType, "", formals, x.getParameters().isVarArgs(), x.getStatements(), peek());
	}

	@Override
	public Result visitExpressionVoidClosure(VoidClosure x) {
		Type formals = te.eval(x.getParameters(), peek());
		return new Lambda(this, tf.voidType(), "", formals, x.getParameters().isVarArgs(), x.getStatements(), peek());
	}
	
	@Override
	public Result visitExpressionFieldUpdate(FieldUpdate x) {
		Result expr = x.getExpression().accept(this);
		Result repl = x.getReplacement().accept(this);
		String name = x.getKey().toString();
		
		try {
			if (expr.type.isTupleType()) {
				Type tuple = expr.type;
				Type argType = tuple.getFieldType(name);
				ITuple value = (ITuple) expr.value;
				
				checkType(repl.type, argType);
				
				return result(expr.type, value.set(name, repl.value));
			}
			else if (expr.type.isAbstractDataType() || expr.type.isConstructorType()) {
				Type node = ((IConstructor) expr.value).getConstructorType();
				
				if (!expr.type.hasField(name)) {
					throw new RascalTypeError(expr.type + " does not have a field named " + name, x);
				}
				
				if (!node.hasField(name)) {
					throw new RascalException(vf, "Field " + name + " accessed on constructor that does not have it." + expr.value.getType());
				}
				
				int index = node.getFieldIndex(name);
				
				checkType(repl.type, node.getFieldType(index));
				
				return result(expr.type, ((IConstructor) expr.value).set(index, repl.value));
			}
			else {
				throw new RascalTypeError("Field updates only possible on tuples with labeled fields, relations with labeled fields and data constructors", x);
			}
		} catch (FactTypeError e) {
			throw new RascalTypeError(e.getMessage(), e);
		}
	}
	
	@Override
	public Result visitExpressionFileLocation(FileLocation x) {
		Result result = x.getFilename().accept(this);
		
		 if (!result.type.isSubtypeOf(tf.stringType())) {
			   throw new RascalTypeError("Expected area, got " + result.type, x);
		 }
		 
		 return result(tf.sourceLocationType(), 
				 vf.sourceLocation(((IString) result.value).getValue(), 
				 vf.sourceRange(0, 0, 1, 1, 0, 0)));
	}
	
	@Override
	public Result visitExpressionLexical(Lexical x) {
		throw new RascalBug("Lexical NYI: " + x);// TODO
	}
	
	@Override
	public Result visitExpressionRange(Range x) {
		IListWriter w = vf.listWriter(tf.integerType());
		Result from = x.getFirst().accept(this);
		Result to = x.getLast().accept(this);

		int iFrom = intValue(from);
		int iTo = intValue(to);
		
		if (iTo < iFrom) {
			for (int i = iFrom; i >= iTo; i--) {
				w.append(vf.integer(i));
			}	
		}
		else {
			for (int i = iFrom; i <= iTo; i++) {
				w.append(vf.integer(i));
			}
		}
		
		return result(tf.listType(tf.integerType()), w.done());
	}
	
	@Override
	public Result visitExpressionStepRange(StepRange x) {
		IListWriter w = vf.listWriter(tf.integerType());
		Result from = x.getFirst().accept(this);
		Result to = x.getLast().accept(this);
		Result second = x.getSecond().accept(this);

		int iFrom = intValue(from);
		int iSecond = intValue(second);
		int iTo = intValue(to);
		
		int diff = iSecond - iFrom;
		
		if(iFrom <= iTo && diff > 0){
			for (int i = iFrom; i >= iFrom && i <= iTo; i += diff) {
				w.append(vf.integer(i));
			}
		} else if(iFrom >= iTo && diff < 0){
			for (int i = iFrom; i <= iFrom && i >= iTo; i += diff) {
				w.append(vf.integer(i));
			}	
		}
		
		return result(tf.listType(tf.integerType()), w.done());
		
	}
	
	@Override
	public Result visitExpressionTypedVariable(TypedVariable x) {
		throw new RascalTypeError("Use of typed variable outside matching context", x);
	}
	
	private boolean matchAndEval(IValue subject, org.meta_environment.rascal.ast.Expression pat, Statement stat){
		MatchPattern mp = evalPattern(pat);
		mp.initMatch(subject, peek());
		lastPattern = mp;
		//System.err.println("matchAndEval: subject=" + subject + ", pat=" + pat);
		try {
			push(); 	// Create a separate scope for match and statement
			while(mp.hasNext()){
				if(mp.next()){
					try {
						//System.err.println(stack);
						stat.accept(this);
						return true;
					} catch (FailureException e){
						//System.err.println("failure occurred");
					}
				}
			}
		} finally {
			pop();
		}
		return false;
	}
	
	private boolean matchEvalAndReplace(IValue subject, 
			org.meta_environment.rascal.ast.Expression pat, 
			java.util.List<Expression> conditions,
			Expression replacementExpr){
		MatchPattern mp = evalPattern(pat);
		mp.initMatch(subject, peek());
		lastPattern = mp;
		//System.err.println("matchEvalAndReplace: subject=" + subject + ", pat=" + pat + ", conditions=" + conditions);
		try {
			push(); 	// Create a separate scope for match and statement
			while(mp.hasNext()){
				//System.err.println("mp.hasNext()==true; mp=" + mp);
				if(mp.next()){
					try {
						boolean trueConditions = true;
						for(Expression cond : conditions){
							//System.err.println("cond = " + cond);
							if(!cond.accept(this).isTrue()){
								trueConditions = false;
								break;
							}
						}
						if(trueConditions){
							throw new InsertException(replacementExpr.accept(this));		
						}
					} catch (FailureException e){
						//System.err.println("failure occurred");
					}
				}
			}
		} finally {
			push();
		}
		return false;
	}
	
	@Override
	public Result visitStatementSwitch(Switch x) {
		Result subject = x.getExpression().accept(this);

		for(Case cs : x.getCases()){
			if(cs.isDefault()){
				return cs.getStatement().accept(this);
			}
			org.meta_environment.rascal.ast.Rule rule = cs.getRule();
			if(rule.isArbitrary() && matchAndEval(subject.value, rule.getPattern(), rule.getStatement())){
				return result();
			} else if(rule.isGuarded())	{
				org.meta_environment.rascal.ast.Type tp = rule.getType();
				Type t = evalType(tp);
				if(subject.type.isSubtypeOf(t) && matchAndEval(subject.value, rule.getPattern(), rule.getStatement())){
					return result();
				}
			} else if(rule.isReplacing()){
				throw new RascalBug("Replacing Rule not yet implemented: " + rule);
			}
		}
		return null;
	}
	
	@Override
	public Result visitExpressionVisit(Visit x) {
		return x.getVisit().accept(this);
	}
	
	/*
	 * TraverseResult contains the value returned by a traversal
	 * and a changed flag that indicates whether the value itself or
	 * any of its children has been changed during the traversal.
	 */
	
	class TraverseResult {
		boolean matched;   // Some rule matched;
		IValue value; 		// Result of the 
		boolean changed;   // Original subject has been changed
		
		TraverseResult(boolean someMatch, IValue value){
			this.matched = someMatch;
			this.value = value;
			this.changed = false;
		}
		
		TraverseResult(IValue value){
			this.matched = false;
			this.value = value;
			this.changed = false;
		}
		
		TraverseResult(IValue value, boolean changed){
			this.matched = true;
			this.value = value;
			this.changed = changed;
		}
		TraverseResult(boolean someMatch, IValue value, boolean changed){
			this.matched = someMatch;
			this.value = value;
			this.changed = changed;
		}
	}
	
	/*
	 * CaseOrRule is the union of a Case or a Rule and allows the sharing of
	 * traversal code for both.
	 */
	class CasesOrRules {
		private java.util.List<Case> cases;
		private java.util.List<org.meta_environment.rascal.ast.Rule> rules;
		
		@SuppressWarnings("unchecked")
		CasesOrRules(java.util.List<?> casesOrRules){
			if(casesOrRules.get(0) instanceof Case){
				this.cases = (java.util.List<Case>) casesOrRules;
			} else {
				rules = (java.util.List<org.meta_environment.rascal.ast.Rule>)casesOrRules;
			}
		}
		
		public boolean hasRules(){
			return rules != null;
		}
		
		public boolean hasCases(){
			return cases != null;
		}
		
		public int length(){
			return (cases != null) ? cases.size() : rules.size();
		}
		
		public java.util.List<Case> getCases(){
			return cases;
		}
		public java.util.List<org.meta_environment.rascal.ast.Rule> getRules(){
			return rules;
		}
	}
	
	private TraverseResult traverse(IValue subject, CasesOrRules casesOrRules,
			boolean bottomup, boolean breaking, boolean fixedpoint) {
		//System.err.println("traverse: subject=" + subject + ", casesOrRules=" + casesOrRules);
		do {
			TraverseResult tr = traverseOnce(subject, casesOrRules, bottomup, breaking);
			if(fixedpoint){
				if (!tr.changed) {
					return tr;
				}
				subject = tr.value;
			} else {
				return tr;
			}
		} while (true);
	}
	
	/*
	 * StringReplacement represents a single replacement in the subject string.
	 */
	
	private class StringReplacement {
		int start;
		int end;
		String replacement;
		
		StringReplacement(int start, int end, String repl){
			this.start = start;
			this.end = end;
			replacement = repl;
		}
		
		public String toString(){
			return "StringReplacement(" + start + ", " + end + ", " + replacement + ")";
		}
	}
	
	/*
	 *  singleCase returns a single case or rules if casesOrRueles has length 1 and null otherwise.
	 */
	
	private Object singleCase(CasesOrRules casesOrRules){
		if(casesOrRules.length() == 1){
			if(casesOrRules.hasCases()){
				return casesOrRules.getCases().get(0);
			} else {
				return casesOrRules.getRules().get(0);
			}
		}
		return null;
	}
	
	/*
	 * traverString implements a visit of a string subject and applies the set of cases
	 * for all substrings of the subject. At the end, all replacements are applied and the modified
	 * subject is returned.
	 */
	
	private TraverseResult traverseString(IString subject, CasesOrRules casesOrRules){
		String subjectString = subject.getValue();
		int len = subjectString.length();
		java.util.List<StringReplacement> replacements = new ArrayList<StringReplacement>();
		boolean matched = false;
		boolean changed = false;
		int cursor = 0;
		
		Case cs = (Case) singleCase(casesOrRules);
		
		RegExpPatternEvaluator re = new RegExpPatternEvaluator(vf, peek());
		if(cs != null && cs.isRule() && re.isRegExpPattern(cs.getRule().getPattern())){
			/*
			 * In the frequently occurring case that there is one case with a regexp as pattern,
			 * we can delegate all the work to the regexp matcher.
			 */
			org.meta_environment.rascal.ast.Rule rule = cs.getRule();
			
			Expression patexp = rule.getPattern();
			MatchPattern mp = evalPattern(patexp);
			mp.initMatch(subject, peek());

			try {
				push(); // a separate scope for match and statement/replacement
				while(mp.hasNext()){
					if(mp.next()){
						try {
							if(rule.isReplacing()){
								Replacement repl = rule.getReplacement();
								boolean trueConditions = true;
								if(repl.isConditional()){
									for(Expression cond : repl.getConditions()){
										Result res = cond.accept(this);
										if(!res.isTrue()){         // TODO: How about alternatives?
											trueConditions = false;
											break;
										}
									}
								}
								if(trueConditions){
									throw new InsertException(repl.getReplacementExpression().accept(this));
								}
							
							} else {
								rule.getStatement().accept(this);
							}
						} catch (InsertException e){
							changed = true;
							IValue repl = e.getValue().value;
							if(repl.getType().isStringType()){
								int start = ((RegExpPatternValue) mp).getStart();
								int end = ((RegExpPatternValue) mp).getEnd();
								replacements.add(new StringReplacement(start, end, ((IString)repl).getValue()));
							} else {
								throw new RascalTypeError("String replacement should be of type str, not " + repl.getType());
							}
						} catch (FailureException e){
							//System.err.println("failure occurred");
						}
					}
				}
			} finally {
				pop();
			}
	} else {
			/*
			 * In all other cases we generate subsequent substrings subject[0,len], subject[1,len] ...
			 * and try to match all the cases.
			 * Performance issue: we create a lot of garbage by producing all these substrings.
			 */
		
			while(cursor < len){
				//System.err.println("cursor = " + cursor);
				try {
					TraverseResult tr = applyCasesOrRules(vf.string(subjectString.substring(cursor, len)), casesOrRules);
					matched |= tr.matched;
					changed |= tr.changed;
					//System.err.println("matched=" + matched + ", changed=" + changed);
					cursor++;
				} catch (InsertException e){
					IValue repl = e.getValue().value;
					if(repl.getType().isStringType()){
						int start;
						int end;
						if(lastPattern instanceof RegExpPatternValue){
							start = ((RegExpPatternValue)lastPattern).getStart();
							end = ((RegExpPatternValue)lastPattern).getEnd();
						} else if(lastPattern instanceof AbstractPatternLiteral){
							start = 0;
							end = ((IString)repl).getValue().length();
						} else {
							throw new RascalTypeError("Illegal pattern " + lastPattern + " in string visit");
						}
						
						replacements.add(new StringReplacement(cursor + start, cursor + end, ((IString)repl).getValue()));
						matched = changed = true;
						cursor += end;
					} else {
						throw new RascalTypeError("String replacement should be of type str, not " + repl.getType());
					}
				}
			}
		}
	
		if(!changed){
			return new TraverseResult(matched, subject, changed);
		}
		/*
		 * The replacements are now known. Create a version of the subject with all replacement applied.
		 */
		StringBuffer res = new StringBuffer();
		cursor = 0;
		for(StringReplacement sr : replacements){
			for( ;cursor < sr.start; cursor++){
				res.append(subjectString.charAt(cursor));
			}
			cursor = sr.end;
			res.append(sr.replacement);
		}
		for( ; cursor < len; cursor++){
			res.append(subjectString.charAt(cursor));
		}
		
		return new TraverseResult(matched, vf.string(res.toString()), changed);
	}
	
	/*
	 * traverseOnce: traverse an arbitrary IVAlue once. Implements the strategies bottomup/topdown.
	 */
	
	private TraverseResult traverseOnce(IValue subject, CasesOrRules casesOrRules, 
			boolean bottomup, 
			boolean breaking){
		Type subjectType = subject.getType();
		boolean matched = false;
		boolean changed = false;
		IValue result = subject;

		//System.err.println("traverseOnce: " + subject + ", type=" + subject.getType());
		if(subjectType.isStringType()){
			return traverseString((IString) subject, casesOrRules);
		}

		if(!bottomup){
			TraverseResult tr = traverseTop(subject, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			if(breaking && changed){
				return tr;
			}
			subject = tr.value;
		}

		if(subjectType.isAbstractDataType()){
			IConstructor cons = (IConstructor)subject;
			if(cons.arity() == 0){
				result = subject;
			} else {
				IValue args[] = new IValue[cons.arity()];

				for(int i = 0; i < cons.arity(); i++){
					TraverseResult tr = traverseOnce(cons.get(i), casesOrRules, bottomup, breaking);
					matched |= tr.matched;
					changed |= tr.changed;
					args[i] = tr.value;
				}
				
				result = vf.constructor(cons.getConstructorType(), args);
			}
		} else
			if(subjectType.isNodeType()){
				INode node = (INode)subject;
				if(node.arity() == 0){
					result = subject;
				} else {
					IValue args[] = new IValue[node.arity()];

					for(int i = 0; i < node.arity(); i++){
						TraverseResult tr = traverseOnce(node.get(i), casesOrRules, bottomup, breaking);
						matched |= tr.matched;
						changed |= tr.changed;
						args[i] = tr.value;
					}
					result = vf.node(node.getName(), args);
				}
			} else
				if(subjectType.isListType()){
					IList list = (IList) subject;
					int len = list.length();
					if(len > 0){
						IListWriter w = list.getType().writer(vf);
						for(int i = len - 1; i >= 0; i--){
							TraverseResult tr = traverseOnce(list.get(i), casesOrRules, bottomup, breaking);
							matched |= tr.matched;
							changed |= tr.changed;
							w.insert(tr.value);
						}
						result = w.done();
					} else {
						result = subject;
					}
				} else 
					if(subjectType.isSetType()){
						ISet set = (ISet) subject;
						if(!set.isEmpty()){
							ISetWriter w = set.getType().writer(vf);
							for(IValue v : set){
								TraverseResult tr = traverseOnce(v, casesOrRules, bottomup, breaking);
								matched |= tr.matched;
								changed |= tr.changed;
								w.insert(tr.value);
							}
							result = w.done();
						} else {
							result = subject;
						}
					} else
						if (subjectType.isMapType()) {
							IMap map = (IMap) subject;
							if(!map.isEmpty()){
								IMapWriter w = map.getType().writer(vf);
								Iterator<Entry<IValue,IValue>> iter = map.entryIterator();

								while (iter.hasNext()) {
									Entry<IValue,IValue> entry = iter.next();
									TraverseResult tr = traverseOnce(entry.getKey(), casesOrRules, bottomup, breaking);
									matched |= tr.matched;
									changed |= tr.changed;
									IValue newKey = tr.value;
									tr = traverseOnce(entry.getValue(), casesOrRules, bottomup, breaking);
									matched |= tr.matched;
									changed |= tr.changed;
									IValue newValue = tr.value;
									w.put(newKey, newValue);
								}
								result = w.done();
							} else {
								result = subject;
							}
						} else
							if(subjectType.isTupleType()){
								ITuple tuple = (ITuple) subject;
								int arity = tuple.arity();
								IValue args[] = new IValue[arity];
								for(int i = 0; i < arity; i++){
									TraverseResult tr = traverseOnce(tuple.get(i), casesOrRules, bottomup, breaking);
									matched |= tr.matched;
									changed |= tr.changed;
									args[i] = tr.value;
								}
								result = vf.tuple(args);
							} else {
								result = subject;
							}

		if(bottomup){
			if(breaking && changed){
				return new TraverseResult(matched, result, changed);
			} else {
				TraverseResult tr = traverseTop(result, casesOrRules);
				matched |= tr.matched;
				changed |= tr.changed;
				return new TraverseResult(matched, tr.value, changed);
			}
		}
		return new TraverseResult(matched,result,changed);
	}
	
	/**
	 * Replace an old subject by a new one as result of an insert statement.
	 */
	private TraverseResult replacement(IValue oldSubject, IValue newSubject){
		return new TraverseResult(true, newSubject, true);
	}
	
	/**
	 * Loop over all cases or rules.
	 */
	
	private TraverseResult applyCasesOrRules(IValue subject, CasesOrRules casesOrRules) {
		if(casesOrRules.hasCases()){
			for (Case cs : casesOrRules.getCases()) {
				if (cs.isDefault()) {
					cs.getStatement().accept(this);
					return new TraverseResult(true,subject);
				} else {
					TraverseResult tr = applyOneRule(subject, cs.getRule());
					if(tr.matched){
						//System.err.println(" *** matches ***");
						return tr;
					}
				}
			}
		} else {
			//System.err.println("hasRules");
			for(org.meta_environment.rascal.ast.Rule rule : casesOrRules.getRules()){
				//System.err.println(rule);
				TraverseResult tr = applyOneRule(subject, rule);
				//System.err.println("rule fails");
				if(tr.matched){
					//System.err.println(" *** matches ***");
					return tr;
				}
			}
		}
		//System.err.println("applyCasesorRules does not match");
		return new TraverseResult(subject);
	}
	
	/*
	 * traverseTop: traverse the outermost symbol of the subject.
	 */

	private TraverseResult traverseTop(IValue subject, CasesOrRules casesOrRules) {
		//System.err.println("traversTop(" + subject + ")");
		try {
			return applyCasesOrRules(subject, casesOrRules);	
		} catch (InsertException e) {

			return replacement(subject, e.getValue().value);
		}
	}
	
	/*
	 * applyOneRule: try to apply one rule to the subject.
	 */
	
	private TraverseResult applyOneRule(IValue subject,
			org.meta_environment.rascal.ast.Rule rule) {
		
		//System.err.println("applyOneRule: subject=" + subject + ", type=" + subject.getType() + ", rule=" + rule);
	
		if (rule.isArbitrary()){
			if(matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
		} else if (rule.isGuarded()) {
			org.meta_environment.rascal.ast.Type tp = rule.getType();
			Type type = evalType(tp);
			rule = rule.getRule();
			if (subject.getType().isSubtypeOf(type) && 
				matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
		} else if (rule.isReplacing()) {
			Replacement repl = rule.getReplacement();
			java.util.List<Expression> conditions = repl.isConditional() ? repl.getConditions() : new ArrayList<Expression>();
			if(matchEvalAndReplace(subject, rule.getPattern(), conditions, repl.getReplacementExpression())){
				return new TraverseResult(true, subject);
			}
		} else {
			throw new RascalBug("Impossible case in a rule: " + rule);
		}
			return new TraverseResult(subject);
	}
	
	@Override
	public Result visitVisitDefaultStrategy(DefaultStrategy x) {
		
		IValue subject = x.getSubject().accept(this).value;
		java.util.List<Case> cases = x.getCases();
		
		TraverseResult tr = traverse(subject, new CasesOrRules(cases), 
									/* bottomup */ true, 
									/* breaking */ false, 
									/* fixedpoint */ false);
		return normalizedResult(tr.value.getType(), tr.value);
	}
	
	@Override
	public Result visitVisitGivenStrategy(GivenStrategy x) {
		
		IValue subject = x.getSubject().accept(this).value;
		Type subjectType = subject.getType();
		
		if(subjectType.isConstructorType()){
			subjectType = subjectType.getAbstractDataType();
		}
		
		java.util.List<Case> cases = x.getCases();
		Strategy s = x.getStrategy();
		
		boolean bottomup = false;
		boolean breaking = false;
		boolean fixedpoint = false;
		
		if(s.isBottomUp()){
			bottomup = true;
		} else if(s.isBottomUpBreak()){
			bottomup = true; breaking = true;
		} else if(s.isInnermost()){
			bottomup = true;  fixedpoint = true;
		} else if(s.isTopDown()){
			bottomup = false;
		} else if(s.isTopDownBreak()){
			bottomup = false; breaking = true;
		} else if(s.isOutermost()){
			bottomup = false; fixedpoint = true;
		} else {
			throw new RascalBug("Unknown strategy: " + s);
		}
		
		TraverseResult tr = traverse(subject, new CasesOrRules(cases), bottomup, breaking, fixedpoint);
		return normalizedResult(subjectType, tr.value);
	}
	
	@Override
	public Result visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		if (!left.type.comparable(right.type)) {
			throw new RascalTypeError("Arguments of unequal have incomparable types: " + left.type + " and " + right.type, x);
		}
		
		return result(vf.bool(compare(left, right) != 0));
	}
	
	
	// TODO distribute over subclasses of Result
	public static int compare(Result left, Result right){
		// compare must use run-time types because it is complete for all types
		// even if statically two values have type 'value' but one is an int 1
		// and the other is Real 1.0 they must be equal.
		
		// TODO distribute this method over subclasses of Result,
		// IntegerResult to MapResult which implement equals and compare and add and
		// subtract, etc.

		widenArgs(left, right);
		Type leftType = left.value.getType();
		Type rightType = right.value.getType();
		
		if (leftType.isBoolType() && rightType.isBoolType()) {
			boolean lb = ((IBool) left.value).getValue();
			boolean rb = ((IBool) right.value).getValue();
			return (lb == rb) ? 0 : ((!lb && rb) ? -1 : 1);
		}
		if (left.type.isIntegerType() && rightType.isIntegerType()) {
			return ((IInteger) left.value).compare((IInteger) right.value);
		}
		if (leftType.isDoubleType() && rightType.isDoubleType()) {
			return ((IDouble) left.value).compare((IDouble) right.value);
		}
		if (leftType.isStringType() && rightType.isStringType()) {
			return ((IString) left.value).compare((IString) right.value);
		}
		if (leftType.isListType() && rightType.isListType()) {
			return compareList(((IList) left.value).iterator(), ((IList) left.value).length(),
					            ((IList) right.value).iterator(), ((IList) right.value).length());
		}
		if (leftType.isSetType() && rightType.isSetType()) {
			return compareSet((ISet) left.value, (ISet) right.value);
		}
		if (leftType.isMapType() && rightType.isMapType()) {
			return compareMap((IMap) left.value, (IMap) right.value);
		}
		if (leftType.isTupleType() && rightType.isTupleType()) {
			return compareList(((ITuple) left.value).iterator(), ((ITuple) left.value).arity(),
		            ((ITuple) right.value).iterator(), ((ITuple) right.value).arity());
		} 
		if (leftType.isRelationType() && rightType.isRelationType()) {
			return compareSet((ISet) left.value, (ISet) right.value);
		}
		
		if (leftType.isNodeType() && rightType.isNodeType()) {
			return compareNode((INode) left.value, (INode) right.value);
		}
		
		if (leftType.isAbstractDataType() && rightType.isAbstractDataType()) {
			return compareNode((INode) left.value, (INode) right.value);
		}
		
		if(leftType.isSourceLocationType() && rightType.isSourceLocationType()){	
			return compareSourceLocation((ISourceLocation) left.value, (ISourceLocation) right.value);
		}
			
		// VoidType
		// ValueType
		
		return leftType.toString().compareTo(rightType.toString());
	}
	
	private static int compareNode(INode left, INode right){
		String leftName = left.getName().toString();
		String rightName = right.getName().toString();
		int compare = leftName.compareTo(rightName);
		
		if(compare != 0){
			return compare;
		}
		return compareList(left.iterator(), left.arity(), right.iterator(), right.arity());
	}
	
	private static int compareSourceLocation(ISourceLocation leftSL, ISourceLocation rightSL){
		if(leftSL.getPath().equals(rightSL.getPath())){
			ISourceRange leftSR = leftSL.getRange();
			ISourceRange rightSR = rightSL.getRange();
			
			if(leftSR.isEqual(rightSR)){
				return 0;
			}
			
			int lStartLine = leftSR.getStartLine();
			int rStartLine = rightSR.getStartLine();
			
			int lEndLine = leftSR.getEndLine();
			int rEndLine = rightSR.getEndLine();
			
			int lStartColumn = leftSR.getStartColumn();
			int rStartColumn = rightSR.getStartColumn();
			
			int lEndColumn = leftSR.getEndColumn();
			int rEndColumn = rightSR.getEndColumn();
			
			if((lStartLine > rStartLine ||
				(lStartLine == rStartLine && lStartColumn > rStartColumn)) &&
				(lEndLine < rEndLine ||
						((lEndLine == rEndLine) && lEndColumn < rEndColumn))){
				return -1;	
			} else {
				return 1;
			}
		} else {
			return leftSL.getPath().compareTo(rightSL.getPath());
		}
	}
	
	private static int compareSet(ISet value1, ISet value2) {
		
		if (value1.isEqual(value2)) {
			return 0;
		}
		else if (value1.isSubSet(value2)) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	private static int compareMap(IMap value1, IMap value2) {
		if (value1.isEqual(value2)) {
			return 0;
		}
		else if (value1.isSubMap(value2)) {
			return -1;
		}
		else {
			return 1;
		}
	}

	private static int compareList(Iterator<IValue> left, int leftLen, Iterator<IValue> right, int rightLen){
		
		if(leftLen == 0){
			return rightLen == 0 ? 0 : -1;
		}
		if(rightLen == 0){
			return 1;
		}
		int m = (leftLen > rightLen) ? rightLen : leftLen;  
		int compare = 0;
		
		for(int i = 0; i < m; i++){
			IValue leftVal = left.next();
			IValue rightVal = right.next();
			Result vl = new Result(leftVal.getType(), leftVal);
			Result vr = new Result(rightVal.getType(), rightVal);
			int c = compare(vl, vr);

			if (c < 0 || c > 0) {
				return c;
			}
		}
		
		if(compare == 0 && leftLen != rightLen){
			compare = leftLen < rightLen ? -1 : 1;
		}
			
		return compare;
	}
	
	@Override
	public Result visitExpressionLessThan(LessThan x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) < 0));
	}
	
	@Override
	public Result visitExpressionLessThanOrEq(LessThanOrEq x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) <= 0));
	}
	@Override
	public Result visitExpressionGreaterThan(GreaterThan x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) > 0));
	}
	
	@Override
	public Result visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) >= 0));
	}
	
	@Override
	public Result visitExpressionIfThenElse(
			org.meta_environment.rascal.ast.Expression.IfThenElse x) {
		Result cval = x.getCondition().accept(this);
	
		if (cval.type.isBoolType()) {
			if (cval.value.isEqual(vf.bool(true))) {
				return x.getThenExp().accept(this);
			}
		} else {
			throw new RascalTypeError("Condition has type "
					+ cval.type + " but should be bool", x);
		}
		return x.getElseExp().accept(this);
	}
	
	@Override
	public Result visitExpressionIfDefined(IfDefined x) {
		try {
			return x.getLhs().accept(this);
		} catch (RascalUndefinedValue e) {
			Result res = x.getRhs().accept(this);
			return res;
		}
	}
	
	private boolean in(org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Expression expression2){
		Result left = expression.accept(this);
		Result right = expression2.accept(this);
		
		//List
		if(right.type.isListType() &&
		    left.type.isSubtypeOf(right.type.getElementType())){
			IList lst = (IList) right.value;
			IValue val = left.value;
			for(int i = 0; i < lst.length(); i++){
				if(lst.get(i).isEqual(val))
					return true;
			}
			return false;
			
	    //Set
		} else if(right.type.isSetType() && 
				   left.type.isSubtypeOf(right.type.getElementType())){
			return ((ISet) right.value).contains(left.value);
		//Map
		} else if(right.type.isMapType() && left.type.isSubtypeOf(right.type.getValueType())){
			return ((IMap) right.value).containsValue(left.value);
			
		//Relation
		} else if(right.type.isRelationType() && left.type.isSubtypeOf(right.type.getElementType())){
			return ((ISet) right.value).contains(left.value);
		} else {
			throw new RascalTypeError("Operands of in have wrong types: "
					+ left.type + ", " + right.type, expression2);
		}
	}
	
	@Override
	public Result visitExpressionIn(In x) {
		return result(vf.bool(in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public Result visitExpressionNotIn(NotIn x) {
		return result(vf.bool(!in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public Result visitExpressionComposition(Composition x) {
		Result left = x.getLhs().accept(this);
		Result right = x.getRhs().accept(this);
	
		//Relation
		if(left.type.isRelationType() && 
			right.type.isRelationType()){
			Type leftrelType = left.type; 
			Type rightrelType = right.type;
			int leftArity = leftrelType.getArity();
			int rightArity = rightrelType.getArity();

			
			if ((leftArity == 0 || leftArity == 2) && (rightArity == 0 || rightArity ==2 )) {
				Type resultType = leftrelType.compose(rightrelType);
				return result(resultType, ((IRelation) left.value)
						.compose((IRelation) right.value));
			}
		}
		
		throw new RascalTypeError("Operands of o have wrong types: "
				+ left.type + ", " + right.type, x);
	}

	private Result closure(Result arg, boolean reflexive) {

		//Relation
		if (arg.type.isRelationType() && arg.type.getArity() < 3) {
			Type relType = arg.type;
			Type fieldType1 = relType.getFieldType(0);
			Type fieldType2 = relType.getFieldType(1);
			if (fieldType1.comparable(fieldType2)) {
				Type lub = fieldType1.lub(fieldType2);
				
				Type resultType = relType.hasFieldNames() ? tf.relType(lub, relType.getFieldName(0), lub, relType.getFieldName(1)) : tf.relType(lub,lub);
				return result(resultType, reflexive ? ((IRelation) arg.value).closureStar()
						: ((IRelation) arg.value).closure());
			}
		}
		
		throw new RascalTypeError("Operand of + or * closure has wrong type: "
				+ arg.type);
	}
	
	@Override
	public Result visitExpressionTransitiveClosure(TransitiveClosure x) {
		return closure(x.getArgument().accept(this), false);
	}
	
	@Override
	public Result visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		return closure(x.getArgument().accept(this), true);
	}
	
	// Comprehensions ----------------------------------------------------
	
	@Override
	public Result visitExpressionComprehension(Comprehension x) {
		return x.getComprehension().accept(this);
	}
	
	
	
	@Override
	public Result visitGeneratorExpression(
			org.meta_environment.rascal.ast.Generator.Expression x) {
		return new GeneratorEvaluator(x, this).next();
	}
	
	class GeneratorEvaluator implements Iterator<Result>{
		private boolean isValueProducer;
		private boolean firstTime = true;
		private org.meta_environment.rascal.ast.Expression expr;
		private MatchPattern pat;
		private org.meta_environment.rascal.ast.Expression patexpr;
		private Evaluator evaluator;
		private Iterator<?> iter;
		
		GeneratorEvaluator(ValueProducer vp, Evaluator ev){
			make(vp, ev);
		}

		GeneratorEvaluator(Generator g, Evaluator ev){
			if(g.isProducer()){
				make(g.getProducer(), ev);
			} else {
				evaluator = ev;
				isValueProducer = false;
				expr = g.getExpression();
			}
		}
		
		void make(ValueProducer vp, Evaluator ev){
			evaluator = ev;
			isValueProducer = true;
			
			pat = evalPattern(vp.getPattern());
			patexpr = vp.getExpression();
			Result r = patexpr.accept(ev);
			// List
			if(r.type.isListType()){
				iter = ((IList) r.value).iterator();
				
			// Set
			} else 	if(r.type.isSetType()){
				iter = ((ISet) r.value).iterator();
			
			// Map
			} else if(r.type.isMapType()){
				iter = ((IMap) r.value).iterator();
				
			// Node and ADT
			} else if(r.type.isNodeType() || r.type.isAbstractDataType()){
				boolean bottomup = true;
				if(vp.hasStrategy()){
					Strategy strat = vp.getStrategy();

					if(strat.isTopDown()){
						bottomup = false;
					} else if(strat.isBottomUp()){
							bottomup = true;
					} else {
						throw new RascalTypeError("Strategy " + strat + " not allowed in generator", vp);
					}
				}
				iter = new INodeReader((INode) r.value, bottomup);
			} else if(r.type.isStringType()){
				iter = new SingleIValueIterator(r.value);
			} else {
				throw new RascalTypeError("Unimplemented expression type " + r.type + " in generator", vp);
			}
		}
		
		public boolean hasNext(){
			if(isValueProducer){
				return pat.hasNext() || iter.hasNext();
			} else {
				return firstTime;
			}	
		}

		public Result next(){
			if(isValueProducer){
				//System.err.println("getNext, trying pat " + pat);
				/*
				 * First, explore alternatives that remain to be matched by the current pattern
				 */
				while(pat.hasNext()){
					if(pat.next()){
						//System.err.println("return true");
						return new IterableEvalResult(this, true);
					}
				}
				
				/*
				 * Next, fetch a new data element (if any) and create a new pattern.
				 */
				
				while(iter.hasNext()){
					IValue v = (IValue) iter.next();
					//System.err.println("getNext, try next from value iterator: " + v);
					pat.initMatch(v, peek());
					while(pat.hasNext()){
						if(pat.next()){
							//System.err.println("return true");
							return new IterableEvalResult(this,true);						
						}	
					}
				}
				//System.err.println("return false");
				return normalizedResult(tf.boolType(), vf.bool(false));
			} else {
				if(firstTime){
					/* Evaluate expression only once */
					firstTime = false;
					Result v = expr.accept(evaluator);
					if(v.type.isBoolType()){
						return result(tf.boolType(), vf.bool(v.value.isEqual(vf.bool(true))));
					} else {
						throw new RascalTypeError("Expression as generator should have type bool", expr);
					}
				} else {
					return result(tf.boolType(), vf.bool(false));
				}
			}
		}

		public void remove() {
			throw new RascalBug("remove() not implemented for GeneratorEvaluator");
		}
	}
	
	/*
	 * Enumerate the possible kinds of collections that can be produced by any comprehension.
	 */
	
	private static enum collectionKind {LIST, SET, MAP};
	
	/*
	 * ComprehensionCollectionWriter provides a uniform interface for writing elements
	 * to a list/set/map during the evaluation of a list/set/map comprehension.
	 */
	
	private class ComprehensionCollectionWriter {
		private Type elementType1;
		private Type elementType2;
		private Type resultType;
		private org.meta_environment.rascal.ast.Expression resultExpr1;
		private org.meta_environment.rascal.ast.Expression resultExpr2;
		
		private collectionKind kind;
		
		private IWriter writer;
		private Evaluator ev;
		
		ComprehensionCollectionWriter(collectionKind kind, 
				org.meta_environment.rascal.ast.Expression resultExpr1,
				org.meta_environment.rascal.ast.Expression resultExpr2, 
				Evaluator ev){
			this.kind = kind;
			this.ev = ev;
			this.resultExpr1 = resultExpr1;
			this.resultExpr2 = resultExpr2;
			this.writer = null;
		}
		
		public void append(){
			Result r1 = resultExpr1.accept(ev);
			Result r2 = null;
			
			if(kind == collectionKind.MAP){
				r2 = resultExpr2.accept(ev);
			}
			if(writer == null){
				elementType1 = r1.type;
				switch(kind){
				case LIST:
					resultType = tf.listType(elementType1); break;
				case SET:
					resultType = tf.setType(elementType1); break;
				case MAP:
					elementType2 = r2.type;
					resultType = tf.mapType(elementType1, elementType2);
				}
				writer = resultType.writer(vf);
			}
			if(!r1.type.isSubtypeOf(elementType1)){
				throw new RascalTypeError("Cannot add value of type " + r1.type +
						                   " to list/set/map comprehension with element/key type " + 
						                   elementType1, resultExpr1);
			} else {		
				elementType1 = elementType1.lub(r1.type);
				switch(kind){
				case LIST:
					((IListWriter)writer).append(r1.value); break;
				case SET:
					writer.insert(r1.value); break;
				case MAP:
					if(!r2.type.isSubtypeOf(elementType2)){
						throw new RascalTypeError("Cannot add value of type " + r2.type + 
												   " to map comprehension with value type " + 
												   elementType2, resultExpr2);
					} 
					((IMapWriter)writer).put(r1.value, r2.value);
				}	
			}
		}
		
		public Result done(){
			switch(kind){
			case LIST:
				return (writer== null) ? result(tf.listType(tf.voidType()), vf.list()) : 
										   result(tf.listType(elementType1), writer.done());
			case SET:
				return (writer == null) ? 	result(tf.setType(tf.voidType()), vf.set()) : 
                    						result(tf.setType(elementType1), writer.done());
			case MAP:
				return (writer == null) ? result(tf.mapType(tf.voidType(), tf.voidType()), vf.map(tf.voidType(),tf.voidType())) : 
                    result(tf.mapType(elementType1, elementType2), writer.done());
			}
			return result();
		}
		
	}
	/*
	 * The common comprehension evaluator
	 */
	
	private Result evalComprehension(java.util.List<Generator> generators, 
										  ComprehensionCollectionWriter w){
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		
		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while(i >= 0 && i < size){		
			if(gens[i].hasNext() && gens[i].next().isTrue()){
				if(i == size - 1){
					w.append();
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return w.done();
	}
	
	@Override
	public Result visitComprehensionList(org.meta_environment.rascal.ast.Comprehension.List x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.LIST, x.getResult(), null, this));
	}
	
	@Override
	public Result visitComprehensionSet(
			org.meta_environment.rascal.ast.Comprehension.Set x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.SET, x.getResult(), null, this));
	}
	
	@Override
	public Result visitComprehensionMap(
			org.meta_environment.rascal.ast.Comprehension.Map x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.MAP, x.getFrom(), x.getTo(), this));
	}

	@Override
	public Result visitStatementFor(For x) {
		Statement body = x.getBody();
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		Result result = result();
		
		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while(i >= 0 && i < size){		
			if(gens[i].hasNext() && gens[i].next().isTrue()){
				if(i == size - 1){
					result = body.accept(this);
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return result;
	}
	
	@Override
	public Result visitExpressionAny(Any x) {
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];

		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while (i >= 0 && i < size) {
			if (gens[i].hasNext() && gens[i].next().isTrue()) {
				if (i == size - 1) {
					return result(vf.bool(true));
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return result(vf.bool(false));
	}
	
	@Override
	public Result visitExpressionAll(All x) {
		java.util.List<Generator> producers = x.getGenerators();
		int size = producers.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];

		int i = 0;
		gens[0] = new GeneratorEvaluator(producers.get(0), this);
		while (i >= 0 && i < size) {
			if (gens[i].hasNext()) {
				if (!gens[i].next().isTrue()) {
					return result(vf.bool(false));
				}
				if (i < size - 1) {
					i++;
					gens[i] = new GeneratorEvaluator(producers.get(i), this);
				}
			} else {
				i--;
			}
		}
		return result(vf.bool(true));
	}
	
	// ------------ solve -----------------------------------------
	
	@Override
	public Result visitStatementSolve(Solve x) {
		java.util.ArrayList<Name> vars = new java.util.ArrayList<Name>();
		
		for(Declarator d : x.getDeclarations()){
			for(org.meta_environment.rascal.ast.Variable v : d.getVariables()){
				vars.add(v.getName());
			}
			d.accept(this);
		}
		IValue currentValue[] = new IValue[vars.size()];
		for(int i = 0; i < vars.size(); i++){
			currentValue[i] = peek().getVariable(Names.name(vars.get(i))).value;
		}
		
		Statement body = x.getBody();
		
		int max = 1000;
		
		Bound bound= x.getBound();
		if(bound.isDefault()){
			Result res = bound.getExpression().accept(this);
			if(!res.type.isIntegerType()){
				throw new RascalTypeError("Bound in solve statement should be integer, instead of " + res.type, x);
			}
			max = ((IInteger)res.value).getValue();
			if(max <= 0){
				throw new RascalRunTimeError("Bound in solve statement should be positive");
			}
		}
		
		Result bodyResult = null;
		
		boolean change = true;
		int iterations = 0;
		
		while (change && iterations < max){
			change = false;
			iterations++;
			bodyResult = body.accept(this);
			for(int i = 0; i < vars.size(); i++){
				Result v = peek().getVariable(Names.name(vars.get(i)));
				if(currentValue[i] == null || !v.value.isEqual(currentValue[i])){
					change = true;
					currentValue[i] = v.value;
				}
			}
		}
		return bodyResult;
	}
}
