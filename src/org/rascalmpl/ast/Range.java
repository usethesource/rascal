package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Range extends AbstractAST { 
  public org.rascalmpl.ast.Char getCharacter() { throw new UnsupportedOperationException(); }
public boolean hasCharacter() { return false; }
public boolean isCharacter() { return false; }
static public class Character extends Range {
/** character:Char -> Range {cons("Character")} */
	protected Character(INode node, org.rascalmpl.ast.Char character) {
		this.node = node;
		this.character = character;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRangeCharacter(this);
	}

	public boolean isCharacter() { return true; }

	public boolean hasCharacter() { return true; }

private final org.rascalmpl.ast.Char character;
	public org.rascalmpl.ast.Char getCharacter() { return character; }	
}
static public class Ambiguity extends Range {
  private final java.util.List<org.rascalmpl.ast.Range> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Range> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Range> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRangeAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Char getStart() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Char getEnd() { throw new UnsupportedOperationException(); }
public boolean hasStart() { return false; }
	public boolean hasEnd() { return false; }
public boolean isFromTo() { return false; }
static public class FromTo extends Range {
/** start:Char "-" end:Char -> Range {cons("FromTo")} */
	protected FromTo(INode node, org.rascalmpl.ast.Char start, org.rascalmpl.ast.Char end) {
		this.node = node;
		this.start = start;
		this.end = end;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRangeFromTo(this);
	}

	public boolean isFromTo() { return true; }

	public boolean hasStart() { return true; }
	public boolean hasEnd() { return true; }

private final org.rascalmpl.ast.Char start;
	public org.rascalmpl.ast.Char getStart() { return start; }
	private final org.rascalmpl.ast.Char end;
	public org.rascalmpl.ast.Char getEnd() { return end; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}