package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionBody extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.Statement> getStatements() { throw new UnsupportedOperationException(); }
public boolean hasStatements() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionBody {
/** "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
	public Default(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		this.node = node;
		this.statements = statements;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionBodyDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasStatements() { return true; }

private final java.util.List<org.rascalmpl.ast.Statement> statements;
	@Override
	public java.util.List<org.rascalmpl.ast.Statement> getStatements() { return statements; }	
}
static public class Ambiguity extends FunctionBody {
  private final java.util.List<org.rascalmpl.ast.FunctionBody> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionBody> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionBody> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionBodyAmbiguity(this);
  }
}
}