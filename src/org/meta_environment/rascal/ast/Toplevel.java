package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Toplevel extends AbstractAST
{
  static public class GivenVisibility extends Toplevel
  {
/* visibility:Visibility declaration:Declaration -> Toplevel {cons("GivenVisibility")} */
    private GivenVisibility ()
    {
    }
    /*package */ GivenVisibility (ITree tree, Visibility visibility,
				  Declaration declaration)
    {
      this.tree = tree;
      this.visibility = visibility;
      this.declaration = declaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitToplevelGivenVisibility (this);
    }
    private Visibility visibility;
    public Visibility getVisibility ()
    {
      return visibility;
    }
    private void $setVisibility (Visibility x)
    {
      this.visibility = x;
    }
    public GivenVisibility setVisibility (Visibility x)
    {
      GivenVisibility z = new GivenVisibility ();
      z.$setVisibility (x);
      return z;
    }
    private Declaration declaration;
    public Declaration getDeclaration ()
    {
      return declaration;
    }
    private void $setDeclaration (Declaration x)
    {
      this.declaration = x;
    }
    public GivenVisibility setDeclaration (Declaration x)
    {
      GivenVisibility z = new GivenVisibility ();
      z.$setDeclaration (x);
      return z;
    }
  }
  public class Ambiguity extends Toplevel
  {
    private final java.util.List < Toplevel > alternatives;
    public Ambiguity (java.util.List < Toplevel > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Toplevel > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class DefaultVisibility extends Toplevel
  {
/* declaration:Declaration -> Toplevel {cons("DefaultVisibility")} */
    private DefaultVisibility ()
    {
    }
    /*package */ DefaultVisibility (ITree tree, Declaration declaration)
    {
      this.tree = tree;
      this.declaration = declaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitToplevelDefaultVisibility (this);
    }
    private Declaration declaration;
    public Declaration getDeclaration ()
    {
      return declaration;
    }
    private void $setDeclaration (Declaration x)
    {
      this.declaration = x;
    }
    public DefaultVisibility setDeclaration (Declaration x)
    {
      DefaultVisibility z = new DefaultVisibility ();
      z.$setDeclaration (x);
      return z;
    }
  }
}
