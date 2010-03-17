package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Renaming extends AbstractAST { 
public org.rascalmpl.ast.Name getFrom() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Name getTo() { throw new UnsupportedOperationException(); }
public boolean hasFrom() { return false; }
	public boolean hasTo() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Renaming {
/** from:Name "=>" to:Name -> Renaming {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Name from, org.rascalmpl.ast.Name to) {
		this.node = node;
		this.from = from;
		this.to = to;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRenamingDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasFrom() { return true; }
	@Override
	public boolean hasTo() { return true; }

private final org.rascalmpl.ast.Name from;
	@Override
	public org.rascalmpl.ast.Name getFrom() { return from; }
	private final org.rascalmpl.ast.Name to;
	@Override
	public org.rascalmpl.ast.Name getTo() { return to; }	
}
static public class Ambiguity extends Renaming {
  private final java.util.List<org.rascalmpl.ast.Renaming> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Renaming> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Renaming> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitRenamingAmbiguity(this);
  }
}
}