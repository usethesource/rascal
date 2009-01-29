package org.meta_environment.rascal.interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.meta_environment.rascal.ast.Bound;
import org.meta_environment.rascal.ast.Case;
import org.meta_environment.rascal.ast.Catch;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.Declarator;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Field;
import org.meta_environment.rascal.ast.Formal;
import org.meta_environment.rascal.ast.FunctionBody;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.FunctionModifier;
import org.meta_environment.rascal.ast.FunctionModifiers;
import org.meta_environment.rascal.ast.Generator;
import org.meta_environment.rascal.ast.Import;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Replacement;
import org.meta_environment.rascal.ast.Signature;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.ast.Tags;
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
import org.meta_environment.rascal.interpreter.env.ClosureResult;
import org.meta_environment.rascal.interpreter.env.EnvironmentHolder;
import org.meta_environment.rascal.interpreter.env.EvalResult;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.IterableEvalResult;
import org.meta_environment.rascal.interpreter.exceptions.FailureException;
import org.meta_environment.rascal.interpreter.exceptions.InsertException;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.RascalRunTimeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalUndefinedValue;
import org.meta_environment.rascal.interpreter.exceptions.ReturnException;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class Evaluator extends NullASTVisitor<EvalResult> {
	public static final String RASCAL_FILE_EXT = ".rsc";
	final IValueFactory vf;
	final TypeFactory tf = TypeFactory.getInstance();
	final TypeEvaluator te = TypeEvaluator.getInstance();
	private final RegExpPatternEvaluator re = new RegExpPatternEvaluator();
	private final AbstractPatternEvaluator pe;
	protected GlobalEnvironment env = GlobalEnvironment.getInstance();
	private ASTFactory astFactory = new ASTFactory();
	private boolean callTracing = false;
	private int callNesting = 0;
	
	private final ASTFactory af;
	private final JavaFunctionCaller javaFunctionCaller;
	
	protected MatchPattern lastPattern;	// The most recent pattern applied in a match
	                                    	// For the benefit of string matching.

	public Evaluator(IValueFactory f, ASTFactory astFactory, Writer errorWriter) {
		this.vf = f;
		this.af = astFactory;
		javaFunctionCaller = new JavaFunctionCaller(errorWriter, te);
		this.pe = new AbstractPatternEvaluator(this);
		GlobalEnvironment.clean();
	}
	
	/**
	 * Clean the global environment for the benefit of repeated testing.
	 * TODO: can probably be removed.
	 */
	public void clean(){
		GlobalEnvironment.clean();
	}
	
	/**
	 * Evaluate a statement
	 * @param stat
	 * @return
	 */
	public IValue eval(Statement stat) {
		try {
			EvalResult r = stat.accept(this);
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
		EvalResult r = declaration.accept(this);
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
		EvalResult r = imp.accept(this);
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
	
	EvalResult normalizedResult(Type t, IValue v){
		Map<Type, Type> bindings = env.getTypeBindings();
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
		return new EvalResult(instance, v);
	}
	
	/*
	 * Return an evaluation result that may need normalization.
	 */
	
	EvalResult result(Type t, IValue v) {
		Map<Type, Type> bindings = env.getTypeBindings();
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

		return applyRules(instance, v);
	}

	EvalResult result(IValue v) {
		Type type = v.getType();
		
		if (type.isRelationType() 
				|| type.isSetType() 
				|| type.isMapType()
				|| type.isListType()) {
			throw new RascalBug("Should not used run-time type for type checking!!!!");
		}
		if(v != null){
			return applyRules(type, v);
		} else {
			return new EvalResult(null, v);
		}
	}
	
	private EvalResult applyRules(Type t, IValue v){
		// we search using the run-time type of a value
		Type typeToSearchFor = v.getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) v).getConstructorType();
		}
		java.util.List<org.meta_environment.rascal.ast.Rule> rules = env.getRules(typeToSearchFor);
		if(rules.isEmpty()){
			//System.err.println("applyRules: no matching rules for " + t);
			return new EvalResult(t, v);
		}
		env.pushFrame();
		try {
			TraverseResult tr = traverse(v, new CasesOrRules(rules), 
				/* bottomup */ true,  
				/* breaking */ false, 
				/* fixedpoint */ false);  /* innermost is achieved by repeated applications of applyRules
				 							* when intermediate results are produced.
				 							*/
			//System.err.println("applyRules: tr.value =" + tr.value);
			
			// rules can not change the declared type!
			return new EvalResult(t, tr.value);
		} finally {
			env.popFrame();
		}

	}
	
	private EvalResult result() {
		return new EvalResult(null, null);
	}
	
	private void checkInteger(EvalResult val) {
		checkType(val, tf.integerType());
	}
	
	private void checkReal(EvalResult val) {
		checkType(val, tf.doubleType());
	}
	
	private void checkString(EvalResult val) {
		checkType(val, tf.stringType());
	}
	
	private void checkType(EvalResult val, Type expected) {
		checkType(val.type, expected);
	}
	
	private void checkType(Type given, Type expected) {
		if (expected == ClosureResult.getClosureType()) {
			return;
		}
		if (given.isSubtypeOf(expected) || expected.isAbstractDataType() &&
				given.isSubtypeOf(expected.getAbstractDataType())){
			// ok
		} else {
			throw new RascalTypeError("Expected " + expected + ", got " + given);
		}
	}
	
	private int intValue(EvalResult val) {
		checkInteger(val);
		return ((IInteger) val.value).getValue();
	}
	
	private double RealValue(EvalResult val) {
		checkReal(val);
		return ((IDouble) val.value).getValue();
	}
	
	private String stringValue(EvalResult val) {
		checkString(val);
		return ((IString) val.value).getValue();
	}
	
	private void bindTypeParameters(Type actualTypes, Type formals) {
		try {
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			formals.match(actualTypes, bindings);
			env.storeTypeBindings(bindings);
		}
		catch (FactTypeError e) {
			throw new RascalTypeError("Could not bind type parameters in " + formals + " to " + actualTypes, e);
		}
	}	

	// Ambiguity ...................................................
	
	@Override
	public EvalResult visitExpressionAmbiguity(Ambiguity x) {
		throw new RascalBug("Ambiguous expression: " + x);
	}
	
	@Override
	public EvalResult visitStatementAmbiguity(
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
		throw new RascalBug("Ambiguous statement: " + x);
	}
	
	// Modules -------------------------------------------------------------
	
	@Override
	public EvalResult visitImportDefault(
			org.meta_environment.rascal.ast.Import.Default x) {
		// TODO support for full complexity of import declarations
		String name = x.getModule().getName().toString();
		env.addImport(name);
		
		if (!env.existsModule(name)) {
		Parser p = Parser.getInstance();
		ASTBuilder b = new ASTBuilder(af);
		
		try {
			String fileName = name.replaceAll("::","/") + RASCAL_FILE_EXT;
			File file = new File(fileName);
			
			// TODO: support proper search path for modules
			// TODO: support properly packaged/qualified module names
			// TODO: mind the / at the end of each directory!
			String searchPath[] = {
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
					"demo/Rascal/"
					};
			
			for(int i = 0; i < searchPath.length; i++){
				file = new File(searchPath[i] + fileName);
				if(file.exists()){
					break;
				}
			}
			if (!file.exists()) {
					throw new RascalTypeError("Can not find file for module " + name);
			}
			
			IConstructor tree = p.parse(new FileInputStream(file));
			
			if (tree.getType() == Factory.ParseTree_Summary) {
				throw new RascalTypeError("Parse error in module " + name + ":\n" + tree);
			}
			
			Module m = b.buildModule(tree);
		
			// TODO reactivate checking module name
//			ModuleName declaredNamed = m.getHeader().getName();
//			if (!declaredNamed.toString().equals(name)) {
//				throw new RascalTypeError("Module " + declaredNamed + " should be in a file called " + declaredNamed + RASCAL_FILE_EXT + ", not " + name + RASCAL_FILE_EXT);
//			}
			return m.accept(this);
		
		} catch (FactTypeError e) {
			throw new RascalTypeError("Something went wrong during parsing of " + name + ": ", e);
		} catch (FileNotFoundException e) {
			throw new RascalTypeError("Could not import module", e);
		} catch (IOException e) {
			throw new RascalTypeError("Could not import module", e);
		}
		}
		return result();
	}
	
	@Override 
	public EvalResult visitModuleDefault(
			org.meta_environment.rascal.ast.Module.Default x) {
		String name = x.getHeader().getName().toString();

		try {
			env.addModule(name);
			env.pushModule(name);

			x.getHeader().accept(this);

			java.util.List<Toplevel> decls = x.getBody().getToplevels();
			for (Toplevel l : decls) {
				l.accept(this);
			}

			return result();
		}
		finally {
			env.popModule();
		}
	}
	
	@Override
	public EvalResult visitHeaderDefault(
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
	public EvalResult visitHeaderParameters(Parameters x) {
		visitImports(x.getImports());
		return result();
	}
	
	@Override
	public EvalResult visitToplevelDefaultVisibility(DefaultVisibility x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public EvalResult visitToplevelGivenVisibility(GivenVisibility x) {
		// order dependent code here:
		Declaration decl = x.getDeclaration();
		
		env.setVisibility(decl, x.getVisibility());
		x.getDeclaration().accept(this);

		return result();
	}
	
	@Override
	public EvalResult visitDeclarationFunction(Function x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	@Override
	public EvalResult visitDeclarationVariable(Variable x) {
		Type declaredType = x.getType().accept(te);
		EvalResult r = result();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			if (var.isUnInitialized()) {  
				throw new RascalTypeError("Module variable is not initialized: " + x);
			} else {
				EvalResult v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.type, bindings);
					declaredType = declaredType.instantiate(bindings);
					r = normalizedResult(declaredType, v.value);
					env.storeVariable(var.getName(), r);
				} else {
					throw new RascalTypeError("variable " + var + ", declared type " + declaredType + " incompatible with initial type " + v.type);
				}
			}
		}
		
		return r;
	}
	
	@Override
	public EvalResult visitDeclarationAnnotation(Annotation x) {
		Type annoType = x.getType().accept(te);
		String name = x.getName().toString();
		
		for (org.meta_environment.rascal.ast.Type type : x.getTypes()) {
		  Type onType = type.accept(te);
		  tf.declareAnnotation(onType, name, annoType);	
		  env.storeAnnotation(onType, name, annoType);
		}
		
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationData(Data x) {
		String name = x.getUser().getName().toString();
		Type sort = tf.abstractDataType(name);
		env.storeAbstractDataType(sort);
		
		for (Variant var : x.getVariants()) {
			String altName = Names.name(var.getName());
			
		    if (var.isNAryConstructor()) {
		    	java.util.List<TypeArg> args = var.getArguments();
		    	Type[] fields = new Type[args.size()];
		    	String[] labels = new String[args.size()];

		    	for (int i = 0; i < args.size(); i++) {
		    		TypeArg arg = args.get(i);
					fields[i] = arg.getType().accept(te);
					
					if (arg.hasName()) {
						labels[i] = arg.getName().toString();
					}
					else {
						labels[i] = java.lang.Integer.toString(i);
					}
		    	}

		    	Type children = tf.tupleType(fields, labels);
		    	env.storeConstructor(tf.constructorFromTuple(sort, altName, children));
		    }
		    else if (var.isNillaryConstructor()) {
		    	env.storeConstructor(tf.constructor(sort, altName, new Object[] { }));
		    }
		    else if (var.isAnonymousConstructor()) {
		    	// TODO remove syntactic support for anonymous constructors
		    	throw new RascalBug("Anonymous constructors are not allowed anymore, have to change syntax");
//		    	Type argType = var.getType().accept(te);
//		    	String label = var.getName().toString();
//		    	tf.extendAbstractDataType(sort, argType, label);
//		    	env.storeDefinition(sort, argType);
		    }
		}
		
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationAlias(
			org.meta_environment.rascal.ast.Declaration.Alias x) {
		// TODO add support for parameterized types
		String user = x.getUser().getName().toString();
		Type[] params;
		if (x.getUser().isParametric()) {
			java.util.List<TypeVar> formals = x.getUser().getParameters();
			params = new Type[formals.size()];
			int i = 0;
			for (TypeVar formal : formals) {
				Type bound = formal.hasBound() ? formal.getBound().accept(te) : tf.valueType();
				params[i++] = tf.parameterType(Names.name(formal.getName()), bound);
			}
		}
		else {
			params = new Type[0];
		}
		Type base = x.getBase().accept(te);
		Type decl = tf.aliasType(user, base, params);
		env.storeTypeAlias(decl);
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationView(View x) {
		// TODO implement
		throw new RascalBug("views are not yet implemented");
	}
	
	@Override
	public EvalResult visitDeclarationRule(Rule x) {
		return x.getRule().accept(this);
	}
	
	@Override
	public EvalResult visitRuleArbitrary(Arbitrary x) {
		MatchPattern pv = x.getPattern().accept(pe);
		//System.err.println("visitRule: " + pv.getType(this));
		env.storeRule(pv.getType(this), x);
		return result();
	}
	
	@Override
	public EvalResult visitRuleReplacing(Replacing x) {
		MatchPattern pv = x.getPattern().accept(pe);
		System.err.println("visitRule: " + pv.getType(this));
		env.storeRule(pv.getType(this), x);
		return result();
	}
	
	@Override
	public EvalResult visitRuleGuarded(Guarded x) {
		//TODO adapt to new scheme
		EvalResult result = x.getRule().getPattern().getPattern().accept(this);
		if (!result.type.isSubtypeOf(x.getType().accept(te))) {
			throw new RascalTypeError("Declared type of rule does not match type of left-hand side: " + x);
		}
		return x.getRule().accept(this);
	}
	
	@Override
	public EvalResult visitDeclarationTag(Tag x) {
		throw new RascalBug("tags are not yet implemented");
	}
	
	// Variable Declarations -----------------------------------------------

	@Override
	public EvalResult visitLocalVariableDeclarationDefault(Default x) {
		// TODO deal with dynamic variables
		return x.getDeclarator().accept(this);
	}

	@Override
	public EvalResult visitDeclaratorDefault(
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type declaredType = x.getType().accept(te);
		EvalResult r = result();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			if (var.isUnInitialized()) {  // variable declaration without initialization
				r = result(declaredType, null);
				env.top().storeVariable(var.getName(), r);
			} else {                     // variable declaration with initialization
				EvalResult v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.type, bindings);
					declaredType = declaredType.instantiate(bindings);
					r = result(declaredType, v.value);
					env.top().storeVariable(var.getName(), r);
				} else {
					throw new RascalTypeError("variable " + var.getName() + ": declared type " + declaredType + " incompatible with initialization type " + v.type);
				}
			}
		}
		return r;
	}
	
	// Function calls and node constructors
	
	@Override
	public EvalResult visitClosureAsFunctionEvaluated(Evaluated x) {
		Expression expr = x.getExpression();
		
		if (expr.isQualifiedName()) {
			
		}
		
		return result(vf.string(Names.name(Names.lastName(expr.getQualifiedName()))));
	}
	
	@Override
	public EvalResult visitExpressionClosureCall(ClosureCall x) {
		EvalResult func = x.getClosure().getExpression().accept(this);
		java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();

		IValue[] actuals = new IValue[args.size()];
		Type[] types = new Type[args.size()];

		for (int i = 0; i < args.size(); i++) {
			EvalResult resultElem = args.get(i).accept(this);
			types[i] = resultElem.type;
			actuals[i] = resultElem.value;
		}

		Type actualTypes = tf.tupleType(types);

		if (func.type == ClosureResult.getClosureType()) {
			ClosureResult closure = (ClosureResult) func.value;
			return closure.call(actuals, actualTypes);
		}
		else {
			throw new RascalTypeError("Expected a closure, a function or an operator, but got a " + func.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionCallOrTree(CallOrTree x) {
		 java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();
		 QualifiedName name = x.getQualifiedName();
		 
		 IValue[] actuals = new IValue[args.size()];
		 Type[] types = new Type[args.size()];

		 for (int i = 0; i < args.size(); i++) {
			 EvalResult resultElem = args.get(i).accept(this);
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
	
	private EvalResult call(QualifiedName name, IValue[] actuals, Type actualTypes) {
		EnvironmentHolder envHolder = new EnvironmentHolder();
		 FunctionDeclaration func = env.getFunction(name, actualTypes, envHolder);

		 if (func != null) {
			 
			 try {
				 env.pushFrame(envHolder.getEnvironment());
				 Type formals = func.getSignature().getParameters().accept(te);
					
				 EvalResult res = call(func, formals, actuals, actualTypes);

				 return res;
			 }
			 finally {
				 env.popFrame();
			 }
		 }
		 else {
			 StringBuffer sb = new StringBuffer();
			 String sep = "";
			 for(int i = 0; i < actualTypes.getArity(); i++){
				 sb.append(sep);
				 sep = ", ";
				 sb.append(actualTypes.getFieldType(i).toString());
			 }
			 throw new RascalTypeError("No function/constructor " + name + "(" +  sb.toString() + ") is defined");
		 }
	}

	protected boolean isTreeConstructorName(QualifiedName name, Type signature) {
		java.util.List<Name> names = name.getNames();
		
		if (names.size() > 1) {
			String sort = Names.sortName(name);
			Type sortType = env.getAbstractDataType(sort);
			
			if (sortType != null) {
				String cons = Names.consName(name);
				
				if (env.getConstructor(sortType, cons, signature) != null) {
					return true;
				}
			}
			else {
				if (!env.existsModule(sort)) {
					throw new RascalTypeError("Qualified name is neither module name nor data type name");
				}
			}
		}
		else {
			String cons = Names.consName(name);
			if (env.getConstructor(cons, signature) != null) {
				return true;
			}
		}
		
		return false;
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
	private EvalResult constructTree(QualifiedName functionName, IValue[] actuals, Type signature) {
		String sort;
		String cons;
		
		cons = Names.consName(functionName);
		sort = Names.sortName(functionName);

		Type candidate = null;
	
		if (sort != null) {
			Type sortType = env.getAbstractDataType(sort);
			
			if (sortType != null) {
			  candidate = env.getConstructor(sortType, cons, signature);
			}
			else {
			  return result(tf.nodeType(), vf.node(cons, actuals));
			}
		}
		
		candidate = env.getConstructor(cons, signature);
		if (candidate != null) {
			return result(candidate.getAbstractDataType(), candidate.make(vf, actuals));
		}
		
		return result(tf.nodeType(), vf.node(cons, actuals));
	}
	
	@Override
	public EvalResult visitExpressionFunctionAsValue(FunctionAsValue x) {
		return x.getFunction().accept(this);
	}
	
	@Override
	public EvalResult visitFunctionAsValueDefault(
			org.meta_environment.rascal.ast.FunctionAsValue.Default x) {
		Name name = x.getName();
		Type formals = tf.voidType();
		EnvironmentHolder h = new EnvironmentHolder();
		FunctionDeclaration func = env.getFunction(Names.name(name), formals, h);
		
		if (func == null) {
			throw new RascalTypeError("Could not find function " + name);
		}
		
		return new ClosureResult(this, func, h.getEnvironment());
	}

	private StringBuffer showCall(FunctionDeclaration func, IValue[] actuals, String arrow){
		StringBuffer trace = new StringBuffer();
		for(int i = 0; i < callNesting; i++){
			trace.append("-");
		}
		trace.append(arrow).append(" ").append(func.getSignature().getName()).append("(");
		String sep = "";
		for(int i = 0; i < actuals.length; i++){
			trace.append(sep).append(actuals[i]);
			sep = ", ";
		}
		trace.append(")");
		return trace;
	}
	
	public EvalResult call(FunctionDeclaration func, Type formalTypes, IValue[] actuals, Type actualTypes) {
		if (func.getSignature().getParameters().isVarArgs()) {
			Type newActualTypes = computeVarArgsActualTypes(actualTypes, formalTypes);
			
			if (actualTypes != newActualTypes) {
				actuals = computeVarArgsActuals(actuals, formalTypes);
			}
			
			actualTypes = newActualTypes;
		}
		
		if(callTracing){
			EvalResult res;
			System.err.println(showCall(func, actuals, ">").toString());
			callNesting++;
			if (isJavaFunction(func)) { 
				res = callJavaFunction(func, formalTypes, actuals, actualTypes);
			}
			else {
				res = callRascalFunction(func, formalTypes, actuals, actualTypes);
			}

			callNesting--;
			StringBuffer trace = showCall(func, actuals, "<");
			trace.append(" returns ").append(res.value);
			System.err.println(trace.toString());
			return res;
		} else {
			if (isJavaFunction(func)) { 
				return callJavaFunction(func, formalTypes, actuals, actualTypes);
			}
			else {
				return callRascalFunction(func, formalTypes, actuals, actualTypes);
			}
		}

	}

	private IValue[] computeVarArgsActuals(IValue[] actuals, Type formals) {
		int arity = formals.getArity();
		IValue[] newActuals = new IValue[arity];
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			newActuals[i] = actuals[i];
		}
		
		Type lub = tf.voidType();
		for (int j = i; j < actuals.length; j++) {
			lub = lub.lub(actuals[j].getType());
		}
		
		IListWriter list = vf.listWriter(lub);
		list.insertAt(0, actuals, i, actuals.length - arity + 1);
		newActuals[i] = list.done();
		return newActuals;
	}

	private Type computeVarArgsActualTypes(Type actualTypes, Type formals) {
		if (actualTypes.isSubtypeOf(formals)) {
			// the argument is already provided as a list
			return actualTypes;
		}
		
		int arity = formals.getArity();
		Type[] types = new Type[arity];
		java.lang.String[] labels = new java.lang.String[arity];
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			types[i] = formals.getFieldType(i);
			labels[i] = formals.getFieldName(i);
		}
		
		Type lub = tf.voidType();
		for (int j = i; j < actualTypes.getArity(); j++) {
			lub = lub.lub(actualTypes.getFieldType(j));
		}
		
		types[i] = tf.listType(lub);
		labels[i] = formals.getFieldName(i);
		
		return tf.tupleType(types, labels);
	}

	private boolean isJavaFunction(FunctionDeclaration func) {
		java.util.List<FunctionModifier> mods = func.getSignature().getModifiers().getModifiers();
		for (FunctionModifier m : mods) {
			if (m.isJava()) {
				return true;
			}
		}
		
		return false;
	}

	private EvalResult callJavaFunction(FunctionDeclaration func,
			Type formals, IValue[] actuals, Type actualTypes) {
		Type type = func.getSignature().getType().accept(te);
		
		try {
			env.pushFrame();
			IValue result = javaFunctionCaller.callJavaMethod(func, actuals);
			bindTypeParameters(actualTypes, formals);
			Type resultType = type.instantiate(env.getTypeBindings());
			return result(resultType, result);
		}
		finally {
			env.popFrame();
		}
	}

	private EvalResult callRascalFunction(FunctionDeclaration func,
			Type formals, IValue[] actuals, Type actualTypes) {
		try {
			env.pushFrame();
			
			bindTypeParameters(actualTypes, formals);
			
			for (int i = 0; i < formals.getArity(); i++) {
				Type formal = formals.getFieldType(i).instantiate(env.getTypeBindings());
				EvalResult result = normalizedResult(formal, actuals[i]);
				env.top().storeVariable(formals.getFieldName(i), result);
			}
			
			if (func.getBody().isDefault()) {
			  func.getBody().accept(this);
			}
			else {
				throw new RascalTypeError("Java method body without a java function modifier in:\n" + func);
			}
			if(!func.getSignature().getType().accept(te).isVoidType()){
				throw new RascalTypeError("Function definition:" + func + "\n does not have a return statement.");
			}
			return result(tf.voidType(), null);
		}
		catch (ReturnException e) {
			EvalResult result = e.getValue();
			result.type = result.type.instantiate(env.getTypeBindings());
			Type returnType = func.getSignature().getType().accept(te);
			if(!result.type.isSubtypeOf(returnType)){
				throw new RascalTypeError("Actual return type " + result.type + " is not compatible with declared return type " + returnType);
			}
			return result;
		} 
		catch (FailureException e){
			throw new RascalRunTimeError("Fail statement occurred outside switch or visit statement");
		}
		finally {
			env.popFrame();
		}
	}



	@Override
	public EvalResult visitFunctionBodyDefault(
			org.meta_environment.rascal.ast.FunctionBody.Default x) {
		EvalResult result = result();
		
		for (Statement statement : x.getStatements()) {
			result = statement.accept(this);
		}
		
		return result;
	}
	
   // Statements ---------------------------------------------------------
	
	@Override
	public EvalResult visitStatementAssert(Assert x) {
		String msg = x.getMessage().toString();
		EvalResult r = x.getExpression().accept(this);
		if(r.type.equals(tf.boolType())){
			if(r.value.isEqual(vf.bool(false))){
				System.err.println("Assertion failed: " + msg + "\n");
			}
		} else {
			throw new RascalTypeError("expression in assertion should be bool instead of " + r.type);
		}
		return r;	
	}
	
	@Override
	public EvalResult visitStatementVariableDeclaration(VariableDeclaration x) {
		return x.getDeclaration().accept(this);
	}
	
	@Override
	public EvalResult visitStatementExpression(Statement.Expression x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public EvalResult visitStatementFunctionDeclaration(
			org.meta_environment.rascal.ast.Statement.FunctionDeclaration x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	EvalResult assignVariable(QualifiedName name, EvalResult right){
		EvalResult previous = env.getVariable(name);
		if (previous != null) {
			if (right.type.isSubtypeOf(previous.type)) {
				right.type = previous.type;
			} else {
				throw new RascalTypeError("Variable " + name
						+ " has type " + previous.type
						+ "; cannot assign value of type " + right.type);
			}
		}
		env.top().storeVariable(name.toString(), right);
		return right;
	}
	
	@Override
	public EvalResult visitExpressionSubscript(Subscript x) {
		
		EvalResult expr = x.getExpression().accept(this);
		Type exprType = expr.type;
		int nSubs = x.getSubscripts().size();
		
		if (exprType.isRelationType()) {
			int relArity = exprType.getArity();
			
			if(nSubs >= relArity){
				throw new RascalTypeError("Too many subscripts (" + nSubs + ") for relation of arity " + relArity);
			}
			EvalResult subscriptResult[] = new EvalResult[nSubs];
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
								relFieldType);
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
			throw new RascalTypeError("Too many subscripts");
		}
		
		EvalResult subs = x.getSubscripts().get(0).accept(this);
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
			throw new RascalTypeError("Subscript should have type integer");
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
		if ((exprType.isAbstractDataType() || exprType.isConstructorType())) {
			if(index >= ((IConstructor) expr.value).arity()){
				throw new RascalRunTimeError("Subscript out of bounds");
			}
			Type elementType = ((IConstructor) expr.value).getType()
					.getFieldType(index);
			IValue element = ((INode) expr.value).get(index);
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
	public EvalResult visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		EvalResult expr = x.getExpression().accept(this);
		String field = x.getField().toString();
		
		if (expr.type.isTupleType()) {
			Type tuple = expr.type;
			if (!tuple.hasFieldNames()) {
				throw new RascalTypeError("Tuple does not have field names: " + tuple);
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
				throw new RascalTypeError(expr.type + " does not have a field named " + field);
			}
			
			if (!node.hasField(field)) {
				throw new RascalException(vf, "Field " + field + " accessed on constructor that does not have it." + expr.value.getType());
			}
			
			int index = node.getFieldIndex(field);
			return normalizedResult(node.getFieldType(index),((IConstructor) expr.value).get(index));
		}
		
		throw new RascalTypeError("Field selection is not allowed on " + expr.type);
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
	public EvalResult visitExpressionFieldProject(FieldProject x) {
		EvalResult  base = x.getExpression().accept(this);
		
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
						throw new RascalTypeError("Undefined field " + fieldName + " in projection");
					}
				}
				if(selectedFields[i] < 0 || selectedFields[i] > base.type.getArity()){
					throw new RascalTypeError("Index " + selectedFields[i] + " in projection exceeds arity of tuple");
				}
				fieldTypes[i] = base.type.getFieldType(selectedFields[i]);
			}
			if(duplicateIndices(selectedFields)){
				throw new RascalTypeError("Duplicate fields in projection");
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
						throw new RascalTypeError("Undefined field " + fieldName + " in projection");
					}
				}
				if(selectedFields[i] < 0 || selectedFields[i] > base.type.getArity()){
					throw new RascalTypeError("Index " + selectedFields[i] + " in projection exceeds arity of tuple");
				}
				fieldTypes[i] = base.type.getFieldType(selectedFields[i]);
			}
			if(duplicateIndices(selectedFields)){
				throw new RascalTypeError("Duplicate fields in projection");
			}
			Type resultType = nFields == 1 ? tf.setType(fieldTypes[0]) : tf.relType(fieldTypes);
			
			return result(resultType, ((IRelation)base.value).select(selectedFields));				     
		}
		throw new RascalTypeError("Type " + base.type + " does not allow projection");
	}
	
	@Override
	public EvalResult visitStatementFail(Fail x) {
		if (x.getFail().isWithLabel()) {
			throw FailureException.getInstance(x.getFail().getLabel().toString());
		}
		else {
		  throw FailureException.getInstance();
		}
	}
	
	@Override
	public EvalResult visitStatementReturn(
			org.meta_environment.rascal.ast.Statement.Return x) {
		org.meta_environment.rascal.ast.Return r = x.getRet();
		
		if (r.isWithExpression()) {
		  throw ReturnException.getInstance(x.getRet().getExpression().accept(this));
		}
		else {
			throw ReturnException.getInstance(result(tf.voidType(), null));
		}
	}
	
	@Override
	public EvalResult visitStatementBreak(Break x) {
		throw new RascalBug("NYI break" + x); // TODO
	}
	
	@Override
	public EvalResult visitStatementContinue(Continue x) {
		throw new RascalBug("NYI" + x); // TODO
	}
	
	@Override
	public EvalResult visitStatementGlobalDirective(GlobalDirective x) {
		throw new RascalBug("NYI" + x); // TODO
	}
	
	@Override
	public EvalResult visitStatementThrow(Throw x) {
		throw new RascalException(x.getExpression().accept(this).value);
	}
	
	@Override
	public EvalResult visitStatementTry(Try x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), null);
	}
	
	@Override
	public EvalResult visitStatementTryFinally(TryFinally x) {
		return evalStatementTry(x.getBody(), x.getHandlers(), x.getFinallyBody());
	}
	
	private EvalResult evalStatementTry(Statement body, java.util.List<Catch> handlers, Statement finallyBody){
		EvalResult res = result();
		
		try {
			res = body.accept(this);

		} catch (RascalException e){
			
			IValue eValue = e.getException();
			Type eType = eValue.getType();

			for(Catch c : handlers){
				if(c.isDefault()){
					res = c.getBody().accept(this);
					break;
				} else {
					if(eType.isSubtypeOf(c.getType().accept(te))){
						//System.err.println("matching case: " + c);
						try {
							env.pushFrame();	// Create local scope for executing handler	
							Name name = c.getName();
							env.top().storeVariable(name, normalizedResult(eType, eValue));
							res =  c.getBody().accept(this);
							break;
						} finally {
							env.popFrame();
						}
					}
				}
			}
		}
		if(finallyBody != null){
			finallyBody.accept(this);
		}
		return res;
	}
	
	@Override
	public EvalResult visitStatementVisit(
			org.meta_environment.rascal.ast.Statement.Visit x) {
		return x.getVisit().accept(this);
	}
	
	@Override
	public EvalResult visitStatementInsert(Insert x) {
		throw InsertException.getInstance(x.getExpression().accept(this));
	}
	
	@Override
	public EvalResult visitStatementAssignment(Assignment x) {
		EvalResult right = x.getExpression().accept(this);
		return x.getAssignable().accept(new AssignableEvaluator(env, right, this));
	}
	
	@Override
	public EvalResult visitStatementBlock(Block x) {
		EvalResult r = result();
		try {
			env.pushFrame(); // blocks are scopes
			for(Statement stat : x.getStatements()){
				r = stat.accept(this);
			}
		}
		finally {
			env.popFrame();
		}
		return r;
	}
  
	@Override
	public EvalResult visitAssignableVariable(
			org.meta_environment.rascal.ast.Assignable.Variable x) {
		return env.getVariable(x.getQualifiedName().toString());
	}
	
	@Override
	public EvalResult visitAssignableFieldAccess(FieldAccess x) {
		EvalResult receiver = x.getReceiver().accept(this);
		String label = x.getField().toString();
	
		if (receiver.type.isTupleType()) {
			IValue result = ((ITuple) receiver.value).get(label);
			Type type = ((ITuple) receiver.value).getType().getFieldType(label);
			return normalizedResult(type, result);
		}
		else if (receiver.type.isConstructorType() || receiver.type.isAbstractDataType()) {
			IValue result = ((IConstructor) receiver.value).get(label);
			Type treeNodeType = ((IConstructor) receiver.value).getType();
			Type type = treeNodeType.getFieldType(treeNodeType.getFieldIndex(label));
			return normalizedResult(type, result);
		}
		else {
			throw new RascalTypeError(x.getReceiver() + " has no field named " + label);
		}
	}
	
	@Override
	public EvalResult visitAssignableAnnotation(
			org.meta_environment.rascal.ast.Assignable.Annotation x) {
		EvalResult receiver = x.getReceiver().accept(this);
		String label = x.getAnnotation().toString();
		
		if (receiver.type.declaresAnnotation(label)) {
			throw new RascalTypeError("No annotation " + label + " declared for " + receiver.type);
		}
		
		// TODO get annotation from local and imported environments
		Type type = tf.getAnnotationType(receiver.type, label);
		IValue value = ((IConstructor) receiver.value).getAnnotation(label);
		
		return normalizedResult(type, value);
	}
	
	@Override
	public EvalResult visitAssignableConstructor(Constructor x) {
		throw new RascalBug("constructor assignable does not represent a value:" + x);
	}
	
	@Override
	public EvalResult visitAssignableIfDefined(
			org.meta_environment.rascal.ast.Assignable.IfDefined x) {
		throw new RascalBug("ifdefined assignable does not represent a value");
	}
	
	@Override
	public EvalResult visitAssignableSubscript(
			org.meta_environment.rascal.ast.Assignable.Subscript x) {
		EvalResult receiver = x.getReceiver().accept(this);
		EvalResult subscript = x.getSubscript().accept(this);
		
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

		throw new RascalTypeError("Illegal subscript " + x.getSubscript() + " for receiver " + x.getReceiver());
	}
	
	@Override
	public EvalResult visitAssignableTuple(
			org.meta_environment.rascal.ast.Assignable.Tuple x) {
		throw new RascalBug("tuple in assignable does not represent a value:" + x);
	}
	
	@Override
	public EvalResult visitAssignableAmbiguity(
			org.meta_environment.rascal.ast.Assignable.Ambiguity x) {
		throw new RascalBug("ambiguous Assignable: " + x);
	}
	
	@Override
	public EvalResult visitFunctionDeclarationDefault(
			org.meta_environment.rascal.ast.FunctionDeclaration.Default x) {
		Signature sig = x.getSignature();
		String name = sig.getName().toString();
		
		if (isJavaFunction(x)) {
			javaFunctionCaller.compileJavaMethod(x);
		}
		else if (!x.getBody().isDefault()) {
			throw new RascalTypeError("Java function body without java function modifier in: " + x);
		}
		
		env.storeFunction(name,(org.meta_environment.rascal.ast.FunctionDeclaration)x);
		return result();
	}
	
	@Override
	public EvalResult visitStatementIfThenElse(IfThenElse x) {
		env.pushFrame(); // For the benefit of variables bound in the condition
		try {
			for (org.meta_environment.rascal.ast.Expression expr : x
					.getConditions()) {
				EvalResult cval = expr.accept(this);
				if (cval.type.isBoolType()) {
					if (cval.value.isEqual(vf.bool(false))) {
						return x.getElseStatement().accept(this);
					}
				} else {
					throw new RascalTypeError("Condition " + expr + " has type "
							+ cval.type + " but should be bool");
				}
			}
			return x.getThenStatement().accept(this);
		} finally {
			env.popFrame();	// Remove any bindings due to condition evaluation.
		}
	}

	@Override
	public EvalResult visitStatementIfThen(IfThen x) {
		for (org.meta_environment.rascal.ast.Expression expr : x
				.getConditions()) {
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.isEqual(vf.bool(false))) {
					return result();
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		}
		return x.getThenStatement().accept(this);
	}

	@Override
	public EvalResult visitStatementWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		EvalResult statVal = result();
		do {
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.isEqual(vf.bool(false))) {
					return statVal;
				} else {
					statVal = x.getBody().accept(this);
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		} while (true);
	}
	
	@Override
	public EvalResult visitStatementDoWhile(DoWhile x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		do {
			EvalResult result = x.getBody().accept(this);
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.isEqual(vf.bool(false))) {
					return result;
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		} while (true);
	}
	
    @Override
    public EvalResult visitExpressionMatch(Match x) {
    	return new MatchEvaluator(x.getPattern(), x.getExpression(), true, this).next();
    }
    
    @Override
    public EvalResult visitExpressionNoMatch(NoMatch x) {
    	return new MatchEvaluator(x.getPattern(), x.getExpression(), false, this).next();
    }
	
	// ----- General method for matching --------------------------------------------------
    
    protected MatchPattern evalPattern(org.meta_environment.rascal.ast.Expression pat){
    	if(pe.isPattern(pat)){
    		return pat.accept(pe);
    	} else if(re.isRegExpPattern(pat)){ 
			return pat.accept(re);
		} else {
			throw new RascalTypeError("pattern expected instead of " + pat);
		}
    }
	
	private boolean matchOne(IValue subj, org.meta_environment.rascal.ast.Expression pat){
		//System.err.println("matchOne: subj=" + subj + ", pat= " + pat);
		MatchPattern mp = evalPattern(pat);
		lastPattern = mp;
		mp.initMatch(subj, this);
		return mp.next();
	}


	// Expressions -----------------------------------------------------------

	@Override
	public EvalResult visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public EvalResult visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}

	@Override
	public EvalResult visitLiteralReal(Real x) {
		String str = x.getRealLiteral().toString();
		return result(vf.dubble(java.lang.Double.parseDouble(str)));
	}

	@Override
	public EvalResult visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		return result(vf.bool(str.equals("true")));
	}

	@Override
	public EvalResult visitLiteralString(
			org.meta_environment.rascal.ast.Literal.String x) {
		String str = x.getStringLiteral().toString();
		return result(vf.string(deescape(str)));
	}

	private String deescape(String str) {
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
				EvalResult val = env.getVariable(var.toString());
				String replacement;
				if(val == null || val.value == null){
					replacement = "**undefined**";	
				} else {
					if(val.type.isStringType()){
						replacement = ((IString)val.value).getValue();
					} else {
						replacement = val.value.toString();
					}
				}
				replacement = replacement.replaceAll("<", "\\\\<");
				int len = replacement.length();
				b = replacement.charAt(len-1);
				result.append(replacement.substring(0, len-1));
				break;
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
	public EvalResult visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = x.getDecimal().toString();
		return result(vf.integer(java.lang.Integer.parseInt(str)));
	}
	
	@Override
	public EvalResult visitExpressionQualifiedName(
			org.meta_environment.rascal.ast.Expression.QualifiedName x) {
		if (isTreeConstructorName(x.getQualifiedName(), tf.tupleEmpty())) {
			return constructTree(x.getQualifiedName(), new IValue[0], tf.tupleType(new Type[0]));
		}
		else {
			EvalResult result = env.getVariable(x.getQualifiedName());

			if (result != null && result.value != null) {
				return result;
			} else {
				throw new RascalUndefinedValue("Uninitialized variable: " + x);
			}
		}
	}
	
	@Override
	public EvalResult visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType = tf.voidType();
		java.util.List<IValue> results = new LinkedList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			EvalResult resultElem = expr.accept(this);
			if(resultElem.type.isListType() && !expr.isList()){
				for(IValue val : ((IList) resultElem.value)){
					elementType = elementType.lub(val.getType());
					results.add(val);
				}
			} else {
				elementType = elementType.lub(resultElem.type);
				results.add(results.size(), resultElem.value);
			}
		}
		System.err.println("elementType=" + elementType);
		if(elementType.isAbstractDataType() && elementType.isConstructorType()){
			elementType = elementType.getAbstractDataType();
		}
		Type resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		return result(resultType, w.done());
	}

	@Override
	public EvalResult visitExpressionSet(Set x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType = tf.voidType();
		java.util.List<IValue> results = new LinkedList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			EvalResult resultElem = expr.accept(this);
			if(resultElem.type.isSetType() && !expr.isSet()){
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
	public EvalResult visitExpressionMap(
			org.meta_environment.rascal.ast.Expression.Map x) {

		java.util.List<org.meta_environment.rascal.ast.Mapping> mappings = x
				.getMappings();
		Map<IValue,IValue> result = new HashMap<IValue,IValue>();
		Type keyType = tf.voidType();
		Type valueType = tf.voidType();

		for (org.meta_environment.rascal.ast.Mapping mapping : mappings) {
			EvalResult keyResult = mapping.getFrom().accept(this);
			EvalResult valueResult = mapping.getTo().accept(this);
			
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
	public EvalResult visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		FunctionDeclaration function = convertBlockToFunction(x);
		return new ClosureResult(this, function, env.top());
	}

	private FunctionDeclaration convertBlockToFunction(
			NonEmptyBlock x) {
		INode node = x.getTree();
		org.meta_environment.rascal.ast.Type type = astFactory.makeTypeBasic(node, astFactory.makeBasicTypeVoid(node));
		org.meta_environment.rascal.ast.Formals.Default formals = astFactory.makeFormalsDefault(node, new LinkedList<Formal>());
		org.meta_environment.rascal.ast.Parameters params = astFactory.makeParametersDefault(node, formals);
		java.util.List<Statement> stats = x.getStatements();
		FunctionModifiers mods =astFactory.makeFunctionModifiersList(node, new LinkedList<FunctionModifier>());
		Signature s = astFactory.makeSignatureNoThrows(params.getTree(), type, mods, Names.toName(x.toString()), params);
		Tags tags = astFactory.makeTagsDefault(node, new LinkedList<org.meta_environment.rascal.ast.Tag>());
		FunctionBody body = astFactory.makeFunctionBodyDefault(node, stats);
		return astFactory.makeFunctionDeclarationDefault(node, s, tags, body);
	}

	@Override
	public EvalResult visitExpressionTuple(Tuple x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();

		IValue[] values = new IValue[elements.size()];
		Type[] types = new Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			EvalResult resultElem = elements.get(i).accept(this);
			types[i] = resultElem.type;
			values[i] = resultElem.value;
		}

		return result(tf.tupleType(types), vf.tuple(values));
	}
	
	@Override
	public EvalResult visitExpressionAnnotation(
			org.meta_environment.rascal.ast.Expression.Annotation x) {
		  EvalResult expr = x.getExpression().accept(this);
		String name = x.getName().toString();

		// TODO: get annotations from local and imported environments
		Type annoType = tf.getAnnotationType(expr.type, name);

		if (annoType == null) {
			throw new RascalTypeError("No annotation " + x.getName()
					+ " declared on " + expr.type);
		}

		IValue annoValue = ((IConstructor) expr.value).getAnnotation(name);
		
		if (annoValue == null) {
			// TODO: make this a Rascal exception that can be caught by the programmer
			throw new RascalTypeError("This " + expr.type + " does not have a " + name + " annotation set");
		}
		return result(annoType, annoValue);
	}
	
	private void widenArgs(EvalResult left, EvalResult right){
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
	public EvalResult visitExpressionAddition(Addition x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);

		widenArgs(left, right);
		Type resultType = left.type.lub(right.type);
		
		System.err.println("left=" + left + "; right=" + right + "; resulType=" + resultType);

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
			    System.err.println("left.type = " + left.type);
			    System.err.println("right.type = " + right.type);
			    System.err.println("resultType = " + resultType);
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
					+ left.type + ", " + right.type);
	}
    
	public EvalResult visitExpressionSubtraction(Subtraction x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
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
					+ left.type + ", " + right.type);
	}
	
	@Override
	public EvalResult visitExpressionNegative(Negative x) {
		EvalResult arg = x.getArgument().accept(this);
		
		if (arg.type.isIntegerType()) {
			return result(vf.integer(- intValue(arg)));
		}
		else if (arg.type.isDoubleType()) {
				return result(vf.dubble(- RealValue(arg)));
		} else {
			throw new RascalTypeError(
					"Operand of unary - should be integer or Real instead of: " + arg.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionProduct(Product x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
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
					+ left.type + ", " + right.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionDivision(Division x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
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
					+ left.type + ", " + right.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionModulo(Modulo x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).remainder((IInteger) right.value));
		} 
		else {
			throw new RascalTypeError("Operands of % have illegal types: "
					+ left.type + ", " + right.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public EvalResult visitExpressionIntersection(Intersection x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
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
					+ left.type + ", " + right.type);
		}
	}

	@Override
	public EvalResult visitExpressionOr(Or x) {
		return new OrEvaluator(x, this).next();
	}

	@Override
	public EvalResult visitExpressionAnd(And x) {
		return new AndEvaluator(x, this).next();
	}

	@Override
	public EvalResult visitExpressionNegation(Negation x) {
		return new NegationEvaluator(x, this).next();
	}
	
	@Override
	public EvalResult visitExpressionImplication(Implication x) {
		return new ImplicationEvaluator(x, this).next();
	}
	
	@Override
	public EvalResult visitExpressionEquivalence(Equivalence x) {
		return new EquivalenceEvaluator(x, this).next();
	}
	
	boolean equals(EvalResult left, EvalResult right){
		
		widenArgs(left, right);
		
		if (left.type.comparable(right.type)) {
			return compare(left, right) == 0;
		} else {
			return false;
				//TODO; type error
		}
	}

	@Override
	public EvalResult visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		widenArgs(left, right);
/*		
		if (!left.type.comparable(right.type)) {
			throw new RascalTypeError("Arguments of equals have incomparable types: " + left.type + " and " + right.type);
		}
*/
		
		return result(vf.bool(equals(left, right)));
	}
	
	@Override
	public EvalResult visitExpressionOperatorAsValue(OperatorAsValue x) {
		// TODO
		throw new RascalBug("Operator as value not yet implemented:" + x);
	}
	
	@Override
	public EvalResult visitExpressionArea(Area x) {
		EvalResult beginLine = x.getBeginLine().accept(this);
		EvalResult endLine = x.getEndLine().accept(this);
		EvalResult beginColumn = x.getBeginColumn().accept(this);
		EvalResult endColumn = x.getEndColumn().accept(this);
		EvalResult length = x.getLength().accept(this);
		EvalResult offset = x.getOffset().accept(this);
		
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
	public EvalResult visitExpressionAreaInFileLocation(AreaInFileLocation x) {
		
	   EvalResult area = x.getAreaExpression().accept(this);
	   
	   if (!area.type.isSubtypeOf(tf.sourceRangeType())) {
		   throw new RascalTypeError("Expected area, got " + area.type);
	   }
	   
	   EvalResult file = x.getFilename().accept(this);
	   
	   if (!area.type.isSubtypeOf(tf.sourceRangeType())) {
		   throw new RascalTypeError("Expected area, got " + file.type);
	   }
	   
	   checkType(area, tf.sourceRangeType());
	   checkType(file, tf.stringType());
	   
	   ISourceRange range = (ISourceRange) area.value;
	   
	   return result(tf.sourceLocationType(), vf.sourceLocation(stringValue(file), range));
	}
	
	@Override
	public EvalResult visitExpressionClosure(Closure x) {
		FunctionDeclaration f = convertClosureToFunctionDeclaration(x);
		return new ClosureResult(this, f, env.top());
	}

	@Override
	public EvalResult visitExpressionVoidClosure(VoidClosure x) {
		FunctionDeclaration f = convertVoidClosureToFunctionDeclaration(x);
		return new ClosureResult(this, f, env.top());
	}
	
	/**
	 * To be compatible with the call functions, we convert a closure to a plain
	 * old function declaration here.
	 * 
	 * TODO: make the call functions work on parameter lists and bodies instead,
	 * since the name of the function is not used after the function declaration
	 * has been looked up. That should remove the need for this clumsiness.
	 */
	private FunctionDeclaration convertClosureToFunctionDeclaration(Closure x) {
		org.meta_environment.rascal.ast.Type type = x.getType();
		org.meta_environment.rascal.ast.Parameters params = x.getParameters();
		java.util.List<Statement> stats = x.getStatements();
		INode node = x.getTree();
		FunctionModifiers mods =astFactory.makeFunctionModifiersList(node, new LinkedList<FunctionModifier>());
		Signature s = astFactory.makeSignatureNoThrows(params.getTree(), type, mods, Names.toName(x.toString()), params);
		Tags tags = astFactory.makeTagsDefault(node, new LinkedList<org.meta_environment.rascal.ast.Tag>());
		FunctionBody body = astFactory.makeFunctionBodyDefault(node, stats);
		return astFactory.makeFunctionDeclarationDefault(node, s, tags, body);
	}
	
	/**
	 * To be compatible with the call functions, we convert a closure to a plain
	 * old function declaration here.
	 * 
	 * TODO: make the call functions work on parameter lists and bodies instead,
	 * since the name of the function is not used after the function declaration
	 * has been looked up. That should remove the need for this clumsiness.
	 */
	private FunctionDeclaration convertVoidClosureToFunctionDeclaration(VoidClosure x) {
		INode node = x.getTree();
		org.meta_environment.rascal.ast.Type type = astFactory.makeTypeBasic(node, astFactory.makeBasicTypeVoid(node));
		org.meta_environment.rascal.ast.Parameters params = x.getParameters();
		java.util.List<Statement> stats = x.getStatements();
		FunctionModifiers mods =astFactory.makeFunctionModifiersList(node, new LinkedList<FunctionModifier>());
		Signature s = astFactory.makeSignatureNoThrows(params.getTree(), type, mods, Names.toName(x.toString()), params);
		Tags tags = astFactory.makeTagsDefault(node, new LinkedList<org.meta_environment.rascal.ast.Tag>());
		FunctionBody body = astFactory.makeFunctionBodyDefault(node, stats);
		return astFactory.makeFunctionDeclarationDefault(node, s, tags, body);
	}
	
	@Override
	public EvalResult visitExpressionFieldUpdate(FieldUpdate x) {
		EvalResult expr = x.getExpression().accept(this);
		EvalResult repl = x.getReplacement().accept(this);
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
				Type tuple = expr.type.getFieldTypes();
				Type argType = tuple.getFieldType(name);
				IConstructor value = (IConstructor) expr.value;
				
				checkType(repl.type, argType);
				
				return result(expr.type, value.set(name, repl.value));
			}
			else {
				throw new RascalTypeError("Field updates only possible on tuples with labeled fields, relations with labeled fields and data constructors");
			}
		} catch (FactTypeError e) {
			throw new RascalTypeError(e.getMessage(), e);
		}
	}
	
	@Override
	public EvalResult visitExpressionFileLocation(FileLocation x) {
		EvalResult result = x.getFilename().accept(this);
		
		 if (!result.type.isSubtypeOf(tf.stringType())) {
			   throw new RascalTypeError("Expected area, got " + result.type);
		 }
		 
		 return result(tf.sourceLocationType(), 
				 vf.sourceLocation(((IString) result.value).getValue(), 
				 vf.sourceRange(0, 0, 1, 1, 0, 0)));
	}
	
	@Override
	public EvalResult visitExpressionLexical(Lexical x) {
		throw new RascalBug("Lexical NYI: " + x);// TODO
	}
	
	@Override
	public EvalResult visitExpressionRange(Range x) {
		IListWriter w = vf.listWriter(tf.integerType());
		EvalResult from = x.getFirst().accept(this);
		EvalResult to = x.getLast().accept(this);

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
	public EvalResult visitExpressionStepRange(StepRange x) {
		IListWriter w = vf.listWriter(tf.integerType());
		EvalResult from = x.getFirst().accept(this);
		EvalResult to = x.getLast().accept(this);
		EvalResult second = x.getSecond().accept(this);

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
	public EvalResult visitExpressionTypedVariable(TypedVariable x) {
		throw new RascalTypeError("Use of typed variable outside matching context");
	}
	
	private boolean matchAndEval(IValue subject, org.meta_environment.rascal.ast.Expression pat, Statement stat){
		MatchPattern mp = evalPattern(pat);
		mp.initMatch(subject, this);
		lastPattern = mp;
		//System.err.println("matchAndEval: subject=" + subject + ", pat=" + pat);
		try {
			env.pushFrame(); 	// Create a separate scope for match and statement
			while(mp.hasNext()){
				if(mp.next()){
					try {
						//System.err.println(env);
						stat.accept(this);
						return true;
					} catch (FailureException e){
						//System.err.println("failure occurred");
					}
				}
			}
		} finally {
			env.popFrame();
		}
		return false;
	}
	
	private boolean matchEvalAndReplace(IValue subject, 
			org.meta_environment.rascal.ast.Expression pat, 
			java.util.List<Expression> conditions,
			Expression replacementExpr){
		MatchPattern mp = evalPattern(pat);
		mp.initMatch(subject, this);
		lastPattern = mp;
		//System.err.println("matchEvalAndEval: subject=" + subject + ", pat=" + pat + ", conditions=" + conditions);
		try {
			env.pushFrame(); 	// Create a separate scope for match and statement
			while(mp.hasNext()){
				if(mp.next()){
					try {
						boolean trueConditions = true;
						for(Expression cond : conditions){
							if(!cond.accept(this).isTrue()){
								trueConditions = false;
								break;
							}
						}
						if(trueConditions){
							throw InsertException.getInstance(replacementExpr.accept(this));		
						}
					} catch (FailureException e){
						//System.err.println("failure occurred");
					}
				}
			}
		} finally {
			env.popFrame();
		}
		return false;
	}
	
	@Override
	public EvalResult visitStatementSwitch(Switch x) {
		EvalResult subject = x.getExpression().accept(this);

		for(Case cs : x.getCases()){
			if(cs.isDefault()){
				return cs.getStatement().accept(this);
			}
			org.meta_environment.rascal.ast.Rule rule = cs.getRule();
			if(rule.isArbitrary() && matchAndEval(subject.value, rule.getPattern(), rule.getStatement())){
				return result();
			} else if(rule.isGuarded())	{
				org.meta_environment.rascal.ast.Type tp = rule.getType();
				Type t = tp.accept(te);
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
	public EvalResult visitExpressionVisit(Visit x) {
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
		java.util.List<StringReplacement> replacements = new LinkedList<StringReplacement>();
		boolean matched = false;
		boolean changed = false;
		int cursor = 0;
		
		Case cs = (Case) singleCase(casesOrRules);
		
		if(cs != null && cs.isRule() && re.isRegExpPattern(cs.getRule().getPattern())){
			/*
			 * In the frequently occurring case that there is one case with a regexp as pattern,
			 * we can delegate all the work to the regexp matcher.
			 */
			org.meta_environment.rascal.ast.Rule rule = cs.getRule();
			
			Expression patexp = rule.getPattern();
			MatchPattern mp = evalPattern(patexp);
			mp.initMatch(subject, this);

			try {
				env.pushFrame(); 	// Create a separate scope for match and statement/replacement
				while(mp.hasNext()){
					if(mp.next()){
						try {
							if(rule.isReplacing()){
								Replacement repl = rule.getReplacement();
								boolean trueConditions = true;
								if(repl.isConditional()){
									for(Expression cond : repl.getConditions()){
										EvalResult res = cond.accept(this);
										if(!res.isTrue()){         // TODO: How about alternatives?
											trueConditions = false;
											break;
										}
									}
								}
								if(trueConditions){
									throw InsertException.getInstance(repl.getReplacementExpression().accept(this));
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
				env.popFrame();
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
	 * traverseOnce: traverse an arbitrary IVAlue once. Implements the strategied bottom/topdown.
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
				if(subjectType.isAbstractDataType() || subjectType.isConstructorType()){
					result = vf.constructor(subject.getType(), args);
					
				} else {
					result = vf.node(node.getName(), args);
				}
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
	
	/*
	 * Replace an old subject by a new one as result of an insert statement.
	 */
	private TraverseResult replacement(IValue oldSubject, IValue newSubject){
		Type oldType = oldSubject.getType();
		Type newType = newSubject.getType();
	
		
	  //TODO: PDB: Should AbstractDataType not be a subtype of NodeType?
		//System.err.println("Replacing " + oldSubject + " by " + newSubject);
		/*
		if(!newType.isSubtypeOf(oldType)){
			throw new RascalTypeError("Replacing " + oldType + " by " + newType + " value");
		}
		*/
		
		return new TraverseResult(true, newSubject, true);
	}
	
	/*
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
			for(org.meta_environment.rascal.ast.Rule rule : casesOrRules.getRules()){
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
			Type type = tp.accept(te);
			rule = rule.getRule();
			if (subject.getType().isSubtypeOf(type) && 
				matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
		} else if (rule.isReplacing()) {
			Replacement repl = rule.getReplacement();
			java.util.List<Expression> conditions = repl.isConditional() ? repl.getConditions() : new java.util.LinkedList<Expression>();
			if(matchEvalAndReplace(subject, rule.getPattern(), conditions, repl.getReplacementExpression())){
				return new TraverseResult(true, subject);
			}
		} else {
			throw new RascalBug("Impossible case in a rule: " + rule);
		}
			return new TraverseResult(subject);
	}
	
	@Override
	public EvalResult visitVisitDefaultStrategy(DefaultStrategy x) {
		
		IValue subject = x.getSubject().accept(this).value;
		java.util.List<Case> cases = x.getCases();
		
		TraverseResult tr = traverse(subject, new CasesOrRules(cases), 
									/* bottomup */ true, 
									/* breaking */ false, 
									/* fixedpoint */ false);
		return normalizedResult(tr.value.getType(), tr.value);
	}
	
	@Override
	public EvalResult visitVisitGivenStrategy(GivenStrategy x) {
		
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
	public EvalResult visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		if (!left.type.comparable(right.type)) {
			throw new RascalTypeError("Arguments of unequal have incomparable types: " + left.type + " and " + right.type);
		}
		
		return result(vf.bool(compare(left, right) != 0));
	}
	
	
	private int compare(EvalResult left, EvalResult right){
		// compare must use run-time types because it is complete for all types
		// even if statically two values have type 'value' but one is an int 1
		// and the other is Real 1.0 they must be equal.

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
	
	private int compareNode(INode left, INode right){
		String leftName = left.getName().toString();
		String rightName = right.getName().toString();
		int compare = leftName.compareTo(rightName);
		
		if(compare != 0){
			return compare;
		}
		return compareList(left.iterator(), left.arity(), right.iterator(), right.arity());
	}
	
	private int compareSourceLocation(ISourceLocation leftSL, ISourceLocation rightSL){
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
	
	private int compareSet(ISet value1, ISet value2) {
		
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
	
	private int compareMap(IMap value1, IMap value2) {
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

	private int compareList(Iterator<IValue> left, int leftLen, Iterator<IValue> right, int rightLen){
		
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
			EvalResult vl = normalizedResult(leftVal.getType(), leftVal);
			EvalResult vr = normalizedResult(rightVal.getType(), rightVal);
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
	public EvalResult visitExpressionLessThan(LessThan x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) < 0));
	}
	
	@Override
	public EvalResult visitExpressionLessThanOrEq(LessThanOrEq x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) <= 0));
	}
	@Override
	public EvalResult visitExpressionGreaterThan(GreaterThan x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) > 0));
	}
	
	@Override
	public EvalResult visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) >= 0));
	}
	
	@Override
	public EvalResult visitExpressionIfThenElse(
			org.meta_environment.rascal.ast.Expression.IfThenElse x) {
		EvalResult cval = x.getCondition().accept(this);
	
		if (cval.type.isBoolType()) {
			if (cval.value.isEqual(vf.bool(true))) {
				return x.getThenExp().accept(this);
			}
		} else {
			throw new RascalTypeError("Condition has type "
					+ cval.type + " but should be bool");
		}
		return x.getElseExp().accept(this);
	}
	
	@Override
	public EvalResult visitExpressionIfDefined(IfDefined x) {
		try {
			return x.getLhs().accept(this);
		} catch (RascalUndefinedValue e) {
			EvalResult res = x.getRhs().accept(this);
			return res;
		}
	}
	
	private boolean in(org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Expression expression2){
		EvalResult left = expression.accept(this);
		EvalResult right = expression2.accept(this);
		
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
					+ left.type + ", " + right.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionIn(In x) {
		return result(vf.bool(in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public EvalResult visitExpressionNotIn(NotIn x) {
		return result(vf.bool(!in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public EvalResult visitExpressionComposition(Composition x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
	
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
				+ left.type + ", " + right.type);
	}

	private EvalResult closure(EvalResult arg, boolean reflexive) {

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
	public EvalResult visitExpressionTransitiveClosure(TransitiveClosure x) {
		return closure(x.getArgument().accept(this), false);
	}
	
	@Override
	public EvalResult visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		return closure(x.getArgument().accept(this), true);
	}
	
	// Comprehensions ----------------------------------------------------
	
	@Override
	public EvalResult visitExpressionComprehension(Comprehension x) {
		return x.getComprehension().accept(this);
	}
	
	/*
	 * SingleIValueIterator turns a single IValue into an Iterator that
	 * can be used for implementing generators.
	 */

	private class SingleIValueIterator implements Iterator<IValue> {
		private IValue value;
		private boolean firstCall;
		
		SingleIValueIterator(IValue value){
			this.value = value;
			firstCall = true;
		}

		public boolean hasNext() {
			
			return firstCall;
		}

		public IValue next() {
			if(!firstCall){
				throw new RascalBug("next called more than once");
			}
			firstCall = false;
			return value;
		}

		public void remove() {
			// TODO Auto-generated method stub
		}	
	}
	
	@Override
	public EvalResult visitGeneratorExpression(
			org.meta_environment.rascal.ast.Generator.Expression x) {
		return new GeneratorEvaluator(x, this).next();
	}
	
	class GeneratorEvaluator implements Iterator<EvalResult>{
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
			EvalResult r = patexpr.accept(ev);
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
						throw new RascalTypeError("Strategy " + strat + " not allowed in generator");
					}
				}
				iter = new INodeReader((INode) r.value, bottomup);
			} else if(r.type.isStringType()){
				iter = new SingleIValueIterator(r.value);
			} else {
				throw new RascalTypeError("Unimplemented expression type " + r.type + " in generator");
			}
		}
		
		public boolean hasNext(){
			if(isValueProducer){
				return pat.hasNext() || iter.hasNext();
			} else {
				return firstTime;
			}	
		}

		public EvalResult next(){
			if(isValueProducer){
				//System.err.println("getNext, trying pat");
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
					pat.initMatch(v, evaluator);
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
					EvalResult v = expr.accept(evaluator);
					if(v.type.isBoolType()){
						return result(tf.boolType(), vf.bool(v.value.isEqual(vf.bool(true))));
					} else {
						throw new RascalTypeError("Expression as generator should have type bool");
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
			EvalResult r1 = resultExpr1.accept(ev);
			EvalResult r2 = null;
			
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
						                   elementType1);
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
												   elementType2);
					} 
					((IMapWriter)writer).put(r1.value, r2.value);
				}	
			}
		}
		
		public EvalResult done(){
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
	
	private EvalResult evalComprehension(java.util.List<Generator> generators, 
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
	public EvalResult visitComprehensionList(org.meta_environment.rascal.ast.Comprehension.List x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.LIST, x.getResult(), null, this));
	}
	
	@Override
	public EvalResult visitComprehensionSet(
			org.meta_environment.rascal.ast.Comprehension.Set x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.SET, x.getResult(), null, this));
	}
	
	@Override
	public EvalResult visitComprehensionMap(
			org.meta_environment.rascal.ast.Comprehension.Map x) {
		return evalComprehension(
				x.getGenerators(),
				new ComprehensionCollectionWriter(collectionKind.MAP, x.getFrom(), x.getTo(), this));
	}

	@Override
	public EvalResult visitStatementFor(For x) {
		Statement body = x.getBody();
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		EvalResult result = result();
		
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
	public EvalResult visitExpressionAny(Any x) {
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
	public EvalResult visitExpressionAll(All x) {
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
	public EvalResult visitStatementSolve(Solve x) {
		java.util.ArrayList<Name> vars = new java.util.ArrayList<Name>();
		
		for(Declarator d : x.getDeclarations()){
			for(org.meta_environment.rascal.ast.Variable v : d.getVariables()){
				vars.add(v.getName());
			}
			d.accept(this);
		}
		IValue currentValue[] = new IValue[vars.size()];
		for(int i = 0; i < vars.size(); i++){
			currentValue[i] = env.getVariable(vars.get(i)).value;
		}
		
		Statement body = x.getBody();
		
		int max = 1000;
		
		Bound bound= x.getBound();
		if(bound.isDefault()){
			EvalResult res = bound.getExpression().accept(this);
			if(!res.type.isIntegerType()){
				throw new RascalTypeError("Bound in solve statement should be integer, instead of " + res.type);
			}
			max = ((IInteger)res.value).getValue();
			if(max <= 0){
				throw new RascalRunTimeError("Bound in solve statement should be positive");
			}
		}
		
		EvalResult bodyResult = null;
		
		boolean change = true;
		int iterations = 0;
		
		while (change && iterations < max){
			change = false;
			iterations++;
			bodyResult = body.accept(this);
			for(int i = 0; i < vars.size(); i++){
				EvalResult v = env.getVariable(vars.get(i));
				if(currentValue[i] == null || !v.value.isEqual(currentValue[i])){
					change = true;
					currentValue[i] = v.value;
				}
			}
		}
		return bodyResult;
	}
}
