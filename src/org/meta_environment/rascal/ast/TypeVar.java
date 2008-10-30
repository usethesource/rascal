package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class TypeVar extends AbstractAST
{
  public class Free extends TypeVar
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
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Free setname (Name x)
    {
      Free z = new Free ();
      z.$setname (x);
      return z;
    }
  }
  public class Ambiguity extends TypeVar
  {
    private final List < TypeVar > alternatives;
    public Ambiguity (List < TypeVar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < TypeVar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Bounded extends TypeVar
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
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Bounded setname (Name x)
    {
      Bounded z = new Bounded ();
      z.$setname (x);
      return z;
    }
    private Type bound;
    public Type getbound ()
    {
      return bound;
    }
    private void $setbound (Type x)
    {
      this.bound = x;
    }
    public Bounded setbound (Type x)
    {
      Bounded z = new Bounded ();
      z.$setbound (x);
      return z;
    }
  }
}
