package org.rascalmpl.interpreter.matching;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public class VariableBecomesPattern extends AbstractMatchingResult {
	private IMatchingResult pat;
	private IMatchingResult var;

	public VariableBecomesPattern(IEvaluatorContext ctx, Expression x,
			IMatchingResult var, IMatchingResult pat){
		super(ctx, x);
		this.pat = pat;
		this.var = var;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		var.initMatch(subject);
		if (var.hasNext()) { 
			pat.initMatch(subject);
			hasNext = pat.hasNext();
		}
		else {
			hasNext = false;
		}
	}
	
	@Override
	public List<String> getVariables() {
		List<String> first = var.getVariables();
		List<String> second = pat.getVariables();
		List<String> vars = new ArrayList<String>(first.size() + second.size());
		vars.addAll(first);
		vars.addAll(second);
		return vars;
	}
	
	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean hasNext(){
		return hasNext && pat.hasNext() && var.hasNext();
	}

	@Override
	public boolean next() {
		if (!pat.next()) {
			return false;
		}
		return var.next();
	}
}