package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Header extends AbstractAST {
	static public class Ambiguity extends Header {
		private final java.util.List<org.meta_environment.rascal.ast.Header> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Header> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Header> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Header {
		private java.util.List<org.meta_environment.rascal.ast.Import> imports;
		private org.meta_environment.rascal.ast.ModuleName name;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * "module" name:ModuleName tags:Tags imports:Import -> Header
		 * {cons("Default")}
		 */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Import> imports) {
			this.tree = tree;
			this.name = name;
			this.tags = tags;
			this.imports = imports;
		}

		private void $setImports(
				java.util.List<org.meta_environment.rascal.ast.Import> x) {
			this.imports = x;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitHeaderDefault(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Import> getImports() {
			return imports;
		}

		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public Default setImports(
				java.util.List<org.meta_environment.rascal.ast.Import> x) {
			Default z = new Default();
			z.$setImports(x);
			return z;
		}

		public Default setName(org.meta_environment.rascal.ast.ModuleName x) {
			Default z = new Default();
			z.$setName(x);
			return z;
		}

		public Default setTags(org.meta_environment.rascal.ast.Tags x) {
			Default z = new Default();
			z.$setTags(x);
			return z;
		}
	}

	static public class Parameters extends Header {
		private java.util.List<org.meta_environment.rascal.ast.Import> imports;
		private org.meta_environment.rascal.ast.ModuleName name;
		private org.meta_environment.rascal.ast.ModuleParameters params;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * "module" name:ModuleName params:ModuleParameters tags:Tags
		 * imports:Import -> Header {cons("Parameters")}
		 */
		private Parameters() {
		}

		/* package */Parameters(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name,
				org.meta_environment.rascal.ast.ModuleParameters params,
				org.meta_environment.rascal.ast.Tags tags,
				java.util.List<org.meta_environment.rascal.ast.Import> imports) {
			this.tree = tree;
			this.name = name;
			this.params = params;
			this.tags = tags;
			this.imports = imports;
		}

		private void $setImports(
				java.util.List<org.meta_environment.rascal.ast.Import> x) {
			this.imports = x;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		private void $setParams(
				org.meta_environment.rascal.ast.ModuleParameters x) {
			this.params = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitHeaderParameters(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Import> getImports() {
			return imports;
		}

		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.ModuleParameters getParams() {
			return params;
		}

		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public Parameters setImports(
				java.util.List<org.meta_environment.rascal.ast.Import> x) {
			Parameters z = new Parameters();
			z.$setImports(x);
			return z;
		}

		public Parameters setName(org.meta_environment.rascal.ast.ModuleName x) {
			Parameters z = new Parameters();
			z.$setName(x);
			return z;
		}

		public Parameters setParams(
				org.meta_environment.rascal.ast.ModuleParameters x) {
			Parameters z = new Parameters();
			z.$setParams(x);
			return z;
		}

		public Parameters setTags(org.meta_environment.rascal.ast.Tags x) {
			Parameters z = new Parameters();
			z.$setTags(x);
			return z;
		}
	}
}
