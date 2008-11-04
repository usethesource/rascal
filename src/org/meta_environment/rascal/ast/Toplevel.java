package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Toplevel extends AbstractAST
{
  public org.meta_environment.rascal.ast.VisIbility getVisIbility ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Declaration getDeclaration ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasVisIbility ()
  {
    return false;
  }
  public boolean hasDeclaration ()
  {
    return false;
  }
  public boolean isGivenVisIbility ()
  {
    return false;
  }
  static public class GivenVisIbility extends Toplevel
  {
/* visIbility:VisIbility declaration:Declaration -> Toplevel {cons("GivenVisIbility")} */
    private GivenVisIbility ()
    {
    }
    /*package */ GivenVisIbility (ITree tree,
				  org.meta_environment.rascal.ast.
				  VisIbility visIbility,
				  org.meta_environment.rascal.ast.
				  Declaration declaration)
    {
      this.tree = tree;
      this.visIbility = visIbility;
      this.declaration = declaration;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItToplevelGivenVisIbility (this);
    }

    public boolean isGivenVisIbility ()
    {
      return true;
    }

    public boolean hasVisIbility ()
    {
      return true;
    }
    public boolean hasDeclaration ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.VisIbility visIbility;
    public org.meta_environment.rascal.ast.VisIbility getVisIbility ()
    {
      return visIbility;
    }
    private void $setVisIbility (org.meta_environment.rascal.ast.VisIbility x)
    {
      this.visIbility = x;
    }
    public GivenVisIbility setVisIbility (org.meta_environment.rascal.ast.
					  VisIbility x)
    {
      GivenVisIbility z = new GivenVisIbility ();
      z.$setVisIbility (x);
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
    public GivenVisIbility setDeclaration (org.meta_environment.rascal.ast.
					   Declaration x)
    {
      GivenVisIbility z = new GivenVisIbility ();
      z.$setDeclaration (x);
      return z;
    }
  }
  static public class Ambiguity extends Toplevel
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Toplevel >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Toplevel > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Toplevel >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public boolean isDefaultVisIbility ()
  {
    return false;
  }
  static public class DefaultVisIbility extends Toplevel
  {
/* declaration:Declaration -> Toplevel {cons("DefaultVisIbility")} */
    private DefaultVisIbility ()
    {
    }
    /*package */ DefaultVisIbility (ITree tree,
				    org.meta_environment.rascal.ast.
				    Declaration declaration)
    {
      this.tree = tree;
      this.declaration = declaration;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItToplevelDefaultVisIbility (this);
    }

    public boolean isDefaultVisIbility ()
    {
      return true;
    }

    public boolean hasDeclaration ()
    {
      return true;
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
    public DefaultVisIbility setDeclaration (org.meta_environment.rascal.ast.
					     Declaration x)
    {
      DefaultVisIbility z = new DefaultVisIbility ();
      z.$setDeclaration (x);
      return z;
    }
  }
}
