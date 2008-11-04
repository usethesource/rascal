package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionModifier extends AbstractAST
{
  public boolean isJava ()
  {
    return false;
  }
  static public class Java extends FunctionModifier
  {
/* "java" -> FunctionModifier {cons("Java")} */
    private Java ()
    {
    }
    /*package */ Java (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItFunctionModifierJava (this);
    }

    public boolean isJava ()
    {
      return true;
    }
  }
  static public class Ambiguity extends FunctionModifier
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.FunctionModifier > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.FunctionModifier >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.FunctionModifier >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
