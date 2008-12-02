package org.meta_environment.rascal.interpreter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.eclipse.imp.pdb.facts.type.MapType;
import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.RelationType;
import org.eclipse.imp.pdb.facts.type.SetType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Case;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Generator;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.ModuleName;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Signature;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Toplevel;
import org.meta_environment.rascal.ast.TypeArg;
import org.meta_environment.rascal.ast.ValueProducer;
import org.meta_environment.rascal.ast.Variant;
import org.meta_environment.rascal.ast.Assignable.Constructor;
import org.meta_environment.rascal.ast.Assignable.FieldAccess;
import org.meta_environment.rascal.ast.Declaration.Annotation;
import org.meta_environment.rascal.ast.Declaration.Data;
import org.meta_environment.rascal.ast.Declaration.Function;
import org.meta_environment.rascal.ast.Declaration.Rule;
import org.meta_environment.rascal.ast.Declaration.Tag;
import org.meta_environment.rascal.ast.Declaration.Variable;
import org.meta_environment.rascal.ast.Declaration.View;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.Ambiguity;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Division;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.IfDefined;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.Intersection;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Modulo;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Negative;
import org.meta_environment.rascal.ast.Expression.NonEmptyBlock;
import org.meta_environment.rascal.ast.Expression.NotIn;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.Product;
import org.meta_environment.rascal.ast.Expression.RegExpMatch;
import org.meta_environment.rascal.ast.Expression.RegExpNoMatch;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.Subscript;
import org.meta_environment.rascal.ast.Expression.Subtraction;
import org.meta_environment.rascal.ast.Expression.TransitiveClosure;
import org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Double;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.LocalVariableDeclaration.Default;
import org.meta_environment.rascal.ast.Statement.Assert;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Block;
import org.meta_environment.rascal.ast.Statement.Expression;
import org.meta_environment.rascal.ast.Statement.Fail;
import org.meta_environment.rascal.ast.Statement.For;
import org.meta_environment.rascal.ast.Statement.IfThen;
import org.meta_environment.rascal.ast.Statement.IfThenElse;
import org.meta_environment.rascal.ast.Statement.Insert;
import org.meta_environment.rascal.ast.Statement.Switch;
import org.meta_environment.rascal.ast.Statement.VariableDeclaration;
import org.meta_environment.rascal.ast.Statement.While;
import org.meta_environment.rascal.ast.Toplevel.DefaultVisibility;
import org.meta_environment.rascal.ast.Toplevel.GivenVisibility;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;

public class Evaluator extends NullASTVisitor<EvalResult> {
	public static final String RASCAL_FILE_EXT = ".rsc";
	final IValueFactory vf;
	final TypeFactory tf;
	private final TypeEvaluator te = new TypeEvaluator();
	private final RegExpEvaluator re = new RegExpEvaluator();
	private final EnvironmentStack env = new EnvironmentStack();
	private final ASTFactory af;

	public Evaluator(IValueFactory f, ASTFactory astFactory) {
		this.vf = f;
		this.af = astFactory;
		tf = TypeFactory.getInstance();
	}

	EvalResult result(Type t, IValue v) {
		return new EvalResult(t, v);
	}

	private EvalResult result(IValue v) {
		Type type = v.getType();
		
		if (type.isRelationType() 
				|| type.isSetType() 
				|| type.isMapType()
				|| type.isListType()) {
			throw new RascalBug("Should not used run-time type for type checking!!!!");
		}
				
		return new EvalResult(v != null ? type : null, v);
	}
	
	private EvalResult result() {
		return new EvalResult(null, null);
	}
	
	private EvalResult notImplemented(String s){
		throw new RascalTypeError(s + " not yet implemented");
	}

	public IValue eval(Statement S) {
		EvalResult r = S.accept(this);
        if(r != null){
        	return r.value;
        } else {
        	throw new RascalTypeError("Not yet implemented: " + S.getTree());
        }
	}
	
