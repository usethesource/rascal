package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Tags extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Tag> getTags() { throw new UnsupportedOperationException(); }
public boolean hasTags() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Tags {
/** tags:Tag* -> Tags {cons("Default"), category("Comment")} */
	public Default(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> tags) {
		this.node = node;
		this.tags = tags;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagsDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasTags() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Tag> tags;
	public java.util.List<org.meta_environment.rascal.ast.Tag> getTags() { return tags; }	
}
static public class Ambiguity extends Tags {
  private final java.util.List<org.meta_environment.rascal.ast.Tags> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Tags> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagsAmbiguity(this);
  }
}
}