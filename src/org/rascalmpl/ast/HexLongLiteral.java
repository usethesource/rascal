package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class HexLongLiteral extends AbstractAST { 
static public class Lexical extends HexLongLiteral {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitHexLongLiteralLexical(this);
  	}
}
static public class Ambiguity extends HexLongLiteral {
  private final java.util.List<org.rascalmpl.ast.HexLongLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.HexLongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.HexLongLiteral> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitHexLongLiteralAmbiguity(this);
  }
}
}