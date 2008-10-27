package org.meta_environment.rascal.ast;
public abstract class FunctionDeclaration extends AbstractAST
{
  public class Default extends FunctionDeclaration
  {
    private Signature signature;
    private Annotations annotations;
    private FunctionBody body;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, Signature signature,
			  Annotations annotations, FunctionBody body)
    {
      this.tree = tree;
      this.signature = signature;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Default setannotations (Annotations x)
    {
      z = new Default ();
      z.privateSetannotations (x);
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
    private Annotations annotations;

    private Abstract ()
    {
    }
    /*package */ Abstract (ITree tree, Signature signature,
			   Annotations annotations)
    {
      this.tree = tree;
      this.signature = signature;
      this.annotations = annotations;
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
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public Abstract setannotations (Annotations x)
    {
      z = new Abstract ();
      z.privateSetannotations (x);
      return z;
    }
  }
}
