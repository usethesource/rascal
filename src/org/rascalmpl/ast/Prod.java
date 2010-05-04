package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Prod extends AbstractAST { 
  public boolean isOthers() { return false; }
static public class Others extends Prod {
/** "..." -> Prod {cons("Others")} */
	public Others(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdOthers(this);
	}

	@Override
	public boolean isOthers() { return true; }	
}
static public class Ambiguity extends Prod {
  private final java.util.List<org.rascalmpl.ast.Prod> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Prod> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Prod> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitProdAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Name getReferenced() { throw new UnsupportedOperationException(); }
public boolean hasReferenced() { return false; }
public boolean isReference() { return false; }
static public class Reference extends Prod {
/** ":" referenced:Name -> Prod {cons("Reference")} */
	public Reference(INode node, org.rascalmpl.ast.Name referenced) {
		this.node = node;
		this.referenced = referenced;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdReference(this);
	}

	@Override
	public boolean isReference() { return true; }

	@Override
	public boolean hasReferenced() { return true; }

private final org.rascalmpl.ast.Name referenced;
	@Override
	public org.rascalmpl.ast.Name getReferenced() { return referenced; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Sym> getArgs() { throw new UnsupportedOperationException(); } public boolean hasModifiers() { return false; } public boolean hasName() { return false; } public boolean hasArgs() { return false; } public boolean isLabeled() { return false; }
static public class Labeled extends Prod {
/** modifiers:ProdModifier* name:Name ":" args:Sym* -> Prod {cons("Labeled")} */
	public Labeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Sym> args) {
		this.node = node;
		this.modifiers = modifiers;
		this.name = name;
		this.args = args;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdLabeled(this);
	}

	@Override
	public boolean isLabeled() { return true; }

	@Override
	public boolean hasModifiers() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasArgs() { return true; }

private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
	@Override
	public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Sym> args;
	@Override
	public java.util.List<org.rascalmpl.ast.Sym> getArgs() { return args; }	
} public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Prod {
/** modifiers:ProdModifier* args:Sym* -> Prod {cons("Unlabeled")} */
	public Unlabeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, java.util.List<org.rascalmpl.ast.Sym> args) {
		this.node = node;
		this.modifiers = modifiers;
		this.args = args;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdUnlabeled(this);
	}

	@Override
	public boolean isUnlabeled() { return true; }

	@Override
	public boolean hasModifiers() { return true; }
	@Override
	public boolean hasArgs() { return true; }

private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
	@Override
	public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() { return modifiers; }
	private final java.util.List<org.rascalmpl.ast.Sym> args;
	@Override
	public java.util.List<org.rascalmpl.ast.Sym> getArgs() { return args; }	
} 
public org.rascalmpl.ast.Prod getProd() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.LanguageAction getAction() { throw new UnsupportedOperationException(); }
public boolean hasProd() { return false; }
	public boolean hasAction() { return false; }
public boolean isAction() { return false; }
static public class Action extends Prod {
/** prod:Prod action:LanguageAction -> Prod {cons("Action"), non-assoc} */
	public Action(INode node, org.rascalmpl.ast.Prod prod, org.rascalmpl.ast.LanguageAction action) {
		this.node = node;
		this.prod = prod;
		this.action = action;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAction(this);
	}

	@Override
	public boolean isAction() { return true; }

	@Override
	public boolean hasProd() { return true; }
	@Override
	public boolean hasAction() { return true; }

private final org.rascalmpl.ast.Prod prod;
	@Override
	public org.rascalmpl.ast.Prod getProd() { return prod; }
	private final org.rascalmpl.ast.LanguageAction action;
	@Override
	public org.rascalmpl.ast.LanguageAction getAction() { return action; }	
} public org.rascalmpl.ast.Prod getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Prod getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isSubtract() { return false; }
static public class Subtract extends Prod {
/** lhs:Prod "-" rhs:Prod -> Prod {cons("Subtract"), left} */
	public Subtract(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdSubtract(this);
	}

	@Override
	public boolean isSubtract() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	@Override
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	@Override
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} public boolean isFirst() { return false; }
static public class First extends Prod {
/** lhs:Prod ">" rhs:Prod -> Prod {cons("First"), left} */
	public First(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdFirst(this);
	}

	@Override
	public boolean isFirst() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	@Override
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	@Override
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} public boolean isAll() { return false; }
static public class All extends Prod {
/** lhs:Prod "|" rhs:Prod -> Prod {cons("All"), left} */
	public All(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAll(this);
	}

	@Override
	public boolean isAll() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Prod lhs;
	@Override
	public org.rascalmpl.ast.Prod getLhs() { return lhs; }
	private final org.rascalmpl.ast.Prod rhs;
	@Override
	public org.rascalmpl.ast.Prod getRhs() { return rhs; }	
} 
public org.rascalmpl.ast.Assoc getAssociativity() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Prod getGroup() { throw new UnsupportedOperationException(); }
public boolean hasAssociativity() { return false; }
	public boolean hasGroup() { return false; }
public boolean isAssociativityGroup() { return false; }
static public class AssociativityGroup extends Prod {
/** associativity:Assoc "(" group:Prod ")" -> Prod {cons("AssociativityGroup")} */
	public AssociativityGroup(INode node, org.rascalmpl.ast.Assoc associativity, org.rascalmpl.ast.Prod group) {
		this.node = node;
		this.associativity = associativity;
		this.group = group;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProdAssociativityGroup(this);
	}

	@Override
	public boolean isAssociativityGroup() { return true; }

	@Override
	public boolean hasAssociativity() { return true; }
	@Override
	public boolean hasGroup() { return true; }

private final org.rascalmpl.ast.Assoc associativity;
	@Override
	public org.rascalmpl.ast.Assoc getAssociativity() { return associativity; }
	private final org.rascalmpl.ast.Prod group;
	@Override
	public org.rascalmpl.ast.Prod getGroup() { return group; }	
}
}