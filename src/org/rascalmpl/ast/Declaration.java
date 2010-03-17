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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationView(this);
	}

	@Override
	public boolean isView() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasView() { return true; }
	@Override
	public boolean hasSuperType() { return true; }
	@Override
	public boolean hasAlts() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Name view;
	@Override
	public org.rascalmpl.ast.Name getView() { return view; }
	private final org.rascalmpl.ast.Name superType;
	@Override
	public org.rascalmpl.ast.Name getSuperType() { return superType; }
	private final java.util.List<org.rascalmpl.ast.Alternative> alts;
	@Override
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
  
  @Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAlias(this);
	}

	@Override
	public boolean isAlias() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasUser() { return true; }
	@Override
	public boolean hasBase() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	@Override
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final org.rascalmpl.ast.Type base;
	@Override
	public org.rascalmpl.ast.Type getBase() { return base; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.rascalmpl.ast.Variant> getVariants() { throw new UnsupportedOperationException(); } public boolean hasVariants() { return false; }
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationData(this);
	}

	@Override
	public boolean isData() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasUser() { return true; }
	@Override
	public boolean hasVariants() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	@Override
	public org.rascalmpl.ast.UserType getUser() { return user; }
	private final java.util.List<org.rascalmpl.ast.Variant> variants;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationDataAbstract(this);
	}

	@Override
	public boolean isDataAbstract() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasUser() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.UserType user;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTest(this);
	}

	@Override
	public boolean isTest() { return true; }

	@Override
	public boolean hasTest() { return true; }

private final org.rascalmpl.ast.Test test;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationFunction(this);
	}

	@Override
	public boolean isFunction() { return true; }

	@Override
	public boolean hasFunctionDeclaration() { return true; }

private final org.rascalmpl.ast.FunctionDeclaration functionDeclaration;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationVariable(this);
	}

	@Override
	public boolean isVariable() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasType() { return true; }
	@Override
	public boolean hasVariables() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Type type;
	@Override
	public org.rascalmpl.ast.Type getType() { return type; }
	private final java.util.List<org.rascalmpl.ast.Variable> variables;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationRule(this);
	}

	@Override
	public boolean isRule() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasPatternAction() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.PatternWithAction patternAction;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAnnotation(this);
	}

	@Override
	public boolean isAnnotation() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasAnnoType() { return true; }
	@Override
	public boolean hasOnType() { return true; }
	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Type annoType;
	@Override
	public org.rascalmpl.ast.Type getAnnoType() { return annoType; }
	private final org.rascalmpl.ast.Type onType;
	@Override
	public org.rascalmpl.ast.Type getOnType() { return onType; }
	private final org.rascalmpl.ast.Name name;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTag(this);
	}

	@Override
	public boolean isTag() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasVisibility() { return true; }
	@Override
	public boolean hasKind() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasTypes() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Visibility visibility;
	@Override
	public org.rascalmpl.ast.Visibility getVisibility() { return visibility; }
	private final org.rascalmpl.ast.Kind kind;
	@Override
	public org.rascalmpl.ast.Kind getKind() { return kind; }
	private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Type> types;
	@Override
	public java.util.List<org.rascalmpl.ast.Type> getTypes() { return types; }	
}
}