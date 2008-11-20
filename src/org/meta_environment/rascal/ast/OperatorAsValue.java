package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class OperatorAsValue extends AbstractAST { 
  public boolean isAddition() { return false; }
static public class Addition extends OperatorAsValue {
/* "#+" -> OperatorAsValue {cons("Addition")} */
	private Addition() { }
	/*package*/ Addition(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueAddition(this);
	}

	@Override
	public boolean isAddition() { return true; }	
}
static public class Ambiguity extends OperatorAsValue {
  private final java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitOperatorAsValueAmbiguity(this);
  }
} 
public boolean isSubtraction() { return false; }
static public class Subtraction extends OperatorAsValue {
/* "#-" -> OperatorAsValue {cons("Subtraction")} */
	private Subtraction() { }
	/*package*/ Subtraction(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueSubtraction(this);
	}

	@Override
	public boolean isSubtraction() { return true; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isProduct() { return false; }
static public class Product extends OperatorAsValue {
/* "#*" -> OperatorAsValue {cons("Product")} */
	private Product() { }
	/*package*/ Product(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueProduct(this);
	}

	@Override
	public boolean isProduct() { return true; }	
} 
public boolean isDivision() { return false; }
static public class Division extends OperatorAsValue {
/* "#/" -> OperatorAsValue {cons("Division")} */
	private Division() { }
	/*package*/ Division(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueDivision(this);
	}

	@Override
	public boolean isDivision() { return true; }	
} 
public boolean isIntersection() { return false; }
static public class Intersection extends OperatorAsValue {
/* "#&" -> OperatorAsValue {cons("Intersection")} */
	private Intersection() { }
	/*package*/ Intersection(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueIntersection(this);
	}

	@Override
	public boolean isIntersection() { return true; }	
} 
public boolean isEquals() { return false; }
static public class Equals extends OperatorAsValue {
/* "#==" -> OperatorAsValue {cons("Equals")} */
	private Equals() { }
	/*package*/ Equals(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueEquals(this);
	}

	@Override
	public boolean isEquals() { return true; }	
} 
public boolean isNotEquals() { return false; }
static public class NotEquals extends OperatorAsValue {
/* "#!=" -> OperatorAsValue {cons("NotEquals")} */
	private NotEquals() { }
	/*package*/ NotEquals(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNotEquals(this);
	}

	@Override
	public boolean isNotEquals() { return true; }	
} 
public boolean isLessThan() { return false; }
static public class LessThan extends OperatorAsValue {
/* "#<" -> OperatorAsValue {cons("LessThan")} */
	private LessThan() { }
	/*package*/ LessThan(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueLessThan(this);
	}

	@Override
	public boolean isLessThan() { return true; }	
} 
public boolean isLessThanOrEq() { return false; }
static public class LessThanOrEq extends OperatorAsValue {
/* "#<=" -> OperatorAsValue {cons("LessThanOrEq")} */
	private LessThanOrEq() { }
	/*package*/ LessThanOrEq(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueLessThanOrEq(this);
	}

	@Override
	public boolean isLessThanOrEq() { return true; }	
} 
public boolean isGreaterThan() { return false; }
static public class GreaterThan extends OperatorAsValue {
/* "#>" -> OperatorAsValue {cons("GreaterThan")} */
	private GreaterThan() { }
	/*package*/ GreaterThan(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueGreaterThan(this);
	}

	@Override
	public boolean isGreaterThan() { return true; }	
} 
public boolean isGreaterThanOrEq() { return false; }
static public class GreaterThanOrEq extends OperatorAsValue {
/* "#>=" -> OperatorAsValue {cons("GreaterThanOrEq")} */
	private GreaterThanOrEq() { }
	/*package*/ GreaterThanOrEq(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueGreaterThanOrEq(this);
	}

	@Override
	public boolean isGreaterThanOrEq() { return true; }	
} 
public boolean isAnd() { return false; }
static public class And extends OperatorAsValue {
/* "#&&" -> OperatorAsValue {cons("And")} */
	private And() { }
	/*package*/ And(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueAnd(this);
	}

	@Override
	public boolean isAnd() { return true; }	
} 
public boolean isOr() { return false; }
static public class Or extends OperatorAsValue {
/* "#||" -> OperatorAsValue {cons("Or")} */
	private Or() { }
	/*package*/ Or(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueOr(this);
	}

	@Override
	public boolean isOr() { return true; }	
} 
public boolean isNot() { return false; }
static public class Not extends OperatorAsValue {
/* "#!" -> OperatorAsValue {cons("Not")} */
	private Not() { }
	/*package*/ Not(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNot(this);
	}

	@Override
	public boolean isNot() { return true; }	
} 
public boolean isIn() { return false; }
static public class In extends OperatorAsValue {
/* "#in" -> OperatorAsValue {cons("In")} */
	private In() { }
	/*package*/ In(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueIn(this);
	}

	@Override
	public boolean isIn() { return true; }	
} 
public boolean isNotIn() { return false; }
static public class NotIn extends OperatorAsValue {
/* "#notin" -> OperatorAsValue {cons("NotIn")} */
	private NotIn() { }
	/*package*/ NotIn(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitOperatorAsValueNotIn(this);
	}

	@Override
	public boolean isNotIn() { return true; }	
}
}