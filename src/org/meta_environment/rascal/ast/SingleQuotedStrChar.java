package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class SingleQuotedStrChar extends AbstractAST { 
public class newline extends SingleQuotedStrChar {
/* "\\n" -> SingleQuotedStrChar {cons("newline")} */
	private newline() { }
	/*package*/ newline(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitnewlineSingleQuotedStrChar(this);
	}	
}
public class Ambiguity extends SingleQuotedStrChar {
  private final List<SingleQuotedStrChar> alternatives;
  public Ambiguity(List<SingleQuotedStrChar> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<SingleQuotedStrChar> getAlternatives() {
	return alternatives;
  }
} 
public class tab extends SingleQuotedStrChar {
/* "\\t" -> SingleQuotedStrChar {cons("tab")} */
	private tab() { }
	/*package*/ tab(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visittabSingleQuotedStrChar(this);
	}	
} 
public class quote extends SingleQuotedStrChar {
/* "\\'" -> SingleQuotedStrChar {cons("quote")} */
	private quote() { }
	/*package*/ quote(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitquoteSingleQuotedStrChar(this);
	}	
} 
public class backslash extends SingleQuotedStrChar {
/* "\\\\" -> SingleQuotedStrChar {cons("backslash")} */
	private backslash() { }
	/*package*/ backslash(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitbackslashSingleQuotedStrChar(this);
	}	
} 
public class decimal extends SingleQuotedStrChar {
/* "\\" a:[0-9]b:[0-9]c:[0-9] -> SingleQuotedStrChar {cons("decimal")} */
	private decimal() { }
	/*package*/ decimal(ITree tree, List<get-sort-from-symbol([0-9])> a, List<get-sort-from-symbol([0-9])> b, List<get-sort-from-symbol([0-9])> c) {
		this.tree = tree;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitdecimalSingleQuotedStrChar(this);
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
public class normal extends SingleQuotedStrChar {
/* ~[\0-\31\n\t\'\\] -> SingleQuotedStrChar {cons("normal")} */
	private normal() { }
	/*package*/ normal(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitnormalSingleQuotedStrChar(this);
	}	
}
}