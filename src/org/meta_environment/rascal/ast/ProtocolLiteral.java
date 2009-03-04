package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolLiteral extends AbstractAST { 
  static public class Lexical extends ProtocolLiteral {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitProtocolLiteralLexical(this);
  	}
} static public class Ambiguity extends ProtocolLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.ProtocolLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.ProtocolLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}