package org.meta_environment.rascal.interpreter.env;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.control_exceptions.Failure;
import org.meta_environment.rascal.interpreter.control_exceptions.Return;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.MissingReturnError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnguardedFailError;


/**
 * TODO: find a more elegant solution for this, by implementing IValue we
 * get away with storing a Lambda as an IValue, but what happens when 
 * somebody actually starts interpreting this IValue as an IConstructor?
 */
public class Lambda extends Result<IValue> implements IValue {
    protected static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	protected static final TypeEvaluator TE = TypeEvaluator.getInstance();
	protected static final TypeFactory TF = TypeFactory.getInstance();
    
	private final Environment env;
    protected final Evaluator eval;
    
	protected final Type formals;
	
	public Type getFormals() {
		return formals;
	}

	protected final boolean hasVarArgs;
	private boolean isVoidFunction;
    
	protected final static TypeStore hiddenStore = new TypeStore();
    private final static Type FunctionType = TF.abstractDataType(hiddenStore, "Rascal.Function");
    private final static Type ClosureType = TF.constructor(hiddenStore, FunctionType, "Rascal.Function.Closure");
	protected final Type returnType;
	private final List<Statement> body;
	private final String name;
	
	protected final AbstractAST ast;
	
	public String getName() {
		return name;
	}

	public AbstractAST getAst() {
		return ast;
	}

	protected static int callNesting = 0;
	protected static boolean callTracing = false;
	
	
	public Lambda(AbstractAST ast, Evaluator eval, Type returnType, String name, Type formals, boolean varargs, 
				java.util.List<Statement> body, Environment env) {
		super(ClosureType, null /*VF.constructor(ClosureType)*/, ast);
		this.ast = ast;
		this.eval = eval;
		this.returnType = returnType;
		this.name = name;
		this.formals = formals;
		this.body = body;
		this.hasVarArgs = varargs;
		this.isVoidFunction = returnType.isSubtypeOf(TF.voidType());
		this.env = env;
	}
    
	@Override
	public Type getType() {
		return ClosureType;
	}

	@Override
	public IValue getValue() {
		// TODO: is this a hack?
		// Lambdas are results and IValues at the same time...
		return this;
	}

	public Environment getEnv() {
		return env;
	}
	
	public boolean match(Type actuals) {
		if (actuals.isSubtypeOf(formals)) {
			return true;
		}
		
		if (hasVarArgs) {
			return matchVarArgsFunction(actuals);
		}
		
		return false;
	}
	
	public boolean isAmbiguous(Lambda other) {
		return other.match(formals) || match(other.formals);
	}
	
