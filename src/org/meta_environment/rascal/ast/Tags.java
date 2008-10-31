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
    /*package */ Default (ITree tree,
			  java.util.List <
			  org.meta_environment.rascal.ast.Tag > annotations)
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
    private final java.util.List < org.meta_environment.rascal.ast.Tags >
      alternatives;
    public Ambiguity (java.util.List < org.meta_environment.rascal.ast.Tags >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Tags >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
