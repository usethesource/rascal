package org.meta_environment.rascal.ast;
public abstract class TypeVar extends AbstractAST
{
  public class Free extends TypeVar
  {
    private Name name;

    private Free ()
    {
    }
    /*package */ Free (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFreeTypeVar (this);
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
    public Free setname (Name x)
    {
      z = new Free ();
      z.privateSetname (x);
      return z;
    }
  }
  public class Bounded extends TypeVar
  {
    private Name name;
    private Type bound;

    private Bounded ()
    {
    }
    /*package */ Bounded (ITree tree, Name name, Type bound)
    {
      this.tree = tree;
      this.name = name;
      this.bound = bound;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBoundedTypeVar (this);
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
    public Bounded setname (Name x)
    {
      z = new Bounded ();
      z.privateSetname (x);
      return z;
    }
    private final Type bound;
    public Type getbound ()
    {
      return bound;
    }
    private void privateSetbound (Type x)
    {
      this.bound = x;
    }
    public Bounded setbound (Type x)
    {
      z = new Bounded ();
      z.privateSetbound (x);
      return z;
    }
  }
}
