package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Tag extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.TagString getContents() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasContents() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Tag {
/** "@" name:Name contents:TagString -> Tag {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.TagString contents) {
		this.node = node;
		this.name = name;
		this.contents = contents;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasName() { return true; }
	public boolean hasContents() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Default setName(org.meta_environment.rascal.ast.Name x) { 
		Default z = new Default();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.TagString contents;
	public org.meta_environment.rascal.ast.TagString getContents() { return contents; }
	private void $setContents(org.meta_environment.rascal.ast.TagString x) { this.contents = x; }
	public Default setContents(org.meta_environment.rascal.ast.TagString x) { 
		Default z = new Default();
 		z.$setContents(x);
		return z;
	}	
}
static public class Ambiguity extends Tag {
  private final java.util.List<org.meta_environment.rascal.ast.Tag> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Tag> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagAmbiguity(this);
  }
} public boolean isEmpty() { return false; }
static public class Empty extends Tag {
/** "@" name:Name -> Tag {cons("Empty")} */
	private Empty() {
		super();
	}
	public Empty(INode node, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagEmpty(this);
	}

	public boolean isEmpty() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Empty setName(org.meta_environment.rascal.ast.Name x) { 
		Empty z = new Empty();
 		z.$setName(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}