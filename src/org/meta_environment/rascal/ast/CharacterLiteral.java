package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CharacterLiteral extends AbstractAST
{
  static public class Lexical extends CharacterLiteral
  {
    /* "'" SingleCharacter "'" -> CharacterLiteral  */
  } static public class Ambiguity extends CharacterLiteral
  {
    public CharacterLiteral.Ambiguity makeCharacterLiteralAmbiguity (java.
								     util.
								     List <
								     CharacterLiteral
								     >
								     alternatives)
    {
      CharacterLiteral.Ambiguity amb =
	new CharacterLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (CharacterLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < CharacterLiteral > alternatives;
    public Ambiguity (java.util.List < CharacterLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharacterLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
