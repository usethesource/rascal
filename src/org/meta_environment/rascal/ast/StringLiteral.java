package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StringLiteral extends AbstractAST
{
  static public class Lexical extends StringLiteral
  {
    /* "\"" StringCharacter* "\"" -> StringLiteral  */
  }
  static public class Ambiguity extends StringLiteral
  {
    public StringLiteral.Ambiguity makeStringLiteralAmbiguity (java.util.
							       List <
							       StringLiteral >
							       alternatives)
    {
      StringLiteral.Ambiguity amb =
	new StringLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (StringLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < StringLiteral > alternatives;
    public Ambiguity (java.util.List < StringLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StringLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
