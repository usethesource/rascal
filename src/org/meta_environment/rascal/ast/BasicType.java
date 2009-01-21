package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class BasicType extends AbstractAST { 
  public boolean isBool() { return false; }
static public class Bool extends BasicType {
/* "bool" -> BasicType {cons("Bool")} */
	private Bool() { }
	/*package*/ Bool(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeBool(this);
	}

	public boolean isBool() { return true; }	
}
static public class Ambiguity extends BasicType {
  private final java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
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
	/*package*/ Int(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeInt(this);
	}

	public boolean isInt() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isReal() { return false; }
static public class Real extends BasicType {
/* "real" -> BasicType {cons("Real")} */
	private Real() { }
	/*package*/ Real(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeReal(this);
	}

	public boolean isReal() { return true; }	
} 
public boolean isString() { return false; }
static public class String extends BasicType {
/* "str" -> BasicType {cons("String")} */
	private String() { }
	/*package*/ String(INode node) {
		this.node = node;
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
	/*package*/ Value(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeValue(this);
	}

	public boolean isValue() { return true; }	
} 
public boolean isTree() { return false; }
static public class Tree extends BasicType {
/* "node" -> BasicType {cons("Node")} */
	private Tree() { }
	/*package*/ Tree(INode node) {
		this.node = node;
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
	/*package*/ Void(INode node) {
		this.node = node;
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
	/*package*/ Loc(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeLoc(this);
	}

	public boolean isLoc() { return true; }	
} 
public boolean isArea() { return false; }
static public class Area extends BasicType {
/* "area" -> BasicType {cons("Area")} */
	private Area() { }
	/*package*/ Area(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBasicTypeArea(this);
	}

	public boolean isArea() { return true; }	
}
}