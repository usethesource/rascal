package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class ModuleActuals extends AbstractAST
{
  public class Default extends ModuleActuals
  {
/* "[" types:{Type ","}+ "]" -> ModuleActuals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Type > types)
    {
      this.tree = tree;
      this.types = types;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleActualsDefault (this);
    }
    private List < Type > types;
    public List < Type > gettypes ()
    {
      return types;
    }
    private void $settypes (List < Type > x)
    {
      this.types = x;
    }
    public Default settypes (List < Type > x)
    {
      Default z = new Default ();
      z.$settypes (x);
      return z;
    }
  }
  public class Ambiguity extends ModuleActuals
  {
    private final List < ModuleActuals > alternatives;
    public Ambiguity (List < ModuleActuals > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ModuleActuals > getAlternatives ()
    {
      return alternatives;
    }
  }
}
