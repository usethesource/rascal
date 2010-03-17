package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Target extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Target {
/**  -> Target {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTargetEmpty(this);
	}

	@Override
	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Target {
  private final java.util.List<org.rascalmpl.ast.Target> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Target> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Target> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitTargetAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Target {
/** name:Name -> Target {cons("Labeled")} */
	public Labeled(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTargetLabeled(this);
	}

	@Override
	public boolean isLabeled() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}