package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DoubleLiteral extends AbstractAST
{
  static public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [dD]? -> DoubleLiteral  */
  } static public class Ambiguity extends DoubleLiteral
  {
    public DoubleLiteral.Ambiguity makeDoubleLiteralAmbiguity (java.util.
							       List <
							       DoubleLiteral >
							       alternatives)
    {
      DoubleLiteral.Ambiguity amb =
	new DoubleLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (DoubleLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < DoubleLiteral > alternatives;
    public Ambiguity (java.util.List < DoubleLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < DoubleLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
