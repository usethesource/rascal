package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Character extends AbstractAST { 
  public org.meta_environment.rascal.ast.NumChar getNumChar() { throw new UnsupportedOperationException(); }
public boolean hasNumChar() { return false; }
public boolean isNumeric() { return false; }
static public class Numeric extends Character {
/** numChar:NumChar -> Character {cons("Numeric")} */
	public Numeric(INode node, org.meta_environment.rascal.ast.NumChar numChar) {
		this.node = node;
		this.numChar = numChar;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterNumeric(this);
	}

	public boolean isNumeric() { return true; }

	public boolean hasNumChar() { return true; }

private final org.meta_environment.rascal.ast.NumChar numChar;
	public org.meta_environment.rascal.ast.NumChar getNumChar() { return numChar; }	
}
static public class Ambiguity extends Character {
  private final java.util.List<org.meta_environment.rascal.ast.Character> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Character> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Character> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharacterAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.ShortChar getShortChar() { throw new UnsupportedOperationException(); }
public boolean hasShortChar() { return false; }
public boolean isShort() { return false; }
static public class Short extends Character {
/** shortChar:ShortChar -> Character {cons("Short")} */
	public Short(INode node, org.meta_environment.rascal.ast.ShortChar shortChar) {
		this.node = node;
		this.shortChar = shortChar;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterShort(this);
	}

	public boolean isShort() { return true; }

	public boolean hasShortChar() { return true; }

private final org.meta_environment.rascal.ast.ShortChar shortChar;
	public org.meta_environment.rascal.ast.ShortChar getShortChar() { return shortChar; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isTop() { return false; }
static public class Top extends Character {
/** "\\TOP" -> Character {cons("Top")} */
	public Top(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterTop(this);
	}

	public boolean isTop() { return true; }	
} 
public boolean isEOF() { return false; }
static public class EOF extends Character {
/** "\\EOF" -> Character {cons("EOF")} */
	public EOF(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterEOF(this);
	}

	public boolean isEOF() { return true; }	
} 
public boolean isBottom() { return false; }
static public class Bottom extends Character {
/** "\\BOT" -> Character {cons("Bottom")} */
	public Bottom(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterBottom(this);
	}

	public boolean isBottom() { return true; }	
}
}