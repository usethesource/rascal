package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Bound extends AbstractAST
{
  public class Empty extends Bound
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
  public class Ambiguity extends Bound
  {
    private final List < Bound > alternatives;
    public Ambiguity (List < Bound > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Bound > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Default extends Bound
  {
/* "(" expression:Expression ")" -> Bound {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBoundDefault (this);
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Default setexpression (Expression x)
    {
      Default z = new Default ();
      z.$setexpression (x);
      return z;
    }
  }
}
