package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;

public class DescendantPattern extends AbstractMatchingResult  {
	private IMatchingResult pat;
	private Iterator<?> iterator;

	public DescendantPattern(IEvaluatorContext ctx, Expression.Descendant x, IMatchingResult pat) {
		super(ctx, x);
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return TypeFactory.getInstance().valueType();
		// TODO: return pat.getType(env) is too restrictive, reconsider this.
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return ctx.getEvaluator().mayOccurIn(getType(env), subjectType);
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		iterator = IteratorFactory.make(ctx, pat, subject, false);
		hasNext = true;
	}
	
	@Override
	public boolean hasNext(){
		if(!initialized)
			return false;
		
		if(hasNext){
			boolean hn = pat.hasNext() || iterator.hasNext();
			if(!hn){
				hasNext = false;
			}
			return hn;
		}
		return false;
	}
	
	@Override
	public boolean next() {
		/*
		 * First, explore alternatives that remain to be matched by the current pattern
		 */
		while(pat.hasNext()){
			if(pat.next()){
				return true;
			}
		}
		/*
		 * Next, fetch a new data element (if any) and create a new pattern.
		 */
		while(iterator.hasNext()){
			IValue v = (IValue) iterator.next();
			
			// TODO: extract the proper static element type that will be generated
			pat.initMatch(ResultFactory.makeResult(v.getType(), v, ctx));
			while(pat.hasNext()){
				if(pat.next()){
					return true;						
				}	
			}
		}
		hasNext = false;
		return false;
	}
}
