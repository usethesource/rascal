package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class DataTypeSelector extends AbstractAST { 
public org.meta_environment.rascal.ast.Name getSort() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Name getProduction() { throw new UnsupportedOperationException(); }
public boolean hasSort() { return false; }
	public boolean hasProduction() { return false; }
public boolean isSelector() { return false; }
static public class Selector extends DataTypeSelector {
/* sort:Name "." production:Name -> DataTypeSelector {cons("Selector")} */
	private Selector() { }
	/*package*/ Selector(ITree tree, org.meta_environment.rascal.ast.Name sort, org.meta_environment.rascal.ast.Name production) {
		this.tree = tree;
		this.sort = sort;
		this.production = production;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDataTypeSelectorSelector(this);
	}

	public boolean isSelector() { return true; }

	public boolean hasSort() { return true; }
	public boolean hasProduction() { return true; }

private org.meta_environment.rascal.ast.Name sort;
	public org.meta_environment.rascal.ast.Name getSort() { return sort; }
	private void $setSort(org.meta_environment.rascal.ast.Name x) { this.sort = x; }
	public Selector setSort(org.meta_environment.rascal.ast.Name x) { 
		Selector z = new Selector();
 		z.$setSort(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name production;
	public org.meta_environment.rascal.ast.Name getProduction() { return production; }
	private void $setProduction(org.meta_environment.rascal.ast.Name x) { this.production = x; }
	public Selector setProduction(org.meta_environment.rascal.ast.Name x) { 
		Selector z = new Selector();
 		z.$setProduction(x);
		return z;
	}	
}
static public class Ambiguity extends DataTypeSelector {
  private final java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> getAlternatives() {
	return alternatives;
  }
}
}