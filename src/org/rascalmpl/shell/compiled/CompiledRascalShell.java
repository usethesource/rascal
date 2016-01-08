package org.rascalmpl.shell.compiled;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.shell.ManifestRunner;
import org.rascalmpl.shell.ModuleRunner;
import org.rascalmpl.shell.ShellRunner;


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
        runner = new CompiledREPLRunner(System.in, System.out, new PathConfig());
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
