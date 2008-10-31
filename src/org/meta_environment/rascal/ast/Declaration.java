package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Declaration extends AbstractAST {
	static public class Ambiguity extends Declaration {
		private final java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Declaration> getAlternatives() {
			return alternatives;
		}
	}

	static public class Annotation extends Declaration {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.Type type;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationAnnotation(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		public Annotation setName(org.meta_environment.rascal.ast.Name x) {
			Annotation z = new Annotation();
			z.$setName(x);
			return z;
		}

		public Annotation setTags(org.meta_environment.rascal.ast.Tags x) {
			Annotation z = new Annotation();
			z.$setTags(x);
			return z;
		}

		public Annotation setType(org.meta_environment.rascal.ast.Type x) {
			Annotation z = new Annotation();
			z.$setType(x);
			return z;
		}

		public Annotation setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			Annotation z = new Annotation();
			z.$setTypes(x);
			return z;
		}
	}

	static public class Data extends Declaration {
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.UserType user;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationData(this);
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		public java.util.List<org.meta_environment.rascal.ast.Variant> getVariants() {
			return variants;
		}

		public Data setTags(org.meta_environment.rascal.ast.Tags x) {
			Data z = new Data();
			z.$setTags(x);
			return z;
		}

		public Data setUser(org.meta_environment.rascal.ast.UserType x) {
			Data z = new Data();
			z.$setUser(x);
			return z;
		}

		public Data setVariants(
				java.util.List<org.meta_environment.rascal.ast.Variant> x) {
			Data z = new Data();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationFunction(this);
		}

		public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
			return functionDeclaration;
		}

		public Function setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			Function z = new Function();
			z.$setFunctionDeclaration(x);
			return z;
		}
	}

	static public class Rule extends Declaration {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Rule rule;
		private org.meta_environment.rascal.ast.Tags tags;

		/* "rule" name:Name tags:Tags rule:Rule -> Declaration {cons("Rule")} */
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationRule(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public Rule setName(org.meta_environment.rascal.ast.Name x) {
			Rule z = new Rule();
			z.$setName(x);
			return z;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			Rule z = new Rule();
			z.$setRule(x);
			return z;
		}

		public Rule setTags(org.meta_environment.rascal.ast.Tags x) {
			Rule z = new Rule();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationTag(this);
		}

		public org.meta_environment.rascal.ast.Kind getKind() {
			return kind;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		public Tag setKind(org.meta_environment.rascal.ast.Kind x) {
			Tag z = new Tag();
			z.$setKind(x);
			return z;
		}

		public Tag setName(org.meta_environment.rascal.ast.Name x) {
			Tag z = new Tag();
			z.$setName(x);
			return z;
		}

		public Tag setTags(org.meta_environment.rascal.ast.Tags x) {
			Tag z = new Tag();
			z.$setTags(x);
			return z;
		}

		public Tag setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			Tag z = new Tag();
			z.$setTypes(x);
			return z;
		}
	}

	static public class Type extends Declaration {
		private org.meta_environment.rascal.ast.Type base;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.UserType user;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationType(this);
		}

		public org.meta_environment.rascal.ast.Type getBase() {
			return base;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		public Type setBase(org.meta_environment.rascal.ast.Type x) {
			Type z = new Type();
			z.$setBase(x);
			return z;
		}

		public Type setTags(org.meta_environment.rascal.ast.Tags x) {
			Type z = new Type();
			z.$setTags(x);
			return z;
		}

		public Type setUser(org.meta_environment.rascal.ast.UserType x) {
			Type z = new Type();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationVariable(this);
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
			return variables;
		}

		public Variable setType(org.meta_environment.rascal.ast.Type x) {
			Variable z = new Variable();
			z.$setType(x);
			return z;
		}

		public Variable setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			Variable z = new Variable();
			z.$setVariables(x);
			return z;
		}
	}

	static public class View extends Declaration {
		private java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.Name type;
		private org.meta_environment.rascal.ast.Name view;

		/*
		 * "view" view:Name "<:" type:Name tags:Tags alternatives:{Alternative
		 * "|"}+ ";" -> Declaration {cons("View")}
		 */
		private View() {
		}

		/* package */View(
				ITree tree,
				org.meta_environment.rascal.ast.Name view,
				org.meta_environment.rascal.ast.Name type,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) {
			this.tree = tree;
			this.view = view;
			this.type = type;
			this.tags = tags;
			this.alternatives = alternatives;
		}

		private void $setAlternatives(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			this.alternatives = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Name x) {
			this.type = x;
		}

		private void $setView(org.meta_environment.rascal.ast.Name x) {
			this.view = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitDeclarationView(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlternatives() {
			return alternatives;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public org.meta_environment.rascal.ast.Name getType() {
			return type;
		}

		public org.meta_environment.rascal.ast.Name getView() {
			return view;
		}

		public View setAlternatives(
				java.util.List<org.meta_environment.rascal.ast.Alternative> x) {
			View z = new View();
			z.$setAlternatives(x);
			return z;
		}

		public View setTags(org.meta_environment.rascal.ast.Tags x) {
			View z = new View();
			z.$setTags(x);
			return z;
		}

		public View setType(org.meta_environment.rascal.ast.Name x) {
			View z = new View();
			z.$setType(x);
			return z;
		}

		public View setView(org.meta_environment.rascal.ast.Name x) {
			View z = new View();
			z.$setView(x);
			return z;
		}
	}
}
