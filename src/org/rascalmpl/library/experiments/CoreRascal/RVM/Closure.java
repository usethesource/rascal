package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Closure {

	final Function function;
	final Frame frame;
	
	Closure(Function function, Frame frame){
		this.function = function;
		this.frame = frame;
	}

}
