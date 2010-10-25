package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class SyntaxDefinition extends AbstractAST { 
  public org.rascalmpl.ast.Start getStart() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Sym getDefined() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Prod getProduction() { throw new UnsupportedOperationException(); } public boolean hasStart() { return false; } public boolean hasDefined() { return false; } public boolean hasProduction() { return false; } public boolean isLanguage() { return false; }
static public class Language extends SyntaxDefinition {
/** start:Start "syntax" defined:Sym "=" production:Prod ";" -> SyntaxDefinition {cons("Language")} */
	protected Language(INode node, org.rascalmpl.ast.Start start, org.rascalmpl.ast.Sym defined, org.rascalmpl.ast.Prod production) {
		this.node = node;
		this.start = start;
		this.defined = defined;
		this.production = production;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSyntaxDefinitionLanguage(this);
	}

	public boolean isLanguage() { return true; }

	public boolean hasStart() { return true; }
	public boolean hasDefined() { return true; }
	public boolean hasProduction() { return true; }

private final org.rascalmpl.ast.Start start;
	public org.rascalmpl.ast.Start getStart() { return start; }
	private final org.rascalmpl.ast.Sym defined;
	public org.rascalmpl.ast.Sym getDefined() { return defined; }
	private final org.rascalmpl.ast.Prod production;
	public org.rascalmpl.ast.Prod getProduction() { return production; }	
}
static public class Ambiguity extends SyntaxDefinition {
  private final java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives) {
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
/** "layout" defined:Sym "=" production:Prod ";" -> SyntaxDefinition {cons("Layout")} */
	protected Layout(INode node, org.rascalmpl.ast.Sym defined, org.rascalmpl.ast.Prod production) {
		this.node = node;
		this.defined = defined;
		this.production = production;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSyntaxDefinitionLayout(this);
	}

	public boolean isLayout() { return true; }

	public boolean hasDefined() { return true; }
	public boolean hasProduction() { return true; }

private final org.rascalmpl.ast.Sym defined;
	public org.rascalmpl.ast.Sym getDefined() { return defined; }
	private final org.rascalmpl.ast.Prod production;
	public org.rascalmpl.ast.Prod getProduction() { return production; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}