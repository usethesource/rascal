package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleQuotedStrChar extends AbstractAST
{
  static public class Lexical extends SingleQuotedStrChar
  {
    /* "\\n" -> SingleQuotedStrChar  */
  } static public class Ambiguity extends SingleQuotedStrChar
  {
    public SingleQuotedStrChar.
      Ambiguity makeSingleQuotedStrCharAmbiguity (java.util.List <
						  SingleQuotedStrChar >
						  alternatives)
    {
      SingleQuotedStrChar.Ambiguity amb =
	new SingleQuotedStrChar.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (SingleQuotedStrChar.Ambiguity) table.get (amb);
    }
    private final java.util.List < SingleQuotedStrChar > alternatives;
    public Ambiguity (java.util.List < SingleQuotedStrChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SingleQuotedStrChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
