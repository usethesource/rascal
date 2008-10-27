package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variant extends AbstractAST
{
  public class Type extends Variant
  {
/* type:Type name:Name -> Variant {cons("Type")} */
    private Type ()
    {
    }
    /*package */ Type (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVariant (this);
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
    public Type settype (Type x)
    {
      z = new Type ();
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
    public Type setname (Name x)
    {
      z = new Type ();
      z.privateSetname (x);
      return z;
    }
  }
  public class NAryConstructor extends Variant
  {
/* name:Name "(" arguments:{TypeArg ","}+ ")" -> Variant {cons("NAryConstructor")} */
    private NAryConstructor ()
    {
    }
    /*package */ NAryConstructor (ITree tree, Name name,
				  List < TypeArg > arguments)
    {
      this.tree = tree;
      this.name = name;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNAryConstructorVariant (this);
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
    public NAryConstructor setname (Name x)
    {
      z = new NAryConstructor ();
      z.privateSetname (x);
      return z;
    }
    private List < TypeArg > arguments;
    public List < TypeArg > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < TypeArg > x)
    {
      this.arguments = x;
    }
    public NAryConstructor setarguments (List < TypeArg > x)
    {
      NAryConstructor z = new NAryConstructor ();
      z.privateSetarguments (x);
      return z;
    }
  }
  public class NillaryConstructor extends Variant
  {
/* name:Name -> Variant {cons("NillaryConstructor")} */
    private NillaryConstructor ()
    {
    }
    /*package */ NillaryConstructor (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNillaryConstructorVariant (this);
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
    public NillaryConstructor setname (Name x)
    {
      z = new NillaryConstructor ();
      z.privateSetname (x);
      return z;
    }
  }
}
