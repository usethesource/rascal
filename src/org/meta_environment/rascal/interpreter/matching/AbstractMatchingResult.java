package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.types.ConcreteSyntaxType;
import org.meta_environment.uptr.Factory;

public abstract class AbstractMatchingResult extends AbstractBooleanResult implements IMatchingResult {
	protected Result<IValue> subject = null;
	
	public AbstractMatchingResult(IValueFactory vf, IEvaluatorContext ctx) {
		super(vf, ctx);
	}
	
	public AbstractAST getAST(){
		return ctx.getCurrentAST();
	}
	
	public void initMatch(Result<IValue> subject) {
		init();
		this.subject = subject;
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
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<IMatchingResult> iterator){
		while (iterator.hasNext()) {
			if (!iterator.next().next()){
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