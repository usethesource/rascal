package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
      return visitor.visitDefaultTag (this);
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public Default setname (Name x)
    {
      z = new Default ();
      z.privateSetname (x);
      return z;
    }
  }
}
