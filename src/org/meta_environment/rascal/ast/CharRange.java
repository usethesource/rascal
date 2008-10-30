package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class CharRange extends AbstractAST
{
  public class Character extends CharRange
  {
/* character:Character -> CharRange {cons("Character")} */
    private Character ()
    {
    }
    /*package */ Character (ITree tree, Character character)
    {
      this.tree = tree;
      this.character = character;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangeCharacter (this);
    }
    private Character character;
    public Character getcharacter ()
    {
      return character;
    }
    private void $setcharacter (Character x)
    {
      this.character = x;
    }
    public Character setcharacter (Character x)
    {
      Character z = new Character ();
      z.$setcharacter (x);
      return z;
    }
  }
  public class Ambiguity extends CharRange
  {
    private final List < CharRange > alternatives;
    public Ambiguity (List < CharRange > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < CharRange > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Range extends CharRange
  {
/* start:Character "-" end:Character -> CharRange {cons("Range")} */
    private Range ()
    {
    }
    /*package */ Range (ITree tree, Character start, Character end)
    {
      this.tree = tree;
      this.start = start;
      this.end = end;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangeRange (this);
    }
    private Character start;
    public Character getstart ()
    {
      return start;
    }
    private void $setstart (Character x)
    {
      this.start = x;
    }
    public Range setstart (Character x)
    {
      Range z = new Range ();
      z.$setstart (x);
      return z;
    }
    private Character end;
    public Character getend ()
    {
      return end;
    }
    private void $setend (Character x)
    {
      this.end = x;
    }
    public Range setend (Character x)
    {
      Range z = new Range ();
      z.$setend (x);
      return z;
    }
  }
}
