package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Formal extends AbstractAST
{
  static public class TypeName extends Formal
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
      return visitor.visitFormalTypeName (this);
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
    public TypeName setType (Type x)
    {
      TypeName z = new TypeName ();
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
    public TypeName setName (Name x)
    {
      TypeName z = new TypeName ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends Formal
  {
    private final java.util.List < Formal > alternatives;
    public Ambiguity (java.util.List < Formal > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Formal > getAlternatives ()
    {
      return alternatives;
    }
  }
}
