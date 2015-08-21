package org.rascalmpl.repl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import jline.Terminal;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class BaseRascalREPL extends BaseREPL {

  private final static int LINE_LIMIT = 200;
  private final static int CHAR_LIMIT = LINE_LIMIT * 20;
  protected String currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
  private StringBuffer currentCommand;
  private final StandardTextWriter indentedPrettyPrinter;
  private final StandardTextWriter singleLinePrettyPrinter;
  
  public BaseRascalREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File persistentHistory,Terminal terminal)
      throws IOException {
    super(stdin, stdout, prettyPrompt, allowColors, persistentHistory, terminal);
    if (terminal.isAnsiSupported() && allowColors) {
      indentedPrettyPrinter = new ReplTextWriter();
      singleLinePrettyPrinter = new ReplTextWriter(false);
    }
    else {
      indentedPrettyPrinter = new StandardTextWriter();
      singleLinePrettyPrinter = new StandardTextWriter(false);
    }
  }

  @Override
  protected String getPrompt() {
    return currentPrompt;
  }

  @Override
  protected void handleInput(String line) throws InterruptedException {
    assert line != null;

    try {
      if (line.trim().length() == 0) {
        // cancel command
        getErrorWriter().println(ReadEvalPrintDialogMessages.CANCELLED);
        currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
        currentCommand = null;
        return;
      }
      if (currentCommand == null) {
        // we are still at a new command so let's see if the line is a full command
        if (isStatementComplete(line)) {
          printResult(evalStatement(line, line));
        }
        else {
          currentCommand = new StringBuffer(line);
          currentPrompt = ReadEvalPrintDialogMessages.CONTINUE_PROMPT;
          return;
        }
      }
      else {
        currentCommand.append('\n');
        currentCommand.append(line);
        if (isStatementComplete(currentCommand.toString())) {
          printResult(evalStatement(currentCommand.toString(), line));
          currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
          currentCommand = null;
          return;
        }
      }
    } catch (IOException ie) {
      throw new RuntimeException(ie);
    }
  }
  
  private void printResult(IRascalResult result) throws IOException {
    if (result == null) {
      return;
    }
    PrintWriter out = getOutputWriter();
    IValue value = result.getValue();
    if (value == null) {
      out.println("ok");
      return;
    }
    Type type = result.getType();

    if (type.isAbstractData() && type.isSubtypeOf(RascalValueFactory.Tree)) {
    	out.print(type.toString());
        out.print(": ");
      // we first unparse the tree
      out.print("`");
      TreeAdapter.yield((IConstructor)result.getValue(), true, out);
      out.print("`\n");
      // write parse tree out one a single line for reference
      out.print("Tree: ");
      try (Writer wrt = new LimitedWriter(out, CHAR_LIMIT)) {
    	  singleLinePrettyPrinter.write(value, wrt);
      }
    }
    else {
    	out.print(type.toString());
    	out.print(": ");
    	// limit both the lines and the characters
    	try (Writer wrt = new LimitedWriter(new LimitedLineWriter(out, LINE_LIMIT), CHAR_LIMIT)) {
    		indentedPrettyPrinter.write(value, wrt);
    	}
    }
    out.println();
  }

  protected abstract PrintWriter getErrorWriter();
  protected abstract PrintWriter getOutputWriter();

  protected abstract boolean isStatementComplete(String command);
  protected abstract IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException;

}
