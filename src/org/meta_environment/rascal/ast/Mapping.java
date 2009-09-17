package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Mapping extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getFrom() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getTo() { throw new UnsupportedOperationException(); } public boolean hasFrom() { return false; } public boolean hasTo() { return false; } public boolean isDefault() { return false; } static public class Default extends Mapping {
/** from:Expression ":" to:Expression -> Mapping {cons("Default")} */
	public Default(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to) {
		this.node = node;
		this.from = from;
		this.to = to;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitMappingDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasFrom() { return true; }
	public boolean hasTo() { return true; }

private final org.meta_environment.rascal.ast.Expression from;
	public org.meta_environment.rascal.ast.Expression getFrom() { return from; }
	private final org.meta_environment.rascal.ast.Expression to;
	public org.meta_environment.rascal.ast.Expression getTo() { return to; }	
} static public class Ambiguity extends Mapping {
  private final java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Mapping> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitMappingAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}