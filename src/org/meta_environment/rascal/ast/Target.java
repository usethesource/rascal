package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Target extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Target {
/**  -> Target {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTargetEmpty(this);
	}

	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Target {
  private final java.util.List<org.meta_environment.rascal.ast.Target> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Target> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Target> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTargetAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Target {
/** name:Name -> Target {cons("Labeled")} */
	public Labeled(INode node, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTargetLabeled(this);
	}

	public boolean isLabeled() { return true; }

	public boolean hasName() { return true; }

private final org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}