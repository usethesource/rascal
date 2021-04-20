package org.rascalmpl.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellEvaluatorFactory {

    public static Evaluator getDefaultEvaluator(InputStream input, OutputStream stdout, OutputStream stderr) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap);
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

    public static Evaluator getDefaultEvaluatorForLocation(File fileOrFolderInProject, InputStream input, OutputStream stdout, OutputStream stderr) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        evaluator.setMonitor(new ConsoleRascalMonitor());

        ISourceLocation rootFolder = inferProjectRoot(fileOrFolderInProject);
        if (rootFolder != null) {
            configureProjectEvaluator(evaluator, rootFolder);
        }

        return evaluator;
    }

    public static void configureProjectEvaluator(Evaluator evaluator, ISourceLocation projectRoot) {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();

        String projectName = new RascalManifest().getProjectName(projectRoot);
        reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
        reg.registerLogical(new TargetURIResolver(projectRoot, projectName));

        try {
            PathConfig pcfg = PathConfig.fromSourceProjectRascalManifest(projectRoot);

            for (IValue path : pcfg.getSrcs()) {
                evaluator.addRascalSearchPath((ISourceLocation) path); 
            }

            // TODO the interpreter still needs to find the source files in the lib jars
            // TODO remove after bootstrap
            for (IValue path : pcfg.getLibs()) {
                evaluator.addRascalSearchPath((ISourceLocation) path);
            }

            ClassLoader cl = new SourceLocationClassLoader(pcfg.getClassloaders(), ShellEvaluatorFactory.class.getClassLoader());
            evaluator.addClassLoader(cl);
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Searchers for META-INF/RASCAL.MF to infer the root of a Rascal source project.
     * If cwd has a parent which contains this META-INF/RASCAL.MF file then the
     * location of this parent is returned. If it is not found, this function returns null.
     * @param cwd
     * @return
     */
    public static ISourceLocation inferProjectRoot(File cwd) {
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
