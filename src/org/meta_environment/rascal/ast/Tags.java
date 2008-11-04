package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Tags extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.Tag >
    getAnnotations ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasAnnotations ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends Tags
  {
/* annotations:Tag* -> Tags {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Tag > annotations)
    {
      this.tree = tree;
      this.annotations = annotations;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItTagsDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasAnnotations ()
    {
      return true;
    }

    private java.util.LisT < org.meta_environment.rascal.ast.Tag >
      annotations;
    public java.util.LisT < org.meta_environment.rascal.ast.Tag >
      getAnnotations ()
    {
      return annotations;
    }
    private void $setAnnotations (java.util.LisT <
				  org.meta_environment.rascal.ast.Tag > x)
    {
      this.annotations = x;
    }
    public Default setAnnotations (java.util.LisT <
				   org.meta_environment.rascal.ast.Tag > x)
    {
      Default z = new Default ();
      z.$setAnnotations (x);
      return z;
    }
  }
  static public class Ambiguity extends Tags
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Tags >
      alternatives;
    public Ambiguity (java.util.LisT < org.meta_environment.rascal.ast.Tags >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Tags >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
