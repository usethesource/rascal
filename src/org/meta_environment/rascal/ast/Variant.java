package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
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
      return visitor.visitVariantType (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public Type setType (Type x)
    {
      Type z = new Type ();
      z.$setType (x);
      return z;
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Type setName (Name x)
    {
      Type z = new Type ();
      z.$setName (x);
      return z;
    }
  }
  public class Ambiguity extends Variant
  {
    private final java.util.List < Variant > alternatives;
    public Ambiguity (java.util.List < Variant > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Variant > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class NAryConstructor extends Variant
  {
/* name:Name "(" arguments:{TypeArg ","}+ ")" -> Variant {cons("NAryConstructor")} */
    private NAryConstructor ()
    {
    }
    /*package */ NAryConstructor (ITree tree, Name name,
				  java.util.List < TypeArg > arguments)
    {
      this.tree = tree;
      this.name = name;
    params2statements (java.util.List < TypeArg > arguments)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariantNAryConstructor (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public NAryConstructor setName (Name x)
    {
      NAryConstructor z = new NAryConstructor ();
      z.$setName (x);
      return z;
    }
    private java.util.List < TypeArg > arguments;
    public java.util.List < TypeArg > getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List < TypeArg > x)
    {
      this.arguments = x;
    }
    public NAryConstructor setArguments (java.util.List < TypeArg > x)
    {
      NAryConstructor z = new NAryConstructor ();
      z.$setArguments (x);
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
      return visitor.visitVariantNillaryConstructor (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public NillaryConstructor setName (Name x)
    {
      NillaryConstructor z = new NillaryConstructor ();
      z.$setName (x);
      return z;
    }
  }
}
