package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Renaming extends AbstractAST
{
  public class Default extends Renaming
  {
/* from:Name "=>" to:Name -> Renaming {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Name from, Name to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingDefault (this);
    }
    private Name from;
    public Name getFrom ()
    {
      return from;
    }
    private void $setFrom (Name x)
    {
      this.from = x;
    }
    public Default setFrom (Name x)
    {
      Default z = new Default ();
      z.$setFrom (x);
      return z;
    }
    private Name to;
    public Name getTo ()
    {
      return to;
    }
    private void $setTo (Name x)
    {
      this.to = x;
    }
    public Default setTo (Name x)
    {
      Default z = new Default ();
      z.$setTo (x);
      return z;
    }
  }
  public class Ambiguity extends Renaming
  {
    private final java.util.List < Renaming > alternatives;
    public Ambiguity (java.util.List < Renaming > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Renaming > getAlternatives ()
    {
      return alternatives;
    }
  }
}
