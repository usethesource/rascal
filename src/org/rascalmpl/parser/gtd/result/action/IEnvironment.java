package org.rascalmpl.parser.gtd.result.action;

public interface IEnvironment{
	IEnvironment getParent();
	
	boolean isRoot();
	
	boolean isEqual(IEnvironment environment);
	
	IEnvironment split();
}
