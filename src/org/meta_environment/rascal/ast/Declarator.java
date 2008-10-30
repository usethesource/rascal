package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Declarator extends AbstractAST { 
public class Default extends Declarator {
/* type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, Type type, List<Variable> variables) {
		this.tree = tree;
		this.type = type;
		this.variables = variables;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDefaultDeclarator(this);
	}
private Type type;
	public Type gettype() { return type; }
	private void privateSettype(Type x) { this.type = x; }
	public Default settype(Type x) { 
		Default z = new Default();
 		z.privateSettype(x);
		return z;
	}
	private List<Variable> variables;
	public List<Variable> getvariables() { return variables; }
	private void privateSetvariables(List<Variable> x) { this.variables = x; }
	public Default setvariables(List<Variable> x) { 
		Default z = new Default();
 		z.privateSetvariables(x);
		return z;
	}	
}
public class Ambiguity extends Declarator {
  private final List<Declarator> alternatives;
  public Ambiguity(List<Declarator> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Declarator> getAlternatives() {
	return alternatives;
  }
}
}