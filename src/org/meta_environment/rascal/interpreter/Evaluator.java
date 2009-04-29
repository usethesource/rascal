
package org.meta_environment.rascal.interpreter;

import static org.meta_environment.rascal.interpreter.Utils.unescape;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.nothing;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.exceptions.UndeclaredFieldException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
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
import org.meta_environment.rascal.ast.Import;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Replacement;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Strategy;
import org.meta_environment.rascal.ast.Toplevel;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated;
import org.meta_environment.rascal.ast.Declaration.Alias;
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
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Closure;
import org.meta_environment.rascal.ast.Expression.ClosureCall;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Division;
import org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.FieldProject;
import org.meta_environment.rascal.ast.Expression.FieldUpdate;
import org.meta_environment.rascal.ast.Expression.FunctionAsValue;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.Intersection;
import org.meta_environment.rascal.ast.Expression.IsDefined;
import org.meta_environment.rascal.ast.Expression.Join;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.Lexical;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Location;
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
import org.meta_environment.rascal.ast.FunctionDeclaration.Abstract;
import org.meta_environment.rascal.ast.Header.Parameters;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.Literal.Real;
import org.meta_environment.rascal.ast.LocalVariableDeclaration.Default;
import org.meta_environment.rascal.ast.PatternAction.Arbitrary;
import org.meta_environment.rascal.ast.PatternAction.Replacing;
import org.meta_environment.rascal.ast.Statement.Assert;
import org.meta_environment.rascal.ast.Statement.AssertWithMessage;
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
import org.meta_environment.rascal.interpreter.asserts.Ambiguous;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.control_exceptions.Failure;
import org.meta_environment.rascal.interpreter.control_exceptions.Return;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.JavaFunction;
import org.meta_environment.rascal.interpreter.env.Lambda;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.env.RascalFunction;
import org.meta_environment.rascal.interpreter.env.RewriteRule;
import org.meta_environment.rascal.interpreter.load.FromResourceLoader;
import org.meta_environment.rascal.interpreter.load.IModuleLoader;
import org.meta_environment.rascal.interpreter.result.BoolResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.MissingModifierError;
import org.meta_environment.rascal.interpreter.staticErrors.ModuleNameMismatchError;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredAnnotationError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredModuleError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnguardedFailError;
import org.meta_environment.rascal.interpreter.staticErrors.UnguardedInsertError;
import org.meta_environment.rascal.interpreter.staticErrors.UnguardedReturnError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;


@SuppressWarnings("unchecked")
public class Evaluator extends NullASTVisitor<Result> {
	final IValueFactory vf;
	final TypeFactory tf = TypeFactory.getInstance();
	private final TypeEvaluator te = TypeEvaluator.getInstance();
	private final java.util.ArrayDeque<Environment> callStack;
	private final GlobalEnvironment heap;
	private final java.util.ArrayDeque<ModuleEnvironment> scopeStack;
	
	private final JavaBridge javaBridge;
//	private final boolean LAZY = false;
	private boolean importResetsInterpreter = true;
	
	enum DIRECTION  {BottomUp, TopDown};	// Parameters for traversing trees
	enum FIXEDPOINT {Yes, No};
	enum PROGRESS   {Continuing, Breaking};
	
	
	private AbstractAST currentAST; 	// used in runtime errormessages

	private Profiler profiler;
	private boolean doProfiling = false;
	
	private TypeDeclarationEvaluator typeDeclarator = new TypeDeclarationEvaluator();
	private java.util.List<IModuleLoader> loaders;
	private java.util.List<ClassLoader> classLoaders;

	public Evaluator(IValueFactory f, ASTFactory astFactory, Writer errorWriter, ModuleEnvironment scope) {
		this(f, astFactory, errorWriter, scope, new GlobalEnvironment());
	}

	public Evaluator(IValueFactory f, ASTFactory astFactory, Writer errorWriter, ModuleEnvironment scope, GlobalEnvironment heap) {
		this.vf = f;
		this.heap = heap;
		this.callStack = new ArrayDeque<Environment>();
		this.callStack.push(scope);
		this.scopeStack = new ArrayDeque<ModuleEnvironment>();
		this.scopeStack.push(scope);
		this.loaders = new LinkedList<IModuleLoader>();
		this.classLoaders = new LinkedList<ClassLoader>();
		this.javaBridge = new JavaBridge(errorWriter, classLoaders);
		
		// library
	    loaders.add(new FromResourceLoader(this.getClass(), "StandardLibrary"));
	    
	    // everything rooted at the src directory 
	    loaders.add(new FromResourceLoader(this.getClass()));
	    
	    // load Java classes from the current jar (for the standard library)
	    classLoaders.add(getClass().getClassLoader());
	    
	}
	
	/**
	 * In interactive mode this flag should be set to true, such that re-importing
	 * a module causes a re-initialization. 
	 */
	public void setImportResetsInterpreter(boolean flag) {
		this.importResetsInterpreter = flag;
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

	Environment peek() {
		return this.callStack.peek();
	}
	
	public void addModuleLoader(IModuleLoader loader) {
		// later loaders have precedence
		loaders.add(0, loader);
	}
	
	public void addClassLoader(ClassLoader loader) {
		// later loaders have precedence
		classLoaders.add(0, loader);
	}
	
	public String getStackTrace() {
		StringBuilder b = new StringBuilder();
		for (Environment env : callStack) {
			ISourceLocation loc = env.getLocation();
			String name = env.getName();
			if (name != null && loc != null) {
				URL url = loc.getURL();
				b.append(url.getAuthority() + url.getPath() + "::" + name + ":" + loc.getBeginLine() + "," + loc.getBeginColumn());
			}
		}
		return b.toString();
	}
	
	/**
	 * Evaluate a statement
	 * @param stat
	 * @return
	 */
	public IValue eval(Statement stat) {
		try {
			 if(doProfiling){
			    	profiler = new Profiler(this);
			    	profiler.start();
			    	
			    }
			currentAST = stat;
			Result<IValue> r = stat.accept(this);
	        if(r != null){
	        	if(doProfiling){
	        		profiler.pleaseStop();
	        		profiler.report();
	        	}
	        	return r.getValue();
	        } else {
	        	throw new ImplementationError("Not yet implemented: " + stat.toString());
	        }
		} catch (Return e){
			throw new UnguardedReturnError(stat);
		}
		catch (Failure e){
			throw new UnguardedFailError(stat);
		}
		catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
			throw new UnguardedInsertError(stat);
		}
	}
	
	/**
	 * Evaluate a declaration
	 * @param declaration
	 * @return
	 */
	public IValue eval(Declaration declaration) {
		currentAST = declaration;
		Result<IValue> r = declaration.accept(this);
        if(r != null){
        	return r.getValue();
        } else {
        	throw new NotYetImplemented(declaration.toString());
        }
	}
	
	/**
	 * Evaluate an import
	 * @param imp
	 * @return
	 */
	public IValue eval(org.meta_environment.rascal.ast.Import imp) {
		currentAST = imp;
		Result<IValue> r = imp.accept(this);
        if(r != null){
        	return r.getValue();
        } else {
        	throw new ImplementationError("Not yet implemented: " + imp.getTree());
        }
	}

	/* First a number of general utility methods */
	
	/*
	 * Return an evaluation result that is already in normal form,
	 * i.e., all potential rules have already been applied to it.
	 */
	
	Result<IValue> normalizedResult(Type t, IValue v){
		Map<Type, Type> bindings = peek().getTypeBindings();
		Type instance;
		
		if (bindings.size() > 0) {
		    instance = t.instantiate(peek().getStore(), bindings);
		}
		else {
			instance = t;
		}
		
		if (v != null) {
			checkType(v.getType(), instance);
		}
		return makeResult(instance, v, getCurrentAST());
	}
	
	
	private IValue applyRules(IValue v) {
		
		//System.err.println("applyRules(" + v + ")");
		// we search using the run-time type of a value
		Type typeToSearchFor = v.getType();
		if (typeToSearchFor.isAbstractDataType()) {
			typeToSearchFor = ((IConstructor) v).getConstructorType();
		}

		java.util.List<RewriteRule> rules = heap.getRules(typeToSearchFor);
		if(rules.isEmpty()){
			return v;
		}
			
		if (rules.size() == 1) {
			System.err.println("hier");
		}
		TraverseResult tr = traverseTop(v, new CasesOrRules(rules));
		/* innermost is achieved by repeated applications of applyRules
		 * when intermediate results are produced.
		 */
		return tr.value;
	}
/*
	private void pop() {
		System.err.println("pop");
		callStack.pop();
	}

	private void push() {
		System.err.println("push");
		callStack.push(new Environment(peek()));
	}
	*/
	
	Environment pushEnv() {
		Environment env = new Environment(peek());
		callStack.push(env);
		return env;
	}
	
	void popUntil(Environment env){
		Environment previousEnv;
		if(!callStack.contains(env))
			throw new ImplementationError("popUntil");
		do {
			previousEnv = callStack.pop();	
		} while (previousEnv != env);	
	}
	
	
	private void checkType(Type given, Type expected) {
		if (expected == org.meta_environment.rascal.interpreter.env.Lambda.getClosureType()) {
			return;
		}
		if (!given.isSubtypeOf(expected)){
			throw new UnexpectedTypeError(expected, given, getCurrentAST());
		}
	}
	
	private boolean mayOccurIn(Type small, Type large) {
		return mayOccurIn(small, large, new HashSet<Type>());
	}
		
