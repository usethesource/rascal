package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variable extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isUnInitialized() { return false; }
static public class UnInitialized extends Variable {
/** name:Name -> Variable {cons("UnInitialized")} */
	public UnInitialized(INode node, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableUnInitialized(this);
	}

	public boolean isUnInitialized() { return true; }

	public boolean hasName() { return true; }

private final org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }	
}
static public class Ambiguity extends Variable {
  private final java.util.List<org.meta_environment.rascal.ast.Variable> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Variable> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariableAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Expression getInitial() { throw new UnsupportedOperationException(); } public boolean hasInitial() { return false; }
public boolean isInitialized() { return false; }
static public class Initialized extends Variable {
/** name:Name "=" initial:Expression -> Variable {cons("Initialized")} */
	public Initialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression initial) {
		this.node = node;
		this.name = name;
		this.initial = initial;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableInitialized(this);
	}

	public boolean isInitialized() { return true; }

	public boolean hasName() { return true; }
	public boolean hasInitial() { return true; }

private final org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private final org.meta_environment.rascal.ast.Expression initial;
	public org.meta_environment.rascal.ast.Expression getInitial() { return initial; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}