package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variable extends AbstractAST { 
  public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isUnInitialized() { return false; }
static public class UnInitialized extends Variable {
/** name:Name -> Variable {cons("UnInitialized")} */
	protected UnInitialized(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableUnInitialized(this);
	}

	public boolean isUnInitialized() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }	
}
static public class Ambiguity extends Variable {
  private final java.util.List<org.rascalmpl.ast.Variable> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Variable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Variable> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariableAmbiguity(this);
  }
} public org.rascalmpl.ast.Expression getInitial() { throw new UnsupportedOperationException(); } public boolean hasInitial() { return false; }
public boolean isInitialized() { return false; }
static public class Initialized extends Variable {
/** name:Name "=" initial:Expression -> Variable {cons("Initialized")} */
	protected Initialized(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression initial) {
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

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Expression initial;
	public org.rascalmpl.ast.Expression getInitial() { return initial; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}