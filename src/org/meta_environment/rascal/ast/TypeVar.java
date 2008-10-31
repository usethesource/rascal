package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TypeVar extends AbstractAST
{
  static public class Free extends TypeVar
  {
/* "&" name:Name -> TypeVar {cons("Free")} */
    private Free ()
    {
    }
    /*package */ Free (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVarFree (this);
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
    public Free setName (Name x)
    {
      Free z = new Free ();
      z.$setName (x);
      return z;
    }
  }
  public class Ambiguity extends TypeVar
  {
    private final java.util.List < TypeVar > alternatives;
    public Ambiguity (java.util.List < TypeVar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < TypeVar > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Bounded extends TypeVar
  {
/* "&" name:Name "<:" bound:Type -> TypeVar {cons("Bounded")} */
    private Bounded ()
    {
    }
    /*package */ Bounded (ITree tree, Name name, Type bound)
    {
      this.tree = tree;
      this.name = name;
      this.bound = bound;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVarBounded (this);
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
    public Bounded setName (Name x)
    {
      Bounded z = new Bounded ();
      z.$setName (x);
      return z;
    }
    private Type bound;
    public Type getBound ()
    {
      return bound;
    }
    private void $setBound (Type x)
    {
      this.bound = x;
    }
    public Bounded setBound (Type x)
    {
      Bounded z = new Bounded ();
      z.$setBound (x);
      return z;
    }
  }
}
