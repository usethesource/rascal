package org.rascalmpl.parser.gtd.result.action;

public class VoidEnvironment implements IEnvironment{
	private final IEnvironment parent;
	
	public VoidEnvironment(IEnvironment parent){
		super();
		
		this.parent = parent;
	}
	
	public IEnvironment getParent(){
		return parent;
	}
	
	public boolean isEqual(IEnvironment environment){
		return false;
	}
	
	public boolean isRoot(){
		return false;
	}
	
	public IEnvironment split(){
		return this; // Don't split.
	}
}
