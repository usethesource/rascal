package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class EscapeSequence extends AbstractAST
{
  static public class Lexical extends EscapeSequence
  {
    /* "\\" [0-7] -> EscapeSequence  */
  } static public class Ambiguity extends EscapeSequence
  {
    public EscapeSequence.Ambiguity makeEscapeSequenceAmbiguity (java.util.
								 List <
								 EscapeSequence
								 >
								 alternatives)
    {
      EscapeSequence.Ambiguity amb =
	new EscapeSequence.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (EscapeSequence.Ambiguity) table.get (amb);
    }
    private final java.util.List < EscapeSequence > alternatives;
    public Ambiguity (java.util.List < EscapeSequence > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < EscapeSequence > getAlternatives ()
    {
      return alternatives;
    }
  }
}
