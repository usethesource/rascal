package org.meta_environment.rascal.ast;
public abstract class FunctionDeclaration extends AbstractAST
{
  public class Default extends FunctionDeclaration
  {
    private Signature signature;
    private Tags tags;
    private FunctionBody body;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, Signature signature, Tags tags,
			  FunctionBody body)
    {
      this.tree = tree;
      this.signature = signature;
      this.tags = tags;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultFunctionDeclaration (this);
    }
    private final Signature signature;
    public Signature getsignature ()
    {
      return signature;
    }
    private void privateSetsignature (Signature x)
    {
      this.signature = x;
    }
    public Default setsignature (Signature x)
    {
      z = new Default ();
      z.privateSetsignature (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Default settags (Tags x)
    {
      z = new Default ();
      z.privateSettags (x);
      return z;
    }
    private final FunctionBody body;
    public FunctionBody getbody ()
    {
      return body;
    }
    private void privateSetbody (FunctionBody x)
    {
      this.body = x;
    }
    public Default setbody (FunctionBody x)
    {
      z = new Default ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class Abstract extends FunctionDeclaration
  {
    private Signature signature;
    private Tags tags;

    private Abstract ()
    {
    }
    /*package */ Abstract (ITree tree, Signature signature, Tags tags)
    {
      this.tree = tree;
      this.signature = signature;
      this.tags = tags;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAbstractFunctionDeclaration (this);
    }
    private final Signature signature;
    public Signature getsignature ()
    {
      return signature;
    }
    private void privateSetsignature (Signature x)
    {
      this.signature = x;
    }
    public Abstract setsignature (Signature x)
    {
      z = new Abstract ();
      z.privateSetsignature (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Abstract settags (Tags x)
    {
      z = new Abstract ();
      z.privateSettags (x);
      return z;
    }
  }
}
