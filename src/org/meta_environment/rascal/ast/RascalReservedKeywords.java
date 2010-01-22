package org.meta_environment.rascal.ast; 
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

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRascalReservedKeywordsLexical(this);
  	}
} static public class Ambiguity extends RascalReservedKeywords {
  private final java.util.List<org.meta_environment.rascal.ast.RascalReservedKeywords> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RascalReservedKeywords> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.RascalReservedKeywords> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRascalReservedKeywordsAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}