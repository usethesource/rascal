package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class FunctionName extends AbstractAST { 
public class Name extends FunctionName {
/* name:Name -> FunctionName {cons("Name")} */
	private Name() { }
	/*package*/ Name(ITree tree, Name name) {
		this.tree = tree;
		this.name = name;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitNameFunctionName(this);
	}
private Name name;
	public Name getname() { return name; }
	private void privateSetname(Name x) { this.name = x; }
	public Name setname(Name x) { 
		Name z = new Name();
 		z.privateSetname(x);
		return z;
	}	
}
public class Ambiguity extends FunctionName {
  private final List<FunctionName> alternatives;
  public Ambiguity(List<FunctionName> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<FunctionName> getAlternatives() {
	return alternatives;
  }
} 
public class Operator extends FunctionName {
/* operator:StandardOperator -> FunctionName {cons("Operator")} */
	private Operator() { }
	/*package*/ Operator(ITree tree, StandardOperator operator) {
		this.tree = tree;
		this.operator = operator;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitOperatorFunctionName(this);
	}
private StandardOperator operator;
	public StandardOperator getoperator() { return operator; }
	private void privateSetoperator(StandardOperator x) { this.operator = x; }
	public Operator setoperator(StandardOperator x) { 
		Operator z = new Operator();
 		z.privateSetoperator(x);
		return z;
	}	
}
}