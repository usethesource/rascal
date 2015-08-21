package org.rascalmpl.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import jline.TerminalFactory;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.repl.RascalInterpreterREPL;

public class REPLRunner extends RascalInterpreterREPL  implements ShellRunner {

  public REPLRunner(InputStream stdin, OutputStream stdout) throws IOException {
    super(stdin, stdout, true, true, TerminalFactory.get());
    setMeasureCommandTime(false);
  }

  @Override
  protected Evaluator constructEvaluator(Writer stdout, Writer stderr) {
    return ShellEvaluatorFactory.getDefaultEvaluator(new PrintWriter(stdout), new PrintWriter(stderr));
  }

  @Override
  public void run(String[] args) throws IOException {
    // there are no args for now
    run();
  }
}