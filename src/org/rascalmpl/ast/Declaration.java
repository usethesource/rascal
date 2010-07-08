package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Declaration extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Visibility getVisibility() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Name getView() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Name getSuperType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.Alternative> getAlts() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasVisibility() { return false; } public boolean hasView() { return false; }
	public boolean hasSuperType() { return false; }
	public boolean hasAlts() { return false; }
public boolean isView() { return false; }
static public class View extends Declaration {
/** tags:Tags visibility:Visibility "view" view:Name "<:" superType:Name "=" alts:{Alternative "|"}+ ";" -> Declaration {cons("View")} */
	public View(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Name view, org.rascalmpl.ast.Name superType, java.util.List<org.rascalmpl.ast.Alternative> alts) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.view = view;
		this.superType = superType;
		this.alts = alts;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationView(this);
	}

	public boolean isView() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasView() { return true; }
	public boolean hasSuperType() { return true; }
	public boolean hasAlts() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Name view;
	public org.rascalmpl.ast.Name getView() { return view; }
	private final org.rascalmpl.ast.Name superType;
	public org.rascalmpl.ast.Name getSuperType() { return superType; }
	private final java.util.List<org.rascalmpl.ast.Alternative> alts;
	public java.util.List<org.rascalmpl.ast.Alternative> getAlts() { return alts; }	
}
static public class Ambiguity extends Declaration {
  private final java.util.List<org.rascalmpl.ast.Declaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Declaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Declaration> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDeclarationAmbiguity(this);
  }
} public org.rascalmpl.ast.UserType getUser() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Type getBase() { throw new UnsupportedOperationException(); } public boolean hasUser() { return false; } public boolean hasBase() { return false; }
public boolean isAlias() { return false; }
static public class Alias extends Declaration {
/** tags:Tags visibility:Visibility "alias" user:UserType "=" base:Type ";" -> Declaration {cons("Alias")} */
	public Alias(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user, org.rascalmpl.ast.Type base) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.user = user;
		this.base = base;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAlias(this);
	}

	public boolean isAlias() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasUser() { return true; }
	public boolean hasBase() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final org.rascalmpl.ast.Type base;
	public org.rascalmpl.ast.Type getBase() { return base; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.rascalmpl.ast.Variant> getVariants() { throw new UnsupportedOperationException(); } public boolean hasVariants() { return false; }
public boolean isData() { return false; }
static public class Data extends Declaration {
/** tags:Tags visibility:Visibility "data" user:UserType "=" variants:{Variant "|"}+ ";" -> Declaration {cons("Data")} */
	public Data(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user, java.util.List<org.rascalmpl.ast.Variant> variants) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.user = user;
		this.variants = variants;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationData(this);
	}

	public boolean isData() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasUser() { return true; }
	public boolean hasVariants() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final java.util.List<org.rascalmpl.ast.Variant> variants;
	public java.util.List<org.rascalmpl.ast.Variant> getVariants() { return variants; }	
} public boolean isDataAbstract() { return false; }
static public class DataAbstract extends Declaration {
/** tags:Tags visibility:Visibility "data" user:UserType ";" -> Declaration {cons("DataAbstract")} */
	public DataAbstract(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.user = user;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationDataAbstract(this);
	}

	public boolean isDataAbstract() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasUser() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	public org.rascalmpl.ast.UserType getUser() { return user; }	
} 
public org.rascalmpl.ast.Test getTest() { throw new UnsupportedOperationException(); }
public boolean hasTest() { return false; }
public boolean isTest() { return false; }
static public class Test extends Declaration {
/** test:Test ";" -> Declaration {cons("Test")} */
	public Test(INode node, org.rascalmpl.ast.Test test) {
		this.node = node;
		this.test = test;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTest(this);
	}

	public boolean isTest() { return true; }

