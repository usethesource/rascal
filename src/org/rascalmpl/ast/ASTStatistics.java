package org.rascalmpl.ast;

public class ASTStatistics implements Comparable<ASTStatistics> {
	private int concreteFragmentCount = 0;
	private int concreteFragmentSize = 0;
	private int nestedMetaVariables = 0;
	private int injections = 0;
	private boolean ambiguous = false;

	public boolean isAmbiguous() {
		return ambiguous;
	}
	
	public void setAmbiguous(boolean ambiguous) {
		this.ambiguous = ambiguous;
	}
	
	public int getInjections() {
		return injections;
	}
	
	public void setInjections(int injections) {
		this.injections = injections;
	}
	
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
	
	public void add(ASTStatistics other) {
		this.concreteFragmentCount += other.concreteFragmentCount;
		this.concreteFragmentSize += other.concreteFragmentSize;
		this.injections += other.injections;
		this.nestedMetaVariables += other.nestedMetaVariables;
		this.ambiguous = this.ambiguous || other.ambiguous;
	}
	
	@Override
	public String toString() {
		return "[fragment count: " + concreteFragmentCount + ", fragment size: " + concreteFragmentSize + ", variables: " + nestedMetaVariables + ", injections: " + injections +  "]";
	}

	public int compareTo(ASTStatistics other) {
		return compareFragmentSize(other);
	}
	
	private int compareFragmentSize(ASTStatistics other) {
		if (concreteFragmentSize < other.concreteFragmentSize) {
			return -1;
		}
		else if (concreteFragmentSize == other.concreteFragmentSize) {
			return compareNestedMetaVariables(other);
		}
	
		return 1;
	}

	private int compareNestedMetaVariables(ASTStatistics other) {
		if (nestedMetaVariables > other.nestedMetaVariables) {
			return -1;
		}
		else if (nestedMetaVariables == other.nestedMetaVariables) {
			return compareInjections(other);
		}
		else {
			return 1;
		}
	}
	
	private int compareInjections(ASTStatistics other) {
		if (injections < other.injections) {
			return -1;
		}
		else if (injections == other.injections) {
			return 0;
		}
		else {
			return 1;
		}
	}
}
