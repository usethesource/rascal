package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Pattern extends AbstractAST
{
  public class TypedVariable extends Pattern
  {
/* type:Type name:Name -> Pattern {cons("TypedVariable")} */
    private TypedVariable ()
    {
    }
    /*package */ TypedVariable (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypedVariablePattern (this);
    }
    private Type type;
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
      TypedVariable z = new TypedVariable ();
      z.privateSettype (x);
      return z;
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
    public TypedVariable setname (Name x)
    {
      TypedVariable z = new TypedVariable ();
      z.privateSetname (x);
      return z;
    }
  }
}
