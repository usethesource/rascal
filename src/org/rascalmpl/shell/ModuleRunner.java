package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.rascalmpl.interpreter.Evaluator;

public class ModuleRunner implements ShellRunner {

  private final Evaluator eval;

  public ModuleRunner(PrintWriter stdout, PrintWriter stderr) {
    eval = ShellEvaluatorFactory.getDefaultEvaluator(stdout, stderr);
  }

  @Override
  public void run(String args[]) throws IOException {
    String module = args[0];
    if (module.endsWith(".rsc")) {
      module = module.substring(0, module.length() - 4);
    }
    module = module.replaceAll("/", "::");

    eval.doImport(null, module);
    String[] realArgs = new String[args.length - 1];
    System.arraycopy(args, 1, realArgs, 0, args.length - 1);

    IValue v = eval.main(null, module, "main", realArgs);

    if (v != null) {
      new StandardTextWriter(true).write(v, eval.getStdOut());
      eval.getStdOut().flush();
    }

    System.exit(0);
    return;
  }

}
