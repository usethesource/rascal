package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionDeclaration extends AbstractAST { 
  public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Visibility getVisibility() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Signature getSignature() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.FunctionBody getBody() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasVisibility() { return false; } public boolean hasSignature() { return false; } public boolean hasBody() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature body:FunctionBody -> FunctionDeclaration {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.FunctionBody body) {
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

private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Default setTags(org.meta_environment.rascal.ast.Tags x) { 
		Default z = new Default();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Visibility visibility;
	public org.meta_environment.rascal.ast.Visibility getVisibility() { return visibility; }
	private void $setVisibility(org.meta_environment.rascal.ast.Visibility x) { this.visibility = x; }
	public Default setVisibility(org.meta_environment.rascal.ast.Visibility x) { 
		Default z = new Default();
 		z.$setVisibility(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Signature signature;
	public org.meta_environment.rascal.ast.Signature getSignature() { return signature; }
	private void $setSignature(org.meta_environment.rascal.ast.Signature x) { this.signature = x; }
	public Default setSignature(org.meta_environment.rascal.ast.Signature x) { 
		Default z = new Default();
 		z.$setSignature(x);
		return z;
	}
	private org.meta_environment.rascal.ast.FunctionBody body;
	public org.meta_environment.rascal.ast.FunctionBody getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.FunctionBody x) { this.body = x; }
	public Default setBody(org.meta_environment.rascal.ast.FunctionBody x) { 
		Default z = new Default();
 		z.$setBody(x);
		return z;
	}	
}
static public class Ambiguity extends FunctionDeclaration {
  private final java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionDeclarationAmbiguity(this);
  }
} public boolean isAbstract() { return false; }
static public class Abstract extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature ";" -> FunctionDeclaration {cons("Abstract")} */
	private Abstract() {
		super();
	}
	public Abstract(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Signature signature) {
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

private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Abstract setTags(org.meta_environment.rascal.ast.Tags x) { 
		Abstract z = new Abstract();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Visibility visibility;
	public org.meta_environment.rascal.ast.Visibility getVisibility() { return visibility; }
	private void $setVisibility(org.meta_environment.rascal.ast.Visibility x) { this.visibility = x; }
	public Abstract setVisibility(org.meta_environment.rascal.ast.Visibility x) { 
		Abstract z = new Abstract();
 		z.$setVisibility(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Signature signature;
	public org.meta_environment.rascal.ast.Signature getSignature() { return signature; }
	private void $setSignature(org.meta_environment.rascal.ast.Signature x) { this.signature = x; }
	public Abstract setSignature(org.meta_environment.rascal.ast.Signature x) { 
		Abstract z = new Abstract();
 		z.$setSignature(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}