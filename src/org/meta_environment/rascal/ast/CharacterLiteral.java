package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class CharacterLiteral extends AbstractAST
{
  public class Lexical extends CharacterLiteral
  {
    /* "'" SingleCharacter "'" -> CharacterLiteral  */
  }
  public class Ambiguity extends CharacterLiteral
  {
    private final List < CharacterLiteral > alternatives;
    public Ambiguity (List < CharacterLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < CharacterLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends CharacterLiteral
  {
    /* "'" EscapeSequence "'" -> CharacterLiteral  */
  }
}