	public boolean hasTest() { return true; }

private final org.rascalmpl.ast.Test test;
	public org.rascalmpl.ast.Test getTest() { return test; }	
} 
public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasFunctionDeclaration() { return false; }
public boolean isFunction() { return false; }
static public class Function extends Declaration {
/** functionDeclaration:FunctionDeclaration -> Declaration {cons("Function")} */
	public Function(INode node, org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
		this.node = node;
		this.functionDeclaration = functionDeclaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationFunction(this);
	}

	public boolean isFunction() { return true; }

	public boolean hasFunctionDeclaration() { return true; }

private final org.rascalmpl.ast.FunctionDeclaration functionDeclaration;
	public org.rascalmpl.ast.FunctionDeclaration getFunctionDeclaration() { return functionDeclaration; }	
} public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.Variable> getVariables() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; }
	public boolean hasVariables() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Declaration {
/** tags:Tags visibility:Visibility type:Type variables:{Variable ","}+ ";" -> Declaration {cons("Variable")} */
	public Variable(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.Variable> variables) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.type = type;
		this.variables = variables;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationVariable(this);
	}

	public boolean isVariable() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasType() { return true; }
	public boolean hasVariables() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Type type;
	public org.rascalmpl.ast.Type getType() { return type; }
	private final java.util.List<org.rascalmpl.ast.Variable> variables;
	public java.util.List<org.rascalmpl.ast.Variable> getVariables() { return variables; }	
} public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.PatternWithAction getPatternAction() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasPatternAction() { return false; }
public boolean isRule() { return false; }
static public class Rule extends Declaration {
/** tags:Tags "rule" name:Name patternAction:PatternWithAction ";" -> Declaration {cons("Rule")} */
	public Rule(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Name name, org.rascalmpl.ast.PatternWithAction patternAction) {
		this.node = node;
		this.tags = tags;
		this.name = name;
		this.patternAction = patternAction;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationRule(this);
	}

	public boolean isRule() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasName() { return true; }
	public boolean hasPatternAction() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.PatternWithAction patternAction;
	public org.rascalmpl.ast.PatternWithAction getPatternAction() { return patternAction; }	
} public org.rascalmpl.ast.Type getAnnoType() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Type getOnType() { throw new UnsupportedOperationException(); } public boolean hasAnnoType() { return false; }
	public boolean hasOnType() { return false; } public boolean isAnnotation() { return false; }
static public class Annotation extends Declaration {
/** tags:Tags visibility:Visibility  "anno" annoType:Type onType:Type "@" name:Name ";" -> Declaration {cons("Annotation")} */
	public Annotation(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Type annoType, org.rascalmpl.ast.Type onType, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.annoType = annoType;
		this.onType = onType;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAnnotation(this);
	}

	public boolean isAnnotation() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasAnnoType() { return true; }
	public boolean hasOnType() { return true; }
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Type annoType;
	public org.rascalmpl.ast.Type getAnnoType() { return annoType; }
	private final org.rascalmpl.ast.Type onType;
	public org.rascalmpl.ast.Type getOnType() { return onType; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }	
} public org.rascalmpl.ast.Kind getKind() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Type> getTypes() { throw new UnsupportedOperationException(); } public boolean hasKind() { return false; } public boolean hasTypes() { return false; }
public boolean isTag() { return false; }
static public class Tag extends Declaration {
/** tags:Tags visibility:Visibility  "tag"  kind:Kind name:Name "on" types:{Type ","}+ ";" -> Declaration {cons("Tag")} */
	public Tag(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Kind kind, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Type> types) {
		this.node = node;
		this.tags = tags;
		this.visibility = visibility;
		this.kind = kind;
		this.name = name;
		this.types = types;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTag(this);
	}

	public boolean isTag() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasVisibility() { return true; }
	public boolean hasKind() { return true; }
	public boolean hasName() { return true; }
	public boolean hasTypes() { return true; }

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Kind kind;
	public org.rascalmpl.ast.Kind getKind() { return kind; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Type> types;
	public java.util.List<org.rascalmpl.ast.Type> getTypes() { return types; }	
}
}