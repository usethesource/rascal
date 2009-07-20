package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.MatchPattern;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.uptr.Factory;

public abstract class AbstractPattern implements MatchPattern {
	protected IValue subject = null;
	protected Environment env = null;
	protected boolean initialized = false;
	protected boolean hasNext = true;
	protected TypeFactory tf = TypeFactory.getInstance();
	protected IValueFactory vf;
	protected EvaluatorContext ctx;
	protected Evaluator evaluator;
	
	public AbstractPattern(IValueFactory vf, EvaluatorContext ctx) {
		this.vf = vf;
		this.ctx = ctx;
		this.evaluator = ctx.getEvaluator();
	}
	
	public AbstractAST getAST(){
		return ctx.getCurrentAST();
	}
	
	public void initMatch(IValue subject, Environment env){
		this.subject = subject;
		this.env = env;
		this.initialized = true;
		this.hasNext = true;
	}
	
	public boolean mayMatch(Type subjectType, Environment env){
		return mayMatch(getType(env), subjectType);
	}
	
	protected void checkInitialized(){
		if(!initialized){
			throw new ImplementationError("hasNext or match called before initMatch");
		}
	}
	
	public boolean hasNext()
	{
		return initialized && hasNext;
	}
	
	public java.util.List<String> getVariables(){
		return new java.util.LinkedList<String>();
	}
	
	abstract public IValue toIValue(Environment env);
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<AbstractPattern> patChildren, Environment ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().next()){
				return false;
			}
		}
		return true;
	}

	abstract public Type getType(Environment env);

	abstract public boolean next();
	
	protected boolean mayMatch(Type small, Type large){
		if(small.equivalent(large))
			return true;

		if(small.isVoidType() || large.isVoidType())
			return false;

		if(small.isSubtypeOf(large) || large.isSubtypeOf(small))
			return true;

		if (small instanceof ConcreteSyntaxType && large instanceof ConcreteSyntaxType) {
			return small.equals(large);
		}
		
		if (small instanceof ConcreteSyntaxType) {
			return large.isSubtypeOf(Factory.Tree);
		}
		
		if (large instanceof ConcreteSyntaxType) {
			return small.isSubtypeOf(Factory.Tree);
		}
		
		if(small.isListType() && large.isListType() || 
				small.isSetType() && large.isSetType())
			return mayMatch(small.getElementType(),large.getElementType());
		if(small.isMapType() && large.isMapType())
			return mayMatch(small.getKeyType(), large.getKeyType()) &&
			mayMatch(small.getValueType(), large.getValueType());
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

}