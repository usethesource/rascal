package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class Time {

	public Time(IValueFactory values){
	}
	
	public IValue getNanoTime(){
		return ValueFactory.getInstance().integer(System.nanoTime());
	}
	
	public IValue getMilliTime(){
		return ValueFactory.getInstance().integer(System.currentTimeMillis());
	}
	
}
