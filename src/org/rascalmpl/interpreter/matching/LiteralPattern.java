package org.rascalmpl.interpreter.matching;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

public class LiteralPattern extends AbstractMatchingResult {
	private IValue literal;
	private boolean isPattern = false;
	
	public LiteralPattern(IEvaluatorContext ctx, IValue literal){
		super(ctx);
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
	
		if (isPattern && subject.getValue().getType().comparable(literal.getType())) {
			return subject.equals(makeResult(literal.getType(), literal, ctx)).isTrue();
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
