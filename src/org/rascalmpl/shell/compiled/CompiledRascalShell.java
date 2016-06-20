package org.rascalmpl.shell.compiled;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.experiments.Compiler.Commands.CommandOptions;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.ManifestRunner;
import org.rascalmpl.shell.ModuleRunner;
import org.rascalmpl.shell.ShellRunner;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;


public class CompiledRascalShell  {

  private static void printVersionNumber(){
    try {
      Enumeration<URL> resources = CompiledRascalShell.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
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


  public static void main(String[] args) throws IOException {

	IValueFactory vf = ValueFactoryFactory.getValueFactory();
	CommandOptions cmdOpts = new CommandOptions("CompiledRascalShell");
	try {
		cmdOpts
		.locsOption("src").locsDefault(cmdOpts.getDefaultStdlocs().isEmpty() ? vf.list(cmdOpts.getDefaultStdlocs()) : cmdOpts.getDefaultStdlocs())
		.help("Add (absolute!) source location, use multiple --src arguments for multiple locations")

		.locOption("bin").locDefault(vf.sourceLocation("home", "", "bin"))
		.help("Directory for Rascal binaries")
		
		.locsOption("lib").locsDefault((co) -> vf.list(co.getCommandLocOption("bin")))
		.help("Add new lib location, use multiple --lib arguments for multiple locations")

		.locOption("boot").locDefault(cmdOpts.getDefaultBootLocation())
		.help("Rascal boot directory")

		.boolOption("help")
		.help("Print help message for this command")
		.noModuleArgument()
		.handleArgs(args);
		
	} catch (URISyntaxException e1) {
		e1.printStackTrace();
		System.exit(1);
	}  

	printVersionNumber();
    RascalManifest mf = new RascalManifest();
    try {
      ShellRunner runner; 
      if (mf.hasManifest(CompiledRascalShell.class) && mf.hasMainModule(CompiledRascalShell.class)) {
        runner = new ManifestRunner(mf, new PrintWriter(System.out), new PrintWriter(System.err));
      } 
      else if (args.length > 0) {
        runner = new ModuleRunner(new PrintWriter(System.out), new PrintWriter(System.err));
      } 
      else {
        runner = new CompiledREPLRunner(cmdOpts.getPathConfig(), System.in, System.out);
      }
      runner.run(args);

      System.exit(0);
    }
    catch (Throwable e) {
      System.err.println("\n\nunexpected error: " + e.getMessage());
      e.printStackTrace(System.err);
      System.exit(1);
    }
    finally {
      System.out.flush();
      System.err.flush();
    }
  }
}
