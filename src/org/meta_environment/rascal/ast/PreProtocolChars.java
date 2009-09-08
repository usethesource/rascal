package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PreProtocolChars extends AbstractAST { 
static public class Lexical extends PreProtocolChars {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitPreProtocolCharsLexical(this);
  	}
}
static public class Ambiguity extends PreProtocolChars {
  private final java.util.List<org.meta_environment.rascal.ast.PreProtocolChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PreProtocolChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.PreProtocolChars> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPreProtocolCharsAmbiguity(this);
  }
}
}