package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Tag extends AbstractAST
{
  public class Default extends Tag
  {
/* "@" name:Name TagString -> Tag {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTagDefault (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Default setName (Name x)
    {
      Default z = new Default ();
      z.$setName (x);
      return z;
    }
  }
  public class Ambiguity extends Tag
  {
    private final java.util.List < Tag > alternatives;
    public Ambiguity (java.util.List < Tag > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Tag > getAlternatives ()
    {
      return alternatives;
    }
  }
}
