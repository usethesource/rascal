package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Fail extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getLabel() { throw new UnsupportedOperationException(); }
public boolean hasLabel() { return false; }
public boolean isWithLabel() { return false; }
static public class WithLabel extends Fail {
/* "fail" label:Name ";" -> Fail {cons("WithLabel")} */
	private WithLabel() { }
	/*package*/ WithLabel(INode node, org.meta_environment.rascal.ast.Name label) {
		this.node = node;
		this.label = label;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFailWithLabel(this);
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
static public class Ambiguity extends Fail {
  private final java.util.List<org.meta_environment.rascal.ast.Fail> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Fail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFailAmbiguity(this);
  }
} 
public boolean isNoLabel() { return false; }
static public class NoLabel extends Fail {
/* "fail" ";" -> Fail {cons("NoLabel")} */
	private NoLabel() { }
	/*package*/ NoLabel(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFailNoLabel(this);
	}

	public boolean isNoLabel() { return true; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}