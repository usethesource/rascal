package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Pattern extends AbstractAST { 
  public org.meta_environment.rascal.ast.X getSymbol() { throw new UnsupportedOperationException(); } public boolean hasSymbol() { return false; } public boolean isConcreteQuoted() { return false; }
static public class ConcreteQuoted extends Pattern {
/* "[|" symbol:X "|]" -> Pattern {cons("ConcreteQuoted")} */
	private ConcreteQuoted() {
		super();
	}
	/*package*/ ConcreteQuoted(INode node, org.meta_environment.rascal.ast.X symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPatternConcreteQuoted(this);
	}

	public boolean isConcreteQuoted() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.X symbol;
	public org.meta_environment.rascal.ast.X getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.X x) { this.symbol = x; }
	public ConcreteQuoted setSymbol(org.meta_environment.rascal.ast.X x) { 
		ConcreteQuoted z = new ConcreteQuoted();
 		z.$setSymbol(x);
		return z;
	}	
}
static public class Ambiguity extends Pattern {
  private final java.util.List<org.meta_environment.rascal.ast.Pattern> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Pattern> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Pattern> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPatternAmbiguity(this);
  }
} public boolean isConcreteUnquoted() { return false; }
static public class ConcreteUnquoted extends Pattern {
/* symbol:X -> Pattern {cons("ConcreteUnquoted")} */
	private ConcreteUnquoted() {
		super();
	}
	/*package*/ ConcreteUnquoted(INode node, org.meta_environment.rascal.ast.X symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPatternConcreteUnquoted(this);
	}

	public boolean isConcreteUnquoted() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.X symbol;
	public org.meta_environment.rascal.ast.X getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.X x) { this.symbol = x; }
	public ConcreteUnquoted setSymbol(org.meta_environment.rascal.ast.X x) { 
		ConcreteUnquoted z = new ConcreteUnquoted();
 		z.$setSymbol(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}