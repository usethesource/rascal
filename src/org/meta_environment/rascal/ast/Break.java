package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Break extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getLabel() { throw new UnsupportedOperationException(); }
public boolean hasLabel() { return false; }
public boolean isWithLabel() { return false; }
static public class WithLabel extends Break {
/* "break" label:Name ";" -> Break {cons("WithLabel")} */
	private WithLabel() { }
	/*package*/ WithLabel(INode node, org.meta_environment.rascal.ast.Name label) {
		this.node = node;
		this.label = label;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBreakWithLabel(this);
	}

	public boolean isWithLabel() { return true; }

	public boolean hasLabel() { return true; }

private org.meta_environment.rascal.ast.Name label;
	public org.meta_environment.rascal.ast.Name getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Name x) { this.label = x; }
	public WithLabel setLabel(org.meta_environment.rascal.ast.Name x) { 
		WithLabel z = new WithLabel();
 		z.$setLabel(x);
		return z;
	}	
}
static public class Ambiguity extends Break {
  private final java.util.List<org.meta_environment.rascal.ast.Break> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Break> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Break> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBreakAmbiguity(this);
  }
} 
public boolean isNoLabel() { return false; }
static public class NoLabel extends Break {
/* "break" ";" -> Break {cons("NoLabel")} */
	private NoLabel() { }
	/*package*/ NoLabel(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBreakNoLabel(this);
	}

	public boolean isNoLabel() { return true; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}