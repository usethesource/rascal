package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Literal extends AbstractAST { 
  public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() { throw new UnsupportedOperationException(); }
public boolean hasRegExpLiteral() { return false; }
public boolean isRegExp() { return false; }
static public class RegExp extends Literal {
/** regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
	protected RegExp(INode node, org.rascalmpl.ast.RegExpLiteral regExpLiteral) {
		this.node = node;
		this.regExpLiteral = regExpLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralRegExp(this);
	}

	public boolean isRegExp() { return true; }

	public boolean hasRegExpLiteral() { return true; }

private final org.rascalmpl.ast.RegExpLiteral regExpLiteral;
	public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() { return regExpLiteral; }	
}
static public class Ambiguity extends Literal {
  private final java.util.List<org.rascalmpl.ast.Literal> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Literal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Literal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLiteralAmbiguity(this);
  }
} 
public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() { throw new UnsupportedOperationException(); }
public boolean hasBooleanLiteral() { return false; }
public boolean isBoolean() { return false; }
static public class Boolean extends Literal {
/** booleanLiteral:BooleanLiteral -> Literal {cons("Boolean")} */
	protected Boolean(INode node, org.rascalmpl.ast.BooleanLiteral booleanLiteral) {
		this.node = node;
		this.booleanLiteral = booleanLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralBoolean(this);
	}

	public boolean isBoolean() { return true; }

	public boolean hasBooleanLiteral() { return true; }

private final org.rascalmpl.ast.BooleanLiteral booleanLiteral;
	public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() { return booleanLiteral; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() { throw new UnsupportedOperationException(); }
public boolean hasIntegerLiteral() { return false; }
public boolean isInteger() { return false; }
static public class Integer extends Literal {
/** integerLiteral:IntegerLiteral -> Literal {cons("Integer")} */
	protected Integer(INode node, org.rascalmpl.ast.IntegerLiteral integerLiteral) {
		this.node = node;
		this.integerLiteral = integerLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralInteger(this);
	}

	public boolean isInteger() { return true; }

	public boolean hasIntegerLiteral() { return true; }

private final org.rascalmpl.ast.IntegerLiteral integerLiteral;
	public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() { return integerLiteral; }	
} 
public org.rascalmpl.ast.RealLiteral getRealLiteral() { throw new UnsupportedOperationException(); }
public boolean hasRealLiteral() { return false; }
public boolean isReal() { return false; }
static public class Real extends Literal {
/** realLiteral:RealLiteral -> Literal {cons("Real")} */
	protected Real(INode node, org.rascalmpl.ast.RealLiteral realLiteral) {
		this.node = node;
		this.realLiteral = realLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralReal(this);
	}

	public boolean isReal() { return true; }

	public boolean hasRealLiteral() { return true; }

private final org.rascalmpl.ast.RealLiteral realLiteral;
	public org.rascalmpl.ast.RealLiteral getRealLiteral() { return realLiteral; }	
} 
public org.rascalmpl.ast.StringLiteral getStringLiteral() { throw new UnsupportedOperationException(); }
public boolean hasStringLiteral() { return false; }
public boolean isString() { return false; }
static public class String extends Literal {
/** stringLiteral:StringLiteral -> Literal {cons("String")} */
	protected String(INode node, org.rascalmpl.ast.StringLiteral stringLiteral) {
		this.node = node;
		this.stringLiteral = stringLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralString(this);
	}

	public boolean isString() { return true; }

	public boolean hasStringLiteral() { return true; }

private final org.rascalmpl.ast.StringLiteral stringLiteral;
	public org.rascalmpl.ast.StringLiteral getStringLiteral() { return stringLiteral; }	
} 
public org.rascalmpl.ast.LocationLiteral getLocationLiteral() { throw new UnsupportedOperationException(); }
public boolean hasLocationLiteral() { return false; }
public boolean isLocation() { return false; }
static public class Location extends Literal {
/** locationLiteral:LocationLiteral -> Literal {cons("Location")} */
	protected Location(INode node, org.rascalmpl.ast.LocationLiteral locationLiteral) {
		this.node = node;
		this.locationLiteral = locationLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralLocation(this);
	}

	public boolean isLocation() { return true; }

	public boolean hasLocationLiteral() { return true; }

private final org.rascalmpl.ast.LocationLiteral locationLiteral;
	public org.rascalmpl.ast.LocationLiteral getLocationLiteral() { return locationLiteral; }	
} 
public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() { throw new UnsupportedOperationException(); }
public boolean hasDateTimeLiteral() { return false; }
public boolean isDateTime() { return false; }
static public class DateTime extends Literal {
/** dateTimeLiteral:DateTimeLiteral -> Literal {cons("DateTime")} */
	protected DateTime(INode node, org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral) {
		this.node = node;
		this.dateTimeLiteral = dateTimeLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLiteralDateTime(this);
	}

	public boolean isDateTime() { return true; }

	public boolean hasDateTimeLiteral() { return true; }

private final org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral;
	public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() { return dateTimeLiteral; }	
}
}