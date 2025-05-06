package org.rascalmpl.shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.jar.Manifest;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Messages;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.repl.rascal.RascalInterpreterREPL;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ShellEvaluatorFactory {

    public static Evaluator getEvaluatorForMain(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor) {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, monitor);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

        return evaluator;
    }

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

    public static Evaluator getDefaultEvaluatorForPathConfig(PathConfig pcfg, Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services, RascalInterpreterREPL repl, boolean isRascal) {
        stdout.println("CALLED getDefaultEvaluatorForPathConfig!");
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
        IValueFactory vf = ValueFactoryFactory.getValueFactory();
        Evaluator evaluator = new Evaluator(vf, input, stderr, stdout, root, heap, services);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
        
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        reg.registerLogical(new ProjectURIResolver(services));
        //similarly for target:///

        stdout.println("Rascal " + RascalManifest.getRascalVersionNumber());
        
        //Add rascal-lsp to `pcfg` if it is present on the class path
        ISourceLocation lspJar;
        try {
            lspJar = PathConfig.resolveProjectOnClasspath("rascal-lsp");
            // the interpreter must find the Rascal sources of util::LanguageServer etc.
            pcfg = pcfg.addSourceLoc(JarURIResolver.jarify(lspJar));
            // the interpreter must load the Java parts for calling util::IDEServices and registerLanguage
            pcfg = pcfg.addLibLoc(lspJar);
            stdout.println("Rascal-lsp " + getVersionFromJarManifest(lspJar));
        } catch (IOException e) {
            // carry on
        }

        stdout.println("Rascal Search path: ");
        for (IValue srcPath : pcfg.getSrcs()) {
            ISourceLocation path = (ISourceLocation)srcPath;
            stdout.println("- " + path);
            evaluator.addRascalSearchPath(path);
            try {
                reg.watch(path, true, d -> repl.sourceLocationChanged(path, d));
            }
            catch (IOException e) {
                e.printStackTrace(stderr);
            }
        }

        var libs = (isRascal ? pcfg.getLibs() : pcfg.getLibsAndTarget());
        stdout.println("Rascal Class Loader path: ");
        for (IValue entry: libs) {
            stdout.println("- " + entry);
        }
        evaluator.addClassLoader(new SourceLocationClassLoader(libs, ClassLoader.getSystemClassLoader()));

        if (!pcfg.getMessages().isEmpty()) {
            stdout.println("PathConfig messages:");
            Messages.write(pcfg.getMessages(), stdout);
            services.registerDiagnostics(pcfg.getMessages());
        }

        return evaluator;
    }

    private static String getVersionFromJarManifest(ISourceLocation jar) {
        try {
            return new Manifest(URIResolverRegistry.getInstance()
                .getInputStream(URIUtil.getChildLocation(jar, "META-INF/MANIFEST.MF")))
                .getMainAttributes().getValue("Specification-Version");
        } catch (IOException e) {
            return "Unknown";
        }
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
