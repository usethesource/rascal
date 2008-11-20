package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Parameters extends AbstractAST { 
  public org.meta_environment.rascal.ast.Formals getFormals() { throw new UnsupportedOperationException(); } public boolean hasFormals() { return false; } public boolean isDefault() { return false; }
static public class Default extends Parameters {
/* "(" formals:Formals ")" -> Parameters {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, org.meta_environment.rascal.ast.Formals formals) {
		this.tree = tree;
		this.formals = formals;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitParametersDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasFormals() { return true; }

private org.meta_environment.rascal.ast.Formals formals;
	@Override
	public org.meta_environment.rascal.ast.Formals getFormals() { return formals; }
	private void $setFormals(org.meta_environment.rascal.ast.Formals x) { this.formals = x; }
	public Default setFormals(org.meta_environment.rascal.ast.Formals x) { 
		Default z = new Default();
 		z.$setFormals(x);
		return z;
	}	
}
static public class Ambiguity extends Parameters {
  private final java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Parameters> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitParametersAmbiguity(this);
  }
} public boolean isVarArgs() { return false; }
static public class VarArgs extends Parameters {
/* "(" formals:Formals "..." ")" -> Parameters {cons("VarArgs")} */
	private VarArgs() { }
	/*package*/ VarArgs(ITree tree, org.meta_environment.rascal.ast.Formals formals) {
		this.tree = tree;
		this.formals = formals;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitParametersVarArgs(this);
	}

	@Override
	public boolean isVarArgs() { return true; }

	@Override
	public boolean hasFormals() { return true; }

private org.meta_environment.rascal.ast.Formals formals;
	@Override
	public org.meta_environment.rascal.ast.Formals getFormals() { return formals; }
	private void $setFormals(org.meta_environment.rascal.ast.Formals x) { this.formals = x; }
	public VarArgs setFormals(org.meta_environment.rascal.ast.Formals x) { 
		VarArgs z = new VarArgs();
 		z.$setFormals(x);
		return z;
	}	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}