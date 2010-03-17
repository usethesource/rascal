package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Character extends AbstractAST { 
  public org.rascalmpl.ast.NumChar getNumChar() { throw new UnsupportedOperationException(); }
public boolean hasNumChar() { return false; }
public boolean isNumeric() { return false; }
static public class Numeric extends Character {
/** numChar:NumChar -> Character {cons("Numeric")} */
	public Numeric(INode node, org.rascalmpl.ast.NumChar numChar) {
		this.node = node;
		this.numChar = numChar;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterNumeric(this);
	}

	@Override
	public boolean isNumeric() { return true; }

	@Override
	public boolean hasNumChar() { return true; }

private final org.rascalmpl.ast.NumChar numChar;
	@Override
	public org.rascalmpl.ast.NumChar getNumChar() { return numChar; }	
}
static public class Ambiguity extends Character {
  private final java.util.List<org.rascalmpl.ast.Character> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Character> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Character> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharacterAmbiguity(this);
  }
} 
public org.rascalmpl.ast.ShortChar getShortChar() { throw new UnsupportedOperationException(); }
public boolean hasShortChar() { return false; }
public boolean isShort() { return false; }
static public class Short extends Character {
/** shortChar:ShortChar -> Character {cons("Short")} */
	public Short(INode node, org.rascalmpl.ast.ShortChar shortChar) {
		this.node = node;
		this.shortChar = shortChar;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterShort(this);
	}

	@Override
	public boolean isShort() { return true; }

	@Override
	public boolean hasShortChar() { return true; }

private final org.rascalmpl.ast.ShortChar shortChar;
	@Override
	public org.rascalmpl.ast.ShortChar getShortChar() { return shortChar; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isTop() { return false; }
static public class Top extends Character {
/** "\\TOP" -> Character {cons("Top")} */
	public Top(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterTop(this);
	}

	@Override
	public boolean isTop() { return true; }	
} 
public boolean isEOF() { return false; }
static public class EOF extends Character {
/** "\\EOF" -> Character {cons("EOF")} */
	public EOF(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterEOF(this);
	}

	@Override
	public boolean isEOF() { return true; }	
} 
public boolean isBottom() { return false; }
static public class Bottom extends Character {
/** "\\BOT" -> Character {cons("Bottom")} */
	public Bottom(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterBottom(this);
	}

	@Override
	public boolean isBottom() { return true; }	
}
}