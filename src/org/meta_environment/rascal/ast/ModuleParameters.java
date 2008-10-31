package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleParameters extends AbstractAST
{
  static public class Default extends ModuleParameters
  {
/* "[" parameters:{TypeVar ","}+ "]" -> ModuleParameters {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < TypeVar > parameters)
    {
      this.tree = tree;
      this.parameters = parameters;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleParametersDefault (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.TypeVar >
      parameters;
    public java.util.List < org.meta_environment.rascal.ast.TypeVar >
      getParameters ()
    {
      return parameters;
    }
    private void $setParameters (java.util.List <
				 org.meta_environment.rascal.ast.TypeVar > x)
    {
      this.parameters = x;
    }
    public Default setParameters (java.util.List <
				  org.meta_environment.rascal.ast.TypeVar > x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setParameters (x);
      return z;
    }
  }
  static public class Ambiguity extends ModuleParameters
  {
    public ModuleParameters.Ambiguity makeModuleParametersAmbiguity (java.
								     util.
								     List <
								     ModuleParameters
								     >
								     alternatives)
    {
      ModuleParameters.Ambiguity amb =
	new ModuleParameters.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (ModuleParameters.Ambiguity) table.get (amb);
    }
    private final java.util.List < ModuleParameters > alternatives;
    public Ambiguity (java.util.List < ModuleParameters > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ModuleParameters > getAlternatives ()
    {
      return alternatives;
    }
  }
}
