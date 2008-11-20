package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class BasicType extends AbstractAST { 
  public boolean isBool() { return false; }
static public class Bool extends BasicType {
/* "bool" -> BasicType {cons("Bool")} */
	private Bool() { }
	/*package*/ Bool(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeBool(this);
	}

	public boolean isBool() { return true; }	
}
static public class Ambiguity extends BasicType {
  private final java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.BasicType> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBasicTypeAmbiguity(this);
  }
} 
public boolean isInt() { return false; }
static public class Int extends BasicType {
/* "int" -> BasicType {cons("Int")} */
	private Int() { }
	/*package*/ Int(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeInt(this);
	}

	public boolean isInt() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isDouble() { return false; }
static public class Double extends BasicType {
/* "double" -> BasicType {cons("Double")} */
	private Double() { }
	/*package*/ Double(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeDouble(this);
	}

	public boolean isDouble() { return true; }	
} 
public boolean isString() { return false; }
static public class String extends BasicType {
/* "str" -> BasicType {cons("String")} */
	private String() { }
	/*package*/ String(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeString(this);
	}

	public boolean isString() { return true; }	
} 
public boolean isValue() { return false; }
static public class Value extends BasicType {
/* "value" -> BasicType {cons("Value")} */
	private Value() { }
	/*package*/ Value(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeValue(this);
	}

	public boolean isValue() { return true; }	
} 
public boolean isTree() { return false; }
static public class Tree extends BasicType {
/* "tree" -> BasicType {cons("Tree")} */
	private Tree() { }
	/*package*/ Tree(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeTree(this);
	}

	public boolean isTree() { return true; }	
} 
public boolean isVoid() { return false; }
static public class Void extends BasicType {
/* "void" -> BasicType {cons("Void")} */
	private Void() { }
	/*package*/ Void(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeVoid(this);
	}

	public boolean isVoid() { return true; }	
} 
public boolean isLoc() { return false; }
static public class Loc extends BasicType {
/* "loc" -> BasicType {cons("Loc")} */
	private Loc() { }
	/*package*/ Loc(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeLoc(this);
	}

	public boolean isLoc() { return true; }	
}
}