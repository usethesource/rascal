package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Name extends AbstractAST { 
  static public class Lexical extends Name {
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
     		return v.visitNameLexical(this);
  	}
} static public class Ambiguity extends Name {
  private final java.util.List<org.rascalmpl.ast.Name> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Name> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Name> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitNameAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}