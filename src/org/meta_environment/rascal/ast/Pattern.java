package org.meta_environment.rascal.ast;
public abstract class Pattern extends AbstractAST
{
  public class TypedVariable extends Pattern
  {
    private Type type;
    private Name name;

    private TypedVariable ()
    {
    }
    /*package */ TypedVariable (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTypedVariablePattern (this);
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
    public TypedVariable settype (Type x)
    {
      z = new TypedVariable ();
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
    public TypedVariable setname (Name x)
    {
      z = new TypedVariable ();
      z.privateSetname (x);
      return z;
    }
  }
}
