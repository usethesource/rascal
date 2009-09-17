package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Parameters extends AbstractAST { 
  public org.meta_environment.rascal.ast.Formals getFormals() { throw new UnsupportedOperationException(); } public boolean hasFormals() { return false; } public boolean isDefault() { return false; }
static public class Default extends Parameters {
/** "(" formals:Formals ")" -> Parameters {cons("Default")} */
	public Default(INode node, org.meta_environment.rascal.ast.Formals formals) {
		this.node = node;
		this.formals = formals;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitParametersDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasFormals() { return true; }

private final org.meta_environment.rascal.ast.Formals formals;
	public org.meta_environment.rascal.ast.Formals getFormals() { return formals; }	
}
static public class Ambiguity extends Parameters {
  private final java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Parameters> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitParametersAmbiguity(this);
  }
} public boolean isVarArgs() { return false; }
static public class VarArgs extends Parameters {
/** "(" formals:Formals "..." ")" -> Parameters {cons("VarArgs")} */
	public VarArgs(INode node, org.meta_environment.rascal.ast.Formals formals) {
		this.node = node;
		this.formals = formals;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitParametersVarArgs(this);
	}

	public boolean isVarArgs() { return true; }

	public boolean hasFormals() { return true; }

private final org.meta_environment.rascal.ast.Formals formals;
	public org.meta_environment.rascal.ast.Formals getFormals() { return formals; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}