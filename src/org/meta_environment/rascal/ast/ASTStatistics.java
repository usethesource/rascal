package org.meta_environment.rascal.ast;

public class ASTStatistics {
	private int concreteFragmentCount = 0;
	private int concreteFragmentSize = 0;
	private int nestedMetaVariables = 0;

	public void setConcreteFragmentCount(int concreteFragmentCount) {
		this.concreteFragmentCount = concreteFragmentCount;
	}
	
	public void setConcreteFragmentSize(int concreteFragmentSize) {
		this.concreteFragmentSize = concreteFragmentSize;
	}
	
	public int getConcreteFragmentCount() {
		return concreteFragmentCount;
	}
	
	public int getConcreteFragmentSize() {
		return concreteFragmentSize;
	}
	
	public int getNestedMetaVariables() {
		return nestedMetaVariables;
	}
	
	public void setNestedMetaVariables(int nestedMetaVariables) {
		this.nestedMetaVariables = nestedMetaVariables;
	}
	
	@Override
	public String toString() {
		return "[fragment count: " + concreteFragmentCount + ", fragment size: " + concreteFragmentSize + ", variables: " + nestedMetaVariables +  "]";
	}
}
