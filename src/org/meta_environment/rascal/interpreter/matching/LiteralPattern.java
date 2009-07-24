package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class LiteralPattern extends AbstractMatchingResult {

	private IValue literal;
	
	LiteralPattern(IValueFactory vf, EvaluatorContext ctx, IValue literal){
		super(vf, ctx);
		this.literal = literal;
	}
	
	@Override
	public Type getType(Environment env) {
			return literal.getType();
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if (subject.getType().comparable(literal.getType())) {
			return subject.equals(makeResult(literal.getType(), literal, ctx), ctx).isTrue();
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
