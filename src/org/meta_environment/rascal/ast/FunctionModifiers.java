package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionModifiers extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.FunctionModifier >
    getModifiers ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasModifiers ()
  {
    return false;
  }
  public boolean isLisT ()
  {
    return false;
  }
  static public class LisT extends FunctionModifiers
  {
/* modifiers:FunctionModifier* -> FunctionModifiers {cons("LisT")} */
    private LisT ()
    {
    }
    /*package */ LisT (ITree tree,
		       java.util.LisT <
		       org.meta_environment.rascal.ast.FunctionModifier >
		       modifiers)
    {
      this.tree = tree;
      this.modifiers = modifiers;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItFunctionModifiersLisT (this);
    }

    public boolean isLisT ()
    {
      return true;
    }

    public boolean hasModifiers ()
    {
      return true;
    }

    private java.util.LisT <
      org.meta_environment.rascal.ast.FunctionModifier > modifiers;
    public java.util.LisT < org.meta_environment.rascal.ast.FunctionModifier >
      getModifiers ()
    {
      return modifiers;
    }
    private void $setModifiers (java.util.LisT <
				org.meta_environment.rascal.ast.
				FunctionModifier > x)
    {
      this.modifiers = x;
    }
    public LisT setModifiers (java.util.LisT <
			      org.meta_environment.rascal.ast.
			      FunctionModifier > x)
    {
      LisT z = new LisT ();
      z.$setModifiers (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionModifiers
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.FunctionModifiers > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.FunctionModifiers >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT <
      org.meta_environment.rascal.ast.FunctionModifiers > getAlternatives ()
    {
      return alternatives;
    }
  }
}
