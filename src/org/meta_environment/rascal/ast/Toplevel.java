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
    /*package */ GivenVisibility (ITree tree,
				  org.meta_environment.rascal.ast.
				  Visibility visibility,
				  org.meta_environment.rascal.ast.
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
    private org.meta_environment.rascal.ast.Visibility visibility;
    public org.meta_environment.rascal.ast.Visibility getVisibility ()
    {
      return visibility;
    }
    private void $setVisibility (org.meta_environment.rascal.ast.Visibility x)
    {
      this.visibility = x;
    }
    public org.meta_environment.rascal.ast.GivenVisibility setVisibility (org.
									  meta_environment.
									  rascal.
									  ast.
									  Visibility
									  x)
    {
      org.meta_environment.rascal.ast.GivenVisibility z =
	new GivenVisibility ();
      z.$setVisibility (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Declaration declaration;
    public org.meta_environment.rascal.ast.Declaration getDeclaration ()
    {
      return declaration;
    }
    private void $setDeclaration (org.meta_environment.rascal.ast.
				  Declaration x)
    {
      this.declaration = x;
    }
    public org.meta_environment.rascal.ast.
      GivenVisibility setDeclaration (org.meta_environment.rascal.ast.
				      Declaration x)
    {
      org.meta_environment.rascal.ast.GivenVisibility z =
	new GivenVisibility ();
      z.$setDeclaration (x);
      return z;
    }
  }
  static public class Ambiguity extends Toplevel
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
    /*package */ DefaultVisibility (ITree tree,
				    org.meta_environment.rascal.ast.
				    Declaration declaration)
    {
      this.tree = tree;
      this.declaration = declaration;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitToplevelDefaultVisibility (this);
    }
    private org.meta_environment.rascal.ast.Declaration declaration;
    public org.meta_environment.rascal.ast.Declaration getDeclaration ()
    {
      return declaration;
    }
    private void $setDeclaration (org.meta_environment.rascal.ast.
				  Declaration x)
    {
      this.declaration = x;
    }
    public org.meta_environment.rascal.ast.
      DefaultVisibility setDeclaration (org.meta_environment.rascal.ast.
					Declaration x)
    {
      org.meta_environment.rascal.ast.DefaultVisibility z =
	new DefaultVisibility ();
      z.$setDeclaration (x);
      return z;
    }
  }
}
