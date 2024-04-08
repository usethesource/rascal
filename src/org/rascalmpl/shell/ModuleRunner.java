package org.rascalmpl.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

public class ModuleRunner implements ShellRunner {

  private final Evaluator eval;

  public ModuleRunner(InputStream input, OutputStream stdout, OutputStream stderr, IRascalMonitor monitor) {
      eval = ShellEvaluatorFactory.getDefaultEvaluator(input, stdout, stderr, monitor);
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

    if (v != null && !(v instanceof IInteger)) {
      new StandardTextWriter(true).write(v, eval.getOutPrinter());
      eval.getOutPrinter().flush();
    }

    System.exit(v instanceof IInteger ? ((IInteger) v).intValue() : 0);
    return;
  }

}
