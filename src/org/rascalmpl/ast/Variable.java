package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variable extends AbstractAST { 
  public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isUnInitialized() { return false; }
static public class UnInitialized extends Variable {
/** name:Name -> Variable {cons("UnInitialized")} */
	public UnInitialized(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableUnInitialized(this);
	}

	@Override
	public boolean isUnInitialized() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }	
}
static public class Ambiguity extends Variable {
  private final java.util.List<org.rascalmpl.ast.Variable> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Variable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Variable> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariableAmbiguity(this);
  }
} public org.rascalmpl.ast.Expression getInitial() { throw new UnsupportedOperationException(); } public boolean hasInitial() { return false; }
public boolean isInitialized() { return false; }
static public class Initialized extends Variable {
/** name:Name "=" initial:Expression -> Variable {cons("Initialized")} */
	public Initialized(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression initial) {
		this.node = node;
		this.name = name;
		this.initial = initial;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableInitialized(this);
	}

	@Override
	public boolean isInitialized() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasInitial() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Expression initial;
	@Override
	public org.rascalmpl.ast.Expression getInitial() { return initial; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}