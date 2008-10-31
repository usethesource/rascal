package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Return extends AbstractAST
{
  public class WithExpression extends Return
  {
/* "return" expression:Expression ";" -> Return {cons("WithExpression")} */
    private WithExpression ()
    {
    }
    /*package */ WithExpression (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitReturnWithExpression (this);
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public WithExpression setExpression (Expression x)
    {
      WithExpression z = new WithExpression ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Ambiguity extends Return
  {
    private final java.util.List < Return > alternatives;
    public Ambiguity (java.util.List < Return > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Return > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class NoExpression extends Return
  {
/* "return" ";" -> Return {cons("NoExpression")} */
    private NoExpression ()
    {
    }
    /*package */ NoExpression (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitReturnNoExpression (this);
    }
  }
}
