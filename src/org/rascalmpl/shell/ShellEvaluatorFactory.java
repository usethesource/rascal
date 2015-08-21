package org.rascalmpl.shell;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.values.ValueFactoryFactory;

public class ShellEvaluatorFactory {

  public static Evaluator getDefaultEvaluator(PrintWriter stdout, PrintWriter stderr) {
    GlobalEnvironment heap = new GlobalEnvironment();
    ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
    IValueFactory vf = ValueFactoryFactory.getValueFactory();
    Evaluator evaluator = new Evaluator(vf, stderr, stdout, root, heap);
    evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
    evaluator.setMonitor(new ConsoleRascalMonitor());
    return evaluator;
  }

}
