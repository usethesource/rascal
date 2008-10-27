package org.meta_environment.rascal.ast;
public abstract class Toplevel extends AbstractAST
{
  public class GivenVisibility extends Toplevel
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGivenVisibilityToplevel (this);
    }
    private final Visibility visibility;
    public Visibility getvisibility ()
    {
      return visibility;
    }
    private void privateSetvisibility (Visibility x)
    {
      this.visibility = x;
    }
    public GivenVisibility setvisibility (Visibility x)
    {
      z = new GivenVisibility ();
      z.privateSetvisibility (x);
      return z;
    }
    private final Declaration declaration;
    public Declaration getdeclaration ()
    {
      return declaration;
    }
    private void privateSetdeclaration (Declaration x)
    {
      this.declaration = x;
    }
    public GivenVisibility setdeclaration (Declaration x)
    {
      z = new GivenVisibility ();
      z.privateSetdeclaration (x);
      return z;
    }
  }
  public class DefaultVisibility extends Toplevel
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultVisibilityToplevel (this);
    }
    private final Declaration declaration;
    public Declaration getdeclaration ()
    {
      return declaration;
    }
    private void privateSetdeclaration (Declaration x)
    {
      this.declaration = x;
    }
    public DefaultVisibility setdeclaration (Declaration x)
    {
      z = new DefaultVisibility ();
      z.privateSetdeclaration (x);
      return z;
    }
  }
}