	public IValue eval(Declaration declaration) {
		EvalResult r = declaration.accept(this);
        if(r != null){
        	return r.value;
        } else {
        	throw new RascalBug("Not yet implemented: " + declaration.getTree());
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
		
		Parser p = Parser.getInstance();
		ASTBuilder b = new ASTBuilder(af);
		
		try {
			INode tree = p.parse(new FileInputStream(name + RASCAL_FILE_EXT));
			Module m = b.buildModule(tree);
			
			ModuleName declaredNamed = m.getHeader().getName();
			if (!declaredNamed.toString().equals(name)) {
				throw new RascalTypeError("Module " + declaredNamed + " should be in a file called " + declaredNamed + RASCAL_FILE_EXT + ", not " + name);
			}
			return m.accept(this);
		} catch (FactTypeError e) {
			throw new RascalTypeError("Something went wrong during parsing:", e);
		} catch (FileNotFoundException e) {
			throw new RascalTypeError("Could not import module", e);
		} catch (IOException e) {
			throw new RascalTypeError("Could not import module", e);
		}
	}
	
	@Override 
	public EvalResult visitModuleDefault(
			org.meta_environment.rascal.ast.Module.Default x) {
		String name = x.getHeader().getName().toString();
		ModuleEnvironment module = new ModuleEnvironment(name);
		
		env.addModule(module);
		env.pushModule(name);
		
		java.util.List<Toplevel> decls = x.getBody().getToplevels();
		for (Toplevel l : decls) {
			l.accept(this);
		}
		
		env.pop();
		return result();
	}
	
	@Override
	public EvalResult visitToplevelDefaultVisibility(DefaultVisibility x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public EvalResult visitToplevelGivenVisibility(GivenVisibility x) {
		// order dependent code here:
		x.getDeclaration().accept(this);
		String name = x.getDeclaration().getName().toString();
		ModuleVisibility visibility = getVisibility(x.getVisibility());
		env.setVisibility(name, visibility);
		return result();
	}
	
	private ModuleVisibility getVisibility(org.meta_environment.rascal.ast.Visibility v) {
		if (v.isPublic()) {
			return ModuleVisibility.PUBLIC;
		}
		else {
			return ModuleVisibility.PRIVATE;
		}
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
			String name = var.getName().toString();
			if (var.isUnInitialized()) {  
				throw new RascalTypeError("Module variable is not initialized: " + x);
			} else {
				EvalResult v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					r = result(declaredType, v.value);
					storeVariable(name, r);
				} else {
					throw new RascalTypeError("variable " + name + ", declared type " + declaredType + " incompatible with initial type " + v.type);
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
		  tf.declareAnnotation(type.accept(te), name, annoType);	
		}
		
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationData(Data x) {
		String name = x.getUser().getName().toString();
		NamedTreeType sort = tf.namedTreeType(name);
		
		for (Variant var : x.getVariants()) {
			String altName = var.getName().toString();
			
		    if (var.isNAryConstructor()) {
		    	java.util.List<TypeArg> args = var.getArguments();
		    	Object[] fieldsAndLabels = new Type[args.size() * 2];

		    	for (int i = 0, j = 0; i < args.size(); i++, j++) {
		    		fieldsAndLabels[j++] = args.get(i).getType().accept(te);
		    		fieldsAndLabels[j] = args.get(i).getName().toString();
		    	}

		    	TupleType children = tf.tupleType(fieldsAndLabels);
		    	tf.treeNodeType(sort, altName, children);
		    }
		    else if (var.isNillaryConstructor()) {
		    	tf.treeNodeType(sort, altName, new Object[] { });
		    }
		    else if (var.isAnonymousConstructor()) {
		    	Type argType = var.getType().accept(te);
		    	String label = var.getName().toString();
		    	tf.anonymousTreeType(sort, altName, argType, label);
		    }
		}
		
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationType(
			org.meta_environment.rascal.ast.Declaration.Type x) {
		// TODO add support for parameterized types
		String user = x.getUser().getName().toString();
		Type base = x.getBase().accept(te);
		tf.namedType(user, base);
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationView(View x) {
		// TODO implement
		throw new RascalTypeError("views are not yet implemented");
	}
	
	@Override
	public EvalResult visitDeclarationRule(Rule x) {
		Type outer;
	
		if (x.getRule().isNoGuard()) {
		   EvalResult result = x.getRule().getMatch().getMatch().accept(this);
		   outer = result.type;
		}
		else {
			outer = x.getRule().getType().accept(te);
			EvalResult result = x.getRule().getMatch().getMatch().accept(this);
			
			if (!result.type.isSubtypeOf(outer)) {
				throw new RascalTypeError("Declared type of rule does not match type of left-hand side: " + x);
			}
		}
		
		env.storeRule(outer, x.getRule());
		return result();
	}
	
	@Override
	public EvalResult visitDeclarationTag(Tag x) {
		throw new RascalTypeError("tags are not yet implemented");
	}
	
	// Variable Declarations -----------------------------------------------

	@Override
	public EvalResult visitLocalVariableDeclarationDefault(Default x) {
		return x.getDeclarator().accept(this);
	}

	@Override
	public EvalResult visitDeclaratorDefault(
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type declaredType = x.getType().accept(te);
		EvalResult r = result();

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			String name = var.getName().toString();
			if (var.isUnInitialized()) {  // variable declaration without initialization
				r = result(declaredType, null);
				storeVariable(name, r);
			} else {                     // variable declaration with initialization
				EvalResult v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					r = result(declaredType, v.value);
					storeVariable(name, r);
				} else {
					throw new RascalTypeError("variable " + name + ", declared type " + declaredType + " incompatible with initial type " + v.type);
				}
			}
		}
		return r;
	}
	
	// Function calls and tree constructors
	
	@Override
	public EvalResult visitExpressionCallOrTree(CallOrTree x) {
		 java.util.List<org.meta_environment.rascal.ast.Expression> args = x.getArguments();
		 
		 IValue[] actuals = new IValue[args.size()];
		 Type[] types = new Type[args.size()];

		 for (int i = 0; i < args.size(); i++) {
			 EvalResult resultElem = args.get(i).accept(this);
			 types[i] = resultElem.type;
			 actuals[i] = resultElem.value;
		 }
		 
		 TupleType actualTypes = tf.tupleType(types);
		 java.util.List<Name> names = x.getQualifiedName().getNames();
		 
		 if (names.size() == 1) {
			FunctionDeclaration functionDeclaration = env.getFunction(names.get(0).toString(), actualTypes);
			return call(functionDeclaration, actuals);
		 }
		 else if (names.size() == 2) {
			 String modulename = names.get(0).toString();
			 String name = names.get(1).toString();
			 FunctionDeclaration functionDeclaration = env.getModule(modulename).getFunction(name, actualTypes);
			 env.pushModule(modulename);
			 EvalResult result = call(functionDeclaration, actuals);
			 env.pop();
			 return result;
		 }
		 else {
			 throw new RascalTypeError("Unknown qualified name: " + x.getQualifiedName());
		 }
	
		 // TODO add support for trees
		 
		 
		 
	}

	private EvalResult call(FunctionDeclaration func, IValue[] actuals) {
		try {
			env.push();
			TupleType formals = (TupleType) func.getSignature().accept(te);
			
			for (int i = 0; i < formals.getArity(); i++) {
				EvalResult actual = result(formals.getFieldType(i), actuals[i]);
				env.storeVariable(formals.getFieldName(i), actual);
			}
			
			func.getBody().accept(this);
			
			throw new RascalTypeError("Function definition:" + func + "\n does not have a return statement.");
		}
		catch (ReturnException e) {
			env.pop();
			return e.getValue();
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
			if(r.value.equals(vf.bool(false))){
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
	public EvalResult visitStatementExpression(Expression x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public EvalResult visitStatementFunctionDeclaration(
			org.meta_environment.rascal.ast.Statement.FunctionDeclaration x) {
		return x.getFunctionDeclaration().accept(this);
	}
	
	private EvalResult assignVariable(String name, EvalResult right){
		EvalResult previous = getVariable(name);
		if (previous != null) {
			if (right.type.isSubtypeOf(previous.type)) {
				right.type = previous.type;
			} else {
				throw new RascalTypeError("Variable " + name
						+ " has type " + previous.type
						+ "; cannot assign value of type " + right.type);
			}
		}
		storeVariable(name, right);
		return right;
	}

	private void storeVariable(String name, EvalResult value) {
		env.storeVariable(name, value);
	}
	
	private int getValidIndex(EvalResult subs){
		if(!subs.type.isSubtypeOf(tf.integerType())){
			throw new RascalTypeError("subscript should have type int instead of " + subs.type);
		}
		return ((IInteger) subs.value).getValue();
	}
	
	private Type checkValidListSubscription(EvalResult previous, EvalResult subs, int index){
		if (previous != null) {
			if(previous.type.isListType()){
				Type elementType = ((ListType) previous.type).getElementType();
				if((index < 0) || index >= ((IList) previous.value).length()){
					throw new RascalTypeError("subscript " + index + " out of bounds");
				}
				return elementType;
			} else {
				notImplemented("index in assignment");
			}
		} else {
			throw new RascalTypeError("subscription for unnitialized variable ");
		}
		return null;
	}
	
	private Type checkValidListSubscription(String name, EvalResult subs, int index){
		EvalResult previous = getVariable(name);
		return checkValidListSubscription(previous, subs, index);
	}
	
	private EvalResult getVariable(String name) {
		return env.getVariable(name);
	}
	
	@Override
	public EvalResult visitExpressionSubscript(Subscript x) {
		EvalResult subs = x.getSubscript().accept(this);
		org.meta_environment.rascal.ast.Expression expr = x.getExpression();
		int index = getValidIndex(subs);
		if(expr.isQualifiedName()){
			String name = expr.getQualifiedName().toString();
			checkValidListSubscription(name, subs, index);
			return result(((IList)getVariable(name).value).get(index));
		} else if(expr.isSubscript()){
			EvalResult r = expr.accept(this);
			checkValidListSubscription(r, subs, index);
			return result(((IList) r.value).get(index));
		}
		
		// TODO implement other subscripts
		return null;
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
			throw ReturnException.getInstance(result());
		}
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
		for(Statement stat : x.getStatements()){
			r = stat.accept(this);
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
			Type type = ((TupleType) ((ITuple) receiver.value).getType()).getFieldType(label);
			return result(type, result);
		}
		else if (receiver.type.isTreeNodeType() || receiver.type.isNamedTreeType()) {
			IValue result = ((INode) receiver.value).get(label);
			TreeNodeType treeNodeType = ((INode) receiver.value).getTreeNodeType();
			Type type = treeNodeType.getChildType(treeNodeType.getChildIndex(label));
			return result(type, result);
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
		Type type = tf.getAnnotationType(receiver.type, label);
		IValue value = receiver.value.getAnnotation(label);
		return result(type, value);
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
		
		if (receiver.type.getBaseType().isListType() && subscript.type.getBaseType().isIntegerType()) {
			IList list = (IList) receiver.value;
			IValue result = list.get(((IInteger) subscript.value).getValue());
			Type type = ((ListType) receiver.type).getElementType();
			return result(type, result);
		}
		else if (receiver.type.getBaseType().isMapType()) {
			Type keyType = ((MapType) receiver.type).getKeyType();
			
			if (subscript.type.isSubtypeOf(keyType)) {
				IValue result = ((IMap) receiver.value).get(subscript.value);
				Type type = ((MapType) receiver.type).getValueType();
				return result(type, result);
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
		env.storeFunction(name,(org.meta_environment.rascal.ast.FunctionDeclaration)x);
		return result();
	}
	
	@Override
	public EvalResult visitStatementIfThenElse(IfThenElse x) {
		for (org.meta_environment.rascal.ast.Expression expr : x
				.getConditions()) {
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return x.getElseStatement().accept(this);
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		}
		return x.getThenStatement().accept(this);
	}

	@Override
	public EvalResult visitStatementIfThen(IfThen x) {
		for (org.meta_environment.rascal.ast.Expression expr : x
				.getConditions()) {
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
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
		do {
			EvalResult cval = expr.accept(this);
			EvalResult statVal = result();

			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
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
	
	//@Override
	public EvalResult visitStatementDoWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		do {
			EvalResult result = x.getBody().accept(this);
			EvalResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return result;
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		} while (true);
	}
	
	// TODO: in progress ...
	
	@Override
	public EvalResult visitStatementSwitch(Switch x) {
		EvalResult subject = x.getExpression().accept(this);

		for(Case cs : x.getCases()){
			if(cs.isDefault()){
				return cs.getStatement().accept(this);
			}
			org.meta_environment.rascal.ast.Rule rl = cs.getRule();
			org.meta_environment.rascal.ast.Expression pat = rl.getMatch().getMatch();
			if(match(subject, pat)){
				return rl.getMatch().getStatement().accept(this);
			}
		}
		return null;
	}
	
	private boolean isRegExpPattern(org.meta_environment.rascal.ast.Expression pat){
		
		
		if(pat.isLiteral()){
			org.meta_environment.rascal.ast.Literal lit = ((Literal) pat).getLiteral();
			if(lit.isRegExp()){
				return true;
			}
		}
		return false;
	}
	
	private boolean match(EvalResult subj, org.meta_environment.rascal.ast.Expression pat){
		System.err.println("match: pat : " + pat);
		if(pat.isQualifiedName()){
			//TODO typecheck
			assignVariable(pat.getQualifiedName().toString(), subj);
			return true;
		}
		if(isRegExpPattern(pat)){
			return regExpMatch(subj, pat);
		}
		if(pat.isTypedVariable()){
			TypedVariable tv = (TypedVariable) pat;
			org.meta_environment.rascal.ast.Type tp = tv.getType();
			org.meta_environment.rascal.ast.Name nm = tv.getName();
			//TODO typecheck
			assignVariable(nm.toString(), subj);
			return true;
		}
		if(pat.isTuple()){
			ITuple tup = (ITuple) pat;
			if(!tup.getType().isSubtypeOf(subj.type)){
				throw new RascalTypeError("incompatible types in match: " + tup.getType() + " and " + subj.type);
			}
			ITuple tsubj = (ITuple) subj.value;
			for(int i = 0; i < tup.arity(); i++){
			//	if(!match(result(tsubj.get(i)), tup.get(i))){
			//		return false;
			//	}
			}
		
		}
		return false;
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
	public EvalResult visitLiteralDouble(Double x) {
		String str = x.getDoubleLiteral().toString();
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
		java.util.List<Name> names = x.getQualifiedName().getNames();
		EvalResult result; 
		
		if (names.size() == 1) {
		   result = getVariable(names.get(0).toString());
		}
		else if (names.size() == 2) {
		   String modulename = names.get(0).toString();
		   String name = names.get(1).toString();
		   result = env.getModuleVariable(modulename, name);
		}
		else {
			throw new RascalTypeError("Unknown qualified name: " + x);
		}
		
		if (result != null && result.value != null) {
			return result;
		} else {
			throw new RascalTypeError("Uninitialized variable: " + x);
		}
	}
	
	
	@Override
	public EvalResult visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);

		ListType resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		return result(resultType, w.done());
	}

	@Override
	public EvalResult visitExpressionSet(Set x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);

		SetType resultType = tf.setType(elementType);
		ISetWriter w = resultType.writer(vf);
		w.insertAll(results);
		return result(resultType, w.done());
	}

	private Type evaluateElements(
			java.util.List<org.meta_environment.rascal.ast.Expression> elements,
			java.util.List<IValue> results) {
		Type elementType = tf.voidType();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			EvalResult resultElem = expr.accept(this);
			elementType = elementType.lub(resultElem.type);
			results.add(results.size(), resultElem.value);
		}
		return elementType;
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
		
		MapType type = tf.mapType(keyType, valueType);
		IMapWriter w = type.writer(vf);
		w.putAll(result);
		
		return result(type, w.done());
	}

	
	@Override
	public EvalResult visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		// TODO: shouldn't this be a closure?
		EvalResult r = result();
		for(Statement stat : x.getStatements()){
			r = stat.accept(this);
		}
		return r;
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

		Type annoType = tf.getAnnotationType(expr.type, name);

		if (annoType == null) {
			throw new RascalTypeError("No annotation " + x.getName()
					+ " declared on " + expr.type);
		}

		IValue annoValue = expr.value.getAnnotation(name);
		
		if (annoValue == null) {
			// TODO: make this a Rascal exception that can be caught by the programmer
			throw new RascalTypeError("This " + expr.type + " does not have a " + name + " annotation set");
		}
		return result(annoType, annoValue);
	}
	
    @Override
	public EvalResult visitExpressionAddition(Addition x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		Type resultType = left.type.lub(right.type);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).add((IInteger) right.value));
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).add((IDouble) right.value));
		} else if (left.type.isStringType() && right.type.isStringType()) {
			return result(vf.string(((IString) left.value).getValue()
					+ ((IString) right.value).getValue()));
		} else if (left.type.isListType() && right.type.isListType()) {
			
			return result(resultType, ((IList) left.value)
					.concat((IList) right.value));
		} else if (left.type.isSetType() && right.type.isSetType()) {
				return result(resultType, ((ISet) left.value)
						.union((ISet) right.value));
		} else if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)              //TODO: is this the right operation?
					.join((IMap) right.value));
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
				return result(resultType, ((ISet) left.value)
						.union((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of + have illegal types: "
					+ left.type + ", " + right.type);
		}
	}
    
	public EvalResult visitExpressionSubtraction(Subtraction x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		Type resultType = left.type.lub(right.type);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).subtract((IInteger) right.value));
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).subtract((IDouble) right.value));
		} else if (left.type.isListType() && right.type.isListType()) {
			notImplemented("- on list");
		} else if (left.type.isSetType() && right.type.isSetType()) {
				return result(resultType, ((ISet) left.value)
					.subtract((ISet) right.value));
		} else if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)
					.remove((IMap) right.value));
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			return result(resultType, ((ISet) left.value)
					.subtract((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of - have illegal types: "
					+ left.type + ", " + right.type);
		}
		return result();
	}
	
	@Override
	public EvalResult visitExpressionNegative(Negative x) {
		EvalResult arg = x.getArgument().accept(this);
		if (arg.type.isIntegerType()) {
			return result(vf.integer(- ((IInteger) arg.value).getValue()));
		}
			else	if (arg.type.isDoubleType()) {
				return result(vf.dubble(- ((IDouble) arg.value).getValue()));
		} else {
			throw new RascalTypeError(
					"Operand of unary - should be integer or double instead of: " + arg.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionProduct(Product x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).multiply((IInteger) right.value));
		} 
		else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).multiply((IDouble) right.value));
		}
		else if (left.type.isDoubleType() && right.type.isIntegerType()) {
			return result(((IDouble) left.value).multiply(((IInteger) right.value).toDouble()));
		}
		else if (left.type.isIntegerType() && right.type.isDoubleType()) {
			return result(((IInteger) left.value).toDouble().multiply((IDouble) right.value));
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

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(((IInteger) left.value).divide((IInteger) right.value));
		} 
		else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(((IDouble) left.value).divide((IDouble) right.value));
		}
		else if (left.type.isDoubleType() && right.type.isIntegerType()) {
			return result(((IDouble) left.value).divide(((IInteger) right.value).toDouble()));
		}
		else if (left.type.isIntegerType() && right.type.isDoubleType()) {
			return result(((IInteger) left.value).toDouble().divide((IDouble) right.value));
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

		if (left.type.isSetType() && right.type.isSetType()) {
				return result(resultType, ((ISet) left.value)
				.intersect((ISet) right.value));
		} else if (left.type.isMapType() && right.type.isMapType()) {
			return result(resultType, ((IMap) left.value)
					.common((IMap) right.value));

		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			return result(resultType, ((ISet) left.value)
				.intersect((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of & have illegal types: "
					+ left.type + ", " + right.type);
		}
	}

	@Override
	public EvalResult visitExpressionOr(Or x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		if (left.type.isBoolType() && right.type.isBoolType()) {
			return result(((IBool) left.value).or((IBool) right.value));
		} else {
			throw new RascalTypeError(
					"Operands of || should be boolean instead of: " + left.type
							+ ", " + right.type);
		}
	}

	@Override
	public EvalResult visitExpressionAnd(And x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		if (left.type.isBoolType() && right.type.isBoolType()) {
			return result(((IBool) left.value).and((IBool) right.value));
		} else {
			throw new RascalTypeError(
					"Operands of && should be boolean instead of: " + left.type
							+ ", " + right.type);
		}
	}

	@Override
	public EvalResult visitExpressionNegation(Negation x) {
		EvalResult arg = x.getArgument().accept(this);
		if (arg.type.isBoolType()) {
			return result(((IBool) arg.value).not());
		} else {
			throw new RascalTypeError(
					"Operand of ! should be boolean instead of: " + arg.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionImplication(Implication x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		if(left.type.isBoolType() && right.type.isBoolType()){
			if(left.value.equals(vf.bool(true)) &&
			   right.value.equals(vf.bool(false))){
				return result(vf.bool(false));
			} else {
				return result(vf.bool(true));
			}
		} else {
			throw new RascalTypeError(
					"Operands of ==> should be boolean instead of: " + left.type + ", " + right.type);
		}
	}
	
	@Override
	public EvalResult visitExpressionEquivalence(Equivalence x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		if(left.type.isBoolType() && right.type.isBoolType()){
			if(left.value.equals(right.value)) {
				return result(vf.bool(true));
			} else {
				return result(vf.bool(false));
			}
		} else {
			throw new RascalTypeError(
					"Operands of <==> should be boolean instead of: " + left.type + ", " + right.type);
		}
	}
	
	private boolean equals(EvalResult left, EvalResult right){
		if (left.type.isSubtypeOf(right.type)
				|| right.type.isSubtypeOf(left.type)) {
			return left.value.equals(right.value);
		} else {
			throw new RascalTypeError(
					"Operands of == should have equal types instead of: "
							+ left.type + ", " + right.type);
		}
	}

	@Override
	public EvalResult visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(equals(left, right)));
	}

	@Override
	public EvalResult visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		EvalResult left = x.getLhs().accept(this);
		EvalResult right = x.getRhs().accept(this);
		
		return result(vf.bool(!equals(left, right)));
	}
	
	private int compare(EvalResult left, EvalResult right){
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return ((IInteger) left.value).compare((IInteger) right.value);
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return ((IDouble) left.value).compare((IDouble) right.value);
		} else if (left.type.isStringType() && right.type.isStringType()) {
			return ((IString) left.value).compare((IString) right.value);
		} else if (left.type.isListType() && right.type.isListType()) {
			return compareList((IList) left.value, (IList) right.value);
		} else if (left.type.isSetType() && right.type.isSetType()) {
			return compareSet((ISet) left.value, (ISet) right.value);
		} else if (left.type.isMapType() && right.type.isMapType()) {
			return compareMap((IMap) left.value, (IMap) right.value);
		} else if (left.type.isTupleType() && right.type.isTupleType()) {
			notImplemented("compare for tuples");
			return 0;
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			return compareSet((ISet) left.value, (ISet) right.value);
		} else {
			throw new RascalTypeError("Operands of comparison have different types: "
					+ left.type + ", " + right.type);
		}
	}
	
	private int compareSet(ISet value, ISet value2) {
		if (value.equals(value2)) {
			return 0;
		}
		else if (value.isSubSet(value2)) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	private int compareMap(IMap value, IMap value2) {
		if (value.equals(value2)) {
			return 0;
		}
		else if (value.isSubMap(value2)) {
			return -1;
		}
		else {
			return 1;
		}
	}

	private int compareList(IList l, IList r){
		int ll = l.length();
		int rl = r.length();
		
		if(ll == 0){
			return rl == 0 ? 0 : -1;
		}
		if(rl == 0){
			return 1;
		}
		int m = (ll > rl) ? rl : ll;  
		int compare = 0;
		
		for(int i = 0; i < m; i++){
			EvalResult vl = result(l.get(i));
			EvalResult vr = result(r.get(i));
			int c = compare(vl, vr);

			if(compare == c){
				continue;
			} else
				if(i == 0){
					compare = c;
				} else {
						return c;
				}
		}
		
		if(compare == 0 && ll != rl){
			compare = ll < rl ? -1 : 1;
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
			if (cval.value.equals(vf.bool(true))) {
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
			EvalResult res = x.getLhs().accept(this);
			return res;
		} catch (Exception e) {   //TODO: make this more restrictive
			EvalResult res = x.getRhs().accept(this);
			return res;
		}
	}
	
	private boolean in(org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Expression expression2){
		EvalResult left = expression.accept(this);
		EvalResult right = expression2.accept(this);
		
		if(right.type.isListType() &&
		    left.type.isSubtypeOf(((ListType) right.type).getElementType())){
			IList lst = (IList) right.value;
			IValue val = left.value;
			for(int i = 0; i < lst.length(); i++){
				if(lst.get(i).equals(val))
					return true;
			}
			return false;
		} else if(right.type.isSetType() && 
				   left.type.isSubtypeOf(((SetType) right.type).getElementType())){
			return ((ISet) right.value).contains(left.value);
			
		} else if(right.type.isMapType() && left.type.isSubtypeOf(((MapType) right.type).getValueType())){
			return ((IMap) right.value).containsValue(left.value);
		} else if(right.type.isRelationType() && left.type.isSubtypeOf(((SetType) right.type).getElementType())){
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
	
		if(left.type.isRelationType() && 
			right.type.isRelationType()){
			RelationType leftrelType = (RelationType) left.type; 

			RelationType rightrelType = (RelationType) right.type;
			

			// ALARM: not using declared types
			if (leftrelType.getArity() == 2
					&& rightrelType.getArity() == 2
					&& leftrelType.getFieldType(1).equals(
							rightrelType.getFieldType(0))) {
				RelationType resultType = leftrelType.compose(rightrelType);
				return result(resultType, ((IRelation) left.value)
						.compose((IRelation) right.value));
			}
			if(((IRelation)left.value).size() == 0)
				return left;
			if(((IRelation)right.value).size() == 0)
				return right;
		}
		else if (left.type.isSetType() && ((SetType) left.type).getElementType().isVoidType()) {
			return left;
		}
		else if (right.type.isSetType() && ((SetType) right.type).getElementType().isVoidType()) {
			return right;
		}
		
		throw new RascalTypeError("Operands of o have wrong types: "
				+ left.type + ", " + right.type);
	}

	private EvalResult closure(EvalResult arg, boolean reflexive) {

		if (arg.type.isRelationType()) {
			RelationType relType = (RelationType) arg.type;
			Type fieldType1 = relType.getFieldType(0);
			Type fieldType2 = relType.getFieldType(1);
			String fieldName1 = relType.getFieldName(0);
			String fieldName2 = relType.getFieldName(1);
			
			if (relType.getArity() == 2
					&& (fieldType1.isSubtypeOf(fieldType2)
							|| fieldType2.isSubtypeOf(fieldType1))) {
				Type lub = fieldType1.lub(fieldType2);
				
				RelationType resultType = fieldName1 != null ? tf.relType(lub, fieldName1, lub, fieldName2) : tf.relType(lub,lub);
				return result(resultType, reflexive ? ((IRelation) arg.value).closureStar()
						: ((IRelation) arg.value).closure());
			}
		}
		else if (arg.type.isSetType() && ((SetType) arg.type).getElementType().isVoidType()) {
			return arg;
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

	//TODO: parameterize with an indicator for list/set/map generators;
	
	private class GeneratorEvaluator {
		private boolean isValueProducer;
		private boolean firstTime = true;
		private org.meta_environment.rascal.ast.Expression expr;
		private org.meta_environment.rascal.ast.Expression pat;
		private org.meta_environment.rascal.ast.Expression patexpr;
		private Evaluator evaluator;
		private Iterator iter;
		private Type elementType;

		GeneratorEvaluator(Generator g, Evaluator ev){
			evaluator = ev;
			if(g.isProducer()){
				isValueProducer = true;
				ValueProducer vp = g.getProducer();
				pat = vp.getPattern();
				patexpr = vp.getExpression();
				EvalResult r = patexpr.accept(ev);
				if(r.type.isListType()){
					elementType = ((ListType) r.type).getElementType();
					iter = ((IList) r.value).iterator();
				} else 	if(r.type.isSetType()){
					elementType = ((SetType) r.type).getElementType();
					iter = ((ISet) r.value).iterator();
				// TODO: add more generator types here	in the future
				} else {
					throw new RascalTypeError("expression in generator should be of type list/set");
				}
			} else {
				isValueProducer = false;
				expr = g.getExpression();
			}
		}

		public boolean getNext(){
			if(isValueProducer){
				while(iter.hasNext()){
					if(evaluator.match(result(elementType, (IValue)iter.next()), pat)){
						return true;
					}
				}
				return false;
			} else {
				if(firstTime){
					/* Evaluate expression only once */
					firstTime = false;
					EvalResult v = expr.accept(evaluator);
					if(v.type.isBoolType()){
						return v.value.equals(vf.bool(true));
					} else {
						throw new RascalTypeError("Expression as generator should have type bool");
					}
				} else {
					return false;
				}
			}
		}
	}
	
	@Override
	public EvalResult visitComprehensionList(
			org.meta_environment.rascal.ast.Comprehension.List x) {
		org.meta_environment.rascal.ast.Expression resultExpr = x.getResult();
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		Type elementType = tf.voidType();
		Type resultType = tf.setType(elementType);
		IListWriter res = null;
		
		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while(i >= 0 && i < size){		
			if(gens[i].getNext()){
				if(i == size - 1){
					EvalResult r = resultExpr.accept(this);
					if(res == null){
						elementType = r.type.lub(elementType);
						resultType = tf.listType(elementType);
						res = resultType.writer(vf);
					}
					if(r.type.isSubtypeOf(elementType)){
						elementType = elementType.lub(r.type);	
						res.append(r.value);
					}  else {
							throw new RascalTypeError("Cannot add value of type " + r.type + " to list comprehension with element type " + elementType);
						}
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return (res == null) ? result(tf.listType(tf.voidType()), vf.list()) : 
			                     result(tf.listType(elementType), res.done());
	}
	
	@Override
	public EvalResult visitComprehensionSet(
			org.meta_environment.rascal.ast.Comprehension.Set x) {
		org.meta_environment.rascal.ast.Expression resultExpr = x.getResult();
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		Type elementType = tf.voidType();
		Type resultType = tf.setType(elementType);
		ISetWriter res = null;
		
		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while(i >= 0 && i < size){		
			if(gens[i].getNext()){
				if(i == size - 1){
					EvalResult r = resultExpr.accept(this);
					if(res == null){
						elementType = r.type.lub(elementType);
						resultType = tf.setType(elementType);
						res = resultType.writer(vf);
					}
					if(r.type.isSubtypeOf(elementType)){
						elementType = elementType.lub(r.type);	
						res.insert(r.value);
					}  else {
							throw new RascalTypeError("Cannot add value of type " + r.type + " to set comprehension with element type " + elementType);
						}
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return (res == null) ? result(tf.setType(tf.voidType()), vf.set()) : 
			                     result(tf.setType(elementType), res.done());
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
			if(gens[i].getNext()){
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

	private boolean regExpMatch(EvalResult subject, 
			org.meta_environment.rascal.ast.Expression pat){
		
		RegExpResult regExpResult = pat.accept(re);
		
		if(!subject.type.isStringType()){
			throw new RascalTypeError("Subject of =~ should have type string and not " + subject.type);
		}
		
		if(regExpResult.matches(((IString) subject.value).getValue())){
			Map<String,String> map = regExpResult.getBindings();
			//TODO: check that the names we are going to bind are uninitialized
			for(String name : map.keySet()){
				assignVariable(name, result(vf.string(map.get(name))));
			}
			return true;
		}
		return false;
	}

	@Override
	public EvalResult visitExpressionRegExpMatch(RegExpMatch x) {
		return result(vf.bool(regExpMatch(x.getLhs().accept(this), x.getRhs())));
	}
	
	@Override
	public EvalResult visitExpressionRegExpNoMatch(RegExpNoMatch x) {
		return result(vf.bool(!regExpMatch(x.getLhs().accept(this), x.getRhs())));
	}
}
