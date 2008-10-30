package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class FunctionBody extends AbstractAST { 
public class Default extends FunctionBody {
/* "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, List<Statement> statements) {
		this.tree = tree;
		this.statements = statements;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDefaultFunctionBody(this);
	}
private List<Statement> statements;
	public List<Statement> getstatements() { return statements; }
	private void privateSetstatements(List<Statement> x) { this.statements = x; }
	public Default setstatements(List<Statement> x) { 
		Default z = new Default();
 		z.privateSetstatements(x);
		return z;
	}	
}
public class Ambiguity extends FunctionBody {
  private final List<FunctionBody> alternatives;
  public Ambiguity(List<FunctionBody> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<FunctionBody> getAlternatives() {
	return alternatives;
  }
}
}