package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class StrChar extends AbstractAST { 
public class newline extends StrChar {
/* "\\n" -> StrChar {cons("newline")} */
	private newline() { }
	/*package*/ newline(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitnewlineStrChar(this);
	}	
}
public class Ambiguity extends StrChar {
  private final List<StrChar> alternatives;
  public Ambiguity(List<StrChar> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<StrChar> getAlternatives() {
	return alternatives;
  }
} 
public class tab extends StrChar {
/* "\\t" -> StrChar {cons("tab")} */
	private tab() { }
	/*package*/ tab(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visittabStrChar(this);
	}	
} 
public class quote extends StrChar {
/* "\\\"" -> StrChar {cons("quote")} */
	private quote() { }
	/*package*/ quote(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitquoteStrChar(this);
	}	
} 
public class backslash extends StrChar {
/* "\\\\" -> StrChar {cons("backslash")} */
	private backslash() { }
	/*package*/ backslash(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitbackslashStrChar(this);
	}	
} 
public class decimal extends StrChar {
/* "\\" a:[0-9]b:[0-9]c:[0-9] -> StrChar {cons("decimal")} */
	private decimal() { }
	/*package*/ decimal(ITree tree, List<get-sort-from-symbol([0-9])> a, List<get-sort-from-symbol([0-9])> b, List<get-sort-from-symbol([0-9])> c) {
		this.tree = tree;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitdecimalStrChar(this);
	}
private List<get-sort-from-symbol([0-9])> a;
	public List<get-sort-from-symbol([0-9])> geta() { return a; }
	private void privateSeta(List<get-sort-from-symbol([0-9])> x) { this.a = x; }
	public decimal seta(List<get-sort-from-symbol([0-9])> x) { 
		decimal z = new decimal();
 		z.privateSeta(x);
		return z;
	}
	private List<get-sort-from-symbol([0-9])> b;
	public List<get-sort-from-symbol([0-9])> getb() { return b; }
	private void privateSetb(List<get-sort-from-symbol([0-9])> x) { this.b = x; }
	public decimal setb(List<get-sort-from-symbol([0-9])> x) { 
		decimal z = new decimal();
 		z.privateSetb(x);
		return z;
	}
	private List<get-sort-from-symbol([0-9])> c;
	public List<get-sort-from-symbol([0-9])> getc() { return c; }
	private void privateSetc(List<get-sort-from-symbol([0-9])> x) { this.c = x; }
	public decimal setc(List<get-sort-from-symbol([0-9])> x) { 
		decimal z = new decimal();
 		z.privateSetc(x);
		return z;
	}	
} 
public class normal extends StrChar {
/* ~[\0-\31\n\t\"\\] -> StrChar {cons("normal")} */
	private normal() { }
	/*package*/ normal(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitnormalStrChar(this);
	}	
}
}