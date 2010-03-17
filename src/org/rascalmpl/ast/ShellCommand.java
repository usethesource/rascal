package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ShellCommand extends AbstractAST { 
  public boolean isHelp() { return false; }
static public class Help extends ShellCommand {
/** "help" -> ShellCommand {cons("Help")} */
	public Help(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHelp(this);
	}

	@Override
	public boolean isHelp() { return true; }	
}
static public class Ambiguity extends ShellCommand {
  private final java.util.List<org.rascalmpl.ast.ShellCommand> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ShellCommand> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ShellCommand> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitShellCommandAmbiguity(this);
  }
} 
public boolean isQuit() { return false; }
static public class Quit extends ShellCommand {
/** "quit" -> ShellCommand {cons("Quit")} */
	public Quit(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandQuit(this);
	}

	@Override
	public boolean isQuit() { return true; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isEdit() { return false; }
static public class Edit extends ShellCommand {
/** "edit" name:QualifiedName -> ShellCommand {cons("Edit")} */
	public Edit(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandEdit(this);
	}

	@Override
	public boolean isEdit() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} 
public boolean isListModules() { return false; }
static public class ListModules extends ShellCommand {
/** "modules" -> ShellCommand {cons("ListModules")} */
	public ListModules(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandListModules(this);
	}

	@Override
	public boolean isListModules() { return true; }	
} 
public boolean isListDeclarations() { return false; }
static public class ListDeclarations extends ShellCommand {
/** "declarations" -> ShellCommand {cons("ListDeclarations")} */
	public ListDeclarations(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandListDeclarations(this);
	}

	@Override
	public boolean isListDeclarations() { return true; }	
} 
public boolean isTest() { return false; }
static public class Test extends ShellCommand {
/** "test" -> ShellCommand {cons("Test")} */
	public Test(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandTest(this);
	}

	@Override
	public boolean isTest() { return true; }	
} public boolean isUnimport() { return false; }
static public class Unimport extends ShellCommand {
/** "unimport" name:QualifiedName -> ShellCommand {cons("Unimport")} */
	public Unimport(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUnimport(this);
	}

	@Override
	public boolean isUnimport() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} public boolean isUndeclare() { return false; }
static public class Undeclare extends ShellCommand {
/** "undeclare" name:QualifiedName -> ShellCommand {cons("Undeclare")} */
	public Undeclare(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandUndeclare(this);
	}

	@Override
	public boolean isUndeclare() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
} 
public boolean isHistory() { return false; }
static public class History extends ShellCommand {
/** "history" -> ShellCommand {cons("History")} */
	public History(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHistory(this);
	}

	@Override
	public boolean isHistory() { return true; }	
} public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; }
public boolean isSetOption() { return false; }
static public class SetOption extends ShellCommand {
/** "set" name:QualifiedName expression:Expression -> ShellCommand {cons("SetOption")} */
	public SetOption(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.name = name;
		this.expression = expression;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandSetOption(this);
	}

	@Override
	public boolean isSetOption() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
}