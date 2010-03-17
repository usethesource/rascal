package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class JustDate extends AbstractAST { 
static public class Lexical extends JustDate {
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
     		return v.visitJustDateLexical(this);
  	}
}
static public class Ambiguity extends JustDate {
  private final java.util.List<org.rascalmpl.ast.JustDate> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.JustDate> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.JustDate> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitJustDateAmbiguity(this);
  }
}
}