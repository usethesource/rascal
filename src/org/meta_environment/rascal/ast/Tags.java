package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Tags extends AbstractAST
{
  static public class Default extends Tags
  {
/* annotations:Tag* -> Tags {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Tag > annotations)
    {
      this.tree = tree;
      this.annotations = annotations;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTagsDefault (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Tag >
      annotations;
    public java.util.List < org.meta_environment.rascal.ast.Tag >
      getAnnotations ()
    {
      return annotations;
    }
    private void $setAnnotations (java.util.List <
				  org.meta_environment.rascal.ast.Tag > x)
    {
      this.annotations = x;
    }
    public Default setAnnotations (java.util.List <
				   org.meta_environment.rascal.ast.Tag > x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setAnnotations (x);
      return z;
    }
  }
  static public class Ambiguity extends Tags
  {
    public Tags.Ambiguity makeTagsAmbiguity (java.util.List < Tags >
					     alternatives)
    {
      Tags.Ambiguity amb = new Tags.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Tags.Ambiguity) table.get (amb);
    }
    private final java.util.List < Tags > alternatives;
    public Ambiguity (java.util.List < Tags > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Tags > getAlternatives ()
    {
      return alternatives;
    }
  }
}
