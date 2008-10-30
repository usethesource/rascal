package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      return visitor.visitDataTypeSelectorSelector (this);
    }
    private Name sort;
    public Name getsort ()
    {
      return sort;
    }
    private void $setsort (Name x)
    {
      this.sort = x;
    }
    public Selector setsort (Name x)
    {
      Selector z = new Selector ();
      z.$setsort (x);
      return z;
    }
    private Name production;
    public Name getproduction ()
    {
      return production;
    }
    private void $setproduction (Name x)
    {
      this.production = x;
    }
    public Selector setproduction (Name x)
    {
      Selector z = new Selector ();
      z.$setproduction (x);
      return z;
    }
  }
  public class Ambiguity extends DataTypeSelector
  {
    private final List < DataTypeSelector > alternatives;
    public Ambiguity (List < DataTypeSelector > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < DataTypeSelector > getAlternatives ()
    {
      return alternatives;
    }
  }
}
