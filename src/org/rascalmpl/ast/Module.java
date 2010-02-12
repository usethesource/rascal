package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Module extends AbstractAST { 
public org.rascalmpl.ast.Header getHeader() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Body getBody() { throw new UnsupportedOperationException(); }
public boolean hasHeader() { return false; }
	public boolean hasBody() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Module {
/** header:Header body:Body -> Module {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Header header, org.rascalmpl.ast.Body body) {
		this.node = node;
		this.header = header;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitModuleDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasHeader() { return true; }
	public boolean hasBody() { return true; }

private final org.rascalmpl.ast.Header header;
	public org.rascalmpl.ast.Header getHeader() { return header; }
	private final org.rascalmpl.ast.Body body;
	public org.rascalmpl.ast.Body getBody() { return body; }	
}
static public class Ambiguity extends Module {
  private final java.util.List<org.rascalmpl.ast.Module> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Module> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Module> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitModuleAmbiguity(this);
  }
}
}