package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class FunctionBody extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { throw new UnsupportedOperationException(); }
public boolean hasStatements() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionBody {
/** "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionBodyDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasStatements() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }
	private void $setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { this.statements = x; }
	public Default setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { 
		Default z = new Default();
 		z.$setStatements(x);
		return z;
	}	
}
static public class Ambiguity extends FunctionBody {
  private final java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.FunctionBody> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionBodyAmbiguity(this);
  }
}
}