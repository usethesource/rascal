package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LanguageAction extends AbstractAST { 
  public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
public boolean hasExpression() { return false; }
public boolean isBuild() { return false; }
static public class Build extends LanguageAction {
/** "=>" expression:Expression -> LanguageAction {cons("Build")} */
	protected Build(INode node, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLanguageActionBuild(this);
	}

	public boolean isBuild() { return true; }

	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
static public class Ambiguity extends LanguageAction {
  private final java.util.List<org.rascalmpl.ast.LanguageAction> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LanguageAction> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.LanguageAction> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLanguageActionAmbiguity(this);
  }
} 
public java.util.List<org.rascalmpl.ast.Statement> getStatements() { throw new UnsupportedOperationException(); }
public boolean hasStatements() { return false; }
public boolean isAction() { return false; }
static public class Action extends LanguageAction {
/** "{" statements:Statement* "}" -> LanguageAction {cons("Action")} */
	protected Action(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		this.node = node;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLanguageActionAction(this);
	}

	public boolean isAction() { return true; }

	public boolean hasStatements() { return true; }

private final java.util.List<org.rascalmpl.ast.Statement> statements;
	public java.util.List<org.rascalmpl.ast.Statement> getStatements() { return statements; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}