package org.rascalmpl.parser.gtd.result.action;

public interface IEnvironment{
	IEnvironment getParent();
	
	boolean isEqual(IEnvironment environment);
}
