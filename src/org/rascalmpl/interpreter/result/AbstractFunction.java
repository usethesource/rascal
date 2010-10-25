package org.rascalmpl.interpreter.result;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

/**
 * Lambda implements the basic functionality of callable functions. Since functions are
 * values in Rascal, we need to implement an IValue interface. In this case, since function
 * types are sub-typed of Map types, we should therefore implement IMap. The IMap methods
 * are implemented in such a way such that a function value simulates an empty map if it
 * escapes beyond the reach of the Rascal interpreter. This is useless, but safe behavior.
 */
abstract public class AbstractFunction extends Result<IValue> implements IExternalValue {
	protected static final TypeFactory TF = TypeFactory.getInstance();
    
	protected final Environment declarationEnvironment;
    protected final Evaluator eval;
    
    protected FunctionType functionType;
	protected final boolean hasVarArgs;
	
	protected final static TypeStore hiddenStore = new TypeStore();

	protected final AbstractAST ast;
	protected IValueFactory vf;
	
	protected static int callNesting = 0;
	protected static boolean callTracing = false;
	protected static boolean soundCallTracing = false;
	
	// TODO: change arguments of these constructors to use EvaluatorContexts
	public AbstractFunction(AbstractAST ast, Evaluator eval, FunctionType functionType, boolean varargs, Environment env) {
		super(functionType, null, eval);
		this.ast = ast;
		this.functionType = functionType;
		this.eval = eval;
		this.hasVarArgs = varargs;
		this.declarationEnvironment = env;
		this.vf = eval.getValueFactory();
	}
	
	public static void setCallTracing(boolean value){
		callTracing = value;
	}
    
	public Type getFormals() {
		return functionType.getArgumentTypes();
	}

	public AbstractAST getAst() {
		return ast;
	}
	
	@Override
	public IValue getValue() {
		return this;
	}

	public Environment getEnv() {
		return declarationEnvironment;
	}
	
	public boolean match(Type actuals) {
		if (actuals.isSubtypeOf(getFormals())) {
			return true;
		}
		
		if (hasVarArgs) {
			return matchVarArgsFunction(actuals);
		}
		
		return false;
	}
	
	public boolean hasVarArgs() {
		return hasVarArgs;
	}
	
	public boolean isAmbiguous(AbstractFunction other) {
		return other.match(getFormals()) || match(other.getFormals());
	}
	
	private boolean matchVarArgsFunction(Type actuals) {
		int arity = getFormals().getArity();
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(getFormals().getFieldType(i))) {
				return false;
			}
		}
		
		if (i > actuals.getArity()) {
			return false;
		}

		Type elementType = getFormals().getFieldType(i).getElementType();

		for (; i < actuals.getArity(); i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(elementType)) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private void printNesting(StringBuilder b) {
		for (int i = 0; i < callNesting; i++) {
			b.append('>');
		}
	}
	
	protected void printHeader(StringBuilder b) {
		b.append(getReturnType());
		b.append(' ');
		b.append(getName());
		b.append('(');
		Type formals = getFormals();
		for (int i = 0; i < formals.getArity(); i++) {
			b.append(formals.getFieldType(i));
		    b.append(' ');
		    b.append(formals.getFieldName(i));
		    
		    if (i < formals.getArity() - 1) {
		    	b.append(',');
		    }
		}
		b.append(')');
	}

	
	protected void printStartTrace() {
		StringBuilder b = new StringBuilder();
		b.append("call  >");
		printNesting(b);
		printHeader(b);
		eval.getStdOut().println(b.toString());
		eval.getStdOut().flush();
		callNesting++;
	}

	protected void printEndTrace() {
		callNesting--;
		StringBuilder b = new StringBuilder();
		b.append("return>");
		printNesting(b);
		printHeader(b);
		eval.getStdOut().println(b);
		eval.getStdOut().flush();
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
		
		IListWriter list = vf.listWriter(lub);
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

	protected Type computeVarArgsActualTypes(Type[] actualTypes, Type formals) {
		Type actualTuple = TF.tupleType(actualTypes);
		if (actualTuple.isSubtypeOf(formals)) {
			// the argument is already provided as a list
			return actualTuple;
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
		for (int j = i; j < actualTypes.length; j++) {
			lub = lub.lub(actualTypes[j]);
		}
		
		types[i] = TF.listType(lub);
		labels[i] = formals.getFieldName(i);
		
		return TF.tupleType(types, labels);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	public boolean isEqual(IValue other) throws FactTypeUseException {
		return other == this;
	}

	public boolean isIdentical(IValue other) throws FactTypeUseException {
		return other == this;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> that) {
		return that.compareFunction(this);
	}

	@Override
	public <U extends IValue> Result<U> compareFunction(AbstractFunction that) {
		if (that == this) {
			return ResultFactory.makeResult(TF.integerType(), vf.integer(0), ctx);
		}
		
		int result;
		
		result = getName().compareTo(that.getName());
		
		if (result != 0) {
			return ResultFactory.makeResult(TF.integerType(), vf.integer(result), ctx);
		}
		
		result = getType().compareTo(that.getType());
		
		return ResultFactory.makeResult(TF.integerType(), vf.integer(result), ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeFunction(this);
	}
	
	@Override
	public AbstractFunction composeFunction(AbstractFunction that) {
		if (!getTypeFactory().tupleType(getReturnType()).isSubtypeOf(that.getFunctionType().getArgumentTypes())) {
			undefinedError("composition");
		}
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public String toString() {
		return getHeader() + ";";
	}
	
	public String getHeader(){
		String sep = "";
		String strFormals = "";
		for(Type tp : getFormals()){
			strFormals = strFormals + sep + tp;
			sep = ", ";
		}
		
		String name = getName();
		if (name == null) {
			name = "";
		}
		return getReturnType() + " " + name + "(" + strFormals + ")";
	}
	
	public FunctionType getFunctionType() {
		return (FunctionType) getType();
	}
	
	/* test if a function is of type T(T) for a given T */
	public boolean isTypePreserving() {
		Type t = getReturnType();
		return getFunctionType().equivalent(RascalTypeFactory.getInstance().functionType(t,t));
	}
	
	public String getName() {
		return "";
	}

	public Type getReturnType() {
		return functionType.getReturnType();
	}

	public Evaluator getEval() {
		return eval;
	}

	public static void setSoundCallTracing(boolean on) {
		soundCallTracing = on;
	}
}
