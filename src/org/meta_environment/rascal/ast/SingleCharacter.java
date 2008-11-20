package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class SingleCharacter extends AbstractAST { 
  static public class Lexical extends SingleCharacter {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleCharacterLexical(this);
  	}
} static public class Ambiguity extends SingleCharacter {
  private final java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.SingleCharacter> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleCharacterAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}