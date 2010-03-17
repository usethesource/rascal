package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LocalVariableDeclaration extends AbstractAST { 
  public org.rascalmpl.ast.Declarator getDeclarator() { throw new UnsupportedOperationException(); } public boolean hasDeclarator() { return false; } public boolean isDefault() { return false; }
static public class Default extends LocalVariableDeclaration {
/** declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Declarator declarator) {
		this.node = node;
		this.declarator = declarator;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLocalVariableDeclarationDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasDeclarator() { return true; }

private final org.rascalmpl.ast.Declarator declarator;
	@Override
	public org.rascalmpl.ast.Declarator getDeclarator() { return declarator; }	
}
static public class Ambiguity extends LocalVariableDeclaration {
  private final java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitLocalVariableDeclarationAmbiguity(this);
  }
} public boolean isDynamic() { return false; }
static public class Dynamic extends LocalVariableDeclaration {
/** "dynamic" declarator:Declarator -> LocalVariableDeclaration {cons("Dynamic")} */
	public Dynamic(INode node, org.rascalmpl.ast.Declarator declarator) {
		this.node = node;
		this.declarator = declarator;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLocalVariableDeclarationDynamic(this);
	}

	@Override
	public boolean isDynamic() { return true; }

	@Override
	public boolean hasDeclarator() { return true; }

private final org.rascalmpl.ast.Declarator declarator;
	@Override
	public org.rascalmpl.ast.Declarator getDeclarator() { return declarator; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}