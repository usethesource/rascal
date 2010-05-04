package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Char extends AbstractAST { 
  public boolean isEscape() { return false; }
static public class Escape extends Char {
/** EscapeSequence -> Char {cons("Escape"), category("Constant")} */
	public Escape(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharEscape(this);
	}

	@Override
	public boolean isEscape() { return true; }	
}
static public class Ambiguity extends Char {
  private final java.util.List<org.rascalmpl.ast.Char> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Char> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Char> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharAmbiguity(this);
  }
} 
public boolean isUnicode() { return false; }
static public class Unicode extends Char {
/** UnicodeEscape -> Char {cons("Unicode"), category("Constant")} */
	public Unicode(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharUnicode(this);
	}

	@Override
	public boolean isUnicode() { return true; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isNormal() { return false; }
static public class Normal extends Char {
/** ~[\"\'\\\-\[\]\ ] -> Char {cons("Normal"), category("Constant")} */
	public Normal(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharNormal(this);
	}

	@Override
	public boolean isNormal() { return true; }	
} 
public boolean isExtraEscaped() { return false; }
static public class ExtraEscaped extends Char {
/** "\\" [\-\[\]\ ] -> Char {cons("ExtraEscaped"), category("Constant")} */
	public ExtraEscaped(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharExtraEscaped(this);
	}

	@Override
	public boolean isExtraEscaped() { return true; }	
}
}