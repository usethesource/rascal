package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Sort extends AbstractAST
{
  static public class Lexical extends Sort
  {
    private String string;
    /*package */ Lexical (ITree tree, String string)
    {
      this.tree = tree;
      this.string = string;
    }
    public String getString ()
    {
      return string;
    }
  }
  static public class Ambiguity extends Sort
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Sort >
      alternatives;
    public Ambiguity (java.util.LisT < org.meta_environment.rascal.ast.Sort >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Sort >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
