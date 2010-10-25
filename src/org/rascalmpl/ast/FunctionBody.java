package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionBody extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.Statement> getStatements() { throw new UnsupportedOperationException(); }
public boolean hasStatements() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionBody {
/** "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
	protected Default(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		this.node = node;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionBodyDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasStatements() { return true; }

private final java.util.List<org.rascalmpl.ast.Statement> statements;
	public java.util.List<org.rascalmpl.ast.Statement> getStatements() { return statements; }	
}
static public class Ambiguity extends FunctionBody {
  private final java.util.List<org.rascalmpl.ast.FunctionBody> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionBody> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionBody> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionBodyAmbiguity(this);
  }
}
}