package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleActuals extends AbstractAST
{
  static public class Default extends ModuleActuals
  {
/* "[" types:{Type ","}+ "]" -> ModuleActuals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Type > types)
    {
      this.tree = tree;
      this.types = types;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleActualsDefault (this);
    }
    private java.util.List < Type > types;
    public java.util.List < Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.List < Type > x)
    {
      this.types = x;
    }
    public Default setTypes (java.util.List < Type > x)
    {
      Default z = new Default ();
      z.$setTypes (x);
      return z;
    }
  }
  public class Ambiguity extends ModuleActuals
  {
    private final java.util.List < ModuleActuals > alternatives;
    public Ambiguity (java.util.List < ModuleActuals > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ModuleActuals > getAlternatives ()
    {
      return alternatives;
    }
  }
}
