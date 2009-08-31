package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharRange extends AbstractAST { 
  public org.meta_environment.rascal.ast.Character getCharacter() { throw new UnsupportedOperationException(); }
public boolean hasCharacter() { return false; }
public boolean isCharacter() { return false; }
static public class Character extends CharRange {
/** character:Character -> CharRange {cons("Character")} */
	private Character() {
		super();
	}
	public Character(INode node, org.meta_environment.rascal.ast.Character character) {
		this.node = node;
		this.character = character;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangeCharacter(this);
	}

	public boolean isCharacter() { return true; }

	public boolean hasCharacter() { return true; }

private org.meta_environment.rascal.ast.Character character;
	public org.meta_environment.rascal.ast.Character getCharacter() { return character; }
	private void $setCharacter(org.meta_environment.rascal.ast.Character x) { this.character = x; }
	public Character setCharacter(org.meta_environment.rascal.ast.Character x) { 
		Character z = new Character();
 		z.$setCharacter(x);
		return z;
	}	
}
static public class Ambiguity extends CharRange {
  private final java.util.List<org.meta_environment.rascal.ast.CharRange> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRange> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.CharRange> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharRangeAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Character getStart() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Character getEnd() { throw new UnsupportedOperationException(); }
public boolean hasStart() { return false; }
	public boolean hasEnd() { return false; }
public boolean isRange() { return false; }
static public class Range extends CharRange {
/** start:Character "-" end:Character -> CharRange {cons("Range")} */
	private Range() {
		super();
	}
	public Range(INode node, org.meta_environment.rascal.ast.Character start, org.meta_environment.rascal.ast.Character end) {
		this.node = node;
		this.start = start;
		this.end = end;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangeRange(this);
	}

	public boolean isRange() { return true; }

	public boolean hasStart() { return true; }
	public boolean hasEnd() { return true; }

private org.meta_environment.rascal.ast.Character start;
	public org.meta_environment.rascal.ast.Character getStart() { return start; }
	private void $setStart(org.meta_environment.rascal.ast.Character x) { this.start = x; }
	public Range setStart(org.meta_environment.rascal.ast.Character x) { 
		Range z = new Range();
 		z.$setStart(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Character end;
	public org.meta_environment.rascal.ast.Character getEnd() { return end; }
	private void $setEnd(org.meta_environment.rascal.ast.Character x) { this.end = x; }
	public Range setEnd(org.meta_environment.rascal.ast.Character x) { 
		Range z = new Range();
 		z.$setEnd(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}