package org.meta_environment.rascal.ast;
public abstract class ModuleActuals extends AbstractAST
{
  public class Actuals extends ModuleActuals
  {
    private List < Type > types;

    private Actuals ()
    {
    }
    /*package */ Actuals (ITree tree, List < Type > types)
    {
      this.tree = tree;
      this.types = types;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitActualsModuleActuals (this);
    }
    private final List < Type > types;
    public List < Type > gettypes ()
    {
      return types;
    }
    private void privateSettypes (List < Type > x)
    {
      this.types = x;
    }
    public Actuals settypes (List < Type > x)
    {
      z = new Actuals ();
      z.privateSettypes (x);
      return z;
    }
  }
}
