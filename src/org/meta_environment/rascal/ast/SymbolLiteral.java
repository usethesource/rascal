package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SymbolLiteral extends AbstractAST
{
  static public class Lexical extends SymbolLiteral
  {
    /* "#" Name -> SymbolLiteral  */
  }
  static public class Ambiguity extends SymbolLiteral
  {
    public SymbolLiteral.Ambiguity makeSymbolLiteralAmbiguity (java.util.
							       List <
							       SymbolLiteral >
							       alternatives)
    {
      SymbolLiteral.Ambiguity amb =
	new SymbolLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (SymbolLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < SymbolLiteral > alternatives;
    public Ambiguity (java.util.List < SymbolLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SymbolLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
