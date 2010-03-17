package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LongLiteral extends AbstractAST { 
  public org.rascalmpl.ast.DecimalLongLiteral getDecimalLong() { throw new UnsupportedOperationException(); }
public boolean hasDecimalLong() { return false; }
public boolean isDecimalLongLiteral() { return false; }
static public class DecimalLongLiteral extends LongLiteral {
/** decimalLong:DecimalLongLiteral -> LongLiteral {prefer, cons("DecimalLongLiteral")} */
	public DecimalLongLiteral(INode node, org.rascalmpl.ast.DecimalLongLiteral decimalLong) {
		this.node = node;
		this.decimalLong = decimalLong;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralDecimalLongLiteral(this);
	}

	@Override
	public boolean isDecimalLongLiteral() { return true; }

	@Override
	public boolean hasDecimalLong() { return true; }

private final org.rascalmpl.ast.DecimalLongLiteral decimalLong;
	@Override
	public org.rascalmpl.ast.DecimalLongLiteral getDecimalLong() { return decimalLong; }	
}
static public class Ambiguity extends LongLiteral {
  private final java.util.List<org.rascalmpl.ast.LongLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.LongLiteral> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitLongLiteralAmbiguity(this);
  }
} 
public org.rascalmpl.ast.HexLongLiteral getHexLong() { throw new UnsupportedOperationException(); }
public boolean hasHexLong() { return false; }
public boolean isHexLongLiteral() { return false; }
static public class HexLongLiteral extends LongLiteral {
/** hexLong:HexLongLiteral -> LongLiteral {prefer, cons("HexLongLiteral")} */
	public HexLongLiteral(INode node, org.rascalmpl.ast.HexLongLiteral hexLong) {
		this.node = node;
		this.hexLong = hexLong;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralHexLongLiteral(this);
	}

	@Override
	public boolean isHexLongLiteral() { return true; }

	@Override
	public boolean hasHexLong() { return true; }

private final org.rascalmpl.ast.HexLongLiteral hexLong;
	@Override
	public org.rascalmpl.ast.HexLongLiteral getHexLong() { return hexLong; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.OctalLongLiteral getOctalLong() { throw new UnsupportedOperationException(); }
public boolean hasOctalLong() { return false; }
public boolean isOctalLongLiteral() { return false; }
static public class OctalLongLiteral extends LongLiteral {
/** octalLong:OctalLongLiteral -> LongLiteral {prefer, cons("OctalLongLiteral")} */
	public OctalLongLiteral(INode node, org.rascalmpl.ast.OctalLongLiteral octalLong) {
		this.node = node;
		this.octalLong = octalLong;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLongLiteralOctalLongLiteral(this);
	}

	@Override
	public boolean isOctalLongLiteral() { return true; }

	@Override
	public boolean hasOctalLong() { return true; }

private final org.rascalmpl.ast.OctalLongLiteral octalLong;
	@Override
	public org.rascalmpl.ast.OctalLongLiteral getOctalLong() { return octalLong; }	
}
}