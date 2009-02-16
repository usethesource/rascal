package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Catch extends AbstractAST { 
  public org.meta_environment.rascal.ast.Statement getBody() { throw new UnsupportedOperationException(); } public boolean hasBody() { return false; } public boolean isDefault() { return false; }
static public class Default extends Catch {
/* "catch" ":" body:Statement -> Catch {cons("Default")} */
	private Default() { }
	/*package*/ Default(INode node, org.meta_environment.rascal.ast.Statement body) {
		this.node = node;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCatchDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasBody() { return true; }

private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public Default setBody(org.meta_environment.rascal.ast.Statement x) { 
		Default z = new Default();
 		z.$setBody(x);
		return z;
	}	
}
static public class Ambiguity extends Catch {
  private final java.util.List<org.meta_environment.rascal.ast.Catch> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Catch> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Catch> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCatchAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean isBinding() { return false; }
static public class Binding extends Catch {
/* "catch" pattern:Expression ":" body:Statement -> Catch {cons("Binding")} */
	private Binding() { }
	/*package*/ Binding(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement body) {
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

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public Binding setPattern(org.meta_environment.rascal.ast.Expression x) { 
		Binding z = new Binding();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public Binding setBody(org.meta_environment.rascal.ast.Statement x) { 
		Binding z = new Binding();
 		z.$setBody(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}