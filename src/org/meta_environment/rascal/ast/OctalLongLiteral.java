package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class OctalLongLiteral extends AbstractAST { 
static public class Lexical extends OctalLongLiteral {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitOctalLongLiteralLexical(this);
  	}
}
static public class Ambiguity extends OctalLongLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitOctalLongLiteralAmbiguity(this);
  }
}
}