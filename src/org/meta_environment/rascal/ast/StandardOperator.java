package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class StandardOperator extends AbstractAST { 
public boolean isAddition() { return false; }
static public class Addition extends StandardOperator {
/* "+" -> StandardOperator {cons("Addition")} */
	private Addition() { }
	/*package*/ Addition(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorAddition(this);
	}

	public boolean isAddition() { return true; }	
}
static public class Ambiguity extends StandardOperator {
  private final java.util.List<org.meta_environment.rascal.ast.StandardOperator> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.StandardOperator> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.StandardOperator> getAlternatives() {
	return alternatives;
  }
} 
public boolean isSubstraction() { return false; }
static public class Substraction extends StandardOperator {
/* "-" -> StandardOperator {cons("Substraction")} */
	private Substraction() { }
	/*package*/ Substraction(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorSubstraction(this);
	}

	public boolean isSubstraction() { return true; }	
} 
public boolean isProduct() { return false; }
static public class Product extends StandardOperator {
/* "*" -> StandardOperator {cons("Product")} */
	private Product() { }
	/*package*/ Product(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorProduct(this);
	}

	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends StandardOperator {
/* "/" -> StandardOperator {cons("Division")} */
	private Division() { }
	/*package*/ Division(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorDivision(this);
	}

	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends StandardOperator {
/* "&" -> StandardOperator {cons("Intersection")} */
	private Intersection() { }
	/*package*/ Intersection(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorIntersection(this);
	}

	public boolean isIntersection() { return true; }	
} 
public boolean isEquals() { return false; }
static public class Equals extends StandardOperator {
/* "==" -> StandardOperator {cons("Equals")} */
	private Equals() { }
	/*package*/ Equals(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorEquals(this);
	}

	public boolean isEquals() { return true; }	
} 
public boolean isNotEquals() { return false; }
static public class NotEquals extends StandardOperator {
/* "!=" -> StandardOperator {cons("NotEquals")} */
	private NotEquals() { }
	/*package*/ NotEquals(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorNotEquals(this);
	}

	public boolean isNotEquals() { return true; }	
} 
public boolean isLessThan() { return false; }
static public class LessThan extends StandardOperator {
/* "<" -> StandardOperator {cons("LessThan")} */
	private LessThan() { }
	/*package*/ LessThan(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorLessThan(this);
	}

	public boolean isLessThan() { return true; }	
} 
public boolean isLessThanOrEq() { return false; }
static public class LessThanOrEq extends StandardOperator {
/* "<=" -> StandardOperator {cons("LessThanOrEq")} */
	private LessThanOrEq() { }
	/*package*/ LessThanOrEq(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorLessThanOrEq(this);
	}

	public boolean isLessThanOrEq() { return true; }	
} 
public boolean isGreaterThan() { return false; }
static public class GreaterThan extends StandardOperator {
/* ">" -> StandardOperator {cons("GreaterThan")} */
	private GreaterThan() { }
	/*package*/ GreaterThan(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorGreaterThan(this);
	}

	public boolean isGreaterThan() { return true; }	
} 
public boolean isGreaterThanOrEq() { return false; }
static public class GreaterThanOrEq extends StandardOperator {
/* ">=" -> StandardOperator {cons("GreaterThanOrEq")} */
	private GreaterThanOrEq() { }
	/*package*/ GreaterThanOrEq(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorGreaterThanOrEq(this);
	}

	public boolean isGreaterThanOrEq() { return true; }	
} 
public boolean isAnd() { return false; }
static public class And extends StandardOperator {
/* "&&" -> StandardOperator {cons("And")} */
	private And() { }
	/*package*/ And(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorAnd(this);
	}

	public boolean isAnd() { return true; }	
} 
public boolean isOr() { return false; }
static public class Or extends StandardOperator {
/* "||" -> StandardOperator {cons("Or")} */
	private Or() { }
	/*package*/ Or(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorOr(this);
	}

	public boolean isOr() { return true; }	
} 
public boolean isNot() { return false; }
static public class Not extends StandardOperator {
/* "!" -> StandardOperator {cons("Not")} */
	private Not() { }
	/*package*/ Not(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorNot(this);
	}

	public boolean isNot() { return true; }	
} 
public boolean isIn() { return false; }
static public class In extends StandardOperator {
/* "in" -> StandardOperator {cons("In")} */
	private In() { }
	/*package*/ In(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorIn(this);
	}

	public boolean isIn() { return true; }	
} 
public boolean isNotIn() { return false; }
static public class NotIn extends StandardOperator {
/* "notin" -> StandardOperator {cons("NotIn")} */
	private NotIn() { }
	/*package*/ NotIn(ITree tree) {
		this.tree = tree;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitStandardOperatorNotIn(this);
	}

	public boolean isNotIn() { return true; }	
}
}