package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DataTypeSelector extends AbstractAST
{
  static public class Selector extends DataTypeSelector
  {
/* sort:Name "." production:Name -> DataTypeSelector {cons("Selector")} */
    private Selector ()
    {
    }
    /*package */ Selector (ITree tree,
			   org.meta_environment.rascal.ast.Name sort,
			   org.meta_environment.rascal.ast.Name production)
    {
      this.tree = tree;
      this.sort = sort;
      this.production = production;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDataTypeSelectorSelector (this);
    }
    private org.meta_environment.rascal.ast.Name sort;
    public org.meta_environment.rascal.ast.Name getSort ()
    {
      return sort;
    }
    private void $setSort (org.meta_environment.rascal.ast.Name x)
    {
      this.sort = x;
    }
    public org.meta_environment.rascal.ast.Selector setSort (org.
							     meta_environment.
							     rascal.ast.
							     Name x)
    {
      org.meta_environment.rascal.ast.Selector z = new Selector ();
      z.$setSort (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name production;
    public org.meta_environment.rascal.ast.Name getProduction ()
    {
      return production;
    }
    private void $setProduction (org.meta_environment.rascal.ast.Name x)
    {
      this.production = x;
    }
    public org.meta_environment.rascal.ast.Selector setProduction (org.
								   meta_environment.
								   rascal.ast.
								   Name x)
    {
      org.meta_environment.rascal.ast.Selector z = new Selector ();
      z.$setProduction (x);
      return z;
    }
  }
  static public class Ambiguity extends DataTypeSelector
  {
    private final java.util.List < DataTypeSelector > alternatives;
    public Ambiguity (java.util.List < DataTypeSelector > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DataTypeSelector > getAlternatives ()
    {
      return alternatives;
    }
  }
}
