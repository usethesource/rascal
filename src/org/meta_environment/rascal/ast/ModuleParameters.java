package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ModuleParameters extends AbstractAST { 
public class ModuleParameters extends ModuleParameters {
/* "[" parameters:{TypeVar ","}+ "]" -> ModuleParameters {cons("ModuleParameters")} */
	private ModuleParameters() { }
	/*package*/ ModuleParameters(ITree tree, List<TypeVar> parameters) {
		this.tree = tree;
		this.parameters = parameters;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitModuleParametersModuleParameters(this);
	}
private List<TypeVar> parameters;
	public List<TypeVar> getparameters() { return parameters; }
	private void privateSetparameters(List<TypeVar> x) { this.parameters = x; }
	public ModuleParameters setparameters(List<TypeVar> x) { 
		ModuleParameters z = new ModuleParameters();
 		z.privateSetparameters(x);
		return z;
	}	
}
public class Ambiguity extends ModuleParameters {
  private final List<ModuleParameters> alternatives;
  public Ambiguity(List<ModuleParameters> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<ModuleParameters> getAlternatives() {
	return alternatives;
  }
}
}