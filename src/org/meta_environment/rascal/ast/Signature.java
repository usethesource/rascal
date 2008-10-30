package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Signature extends AbstractAST { 
public class NoThrows extends Signature {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters -> Signature {cons("NoThrows")} */
	private NoThrows() { }
	/*package*/ NoThrows(ITree tree, Type type, FunctionModifiers modifiers, FunctionName name, Parameters parameters) {
		this.tree = tree;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitNoThrowsSignature(this);
	}
private Type type;
	public Type gettype() { return type; }
	private void privateSettype(Type x) { this.type = x; }
	public NoThrows settype(Type x) { 
		NoThrows z = new NoThrows();
 		z.privateSettype(x);
		return z;
	}
	private FunctionModifiers modifiers;
	public FunctionModifiers getmodifiers() { return modifiers; }
	private void privateSetmodifiers(FunctionModifiers x) { this.modifiers = x; }
	public NoThrows setmodifiers(FunctionModifiers x) { 
		NoThrows z = new NoThrows();
 		z.privateSetmodifiers(x);
		return z;
	}
	private FunctionName name;
	public FunctionName getname() { return name; }
	private void privateSetname(FunctionName x) { this.name = x; }
	public NoThrows setname(FunctionName x) { 
		NoThrows z = new NoThrows();
 		z.privateSetname(x);
		return z;
	}
	private Parameters parameters;
	public Parameters getparameters() { return parameters; }
	private void privateSetparameters(Parameters x) { this.parameters = x; }
	public NoThrows setparameters(Parameters x) { 
		NoThrows z = new NoThrows();
 		z.privateSetparameters(x);
		return z;
	}	
}
public class Ambiguity extends Signature {
  private final List<Signature> alternatives;
  public Ambiguity(List<Signature> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Signature> getAlternatives() {
	return alternatives;
  }
} 
public class WithThrows extends Signature {
/* type:Type modifiers:FunctionModifiers name:FunctionName parameters:Parameters "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
	private WithThrows() { }
	/*package*/ WithThrows(ITree tree, Type type, FunctionModifiers modifiers, FunctionName name, Parameters parameters, List<Type> exceptions) {
		this.tree = tree;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
		this.exceptions = exceptions;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitWithThrowsSignature(this);
	}
private Type type;
	public Type gettype() { return type; }
	private void privateSettype(Type x) { this.type = x; }
	public WithThrows settype(Type x) { 
		WithThrows z = new WithThrows();
 		z.privateSettype(x);
		return z;
	}
	private FunctionModifiers modifiers;
	public FunctionModifiers getmodifiers() { return modifiers; }
	private void privateSetmodifiers(FunctionModifiers x) { this.modifiers = x; }
	public WithThrows setmodifiers(FunctionModifiers x) { 
		WithThrows z = new WithThrows();
 		z.privateSetmodifiers(x);
		return z;
	}
	private FunctionName name;
	public FunctionName getname() { return name; }
	private void privateSetname(FunctionName x) { this.name = x; }
	public WithThrows setname(FunctionName x) { 
		WithThrows z = new WithThrows();
 		z.privateSetname(x);
		return z;
	}
	private Parameters parameters;
	public Parameters getparameters() { return parameters; }
	private void privateSetparameters(Parameters x) { this.parameters = x; }
	public WithThrows setparameters(Parameters x) { 
		WithThrows z = new WithThrows();
 		z.privateSetparameters(x);
		return z;
	}
	private List<Type> exceptions;
	public List<Type> getexceptions() { return exceptions; }
	private void privateSetexceptions(List<Type> x) { this.exceptions = x; }
	public WithThrows setexceptions(List<Type> x) { 
		WithThrows z = new WithThrows();
 		z.privateSetexceptions(x);
		return z;
	}	
}
}