package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class DataTarget extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends DataTarget {
/**  -> DataTarget {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDataTargetEmpty(this);
	}

	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends DataTarget {
  private final java.util.List<org.rascalmpl.ast.DataTarget> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DataTarget> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.DataTarget> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDataTargetAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Name getLabel() { throw new UnsupportedOperationException(); }
public boolean hasLabel() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends DataTarget {
/** label:Name ":" -> DataTarget {cons("Labeled")} */
	public Labeled(INode node, org.rascalmpl.ast.Name label) {
		this.node = node;
		this.label = label;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDataTargetLabeled(this);
	}

	public boolean isLabeled() { return true; }

	public boolean hasLabel() { return true; }

private final org.rascalmpl.ast.Name label;
	public org.rascalmpl.ast.Name getLabel() { return label; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}