package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Literal extends AbstractAST { 
  public org.meta_environment.rascal.ast.RegExpLiteral getRegExpLiteral() { throw new UnsupportedOperationException(); }
public boolean hasRegExpLiteral() { return false; }
public boolean isRegExp() { return false; }
static public class RegExp extends Literal {
/** regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
	public RegExp(INode node, org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral) {
		this.node = node;
		this.regExpLiteral = regExpLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralRegExp(this);
	}

	public boolean isRegExp() { return true; }

	public boolean hasRegExpLiteral() { return true; }

private final org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral;
	public org.meta_environment.rascal.ast.RegExpLiteral getRegExpLiteral() { return regExpLiteral; }	
}
static public class Ambiguity extends Literal {
  private final java.util.List<org.meta_environment.rascal.ast.Literal> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Literal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Literal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLiteralAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.BooleanLiteral getBooleanLiteral() { throw new UnsupportedOperationException(); }
public boolean hasBooleanLiteral() { return false; }
public boolean isBoolean() { return false; }
static public class Boolean extends Literal {
/** booleanLiteral:BooleanLiteral -> Literal {cons("Boolean")} */
	public Boolean(INode node, org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral) {
		this.node = node;
		this.booleanLiteral = booleanLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralBoolean(this);
	}

	public boolean isBoolean() { return true; }

	public boolean hasBooleanLiteral() { return true; }

private final org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral;
	public org.meta_environment.rascal.ast.BooleanLiteral getBooleanLiteral() { return booleanLiteral; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.IntegerLiteral getIntegerLiteral() { throw new UnsupportedOperationException(); }
public boolean hasIntegerLiteral() { return false; }
public boolean isInteger() { return false; }
static public class Integer extends Literal {
/** integerLiteral:IntegerLiteral -> Literal {cons("Integer")} */
	public Integer(INode node, org.meta_environment.rascal.ast.IntegerLiteral integerLiteral) {
		this.node = node;
		this.integerLiteral = integerLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralInteger(this);
	}

	public boolean isInteger() { return true; }

	public boolean hasIntegerLiteral() { return true; }

private final org.meta_environment.rascal.ast.IntegerLiteral integerLiteral;
	public org.meta_environment.rascal.ast.IntegerLiteral getIntegerLiteral() { return integerLiteral; }	
} 
public org.meta_environment.rascal.ast.RealLiteral getRealLiteral() { throw new UnsupportedOperationException(); }
public boolean hasRealLiteral() { return false; }
public boolean isReal() { return false; }
static public class Real extends Literal {
/** realLiteral:RealLiteral -> Literal {cons("Real")} */
	public Real(INode node, org.meta_environment.rascal.ast.RealLiteral realLiteral) {
		this.node = node;
		this.realLiteral = realLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralReal(this);
	}

	public boolean isReal() { return true; }

	public boolean hasRealLiteral() { return true; }

private final org.meta_environment.rascal.ast.RealLiteral realLiteral;
	public org.meta_environment.rascal.ast.RealLiteral getRealLiteral() { return realLiteral; }	
} 
public org.meta_environment.rascal.ast.StringLiteral getStringLiteral() { throw new UnsupportedOperationException(); }
public boolean hasStringLiteral() { return false; }
public boolean isString() { return false; }
static public class String extends Literal {
/** stringLiteral:StringLiteral -> Literal {cons("String")} */
	public String(INode node, org.meta_environment.rascal.ast.StringLiteral stringLiteral) {
		this.node = node;
		this.stringLiteral = stringLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralString(this);
	}

	public boolean isString() { return true; }

	public boolean hasStringLiteral() { return true; }

private final org.meta_environment.rascal.ast.StringLiteral stringLiteral;
	public org.meta_environment.rascal.ast.StringLiteral getStringLiteral() { return stringLiteral; }	
} 
public org.meta_environment.rascal.ast.LocationLiteral getLocationLiteral() { throw new UnsupportedOperationException(); }
public boolean hasLocationLiteral() { return false; }
public boolean isLocation() { return false; }
static public class Location extends Literal {
/** locationLiteral:LocationLiteral -> Literal {cons("Location")} */
	public Location(INode node, org.meta_environment.rascal.ast.LocationLiteral locationLiteral) {
		this.node = node;
		this.locationLiteral = locationLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralLocation(this);
	}

	public boolean isLocation() { return true; }

	public boolean hasLocationLiteral() { return true; }

private final org.meta_environment.rascal.ast.LocationLiteral locationLiteral;
	public org.meta_environment.rascal.ast.LocationLiteral getLocationLiteral() { return locationLiteral; }	
}
}