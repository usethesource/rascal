package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class XAlpha extends AbstractAST { 
static public class Lexical extends XAlpha {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitXAlphaLexical(this);
  	}
}
static public class Ambiguity extends XAlpha {
  private final java.util.List<org.meta_environment.rascal.ast.XAlpha> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.XAlpha> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.XAlpha> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitXAlphaAmbiguity(this);
  }
}
}