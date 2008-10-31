package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Case extends AbstractAST
{
  static public class Rule extends Case
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
    public Rule getRule ()
    {
      return rule;
    }
    private void $setRule (Rule x)
    {
      this.rule = x;
    }
    public Rule setRule (Rule x)
    {
      Rule z = new Rule ();
      z.$setRule (x);
      return z;
    }
  }
  public class Ambiguity extends Case
  {
    private final java.util.List < Case > alternatives;
    public Ambiguity (java.util.List < Case > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Case > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Default extends Case
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
    public Statement getStatement ()
    {
      return statement;
    }
    private void $setStatement (Statement x)
    {
      this.statement = x;
    }
    public Default setStatement (Statement x)
    {
      Default z = new Default ();
      z.$setStatement (x);
      return z;
    }
  }
}
