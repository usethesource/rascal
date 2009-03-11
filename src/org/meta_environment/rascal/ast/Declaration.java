package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Declaration extends AbstractAST {
	public org.meta_environment.rascal.ast.Name getView() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getSuperType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() {
		throw new UnsupportedOperationException();
	}

	public boolean hasView() {
		return false;
	}

	public boolean hasSuperType() {
		return false;
	}

	public boolean hasTags() {
		return false;
	}

	public boolean hasAlts() {
		return false;
	}

	public boolean isView() {
		return false;
	}

	static public class View extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private View() {
		}

		/* package */View(INode node,
				org.meta_environment.rascal.ast.Name view,
				org.meta_environment.rascal.ast.Name superType,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Alternative> alts) {
			this.node = node;
			this.view = view;
			this.superType = superType;
			this.tags = tags;
			this.alts = alts;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationView(this);
		}

		@Override
		public boolean isView() {
			return true;
		}

		@Override
		public boolean hasView() {
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
		public boolean hasAlts() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name view;

		@Override
		public org.meta_environment.rascal.ast.Name getView() {
			return view;
		}

		private void $setView(org.meta_environment.rascal.ast.Name x) {
			this.view = x;
		}

		public View setView(org.meta_environment.rascal.ast.Name x) {
			View z = new View();
			z.$setView(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Name superType;

		@Override
		public org.meta_environment.rascal.ast.Name getSuperType() {
			return superType;
		}

		private void $setSuperType(org.meta_environment.rascal.ast.Name x) {
			this.superType = x;
		}

		public View setSuperType(org.meta_environment.rascal.ast.Name x) {
			View z = new View();
			z.$setSuperType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public View setTags(org.meta_environment.rascal.ast.Tags x) {
			View z = new View();
			z.$setTags(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Alternative> alts;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlts() {
			return alts;
		}

		private void $setAlts(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			this.alts = x;
		}

		public View setAlts(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			View z = new View();
			z.$setAlts(x);
			return z;
		}
	}

	static public class Ambiguity extends Declaration {
		private final java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Declaration> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDeclarationAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.UserType getUser() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getBase() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUser() {
		return false;
	}

	public boolean hasBase() {
		return false;
	}

	public boolean isAlias() {
		return false;
	}

	static public class Alias extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Alias() {
		}

		/* package */Alias(INode node,
				org.meta_environment.rascal.ast.UserType user,
				org.meta_environment.rascal.ast.Type base,
				org.meta_environment.rascal.ast.Tags tags) {
			this.node = node;
			this.user = user;
			this.base = base;
			this.tags = tags;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationAlias(this);
		}

		@Override
		public boolean isAlias() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		@Override
		public boolean hasBase() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		private org.meta_environment.rascal.ast.UserType user;

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		public Alias setUser(org.meta_environment.rascal.ast.UserType x) {
			Alias z = new Alias();
			z.$setUser(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Type base;

		@Override
		public org.meta_environment.rascal.ast.Type getBase() {
			return base;
		}

		private void $setBase(org.meta_environment.rascal.ast.Type x) {
			this.base = x;
		}

		public Alias setBase(org.meta_environment.rascal.ast.Type x) {
			Alias z = new Alias();
			z.$setBase(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public Alias setTags(org.meta_environment.rascal.ast.Tags x) {
			Alias z = new Alias();
			z.$setTags(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() {
		throw new UnsupportedOperationException();
	}

	public boolean hasVariants() {
		return false;
	}

	public boolean isData() {
		return false;
	}

	static public class Data extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Data() {
		}

		/* package */Data(INode node,
				org.meta_environment.rascal.ast.UserType user,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Variant> variants) {
			this.node = node;
			this.user = user;
			this.tags = tags;
			this.variants = variants;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationData(this);
		}

		@Override
		public boolean isData() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean hasVariants() {
			return true;
		}

		private org.meta_environment.rascal.ast.UserType user;

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		public Data setUser(org.meta_environment.rascal.ast.UserType x) {
			Data z = new Data();
			z.$setUser(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public Data setTags(org.meta_environment.rascal.ast.Tags x) {
			Data z = new Data();
			z.$setTags(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Variant> variants;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() {
			return variants;
		}

		private void $setVariants(
				java.util.List<org.meta_environment.rascal.ast.Variant> x) {
			this.variants = x;
		}

		public Data setVariants(
				java.util.List<org.meta_environment.rascal.ast.Variant> x) {
			Data z = new Data();
			z.$setVariants(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFunctionDeclaration() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	static public class Function extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Function() {
		}

		/* package */Function(
				INode node,
				org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
			this.node = node;
			this.functionDeclaration = functionDeclaration;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationFunction(this);
		}

		@Override
		public boolean isFunction() {
			return true;
		}

		@Override
		public boolean hasFunctionDeclaration() {
			return true;
		}

		private org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;

		@Override
		public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
			return functionDeclaration;
		}

		private void $setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			this.functionDeclaration = x;
		}

		public Function setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			Function z = new Function();
			z.$setFunctionDeclaration(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
		throw new UnsupportedOperationException();
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasVariables() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	static public class Variable extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Variable() {
		}

		/* package */Variable(
				INode node,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.Variable> variables) {
			this.node = node;
			this.type = type;
			this.variables = variables;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationVariable(this);
		}

		@Override
		public boolean isVariable() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasVariables() {
			return true;
		}

		private org.meta_environment.rascal.ast.Type type;

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public Variable setType(org.meta_environment.rascal.ast.Type x) {
			Variable z = new Variable();
			z.$setType(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Variable> variables;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
			return variables;
		}

		private void $setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			this.variables = x;
		}

		public Variable setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			Variable z = new Variable();
			z.$setVariables(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasRule() {
		return false;
	}

	public boolean isRule() {
		return false;
	}

	static public class Rule extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Rule() {
		}

		/* package */Rule(INode node,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				org.meta_environment.rascal.ast.Rule rule) {
			this.node = node;
			this.name = name;
			this.tags = tags;
			this.rule = rule;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationRule(this);
		}

		@Override
		public boolean isRule() {
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
		public boolean hasRule() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Rule setName(org.meta_environment.rascal.ast.Name x) {
			Rule z = new Rule();
			z.$setName(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public Rule setTags(org.meta_environment.rascal.ast.Tags x) {
			Rule z = new Rule();
			z.$setTags(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Rule rule;

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			Rule z = new Rule();
			z.$setRule(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Type getAnnoType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getOnType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasAnnoType() {
		return false;
	}

	public boolean hasOnType() {
		return false;
	}

	public boolean isAnnotation() {
		return false;
	}

	static public class Annotation extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Annotation() {
		}

		/* package */Annotation(INode node,
				org.meta_environment.rascal.ast.Type annoType,
				org.meta_environment.rascal.ast.Type onType,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags) {
			this.node = node;
			this.annoType = annoType;
			this.onType = onType;
			this.name = name;
			this.tags = tags;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationAnnotation(this);
		}

		@Override
		public boolean isAnnotation() {
			return true;
		}

		@Override
		public boolean hasAnnoType() {
			return true;
		}

		@Override
		public boolean hasOnType() {
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

		private org.meta_environment.rascal.ast.Type annoType;

		@Override
		public org.meta_environment.rascal.ast.Type getAnnoType() {
			return annoType;
		}

		private void $setAnnoType(org.meta_environment.rascal.ast.Type x) {
			this.annoType = x;
		}

		public Annotation setAnnoType(org.meta_environment.rascal.ast.Type x) {
			Annotation z = new Annotation();
			z.$setAnnoType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Type onType;

		@Override
		public org.meta_environment.rascal.ast.Type getOnType() {
			return onType;
		}

		private void $setOnType(org.meta_environment.rascal.ast.Type x) {
			this.onType = x;
		}

		public Annotation setOnType(org.meta_environment.rascal.ast.Type x) {
			Annotation z = new Annotation();
			z.$setOnType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Annotation setName(org.meta_environment.rascal.ast.Name x) {
			Annotation z = new Annotation();
			z.$setName(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public Annotation setTags(org.meta_environment.rascal.ast.Tags x) {
			Annotation z = new Annotation();
			z.$setTags(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Kind getKind() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
		throw new UnsupportedOperationException();
	}

	public boolean hasKind() {
		return false;
	}

	public boolean hasTypes() {
		return false;
	}

	public boolean isTag() {
		return false;
	}

	static public class Tag extends Declaration {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Tag() {
		}

		/* package */Tag(INode node, org.meta_environment.rascal.ast.Kind kind,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Type> types) {
			this.node = node;
			this.kind = kind;
			this.name = name;
			this.tags = tags;
			this.types = types;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclarationTag(this);
		}

		@Override
		public boolean isTag() {
			return true;
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

		private org.meta_environment.rascal.ast.Kind kind;

		@Override
		public org.meta_environment.rascal.ast.Kind getKind() {
			return kind;
		}

		private void $setKind(org.meta_environment.rascal.ast.Kind x) {
			this.kind = x;
		}

		public Tag setKind(org.meta_environment.rascal.ast.Kind x) {
			Tag z = new Tag();
			z.$setKind(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Tag setName(org.meta_environment.rascal.ast.Name x) {
			Tag z = new Tag();
			z.$setName(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Tags tags;

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public Tag setTags(org.meta_environment.rascal.ast.Tags x) {
			Tag z = new Tag();
			z.$setTags(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Type> types;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		private void $setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.types = x;
		}

		public Tag setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			Tag z = new Tag();
			z.$setTypes(x);
			return z;
		}
	}
}