	private boolean mayOccurIn(Type small, Type large, java.util.Set<Type> seen){
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
			for(Type alt : peek().lookupAlternatives(large)){				
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
	
	boolean mayMatch(Type small, Type large){
		//System.err.println("mayMatch: " + small + " " + large);
		if(small.equivalent(large))
			return true;
	
		if(small.isVoidType() || large.isVoidType())
				return false;
	
		if(small.isSubtypeOf(large) || large.isSubtypeOf(small))
				return true;
	
		
		if(small.isListType() && large.isListType() || 
		   small.isSetType() && large.isSetType())
			return mayMatch(small.getElementType(),large.getElementType());
		if(small.isMapType() && large.isMapType())
			return mayMatch(small.getKeyType(), large.getKeyType()) &&
			        mayMatch(small.getKeyType(), large.getValueType());
		if(small.isTupleType() && large.isTupleType()){
			if(small.getArity() != large.getArity())
					return false;
			for(int i = 0; i < large.getArity(); i++){
				if(mayMatch(small.getFieldType(i), large.getFieldType(i)))
						return true;
			}
			return false;
		}
		if(small.isConstructorType() && large.isConstructorType()){
			if(small.getName().equals(large.getName()))
				return false;
			for(int i = 0; i < large.getArity(); i++){
				if(mayMatch(small.getFieldType(i), large.getFieldType(i)))
						return true;
			}
			return false;
		}
		if(small.isConstructorType() && large.isAbstractDataType())
			return small.getAbstractDataType().equivalent(large);
		if(small.isAbstractDataType() && large.isConstructorType())
			return small.equivalent(large.getAbstractDataType());
		
		
		return false;
		
	}
	
	// Ambiguity ...................................................
	
	@Override
	public Result<IValue> visitExpressionAmbiguity(Ambiguity x) {
		throw new Ambiguous(x.toString());
	}
	
	@Override
	public Result<IValue> visitStatementAmbiguity(
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
		throw new Ambiguous(x.toString());
	}
	
	// Modules -------------------------------------------------------------
	
	@Override
	public Result<IValue> visitImportDefault(
			org.meta_environment.rascal.ast.Import.Default x) {
		// TODO support for full complexity of import declarations
		String name = x.getModule().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		if (!heap.existsModule(name)) {
			evalModule(x, name);
		}
		else {
			if (importResetsInterpreter && scopeStack.size() == 1 && callStack.size() == 1) {
				reloadAll(x);
			}
		}
		
		scopeStack.peek().addImport(name, heap.getModule(name, x));
		return ResultFactory.nothing();
	}

	private void evalModule(AbstractAST x,
			String name) {
		Module module = loadModule(name, x);
		if (!getModuleName(module).equals(name)) {
			throw new ModuleNameMismatchError(getModuleName(module), name, x);
		}
		module.accept(this);
	}


	private void reloadAll(AbstractAST cause) {
		heap.clear();
		
		java.util.Set<String> topModules = scopeStack.getFirst().getImports();
		for (String mod : topModules) {
			evalModule(cause, mod);
			scopeStack.peek().addImport(mod, heap.getModule(mod, cause));
		}
	}
	

	private Module loadModule(String name, AbstractAST ast) {
		for (IModuleLoader loader : loaders) {
			try {
				return loader.loadModule(name);
			}
			catch (IOException e) {
				// this happens regularly
			}
		}
		
		throw RuntimeExceptionFactory.moduleNotFound(vf.string(name), ast, getStackTrace());
	}
	
	@Override 
	public Result<IValue> visitModuleDefault(
			org.meta_environment.rascal.ast.Module.Default x) {
		String name = getModuleName(x);

		if (!heap.existsModule(name)) {
			ModuleEnvironment env = new ModuleEnvironment(name);
			scopeStack.push(env);
			callStack.push(env); // such that declarations end up in the module scope
			
			try {
				x.getHeader().accept(this);

				java.util.List<Toplevel> decls = x.getBody().getToplevels();
				typeDeclarator.evaluateDeclarations(decls, peek());
				
				for (Toplevel l : decls) {
					l.accept(this);
				}
				
				// only after everything was successful add the module
				heap.addModule(env);
			}
			finally {
				scopeStack.pop();
				callStack.pop();
			}
		}
		return ResultFactory.nothing();
	}

	private String getModuleName(
			Module module) {
		String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	@Override
	public Result<IValue> visitHeaderDefault(
			org.meta_environment.rascal.ast.Header.Default x) {
		visitImports(x.getImports());
		return ResultFactory.nothing();
	}
	
	@Override
	public Result<IValue> visitDeclarationAlias(Alias x) {
		typeDeclarator.declareAlias(x, peek());
		return ResultFactory.nothing();
	}
	
	@Override
	public Result<IValue> visitDeclarationData(Data x) {
		typeDeclarator.declareConstructor(x, peek());
		return ResultFactory.nothing();
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
	public Result<IValue> visitToplevelDefaultVisibility(DefaultVisibility x) {
		Result<IValue> r = x.getDeclaration().accept(this);
		r.setPublic(false);
		return r;
	}

	@Override
	public Result<IValue> visitToplevelGivenVisibility(GivenVisibility x) {
		Result<IValue> r = x.getDeclaration().accept(this);
		r.setPublic(x.getVisibility().isPublic());
		return r;
	}
	
	@Override
	public Result<IValue> visitDeclarationFunction(Function x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	@Override
	public Result<IValue> visitDeclarationVariable(Variable x) {
		Type declaredType = te.eval(x.getType(), scopeStack.peek());
		Result<IValue> r = nothing();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			if(peek().getLocalVariable(var.getName()) != null){
				throw new RedeclaredVariableError(var.getName().toString(), var);
			}
			if (var.isUnInitialized()) {  
				throw new UninitializedVariableError(var.toString(), var);
			} else {
				Result<IValue> v = var.getInitial().accept(this);
				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(peek().getStore(), bindings);
					r = makeResult(declaredType, v.getValue(), getCurrentAST());
					scopeStack.peek().storeInnermostVariable(var.getName(), r);
				} else {
					throw new UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
		}
		
		return r;
	}

	@Override
	public Result<IValue> visitDeclarationAnnotation(Annotation x) {
		Type annoType = te.eval(x.getAnnoType(), scopeStack.peek());
		String name = x.getName().toString();

		Type onType = te.eval(x.getOnType(), scopeStack.peek());
		scopeStack.peek().declareAnnotation(onType, name, annoType);	

		return ResultFactory.nothing();
	}
	
	
	private Type evalType(org.meta_environment.rascal.ast.Type type) {
		return te.eval(type, peek());
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
	public Result<IValue> visitPatternActionArbitrary(Arbitrary x) {
		MatchPattern pv = x.getPattern().accept(makePatternEvaluator());
		Type pt = pv.getType(peek());
		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
				throw new UnexpectedTypeError(tf.nodeType(), pt, x);
		heap.storeRule(pv.getType(scopeStack.peek()), x, scopeStack.peek());
		return ResultFactory.nothing();
	}

	private AbstractPatternEvaluator makePatternEvaluator() {
		return new AbstractPatternEvaluator(vf, peek(), peek(), this);
	}
	
	@Override
	public Result<IValue> visitPatternActionReplacing(Replacing x) {
		MatchPattern pv = x.getPattern().accept(makePatternEvaluator());
		Type pt = pv.getType(peek());
		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
				throw new UnexpectedTypeError(tf.nodeType(), pt, x);
		heap.storeRule(pv.getType(scopeStack.peek()), x, scopeStack.peek());
		return ResultFactory.nothing();
	}
	
	/*@Override
	public Result<IValue> visitPatternActionGuarded(Guarded x) {
		//TODO adapt to new scheme
		Result<IValue> result = x.getRule().getPattern().getPattern().accept(this);
		Type expected = evalType(x.getType());
		Type got = result.getType();
		if (!got.isSubtypeOf(expected)) {
			throw new UnexpectedTypeError(expected, got, x);
		}
		return x.getRule().accept(this);
	}
	*/
	
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
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type declaredType = evalType(x.getType());
		Result<IValue> r = ResultFactory.nothing();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			String varAsString = var.getName().toString();
			if(peek().getLocalVariable(varAsString) != null ||
					peek().isRoot() && peek().getInnermostVariable(var.getName()) != null){
				throw new RedeclaredVariableError(varAsString, var);
			}
			if (var.isUnInitialized()) {  // variable declaration without initialization
				r = ResultFactory.makeResult(declaredType, null, var);
				peek().storeInnermostVariable(var.getName(), r);
			} else {                     // variable declaration with initialization
				Result<IValue> v = var.getInitial().accept(this);
				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(peek().getStore(), bindings);
					// Was: r = makeResult(declaredType, applyRules(v.getValue()));
					r = makeResult(declaredType, v.getValue(), var);
					peek().storeInnermostVariable(var.getName(), r);
				} else {
					throw new UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
		}
		return r;
	}
	
	// Function calls and node constructors
	
	@Override
	public Result<IValue> visitClosureAsFunctionEvaluated(Evaluated x) {
		// TODOOOOOOOO!!!!
		Expression expr = x.getExpression();
		
		if (expr.isQualifiedName()) {
			
		}
		
		return nothing();
	}
	
	@Override
	public Result<IValue> visitExpressionClosureCall(ClosureCall x) {
		Result<IValue> func = x.getClosure().getExpression().accept(this);
		java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();

		IValue[] actuals = new IValue[args.size()];
		Type[] types = new Type[args.size()];

		for (int i = 0; i < args.size(); i++) {
			Result<IValue> resultElem = args.get(i).accept(this);
			types[i] = resultElem.getType();
			actuals[i] = resultElem.getValue();
		}

		Type actualTypes = tf.tupleType(types);

		if (func.getType() == Lambda.getClosureType()) {
			Lambda lambda = (Lambda) func.getValue();
			Environment newEnv = pushCallFrame(lambda.getEnv(), x.getLocation(), lambda.getName()); 
			try {
				return lambda.call(actuals, actualTypes, peek());
			}
			finally {
				popUntil(newEnv);
			}
		}
		else {
			throw new ImplementationError("Lambda's should have the closure type");
		}
	}
	
	@Override
	public Result<IValue> visitExpressionCallOrTree(CallOrTree x) {
		 java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();
		 QualifiedName name = x.getQualifiedName();
//System.err.println("CallOrTree: " + name.toString());
		 IValue[] actuals = new IValue[args.size()];
		 Type[] types = new Type[args.size()];

		 for (int i = 0; i < args.size(); i++) {
			 Result<IValue> resultElem = args.get(i).accept(this);
//			 System.out.println("Arg:" + i + ": " + resultElem);
			 types[i] = resultElem.getType();
			 actuals[i] = resultElem.getValue();
		 }
		 
		 Type signature = tf.tupleType(types);
		 
		 if (isTreeConstructorName(name, signature)) {
//			 System.err.println("constructor:" + name);
			 return constructTree(name, actuals, signature);
		 }
		 else {
//			 System.err.println("function: " + name);
			 return call(name, actuals, signature);
		 }
	}
	
	private Result<IValue> call(QualifiedName name, IValue[] actuals, Type actualTypes) {
		String moduleName = Names.moduleName(name);
		Environment env;
		
		if (moduleName == null) {
			env = peek();
		}
		else {
			env = peek().getImport(moduleName);
			if (env == null) {
				throw new UndeclaredModuleError(moduleName, name);
			}
		}
		Lambda func = env.getFunction(Names.name(Names.lastName(name)), actualTypes, name);
		if (func != null) {
			Environment newEnv = pushCallFrame(func.getEnv(), name.getLocation(), func.getName());
			try {
				return func.call(actuals, actualTypes, peek());
			}
			finally {
				popUntil(newEnv);
			}
		}
		undefinedFunctionException(name, actualTypes);
		return null;
	}


	private Environment pushCallFrame(Environment env, ISourceLocation loc, String name) {
		Environment newEnv = new Environment(env, loc, name);
		callStack.push(newEnv);
		return newEnv;
	}

	private void undefinedFunctionException(QualifiedName name, Type actualTypes) {
		StringBuffer sb = new StringBuffer();
		String sep = "";
		for(int i = 0; i < actualTypes.getArity(); i++){
			sb.append(sep);
			sep = ", ";
			sb.append(actualTypes.getFieldType(i).toString());
		}
		
		throw new UndeclaredFunctionError(name + "(" +  sb.toString() + ")", name);
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
	 * TODO: We now first build the tree and then apply rules to it. Change this so
	 * that we can avoid building the tree at all.
	 */
	private Result<IValue> constructTree(QualifiedName functionName, IValue[] actuals, Type signature) {
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
			  return makeResult(tf.nodeType(), applyRules(vf.node(cons, actuals)), getCurrentAST());
			}
		}
		
		candidate = peek().getConstructor(cons, signature);
		if (candidate != null) {
			Map<Type,Type> localBindings = new HashMap<Type,Type>();
			candidate.getFieldTypes().match(tf.tupleType(actuals), localBindings);
			
			return makeResult(candidate.getAbstractDataType().instantiate(new TypeStore(), localBindings), 
					applyRules(candidate.make(vf, actuals)), getCurrentAST());
		}
		
		return makeResult(tf.nodeType(), applyRules(vf.node(cons, actuals)), getCurrentAST());
	}
	
	@Override
	public Result<IValue> visitExpressionFunctionAsValue(FunctionAsValue x) {
		return x.getFunction().accept(this);
	}
	
	@Override
	public Result visitFunctionAsValueDefault(
			org.meta_environment.rascal.ast.FunctionAsValue.Default x) {
		Name name = x.getName();
		
		//TODO is this a bug, what if name was overloaded?
		//TODO add support for typed function names
		Lambda func = peek().getFunction(Names.name(name), tf.voidType(), x);
		
		if (func == null) {
			throw new UndeclaredFunctionError(Names.name(name), x);
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
	public Result<IValue> visitFunctionBodyDefault(
			org.meta_environment.rascal.ast.FunctionBody.Default x) {
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
			throw RuntimeExceptionFactory.assertionFailed(getCurrentAST(), getStackTrace());
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
			String str = x.getMessage().toString();
			IString msg = vf.string(unescape(str, x, peek()));
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
		setCurrentAST(x);
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result<IValue> visitStatementFunctionDeclaration(
			org.meta_environment.rascal.ast.Statement.FunctionDeclaration x) {
		return x.getFunctionDeclaration().accept(this);
	}
	

	@Override
	public Result<IValue> visitExpressionSubscript(Subscript x) {		
		Result<IValue> expr = x.getExpression().accept(this);
		int nSubs = x.getSubscripts().size();
		Result<?> subscripts[] = new Result<?>[nSubs];
		for (int i = 0; i < nSubs; i++) {
			subscripts[i] = x.getSubscripts().get(i).accept(this);
		}
		return expr.subscript(subscripts, x);
	}

	@Override
	public Result<IValue> visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		Result<IValue> expr = x.getExpression().accept(this);
		String field = x.getField().toString();
		return expr.fieldAccess(field, peek().getStore(), x);
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
	public Result<IValue> visitExpressionFieldProject(FieldProject x) {
		// TODO: move to result classes
		Result<IValue>  base = x.getExpression().accept(this);
		
		java.util.List<Field> fields = x.getFields();
		int nFields = fields.size();
		int selectedFields[] = new int[nFields];
		
		if(base.getType().isTupleType()){
			Type fieldTypes[] = new Type[nFields];
			
			for(int i = 0 ; i < nFields; i++){
				Field f = fields.get(i);
				if(f.isIndex()){
					selectedFields[i] = ((IInteger) f.getFieldIndex().accept(this).getValue()).intValue();
				} else {
					String fieldName = f.getFieldName().toString();
					try {
						selectedFields[i] = base.getType().getFieldIndex(fieldName);
					} catch (UndeclaredFieldException e){
						throw new UndeclaredFieldError(fieldName, base.getType(), x);
					}
				}
				
				if (selectedFields[i] < 0 || selectedFields[i] > base.getType().getArity()) {
					throw RuntimeExceptionFactory.indexOutOfBounds(vf.integer(i), getCurrentAST(), getStackTrace());
				}
				fieldTypes[i] = base.getType().getFieldType(selectedFields[i]);
			}
			
			if(duplicateIndices(selectedFields)){
				// TODO: what does it matter if there are duplicate indices???
				throw new ImplementationError("Duplicate fields in projection");
			}
			Type resultType = nFields == 1 ? fieldTypes[0] : tf.tupleType(fieldTypes);
			
			// Was: return makeResult(resultType, applyRules(((ITuple)base.getValue()).select(selectedFields)));
			return makeResult(resultType, ((ITuple)base.getValue()).select(selectedFields), getCurrentAST());
		}
		if(base.getType().isRelationType()){
			
			Type fieldTypes[] = new Type[nFields];
			
			for(int i = 0 ; i < nFields; i++){
				Field f = fields.get(i);
				if(f.isIndex()){
					selectedFields[i] = ((IInteger) f.getFieldIndex().accept(this).getValue()).intValue();
				} else {
					String fieldName = f.getFieldName().toString();
					try {
						selectedFields[i] = base.getType().getFieldIndex(fieldName);
					} catch (Exception e){
						throw new UndeclaredFieldError(fieldName, base.getType(), x);
					}
				}
				if(selectedFields[i] < 0 || selectedFields[i] > base.getType().getArity()) {
					throw RuntimeExceptionFactory.indexOutOfBounds(vf.integer(i), getCurrentAST(), getStackTrace());
				}
				fieldTypes[i] = base.getType().getFieldType(selectedFields[i]);
			}
			if(duplicateIndices(selectedFields)){
				// TODO: what does it matter if there are duplicate indices? Duplicate
				// field names may be a problem, but not this.
				throw new ImplementationError("Duplicate fields in projection");
			}
			Type resultType = nFields == 1 ? tf.setType(fieldTypes[0]) : tf.relType(fieldTypes);
			
			//return makeResult(resultType, applyRules(((IRelation)base.getValue()).select(selectedFields)));	
			return makeResult(resultType, ((IRelation)base.getValue()).select(selectedFields), getCurrentAST());	
		}
		
		throw new UnsupportedOperationError("projection", base.getType(), x);
	}
	
	@Override
	public Result<IValue> visitStatementEmptyStatement(EmptyStatement x) {
		return ResultFactory.nothing();
	}
	
	@Override
	public Result<IValue> visitStatementFail(Fail x) {
		if (x.getFail().isWithLabel()) {
			throw new Failure(x.getFail().getLabel().toString());
		}
		else {
		  throw new Failure();
		}
	}
	
	@Override
	public Result<IValue> visitStatementReturn(
			org.meta_environment.rascal.ast.Statement.Return x) {
		org.meta_environment.rascal.ast.Return r = x.getRet();
		if (r.isWithExpression()) {
		  throw new Return(x.getRet().getExpression().accept(this));
		}
		else {
			throw new Return(nothing());
		}
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
		throw new org.meta_environment.rascal.interpreter.control_exceptions.Throw(x.getExpression().accept(this).getValue(), getCurrentAST(), getStackTrace());
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
		} catch (org.meta_environment.rascal.interpreter.control_exceptions.Throw e){
			IValue eValue = e.getException();

			for (Catch c : handlers){
				if(c.isDefault()){
					res = c.getBody().accept(this);
					break;
				} 
				Environment newEnv = pushEnv();
				try {
					if(matchAndEval(eValue, c.getPattern(), c.getBody())){
						break;
					}
				} 
				finally {
					popUntil(newEnv);
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
			org.meta_environment.rascal.ast.Statement.Visit x) {
		return x.getVisit().accept(this);
	}
	
	@Override
	public Result<IValue> visitStatementInsert(Insert x) {
		throw new org.meta_environment.rascal.interpreter.control_exceptions.Insert(x.getExpression().accept(this));
	}
	
	@Override
	public Result<IValue> visitStatementAssignment(Assignment x) {
		Result<IValue> right = x.getExpression().accept(this);
		return x.getAssignable().accept(new AssignableEvaluator(peek(), x.getOperator(), right, this));
	}
	
	@Override
	public Result<IValue> visitStatementBlock(Block x) {
		Result<IValue> r = nothing();
		Environment newEnv = pushEnv();
		try {
			for (Statement stat : x.getStatements()) {
				setCurrentAST(stat);
				r = stat.accept(this);
			}
		}
		finally {
			popUntil(newEnv);
		}
		return r;
	}
  
	@Override
	public Result<IValue> visitAssignableVariable(
			org.meta_environment.rascal.ast.Assignable.Variable x) {
		return peek().getVariable(x.getQualifiedName(),x.getQualifiedName().toString());
	}
	
	@Override
	public Result<IValue> visitAssignableFieldAccess(FieldAccess x) {
		Result<IValue> receiver = x.getReceiver().accept(this);
		String label = x.getField().toString();
	
		if (receiver.getType().isTupleType()) {
			IValue result = ((ITuple) receiver.getValue()).get(label);
			Type type = ((ITuple) receiver.getValue()).getType().getFieldType(label);
			return makeResult(type, result, x);
		}
		else if (receiver.getType().isConstructorType() || receiver.getType().isAbstractDataType()) {
			IConstructor cons = (IConstructor) receiver.getValue();
			Type node = cons.getConstructorType();
			
			if (!receiver.getType().hasField(label)) {
				throw new UndeclaredFieldError(label, receiver.getType(), x);
			}
			
			if (!node.hasField(label)) {
				throw RuntimeExceptionFactory.noSuchField(label, x,getStackTrace());
			}
			
			int index = node.getFieldIndex(label);
			return makeResult(node.getFieldType(index), cons.get(index), x);
		}
		else {
			throw new UndeclaredFieldError(label, receiver.getType(), x);
		}
	}
	
	@Override
	public Result<IValue> visitAssignableAnnotation(
			org.meta_environment.rascal.ast.Assignable.Annotation x) {
		Result<IValue> receiver = x.getReceiver().accept(this);
		String label = x.getAnnotation().toString();
		
		if (!callStack.peek().declaresAnnotation(receiver.getType(), label)) {
			throw new UndeclaredAnnotationError(label, receiver.getType(), x);
		}
		
		Type type = callStack.peek().getAnnotationType(receiver.getType(), label);
		IValue value = ((IConstructor) receiver.getValue()).getAnnotation(label);
		
		return makeResult(type, value, x);
	}
	
	@Override
	public Result<IValue> visitAssignableConstructor(Constructor x) {
		throw new ImplementationError("Constructor assignable does not represent a value");
	}
	
	@Override
	public Result<IValue> visitAssignableIfDefinedOrDefault(
			org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault x) {
		throw new ImplementationError("ifdefined assignable does not represent a value");
	}
	
	@Override
	public Result<IValue> visitAssignableSubscript(
			org.meta_environment.rascal.ast.Assignable.Subscript x) {
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
			
			if (subscript.getType().isSubtypeOf(keyType)) {
				IValue result = ((IMap) receiver.getValue()).get(subscript.getValue());
				Type type = receiver.getType().getValueType();
				return makeResult(type, result, x);
			}
			
			throw new UnexpectedTypeError(keyType, subscript.getType(), x);
		}
		// TODO implement other subscripts
		throw new UnsupportedOperationError("subscript", receiver.getType(), x);
	}
	
	@Override
	public Result<IValue> visitAssignableTuple(
			org.meta_environment.rascal.ast.Assignable.Tuple x) {
		throw new ImplementationError("Tuple in assignable does not represent a value:" + x);
	}
	
	@Override
	public Result<IValue> visitAssignableAmbiguity(
			org.meta_environment.rascal.ast.Assignable.Ambiguity x) {
		throw new Ambiguous(x.toString());
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
				throw new MissingModifierError("java", x);
			}
			
			lambda = new RascalFunction(this, x, varArgs, peek());
		}
		
		String name = Names.name(x.getSignature().getName());
		peek().storeFunction(name, lambda);
		
		return lambda;
	}
	
	@Override
	public Result visitFunctionDeclarationAbstract(Abstract x) {
		Lambda lambda;
		boolean varArgs = x.getSignature().getParameters().isVarArgs();
		
		if (hasJavaModifier(x)) {
			lambda = new org.meta_environment.rascal.interpreter.env.JavaMethod(this, x, varArgs, peek(), javaBridge);
		}
		else {
			throw new MissingModifierError("java", x);
		}
		
		String name = Names.name(x.getSignature().getName());
		peek().storeFunction(name, lambda);
		
		return lambda;
	}
	
	@Override
	public Result<IValue> visitStatementIfThenElse(IfThenElse x) {
		elseBranch: 
			do {
				Environment newEnv = pushEnv(); // For the benefit of variables bound in the condition
				try {
					for (org.meta_environment.rascal.ast.Expression expr : x.getConditions()) {
						Result<IValue> cval = expr.accept(this);
						if (!cval.getType().isBoolType()) {
							throw new UnexpectedTypeError(tf.boolType(),
									cval.getType(), x);
						}
						if (cval.getValue().isEqual(vf.bool(false))) {
							break elseBranch;
						}
						// condition is true: continue
					}
					return x.getThenStatement().accept(this);
				} finally {
					popUntil(newEnv);	// Remove any bindings due to condition evaluation.
				}
			} 
			while (false);
		return x.getElseStatement().accept(this);
	}

	
	
	@Override
	public Result<IValue> visitStatementIfThen(IfThen x) {
		Environment newEnv = pushEnv(); // For the benefit of variables bound in the condition
		try {
			for (org.meta_environment.rascal.ast.Expression expr : x.getConditions()) {
				Result<IValue> cval = expr.accept(this);
				if (!cval.getType().isBoolType()) {
					throw new UnexpectedTypeError(tf.boolType(),cval.getType(), x);
				}
				if (cval.getValue().isEqual(vf.bool(false))) {
					return ResultFactory.nothing();
				}
			}
			return x.getThenStatement().accept(this);
		}
		finally {
			popUntil(newEnv);
		}
	}
	
	@Override
	public Result<IValue> visitStatementWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		Result<IValue> statVal = nothing();
		
		do {
			Environment newEnv = pushEnv();
			try {
				Result<IValue> cval = expr.accept(this);
				if (!cval.getType().isBoolType()) {
					throw new UnexpectedTypeError(tf.boolType(),cval.getType(), x);
				}
				if (cval.getValue().isEqual(vf.bool(false))) {
					return statVal;
				}
				statVal = x.getBody().accept(this);
			}
			finally {
				popUntil(newEnv);
			}
		} while (true);
	}
		
	@Override
	public Result<IValue> visitStatementDoWhile(DoWhile x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		
		Environment newEnv = pushEnv();
		try {
			do {
			
				Result<IValue> result = x.getBody().accept(this);
				Result<IValue> cval = expr.accept(this);
				if (!cval.getType().isBoolType()) {
					throw new UnexpectedTypeError(tf.boolType(),cval.getType(), x);
				}
				if (cval.getValue().isEqual(vf.bool(false))) {
					return result;
				}
			} while (true);
		}
		finally {
			popUntil(newEnv);
		}
	}
	
    @Override
    public Result<IValue> visitExpressionMatch(Match x) {
    	return new MatchEvaluator(x.getPattern(), x.getExpression(), true, peek(), this).next();
    }
    
    @Override
    public Result<IValue> visitExpressionNoMatch(NoMatch x) {
    	// Make sure that no bindings escape
    	Environment newEnv = pushEnv();
    	Result res =  new MatchEvaluator(x.getPattern(), x.getExpression(), false, newEnv, this).next();
    	popUntil(newEnv);
    	return res;
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
				// TODO how can this happen?
				throw new ImplementationError("Pattern expected instead of " + pat);
			}
		}
    }

	// Expressions -----------------------------------------------------------

	@Override
	public Result<IValue> visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public Result<IValue> visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}

	@Override
	public Result<IValue> visitLiteralReal(Real x) {
		String str = x.getRealLiteral().toString();
		return makeResult(tf.realType(), vf.real(str), x);
	}

	@Override
	public Result<IValue> visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		return makeResult(tf.boolType(), vf.bool(str.equals("true")), x);
	}

	@Override
	public Result<IValue> visitLiteralString(
			org.meta_environment.rascal.ast.Literal.String x) {
		String str = x.getStringLiteral().toString();
		return makeResult(tf.stringType(), vf.string(unescape(str, x, peek())), x);
	}

	

	@Override
	public Result<IValue> visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = x.getDecimal().toString();
		return makeResult(tf.integerType(), vf.integer(str), x);
	}
	
