package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class BooleanLiteral extends AbstractAST
{
  static public class Lexical extends BooleanLiteral
  {
    /* "true" -> BooleanLiteral  */
  } static public class Ambiguity extends BooleanLiteral
  {
    public BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity (java.util.
								 List <
								 BooleanLiteral
								 >
								 alternatives)
    {
      BooleanLiteral.Ambiguity amb =
	new BooleanLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (BooleanLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < BooleanLiteral > alternatives;
    public Ambiguity (java.util.List < BooleanLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < BooleanLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
