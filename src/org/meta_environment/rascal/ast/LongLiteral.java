package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class LongLiteral extends AbstractAST { 
public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() { throw new UnsupportedOperationException(); }
public boolean hasDecimalLong() { return false; }
public boolean isDecimalLongLiteral() { return false; }
static public class DecimalLongLiteral extends LongLiteral {
/* decimalLong:DecimalLongLiteral -> LongLiteral {prefer, cons("DecimalLongLiteral")} */
	private DecimalLongLiteral() { }
	/*package*/ DecimalLongLiteral(ITree tree, org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) {
		this.tree = tree;
		this.decimalLong = decimalLong;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitLongLiteralDecimalLongLiteral(this);
	}

	public boolean isDecimalLongLiteral() { return true; }

	public boolean hasDecimalLong() { return true; }

private org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong;
	public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() { return decimalLong; }
	private void $setDecimalLong(org.meta_environment.rascal.ast.DecimalLongLiteral x) { this.decimalLong = x; }
	public DecimalLongLiteral setDecimalLong(org.meta_environment.rascal.ast.DecimalLongLiteral x) { 
		DecimalLongLiteral z = new DecimalLongLiteral();
 		z.$setDecimalLong(x);
		return z;
	}	
}
static public class Ambiguity extends LongLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.LongLiteral> getAlternatives() {
	return alternatives;
  }
} 
public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() { throw new UnsupportedOperationException(); }
public boolean hasHexLong() { return false; }
public boolean isHexLongLiteral() { return false; }
static public class HexLongLiteral extends LongLiteral {
/* hexLong:HexLongLiteral -> LongLiteral {prefer, cons("HexLongLiteral")} */
	private HexLongLiteral() { }
	/*package*/ HexLongLiteral(ITree tree, org.meta_environment.rascal.ast.HexLongLiteral hexLong) {
		this.tree = tree;
		this.hexLong = hexLong;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitLongLiteralHexLongLiteral(this);
	}

	public boolean isHexLongLiteral() { return true; }

	public boolean hasHexLong() { return true; }

private org.meta_environment.rascal.ast.HexLongLiteral hexLong;
	public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() { return hexLong; }
	private void $setHexLong(org.meta_environment.rascal.ast.HexLongLiteral x) { this.hexLong = x; }
	public HexLongLiteral setHexLong(org.meta_environment.rascal.ast.HexLongLiteral x) { 
		HexLongLiteral z = new HexLongLiteral();
 		z.$setHexLong(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() { throw new UnsupportedOperationException(); }
public boolean hasOctalLong() { return false; }
public boolean isOctalLongLiteral() { return false; }
static public class OctalLongLiteral extends LongLiteral {
/* octalLong:OctalLongLiteral -> LongLiteral {prefer, cons("OctalLongLiteral")} */
	private OctalLongLiteral() { }
	/*package*/ OctalLongLiteral(ITree tree, org.meta_environment.rascal.ast.OctalLongLiteral octalLong) {
		this.tree = tree;
		this.octalLong = octalLong;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitLongLiteralOctalLongLiteral(this);
	}

	public boolean isOctalLongLiteral() { return true; }

	public boolean hasOctalLong() { return true; }

private org.meta_environment.rascal.ast.OctalLongLiteral octalLong;
	public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() { return octalLong; }
	private void $setOctalLong(org.meta_environment.rascal.ast.OctalLongLiteral x) { this.octalLong = x; }
	public OctalLongLiteral setOctalLong(org.meta_environment.rascal.ast.OctalLongLiteral x) { 
		OctalLongLiteral z = new OctalLongLiteral();
 		z.$setOctalLong(x);
		return z;
	}	
}
}