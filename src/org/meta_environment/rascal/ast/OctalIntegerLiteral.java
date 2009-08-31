package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class OctalIntegerLiteral extends AbstractAST { 
static public class Lexical extends OctalIntegerLiteral {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitOctalIntegerLiteralLexical(this);
  	}
}
static public class Ambiguity extends OctalIntegerLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitOctalIntegerLiteralAmbiguity(this);
  }
}
}