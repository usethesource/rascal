package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Label extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Label {
/**  -> Label {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLabelEmpty(this);
	}

	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Label {
  private final java.util.List<org.rascalmpl.ast.Label> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Label> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Label> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLabelAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Label {
/** name:Name ":" -> Label {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLabelDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}