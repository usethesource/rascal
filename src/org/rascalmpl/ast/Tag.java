package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Tag extends AbstractAST { 
  public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.TagString getContents() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasContents() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Tag {
/** "@" name:Name contents:TagString -> Tag {cons("Default"), category("Comment")} */
	public Default(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.TagString contents) {
		this.node = node;
		this.name = name;
		this.contents = contents;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasContents() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.TagString contents;
	@Override
	public org.rascalmpl.ast.TagString getContents() { return contents; }	
}
static public class Ambiguity extends Tag {
  private final java.util.List<org.rascalmpl.ast.Tag> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Tag> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Tag> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagAmbiguity(this);
  }
} public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; }
public boolean isExpression() { return false; }
static public class Expression extends Tag {
/** "@" name:Name "=" expression:Expression -> Tag {cons("Expression"), category("Comment")} */
	public Expression(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.name = name;
		this.expression = expression;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagExpression(this);
	}

	@Override
	public boolean isExpression() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isEmpty() { return false; }
static public class Empty extends Tag {
/** "@" name:Name -> Tag {cons("Empty"), category("Comment")} */
	public Empty(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagEmpty(this);
	}

	@Override
	public boolean isEmpty() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }	
}
}