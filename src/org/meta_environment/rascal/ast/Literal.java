package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Literal extends AbstractAST { 
public class RegExp extends Literal {
/* regExp:RegExpLiteral -> Literal {cons("RegExp")} */
	private RegExp() { }
	/*package*/ RegExp(ITree tree, RegExpLiteral regExp) {
		this.tree = tree;
		this.regExp = regExp;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitRegExpLiteral(this);
	}
private RegExpLiteral regExp;
	public RegExpLiteral getregExp() { return regExp; }
	private void privateSetregExp(RegExpLiteral x) { this.regExp = x; }
	public RegExp setregExp(RegExpLiteral x) { 
		RegExp z = new RegExp();
 		z.privateSetregExp(x);
		return z;
	}	
}
public class Ambiguity extends Literal {
  private final List<Literal> alternatives;
  public Ambiguity(List<Literal> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<Literal> getAlternatives() {
	return alternatives;
  }
} 
public class Symbol extends Literal {
/* symbol:SymbolLiteral -> Literal {cons("Symbol")} */
	private Symbol() { }
	/*package*/ Symbol(ITree tree, SymbolLiteral symbol) {
		this.tree = tree;
		this.symbol = symbol;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitSymbolLiteral(this);
	}
private SymbolLiteral symbol;
	public SymbolLiteral getsymbol() { return symbol; }
	private void privateSetsymbol(SymbolLiteral x) { this.symbol = x; }
	public Symbol setsymbol(SymbolLiteral x) { 
		Symbol z = new Symbol();
 		z.privateSetsymbol(x);
		return z;
	}	
} 
public class Boolean extends Literal {
/* boolean:BooleanLiteral -> Literal {cons("Boolean")} */
	private Boolean() { }
	/*package*/ Boolean(ITree tree, BooleanLiteral boolean) {
		this.tree = tree;
		this.boolean = boolean;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitBooleanLiteral(this);
	}
private BooleanLiteral boolean;
	public BooleanLiteral getboolean() { return boolean; }
	private void privateSetboolean(BooleanLiteral x) { this.boolean = x; }
	public Boolean setboolean(BooleanLiteral x) { 
		Boolean z = new Boolean();
 		z.privateSetboolean(x);
		return z;
	}	
} 
public class Integer extends Literal {
/* integer:IntegerLiteral -> Literal {cons("Integer")} */
	private Integer() { }
	/*package*/ Integer(ITree tree, IntegerLiteral integer) {
		this.tree = tree;
		this.integer = integer;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitIntegerLiteral(this);
	}
private IntegerLiteral integer;
	public IntegerLiteral getinteger() { return integer; }
	private void privateSetinteger(IntegerLiteral x) { this.integer = x; }
	public Integer setinteger(IntegerLiteral x) { 
		Integer z = new Integer();
 		z.privateSetinteger(x);
		return z;
	}	
} 
public class Double extends Literal {
/* double:FloatingPointLiteral -> Literal {cons("Double")} */
	private Double() { }
	/*package*/ Double(ITree tree, FloatingPointLiteral double) {
		this.tree = tree;
		this.double = double;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDoubleLiteral(this);
	}
private FloatingPointLiteral double;
	public FloatingPointLiteral getdouble() { return double; }
	private void privateSetdouble(FloatingPointLiteral x) { this.double = x; }
	public Double setdouble(FloatingPointLiteral x) { 
		Double z = new Double();
 		z.privateSetdouble(x);
		return z;
	}	
} 
public class String extends Literal {
/* string:StringLiteral -> Literal {cons("String")} */
	private String() { }
	/*package*/ String(ITree tree, StringLiteral string) {
		this.tree = tree;
		this.string = string;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStringLiteral(this);
	}
private StringLiteral string;
	public StringLiteral getstring() { return string; }
	private void privateSetstring(StringLiteral x) { this.string = x; }
	public String setstring(StringLiteral x) { 
		String z = new String();
 		z.privateSetstring(x);
		return z;
	}	
}
}