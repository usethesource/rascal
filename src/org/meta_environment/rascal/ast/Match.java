package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Match extends AbstractAST
{
  public class Replacing extends Match
  {
/* match:Pattern "=>" replacement:Expression -> Match {cons("Replacing")} */
    private Replacing ()
    {
    }
    /*package */ Replacing (ITree tree, Pattern match, Expression replacement)
    {
      this.tree = tree;
      this.match = match;
      this.replacement = replacement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitReplacingMatch (this);
    }
    private Pattern match;
    public Pattern getmatch ()
    {
      return match;
    }
    private void privateSetmatch (Pattern x)
    {
      this.match = x;
    }
    public Replacing setmatch (Pattern x)
    {
      z = new Replacing ();
      z.privateSetmatch (x);
      return z;
    }
    private Expression replacement;
    public Expression getreplacement ()
    {
      return replacement;
    }
    private void privateSetreplacement (Expression x)
    {
      this.replacement = x;
    }
    public Replacing setreplacement (Expression x)
    {
      z = new Replacing ();
      z.privateSetreplacement (x);
      return z;
    }
  }
  public class Arbitrary extends Match
  {
/* match:Pattern ":" statement:Statement -> Match {cons("Arbitrary")} */
    private Arbitrary ()
    {
    }
    /*package */ Arbitrary (ITree tree, Pattern match, Statement statement)
    {
      this.tree = tree;
      this.match = match;
      this.statement = statement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitArbitraryMatch (this);
    }
    private Pattern match;
    public Pattern getmatch ()
    {
      return match;
    }
    private void privateSetmatch (Pattern x)
    {
      this.match = x;
    }
    public Arbitrary setmatch (Pattern x)
    {
      z = new Arbitrary ();
      z.privateSetmatch (x);
      return z;
    }
    private Statement statement;
    public Statement getstatement ()
    {
      return statement;
    }
    private void privateSetstatement (Statement x)
    {
      this.statement = x;
    }
    public Arbitrary setstatement (Statement x)
    {
      z = new Arbitrary ();
      z.privateSetstatement (x);
      return z;
    }
  }
}
