package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LongLiteral extends AbstractAST { 
  public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() { throw new UnsupportedOperationException(); }
public boolean hasDecimalLong() { return false; }
public boolean isDecimalLongLiteral() { return false; }
static public class DecimalLongLiteral extends LongLiteral {
/** decimalLong:DecimalLongLiteral -> LongLiteral {prefer, cons("DecimalLongLiteral")} */
	public DecimalLongLiteral(INode node, org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) {
		this.node = node;
		this.decimalLong = decimalLong;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralDecimalLongLiteral(this);
	}

	public boolean isDecimalLongLiteral() { return true; }

	public boolean hasDecimalLong() { return true; }

private final org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong;
	public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() { return decimalLong; }	
}
static public class Ambiguity extends LongLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.LongLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLongLiteralAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() { throw new UnsupportedOperationException(); }
public boolean hasHexLong() { return false; }
public boolean isHexLongLiteral() { return false; }
static public class HexLongLiteral extends LongLiteral {
/** hexLong:HexLongLiteral -> LongLiteral {prefer, cons("HexLongLiteral")} */
	public HexLongLiteral(INode node, org.meta_environment.rascal.ast.HexLongLiteral hexLong) {
		this.node = node;
		this.hexLong = hexLong;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralHexLongLiteral(this);
	}

	public boolean isHexLongLiteral() { return true; }

	public boolean hasHexLong() { return true; }

private final org.meta_environment.rascal.ast.HexLongLiteral hexLong;
	public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() { return hexLong; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() { throw new UnsupportedOperationException(); }
public boolean hasOctalLong() { return false; }
public boolean isOctalLongLiteral() { return false; }
static public class OctalLongLiteral extends LongLiteral {
/** octalLong:OctalLongLiteral -> LongLiteral {prefer, cons("OctalLongLiteral")} */
	public OctalLongLiteral(INode node, org.meta_environment.rascal.ast.OctalLongLiteral octalLong) {
		this.node = node;
		this.octalLong = octalLong;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralOctalLongLiteral(this);
	}

	public boolean isOctalLongLiteral() { return true; }

	public boolean hasOctalLong() { return true; }

private final org.meta_environment.rascal.ast.OctalLongLiteral octalLong;
	public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() { return octalLong; }	
}
}