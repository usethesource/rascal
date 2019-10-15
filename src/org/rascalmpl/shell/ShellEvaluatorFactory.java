package org.rascalmpl.shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellEvaluatorFactory {

  public static Evaluator getDefaultEvaluator(PrintWriter stdout, PrintWriter stderr) {
    GlobalEnvironment heap = new GlobalEnvironment();
    ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
    IValueFactory vf = ValueFactoryFactory.getValueFactory();
    Evaluator evaluator = new Evaluator(vf, stderr, stdout, root, heap);
    evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
    evaluator.setMonitor(new ConsoleRascalMonitor());
    URIResolverRegistry reg = URIResolverRegistry.getInstance();
    
    if (!reg.getRegisteredInputSchemes().contains("project") && !reg.getRegisteredLogicalSchemes().contains("project")) {
        ISourceLocation rootFolder = inferProjectRoot(new File(System.getProperty("user.dir")));
        if (rootFolder != null) {
            configureProjectEvaluator(evaluator, rootFolder);
        }
    }
    
    return evaluator;
  }

  private static void configureProjectEvaluator(Evaluator evaluator, ISourceLocation projectRoot) {
      URIResolverRegistry reg = URIResolverRegistry.getInstance();

      String projectName = new RascalManifest().getProjectName(projectRoot);
      reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
      
      try {
          PathConfig pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot);
          
          for (IValue path : pcfg.getSrcs()) {
              evaluator.addRascalSearchPath((ISourceLocation) path); 
          }
          
          ClassLoader cl = new SourceLocationClassLoader(pcfg.getClassloaders(), ShellEvaluatorFactory.class.getClassLoader());
          evaluator.addClassLoader(cl);
      }
      catch (IOException e) {
          System.err.println(e);
      }
  }
  
  private static ISourceLocation inferProjectRoot(File cwd) {
      try {
          File current = cwd;
          while (current != null && current.exists() && current.isDirectory()) {
              if (new File(current, "META-INF/RASCAL.MF").exists()) {
                  return URIUtil.createFileLocation(current.getAbsolutePath());
              }
              current = current.getParentFile();
          }
      }
      catch (URISyntaxException e) {
          return null;
      }
      
      return null;
  }
}
