package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void $setpattern (Pattern x)
    {
      this.pattern = x;
    }
    public Match setpattern (Pattern x)
    {
      Match z = new Match ();
      z.$setpattern (x);
      return z;
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
    public Match setexpression (Expression x)
    {
      Match z = new Match ();
      z.$setexpression (x);
      return z;
    }
  }
  public class Ambiguity extends Condition
  {
    private final List < Condition > alternatives;
    public Ambiguity (List < Condition > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Condition > getAlternatives ()
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
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void $setpattern (Pattern x)
    {
      this.pattern = x;
    }
    public NoMatch setpattern (Pattern x)
    {
      NoMatch z = new NoMatch ();
      z.$setpattern (x);
      return z;
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
    public NoMatch setexpression (Expression x)
    {
      NoMatch z = new NoMatch ();
      z.$setexpression (x);
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
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public Expression setexpression (Expression x)
    {
      Expression z = new Expression ();
      z.$setexpression (x);
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
    public Condition getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Condition x)
    {
      this.lhs = x;
    }
    public Conjunction setlhs (Condition x)
    {
      Conjunction z = new Conjunction ();
      z.$setlhs (x);
      return z;
    }
    private Condition rhs;
    public Condition getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Condition x)
    {
      this.rhs = x;
    }
    public Conjunction setrhs (Condition x)
    {
      Conjunction z = new Conjunction ();
      z.$setrhs (x);
      return z;
    }
  }
}
