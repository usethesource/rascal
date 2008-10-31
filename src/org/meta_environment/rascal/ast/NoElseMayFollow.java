package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NoElseMayFollow extends AbstractAST
{
  static public class Lexical extends NoElseMayFollow
  {
    /*  -> NoElseMayFollow  */
  }
  static public class Ambiguity extends NoElseMayFollow
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.NoElseMayFollow > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.NoElseMayFollow >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.NoElseMayFollow >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
