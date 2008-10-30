package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class LocalVariableDeclaration extends AbstractAST { 
public class Default extends LocalVariableDeclaration {
/* declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, Declarator declarator) {
		this.tree = tree;
		this.declarator = declarator;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDefaultLocalVariableDeclaration(this);
	}
private Declarator declarator;
	public Declarator getdeclarator() { return declarator; }
	private void privateSetdeclarator(Declarator x) { this.declarator = x; }
	public Default setdeclarator(Declarator x) { 
		Default z = new Default();
 		z.privateSetdeclarator(x);
		return z;
	}	
}
public class Ambiguity extends LocalVariableDeclaration {
  private final List<LocalVariableDeclaration> alternatives;
  public Ambiguity(List<LocalVariableDeclaration> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<LocalVariableDeclaration> getAlternatives() {
	return alternatives;
  }
} 
public class Dynamic extends LocalVariableDeclaration {
/* "dynamic" declarator:Declarator -> LocalVariableDeclaration {cons("Dynamic")} */
	private Dynamic() { }
	/*package*/ Dynamic(ITree tree, Declarator declarator) {
		this.tree = tree;
		this.declarator = declarator;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDynamicLocalVariableDeclaration(this);
	}
private Declarator declarator;
	public Declarator getdeclarator() { return declarator; }
	private void privateSetdeclarator(Declarator x) { this.declarator = x; }
	public Dynamic setdeclarator(Declarator x) { 
		Dynamic z = new Dynamic();
 		z.privateSetdeclarator(x);
		return z;
	}	
}
}