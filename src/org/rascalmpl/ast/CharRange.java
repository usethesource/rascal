package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharRange extends AbstractAST { 
  public org.rascalmpl.ast.Character getCharacter() { throw new UnsupportedOperationException(); }
public boolean hasCharacter() { return false; }
public boolean isCharacter() { return false; }
static public class Character extends CharRange {
/** character:Character -> CharRange {cons("Character")} */
	public Character(INode node, org.rascalmpl.ast.Character character) {
		this.node = node;
		this.character = character;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangeCharacter(this);
	}

	@Override
	public boolean isCharacter() { return true; }

	@Override
	public boolean hasCharacter() { return true; }

private final org.rascalmpl.ast.Character character;
	@Override
	public org.rascalmpl.ast.Character getCharacter() { return character; }	
}
static public class Ambiguity extends CharRange {
  private final java.util.List<org.rascalmpl.ast.CharRange> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharRange> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharRange> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharRangeAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Character getStart() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Character getEnd() { throw new UnsupportedOperationException(); }
public boolean hasStart() { return false; }
	public boolean hasEnd() { return false; }
public boolean isRange() { return false; }
static public class Range extends CharRange {
/** start:Character "-" end:Character -> CharRange {cons("Range")} */
	public Range(INode node, org.rascalmpl.ast.Character start, org.rascalmpl.ast.Character end) {
		this.node = node;
		this.start = start;
		this.end = end;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangeRange(this);
	}

	@Override
	public boolean isRange() { return true; }

	@Override
	public boolean hasStart() { return true; }
	@Override
	public boolean hasEnd() { return true; }

private final org.rascalmpl.ast.Character start;
	@Override
	public org.rascalmpl.ast.Character getStart() { return start; }
	private final org.rascalmpl.ast.Character end;
	@Override
	public org.rascalmpl.ast.Character getEnd() { return end; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}