package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DecimalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends DecimalIntegerLiteral
  {
    /* "0" -> DecimalIntegerLiteral  */
  } static public class Ambiguity extends DecimalIntegerLiteral
  {
    public DecimalIntegerLiteral.
      Ambiguity makeDecimalIntegerLiteralAmbiguity (java.util.List <
						    DecimalIntegerLiteral >
						    alternatives)
    {
      DecimalIntegerLiteral.Ambiguity amb =
	new DecimalIntegerLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (DecimalIntegerLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < DecimalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List < DecimalIntegerLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DecimalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
