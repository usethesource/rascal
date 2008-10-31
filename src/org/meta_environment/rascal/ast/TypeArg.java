package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TypeArg extends AbstractAST
{
  static public class Default extends TypeArg
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeArgDefault (this);
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
    public Default setType (Type x)
    {
      Default z = new Default ();
      z.$setType (x);
      return z;
    }
  }
  public class Ambiguity extends TypeArg
  {
    private final java.util.List < TypeArg > alternatives;
    public Ambiguity (java.util.List < TypeArg > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TypeArg > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Named extends TypeArg
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeArgNamed (this);
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
    public Named setType (Type x)
    {
      Named z = new Named ();
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
    public Named setName (Name x)
    {
      Named z = new Named ();
      z.$setName (x);
      return z;
    }
  }
}
