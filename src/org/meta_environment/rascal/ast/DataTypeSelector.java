package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DataTypeSelector extends AbstractAST
{
  public class Selector extends DataTypeSelector
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
      return visitor.visitSelectorDataTypeSelector (this);
    }
    private Name sort;
    public Name getsort ()
    {
      return sort;
    }
    private void privateSetsort (Name x)
    {
      this.sort = x;
    }
    public Selector setsort (Name x)
    {
      z = new Selector ();
      z.privateSetsort (x);
      return z;
    }
    private Name production;
    public Name getproduction ()
    {
      return production;
    }
    private void privateSetproduction (Name x)
    {
      this.production = x;
    }
    public Selector setproduction (Name x)
    {
      z = new Selector ();
      z.privateSetproduction (x);
      return z;
    }
  }
}
