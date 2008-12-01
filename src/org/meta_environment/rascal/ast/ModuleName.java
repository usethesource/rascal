package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ModuleName extends AbstractAST { 
  static public class Lexical extends ModuleName {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitModuleNameLexical(this);
  	}
} static public class Ambiguity extends ModuleName {
  private final java.util.List<org.meta_environment.rascal.ast.ModuleName> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.ModuleName> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.ModuleName> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitModuleNameAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}