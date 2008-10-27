package org.meta_environment.rascal.ast;
public abstract class ModuleParameters extends AbstractAST
{
  public class ModuleParameters extends ModuleParameters
  {
/* "[" parameters:{TypeVar ","}+ "]" -> ModuleParameters {cons("ModuleParameters")} */
    private ModuleParameters ()
    {
    }
    /*package */ ModuleParameters (ITree tree, List < TypeVar > parameters)
    {
      this.tree = tree;
      this.parameters = parameters;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitModuleParametersModuleParameters (this);
    }
    private final List < TypeVar > parameters;
    public List < TypeVar > getparameters ()
    {
      return parameters;
    }
    private void privateSetparameters (List < TypeVar > x)
    {
      this.parameters = x;
    }
    public ModuleParameters setparameters (List < TypeVar > x)
    {
      z = new ModuleParameters ();
      z.privateSetparameters (x);
      return z;
    }
  }
}
