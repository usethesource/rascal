package org.rascalmpl.shell.compiled;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.experiments.Compiler.Commands.CommandOptions;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices;
import org.rascalmpl.shell.EclipseTerminalConnection;
import org.rascalmpl.shell.ManifestRunner;
import org.rascalmpl.shell.RascalShell;
import org.rascalmpl.shell.ShellRunner;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import jline.Terminal;
import jline.TerminalFactory;


public class CompiledRascalShell  {

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

		.locsOption("courses")
		.help("Add new courses location, use multipl --courses arguments for multiple locations")
		
		.locsOption("javaCompilerPath")
		.help("Add new java classpath location, use multiple --javaCompilerPath options for multiple locations")
		
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

//	printVersionNumber();
    RascalManifest mf = new RascalManifest();
    try {
      ShellRunner runner; 
      if (mf.hasManifest(CompiledRascalShell.class) && mf.hasMainModule(CompiledRascalShell.class)) {
        runner = new ManifestRunner(mf, new PrintWriter(System.out), new PrintWriter(System.err));
      } 
      else {
          Terminal term = TerminalFactory.get();
          String sneakyRepl = System.getProperty(RascalShell.ECLIPSE_TERMINAL_CONNECTION_REPL_KEY);
          if (sneakyRepl != null) {
              term = new EclipseTerminalConnection(term, Integer.parseInt(sneakyRepl));
          }
          runner = new CompiledREPLRunner(cmdOpts.getPathConfig(), System.in, System.out, new BasicIDEServices(), term);
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
