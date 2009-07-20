package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;

/* package */ class MapPattern extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children;
	
	MapPattern(IValueFactory vf, EvaluatorContext ctx, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.children = children;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < children.size(); i++) {
			res.addAll(children.get(i).getVariables());
		 }
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(env);
		 }
		return null; //TODO: make correct
	}
	
	@Override
	public Type getType(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		throw new ImplementationError("AbstractPatternMap.match not implemented");
	}
}