package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Declarator extends AbstractAST { 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasVariables() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Declarator {
/** type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) {
		this.node = node;
		this.type = type;
		this.variables = variables;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclaratorDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasType() { return true; }
	public boolean hasVariables() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public Default setType(org.meta_environment.rascal.ast.Type x) { 
		Default z = new Default();
 		z.$setType(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Variable> variables;
	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() { return variables; }
	private void $setVariables(java.util.List<org.meta_environment.rascal.ast.Variable> x) { this.variables = x; }
	public Default setVariables(java.util.List<org.meta_environment.rascal.ast.Variable> x) { 
		Default z = new Default();
 		z.$setVariables(x);
		return z;
	}	
}
static public class Ambiguity extends Declarator {
  private final java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Declarator> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDeclaratorAmbiguity(this);
  }
}
}