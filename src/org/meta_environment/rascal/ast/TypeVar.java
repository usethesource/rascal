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
    /*package */ Free (ITree tree, org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVarFree (this);
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public org.meta_environment.rascal.ast.Free setName (org.meta_environment.
							 rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Free z = new Free ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends TypeVar
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
    /*package */ Bounded (ITree tree,
			  org.meta_environment.rascal.ast.Name name,
			  org.meta_environment.rascal.ast.Type bound)
    {
      this.tree = tree;
      this.name = name;
      this.bound = bound;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVarBounded (this);
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public org.meta_environment.rascal.ast.Bounded setName (org.
							    meta_environment.
							    rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Bounded z = new Bounded ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Type bound;
    public org.meta_environment.rascal.ast.Type getBound ()
    {
      return bound;
    }
    private void $setBound (org.meta_environment.rascal.ast.Type x)
    {
      this.bound = x;
    }
    public org.meta_environment.rascal.ast.Bounded setBound (org.
							     meta_environment.
							     rascal.ast.
							     Type x)
    {
      org.meta_environment.rascal.ast.Bounded z = new Bounded ();
      z.$setBound (x);
      return z;
    }
  }
}
