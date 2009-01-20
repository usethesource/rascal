package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Command extends AbstractAST {
	static public class Ambiguity extends Command {
		private final java.util.List<org.meta_environment.rascal.ast.Command> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Command> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommandAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Command> getAlternatives() {
			return alternatives;
		}
	}

	static public class Declaration extends Command {
		private org.meta_environment.rascal.ast.Declaration declaration;

		/* declaration:Declaration -> Command {avoid, cons("Declaration")} */
		private Declaration() {
		}

		/* package */Declaration(ITree tree,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.tree = tree;
			this.declaration = declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandDeclaration(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		@Override
		public boolean isDeclaration() {
			return true;
		}

		public Declaration setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			final Declaration z = new Declaration();
			z.$setDeclaration(x);
			return z;
		}
	}

	static public class Import extends Command {
		private org.meta_environment.rascal.ast.Import imported;

		/* imported:Import -> Command {cons("Import")} */
		private Import() {
		}

		/* package */Import(ITree tree,
				org.meta_environment.rascal.ast.Import imported) {
			this.tree = tree;
			this.imported = imported;
		}

		private void $setImported(org.meta_environment.rascal.ast.Import x) {
			this.imported = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandImport(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Import getImported() {
			return imported;
		}

		@Override
		public boolean hasImported() {
			return true;
		}

		@Override
		public boolean isImport() {
			return true;
		}

		public Import setImported(org.meta_environment.rascal.ast.Import x) {
			final Import z = new Import();
			z.$setImported(x);
			return z;
		}
	}

	static public class Shell extends Command {
		private org.meta_environment.rascal.ast.ShellCommand command;

		/* ":" command:ShellCommand -> Command {cons("Shell")} */
		private Shell() {
		}

		/* package */Shell(ITree tree,
				org.meta_environment.rascal.ast.ShellCommand command) {
			this.tree = tree;
			this.command = command;
		}

		private void $setCommand(org.meta_environment.rascal.ast.ShellCommand x) {
			this.command = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandShell(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ShellCommand getCommand() {
			return command;
		}

		@Override
		public boolean hasCommand() {
			return true;
		}

		@Override
		public boolean isShell() {
			return true;
		}

		public Shell setCommand(org.meta_environment.rascal.ast.ShellCommand x) {
			final Shell z = new Shell();
			z.$setCommand(x);
			return z;
		}
	}

	static public class Statement extends Command {
		private org.meta_environment.rascal.ast.Statement statement;

		/* statement:Statement -> Command {cons("Statement")} */
		private Statement() {
		}

		/* package */Statement(ITree tree,
				org.meta_environment.rascal.ast.Statement statement) {
			this.tree = tree;
			this.statement = statement;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandStatement(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		@Override
		public boolean isStatement() {
			return true;
		}

		public Statement setStatement(
				org.meta_environment.rascal.ast.Statement x) {
			final Statement z = new Statement();
			z.$setStatement(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.ShellCommand getCommand() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Declaration getDeclaration() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Import getImported() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCommand() {
		return false;
	}

	public boolean hasDeclaration() {
		return false;
	}

	public boolean hasImported() {
		return false;
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isDeclaration() {
		return false;
	}

	public boolean isImport() {
		return false;
	}

	public boolean isShell() {
		return false;
	}

	public boolean isStatement() {
		return false;
	}
}