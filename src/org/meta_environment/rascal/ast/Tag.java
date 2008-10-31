package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Tag extends AbstractAST
{
  static public class Default extends Tag
  {
/* "@" name:Name TagString -> Tag {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTagDefault (this);
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
    public org.meta_environment.rascal.ast.Default setName (org.
							    meta_environment.
							    rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends Tag
  {
    private final java.util.List < Tag > alternatives;
    public Ambiguity (java.util.List < Tag > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Tag > getAlternatives ()
    {
      return alternatives;
    }
  }
}
