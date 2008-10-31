package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalLongLiteral extends AbstractAST
{
  static public class Lexical extends OctalLongLiteral
  {
    /* [0] [0-7]+ [lL] -> OctalLongLiteral  */
  }
  static public class Ambiguity extends OctalLongLiteral
  {
    public OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity (java.
								     util.
								     List <
								     OctalLongLiteral
								     >
								     alternatives)
    {
      OctalLongLiteral.Ambiguity amb =
	new OctalLongLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (OctalLongLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < OctalLongLiteral > alternatives;
    public Ambiguity (java.util.List < OctalLongLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < OctalLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
