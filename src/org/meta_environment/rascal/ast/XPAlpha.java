package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class XPAlpha extends AbstractAST { 
static public class Lexical extends XPAlpha {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitXPAlphaLexical(this);
  	}
}
static public class Ambiguity extends XPAlpha {
  private final java.util.List<org.meta_environment.rascal.ast.XPAlpha> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.XPAlpha> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.XPAlpha> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitXPAlphaAmbiguity(this);
  }
}
}