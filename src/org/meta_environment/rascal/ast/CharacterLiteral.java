package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class CharacterLiteral extends AbstractAST
{
  public class Lexical extends CharacterLiteral
  {
    /* "'" SingleCharacter "'" -> CharacterLiteral  */
  }
  public class Ambiguity extends CharacterLiteral
  {
    private final java.util.List < CharacterLiteral > alternatives;
    public Ambiguity (java.util.List < CharacterLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharacterLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
