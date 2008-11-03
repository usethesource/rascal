package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Bound extends AbstractAST
{
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBoundEmpty (this);
    }
  }
  static public class Ambiguity extends Bound
  {
    private final java.util.List < org.meta_environment.rascal.ast.Bound >
      alternatives;
    public Ambiguity (java.util.List < org.meta_environment.rascal.ast.Bound >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Bound >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.Expression getExpression ()
  {
    throw new UnsupportedOperationException ();
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBoundDefault (this);
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
