package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class TagString extends AbstractAST { 
static public class Lexical extends TagString {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitTagStringLexical(this);
  	}
}
static public class Ambiguity extends TagString {
  private final java.util.List<org.meta_environment.rascal.ast.TagString> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.TagString> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagStringAmbiguity(this);
  }
}
}