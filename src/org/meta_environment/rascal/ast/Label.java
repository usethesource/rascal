package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Label extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Label {
/*  -> Label {cons("Empty")} */
	private Empty() { }
	/*package*/ Empty(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLabelEmpty(this);
	}

	@Override
	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Label {
  private final java.util.List<org.meta_environment.rascal.ast.Label> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Label> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Label> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitLabelAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Label {
/* name:Name ":" -> Label {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, org.meta_environment.rascal.ast.Name name) {
		this.tree = tree;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLabelDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Name name;
	@Override
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Default setName(org.meta_environment.rascal.ast.Name x) { 
		Default z = new Default();
 		z.$setName(x);
		return z;
	}	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}