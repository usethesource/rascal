package org.meta_environment.rascal.ast;
public abstract class Catch extends AbstractAST
{
  public class Catch extends Catch
  {
    private Statement body;

    private Catch ()
    {
    }
    /*package */ Catch (ITree tree, Statement body)
    {
      this.tree = tree;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitCatchCatch (this);
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public Catch setbody (Statement x)
    {
      z = new Catch ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class BindingCatch extends Catch
  {
    private Type type;
    private Name name;
    private Statement body;

    private BindingCatch ()
    {
    }
    /*package */ BindingCatch (ITree tree, Type type, Name name,
			       Statement body)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBindingCatchCatch (this);
    }
    private final Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public BindingCatch settype (Type x)
    {
      z = new BindingCatch ();
      z.privateSettype (x);
      return z;
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
    public BindingCatch setname (Name x)
    {
      z = new BindingCatch ();
      z.privateSetname (x);
      return z;
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public BindingCatch setbody (Statement x)
    {
      z = new BindingCatch ();
      z.privateSetbody (x);
      return z;
    }
  }
}
