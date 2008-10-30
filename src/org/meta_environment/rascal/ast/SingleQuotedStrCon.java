package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleQuotedStrCon extends AbstractAST
{
  public class default extends SingleQuotedStrCon
  {
/* [\'] chars:SingleQuotedStrChar* [\'] -> SingleQuotedStrCon {cons("default")} */
    private default ()
    {
    }
    /*package */ default (ITree tree, List < SingleQuotedStrChar > chars)
    {
      this.tree = tree;
      this.chars = chars;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSingleQuotedStrCondefault (this);
    }
    private List < SingleQuotedStrChar > chars;
    public List < SingleQuotedStrChar > getchars ()
    {
      return chars;
    }
    private void privateSetchars (List < SingleQuotedStrChar > x)
    {
      this.chars = x;
    }
    public default setchars (List < SingleQuotedStrChar > x)
    {
      default z = new default ();
      z.privateSetchars (x);
      return z;
    }
  }
  public class Ambiguity extends SingleQuotedStrCon
  {
    private final List < SingleQuotedStrCon > alternatives;
    public Ambiguity (List < SingleQuotedStrCon > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < SingleQuotedStrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
