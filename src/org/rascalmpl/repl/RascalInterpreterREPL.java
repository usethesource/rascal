package org.rascalmpl.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

import jline.Terminal;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;

public abstract class RascalInterpreterREPL extends BaseRascalREPL {

  protected Evaluator eval;
  private boolean measureCommandTime;
  
  public RascalInterpreterREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File persistentHistory, Terminal terminal)
      throws IOException {
    super(stdin, stdout, prettyPrompt, allowColors, persistentHistory, terminal);
  }

  public void setMeasureCommandTime(boolean measureCommandTime) {
    this.measureCommandTime = measureCommandTime;
  }
  
  public boolean getMeasureCommandTime() {
    return measureCommandTime;
  }

  @Override
  protected void initialize(Writer stdout, Writer stderr) {
    eval = constructEvaluator(stdout, stderr);
  }
  
  protected abstract Evaluator constructEvaluator(Writer stdout, Writer stderr);
  
  @Override
  protected PrintWriter getErrorWriter() {
    return eval.getStdErr();
  }
  
  @Override
  protected PrintWriter getOutputWriter() {
    return eval.getStdOut();
  }

  @Override
  protected IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException {
    try {
      Result<IValue> value;
      long duration;
      synchronized (eval) {
          Timing tm = new Timing();
          tm.start();
          value = eval.eval(null, statement, URIUtil.rootLocation("prompt"));
          duration = tm.duration();
      }
      if (measureCommandTime) {
        eval.getStdErr().println("\nTime: " + duration + "ms");
      }
      return value;
    }
    catch (ParseError pe) {
      eval.getStdErr().println(parseErrorMessage(lastLine, "prompt", pe));
      return null;
    }
    catch (StaticError e) {
      eval.getStdErr().println(staticErrorMessage(e));
      return null;
    }
    catch (Throw e) {
      eval.getStdErr().println(throwMessage(e));
      return null;
    }
    catch (QuitException q) {
      eval.getStdErr().println("Quiting REPL");
      throw new InterruptedException();
    }
    catch (Throwable e) {
      eval.getStdErr().println(throwableMessage(e, eval.getStackTrace()));
      return null;
    }
  }

  @Override
  protected boolean isStatementComplete(String command) {
    try {
      eval.parseCommand(null, command, URIUtil.rootLocation("prompt"));
    }
    catch (ParseError pe) {
      String[] commandLines = command.split("\n");
      int lastLine = commandLines.length;
      int lastColumn = commandLines[lastLine - 1].length();

      if (pe.getEndLine() + 1 == lastLine && lastColumn <= pe.getEndColumn()) { 
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean supportsCompletion() {
    return true;
  }

  @Override
  protected CompletionResult completeFragment(String line, int cursor) {
    OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
    if (identifier != null) {
      Collection<String> suggestions = eval.completePartialIdentifier(identifier.term);
      if (suggestions != null && ! suggestions.isEmpty()) {
        return new CompletionResult(identifier.offset, identifier.length, suggestions);
      }
    }
    return null;
  }
}
