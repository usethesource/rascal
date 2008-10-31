package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Declarator extends AbstractAST
{
  static public class Default extends Declarator
  {
/* type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Type type,
			  java.util.List < Variable > variables)
    {
      this.tree = tree;
      this.type = type;
      this.variables = variables;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDeclaratorDefault (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Default setType (Type x)
    {
      Default z = new Default ();
      z.$setType (x);
      return z;
    }
    private java.util.List < Variable > variables;
    public java.util.List < Variable > getVariables ()
    {
      return variables;
    }
    private void $setVariables (java.util.List < Variable > x)
    {
      this.variables = x;
    }
    public Default setVariables (java.util.List < Variable > x)
    {
      Default z = new Default ();
      z.$setVariables (x);
      return z;
    }
  }
  public class Ambiguity extends Declarator
  {
    private final java.util.List < Declarator > alternatives;
    public Ambiguity (java.util.List < Declarator > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Declarator > getAlternatives ()
    {
      return alternatives;
    }
  }
}
