package org.meta_environment.rascal.interpreter.env;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;

public class IterableEvalResult extends EvalResult implements Iterator<EvalResult>{
	Iterator<EvalResult> iterator;
	private EvalResult last;
	
	IterableEvalResult(Iterator<EvalResult> beval){
		super(TypeFactory.getInstance().boolType(), null);
		this.iterator = beval;
	}
	
	public IterableEvalResult(Iterator<EvalResult> beval, boolean b){
		super(TypeFactory.getInstance().boolType(), ValueFactory.getInstance().bool(b));
		this.iterator = beval;
	}
	
	public boolean isTrue(){
		return (last == null) || last.isTrue(); //TODO is this ok?
	}
	
	@Override
	public boolean hasNext(){
		return iterator.hasNext();
	}
	
	@Override
	public EvalResult next(){
		return last = iterator.next();
	}

	public void remove() {
		throw new RascalBug("remove() not implemented for IterableEvalResult");
		
	}
}