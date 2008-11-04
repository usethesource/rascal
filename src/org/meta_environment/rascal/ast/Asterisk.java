package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class AsterisK extends AbstractAST
{
  static public class Lexical extends AsterisK
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
  static public class Ambiguity extends AsterisK
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.AsterisK >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.AsterisK > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.AsterisK >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
