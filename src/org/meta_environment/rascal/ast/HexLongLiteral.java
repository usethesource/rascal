package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class HexLongLiteral extends AbstractAST
{
  static public class Lexical extends HexLongLiteral
  {
    /* [0] [xX] [0-9a-fA-F]+ [lL] -> HexLongLiteral  */
  }
  static public class Ambiguity extends HexLongLiteral
  {
    public HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity (java.util.
								 List <
								 HexLongLiteral
								 >
								 alternatives)
    {
      HexLongLiteral.Ambiguity amb =
	new HexLongLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (HexLongLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < HexLongLiteral > alternatives;
    public Ambiguity (java.util.List < HexLongLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < HexLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
