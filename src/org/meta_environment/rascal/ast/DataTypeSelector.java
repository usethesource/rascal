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
    /*package */ Selector (ITree tree, Name sort, Name production)
    {
      this.tree = tree;
      this.sort = sort;
      this.production = production;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDataTypeSelectorSelector (this);
    }
    private Name sort;
    public Name getSort ()
    {
      return sort;
    }
    private void $setSort (Name x)
    {
      this.sort = x;
    }
    public Selector setSort (Name x)
    {
      Selector z = new Selector ();
      z.$setSort (x);
      return z;
    }
    private Name production;
    public Name getProduction ()
    {
      return production;
    }
    private void $setProduction (Name x)
    {
      this.production = x;
    }
    public Selector setProduction (Name x)
    {
      Selector z = new Selector ();
      z.$setProduction (x);
      return z;
    }
  }
  public class Ambiguity extends DataTypeSelector
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
