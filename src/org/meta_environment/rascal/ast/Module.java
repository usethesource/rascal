package org.meta_environment.rascal.ast;
public abstract class Module extends AbstractAST
{
  public class Module extends Module
  {
    private Header header;
    private Body body;

    private Module ()
    {
    }
    /*package */ Module (ITree tree, Header header, Body body)
    {
      this.tree = tree;
      this.header = header;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitModuleModule (this);
    }
    private final Header header;
    public Header getheader ()
    {
      return header;
    }
    private void privateSetheader (Header x)
    {
      this.header = x;
    }
    public Module setheader (Header x)
    {
      z = new Module ();
      z.privateSetheader (x);
      return z;
    }
    private final Body body;
    public Body getbody ()
    {
      return body;
    }
    private void privateSetbody (Body x)
    {
      this.body = x;
    }
    public Module setbody (Body x)
    {
      z = new Module ();
      z.privateSetbody (x);
      return z;
    }
  }
}
