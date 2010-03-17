package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionDeclaration extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Visibility getVisibility() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Signature getSignature() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.FunctionBody getBody() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasVisibility() { return false; } public boolean hasSignature() { return false; } public boolean hasBody() { return false; }
public boolean isDefault() { return false; }
static public class Default extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature body:FunctionBody -> FunctionDeclaration {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Signature signature, org.rascalmpl.ast.FunctionBody body) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.signature = signature;
		this.body = body;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionDeclarationDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasSignature() { return true; }
	@Override
	public boolean hasBody() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Signature signature;
	@Override
	public org.rascalmpl.ast.Signature getSignature() { return signature; }
	private final org.rascalmpl.ast.FunctionBody body;
	@Override
	public org.rascalmpl.ast.FunctionBody getBody() { return body; }	
}
static public class Ambiguity extends FunctionDeclaration {
  private final java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionDeclaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionDeclaration> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionDeclarationAmbiguity(this);
  }
} public boolean isAbstract() { return false; }
static public class Abstract extends FunctionDeclaration {
/** tags:Tags visibility:Visibility signature:Signature ";" -> FunctionDeclaration {cons("Abstract")} */
	public Abstract(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Signature signature) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.signature = signature;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionDeclarationAbstract(this);
	}

	@Override
	public boolean isAbstract() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasSignature() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Signature signature;
	@Override
	public org.rascalmpl.ast.Signature getSignature() { return signature; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}