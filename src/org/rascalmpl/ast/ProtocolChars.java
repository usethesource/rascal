package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolChars extends AbstractAST { 
static public class Lexical extends ProtocolChars {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitProtocolCharsLexical(this);
  	}
}
static public class Ambiguity extends ProtocolChars {
  private final java.util.List<org.rascalmpl.ast.ProtocolChars> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ProtocolChars> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolCharsAmbiguity(this);
  }
}
}