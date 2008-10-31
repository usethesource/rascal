package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
    public Character getCharacter ()
    {
      return character;
    }
    private void $setCharacter (Character x)
    {
      this.character = x;
    }
    public Character setCharacter (Character x)
    {
      Character z = new Character ();
      z.$setCharacter (x);
      return z;
    }
  }
  public class Ambiguity extends CharRange
  {
    private final java.util.List < CharRange > alternatives;
    public Ambiguity (java.util.List < CharRange > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharRange > getAlternatives ()
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
    public Character getStart ()
    {
      return start;
    }
    private void $setStart (Character x)
    {
      this.start = x;
    }
    public Range setStart (Character x)
    {
      Range z = new Range ();
      z.$setStart (x);
      return z;
    }
    private Character end;
    public Character getEnd ()
    {
      return end;
    }
    private void $setEnd (Character x)
    {
      this.end = x;
    }
    public Range setEnd (Character x)
    {
      Range z = new Range ();
      z.$setEnd (x);
      return z;
    }
  }
}
