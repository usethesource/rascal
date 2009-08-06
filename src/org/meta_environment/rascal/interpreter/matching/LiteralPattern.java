package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class LiteralPattern extends AbstractMatchingResult {
	private IValue literal;
	private boolean isPattern = false;
	
	public LiteralPattern(IValueFactory vf, IEvaluatorContext ctx, IValue literal){
		super(vf, ctx);
		this.literal = literal;
	}
	
	@Override
	public Type getType(Environment env) {
			return literal.getType();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		isPattern = true;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
	
		if (isPattern && subject.getType().comparable(literal.getType())) {
			return subject.equals(makeResult(literal.getType(), literal, ctx), ctx).isTrue();
		}
		else if (!isPattern) {
			if (literal.getType().isBoolType()) {
				return ((IBool) literal).getValue(); 
			}
			
			throw new UnexpectedTypeError(tf.boolType(), literal.getType(), ctx.getCurrentAST());
		}
		
		
		return false;
	}
	
	@Override
	public IValue toIValue(Environment env){
		return literal;
	}
	
	@Override
	public String toString(){
		return "pattern: " + literal;
	}
	
	
}
