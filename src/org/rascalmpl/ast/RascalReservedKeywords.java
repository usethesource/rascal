package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class RascalReservedKeywords extends AbstractAST { 
  static public class Lexical extends RascalReservedKeywords {
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
     		return v.visitRascalReservedKeywordsLexical(this);
  	}
} static public class Ambiguity extends RascalReservedKeywords {
  private final java.util.List<org.rascalmpl.ast.RascalReservedKeywords> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RascalReservedKeywords> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.RascalReservedKeywords> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitRascalReservedKeywordsAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}