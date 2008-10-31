package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleCharacter extends AbstractAST
{
  static public class Lexical extends SingleCharacter
  {
    /* UnicodeEscape -> SingleCharacter  */
  } static public class Ambiguity extends SingleCharacter
  {
    public SingleCharacter.Ambiguity makeSingleCharacterAmbiguity (java.util.
								   List <
								   SingleCharacter
								   >
								   alternatives)
    {
      SingleCharacter.Ambiguity amb =
	new SingleCharacter.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (SingleCharacter.Ambiguity) table.get (amb);
    }
    private final java.util.List < SingleCharacter > alternatives;
    public Ambiguity (java.util.List < SingleCharacter > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SingleCharacter > getAlternatives ()
    {
      return alternatives;
    }
  }
}
