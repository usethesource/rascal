package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends OctalIntegerLiteral
  {
    /* [0] [0-7]+ -> OctalIntegerLiteral  */
  }
  static public class Ambiguity extends OctalIntegerLiteral
  {
    public OctalIntegerLiteral.
      Ambiguity makeOctalIntegerLiteralAmbiguity (java.util.List <
						  OctalIntegerLiteral >
						  alternatives)
    {
      OctalIntegerLiteral.Ambiguity amb =
	new OctalIntegerLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (OctalIntegerLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < OctalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List < OctalIntegerLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
