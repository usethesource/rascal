package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Declaration extends AbstractAST {
	static public class Ambiguity extends Declaration {
		private final java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDeclarationAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Declaration> getAlternatives() {
			return alternatives;
		}
	}

	static public class Annotation extends Declaration {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;

		private java.util.List<org.meta_environment.rascal.ast.Type> types;

		/*
		 * "anno" type:Type name:Name tags:Tags types:{Type "|"}+ ";" ->
		 * Declaration {cons("Annotation")}
		 */
		private Annotation() {
		}

		/* package */Annotation(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Type> types) {
			this.tree = tree;
			this.type = type;
			this.name = name;
			this.tags = tags;
			this.types = types;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		private void $setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.types = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationAnnotation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasTypes() {
			return true;
		}

		@Override
		public boolean isAnnotation() {
			return true;
		}

		public Annotation setName(org.meta_environment.rascal.ast.Name x) {
			final Annotation z = new Annotation();
			z.$setName(x);
			return z;
		}

		public Annotation setTags(org.meta_environment.rascal.ast.Tags x) {
			final Annotation z = new Annotation();
			z.$setTags(x);
			return z;
		}

		public Annotation setType(org.meta_environment.rascal.ast.Type x) {
			final Annotation z = new Annotation();
			z.$setType(x);
			return z;
		}

		public Annotation setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			final Annotation z = new Annotation();
			z.$setTypes(x);
			return z;
		}
	}

	static public class Data extends Declaration {
		private org.meta_environment.rascal.ast.UserType user;
		private org.meta_environment.rascal.ast.Tags tags;
		private java.util.List<org.meta_environment.rascal.ast.Variant> variants;

		/*
		 * "data" user:UserType tags:Tags variants:{Variant "|"}+ ";" ->
		 * Declaration {cons("Data")}
		 */
		private Data() {
		}

		/* package */Data(ITree tree,
				org.meta_environment.rascal.ast.UserType user,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Variant> variants) {
			this.tree = tree;
			this.user = user;
			this.tags = tags;
			this.variants = variants;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		private void $setVariants(
				java.util.List<org.meta_environment.rascal.ast.Variant> x) {
			this.variants = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationData(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() {
			return variants;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		@Override
		public boolean hasVariants() {
			return true;
		}

		@Override
		public boolean isData() {
			return true;
		}

		public Data setTags(org.meta_environment.rascal.ast.Tags x) {
			final Data z = new Data();
			z.$setTags(x);
			return z;
		}

		public Data setUser(org.meta_environment.rascal.ast.UserType x) {
			final Data z = new Data();
			z.$setUser(x);
			return z;
		}

		public Data setVariants(
				java.util.List<org.meta_environment.rascal.ast.Variant> x) {
			final Data z = new Data();
			z.$setVariants(x);
			return z;
		}
	}

	static public class Function extends Declaration {
		private org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;

		/*
		 * functionDeclaration:FunctionDeclaration -> Declaration
		 * {cons("Function")}
		 */
		private Function() {
		}

		/* package */Function(
				ITree tree,
				org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
			this.tree = tree;
			this.functionDeclaration = functionDeclaration;
		}

		private void $setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			this.functionDeclaration = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationFunction(this);
		}

		@Override
		public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
			return functionDeclaration;
		}

		@Override
		public boolean hasFunctionDeclaration() {
			return true;
		}

		@Override
		public boolean isFunction() {
			return true;
		}

		public Function setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			final Function z = new Function();
			z.$setFunctionDeclaration(x);
			return z;
		}
	}

	static public class Rule extends Declaration {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.Rule rule;

		/*
		 * "rule" name:Name tags:Tags rule:Rule ";" -> Declaration
		 * {cons("Rule")}
		 */
		private Rule() {
		}

		/* package */Rule(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				org.meta_environment.rascal.ast.Rule rule) {
			this.tree = tree;
			this.name = name;
			this.tags = tags;
			this.rule = rule;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationRule(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasRule() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean isRule() {
			return true;
		}

		public Rule setName(org.meta_environment.rascal.ast.Name x) {
			final Rule z = new Rule();
			z.$setName(x);
			return z;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			final Rule z = new Rule();
			z.$setRule(x);
			return z;
		}

		public Rule setTags(org.meta_environment.rascal.ast.Tags x) {
			final Rule z = new Rule();
			z.$setTags(x);
			return z;
		}
	}

	static public class Tag extends Declaration {
		private org.meta_environment.rascal.ast.Kind kind;
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;

		private java.util.List<org.meta_environment.rascal.ast.Type> types;

		/*
		 * "tag" kind:Kind name:Name tags:Tags types:{Type "|"}+ ";" ->
		 * Declaration {cons("Tag")}
		 */
		private Tag() {
		}

		/* package */Tag(ITree tree, org.meta_environment.rascal.ast.Kind kind,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Type> types) {
			this.tree = tree;
			this.kind = kind;
			this.name = name;
			this.tags = tags;
			this.types = types;
		}

		private void $setKind(org.meta_environment.rascal.ast.Kind x) {
			this.kind = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.types = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationTag(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Kind getKind() {
			return kind;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		@Override
		public boolean hasKind() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasTypes() {
			return true;
		}

		@Override
		public boolean isTag() {
			return true;
		}

		public Tag setKind(org.meta_environment.rascal.ast.Kind x) {
			final Tag z = new Tag();
			z.$setKind(x);
			return z;
		}

		public Tag setName(org.meta_environment.rascal.ast.Name x) {
			final Tag z = new Tag();
			z.$setName(x);
			return z;
		}

		public Tag setTags(org.meta_environment.rascal.ast.Tags x) {
			final Tag z = new Tag();
			z.$setTags(x);
			return z;
		}

		public Tag setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			final Tag z = new Tag();
			z.$setTypes(x);
			return z;
		}
	}

	static public class Type extends Declaration {
		private org.meta_environment.rascal.ast.Type base;
		private org.meta_environment.rascal.ast.UserType user;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * "type" base:Type user:UserType tags:Tags ";" -> Declaration
		 * {cons("Type")}
		 */
		private Type() {
		}

		/* package */Type(ITree tree,
				org.meta_environment.rascal.ast.Type base,
				org.meta_environment.rascal.ast.UserType user,
				org.meta_environment.rascal.ast.Tags tags) {
			this.tree = tree;
			this.base = base;
			this.user = user;
			this.tags = tags;
		}

		private void $setBase(org.meta_environment.rascal.ast.Type x) {
			this.base = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationType(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Type getBase() {
			return base;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		@Override
		public boolean hasBase() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		@Override
		public boolean isType() {
			return true;
		}

		public Type setBase(org.meta_environment.rascal.ast.Type x) {
			final Type z = new Type();
			z.$setBase(x);
			return z;
		}

		public Type setTags(org.meta_environment.rascal.ast.Tags x) {
			final Type z = new Type();
			z.$setTags(x);
			return z;
		}

		public Type setUser(org.meta_environment.rascal.ast.UserType x) {
			final Type z = new Type();
			z.$setUser(x);
			return z;
		}
	}

	static public class Variable extends Declaration {
		private org.meta_environment.rascal.ast.Type type;
		private java.util.List<org.meta_environment.rascal.ast.Variable> variables;

		/*
		 * type:Type variables:{Variable ","}+ ";" -> Declaration
		 * {cons("Variable")}
		 */
		private Variable() {
		}

		/* package */Variable(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.Variable> variables) {
			this.tree = tree;
			this.type = type;
			this.variables = variables;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		private void $setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			this.variables = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationVariable(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
			return variables;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasVariables() {
			return true;
		}

		@Override
		public boolean isVariable() {
			return true;
		}

		public Variable setType(org.meta_environment.rascal.ast.Type x) {
			final Variable z = new Variable();
			z.$setType(x);
			return z;
		}

		public Variable setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			final Variable z = new Variable();
			z.$setVariables(x);
			return z;
		}
	}

	static public class View extends Declaration {
		private org.meta_environment.rascal.ast.Name view;
		private org.meta_environment.rascal.ast.Name superType;
		private org.meta_environment.rascal.ast.Tags tags;

		private java.util.List<org.meta_environment.rascal.ast.Alternative> alts;

		/*
		 * "view" view:Name "<:" superType:Name tags:Tags alts:{Alternative
		 * "|"}+ ";" -> Declaration {cons("View")}
		 */
		private View() {
		}

		/* package */View(ITree tree,
				org.meta_environment.rascal.ast.Name view,
				org.meta_environment.rascal.ast.Name superType,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Alternative> alts) {
			this.tree = tree;
			this.view = view;
			this.superType = superType;
			this.tags = tags;
			this.alts = alts;
		}

		private void $setAlts(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			this.alts = x;
		}

		private void $setSuperType(org.meta_environment.rascal.ast.Name x) {
			this.superType = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setView(org.meta_environment.rascal.ast.Name x) {
			this.view = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationView(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() {
			return alts;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getSuperType() {
			return superType;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getView() {
			return view;
		}

		@Override
		public boolean hasAlts() {
			return true;
		}

		@Override
		public boolean hasSuperType() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasView() {
			return true;
		}

		@Override
		public boolean isView() {
			return true;
		}

		public View setAlts(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			final View z = new View();
			z.$setAlts(x);
			return z;
		}

		public View setSuperType(org.meta_environment.rascal.ast.Name x) {
			final View z = new View();
			z.$setSuperType(x);
			return z;
		}

		public View setTags(org.meta_environment.rascal.ast.Tags x) {
			final View z = new View();
			z.$setTags(x);
			return z;
		}

		public View setView(org.meta_environment.rascal.ast.Name x) {
			final View z = new View();
			z.$setView(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getBase() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Kind getKind() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getSuperType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.UserType getUser() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getView() {
		throw new UnsupportedOperationException();
	}

	public boolean hasAlts() {
		return false;
	}

	public boolean hasBase() {
		return false;
	}

	public boolean hasFunctionDeclaration() {
		return false;
	}

	public boolean hasKind() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasRule() {
		return false;
	}

	public boolean hasSuperType() {
		return false;
	}

	public boolean hasTags() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasTypes() {
		return false;
	}

	public boolean hasUser() {
		return false;
	}

	public boolean hasVariables() {
		return false;
	}

	public boolean hasVariants() {
		return false;
	}

	public boolean hasView() {
		return false;
	}

	public boolean isAnnotation() {
		return false;
	}

	public boolean isData() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	public boolean isRule() {
		return false;
	}

	public boolean isTag() {
		return false;
	}

	public boolean isType() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	public boolean isView() {
		return false;
	}
}