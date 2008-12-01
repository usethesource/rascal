package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ModuleWord extends AbstractAST { 
static public class Lexical extends ModuleWord {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitModuleWordLexical(this);
  	}
}
static public class Ambiguity extends ModuleWord {
  private final java.util.List<org.meta_environment.rascal.ast.ModuleWord> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.ModuleWord> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.ModuleWord> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitModuleWordAmbiguity(this);
  }
}
}