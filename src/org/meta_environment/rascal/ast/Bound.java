package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Bound extends AbstractAST
{
  public boolean isEmpty ()
  {
    return false;
  }
  static public class Empty extends Bound
  {
/*  -> Bound {cons("Empty")} */
    private Empty ()
    {
    }
    /*package */ Empty (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBoundEmpty (this);
    }

    public boolean isEmpty ()
    {
      return true;
    }
  }
  static public class Ambiguity extends Bound
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Bound >
      alternatives;
    public Ambiguity (java.util.LisT < org.meta_environment.rascal.ast.Bound >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Bound >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.Expression getExpression ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasExpression ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends Bound
  {
/* "(" expression:Expression ")" -> Bound {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.
			  Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItBoundDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasExpression ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public Default setExpression (org.meta_environment.rascal.ast.
				  Expression x)
    {
      Default z = new Default ();
      z.$setExpression (x);
      return z;
    }
  }
}
