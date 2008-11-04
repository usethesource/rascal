package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class LocalVariableDeclaration extends AbstractAST
{
  public org.meta_environment.rascal.ast.Declarator getDeclarator ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasDeclarator ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends LocalVariableDeclaration
  {
/* declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.
			  Declarator declarator)
    {
      this.tree = tree;
      this.declarator = declarator;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItLocalVariableDeclarationDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasDeclarator ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Declarator declarator;
    public org.meta_environment.rascal.ast.Declarator getDeclarator ()
    {
      return declarator;
    }
    private void $setDeclarator (org.meta_environment.rascal.ast.Declarator x)
    {
      this.declarator = x;
    }
    public Default setDeclarator (org.meta_environment.rascal.ast.
				  Declarator x)
    {
      Default z = new Default ();
      z.$setDeclarator (x);
      return z;
    }
  }
  static public class Ambiguity extends LocalVariableDeclaration
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.LocalVariableDeclaration > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.
		      LocalVariableDeclaration > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT <
      org.meta_environment.rascal.ast.LocalVariableDeclaration >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public boolean isDynamic ()
  {
    return false;
  }
  static public class Dynamic extends LocalVariableDeclaration
  {
/* "dynamic" declarator:Declarator -> LocalVariableDeclaration {cons("Dynamic")} */
    private Dynamic ()
    {
    }
    /*package */ Dynamic (ITree tree,
			  org.meta_environment.rascal.ast.
			  Declarator declarator)
    {
      this.tree = tree;
      this.declarator = declarator;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItLocalVariableDeclarationDynamic (this);
    }

    public boolean isDynamic ()
    {
      return true;
    }

    public boolean hasDeclarator ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Declarator declarator;
    public org.meta_environment.rascal.ast.Declarator getDeclarator ()
    {
      return declarator;
    }
    private void $setDeclarator (org.meta_environment.rascal.ast.Declarator x)
    {
      this.declarator = x;
    }
    public Dynamic setDeclarator (org.meta_environment.rascal.ast.
				  Declarator x)
    {
      Dynamic z = new Dynamic ();
      z.$setDeclarator (x);
      return z;
    }
  }
}
