package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Formals extends AbstractAST
{
  static public class Default extends Formals
  {
/* formals:{Formal ","}* -> Formals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.List <
			  org.meta_environment.rascal.ast.Formal > formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFormalsDefault (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Formal > formals;
    public java.util.List < org.meta_environment.rascal.ast.Formal >
      getFormals ()
    {
      return formals;
    }
    private void $setFormals (java.util.List <
			      org.meta_environment.rascal.ast.Formal > x)
    {
      this.formals = x;
    }
    public Default setFormals (java.util.List <
			       org.meta_environment.rascal.ast.Formal > x)
    {
      Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  static public class Ambiguity extends Formals
  {
    private final java.util.List < org.meta_environment.rascal.ast.Formals >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Formals > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Formals >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
