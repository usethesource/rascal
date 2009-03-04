package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Segment extends AbstractAST { 
static public class Lexical extends Segment {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSegmentLexical(this);
  	}
}
static public class Ambiguity extends Segment {
  private final java.util.List<org.meta_environment.rascal.ast.Segment> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Segment> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Segment> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSegmentAmbiguity(this);
  }
}
}