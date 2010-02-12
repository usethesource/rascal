package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Marker extends AbstractAST { 
static public class Lexical extends Marker {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitMarkerLexical(this);
  	}
}
static public class Ambiguity extends Marker {
  private final java.util.List<org.rascalmpl.ast.Marker> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Marker> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Marker> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitMarkerAmbiguity(this);
  }
}
}