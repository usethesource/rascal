package org.meta_environment.rascal.ast;
public abstract class TypeArg extends AbstractAST
{
  public class Default extends TypeArg
  {
/* type:Type -> TypeArg {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Type type)
    {
      this.tree = tree;
      this.type = type;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultTypeArg (this);
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
    public Default settype (Type x)
    {
      z = new Default ();
      z.privateSettype (x);
      return z;
    }
  }
  public class Named extends TypeArg
  {
/* type:Type name:Name -> TypeArg {cons("Named")} */
    private Named ()
    {
    }
    /*package */ Named (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNamedTypeArg (this);
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
    public Named settype (Type x)
    {
      z = new Named ();
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
    public Named setname (Name x)
    {
      z = new Named ();
      z.privateSetname (x);
      return z;
    }
  }
}
