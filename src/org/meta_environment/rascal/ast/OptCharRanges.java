package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class OptCharRanges extends AbstractAST { 
  public boolean isAbsent() { return false; }
static public class Absent extends OptCharRanges {
/**  -> OptCharRanges {cons("Absent")} */
	private Absent() {
		super();
	}
	public Absent(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOptCharRangesAbsent(this);
	}

	public boolean isAbsent() { return true; }	
}
static public class Ambiguity extends OptCharRanges {
  private final java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.OptCharRanges> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitOptCharRangesAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.CharRanges getRanges() { throw new UnsupportedOperationException(); }
public boolean hasRanges() { return false; }
public boolean isPresent() { return false; }
static public class Present extends OptCharRanges {
/** ranges:CharRanges -> OptCharRanges {cons("Present")} */
	private Present() {
		super();
	}
	public Present(INode node, org.meta_environment.rascal.ast.CharRanges ranges) {
		this.node = node;
		this.ranges = ranges;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOptCharRangesPresent(this);
	}

	public boolean isPresent() { return true; }

	public boolean hasRanges() { return true; }

private org.meta_environment.rascal.ast.CharRanges ranges;
	public org.meta_environment.rascal.ast.CharRanges getRanges() { return ranges; }
	private void $setRanges(org.meta_environment.rascal.ast.CharRanges x) { this.ranges = x; }
	public Present setRanges(org.meta_environment.rascal.ast.CharRanges x) { 
		Present z = new Present();
 		z.$setRanges(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}