	@Override
	public Result<IValue> visitExpressionQualifiedName(
			org.meta_environment.rascal.ast.Expression.QualifiedName x) {
		if (isTreeConstructorName(x.getQualifiedName(), tf.tupleEmpty())) {
			return constructTree(x.getQualifiedName(), new IValue[0], tf.tupleType(new Type[0]));
		}
		else {
			Result<IValue> result = peek().getVariable(x.getQualifiedName());

			if (result != null && result.getValue() != null) {
				return result;
			} else {
				throw new UninitializedVariableError(x.getQualifiedName().toString(), x);
			}
		}
	}
	
	@Override
	public Result<IValue> visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType =  tf.voidType();;
		java.util.List<IValue> results = new ArrayList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			Result<IValue> resultElem = expr.accept(this);
			
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
		Type resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		// Was: return makeResult(resultType, applyRules(w.done()));
		return makeResult(resultType, w.done(), x);
	}

	@Override
	public Result<IValue> visitExpressionSet(Set x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		
		Type elementType = tf.voidType();
		java.util.List<IValue> results = new ArrayList<IValue>();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			Result<IValue> resultElem = expr.accept(this);
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
		return makeResult(resultType, w.done(), x);
	}

	@Override
	public Result<IValue> visitExpressionMap(
			org.meta_environment.rascal.ast.Expression.Map x) {

		java.util.List<org.meta_environment.rascal.ast.Mapping> mappings = x
				.getMappings();
		Map<IValue,IValue> result = new HashMap<IValue,IValue>();
		Type keyType = tf.voidType();
		Type valueType = tf.voidType();

		for (org.meta_environment.rascal.ast.Mapping mapping : mappings) {
			Result<IValue> keyResult = mapping.getFrom().accept(this);
			Result<IValue> valueResult = mapping.getTo().accept(this);
			
			keyType = keyType.lub(keyResult.getType());
			valueType = valueType.lub(valueResult.getType());
			
			result.put(keyResult.getValue(), valueResult.getValue());
		}
		
		Type type = tf.mapType(keyType, valueType);
		IMapWriter w = type.writer(vf);
		w.putAll(result);
		
		//return makeResult(type, applyRules(w.done()));
		return makeResult(type, w.done(), x);
	}
	
	@Override
	public Result visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		return new Lambda(x, this, tf.voidType(), "", tf.tupleEmpty(), false, x.getStatements(), peek());
	}

	@Override
	public Result<IValue> visitExpressionTuple(Tuple x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();

		IValue[] values = new IValue[elements.size()];
		Type[] types = new Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			Result<IValue> resultElem = elements.get(i).accept(this);
			types[i] = resultElem.getType();
			values[i] = resultElem.getValue();
		}

		//return makeResult(tf.tupleType(types), applyRules(vf.tuple(values)));
		return makeResult(tf.tupleType(types), vf.tuple(values), x);
	}
	
	@Override
	public Result<IValue> visitExpressionGetAnnotation(
			org.meta_environment.rascal.ast.Expression.GetAnnotation x) {
		  Result<IValue> base = x.getExpression().accept(this);
		String annoName = x.getName().toString();
		return base.getAnnotation(annoName, callStack.peek(), x);
	}
	
	@Override
	public Result<IValue> visitExpressionSetAnnotation(
			org.meta_environment.rascal.ast.Expression.SetAnnotation x) {
		Result<IValue> base = x.getExpression().accept(this);
		String annoName = x.getName().toString();
		Result<IValue> anno = x.getValue().accept(this);
		return base.setAnnotation(annoName, anno, callStack.peek(), x);
	}
	
    @Override
	public Result<IValue> visitExpressionAddition(Addition x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.add(right, x);

    }
    
	@Override
	public Result<IValue> visitExpressionSubtraction(Subtraction x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.subtract(right, x);

	}
	
	@Override
	public Result<IValue> visitExpressionNegative(Negative x) {
		Result<IValue> arg = x.getArgument().accept(this);
		return arg.negative(x);
	}
	
	@Override
	public Result<IValue> visitExpressionProduct(Product x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.multiply(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionJoin(Join x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.join(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionDivision(Division x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.divide(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionModulo(Modulo x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.modulo(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result<IValue> visitExpressionIntersection(Intersection x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.intersect(right, x);
	}

	@Override
	public Result<IValue> visitExpressionOr(Or x) {
		return new OrEvaluator(x, this).next();
	}

	@Override
	public Result<IValue> visitExpressionAnd(And x) {
		return new AndEvaluator(x, this).next();
	}

	@Override
	public Result<IValue> visitExpressionNegation(Negation x) {
		return new NegationEvaluator(x, this).next();
	}
	
	@Override
	public Result<IValue> visitExpressionImplication(Implication x) {
		return new ImplicationEvaluator(x, this).next();
	}
	
	@Override
	public Result<IValue> visitExpressionEquivalence(Equivalence x) {
		return new EquivalenceEvaluator(x, this).next();
	}

	@Override
	public Result<IValue> visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.equals(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionOperatorAsValue(OperatorAsValue x) {
		// TODO
		throw new NotYetImplemented(x);
	}
	
	@Override
	public Result<IValue> visitExpressionLocation(Location x){
		
		String urlText = x.getUrl().toString();
		
		Result<IValue> length = x.getLength().accept(this);
		int iLength = ((IInteger) length.getValue()).intValue();
		
		Result<IValue> offset = x.getOffset().accept(this);	
		int iOffset = ((IInteger) offset.getValue()).intValue();
		
		Result<IValue> beginLine = x.getBeginLine().accept(this);
		int iBeginLine = ((IInteger) beginLine.getValue()).intValue();
		
		Result<IValue> endLine = x.getEndLine().accept(this);
		int iEndLine = ((IInteger) endLine.getValue()).intValue();
		
		Result<IValue> beginColumn = x.getBeginColumn().accept(this);
		int iBeginColumn = ((IInteger) beginColumn.getValue()).intValue();
		
		Result<IValue> endColumn = x.getEndColumn().accept(this);
		int iEndColumn = ((IInteger) endColumn.getValue()).intValue();

		try {
			URL url = new URL(urlText);
			ISourceLocation r = vf.sourceLocation(url, iOffset, iLength, iBeginLine, iEndLine, iBeginColumn, iEndColumn);
			return makeResult(tf.sourceLocationType(), r, x);
		} catch (MalformedURLException e){
			throw new SyntaxError("location (malformed URL)", x.getLocation());
		}
	}
	
	@Override
	public Result visitExpressionClosure(Closure x) {
		Type formals = te.eval(x.getParameters(), peek());
		Type returnType = evalType(x.getType());
		return new Lambda(x, this, returnType, "", formals, x.getParameters().isVarArgs(), x.getStatements(), peek());
	}

	@Override
	public Result visitExpressionVoidClosure(VoidClosure x) {
		Type formals = te.eval(x.getParameters(), peek());
		return new Lambda(x, this, tf.voidType(), "", formals, x.getParameters().isVarArgs(), x.getStatements(), peek());
	}
	
	@Override
	public Result<IValue> visitExpressionFieldUpdate(FieldUpdate x) {
		Result<IValue> expr = x.getExpression().accept(this);
		Result<IValue> repl = x.getReplacement().accept(this);
		String name = x.getKey().toString();
		return expr.fieldUpdate(name, repl, peek().getStore(), x);
	}

	
	@Override
	public Result<IValue> visitExpressionLexical(Lexical x) {
		throw new NotYetImplemented(x);// TODO
	}
	
	@Override
	public Result<IValue> visitExpressionRange(Range x) {
		//IListWriter w = vf.listWriter(tf.integerType());
		Result<IValue> from = x.getFirst().accept(this);
		Result<IValue> to = x.getLast().accept(this);
		return from.makeRange(to, x);
	}
	
	@Override
	public Result<IValue> visitExpressionStepRange(StepRange x) {
		Result<IValue> from = x.getFirst().accept(this);
		Result<IValue> to = x.getLast().accept(this);
		Result<IValue> second = x.getSecond().accept(this);
		return from.makeStepRange(to, second, x);
	}
	
	@Override
	public Result<IValue> visitExpressionTypedVariable(TypedVariable x) {
		throw new SyntaxError("Typed variable outside matching context", x.getLocation());
	}
	
	private boolean matchAndEval(IValue subject, org.meta_environment.rascal.ast.Expression pat, Statement stat){
		Environment newEnv = pushEnv(); 	// Create a separate scope for match and statement
		try {
			MatchPattern mp = evalPattern(pat);
			mp.initMatch(subject, peek());
			//System.err.println("matchAndEval: subject=" + subject + ", pat=" + pat);
			//peek().storeInnermostVariable("subject", makeResult(subject.getType(), subject, getCurrentAST()));
			while(mp.hasNext()){
				//System.err.println("matchAndEval: mp.hasNext()==true");
				if(mp.next()){
					//System.err.println("matchAndEval: mp.next()==true");
					try {
						checkPoint(peek());
						//System.err.println(stat.toString());
						try {
							stat.accept(this);
						} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
							// Make sure that the match pattern is set
							if(e.getMatchPattern() == null)
								e.setMatchPattern(mp);
							throw e;
						}
						commit(peek());
						return true;
					} catch (Failure e){
						//System.err.println("failure occurred");
						rollback(peek());
					}
				}
			}
		} finally {
			popUntil(newEnv);
		}
		return false;
	}
	
	private boolean matchEvalAndReplace(IValue subject, 
			org.meta_environment.rascal.ast.Expression pat, 
			java.util.List<Expression> conditions,
			Expression replacementExpr){
		
		Environment newEnv = pushEnv();	// create separate scope for match and statement  
		try {
			MatchPattern mp = evalPattern(pat);
			mp.initMatch(subject, peek());
			//System.err.println("matchEvalAndReplace: subject=" + subject + ", pat=" + pat + ", conditions=" + conditions);

			while(mp.hasNext()){
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
							throw new org.meta_environment.rascal.interpreter.control_exceptions.Insert(replacementExpr.accept(this), mp);		
						}
					} catch (Failure e){
						//System.err.println("failure occurred");
					}
				}
			}
		} finally {
			//System.err.println("matchEvalAndReplace.finally");
			popUntil(newEnv);
		}
		return false;
	}
	
	@Override
	public Result<IValue> visitStatementSwitch(Switch x) {
		Result<IValue> subject = x.getExpression().accept(this);

		for(Case cs : x.getCases()){
			if(cs.isDefault()){
				return cs.getStatement().accept(this);
			}
			org.meta_environment.rascal.ast.PatternAction rule = cs.getPatternAction();
			if(rule.isArbitrary() && matchAndEval(subject.getValue(), rule.getPattern(), rule.getStatement())){
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
	public Result<IValue> visitExpressionVisit(Visit x) {
		return x.getVisit().accept(this);
	}
	
	/*
	 * TraverseResult contains the value returned by a traversal
	 * and a changed flag that indicates whether the value itself or
	 * any of its children has been changed during the traversal.
	 */
	
	class TraverseResult {
		boolean matched;   // Some rule matched;
		IValue value; 		// Result<IValue> of the 
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
			this.value   = value;
			this.changed = changed;
		}
		TraverseResult(boolean someMatch, IValue value, boolean changed){
			this.matched = someMatch;
			this.value   = value;
			this.changed = changed;
		}
	}
	
	/*
	 * CaseOrRule is the union of a Case or a Rule and allows the sharing of
	 * traversal code for both.
	 */
	class CasesOrRules {
		private java.util.List<Case> cases;
		private java.util.List<RewriteRule> rules;
		
		CasesOrRules(java.util.List<?> casesOrRules){
			if(casesOrRules.get(0) instanceof Case){
				this.cases = (java.util.List<Case>) casesOrRules;
			} else {
				rules = (java.util.List<RewriteRule>)casesOrRules;
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
		public java.util.List<RewriteRule> getRules(){
			return rules;
		}
	}
	
	private TraverseResult traverse(IValue subject, CasesOrRules casesOrRules,
			DIRECTION direction, PROGRESS progress, FIXEDPOINT fixedpoint) {
		//System.err.println("traverse: subject=" + subject + ", casesOrRules=" + casesOrRules);
		do {
			TraverseResult tr = traverseOnce(subject, casesOrRules, direction, progress);
			if(fixedpoint == FIXEDPOINT.Yes){
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
		
		@Override
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
		if(cs != null && cs.isPatternAction() && re.isRegExpPattern(cs.getPatternAction().getPattern())){
			/*
			 * In the frequently occurring case that there is one case with a regexp as pattern,
			 * we can delegate all the work to the regexp matcher.
			 */
			org.meta_environment.rascal.ast.PatternAction rule = cs.getPatternAction();
			
			Expression patexp = rule.getPattern();
			MatchPattern mp = evalPattern(patexp);
			mp.initMatch(subject, peek());
			Environment newEnv = pushEnv(); // a separate scope for match and statement/replacement
			try {
				while(mp.hasNext()){
					if(mp.next()){
						try {
							if(rule.isReplacing()){
								Replacement repl = rule.getReplacement();
								boolean trueConditions = true;
								if(repl.isConditional()){
									for(Expression cond : repl.getConditions()){
										Result<IValue> res = cond.accept(this);
										if(!res.isTrue()){         // TODO: How about alternatives?
											trueConditions = false;
											break;
										}
									}
								}
								if(trueConditions){
									throw new org.meta_environment.rascal.interpreter.control_exceptions.Insert(repl.getReplacementExpression().accept(this), mp);
								}
							
							} else {
								rule.getStatement().accept(this);
							}
						} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
							changed = true;
							IValue repl = e.getValue().getValue();
							if(repl.getType().isStringType()){
								int start = ((RegExpPatternValue) mp).getStart();
								int end = ((RegExpPatternValue) mp).getEnd();
								replacements.add(new StringReplacement(start, end, ((IString)repl).getValue()));
							} else {
								throw new UnexpectedTypeError(tf.stringType(),repl.getType(), rule);
							}
						} catch (Failure e){
							//System.err.println("failure occurred");
						}
					}
				}
			} finally {
				popUntil(newEnv);
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
				} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e){
					IValue repl = e.getValue().getValue();
					if(repl.getType().isStringType()){
						int start;
						int end;
						MatchPattern lastPattern = e.getMatchPattern();
						if(lastPattern == null)
							throw new ImplementationError("no last pattern known");
						if(lastPattern instanceof RegExpPatternValue){
							start = ((RegExpPatternValue)lastPattern).getStart();
							end = ((RegExpPatternValue)lastPattern).getEnd();
						} else if(lastPattern instanceof AbstractPatternLiteral){
							start = 0;
							end = ((IString)repl).getValue().length();
						} else {
							throw new SyntaxError("Illegal pattern " + lastPattern + " in string visit", getCurrentAST().getLocation());
						}
						
						replacements.add(new StringReplacement(cursor + start, cursor + end, ((IString)repl).getValue()));
						matched = changed = true;
						cursor += end;
					} else {
						throw new UnexpectedTypeError(tf.stringType(),repl.getType(), getCurrentAST());
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
			DIRECTION direction, 
			PROGRESS progress){
		Type subjectType = subject.getType();
		boolean matched = false;
		boolean changed = false;
		IValue result = subject;

		//System.err.println("traverseOnce: " + subject + ", type=" + subject.getType());
		if(subjectType.isStringType()){
			return traverseString((IString) subject, casesOrRules);
		}

		if(direction == DIRECTION.TopDown){
			TraverseResult tr = traverseTop(subject, casesOrRules);
			matched |= tr.matched;
			changed |= tr.changed;
			if((progress == PROGRESS.Breaking) && changed){
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
					TraverseResult tr = traverseOnce(cons.get(i), casesOrRules, direction, progress);
					matched |= tr.matched;
					changed |= tr.changed;
					args[i] = tr.value;
				}
				IConstructor rcons = vf.constructor(cons.getConstructorType(), args);
				result = rcons.setAnnotations(cons.getAnnotations());
			}
		} else
			if(subjectType.isNodeType()){
				INode node = (INode)subject;
				if(node.arity() == 0){
					result = subject;
				} else {
					IValue args[] = new IValue[node.arity()];

					for(int i = 0; i < node.arity(); i++){
						TraverseResult tr = traverseOnce(node.get(i), casesOrRules, direction, progress);
						matched |= tr.matched;
						changed |= tr.changed;
						args[i] = tr.value;
					}
					result = vf.node(node.getName(), args).setAnnotations(node.getAnnotations());
				}
			} else
				if(subjectType.isListType()){
					IList list = (IList) subject;
					int len = list.length();
					if(len > 0){
						IListWriter w = list.getType().writer(vf);
						for(int i = len - 1; i >= 0; i--){
							TraverseResult tr = traverseOnce(list.get(i), casesOrRules, direction, progress);
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
								TraverseResult tr = traverseOnce(v, casesOrRules, direction, progress);
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
									TraverseResult tr = traverseOnce(entry.getKey(), casesOrRules, direction, progress);
									matched |= tr.matched;
									changed |= tr.changed;
									IValue newKey = tr.value;
									tr = traverseOnce(entry.getValue(), casesOrRules, direction, progress);
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
									TraverseResult tr = traverseOnce(tuple.get(i), casesOrRules, direction, progress);
									matched |= tr.matched;
									changed |= tr.changed;
									args[i] = tr.value;
								}
								result = vf.tuple(args);
							} else {
								result = subject;
							}

		if(direction == DIRECTION.BottomUp){
			if((progress == PROGRESS.Breaking) && changed){
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
		if(newSubject.getType().equivalent((oldSubject.getType())))
			return new TraverseResult(true, newSubject, true);
		throw new UnexpectedTypeError(oldSubject.getType(), newSubject.getType(), getCurrentAST());
	}
	
	/**
	 * Loop over all cases or rules.
	 */
	
	private TraverseResult applyCasesOrRules(IValue subject, CasesOrRules casesOrRules) {
		if(casesOrRules.hasCases()){
			for (Case cs : casesOrRules.getCases()) {
				setCurrentAST(cs);
				if (cs.isDefault()) {
					cs.getStatement().accept(this);
					return new TraverseResult(true,subject);
				} else {
					TraverseResult tr = applyOneRule(subject, cs.getPatternAction());
					if(tr.matched){
						return tr;
					}
				}
			}
		} else {
			//System.err.println("hasRules");

			for(RewriteRule rule : casesOrRules.getRules()){
				setCurrentAST(rule.getRule());
				
				callStack.push(rule.getEnvironment());
				try {
					TraverseResult tr = applyOneRule(subject, rule.getRule());
					if(tr.matched){
						return tr;
					}
				}
				finally {
					popUntil(rule.getEnvironment());
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
		} catch (org.meta_environment.rascal.interpreter.control_exceptions.Insert e) {

			return replacement(subject, e.getValue().getValue());
		}
	}
	
	/*
	 * applyOneRule: try to apply one rule to the subject.
	 */
	
	private TraverseResult applyOneRule(IValue subject,
			org.meta_environment.rascal.ast.PatternAction rule) {
		
		//System.err.println("applyOneRule: subject=" + subject + ", type=" + subject.getType() + ", rule=" + rule);
	
		if (rule.isArbitrary()){
			if(matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
			/*
		} else if (rule.isGuarded()) {
			org.meta_environment.rascal.ast.Type tp = rule.getType();
			Type type = evalType(tp);
			rule = rule.getRule();
			if (subject.getType().isSubtypeOf(type) && 
				matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
				return new TraverseResult(true, subject);
			}
			*/
		} else if (rule.isReplacing()) {
			Replacement repl = rule.getReplacement();
			java.util.List<Expression> conditions = repl.isConditional() ? repl.getConditions() : new ArrayList<Expression>();
			if(matchEvalAndReplace(subject, rule.getPattern(), conditions, repl.getReplacementExpression())){
				return new TraverseResult(true, subject);
			}
		} else {
			throw new ImplementationError("Impossible case in rule");
		}
			return new TraverseResult(subject);
	}
	
	@Override
	public Result<IValue> visitVisitDefaultStrategy(DefaultStrategy x) {
		
		IValue subject = x.getSubject().accept(this).getValue();
		java.util.List<Case> cases = x.getCases();
		
		TraverseResult tr = traverse(subject, new CasesOrRules(cases), 
									DIRECTION.BottomUp,
									PROGRESS.Continuing,
									FIXEDPOINT.No);
		return makeResult(tr.value.getType(), tr.value, x);
	}
	
	@Override
	public Result<IValue> visitVisitGivenStrategy(GivenStrategy x) {
		
		IValue subject = x.getSubject().accept(this).getValue();
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
		
		TraverseResult tr = traverse(subject, new CasesOrRules(cases), direction, progress, fixedpoint);
		return makeResult(subjectType, tr.value, x);
	}
	
	@Override
	public Result<IValue> visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.nonEquals(right, x);
	}
	
	
	@Override
	public Result<IValue> visitExpressionLessThan(LessThan x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.lessThan(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionLessThanOrEq(LessThanOrEq x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.lessThanOrEqual(right, x);
	}
	@Override
	public Result<IValue> visitExpressionGreaterThan(GreaterThan x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.greaterThan(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.greaterThanOrEqual(right, x);
	}
	
	@Override
	public Result<IValue> visitExpressionIfThenElse(
			org.meta_environment.rascal.ast.Expression.IfThenElse x) {
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
		catch (org.meta_environment.rascal.interpreter.control_exceptions.Throw e) {
			// TODO For now we accept any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return x.getRhs().accept(this);
		}
	}
	
	@Override
	public Result<IValue> visitExpressionIsDefined(IsDefined x) {
		try {
			x.getArgument().accept(this); // wait for exception
			return makeResult(tf.boolType(), vf.bool(true), x);
			
		} catch (org.meta_environment.rascal.interpreter.control_exceptions.Throw e) {
			// TODO For now we accept any Throw here, restrict to NoSuchKey and NoSuchAnno?
			return makeResult(tf.boolType(), vf.bool(false), x);
		}
	}

	
	@Override
	public Result<IValue> visitExpressionIn(In x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		// TODO:?!?!!? makes this less obtrusive?
		return right.in(left, x);
		//return result(vf.bool(in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public Result<IValue> visitExpressionNotIn(NotIn x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		// TODO:?!?!!? makes this less obtrusive?
		return right.notIn(left, x);
		//return result(vf.bool(!in(x.getLhs(), x.getRhs())));
	}
	
	@Override
	public Result<IValue> visitExpressionComposition(Composition x) {
		Result<IValue> left = x.getLhs().accept(this);
		Result<IValue> right = x.getRhs().accept(this);
		return left.compose(right, x);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveClosure(TransitiveClosure x) {
		return x.getArgument().accept(this).transitiveClosure(x);
	}
	
	@Override
	public Result<IValue> visitExpressionTransitiveReflexiveClosure(TransitiveReflexiveClosure x) {
		return x.getArgument().accept(this).transitiveReflexiveClosure(x);
	}
	
	// Comprehensions ----------------------------------------------------
	
	@Override
	public Result<IValue> visitExpressionComprehension(Comprehension x) {
		Environment newEnv = pushEnv();	// TODO not necessary?
		try {
			return x.getComprehension().accept(this);	
		}
		finally {
			popUntil(newEnv);
		}
	}
	
	@Override
	public Result<IValue> visitExpressionEnumerator(
			org.meta_environment.rascal.ast.Expression.Enumerator x) {
		return new EnumeratorAsGenerator(x, this);
	}
	
	@Override
	public Result<IValue> visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		return new EnumeratorAsGenerator(x, this);
	}
	
	/*
	 * ExpressionAsGenerator implements an expression appearing in a comprehension.
	 * The expression maybe multiple-valued (e.g. a match expression) and its its successive
	 * values will be generated.
	 */
	
	class ExpressionAsGenerator extends Result<IValue> {
		private boolean firstTime = true;
		private Result<IValue> result;
		private org.meta_environment.rascal.ast.Expression expr;
		private Evaluator evaluator;

		ExpressionAsGenerator(Expression g, Evaluator ev){
			super(tf.boolType(), vf.bool(true), null);
			evaluator = ev;
			expr = g;
		}
		
		@Override
		public Type getType(){
			return TypeFactory.getInstance().boolType();
		}	
		
		@Override
		public IValue getValue(){
			/*
			
			if(hasNext())
				return next().getValue();
			return vf.bool(false);
			*/
			
			if(firstTime){
				firstTime = false;
				return next().getValue();
			}
			return vf.bool(true);
			
		}
		
		@Override
		public boolean hasNext(){
			return firstTime || result.hasNext();
		}

		@Override
		public Result next(){
			if(firstTime){
				/* Evaluate expression only once */
				firstTime = false;
				result = expr.accept(evaluator);
				if(result.getType().isBoolType() && result.getValue() != null){
					// FIXME: if result is of type void, you get a null pointer here.
					if (result.getValue().isEqual(vf.bool(true))) {
						return new BoolResult(true, null, null);
					}
					return new BoolResult(false, null, null);
				} else {
					throw new UnexpectedTypeError(tf.boolType(), result.getType(), expr);
				}
			}
			return result.next();
		}

		@Override
		public void remove() {
			throw new ImplementationError("remove() not implemented for GeneratorEvaluator");
		}
	}
	
	/*
	 * EnumeratorAsGenerator implements an enumerator of the form
	 * 	pattern <- Expression
	 * that occurs in a comprehension. The expression produces a sequence of values and the
	 * pattern is matched against each value.
	 */
	
	class EnumeratorAsGenerator extends Result<IValue> {
		private boolean firstTime = true;
		private boolean hasNext = true;
		private Environment pushedEnv;
		private MatchPattern pat;
		private java.util.List<String> patVars;
		private org.meta_environment.rascal.ast.Expression patexpr;
		private Evaluator evaluator;
		private Iterator<?> iterator;

		EnumeratorAsGenerator(Expression enumerator, Evaluator ev){
			super(tf.boolType(), vf.bool(true), null);

			evaluator = ev;
			if(!(enumerator.isEnumerator() || enumerator.isEnumeratorWithStrategy())){
				throw new ImplementationError("EnumeratorAsGenerator");
			}			
				
			pushedEnv = evaluator.pushEnv();
			pat = evalPattern(enumerator.getPattern());
			
			// Collect all variables in the pattern and remove those that are 
			// already defined in the current environment
			
			java.util.List<String> vars = pat.getVariables();
		
			patVars = new java.util.ArrayList<String>(vars.size());
			for(String name : vars){
				Result r = pushedEnv.getVariable(null, name);
				if(r == null || r.getValue() == null)
					patVars.add(name);
			}
			patexpr = enumerator.getExpression();
		
			Result<IValue> r = patexpr.accept(ev);
			Type patType = pat.getType(evaluator.peek());
			
			// List
			if(r.getType().isListType()){
				if(enumerator.hasStrategy()) {
					throw new UnsupportedOperationError(enumerator.toString(), r.getType(), enumerator);
				}
				if(!mayOccurIn(patType,r.getType().getElementType(), new HashSet<Type>()))
						throw new UnexpectedTypeError(patType, r.getType().getElementType(), enumerator.getPattern());
				iterator = ((IList) r.getValue()).iterator();
				
			// Set
			} else 	if(r.getType().isSetType()){
				if(enumerator.hasStrategy()) {
					throw new UnsupportedOperationError(enumerator.toString(), r.getType(), enumerator);
				}
				if(!mayOccurIn(patType,r.getType().getElementType()))
					throw new UnexpectedTypeError(patType, r.getType().getElementType(), enumerator.getPattern());
				iterator = ((ISet) r.getValue()).iterator();
			
			// Map
			} else if(r.getType().isMapType()){
				if(enumerator.hasStrategy()) {
					throw new UnsupportedOperationError(enumerator.toString(), r.getType(), enumerator);
				}
				if(!mayOccurIn(patType,r.getType().getKeyType()))
					throw new UnexpectedTypeError(patType, r.getType().getKeyType(), enumerator.getPattern());
				iterator = ((IMap) r.getValue()).iterator();
				
			// Node and ADT
			} else if(r.getType().isNodeType() || r.getType().isAbstractDataType()){
				boolean bottomup = true;
				if(enumerator.hasStrategy()){
					Strategy strat = enumerator.getStrategy();

					if(strat.isTopDown()){
						bottomup = false;
					} else if(strat.isBottomUp()){
							bottomup = true;
					} else {
						throw new UnsupportedOperationError(enumerator.toString(), r.getType(), enumerator);
					}
				}
				if(!mayOccurIn(patType, r.getType()))
					throw new UnexpectedTypeError(patType, r.getType(), enumerator.getPattern());
				iterator = new INodeReader((INode) r.getValue(), bottomup);
			} else if(r.getType().isStringType()){
				if(enumerator.hasStrategy()) {
					throw new UnsupportedOperationError(enumerator.getStrategy().toString(), r.getType(), enumerator.getStrategy());
				}
				if(!r.getType().isSubtypeOf(patType))
					throw new UnexpectedTypeError(patType, r.getType(), enumerator.getPattern());
				iterator = new SingleIValueIterator(r.getValue());
			} else {
				throw new UnsupportedOperationError("generator", r.getType(), enumerator);
			}
		}
		
		@Override
		public Type getType(){
			return TypeFactory.getInstance().boolType();
		}
		
		@Override
		public IValue getValue(){
			/*
			if(hasNext())
				return next().getValue();
			return vf.bool(true);
			*/
			
			if(firstTime){
				firstTime = false;
				return next().getValue();
			}
			return vf.bool(true);
		
		}
		
		@Override
		public boolean hasNext(){
			if(hasNext){
				boolean hn = pat.hasNext() || iterator.hasNext();
				if(!hn){
					hasNext = false;
					//System.err.println("GeneratorEvaluator.pop, " + patexpr);
					evaluator.popUntil(pushedEnv);
				}
				return hn;
			}
			return false;
		}

		@Override
		public Result next(){
			//System.err.println("getNext, trying pat " + pat);
			/*
			 * First, explore alternatives that remain to be matched by the current pattern
			 */
			
			while(pat.hasNext()){
				if(pat.next()){
					//System.err.println("return true");
					return new BoolResult(true, this, null);
				}
			}
			
			/*
			 * Next, fetch a new data element (if any) and create a new pattern.
			 */
			
			while(iterator.hasNext()){
				// Nullify the variables set by the pattern
				for(String var : patVars){
					evaluator.peek().storeVariable(var,  ResultFactory.nothing());
				}
				IValue v = (IValue) iterator.next();
				////System.err.println("getNext, try next from value iterator: " + v);
				pat.initMatch(v, peek());
				while(pat.hasNext()){
					if(pat.next()){
						//System.err.println("return true");
					return new BoolResult(true, this, null);						
					}	
				}
			}
			//aSystem.err.println("next returns false and pops env");
			hasNext = false;
			evaluator.popUntil(pushedEnv);
			return new BoolResult(false, null, null);
		}

		@Override
		public void remove() {
			throw new ImplementationError("remove() not implemented for GeneratorEvaluator");
		}
	}
	
	private Result<IValue> makeGenerator(Expression g){
		if(g.isEnumerator() || g.isEnumeratorWithStrategy()){
			return new EnumeratorAsGenerator(g, this);
		} else {
			return new ExpressionAsGenerator(g,this);
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
		protected org.meta_environment.rascal.ast.Expression resultExpr1;
		protected org.meta_environment.rascal.ast.Expression resultExpr2;
		protected IWriter writer;
		protected Evaluator ev;
		
		ComprehensionWriter(
				org.meta_environment.rascal.ast.Expression resultExpr1,
				org.meta_environment.rascal.ast.Expression resultExpr2, 
				Evaluator ev){
			this.ev = ev;
			this.resultExpr1 = resultExpr1;
			this.resultExpr2 = resultExpr2;
			this.writer = null;
		}
		
		public void check(Result<IValue> r, Type t, String kind, org.meta_environment.rascal.ast.Expression expr){
			if(!r.getType().isSubtypeOf(t)){
				throw new UnexpectedTypeError(t, r.getType() ,
					                    expr);
			}
		}
		
		public abstract void append();
		
		public abstract Result<IValue> done();
	}
	
	private class ListComprehensionWriter extends
			ComprehensionWriter {

		ListComprehensionWriter(
				org.meta_environment.rascal.ast.Expression resultExpr1,
				Evaluator ev) {
			super(resultExpr1, null, ev);
		}

		@Override
		public void append() {
			Result<IValue> r1 = resultExpr1.accept(ev);
			if (writer == null) {
				elementType1 = r1.getType();
				resultType = tf.listType(elementType1);
				writer = resultType.writer(vf);
			}
			check(r1, elementType1, "list", resultExpr1);
			elementType1 = elementType1.lub(r1.getType());
			((IListWriter) writer).append(r1.getValue());
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? makeResult(tf.listType(tf.voidType()), vf.list(), resultExpr1) : 
				makeResult(tf.listType(elementType1), writer.done(), resultExpr1);
		}
	}
	
	private class SetComprehensionWriter extends
			ComprehensionWriter {

		SetComprehensionWriter(
				org.meta_environment.rascal.ast.Expression resultExpr1,
				Evaluator ev) {
			super(resultExpr1, null, ev);
		}

		@Override
		public void append() {
			Result<IValue> r1 = resultExpr1.accept(ev);
			if (writer == null) {
				elementType1 = r1.getType();
				resultType = tf.setType(elementType1);
				writer = resultType.writer(vf);
			}
			check(r1, elementType1, "set", resultExpr1);
			elementType1 = elementType1.lub(r1.getType());
			((ISetWriter) writer).insert(r1.getValue());
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? makeResult(tf.setType(tf.voidType()), vf.set(), resultExpr1) : 
				makeResult(tf.setType(elementType1), writer.done(), resultExpr1);
		}
	}

	private class MapComprehensionWriter extends
			ComprehensionWriter {

		MapComprehensionWriter(
				org.meta_environment.rascal.ast.Expression resultExpr1,
				org.meta_environment.rascal.ast.Expression resultExpr2,
				Evaluator ev) {
			super(resultExpr1, resultExpr2, ev);
		}

		@Override
		public void append() {
			Result<IValue> r1 = resultExpr1.accept(ev);
			Result<IValue> r2 = resultExpr2.accept(ev);
			if (writer == null) {
				elementType1 = r1.getType();
				elementType2 = r2.getType();
				resultType = tf.mapType(elementType1, elementType2);
				writer = resultType.writer(vf);
			}
			check(r1, elementType1, "map", resultExpr1);
			check(r2, elementType2, "map", resultExpr2);
			((IMapWriter) writer).put(r1.getValue(), r2.getValue());
		}

		@Override
		public Result<IValue> done() {
			return (writer == null) ? makeResult(tf.mapType(tf.voidType(), tf.voidType()), vf.map(tf.voidType(), tf.voidType()), resultExpr1)
					: makeResult(tf.mapType(elementType1, elementType2), writer.done(), resultExpr1);
		}
	}
	
	
	/*
	 * The common comprehension evaluator
	 */
	
	private Result<IValue> evalComprehension(java.util.List<Expression> generators, 
										  ComprehensionWriter w){
		int size = generators.size();
		Result<IValue>[] gens = new Result[size];
		
		int i = 0;
		gens[0] = makeGenerator(generators.get(0));
		while (i >= 0 && i < size){
			if (gens[i].hasNext() && gens[i].next().isTrue()) {
				if(i == size - 1){
					w.append();
				} 
				else {
					i++;
					gens[i] = makeGenerator(generators.get(i));
				}
			} else {
				i--;
			}
		}
		return w.done();
	}
	
	@Override
	public Result<IValue> visitComprehensionList(org.meta_environment.rascal.ast.Comprehension.List x) {
		return evalComprehension(
				x.getGenerators(),
				new ListComprehensionWriter(x.getResult(), this));
	}
	
	@Override
	public Result<IValue> visitComprehensionSet(
			org.meta_environment.rascal.ast.Comprehension.Set x) {
		return evalComprehension(
				x.getGenerators(),
				new SetComprehensionWriter(x.getResult(), this));
	}
	
	@Override
	public Result<IValue> visitComprehensionMap(
			org.meta_environment.rascal.ast.Comprehension.Map x) {
		return evalComprehension(
				x.getGenerators(),
				new MapComprehensionWriter(x.getFrom(), x.getTo(), this));
	}

	@Override
	public Result<IValue> visitStatementFor(For x) {
		Statement body = x.getBody();
		java.util.List<Expression> generators = x.getGenerators();
		int size = generators.size();
		Result<IValue>[] gens = new Result[size];
		Result<IValue> result = nothing();
		
		int i = 0;
		gens[0] = makeGenerator(generators.get(0));
		while(i >= 0 && i < size){		
			if(gens[i].hasNext() && gens[i].next().isTrue()){
				if(i == size - 1){
					result = body.accept(this);
				} else {
					i++;
					gens[i] = makeGenerator(generators.get(i));
				}
			} else {
				i--;
			}
		}
		return result;
	}
	
	@Override
	public Result visitExpressionAny(Any x) {
		java.util.List<Expression> generators = x.getGenerators();
		int size = generators.size();
		Result<IValue>[] gens = new Result[size];

		int i = 0;
		gens[0] = makeGenerator(generators.get(0));
		while (i >= 0 && i < size) {
			if (gens[i].hasNext() && gens[i].next().isTrue()) {
				if (i == size - 1) {
					return new BoolResult(true, null, null);
				} else {
					i++;
					gens[i] = makeGenerator(generators.get(i));
				}
			} else {
				i--;
			}
		}
		return new BoolResult(false, null, null);
	}
	
	@Override
	public Result visitExpressionAll(All x) {
		java.util.List<Expression> producers = x.getGenerators();
		int size = producers.size();
		Result<IValue>[] gens = new Result[size];

		int i = 0;
		gens[0] = makeGenerator(producers.get(0));
		while (i >= 0 && i < size) {
			if (gens[i].hasNext()) {
				if (!gens[i].next().isTrue()) {
					return new BoolResult(false, null, null);
				}
				if (i < size - 1) {
					i++;
					gens[i] = makeGenerator(producers.get(i));
				}
			} else {
				i--;
			}
		}
		return new BoolResult(true, null, null);
	}
	
	// ------------ solve -----------------------------------------
	
	@Override
	public Result<IValue> visitStatementSolve(Solve x) {
		java.util.ArrayList<org.meta_environment.rascal.ast.Variable> vars = new java.util.ArrayList<org.meta_environment.rascal.ast.Variable>();
		
		for(Declarator d : x.getDeclarations()){
			for(org.meta_environment.rascal.ast.Variable v : d.getVariables()){
				vars.add(v);
			}
			d.accept(this);
		}
		IValue currentValue[] = new IValue[vars.size()];
		for(int i = 0; i < vars.size(); i++){
			org.meta_environment.rascal.ast.Variable v = vars.get(i);
			currentValue[i] = peek().getVariable(v, Names.name(v.getName())).getValue();
		}
		
		Statement body = x.getBody();
		
		int max = 1000;
		
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
		
		while (change && iterations < max){
			change = false;
			iterations++;
			bodyResult = body.accept(this);
			for(int i = 0; i < vars.size(); i++){
				org.meta_environment.rascal.ast.Variable var = vars.get(i);
				Result<IValue> v = peek().getVariable(var, Names.name(var.getName()));
				if(currentValue[i] == null || !v.getValue().isEqual(currentValue[i])){
					change = true;
					currentValue[i] = v.getValue();
				}
			}
		}
		return bodyResult;
	}
}
