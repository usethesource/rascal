package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Start extends AbstractAST { 
  public boolean isAbsent() { return false; }
static public class Absent extends Start {
/**  -> Start {cons("Absent")} */
	public Absent(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStartAbsent(this);
	}

	public boolean isAbsent() { return true; }	
}
static public class Ambiguity extends Start {
  private final java.util.List<org.rascalmpl.ast.Start> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Start> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Start> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStartAmbiguity(this);
  }
} 
public boolean isPresent() { return false; }
static public class Present extends Start {
/** "start" -> Start {cons("Present")} */
	public Present(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStartPresent(this);
	}

	public boolean isPresent() { return true; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}