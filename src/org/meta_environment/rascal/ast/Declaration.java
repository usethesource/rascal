package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class Declaration extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getView() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Name getSuperType() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() { throw new UnsupportedOperationException(); }
public boolean hasView() { return false; }
	public boolean hasSuperType() { return false; } public boolean hasTags() { return false; } public boolean hasAlts() { return false; }
public boolean isView() { return false; }
static public class View extends Declaration {
/** "view" view:Name "<:" superType:Name tags:Tags "=" alts:{Alternative "|"}+ ";" -> Declaration {cons("View")} */
	private View() {
		super();
	}
	public View(INode node, org.meta_environment.rascal.ast.Name view, org.meta_environment.rascal.ast.Name superType, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Alternative> alts) {
		this.node = node;
		this.view = view;
		this.superType = superType;
		this.tags = tags;
		this.alts = alts;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationView(this);
	}

	public boolean isView() { return true; }

	public boolean hasView() { return true; }
	public boolean hasSuperType() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasAlts() { return true; }

private org.meta_environment.rascal.ast.Name view;
	public org.meta_environment.rascal.ast.Name getView() { return view; }
	private void $setView(org.meta_environment.rascal.ast.Name x) { this.view = x; }
	public View setView(org.meta_environment.rascal.ast.Name x) { 
		View z = new View();
 		z.$setView(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name superType;
	public org.meta_environment.rascal.ast.Name getSuperType() { return superType; }
	private void $setSuperType(org.meta_environment.rascal.ast.Name x) { this.superType = x; }
	public View setSuperType(org.meta_environment.rascal.ast.Name x) { 
		View z = new View();
 		z.$setSuperType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public View setTags(org.meta_environment.rascal.ast.Tags x) { 
		View z = new View();
 		z.$setTags(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Alternative> alts;
	public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() { return alts; }
	private void $setAlts(java.util.List<org.meta_environment.rascal.ast.Alternative> x) { this.alts = x; }
	public View setAlts(java.util.List<org.meta_environment.rascal.ast.Alternative> x) { 
		View z = new View();
 		z.$setAlts(x);
		return z;
	}	
}
static public class Ambiguity extends Declaration {
  private final java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Declaration> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDeclarationAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.UserType getUser() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Type getBase() { throw new UnsupportedOperationException(); } public boolean hasUser() { return false; } public boolean hasBase() { return false; } public boolean isAlias() { return false; }
static public class Alias extends Declaration {
/** "alias" user:UserType "=" base:Type tags:Tags ";" -> Declaration {cons("Alias")} */
	private Alias() {
		super();
	}
	public Alias(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Type base, org.meta_environment.rascal.ast.Tags tags) {
		this.node = node;
		this.user = user;
		this.base = base;
		this.tags = tags;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAlias(this);
	}

	public boolean isAlias() { return true; }

	public boolean hasUser() { return true; }
	public boolean hasBase() { return true; }
	public boolean hasTags() { return true; }

private org.meta_environment.rascal.ast.UserType user;
	public org.meta_environment.rascal.ast.UserType getUser() { return user; }
	private void $setUser(org.meta_environment.rascal.ast.UserType x) { this.user = x; }
	public Alias setUser(org.meta_environment.rascal.ast.UserType x) { 
		Alias z = new Alias();
 		z.$setUser(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Type base;
	public org.meta_environment.rascal.ast.Type getBase() { return base; }
	private void $setBase(org.meta_environment.rascal.ast.Type x) { this.base = x; }
	public Alias setBase(org.meta_environment.rascal.ast.Type x) { 
		Alias z = new Alias();
 		z.$setBase(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Alias setTags(org.meta_environment.rascal.ast.Tags x) { 
		Alias z = new Alias();
 		z.$setTags(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() { throw new UnsupportedOperationException(); } public boolean hasVariants() { return false; }
public boolean isData() { return false; }
static public class Data extends Declaration {
/** "data" user:UserType tags:Tags "=" variants:{Variant "|"}+ ";" -> Declaration {cons("Data")} */
	private Data() {
		super();
	}
	public Data(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Variant> variants) {
		this.node = node;
		this.user = user;
		this.tags = tags;
		this.variants = variants;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationData(this);
	}

	public boolean isData() { return true; }

	public boolean hasUser() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasVariants() { return true; }

private org.meta_environment.rascal.ast.UserType user;
	public org.meta_environment.rascal.ast.UserType getUser() { return user; }
	private void $setUser(org.meta_environment.rascal.ast.UserType x) { this.user = x; }
	public Data setUser(org.meta_environment.rascal.ast.UserType x) { 
		Data z = new Data();
 		z.$setUser(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Data setTags(org.meta_environment.rascal.ast.Tags x) { 
		Data z = new Data();
 		z.$setTags(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Variant> variants;
	public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() { return variants; }
	private void $setVariants(java.util.List<org.meta_environment.rascal.ast.Variant> x) { this.variants = x; }
	public Data setVariants(java.util.List<org.meta_environment.rascal.ast.Variant> x) { 
		Data z = new Data();
 		z.$setVariants(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Test getTest() { throw new UnsupportedOperationException(); }
public boolean hasTest() { return false; }
public boolean isTest() { return false; }
static public class Test extends Declaration {
/** test:Test ";" -> Declaration {cons("Test")} */
	private Test() {
		super();
	}
	public Test(INode node, org.meta_environment.rascal.ast.Test test) {
		this.node = node;
		this.test = test;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTest(this);
	}

	public boolean isTest() { return true; }

	public boolean hasTest() { return true; }

private org.meta_environment.rascal.ast.Test test;
	public org.meta_environment.rascal.ast.Test getTest() { return test; }
	private void $setTest(org.meta_environment.rascal.ast.Test x) { this.test = x; }
	public Test setTest(org.meta_environment.rascal.ast.Test x) { 
		Test z = new Test();
 		z.$setTest(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasFunctionDeclaration() { return false; }
public boolean isFunction() { return false; }
static public class Function extends Declaration {
/** functionDeclaration:FunctionDeclaration -> Declaration {cons("Function")} */
	private Function() {
		super();
	}
	public Function(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
		this.node = node;
		this.functionDeclaration = functionDeclaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationFunction(this);
	}

	public boolean isFunction() { return true; }

	public boolean hasFunctionDeclaration() { return true; }

private org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;
	public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { return functionDeclaration; }
	private void $setFunctionDeclaration(org.meta_environment.rascal.ast.FunctionDeclaration x) { this.functionDeclaration = x; }
	public Function setFunctionDeclaration(org.meta_environment.rascal.ast.FunctionDeclaration x) { 
		Function z = new Function();
 		z.$setFunctionDeclaration(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasVariables() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Declaration {
/** type:Type variables:{Variable ","}+ ";" -> Declaration {cons("Variable")} */
	private Variable() {
		super();
	}
	public Variable(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) {
		this.node = node;
		this.type = type;
		this.variables = variables;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationVariable(this);
	}

	public boolean isVariable() { return true; }

	public boolean hasType() { return true; }
	public boolean hasVariables() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public Variable setType(org.meta_environment.rascal.ast.Type x) { 
		Variable z = new Variable();
 		z.$setType(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Variable> variables;
	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() { return variables; }
	private void $setVariables(java.util.List<org.meta_environment.rascal.ast.Variable> x) { this.variables = x; }
	public Variable setVariables(java.util.List<org.meta_environment.rascal.ast.Variable> x) { 
		Variable z = new Variable();
 		z.$setVariables(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.PatternWithAction getPatternAction() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasPatternAction() { return false; }
public boolean isRule() { return false; }
static public class Rule extends Declaration {
/** "rule" name:Name tags:Tags patternAction:PatternWithAction ";" -> Declaration {cons("Rule")} */
	private Rule() {
		super();
	}
	public Rule(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.PatternWithAction patternAction) {
		this.node = node;
		this.name = name;
		this.tags = tags;
		this.patternAction = patternAction;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationRule(this);
	}

	public boolean isRule() { return true; }

	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasPatternAction() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Rule setName(org.meta_environment.rascal.ast.Name x) { 
		Rule z = new Rule();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Rule setTags(org.meta_environment.rascal.ast.Tags x) { 
		Rule z = new Rule();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.PatternWithAction patternAction;
	public org.meta_environment.rascal.ast.PatternWithAction getPatternAction() { return patternAction; }
	private void $setPatternAction(org.meta_environment.rascal.ast.PatternWithAction x) { this.patternAction = x; }
	public Rule setPatternAction(org.meta_environment.rascal.ast.PatternWithAction x) { 
		Rule z = new Rule();
 		z.$setPatternAction(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Type getAnnoType() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Type getOnType() { throw new UnsupportedOperationException(); } public boolean hasAnnoType() { return false; }
	public boolean hasOnType() { return false; } public boolean isAnnotation() { return false; }
static public class Annotation extends Declaration {
/** "anno" annoType:Type onType:Type "@" name:Name tags:Tags ";" -> Declaration {cons("Annotation")} */
	private Annotation() {
		super();
	}
	public Annotation(INode node, org.meta_environment.rascal.ast.Type annoType, org.meta_environment.rascal.ast.Type onType, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) {
		this.node = node;
		this.annoType = annoType;
		this.onType = onType;
		this.name = name;
		this.tags = tags;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationAnnotation(this);
	}

	public boolean isAnnotation() { return true; }

	public boolean hasAnnoType() { return true; }
	public boolean hasOnType() { return true; }
	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }

private org.meta_environment.rascal.ast.Type annoType;
	public org.meta_environment.rascal.ast.Type getAnnoType() { return annoType; }
	private void $setAnnoType(org.meta_environment.rascal.ast.Type x) { this.annoType = x; }
	public Annotation setAnnoType(org.meta_environment.rascal.ast.Type x) { 
		Annotation z = new Annotation();
 		z.$setAnnoType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Type onType;
	public org.meta_environment.rascal.ast.Type getOnType() { return onType; }
	private void $setOnType(org.meta_environment.rascal.ast.Type x) { this.onType = x; }
	public Annotation setOnType(org.meta_environment.rascal.ast.Type x) { 
		Annotation z = new Annotation();
 		z.$setOnType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Annotation setName(org.meta_environment.rascal.ast.Name x) { 
		Annotation z = new Annotation();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Annotation setTags(org.meta_environment.rascal.ast.Tags x) { 
		Annotation z = new Annotation();
 		z.$setTags(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Kind getKind() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() { throw new UnsupportedOperationException(); }
public boolean hasKind() { return false; } public boolean hasTypes() { return false; }
public boolean isTag() { return false; }
static public class Tag extends Declaration {
/** "tag"  kind:Kind name:Name tags:Tags "on" types:{Type ","}+ ";" -> Declaration {cons("Tag")} */
	private Tag() {
		super();
	}
	public Tag(INode node, org.meta_environment.rascal.ast.Kind kind, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Type> types) {
		this.node = node;
		this.kind = kind;
		this.name = name;
		this.tags = tags;
		this.types = types;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitDeclarationTag(this);
	}

	public boolean isTag() { return true; }

	public boolean hasKind() { return true; }
	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasTypes() { return true; }

private org.meta_environment.rascal.ast.Kind kind;
	public org.meta_environment.rascal.ast.Kind getKind() { return kind; }
	private void $setKind(org.meta_environment.rascal.ast.Kind x) { this.kind = x; }
	public Tag setKind(org.meta_environment.rascal.ast.Kind x) { 
		Tag z = new Tag();
 		z.$setKind(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Tag setName(org.meta_environment.rascal.ast.Name x) { 
		Tag z = new Tag();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Tag setTags(org.meta_environment.rascal.ast.Tags x) { 
		Tag z = new Tag();
 		z.$setTags(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Type> types;
	public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() { return types; }
	private void $setTypes(java.util.List<org.meta_environment.rascal.ast.Type> x) { this.types = x; }
	public Tag setTypes(java.util.List<org.meta_environment.rascal.ast.Type> x) { 
		Tag z = new Tag();
 		z.$setTypes(x);
		return z;
	}	
}
}