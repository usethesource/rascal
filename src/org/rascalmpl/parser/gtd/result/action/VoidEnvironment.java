package org.rascalmpl.parser.gtd.result.action;

public class VoidEnvironment implements IEnvironment{
	
	public VoidEnvironment(){
		super();
	}
	
	public boolean isEqual(IEnvironment environment){
		return false;
	}
	
	public IEnvironment split(){
		return this; // Don't split.
	}
}
