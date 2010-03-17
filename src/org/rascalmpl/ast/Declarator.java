package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Declarator extends AbstractAST { 
  public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Variable> getVariables() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; } public boolean hasVariables() { return false; } public boolean isDefault() { return false; } static public class Default extends Declarator {
/** type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.Variable> variables) {
		this.node = node;
		this.type = type;
		this.variables = variables;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclaratorDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasType() { return true; }
	@Override
	public boolean hasVariables() { return true; }

private final org.rascalmpl.ast.Type type;
	@Override
	public org.rascalmpl.ast.Type getType() { return type; }
	private final java.util.List<org.rascalmpl.ast.Variable> variables;
	@Override
	public java.util.List<org.rascalmpl.ast.Variable> getVariables() { return variables; }	
} static public class Ambiguity extends Declarator {
  private final java.util.List<org.rascalmpl.ast.Declarator> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Declarator> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Declarator> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitDeclaratorAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}