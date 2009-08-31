package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LocalVariableDeclaration extends AbstractAST { 
  public org.meta_environment.rascal.ast.Declarator getDeclarator() { throw new UnsupportedOperationException(); } public boolean hasDeclarator() { return false; } public boolean isDefault() { return false; }
static public class Default extends LocalVariableDeclaration {
/** declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.Declarator declarator) {
		this.node = node;
		this.declarator = declarator;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLocalVariableDeclarationDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasDeclarator() { return true; }

private org.meta_environment.rascal.ast.Declarator declarator;
	public org.meta_environment.rascal.ast.Declarator getDeclarator() { return declarator; }
	private void $setDeclarator(org.meta_environment.rascal.ast.Declarator x) { this.declarator = x; }
	public Default setDeclarator(org.meta_environment.rascal.ast.Declarator x) { 
		Default z = new Default();
 		z.$setDeclarator(x);
		return z;
	}	
}
static public class Ambiguity extends LocalVariableDeclaration {
  private final java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLocalVariableDeclarationAmbiguity(this);
  }
} public boolean isDynamic() { return false; }
static public class Dynamic extends LocalVariableDeclaration {
/** "dynamic" declarator:Declarator -> LocalVariableDeclaration {cons("Dynamic")} */
	private Dynamic() {
		super();
	}
	public Dynamic(INode node, org.meta_environment.rascal.ast.Declarator declarator) {
		this.node = node;
		this.declarator = declarator;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLocalVariableDeclarationDynamic(this);
	}

	public boolean isDynamic() { return true; }

	public boolean hasDeclarator() { return true; }

private org.meta_environment.rascal.ast.Declarator declarator;
	public org.meta_environment.rascal.ast.Declarator getDeclarator() { return declarator; }
	private void $setDeclarator(org.meta_environment.rascal.ast.Declarator x) { this.declarator = x; }
	public Dynamic setDeclarator(org.meta_environment.rascal.ast.Declarator x) { 
		Dynamic z = new Dynamic();
 		z.$setDeclarator(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}