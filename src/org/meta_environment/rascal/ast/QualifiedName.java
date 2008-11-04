package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class QualifiedName extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.Name > getNames ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasNames ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends QualifiedName
  {
/* names:{Name "::"}+ -> QualifiedName {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Name > names)
    {
      this.tree = tree;
      this.names = names;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItQualifiedNameDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasNames ()
    {
      return true;
    }

    private java.util.LisT < org.meta_environment.rascal.ast.Name > names;
    public java.util.LisT < org.meta_environment.rascal.ast.Name > getNames ()
    {
      return names;
    }
    private void $setNames (java.util.LisT <
			    org.meta_environment.rascal.ast.Name > x)
    {
      this.names = x;
    }
    public Default setNames (java.util.LisT <
			     org.meta_environment.rascal.ast.Name > x)
    {
      Default z = new Default ();
      z.$setNames (x);
      return z;
    }
  }
  static public class Ambiguity extends QualifiedName
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.QualifiedName > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.QualifiedName >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.QualifiedName >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
