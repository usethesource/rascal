package org.rascalmpl.parser.gtd.result.action;

public class VoidEnvironment implements IEnvironment{
	public final static VoidEnvironment ROOT_VOID_ENVIRONMENT = new VoidEnvironment();
	
	public VoidEnvironment(){
		super();
	}
	
	public IEnvironment getParent(){
		return this;
	}
	
	public boolean isEqual(IEnvironment environment){
		return false;
	}
}
