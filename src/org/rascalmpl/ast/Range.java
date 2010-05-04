package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Range extends AbstractAST { 
  public org.rascalmpl.ast.Char getCharacter() { throw new UnsupportedOperationException(); }
public boolean hasCharacter() { return false; }
public boolean isCharacter() { return false; }
static public class Character extends Range {
/** character:Char -> Range {cons("Character")} */
	public Character(INode node, org.rascalmpl.ast.Char character) {
		this.node = node;
		this.character = character;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRangeCharacter(this);
	}

	@Override
	public boolean isCharacter() { return true; }

	@Override
	public boolean hasCharacter() { return true; }

private final org.rascalmpl.ast.Char character;
	@Override
	public org.rascalmpl.ast.Char getCharacter() { return character; }	
}
static public class Ambiguity extends Range {
  private final java.util.List<org.rascalmpl.ast.Range> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Range> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Range> getAlternatives() {
	return alternatives;
  }
  
  @Override
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
	public FromTo(INode node, org.rascalmpl.ast.Char start, org.rascalmpl.ast.Char end) {
		this.node = node;
		this.start = start;
		this.end = end;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRangeFromTo(this);
	}

	@Override
	public boolean isFromTo() { return true; }

	@Override
	public boolean hasStart() { return true; }
	@Override
	public boolean hasEnd() { return true; }

private final org.rascalmpl.ast.Char start;
	@Override
	public org.rascalmpl.ast.Char getStart() { return start; }
	private final org.rascalmpl.ast.Char end;
	@Override
	public org.rascalmpl.ast.Char getEnd() { return end; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}