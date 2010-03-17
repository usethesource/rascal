package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Asterisk extends AbstractAST { 
static public class Lexical extends Asterisk {
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
     		return v.visitAsteriskLexical(this);
  	}
}
static public class Ambiguity extends Asterisk {
  private final java.util.List<org.rascalmpl.ast.Asterisk> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Asterisk> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Asterisk> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitAsteriskAmbiguity(this);
  }
}
}