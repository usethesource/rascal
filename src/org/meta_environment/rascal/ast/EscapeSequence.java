package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class EscapeSequence extends AbstractAST
{
  static public class Lexical extends EscapeSequence
  {
    /* "\\" [0-7] -> EscapeSequence  */
  } static public class Ambiguity extends EscapeSequence
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.EscapeSequence > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.EscapeSequence >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.EscapeSequence >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
