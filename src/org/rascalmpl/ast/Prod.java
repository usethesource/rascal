package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Prod extends AbstractAST { 
  public boolean isOthers() { return false; }
static public class Others extends Prod {
/** "..." -> Prod {cons("Others")} */
	protected Others(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdOthers(this);
	}

	public boolean isOthers() { return true; }	
}
static public class Ambiguity extends Prod {
  private final java.util.List<org.rascalmpl.ast.Prod> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Prod> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Prod> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProdAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Name getReferenced() { throw new UnsupportedOperationException(); }
public boolean hasReferenced() { return false; }
public boolean isReference() { return false; }
static public class Reference extends Prod {
/** ":" referenced:Name -> Prod {cons("Reference")} */
	protected Reference(INode node, org.rascalmpl.ast.Name referenced) {
		this.node = node;
		this.referenced = referenced;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdReference(this);
	}

	public boolean isReference() { return true; }

	public boolean hasReferenced() { return true; }

private final org.rascalmpl.ast.Name referenced;
	public org.rascalmpl.ast.Name getReferenced() { return referenced; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Sym> getArgs() { throw new UnsupportedOperationException(); } public boolean hasModifiers() { return false; } public boolean hasName() { return false; } public boolean hasArgs() { return false; } public boolean isLabeled() { return false; }
static public class Labeled extends Prod {
/** modifiers:ProdModifier* name:Name ":" args:Sym* -> Prod {cons("Labeled")} */
	protected Labeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Sym> args) {
		this.node = node;
		this.modifiers = modifiers;
		this.name = name;
		this.args = args;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdLabeled(this);
	}

	public boolean isLabeled() { return true; }

	public boolean hasModifiers() { return true; }
	public boolean hasName() { return true; }
	public boolean hasArgs() { return true; }

private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
	public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Sym> args;
	public java.util.List<org.rascalmpl.ast.Sym> getArgs() { return args; }	
} public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Prod {
/** modifiers:ProdModifier* args:Sym* -> Prod {cons("Unlabeled")} */
	protected Unlabeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, java.util.List<org.rascalmpl.ast.Sym> args) {
		this.node = node;
		this.modifiers = modifiers;
		this.args = args;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdUnlabeled(this);
	}

	public boolean isUnlabeled() { return true; }

	public boolean hasModifiers() { return true; }
	public boolean hasArgs() { return true; }

private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
	public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { return modifiers; }
	private final java.util.List<org.rascalmpl.ast.Sym> args;
	public java.util.List<org.rascalmpl.ast.Sym> getArgs() { return args; }	
} 
public org.rascalmpl.ast.Prod getProd() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.LanguageAction getAction() { throw new UnsupportedOperationException(); }
public boolean hasProd() { return false; }
	public boolean hasAction() { return false; }
public boolean isAction() { return false; }
static public class Action extends Prod {
/** prod:Prod action:LanguageAction -> Prod {cons("Action"), non-assoc} */
	protected Action(INode node, org.rascalmpl.ast.Prod prod, org.rascalmpl.ast.LanguageAction action) {
		this.node = node;
		this.prod = prod;
		this.action = action;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAction(this);
	}

	public boolean isAction() { return true; }

	public boolean hasProd() { return true; }
	public boolean hasAction() { return true; }

private final org.rascalmpl.ast.Prod prod;
	public org.rascalmpl.ast.Prod getProd() { return prod; }
	private final org.rascalmpl.ast.LanguageAction action;
	public org.rascalmpl.ast.LanguageAction getAction() { return action; }	
} public org.rascalmpl.ast.Prod getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Prod getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isReject() { return false; }
static public class Reject extends Prod {
/** lhs:Prod "-" rhs:Prod -> Prod {cons("Reject"), left} */
	protected Reject(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdReject(this);
	}

	public boolean isReject() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} public boolean isFollow() { return false; }
static public class Follow extends Prod {
/** lhs:Prod "#" rhs:Prod -> Prod {cons("Follow"), left} */
	protected Follow(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdFollow(this);
	}

	public boolean isFollow() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} public boolean isAll() { return false; }
static public class All extends Prod {
/** lhs:Prod "|" rhs:Prod -> Prod {cons("All"), left} */
	protected All(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAll(this);
	}

	public boolean isAll() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} public boolean isFirst() { return false; }
static public class First extends Prod {
/** lhs:Prod ">" rhs:Prod -> Prod {cons("First"), left} */
	protected First(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdFirst(this);
	}

	public boolean isFirst() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} 
public org.rascalmpl.ast.Assoc getAssociativity() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Prod getGroup() { throw new UnsupportedOperationException(); }
public boolean hasAssociativity() { return false; }
	public boolean hasGroup() { return false; }
public boolean isAssociativityGroup() { return false; }
static public class AssociativityGroup extends Prod {
/** associativity:Assoc "(" group:Prod ")" -> Prod {cons("AssociativityGroup")} */
	protected AssociativityGroup(INode node, org.rascalmpl.ast.Assoc associativity, org.rascalmpl.ast.Prod group) {
		this.node = node;
		this.associativity = associativity;
		this.group = group;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAssociativityGroup(this);
	}

	public boolean isAssociativityGroup() { return true; }

	public boolean hasAssociativity() { return true; }
	public boolean hasGroup() { return true; }

private final org.rascalmpl.ast.Assoc associativity;
	public org.rascalmpl.ast.Assoc getAssociativity() { return associativity; }
	private final org.rascalmpl.ast.Prod group;
	public org.rascalmpl.ast.Prod getGroup() { return group; }	
}
}