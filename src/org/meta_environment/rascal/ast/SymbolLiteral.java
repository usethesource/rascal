package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class SymbolLiteral extends AbstractAST
{
  public class Lexical extends SymbolLiteral
  {
    /* "#" Name -> SymbolLiteral  */
  }
  public class Ambiguity extends SymbolLiteral
  {
    private final List < SymbolLiteral > alternatives;
    public Ambiguity (List < SymbolLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < SymbolLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
