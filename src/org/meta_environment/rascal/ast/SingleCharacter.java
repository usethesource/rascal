package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleCharacter extends AbstractAST
{
  static public class Lexical extends SingleCharacter
  {
    /* UnicodeEscape -> SingleCharacter  */
  } static public class Ambiguity extends SingleCharacter
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.SingleCharacter > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.SingleCharacter >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.SingleCharacter >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
