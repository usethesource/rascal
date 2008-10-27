package org.meta_environment.rascal.ast;
public abstract class QualifiedName extends AbstractAST
{
  public class Default extends QualifiedName
  {
    private List < Name > names;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Name > names)
    {
      this.tree = tree;
      this.names = names;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultQualifiedName (this);
    }
    private final List < Name > names;
    public List < Name > getnames ()
    {
      return names;
    }
    private void privateSetnames (List < Name > x)
    {
      this.names = x;
    }
    public Default setnames (List < Name > x)
    {
      z = new Default ();
      z.privateSetnames (x);
      return z;
    }
  }
}
