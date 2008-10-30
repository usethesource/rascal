package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StrCon extends AbstractAST
{
  public class default extends StrCon
  {
/* [\"] chars:StrChar* [\"] -> StrCon {cons("default")} */
    private default ()
    {
    }
    /*package */ default (ITree tree, List < StrChar > chars)
    {
      this.tree = tree;
      this.chars = chars;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrCondefault (this);
    }
    private List < StrChar > chars;
    public List < StrChar > getchars ()
    {
      return chars;
    }
    private void privateSetchars (List < StrChar > x)
    {
      this.chars = x;
    }
    public default setchars (List < StrChar > x)
    {
      default z = new default ();
      z.privateSetchars (x);
      return z;
    }
  }
  public class Ambiguity extends StrCon
  {
    private final List < StrCon > alternatives;
    public Ambiguity (List < StrCon > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < StrCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
