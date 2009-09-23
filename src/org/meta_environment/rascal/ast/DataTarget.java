package org.meta_environment.rascal.ast; 
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
  private final java.util.List<org.meta_environment.rascal.ast.DataTarget> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DataTarget> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.DataTarget> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDataTargetAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Name getLabel() { throw new UnsupportedOperationException(); }
public boolean hasLabel() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends DataTarget {
/** label:Name ":" -> DataTarget {cons("Labeled")} */
	public Labeled(INode node, org.meta_environment.rascal.ast.Name label) {
		this.node = node;
		this.label = label;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDataTargetLabeled(this);
	}

	public boolean isLabeled() { return true; }

	public boolean hasLabel() { return true; }

private final org.meta_environment.rascal.ast.Name label;
	public org.meta_environment.rascal.ast.Name getLabel() { return label; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}