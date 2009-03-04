package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class IAlpha extends AbstractAST { 
static public class Lexical extends IAlpha {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitIAlphaLexical(this);
  	}
}
static public class Ambiguity extends IAlpha {
  private final java.util.List<org.meta_environment.rascal.ast.IAlpha> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.IAlpha> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.IAlpha> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitIAlphaAmbiguity(this);
  }
}
}