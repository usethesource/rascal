package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Command extends AbstractAST {
	public org.meta_environment.rascal.ast.ShellCommand getCommand() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCommand() {
		return false;
	}

	public boolean isShell() {
		return false;
	}

	static public class Shell extends Command {
		/* ":" command:ShellCommand -> Command {cons("Shell")} */
		private Shell() {
		}

		/* package */Shell(INode node,
				org.meta_environment.rascal.ast.ShellCommand command) {
			this.node = node;
			this.command = command;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandShell(this);
		}

		@Override
		public boolean isShell() {
			return true;
		}

		@Override
		public boolean hasCommand() {
			return true;
		}

		private org.meta_environment.rascal.ast.ShellCommand command;

		@Override
		public org.meta_environment.rascal.ast.ShellCommand getCommand() {
			return command;
		}

		private void $setCommand(org.meta_environment.rascal.ast.ShellCommand x) {
			this.command = x;
		}

		public Shell setCommand(org.meta_environment.rascal.ast.ShellCommand x) {
			Shell z = new Shell();
			z.$setCommand(x);
			return z;
		}
	}

	static public class Ambiguity extends Command {
		private final java.util.List<org.meta_environment.rascal.ast.Command> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Command> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Command> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommandAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isStatement() {
		return false;
	}

	static public class Statement extends Command {
		/* statement:Statement -> Command {cons("Statement")} */
		private Statement() {
		}

		/* package */Statement(INode node,
				org.meta_environment.rascal.ast.Statement statement) {
			this.node = node;
			this.statement = statement;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandStatement(this);
		}

		@Override
		public boolean isStatement() {
			return true;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		private org.meta_environment.rascal.ast.Statement statement;

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		public Statement setStatement(
				org.meta_environment.rascal.ast.Statement x) {
			Statement z = new Statement();
			z.$setStatement(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Declaration getDeclaration() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDeclaration() {
		return false;
	}

	public boolean isDeclaration() {
		return false;
	}

	static public class Declaration extends Command {
		/* declaration:Declaration -> Command {avoid, cons("Declaration")} */
		private Declaration() {
		}

		/* package */Declaration(INode node,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.node = node;
			this.declaration = declaration;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandDeclaration(this);
		}

		@Override
		public boolean isDeclaration() {
			return true;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		private org.meta_environment.rascal.ast.Declaration declaration;

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		public Declaration setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			Declaration z = new Declaration();
			z.$setDeclaration(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Import getImported() {
		throw new UnsupportedOperationException();
	}

	public boolean hasImported() {
		return false;
	}

	public boolean isImport() {
		return false;
	}

	static public class Import extends Command {
		/* imported:Import -> Command {cons("Import")} */
		private Import() {
		}

		/* package */Import(INode node,
				org.meta_environment.rascal.ast.Import imported) {
			this.node = node;
			this.imported = imported;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCommandImport(this);
		}

		@Override
		public boolean isImport() {
			return true;
		}

		@Override
		public boolean hasImported() {
			return true;
		}

		private org.meta_environment.rascal.ast.Import imported;

		@Override
		public org.meta_environment.rascal.ast.Import getImported() {
			return imported;
		}

		private void $setImported(org.meta_environment.rascal.ast.Import x) {
			this.imported = x;
		}

		public Import setImported(org.meta_environment.rascal.ast.Import x) {
			Import z = new Import();
			z.$setImported(x);
			return z;
		}
	}
}