package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Case extends AbstractAST
{
  public class Rule extends Case
  {
/* "case" rule:Rule -> Case {cons("Rule")} */
    private Rule ()
    {
    }
    /*package */ Rule (ITree tree, Rule rule)
    {
      this.tree = tree;
      this.rule = rule;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCaseRule (this);
    }
    private Rule rule;
    public Rule getrule ()
    {
      return rule;
    }
    private void privateSetrule (Rule x)
    {
      this.rule = x;
    }
    public Rule setrule (Rule x)
    {
      Rule z = new Rule ();
      z.privateSetrule (x);
      return z;
    }
  }
  public class Ambiguity extends Case
  {
    private final List < Case > alternatives;
    public Ambiguity (List < Case > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Case > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Default extends Case
  {
/* "default" ":" statement:Statement -> Case {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Statement statement)
    {
      this.tree = tree;
      this.statement = statement;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCaseDefault (this);
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
    public Default setstatement (Statement x)
    {
      Default z = new Default ();
      z.privateSetstatement (x);
      return z;
    }
  }
}
