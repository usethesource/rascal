package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class BooleanLiteral extends AbstractAST
{
  static public class Lexical extends BooleanLiteral
  {
    /* "true" -> BooleanLiteral  */
  } static public class Ambiguity extends BooleanLiteral
  {
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
