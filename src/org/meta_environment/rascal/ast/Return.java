package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Return extends AbstractAST
{
  static public class WithExpression extends Return
  {
/* "return" expression:Expression ";" -> Return {cons("WithExpression")} */
    private WithExpression ()
    {
    }
    /*package */ WithExpression (ITree tree,
				 org.meta_environment.rascal.ast.
				 Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitReturnWithExpression (this);
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
    public WithExpression setExpression (org.meta_environment.rascal.ast.
					 Expression x)
    {
      org.meta_environment.rascal.ast.WithExpression z =
	new WithExpression ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Ambiguity extends Return
  {
    public Return.Ambiguity makeReturnAmbiguity (java.util.List < Return >
						 alternatives)
    {
      Return.Ambiguity amb = new Return.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Return.Ambiguity) table.get (amb);
    }
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
  static public class NoExpression extends Return
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
