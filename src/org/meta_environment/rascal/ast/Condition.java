package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Condition extends AbstractAST
{
  public class Match extends Condition
  {
/* pattern:Pattern ":=" expression:Expression -> Condition {cons("Match")} */
    private Match ()
    {
    }
    /*package */ Match (ITree tree, Pattern pattern, Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitConditionMatch (this);
    }
    private Pattern pattern;
    public Pattern getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Pattern x)
    {
      this.pattern = x;
    }
    public Match setPattern (Pattern x)
    {
      Match z = new Match ();
      z.$setPattern (x);
      return z;
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
    public Match setExpression (Expression x)
    {
      Match z = new Match ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Ambiguity extends Condition
  {
    private final java.util.List < Condition > alternatives;
    public Ambiguity (java.util.List < Condition > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Condition > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class NoMatch extends Condition
  {
/* pattern:Pattern "!:=" expression:Expression -> Condition {cons("NoMatch")} */
    private NoMatch ()
    {
    }
    /*package */ NoMatch (ITree tree, Pattern pattern, Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitConditionNoMatch (this);
    }
    private Pattern pattern;
    public Pattern getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Pattern x)
    {
      this.pattern = x;
    }
    public NoMatch setPattern (Pattern x)
    {
      NoMatch z = new NoMatch ();
      z.$setPattern (x);
      return z;
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
    public NoMatch setExpression (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Expression extends Condition
  {
/* expression:Expression -> Condition {cons("Expression")} */
    private Expression ()
    {
    }
    /*package */ Expression (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitConditionExpression (this);
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
    public Expression setExpression (Expression x)
    {
      Expression z = new Expression ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Conjunction extends Condition
  {
/* lhs:Condition "," rhs:Condition -> Condition {left, cons("Conjunction")} */
    private Conjunction ()
    {
    }
    /*package */ Conjunction (ITree tree, Condition lhs, Condition rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitConditionConjunction (this);
    }
    private Condition lhs;
    public Condition getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Condition x)
    {
      this.lhs = x;
    }
    public Conjunction setLhs (Condition x)
    {
      Conjunction z = new Conjunction ();
      z.$setLhs (x);
      return z;
    }
    private Condition rhs;
    public Condition getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Condition x)
    {
      this.rhs = x;
    }
    public Conjunction setRhs (Condition x)
    {
      Conjunction z = new Conjunction ();
      z.$setRhs (x);
      return z;
    }
  }
}
