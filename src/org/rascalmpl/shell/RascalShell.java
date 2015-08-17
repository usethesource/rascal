package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;

import jline.TerminalFactory;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.staticErrors.CommandlineError;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;


public class RascalShell  {

  private static final boolean PRINTCOMMANDTIME = false;
  protected static final String META_INF = "META-INF";
  protected static final String META_INF_MANIFEST_MF = META_INF + "/MANIFEST.MF";

  public static class NormalREPL extends RascalInterpreterREPL {
    public NormalREPL() throws IOException {
      super(System.in, System.out, true, true, TerminalFactory.get());
      setMeasureCommandTime(PRINTCOMMANDTIME);
    }

    @Override
    protected Evaluator constructEvaluator(Writer stdout, Writer stderr) {
      return getDefaultEvaluator(new PrintWriter(stdout), new PrintWriter(stderr));
    }
  }

	private static Evaluator getDefaultEvaluator(PrintWriter stdout, PrintWriter stderr) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		Evaluator evaluator = new Evaluator(vf, stderr, stdout, root, heap);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		evaluator.setMonitor(new ConsoleRascalMonitor());
		return evaluator;
	}

	private static void printVersionNumber(){
		try {
			Enumeration<URL> resources = RascalShell.class.getClassLoader().getResources(META_INF_MANIFEST_MF);
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				String bundleName = manifest.getMainAttributes().getValue("Bundle-Name");
				if (bundleName != null && bundleName.equals("rascal-shell")) {
					String result = manifest.getMainAttributes().getValue("Bundle-Version");
					if (result != null) {
						System.out.println("Version: " + result);
						return;
					}
				}
			}
		} catch (IOException E) {
		}
		System.out.println("Version: unknown");
	}
	

	private static void runManifest(RascalManifest mf, String[] args) {
	  assert mf.hasManifest(RascalShell.class);

	  Evaluator eval = getDefaultEvaluator(new PrintWriter(System.out), new PrintWriter(System.err));
	  addExtraSourceFolders(mf, eval);
	  IRascalMonitor monitor = new NullRascalMonitor();

	  String module = mf.getMainModule(RascalShell.class);
	  assert module != null;
	  eval.doImport(monitor, module);

	  String main = mf.getMainFunction(RascalShell.class);
	  main = main != null ? main : RascalManifest.DEFAULT_MAIN_FUNCTION;

	  try {
	    IValue v = eval.main(monitor, module, main, args);

	    if (v.getType().isInteger()) {
	      System.exit(((IInteger) v).intValue());
	    } else {
	      System.out.println(v);
	      System.exit(0);
	    }
	  } catch (CommandlineError e) {
	    System.err.println(e.getMessage());
	    System.err.println(e.help("java -jar ..."));
	  }
	}

	private static void addExtraSourceFolders(RascalManifest mf, Evaluator eval) {
	  assert mf.hasManifest(RascalShell.class);
	  List<String> roots = mf.getSourceRoots(RascalShell.class);

	  int count = 0;
	  for (String root : roots) {
	    String scheme = "root" + count;
	    URIResolverRegistry.getInstance().registerInput(new ClassResourceInput(scheme, RascalShell.class, "/" + root));
	    eval.addRascalSearchPath(URIUtil.rootLocation(scheme)); 
	  }
	}

	private static void runModule(String args[]) {
		String module = args[0];
		if (module.endsWith(".rsc")) {
			module = module.substring(0, module.length() - 4);
		}
		module = module.replaceAll("/", "::");
	  Evaluator eval = getDefaultEvaluator(new PrintWriter(System.out), new PrintWriter(System.err));

	  eval.doImport(null, module);
	  String[] realArgs = new String[args.length - 1];
	  System.arraycopy(args, 1, realArgs, 0, args.length - 1);

	  IValue v = eval.main(null, module, "main", realArgs);

	  if (v != null) {
	    System.out.println(v.toString());
	  }
	  System.exit(0);
	  return;
	}

  public static void main(String[] args) throws IOException {
    printVersionNumber();
    RascalManifest mf = new RascalManifest();
    if (mf.hasManifest(RascalShell.class) && mf.hasMainModule(RascalShell.class)) {
      runManifest(mf, args); 
    } 
    else if (args.length == 0) {
      try {
        new NormalREPL().run();
        System.exit(0);
      } 
      catch (Throwable e) {
        System.err.println("unexpected error: " + e.getMessage());
        System.exit(1);
      }
    } 
    else {
      runModule(args);
    }
  }
}
