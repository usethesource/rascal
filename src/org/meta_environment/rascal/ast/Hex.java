package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Hex extends AbstractAST { 
static public class Lexical extends Hex {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitHexLexical(this);
  	}
}
static public class Ambiguity extends Hex {
  private final java.util.List<org.meta_environment.rascal.ast.Hex> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Hex> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Hex> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitHexAmbiguity(this);
  }
}
}