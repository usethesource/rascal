package org.meta_environment.rascal.ast;
public abstract class Annotation extends AbstractAST
{
  public class Default extends Annotation
  {
    private Name name;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultAnnotation (this);
    }
    private final Name name;
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
