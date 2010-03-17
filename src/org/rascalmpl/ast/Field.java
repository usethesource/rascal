package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Field extends AbstractAST { 
  public org.rascalmpl.ast.Name getFieldName() { throw new UnsupportedOperationException(); }
public boolean hasFieldName() { return false; }
public boolean isName() { return false; }
static public class Name extends Field {
/** fieldName:Name -> Field {cons("Name")} */
	public Name(INode node, org.rascalmpl.ast.Name fieldName) {
		this.node = node;
		this.fieldName = fieldName;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFieldName(this);
	}

	@Override
	public boolean isName() { return true; }

	@Override
	public boolean hasFieldName() { return true; }

private final org.rascalmpl.ast.Name fieldName;
	@Override
	public org.rascalmpl.ast.Name getFieldName() { return fieldName; }	
}
static public class Ambiguity extends Field {
  private final java.util.List<org.rascalmpl.ast.Field> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Field> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Field> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitFieldAmbiguity(this);
  }
} 
public org.rascalmpl.ast.IntegerLiteral getFieldIndex() { throw new UnsupportedOperationException(); }
public boolean hasFieldIndex() { return false; }
public boolean isIndex() { return false; }
static public class Index extends Field {
/** fieldIndex:IntegerLiteral -> Field {cons("Index")} */
	public Index(INode node, org.rascalmpl.ast.IntegerLiteral fieldIndex) {
		this.node = node;
		this.fieldIndex = fieldIndex;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFieldIndex(this);
	}

	@Override
	public boolean isIndex() { return true; }

	@Override
	public boolean hasFieldIndex() { return true; }

private final org.rascalmpl.ast.IntegerLiteral fieldIndex;
	@Override
	public org.rascalmpl.ast.IntegerLiteral getFieldIndex() { return fieldIndex; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}