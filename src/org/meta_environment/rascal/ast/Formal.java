package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Formal extends AbstractAST
{
  public class TypeName extends Formal
  {
/* type:Type name:Name -> Formal {cons("TypeName")} */
    private TypeName ()
    {
    }
    /*package */ TypeName (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeNameFormal (this);
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
    public TypeName settype (Type x)
    {
      z = new TypeName ();
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
    public TypeName setname (Name x)
    {
      z = new TypeName ();
      z.privateSetname (x);
      return z;
    }
  }
}