	private boolean matchVarArgsFunction(Type actuals) {
		int arity = formals.getArity();
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(formals.getFieldType(i))) {
				return false;
			}
		}
		
		if (i > actuals.getArity()) {
			return false;
		}

		Type elementType = formals.getFieldType(i).getElementType();

		for (; i < actuals.getArity(); i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(elementType)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Type getClosureType() {
		return ClosureType;
	}
	
	public Result<IValue> call(IValue[] actuals, Type actualTypes, Environment env) {
		Map<Type,Type> bindings = env.getTypeBindings();
		Type instantiatedFormals = formals.instantiate(env.getStore(), bindings);
		
		if (callTracing) {
			printTrace();
			callNesting++;
		}

		if (hasVarArgs) {
			actualTypes = computeVarArgsActualTypes(actualTypes, instantiatedFormals);
		}

		try {
			bindTypeParameters(actualTypes, instantiatedFormals, env);

			if (hasVarArgs) {
				actuals = computeVarArgsActuals(actuals, formals);
			}

			assignFormals(actuals, env);

			for (Statement stat: body) {
				eval.setCurrentAST(stat);
				stat.accept(eval);
			}
			

			if(!isVoidFunction){
				throw new MissingReturnError(ast);
			}

			return ResultFactory.makeResult(TF.voidType(), null, ast);
		}
		catch (Return e) {
			Result<IValue> result = e.getValue();

			Type instantiatedReturnType = returnType.instantiate(env.getStore(), env.getTypeBindings());

			if(!result.getType().isSubtypeOf(instantiatedReturnType)){
				throw new UnexpectedTypeError(returnType, result.getType(), ast);
			}

			return ResultFactory.makeResult(instantiatedReturnType, result.getValue(), ast);
		} 
		catch (Failure e) {
			throw new UnguardedFailError(ast);
		}
		finally {
			if (callTracing) {
				callNesting--;
			}
		}
	}

	protected void printTrace() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < callNesting; i++) {
			b.append('>');
		}
		System.out.println("trace>" + b + " " + getName() + "(" + formals + ")");
	}

	private void assignFormals(IValue[] actuals, Environment env) {
		for (int i = 0; i < formals.getArity(); i++) {
			Type formal = formals.getFieldType(i).instantiate(env.getStore(), env.getTypeBindings());
			
			Result<IValue> result;
			if (actuals[i] instanceof Lambda) {
				result = (Lambda)actuals[i];
			}
			else {	
				result = ResultFactory.makeResult(formal, actuals[i], ast);
			}
//			System.out.println(i + ": Formal " + formals.getFieldName(i) + " actual " + actuals[i]);
			env.storeInnermostVariable(formals.getFieldName(i), result);
		}
	}


	protected void bindTypeParameters(Type actualTypes, Type formals, Environment env) {
		try {
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			formals.match(actualTypes, bindings);
			env.storeTypeBindings(bindings);
		}
		catch (FactTypeUseException e) {
			throw new UnexpectedTypeError(formals, actualTypes, ast);
		}
	}	
	
	protected IValue[] computeVarArgsActuals(IValue[] actuals, Type formals) {
		int arity = formals.getArity();
		IValue[] newActuals = new IValue[arity];
		int i;
		
		if (formals.getArity() == actuals.length && actuals[actuals.length - 1].getType().isSubtypeOf(formals.getFieldType(formals.getArity() - 1))) {
			// variable length argument is provided as a list
			return actuals;
		}

		for (i = 0; i < arity - 1; i++) {
			newActuals[i] = actuals[i];
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actuals.length; j++) {
			lub = lub.lub(actuals[j].getType());
		}
		
		IListWriter list = VF.listWriter(lub);
		list.insertAt(0, actuals, i, actuals.length - arity + 1);
		newActuals[i] = list.done();
		return newActuals;
	}

	protected Type computeVarArgsActualTypes(Type actualTypes, Type formals) {
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
		
		Type lub = TF.voidType();
		for (int j = i; j < actualTypes.getArity(); j++) {
			lub = lub.lub(actualTypes.getFieldType(j));
		}
		
		types[i] = TF.listType(lub);
		labels[i] = formals.getFieldName(i);
		
		return TF.tupleType(types, labels);
	}
	
//	private StringBuffer showCall(FunctionDeclaration func, IValue[] actuals, String arrow){
//		StringBuffer trace = new StringBuffer();
//		for(int i = 0; i < callNesting; i++){
//			trace.append("-");
//		}
//		trace.append(arrow).append(" ").append(func.getSignature().getName()).append("(");
//		String sep = "";
//		for(int i = 0; i < actuals.length; i++){
//			trace.append(sep).append(actuals[i]);
//			sep = ", ";
//		}
//		trace.append(")");
//		return trace;
//	}

	
//	@Override
//	public boolean isNormal() {
//		return false;
//	}
//	
//	@Override
//	public boolean isClosure() {
//		return true;
//	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		throw new UnsupportedOperationException();
	}

	public IValue getAnnotation(String label) {
		throw new UnsupportedOperationException();
	}

	public boolean hasAnnotation(String label) {
		return false;
	}

	public boolean isEqual(IValue other) throws FactTypeUseException {
		return other == this;
	}

	public boolean isIdentical(IValue other) throws FactTypeUseException {
		return other == this;
	}

	public IValue setAnnotation(String label, IValue value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getHeader() + " {");
		for (Statement s : body) {
			b.append("  " + s.toString() + "\n");
		}
		b.append("}\n");
		return b.toString();
	}
	
	public String getHeader(){
		String sep = "";
		String strFormals = "";
		for(Type tp : formals){
			strFormals = strFormals + sep + tp;
			sep = ", ";
		}
		return returnType + " " + name + "(" + strFormals + ")";
	}
}
