package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class TimeZonePart extends AbstractAST { 
  static public class Lexical extends TimeZonePart {
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
     		return v.visitTimeZonePartLexical(this);
  	}
} static public class Ambiguity extends TimeZonePart {
  private final java.util.List<org.rascalmpl.ast.TimeZonePart> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TimeZonePart> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.TimeZonePart> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitTimeZonePartAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}