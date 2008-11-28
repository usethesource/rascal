package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Command extends AbstractAST { 
  public org.meta_environment.rascal.ast.ShellCommand getCommand() { throw new UnsupportedOperationException(); }
public boolean hasCommand() { return false; }
public boolean isShell() { return false; }
static public class Shell extends Command {
/* ":" command:ShellCommand -> Command {cons("Shell")} */
	private Shell() { }
	/*package*/ Shell(ITree tree, org.meta_environment.rascal.ast.ShellCommand command) {
		this.tree = tree;
		this.command = command;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCommandShell(this);
	}

	public boolean isShell() { return true; }

	public boolean hasCommand() { return true; }

private org.meta_environment.rascal.ast.ShellCommand command;
	public org.meta_environment.rascal.ast.ShellCommand getCommand() { return command; }
	private void $setCommand(org.meta_environment.rascal.ast.ShellCommand x) { this.command = x; }
	public Shell setCommand(org.meta_environment.rascal.ast.ShellCommand x) { 
		Shell z = new Shell();
 		z.$setCommand(x);
		return z;
	}	
}
static public class Ambiguity extends Command {
  private final java.util.List<org.meta_environment.rascal.ast.Command> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Command> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Command> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCommandAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Statement getStatement() { throw new UnsupportedOperationException(); }
public boolean hasStatement() { return false; }
public boolean isStatement() { return false; }
static public class Statement extends Command {
/* statement:Statement -> Command {cons("Statement")} */
	private Statement() { }
	/*package*/ Statement(ITree tree, org.meta_environment.rascal.ast.Statement statement) {
		this.tree = tree;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCommandStatement(this);
	}

	public boolean isStatement() { return true; }

	public boolean hasStatement() { return true; }

private org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }
	private void $setStatement(org.meta_environment.rascal.ast.Statement x) { this.statement = x; }
	public Statement setStatement(org.meta_environment.rascal.ast.Statement x) { 
		Statement z = new Statement();
 		z.$setStatement(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Declaration getDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasDeclaration() { return false; }
public boolean isDeclaration() { return false; }
static public class Declaration extends Command {
/* declaration:Declaration -> Command {avoid, cons("Declaration")} */
	private Declaration() { }
	/*package*/ Declaration(ITree tree, org.meta_environment.rascal.ast.Declaration declaration) {
		this.tree = tree;
		this.declaration = declaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCommandDeclaration(this);
	}

	public boolean isDeclaration() { return true; }

	public boolean hasDeclaration() { return true; }

private org.meta_environment.rascal.ast.Declaration declaration;
	public org.meta_environment.rascal.ast.Declaration getDeclaration() { return declaration; }
	private void $setDeclaration(org.meta_environment.rascal.ast.Declaration x) { this.declaration = x; }
	public Declaration setDeclaration(org.meta_environment.rascal.ast.Declaration x) { 
		Declaration z = new Declaration();
 		z.$setDeclaration(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Import getImported() { throw new UnsupportedOperationException(); }
public boolean hasImported() { return false; }
public boolean isImport() { return false; }
static public class Import extends Command {
/* imported:Import -> Command {cons("Import")} */
	private Import() { }
	/*package*/ Import(ITree tree, org.meta_environment.rascal.ast.Import imported) {
		this.tree = tree;
		this.imported = imported;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCommandImport(this);
	}

	public boolean isImport() { return true; }

	public boolean hasImported() { return true; }

private org.meta_environment.rascal.ast.Import imported;
	public org.meta_environment.rascal.ast.Import getImported() { return imported; }
	private void $setImported(org.meta_environment.rascal.ast.Import x) { this.imported = x; }
	public Import setImported(org.meta_environment.rascal.ast.Import x) { 
		Import z = new Import();
 		z.$setImported(x);
		return z;
	}	
}
}