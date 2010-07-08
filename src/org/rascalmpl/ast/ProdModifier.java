package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProdModifier extends AbstractAST { 
  public org.rascalmpl.ast.Assoc getAssociativity() { throw new UnsupportedOperationException(); }
public boolean hasAssociativity() { return false; }
public boolean isAssociativity() { return false; }
static public class Associativity extends ProdModifier {
/** associativity:Assoc -> ProdModifier {cons("Associativity")} */
	public Associativity(INode node, org.rascalmpl.ast.Assoc associativity) {
		this.node = node;
		this.associativity = associativity;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdModifierAssociativity(this);
	}

	public boolean isAssociativity() { return true; }

	public boolean hasAssociativity() { return true; }

private final org.rascalmpl.ast.Assoc associativity;
	public org.rascalmpl.ast.Assoc getAssociativity() { return associativity; }	
}
static public class Ambiguity extends ProdModifier {
  private final java.util.List<org.rascalmpl.ast.ProdModifier> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ProdModifier> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProdModifierAmbiguity(this);
  }
} 
public boolean isBracket() { return false; }
static public class Bracket extends ProdModifier {
/** "bracket" -> ProdModifier {cons("Bracket")} */
	public Bracket(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdModifierBracket(this);
	}

	public boolean isBracket() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isLexical() { return false; }
static public class Lexical extends ProdModifier {
/** "lex" -> ProdModifier {cons("Lexical")} */
	public Lexical(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdModifierLexical(this);
	}

	public boolean isLexical() { return true; }	
}
}