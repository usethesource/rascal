package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Catch extends AbstractAST { 
  public org.rascalmpl.ast.Statement getBody() { throw new UnsupportedOperationException(); } public boolean hasBody() { return false; } public boolean isDefault() { return false; }
static public class Default extends Catch {
/** "catch" ":" body:Statement -> Catch {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Statement body) {
		this.node = node;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCatchDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasBody() { return true; }

private final org.rascalmpl.ast.Statement body;
	public org.rascalmpl.ast.Statement getBody() { return body; }	
}
static public class Ambiguity extends Catch {
  private final java.util.List<org.rascalmpl.ast.Catch> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Catch> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Catch> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCatchAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean isBinding() { return false; }
static public class Binding extends Catch {
/** "catch" pattern:Expression ":" body:Statement -> Catch {cons("Binding")} */
	public Binding(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Statement body) {
		this.node = node;
		this.pattern = pattern;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCatchBinding(this);
	}

	public boolean isBinding() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasBody() { return true; }

private final org.rascalmpl.ast.Expression pattern;
	public org.rascalmpl.ast.Expression getPattern() { return pattern; }
	private final org.rascalmpl.ast.Statement body;
	public org.rascalmpl.ast.Statement getBody() { return body; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}