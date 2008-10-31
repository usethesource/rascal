package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DecimalLongLiteral extends AbstractAST
{
  static public class Lexical extends DecimalLongLiteral
  {
    /* "0" [lL] -> DecimalLongLiteral  */
  } static public class Ambiguity extends DecimalLongLiteral
  {
    public DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity (java.
									 util.
									 List
									 <
									 DecimalLongLiteral
									 >
									 alternatives)
    {
      DecimalLongLiteral.Ambiguity amb =
	new DecimalLongLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (DecimalLongLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < DecimalLongLiteral > alternatives;
    public Ambiguity (java.util.List < DecimalLongLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DecimalLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
