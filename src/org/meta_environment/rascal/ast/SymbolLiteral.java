package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SymbolLiteral extends AbstractAST
{
  static public class Lexical extends SymbolLiteral
  {
    /* "#" Name -> SymbolLiteral  */
  }
  public class Ambiguity extends SymbolLiteral
  {
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
