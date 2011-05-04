package org.rascalmpl.parser.gtd.result.action;

public interface IEnvironment{
	IEnvironment split();
	
	boolean isEqual(IEnvironment environment);
}
