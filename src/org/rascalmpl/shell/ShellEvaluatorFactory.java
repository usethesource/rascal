package org.rascalmpl.shell;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Messages;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellEvaluatorFactory {

    public static Evaluator getDefaultEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        if (!reg.getRegisteredInputSchemes().contains("project") && !reg.getRegisteredLogicalSchemes().contains("project")) {
            ISourceLocation rootFolder = PathConfig.inferProjectRoot(URIUtil.rootLocation("cwd"));
            if (rootFolder != null) {
                configureProjectEvaluator(evaluator, rootFolder);
            }
        }

        return evaluator;
    }

    public static Evaluator getDefaultEvaluatorForLocation(File fileOrFolderInProject, Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        try {
            ISourceLocation rootFolder = PathConfig.inferProjectRoot(URIUtil.createFileLocation(fileOrFolderInProject.toString()));
            if (rootFolder != null) {
                configureProjectEvaluator(evaluator, rootFolder);
            }
        }
        catch (URISyntaxException e) {
            e.printStackTrace(stderr);
        }

        return evaluator;
    }

    public static void configureProjectEvaluator(Evaluator evaluator, ISourceLocation projectRoot) {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        String projectName = new RascalManifest().getProjectName(projectRoot);
        reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
        reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

        PathConfig pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot, RascalConfigMode.INTERPRETER, true);

        for (IValue path : pcfg.getSrcs()) {
            evaluator.addRascalSearchPath((ISourceLocation) path); 
        }

        ClassLoader cl = new SourceLocationClassLoader(pcfg.getLibsAndTarget(), ShellEvaluatorFactory.class.getClassLoader());
        evaluator.addClassLoader(cl);  
        
        Messages.write(pcfg.getMessages(), pcfg.getSrcs(), evaluator.getOutPrinter());
    }
}
