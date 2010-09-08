package org.rascalmpl.interpreter.matching;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

public class LiteralPattern extends AbstractMatchingResult {
	private IValue literal;
	
	public LiteralPattern(IEvaluatorContext ctx, AbstractAST x, IValue literal){
		super(ctx, x);
		this.literal = literal;
	}
	
	@Override
	public List<String> getVariables() {
		return Collections.emptyList();
	}
	
	@Override
	public Type getType(Environment env) {
			return literal.getType();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		if (subject.getValue().getType().comparable(literal.getType())) {
			hasNext = subject.equals(makeResult(literal.getType(), literal, ctx)).isTrue();
		}
		else {
			hasNext = false;
		}
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if (!hasNext) {
			return false;
		}
		else {
			hasNext = false;
			return true;
		}
	}
	
	public IValue toIValue(Environment env){
		return literal;
	}
	
	@Override
	public String toString(){
		return "pattern: " + literal;
	}
	
	
}
