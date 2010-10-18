package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Field extends AbstractAST { 
  public org.rascalmpl.ast.Name getFieldName() { throw new UnsupportedOperationException(); }
public boolean hasFieldName() { return false; }
public boolean isName() { return false; }
static public class Name extends Field {
/** fieldName:Name -> Field {cons("Name")} */
	protected Name(INode node, org.rascalmpl.ast.Name fieldName) {
		this.node = node;
		this.fieldName = fieldName;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFieldName(this);
	}

	public boolean isName() { return true; }

	public boolean hasFieldName() { return true; }

private final org.rascalmpl.ast.Name fieldName;
	public org.rascalmpl.ast.Name getFieldName() { return fieldName; }	
}
static public class Ambiguity extends Field {
  private final java.util.List<org.rascalmpl.ast.Field> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Field> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Field> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFieldAmbiguity(this);
  }
} 
public org.rascalmpl.ast.IntegerLiteral getFieldIndex() { throw new UnsupportedOperationException(); }
public boolean hasFieldIndex() { return false; }
public boolean isIndex() { return false; }
static public class Index extends Field {
/** fieldIndex:IntegerLiteral -> Field {cons("Index")} */
	protected Index(INode node, org.rascalmpl.ast.IntegerLiteral fieldIndex) {
		this.node = node;
		this.fieldIndex = fieldIndex;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFieldIndex(this);
	}

	public boolean isIndex() { return true; }

	public boolean hasFieldIndex() { return true; }

private final org.rascalmpl.ast.IntegerLiteral fieldIndex;
	public org.rascalmpl.ast.IntegerLiteral getFieldIndex() { return fieldIndex; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}