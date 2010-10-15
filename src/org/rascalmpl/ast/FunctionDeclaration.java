package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionDeclaration extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Visibility getVisibility() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Signature getSignature() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.FunctionBody getBody() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasVisibility() { return false; } public boolean hasSignature() { return false; } public boolean hasBody() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature body:FunctionBody -> FunctionDeclaration {cons("Default")} */
	protected Default(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Signature signature, org.rascalmpl.ast.FunctionBody body) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.signature = signature;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionDeclarationDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasSignature() { return true; }
	public boolean hasBody() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Signature signature;
	public org.rascalmpl.ast.Signature getSignature() { return signature; }
	private final org.rascalmpl.ast.FunctionBody body;
	public org.rascalmpl.ast.FunctionBody getBody() { return body; }	
}
static public class Ambiguity extends FunctionDeclaration {
  private final java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionDeclaration> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionDeclarationAmbiguity(this);
  }
} public boolean isAbstract() { return false; }
static public class Abstract extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature ";" -> FunctionDeclaration {cons("Abstract")} */
	protected Abstract(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Signature signature) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.signature = signature;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionDeclarationAbstract(this);
	}

	public boolean isAbstract() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasSignature() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Signature signature;
	public org.rascalmpl.ast.Signature getSignature() { return signature; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}