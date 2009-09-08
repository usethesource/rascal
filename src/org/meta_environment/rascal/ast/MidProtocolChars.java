package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class MidProtocolChars extends AbstractAST { 
static public class Lexical extends MidProtocolChars {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitMidProtocolCharsLexical(this);
  	}
}
static public class Ambiguity extends MidProtocolChars {
  private final java.util.List<org.meta_environment.rascal.ast.MidProtocolChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.MidProtocolChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.MidProtocolChars> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitMidProtocolCharsAmbiguity(this);
  }
}
}