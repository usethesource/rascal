package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class SyntaxDefinition extends AbstractAST { 
  public org.rascalmpl.ast.Start getStart() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.UserType getUser() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Prod getProduction() { throw new UnsupportedOperationException(); } public boolean hasStart() { return false; } public boolean hasUser() { return false; } public boolean hasProduction() { return false; } public boolean isLanguage() { return false; }
static public class Language extends SyntaxDefinition {
/** start:Start "syntax" user:UserType "=" production:Prod ";" -> SyntaxDefinition {cons("Language")} */
	public Language(INode node, org.rascalmpl.ast.Start start, org.rascalmpl.ast.UserType user, org.rascalmpl.ast.Prod production) {
		this.node = node;
		this.start = start;
		this.user = user;
		this.production = production;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSyntaxDefinitionLanguage(this);
	}

	public boolean isLanguage() { return true; }

	public boolean hasStart() { return true; }
	public boolean hasUser() { return true; }
	public boolean hasProduction() { return true; }

private final org.rascalmpl.ast.Start start;
	public org.rascalmpl.ast.Start getStart() { return start; }
	private final org.rascalmpl.ast.UserType user;
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final org.rascalmpl.ast.Prod production;
	public org.rascalmpl.ast.Prod getProduction() { return production; }	
}
static public class Ambiguity extends SyntaxDefinition {
  private final java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.SyntaxDefinition> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSyntaxDefinitionAmbiguity(this);
  }
} public boolean isLayout() { return false; }
static public class Layout extends SyntaxDefinition {
/** "layout" user:UserType "=" production:Prod ";" -> SyntaxDefinition {cons("Layout")} */
	public Layout(INode node, org.rascalmpl.ast.UserType user, org.rascalmpl.ast.Prod production) {
		this.node = node;
		this.user = user;
		this.production = production;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSyntaxDefinitionLayout(this);
	}

	public boolean isLayout() { return true; }

	public boolean hasUser() { return true; }
	public boolean hasProduction() { return true; }

private final org.rascalmpl.ast.UserType user;
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final org.rascalmpl.ast.Prod production;
	public org.rascalmpl.ast.Prod getProduction() { return production; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}