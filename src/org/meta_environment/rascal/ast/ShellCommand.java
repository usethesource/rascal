package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ShellCommand extends AbstractAST {
	static public class Ambiguity extends ShellCommand {
		private final java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitShellCommandAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.ShellCommand> getAlternatives() {
			return alternatives;
		}
	}

	static public class Edit extends ShellCommand {
		private org.meta_environment.rascal.ast.Name name;

		/* "edit" name:Name -> ShellCommand {cons("Edit")} */
		private Edit() {
		}

		/* package */Edit(ITree tree, org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitShellCommandEdit(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isEdit() {
			return true;
		}

		public Edit setName(org.meta_environment.rascal.ast.Name x) {
			final Edit z = new Edit();
			z.$setName(x);
			return z;
		}
	}

	static public class Help extends ShellCommand {
		/* package */Help(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitShellCommandHelp(this);
		}

		@Override
		public boolean isHelp() {
			return true;
		}
	}

	static public class History extends ShellCommand {
		/* package */History(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitShellCommandHistory(this);
		}

		@Override
		public boolean isHistory() {
			return true;
		}
	}

	static public class Quit extends ShellCommand {
		/* package */Quit(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitShellCommandQuit(this);
		}

		@Override
		public boolean isQuit() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isEdit() {
		return false;
	}

	public boolean isHelp() {
		return false;
	}

	public boolean isHistory() {
		return false;
	}

	public boolean isQuit() {
		return false;
	}
}