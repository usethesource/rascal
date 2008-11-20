package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Character extends AbstractAST { 
  public org.meta_environment.rascal.ast.NumChar getNumChar() { throw new UnsupportedOperationException(); }
public boolean hasNumChar() { return false; }
public boolean isNumeric() { return false; }
static public class Numeric extends Character {
/* numChar:NumChar -> Character {cons("Numeric")} */
	private Numeric() { }
	/*package*/ Numeric(ITree tree, org.meta_environment.rascal.ast.NumChar numChar) {
		this.tree = tree;
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

private org.meta_environment.rascal.ast.NumChar numChar;
	@Override
	public org.meta_environment.rascal.ast.NumChar getNumChar() { return numChar; }
	private void $setNumChar(org.meta_environment.rascal.ast.NumChar x) { this.numChar = x; }
	public Numeric setNumChar(org.meta_environment.rascal.ast.NumChar x) { 
		Numeric z = new Numeric();
 		z.$setNumChar(x);
		return z;
	}	
}
static public class Ambiguity extends Character {
  private final java.util.List<org.meta_environment.rascal.ast.Character> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Character> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Character> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharacterAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.ShortChar getShortChar() { throw new UnsupportedOperationException(); }
public boolean hasShortChar() { return false; }
public boolean isShort() { return false; }
static public class Short extends Character {
/* shortChar:ShortChar -> Character {cons("Short")} */
	private Short() { }
	/*package*/ Short(ITree tree, org.meta_environment.rascal.ast.ShortChar shortChar) {
		this.tree = tree;
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

private org.meta_environment.rascal.ast.ShortChar shortChar;
	@Override
	public org.meta_environment.rascal.ast.ShortChar getShortChar() { return shortChar; }
	private void $setShortChar(org.meta_environment.rascal.ast.ShortChar x) { this.shortChar = x; }
	public Short setShortChar(org.meta_environment.rascal.ast.ShortChar x) { 
		Short z = new Short();
 		z.$setShortChar(x);
		return z;
	}	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isTop() { return false; }
static public class Top extends Character {
/* "\\TOP" -> Character {cons("Top")} */
	private Top() { }
	/*package*/ Top(ITree tree) {
		this.tree = tree;
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
/* "\\EOF" -> Character {cons("EOF")} */
	private EOF() { }
	/*package*/ EOF(ITree tree) {
		this.tree = tree;
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
/* "\\BOT" -> Character {cons("Bottom")} */
	private Bottom() { }
	/*package*/ Bottom(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterBottom(this);
	}

	@Override
	public boolean isBottom() { return true; }	
} 
public boolean isLabelStart() { return false; }
static public class LabelStart extends Character {
/* "\\LABEL_START" -> Character {cons("LabelStart")} */
	private LabelStart() { }
	/*package*/ LabelStart(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharacterLabelStart(this);
	}

	@Override
	public boolean isLabelStart() { return true; }	
}
}