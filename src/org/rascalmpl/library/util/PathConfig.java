package org.rascalmpl.library.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.jline.utils.OSUtils;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.StandardLibraryURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.util.maven.Artifact;
import org.rascalmpl.util.maven.ArtifactCoordinate;
import org.rascalmpl.util.maven.MavenMessages;
import org.rascalmpl.util.maven.MavenParser;
import org.rascalmpl.util.maven.ModelResolutionError;
import org.rascalmpl.util.maven.Scope;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class PathConfig {
    private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
    private static final TypeFactory tf = TypeFactory.getInstance();
    private static final TypeStore store = new TypeStore();
    
    // WARNING: these definitions must reflect the definitions in `util::PathConfig`
    public static final Type PathConfigType = tf.abstractDataType(store, "PathConfig"); 
    public static final Map<String, Type> PathConfigFields = Map.of(
        "projectRoot", tf.sourceLocationType(),
        "srcs", tf.listType(tf.sourceLocationType()),
        "ignores", tf.listType(tf.sourceLocationType()),
        "bin", tf.sourceLocationType(),
        "resources", tf.listType(tf.sourceLocationType()),
        "libs", tf.listType(tf.sourceLocationType()),
        "messages", tf.listType(Messages.Message),
        "generatedSources", tf.sourceLocationType() // deprecated!
    );
    private static final Type pathConfigConstructor = tf.constructor(store, PathConfigType, "pathConfig");

    static {
        store.extendStore(Messages.ts);
        PathConfigFields.forEach((n, t) -> {
            store.declareKeywordParameter(pathConfigConstructor, n, t);
        });
    }
    
    private final ISourceLocation projectRoot;
    private final List<ISourceLocation> srcs;		
    private final List<ISourceLocation> libs;     
    private final ISourceLocation bin;  
    private final List<ISourceLocation> ignores; 	
    private final List<ISourceLocation> resources; 
    private final List<IConstructor> messages;     
    

    // defaults are shared here because they occur in different use places.
    private static final ISourceLocation defaultProjectRoot = URIUtil.unknownLocation();
    private static final List<ISourceLocation> defaultIgnores = Collections.emptyList();
    private static final List<ISourceLocation> defaultResources = Collections.emptyList();
    private static final List<IConstructor> defaultMessages = Collections.emptyList();
    private static final ISourceLocation defaultBin = URIUtil.unknownLocation();
    private static final List<ISourceLocation> defaultLibs = Collections.emptyList();
    
    public static enum RascalConfigMode {
        INTERPRETER,
        COMPILER
    }
    
    public PathConfig() {
        this(defaultProjectRoot);
    }

    public PathConfig(ISourceLocation projectRoot) { 
        this.projectRoot = projectRoot;
        srcs = Collections.emptyList();
        ignores = defaultIgnores;
        bin = defaultBin;
        libs = Collections.emptyList();
        resources = defaultResources;
        messages = defaultMessages;
    }

    public PathConfig(IConstructor pcfg) throws IOException {
        this(
            projectRoot(pcfg),
            srcs(pcfg), 
            libs(pcfg), 
            bin(pcfg), 
            ignores(pcfg), 
            resources(pcfg),
            messages(pcfg)
        );
    }

    /**
     * For rebuilding a changed PathConfig
     */
    private PathConfig(ISourceLocation projectRoot, List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> ignores, List<ISourceLocation> resources, List<IConstructor> messages) {
        this.projectRoot = projectRoot;
        this.srcs = srcs;
        this.ignores = ignores;
        this.bin = bin;
        this.libs = ignores;
        this.resources = resources;
        this.messages = messages;
    }

    /**
     * For rebuilding a changed PathConfig
     */
    private PathConfig(ISourceLocation projectRoot, IList srcs, IList libs, ISourceLocation bin, IList ignores, IList resources, IList messages) {
        this.projectRoot = projectRoot;
        this.srcs = srcs.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());
        this.ignores = ignores.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());
        this.bin = bin;
        this.libs = libs.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());
        this.resources = resources.stream().map(ISourceLocation.class::cast).collect(Collectors.toList());;
        this.messages = messages.stream().map(IConstructor.class::cast).collect(Collectors.toList());;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectRoot, srcs, libs, bin, ignores, resources, messages);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PathConfig)) {
            return false;
        }
        PathConfig other = (PathConfig) obj;
        return Objects.equals(projectRoot, other.projectRoot)
            && Objects.equals(srcs, other.srcs)
            && Objects.equals(ignores, other.ignores)
            && Objects.equals(bin, other.bin)
            && Objects.equals(libs, other.libs)
            && Objects.equals(resources, other.resources)
            && Objects.equals(messages, other.messages);
    }

    private static IList messages(IConstructor pcfg) {
        return getListValueFromConstructor(pcfg, defaultMessages, "messages");
    }

    private static IList resources(IConstructor pcfg) {
        IList val = (IList) pcfg.asWithKeywordParameters().getParameter("resources");
        return val == null ? defaultResources.stream().collect(vf.listWriter()) : val;
    }

    private static IList ignores(IConstructor pcfg) {
        return getListValueFromConstructor(pcfg, defaultIgnores, "ignores");
    }

    private static IList getListValueFromConstructor(IConstructor pcfg, List<? extends IValue> def, String label) {
        IList val = (IList) pcfg.asWithKeywordParameters().getParameter(label);
        return val == null ? def.stream().collect(vf.listWriter()) : val;
    }

    private static ISourceLocation bin(IConstructor pcfg) {
        ISourceLocation val = (ISourceLocation) pcfg.asWithKeywordParameters().getParameter("bin");
        return val == null ? defaultBin : val;
    }

    private static IList libs(IConstructor pcfg) {
        return getListValueFromConstructor(pcfg, defaultLibs, "libs");
    }

    private static IList srcs(IConstructor pcfg) {
        return getListValueFromConstructor(pcfg, Collections.emptyList(), "srcs");
    }

    private static ISourceLocation projectRoot(IConstructor pcfg) {
        ISourceLocation val = (ISourceLocation) pcfg.asWithKeywordParameters().getParameter("projectRoot");
        return val == null ? defaultProjectRoot : val;
    }
    
    String makeFileName(String qualifiedModuleName) {
        return makeFileName(qualifiedModuleName, "rsc");
    }
    
    public static ISourceLocation getDefaultBin(){
        return defaultBin;
    }
    
    public static IList getDefaultResources() {
        return defaultResources.stream().collect(vf.listWriter());
    }

    public static IList getDefaultIgnoresList() {
        return convertLocs(defaultIgnores);
    }
    
    private static IList convertLocs(List<ISourceLocation> locs) {
        IListWriter w = vf.listWriter();
        w.appendAll(locs);
        return w.done();
    }

    public static List<ISourceLocation> getDefaultIgnores(){
        return  Collections.unmodifiableList(defaultIgnores);
    }
    
    public IValueFactory getValueFactory() {
        return vf;
    }
    
    public IList getSrcs() {
        return srcs.stream().collect(vf.listWriter());
    }
    
    public IList getResources() {
        return resources.stream().collect(vf.listWriter());
    }
    
    public IList getMessages() {
        return messages.stream().collect(vf.listWriter());
    }
    
    public PathConfig addSourceLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedsrcs = new ArrayList<ISourceLocation>(srcs);
        extendedsrcs.add(dir);
        return new PathConfig(projectRoot, extendedsrcs, libs, bin, ignores, resources, messages);
    }
    
    public PathConfig addResource(ISourceLocation loc) {
        List<ISourceLocation> extendedResources = new ArrayList<ISourceLocation>(resources);
        extendedResources.add(loc);

        return new PathConfig(projectRoot, srcs, libs, bin, ignores, extendedResources, messages);
    }

    public PathConfig setResources(List<ISourceLocation> resources) throws IOException {
        return new PathConfig(projectRoot, srcs, libs, bin, ignores, resources, messages);
    }
    
    public IList getIgnores() {
        return ignores.stream().collect(vf.listWriter());
    }
    
    public PathConfig addIgnoreLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedignores = new ArrayList<ISourceLocation>(ignores);
        extendedignores.add(dir);
        return new PathConfig(projectRoot, srcs, libs, bin, extendedignores, resources, messages);
    }
    
    public IList getLibs() {
        return libs.stream().collect(vf.listWriter());
    }

    public IList getLibsAndTarget() {
        return getLibs().append(getBin());
    }
    
    public PathConfig addLibLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedlibs = new ArrayList<ISourceLocation>(libs);
        extendedlibs.add(dir);
        return new PathConfig(projectRoot, srcs, extendedlibs, bin, ignores, resources, messages);
    }

    public PathConfig setBin(ISourceLocation bin) {
        return new PathConfig(projectRoot, srcs, libs, bin, ignores, resources, messages);
    }
    
    /**
     * This will create a PathConfig by learning from the MANIFEST/RASCAL.MF file where the sources
     * are, which libraries to reference and which classpath entries to add. If this PathConfig is
     * for the interpreter it adds more folders to the source path than if its for the compiler.
     * 
     * If library dependencies exist for open projects in the same IDE, via the pom artifactId == libName and project://libName
     * correspondence, then the target and source folders of the projects are added rather then
     * the jar files. For compiler configs this works differently than for interpreter configs.
     * The latter adds source folders to the sources while the former adds target folders to the libraries.
     * 
     * @param manifest the source location of the folder which contains MANIFEST/RASCAL.MF.
     * @return
     */
    public static PathConfig fromSourceProjectMemberRascalManifest(ISourceLocation projectMember, RascalConfigMode mode) {
        if (!URIResolverRegistry.getInstance().isDirectory(projectMember)) {
            projectMember = URIUtil.getParentLocation(projectMember);
        }
        return fromSourceProjectRascalManifest(inferProjectRoot(projectMember), mode, true);
    }

    /*
     * Sometimes we need access to a library that is already on the classpath, for example this could 
     * be rascal-<version>.jar or typepal.jar or rascal-core or rascal-lsp. The IDE or current runtime 
     * environment of Rascal has provided these dependencies while booting up the JVM using the `-cp` 
     * parameter.
     * 
     * This function searches for the corresponding jar file by looking for instances of RASCAL.MF files 
     * with their Project-Name property set to the parameter `projectName`. Then it uses the actual URL
     * of the location of the RASCAL.MF file (inside jar or a target folder) to derive the root of the
     * given project.
     * 
     * Benefit: After this resolution code has executed, the resulting explicit and transparant jar location is 
     * useful as an entry in PathConfig instances. 
     * 
     * Pitfall: Note however that the current JVM instance can never escape
     * from loading classes from the rascal.jar that was given on its classpath.
     */
    public static ISourceLocation resolveProjectOnClasspath(String projectName) throws IOException {
        RascalManifest mf = new RascalManifest();
        Enumeration<URL> mfs = PathConfig.class.getClassLoader().getResources(RascalManifest.META_INF_RASCAL_MF);

        for (URL url : Collections.list(mfs)) {
            try {
                String libName = mf.getProjectName(url.openStream());
                
                if (libName != null && libName.equals(projectName)) {
                    ISourceLocation loc;

                    if (url.getProtocol().equals("jar") && url.getPath().startsWith("file:/")) {
                        // these are the weird jar URLs we get from `getResources` sometimes. We use the URL
                        // parser to make sense of it and then convert it to an ISourceLocation
                        loc = vf.sourceLocation("file", null, URIUtil.fromURL(new URL(url.getPath())).getPath());

                          // unjarify the path
                          loc = URIUtil.changePath(loc, loc.getPath().replace("!/" + RascalManifest.META_INF_RASCAL_MF, ""));
                    }
                    else {
                        // this is typically a target folder
                        loc = vf.sourceLocation(URIUtil.fromURL(url));
                        loc = URIUtil.getParentLocation(URIUtil.getParentLocation(loc));
                    }
     
                    // can not mavenize here, that would cause a circular static initializer race.
                    return loc;
                }
            }
            catch (IOException | URISyntaxException e) {
                throw new FileNotFoundException(e.getMessage());
            }
        }

        throw new FileNotFoundException(projectName + " jar could not be located in the current runtime classpath");
    }

    public static ISourceLocation resolveCurrentRascalRuntimeJar() throws IOException {
        return resolveProjectOnClasspath("rascal");
    }

    public static ISourceLocation inferProjectRoot(Class<?> clazz) throws IOException {
        var url = clazz.getProtectionDomain().getCodeSource().getLocation();
        
        if (url.getProtocol().equals("file")) {
            try {
                return inferProjectRoot(vf.sourceLocation(URIUtil.fromURL(url)));
            }
            catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }
        else {
            throw new FileNotFoundException();
        }
    }

    public static ISourceLocation inferProjectRoot(ISourceLocation member) {
        ISourceLocation current = member;
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        while (current != null && reg.exists(current) && reg.isDirectory(current)) {
            if (reg.exists(URIUtil.getChildLocation(current, "META-INF/RASCAL.MF"))) {
                return current;
            }

            if (URIUtil.getParentLocation(current).equals(current)) {
                // we went all the way up to the root
                return reg.isDirectory(member) ? member : URIUtil.getParentLocation(member);
            }
            
            current = URIUtil.getParentLocation(current);
        }

        return current;
    }

    public static PathConfig parse(String pathConfigString) throws IOException {
        try {
            IConstructor cons = (IConstructor) new StandardTextReader().read(vf, store, PathConfigType, new StringReader(pathConfigString));
            IWithKeywordParameters<?> kwp = cons.asWithKeywordParameters();

            ISourceLocation projectRoot = (ISourceLocation) kwp.getParameter("projectRoot");
            IList srcs = (IList) kwp.getParameter("srcs");
            IList libs =  (IList) kwp.getParameter("libs");
            IList ignores = (IList) kwp.getParameter("ignores");
            IList resources = (IList) kwp.getParameter("resources");
            IList messages = (IList) kwp.getParameter("messages");

            ISourceLocation bin = (ISourceLocation) kwp.getParameter("bin");

            return new PathConfig(
                projectRoot != null ? projectRoot : defaultProjectRoot,
                srcs != null ? srcs : vf.list(), 
                libs != null ? libs : vf.list(),
                bin != null ? bin : defaultBin,
                ignores != null ? ignores : vf.list(),
                resources != null ? resources : vf.list(),
                messages != null ? messages : vf.list()
            );
        } 
        catch (FactTypeUseException e) {
            throw new IOException(e);
        }
    }

    /**
     * Configure paths for the rascal project itself, so if someone has rascal open in their IDE for example, or is starting a REPL for rascal
     */
    private static void buildRascalSelfApplicationConfig(ISourceLocation workspaceRascal, RascalConfigMode mode, List<Artifact> mavenClassPath, IListWriter srcs, IListWriter libs, IListWriter messages) throws IOException {
        if (mode == RascalConfigMode.INTERPRETER) {
            // if you want to test rascal changes, use RascalShell class and run it as a java process
            srcs.append(URIUtil.rootLocation("std"));
            
            messages.append(Messages.info("Bootstrap |std:///| = " + StandardLibraryURIResolver.getDebugBootstrapLocation(), workspaceRascal));
            // add our own jar to the lib path to make sure rascal classes are found 
            var runtime = resolveCurrentRascalRuntimeJar();
            libs.append(runtime);
            messages.append(Messages.info("Bootstrap runtime   = " + runtime, workspaceRascal));
        }
        else {
            // we want to help rascal devs work on rascal to at least get type-check errors, so if we're in compile mode, you get the source path
            // but otherwise, you alway get the `std:///` in your repl
            srcs.append(URIUtil.getChildLocation(workspaceRascal, "src/org/rascalmpl/library"));
        }

        // compiler & tutor only paths
        srcs.append(URIUtil.getChildLocation(workspaceRascal, "src/org/rascalmpl/compiler"));
        srcs.append(URIUtil.getChildLocation(workspaceRascal, "src/org/rascalmpl/tutor"));
        // test paths
        srcs.append(URIUtil.getChildLocation(workspaceRascal, "test/org/rascalmpl/test/data"));
        srcs.append(URIUtil.getChildLocation(workspaceRascal, "test/org/rascalmpl/benchmark"));

        // now figure out where we can get typepal from
        var reg = URIResolverRegistry.getInstance();
        var typepal = URIUtil.correctLocation("project", "typepal","src");
        if (reg.exists(typepal)) {
            // make sure to resolve the typepal location
            // such that in VS Code the full (local) path names are visible
            typepal = reg.logicalToPhysical(typepal);
        }
        else {
            // we might not be in a VS Code project, but instead running inside of the maven/junit plugin
            // so now we have to look at our class path to find typepal
            try {
                typepal = resolveProjectOnClasspath("typepal");
                typepal = MavenRepositoryURIResolver.mavenize(typepal);
                typepal = JarURIResolver.jarify(typepal);
            } 
            catch (FileNotFoundException e) {
                // last try: the dependencies. This should typically succeed while we have a pom dependency on typepal in the rascal project.
                typepal = getPomXmlTypePalDependency(mavenClassPath);
            }
        }

        // the interpreter should pick up the typepal sources
        // and so should the typechecker, otherwise it might get type-checked against the wrong `std:///` jar (namely from it's pom.xml)
        if (typepal !=  null) {
            srcs.append(typepal);
        } else {
            messages.append(Messages.error("Could not find typepal in local project, or on the classpath, or amongst the dependencies. Loading the compiler will fail.", workspaceRascal));
        }
    }

    /**
     * Configure paths for the rascal-lsp project when it's open in the IDE (the runtime inside rascal-lsp is not configured here)
     */
    private static void buildRascalLSPConfig(ISourceLocation manifestRoot, RascalConfigMode mode, List<Artifact> mavenClasspath, IListWriter srcs, IListWriter libs, IListWriter messages) throws IOException {
        var insideRascalJar = JarURIResolver.jarify(resolveCurrentRascalRuntimeJar());
        var rascalCompiler = URIUtil.getChildLocation(insideRascalJar, "org/rascalmpl/compiler");
        var typepal = URIUtil.getChildLocation(insideRascalJar, "org/rascalmpl/typepal");

        if (mode == RascalConfigMode.INTERPRETER) {
            // we're building a repl for the rascal-lsp project
            // so this is a rascal-lsp developer working on code in rascal-lsp
            // most stuff flows from the
            srcs.append(URIUtil.rootLocation("std"));
            srcs.append(rascalCompiler);
            srcs.append(typepal);
        }
        else {
            libs.append(JarURIResolver.jarify(resolveCurrentRascalRuntimeJar()));
            // while it's tempting to see if the rascal project is there, and we might be able to get the rascal compiler tpls from the target folder.
            // as long as we're hard wiring the rascal compler to follow the runtime
            // we have to let the type-checker for rascal-lsp re-type-check rascal compiler
            // this allows developers in rascal-lsp to get typechecking for parts even if they import rascal compiler/typepal modules
            srcs.append(rascalCompiler);
            srcs.append(typepal);
        }
        libs.append(resolveCurrentRascalRuntimeJar()); // add our own jar to the lib path to make sure rascal classes are found

        translateSources(manifestRoot, srcs, messages);

        for (var art: mavenClasspath) {
            var artId = art.getCoordinate().getArtifactId();
            if (!artId.equals("rascal") && !artId.equals("typepal")) {
                addArtifactToPathConfig(art, manifestRoot, mode, srcs, libs, messages);
            }
        }

    }

    private static void translateSources(ISourceLocation manifestRoot, IListWriter srcs, IListWriter messages) {
        var manifest = new RascalManifest();
        var reg = URIResolverRegistry.getInstance();
        for (String srcName : manifest.getSourceRoots(manifestRoot)) {
            var srcFolder = URIUtil.getChildLocation(manifestRoot, srcName);
            
            if (!reg.exists(srcFolder) || !reg.isDirectory(srcFolder)) {
                messages.append(Messages.error("Source folder " + srcFolder + " does not exist.", getRascalMfLocation(manifestRoot)));
            }

            srcs.append(srcFolder);
        }
    }

    private static void buildNormalProjectConfig(ISourceLocation manifestRoot, RascalConfigMode mode, List<Artifact> mavenClasspath, boolean isRoot, IListWriter srcs, IListWriter libs, IListWriter messages) throws IOException, URISyntaxException {
        if (isRoot) {
            if (mode == RascalConfigMode.INTERPRETER) {
                srcs.append(URIUtil.rootLocation("std")); // you'll always get rascal from standard in case of interpreter mode
                libs.append(resolveCurrentRascalRuntimeJar()); // add our own jar to the lib path to make sure rascal classes are found
            }
            else {
                assert mode == RascalConfigMode.COMPILER: "should be compiler";
                // untill we go pom.xml first, you'll always get the rascal jar from our runtime
                // not the one you requested in the pom.xml
                libs.append(JarURIResolver.jarify(resolveCurrentRascalRuntimeJar())); 
            }
        }


        // This processes Rascal libraries we can find in maven dependencies,
        // and we add them to the srcs unless a project is open with the same name, then we defer to its srcs
        // to make it easier to edit projects in the IDE
        for (var art : mavenClasspath) {
            addArtifactToPathConfig(art, manifestRoot, mode, srcs, libs, messages);
        }
        if (isRoot || mode == RascalConfigMode.INTERPRETER) {
            // we have to fill our own src folder
            translateSources(manifestRoot, srcs, messages);
        }
        else /* for clarity these conditions hold true: if (!isRoot && mode == RascalConfigMode.COMPILER)*/ {
            // we have to write our own target folder to the lib path of the parent
            if (manifestRoot.getScheme().equals("project")) {
                libs.append(URIUtil.correctLocation("target", manifestRoot.getAuthority(), ""));
            }
            else {
                libs.append(URIUtil.getChildLocation(manifestRoot, "target/classes"));
            }
        }

        // We add rascal-lsp to the PathConfig if it is present on the classpath
        // This version of rascal-lsp is added last, so an explicit rascal-lsp dependency takes precedence
        try {
            var lsp = PathConfig.resolveProjectOnClasspath("rascal-lsp");
            if (mode == RascalConfigMode.INTERPRETER) {
                srcs.append(URIUtil.getChildLocation(JarURIResolver.jarify(lsp), "library"));
            }
            // the interpreter must load the Java parts for calling util::IDEServices and registerLanguage
            addLibraryToLibPath(URIResolverRegistry.getInstance(), libs, mode, lsp);
        }
        catch (IOException e) {
            // This is expected when rascal-lsp is not on the classpath
        }
    }

    private static void addArtifactToPathConfig(Artifact art, ISourceLocation manifestRoot, RascalConfigMode mode, IListWriter srcs,
        IListWriter libs, IListWriter messages) throws IOException {
        RascalManifest manifest = new RascalManifest();
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        try {
            var resolvedLocation = art.getResolved();
            if (resolvedLocation == null) {
                // maven could not resolve it, so lets skip this
                // unless this is a local project (that never got added to m2 repo)
                ISourceLocation projectLoc = URIUtil.correctLocation("project", art.getCoordinate().getArtifactId(), "");
                if (reg.exists(projectLoc)) {
                    if (mode == RascalConfigMode.INTERPRETER) {
                        // Note that this is not the main project pom so art.getOrigin() should not be null
                        messages.append(MavenMessages.info("Redirected: " + art.getCoordinate() + " to: " + projectLoc, art));
                    }
                    addProjectAndItsDependencies(mode, srcs, libs, messages, projectLoc);
                }
                else {
                    messages.append(MavenMessages.error("Declared dependency does not exist: " + art.getCoordinate(), art));
                }
                return;
            }
        
            ISourceLocation dep = MavenRepositoryURIResolver.mavenize(URIUtil.createFileLocation(resolvedLocation));
            String libProjectName = manifest.getManifestProjectName(manifest.manifest(dep));

            if (libProjectName == null || libProjectName.isEmpty()) {
                // this is a jar without Rascal meta-data, we need it for the classpath
                libs.append(dep);
                return;
            }
            if (libProjectName.equals("rascal")) {
                return; 
            }
            if (libProjectName.equals("rascal-lsp")) {
                checkLSPVersionsMatch(manifestRoot, messages, dep, art);
                // we'll be adding the rascal-lsp by hand later
                // so we ignore the rascal-lsp dependency
                return;
            }
            ISourceLocation projectLoc = URIUtil.correctLocation("project", libProjectName, "");

            if (reg.exists(projectLoc)) {
                // The project we depend on is available in the current workspace. 
                // so we configure for using the current state of that project.
                if (mode == RascalConfigMode.INTERPRETER) {
                    messages.append(MavenMessages.info("Redirected: " + art.getCoordinate() + " to: " + projectLoc, art));
                }
                libs.append(URIUtil.correctLocation("target", libProjectName, ""));
                addProjectAndItsDependencies(mode, srcs, libs, messages, projectLoc);
            }
            else {
                // just a pre-installed dependency in the local maven repository
                addLibraryToLibPath(reg, libs, mode, dep);
                if (mode == RascalConfigMode.INTERPRETER) {
                    addLibraryToSourcePath(reg, srcs, messages, dep);
                }
            }
        } catch (URISyntaxException e) {
            messages.append(MavenMessages.error("Could not convert " + art.getCoordinate() + " to a loc: " + e, art));
        } 
    }

    private static void addProjectAndItsDependencies(RascalConfigMode mode, IListWriter srcs, IListWriter libs,
        IListWriter messages, ISourceLocation projectLoc) throws IOException, URISyntaxException {
        projectLoc = safeResolve(projectLoc); // for now, remove the project loc, later we can undo this and keep the project loc around
        var childMavenClasspath = getPomXmlCompilerClasspath(projectLoc, messages);
        buildNormalProjectConfig(projectLoc, mode, childMavenClasspath, false, srcs, libs, messages);
    }

    private static void checkLSPVersionsMatch(ISourceLocation manifestRoot, IListWriter messages, ISourceLocation jarLocation, Artifact artifact) throws IOException {
        // Rascal LSP is special because the VScode extension pre-loads it into the parametric DSL VM.
        // If the version is different, then the debugger may point to the wrong code, and also the Rascal
        // IDE features like "jump-to-definition" could be off.
        try {
            var loadedRascalLsp = resolveProjectOnClasspath("rascal-lsp");
            var reg = URIResolverRegistry.getInstance();
            try (InputStream in = reg.getInputStream(loadedRascalLsp); InputStream in2 = reg.getInputStream(jarLocation)) {
                var version = new Manifest(in).getMainAttributes().getValue("Specification-Version");
                var otherVersion = new Manifest(in2).getMainAttributes().getValue("Specification-Version");

                if (version != null && !version.equals(otherVersion)) {
                    messages.append(MavenMessages.warning("Pom.xml dependency on rascal-lsp has version " + otherVersion + " while the effective version in the VScode extension is " + version + ". This can have funny effects in the IDE while debugging or code browsing, for that reason we've replaced it with the effective one, please update your pom.xml.", artifact));
                }
            }
        }
        catch (FileNotFoundException e) {
            // this is ok. there is not a duplicate presence of rascal-lsp.
        }
    }


    /**
     * This creates a PathConfig instance by learning from the `MANIFEST/RASCAL.MF` file where the sources
     * are, and from `pom.xml` which libraries to reference. If this PathConfig is
     * for the interpreter it adds more folders to the source path than if its for the compiler. 
     * 
     * In the future
     * we'd like the source folders also configured by the pom.xml but for now we read it from RASCAL.MF for
     * the sake of efficiency (reading pom.xml requires an XML parse and DOM traversal.) Also we need
     * some level of backward compatibility until everybody has moved to using pom.xml.
     * 
     * If library dependencies exist for _open_ projects in the same IDE, via the `project://<artifactId>`
     * correspondence with `mvn://<groupId>~<artifactId>~<version>`, then the target and source folders of 
     * those projects are added to the configuration instead of the jar files.
     * For compiler configs this works differently than for interpreter configs.
     * The latter adds source folders to the `srcs` while the former adds target folders to the `libs`.
     * 
     * If the current project is the rascal project itself, then precautions are taken to avoid double
     * entries in libs and srcs, always promoting the current project over the released rascal version.
     * 
     * If the current project depends on a rascal project (e.g. for compiling Java code against Rascal's
     * and vallang's run-time classes), then this dependency is ignored and the current JVM's rascal version
     * is used instead. The literal jar file is put in the pathConfig for transparancy's sake, and a
     * warning is added to the `messages` list.
     * 
     * This code also checks for existence of the actual jar files and source folders that are depended on.
     * If the files or folders do not exist, an an error is added to the messages field.
     * 
     * Clients of this method must promote the messages list to a UI facing log, such as the diagnostics
     * or problems view in an IDE, an error LOG for a CI and stderr or stdout for console applications.
     * 
     * @param manifest the source location of the folder which contains MANIFEST/RASCAL.MF.
     * @param RascalConfigMode.INTERPRETER | RascalConfigMode.COMPILER
     * @return a PathConfig instance, fully informed to start initializing a Rascal compiler or interpreter, and including a list of revelant info, warning and error messages.
     * @throws nothing, because all errors are collected in a messages field of  the PathConfig.
     */
    public static PathConfig fromSourceProjectRascalManifest(ISourceLocation manifestRoot, RascalConfigMode mode, boolean isRoot)  {
        manifestRoot = safeResolve(manifestRoot);
        // once we have proper support for project locs we should do this instead:
        //manifestRoot = upgradeToProjectScheme(manifestRoot, projectName);
        RascalManifest manifest = new RascalManifest();
        IRascalValueFactory vf = IRascalValueFactory.getInstance();
        String projectName = manifest.getProjectName(manifestRoot);
        IListWriter libsWriter = (IListWriter) vf.listWriter().unique();
        IListWriter srcsWriter = (IListWriter) vf.listWriter().unique();
        IListWriter resourcesWriter = (IListWriter) vf.listWriter().unique();
        IListWriter messages = vf.listWriter();
        
        if (isRoot && mode == RascalConfigMode.INTERPRETER) {
            messages.append(Messages.info("Rascal version is " + RascalManifest.getRascalVersionNumber(), getPomXmlLocation(manifestRoot)));
        }

        ISourceLocation target;
        
        if (manifestRoot.getScheme().equals("project")) {
            target = URIUtil.correctLocation("target", projectName, "");
        } 
        else {
            target = URIUtil.getChildLocation(manifestRoot, "target/classes");
        }

        try {
            var mavenClasspath = getPomXmlCompilerClasspath(manifestRoot, messages);

            if (projectName.equals("rascal")) {
                messages.append(Messages.info("Detected Rascal project self-application", getPomXmlLocation(manifestRoot)));
                buildRascalSelfApplicationConfig(manifestRoot, mode, mavenClasspath, srcsWriter, libsWriter, messages);
            }
            else if (projectName.equals("rascal-lsp")) {
                buildRascalLSPConfig(manifestRoot, mode, mavenClasspath, srcsWriter, libsWriter, messages);
            }
            else {
                buildNormalProjectConfig(manifestRoot, mode, mavenClasspath, isRoot, srcsWriter, libsWriter, messages);
            }
        }
        catch (IOException | URISyntaxException e) {
            messages.append(Messages.warning(e.getMessage(), getPomXmlLocation(manifestRoot)));
        }

        if (!projectName.isEmpty()) {
            validateProjectName(manifestRoot, projectName, messages);
        }
        
        return new PathConfig(
                manifestRoot,
                srcsWriter.done(), 
                libsWriter.done(), 
                target, 
                vf.list(),
                resourcesWriter.done(), 
                messages.done()
        );
    }

    // right now cannot be called, since we have to wait for VS Code to be able to mix these project and file locs
    @SuppressWarnings("unused")
    private static ISourceLocation upgradeToProjectScheme(ISourceLocation loc, String projectName) {
        if (loc.getScheme().equals("project") || projectName == null || projectName.isBlank()) {
            return loc;
        }
        var projectScheme = URIUtil.correctLocation("project", projectName, "");
        var originalResolved = safeResolve(loc);
        var projectResolved = safeResolve(projectScheme);

        if (semanticallySame(originalResolved, projectResolved)) {
            return projectScheme;
        }
        return loc;

    }

    // TODO: JV look into canonical forms rather than alias resolution to fix this issue
    private static boolean semanticallySame(ISourceLocation a, ISourceLocation b) {
        if (!a.getScheme().equals("file")) {
            return a.equals(b);
        }
        if (!b.getScheme().equals("file")) {
            return false;
        }
        // now we know they're a file path, so we can just look at the path part
        var aPath = a.getPath();
        var bPath = b.getPath();
        if (aPath.endsWith("/")) {
            if (!bPath.endsWith("/")) {
                bPath += "/";
            }
        }
        else if (bPath.endsWith("/")) {
            aPath += "/";
        }
        if (OSUtils.IS_WINDOWS || OSUtils.IS_OSX) {
            // on Windows and Mac we have case-insensitive file systems
            // so to over approximate, we use the caseinsensitive compare
            return aPath.equalsIgnoreCase(bPath);
        }
        return aPath.equals(bPath);
    }

    private static ISourceLocation safeResolve(ISourceLocation loc) {
        try {
            var result = URIResolverRegistry.getInstance().logicalToPhysical(loc);
            if (result != null) {
                return result;
            }
            return loc;
        }
        catch (IOException ignored) {
            return loc;
        }
    }


    private static void validateProjectName(ISourceLocation manifestRoot, String projectName, IListWriter messages) {
        try {
            RascalManifest manifest = new RascalManifest();
            URIResolverRegistry reg = URIResolverRegistry.getInstance();
            ISourceLocation projectLoc = URIUtil.correctLocation("project", projectName, "");

            // we need this to get access to the parent folder, in case of `home:///` or `cwd:///`, etc.
            manifestRoot = reg.logicalToPhysical(manifestRoot);
            
            if (!projectLoc.equals(manifestRoot) && !projectName.equals(URIUtil.getLocationName(manifestRoot))) {
                messages.append(Messages.error("Project-Name in RASCAL.MF (" + projectName + ") should be equal to folder name (" + URIUtil.getLocationName(manifestRoot) + ")", getRascalMfLocation(manifestRoot)));
            }

            try (InputStream mfi = manifest.manifest(manifestRoot)) {
                var reqlibs = new Manifest(mfi).getMainAttributes().getValue("Require-Libraries");
                if (reqlibs != null && !reqlibs.isEmpty()) {
                    messages.append(Messages.info("Require-Libraries in RASCAL.MF are not used anymore. Please use Maven dependencies in pom.xml.", getRascalMfLocation(manifestRoot)));
                }
            }
        }
        catch (IOException e) {
            messages.append(Messages.error(e.getMessage(), getRascalMfLocation(manifestRoot)));
        }
    }

    private static void addLibraryToSourcePath(URIResolverRegistry reg, IListWriter srcsWriter, IListWriter messages, ISourceLocation libLoc) {
        var unpacked = JarURIResolver.jarify(libLoc);

        if (!reg.exists(URIUtil.getChildLocation(unpacked, RascalManifest.META_INF_RASCAL_MF))) {
            // skip all the non Rascal libraries
            return;
        }

        var manifest = new RascalManifest();

        boolean foundSrc = false;

        // For backward compatibility, first check the source roots in the manifest relative to the jar root
        for (String src : manifest.getSourceRoots(libLoc)) {
            ISourceLocation srcLib = URIUtil.getChildLocation(unpacked, src);
            if (reg.exists(srcLib)) {
                srcsWriter.append(srcLib);
                foundSrc = true;
            }
        }

        if (!foundSrc) {
            // if we could not find source roots, we default to the jar root
            srcsWriter.append(libLoc);
        }
    }

    private static void addLibraryToLibPath(URIResolverRegistry reg, IListWriter libsWriter, RascalConfigMode mode, ISourceLocation libLoc) {
        libsWriter.append(libLoc); // for classloading purposes
        if (!libLoc.getScheme().equals("mvn") && !libLoc.getScheme().equals("file+jar") && reg.isDirectory(libLoc)) {
            // a local folder was resolved, for example in a multi-module project
            libsWriter.append(URIUtil.getChildLocation(libLoc, "target/classes"));
        }
        if (mode == RascalConfigMode.COMPILER) {
            // find tpls inside of the jar
            var jarifiedDep = JarURIResolver.jarify(libLoc);
            if (jarifiedDep != libLoc) {
                libsWriter.append(jarifiedDep);
            }
        }
    }

    private static ISourceLocation getRascalMfLocation(ISourceLocation project) {
        return URIUtil.getChildLocation(project, RascalManifest.META_INF_RASCAL_MF);
    }
    
    private static ISourceLocation getPomXmlLocation(ISourceLocation project) {
        try {
            ISourceLocation pomxml = URIUtil.getChildLocation(project, "pom.xml");
            return URIResolverRegistry.getInstance().logicalToPhysical(pomxml);
        }
        catch (IOException e) {
            assert false : e.getMessage();
            return URIUtil.correctLocation("unknown", "", "pom.xml");
        }
    }

    /**
     * See if there is a pom.xml and extract the compile-time classpath from a mvn run
     * if there is such a file.
     * 
     * Note that this method should not filter or enhance the path beyond what is written in the pom.xml
     * and the semantics of compile-time dependencies of Maven.
     * @param manifestRoot
     * @param skipUnresolvedDependencyErrors If true, do not propagate "Could not resolve ..." errors from dependencies.
     * @return
     */
    private static List<Artifact> getPomXmlCompilerClasspath(ISourceLocation manifestRoot, IListWriter messages) {
        try {
            var reg = URIResolverRegistry.getInstance();
            if (!manifestRoot.getScheme().equals("file")) {
                var resolved = reg.logicalToPhysical(manifestRoot);
                if (resolved != null) {
                    if (!resolved.getScheme().equals("file")) {
                        return Collections.emptyList();
                    }
                    manifestRoot = resolved;
                }
            }
            if (!manifestRoot.getPath().endsWith("pom.xml")) {
                manifestRoot = URIUtil.getChildLocation(manifestRoot, "pom.xml");
            }
            var mavenParser = new MavenParser(Path.of(manifestRoot.getURI()));
            var rootProject = mavenParser.parseProject();
            messages.appendAll(rootProject.getMessages());
            var result = rootProject.resolveDependencies(Scope.COMPILE, mavenParser);
            for (var a : result) {
                var errorMsg = String.format("Could not resolve %s", a.getCoordinate().toString());
                // errors of the artifacts downloaded should be propagated as well
                // skip "Could not resolve" errors, since our caller will re-try resolution and re-add the error when necessary
                for (var m : a.getMessages()) {
                    if (!messageStartsWith(m, errorMsg)) {
                        messages.append(m);
                    }
                }
            }
            return result;
        }
        catch (RuntimeException | IOException | ModelResolutionError e) {
            return Collections.emptyList();
        }
    }

    private static boolean messageStartsWith(IValue message, String prefix) {
        if (!(message instanceof IConstructor)) {
            return false;
        }
        var msg = ((IConstructor) message).get("msg");
        if (!(msg instanceof IString)) {
            return false;
        }
        return ((IString) msg).getValue().startsWith(prefix);
    }

    private static boolean isTypePalArtifact(ArtifactCoordinate artifact) {
        return "org.rascalmpl".equals(artifact.getGroupId())
            && "typepal".equals(artifact.getArtifactId());
    }

    private static ISourceLocation getPomXmlTypePalDependency(List<Artifact> pomDependencies) {
        
        for (Artifact artifact : pomDependencies) {
            var coordinate = artifact.getCoordinate();

            if (isTypePalArtifact(coordinate)) {
                return MavenRepositoryURIResolver.make(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), "");
            }
        }

        return null;
    }

    public ISourceLocation getProjectRoot() {
        return projectRoot;
    }

    public ISourceLocation getBin() {
        return bin;
    }
    
    @Deprecated
    String makeFileName(String qualifiedModuleName, String extension) {
        return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
    }
    
    @Deprecated
    public String getModuleName(ISourceLocation moduleLoc) throws IOException{
        String modulePath = moduleLoc.getPath();
        if(!modulePath.endsWith(".rsc")){
            throw new IOException("Not a Rascal source file: " + moduleLoc);
        }

        if (moduleLoc.getScheme().equals("std") || moduleLoc.getScheme().equals("mvn")) {
            return pathToModulename(modulePath, "/");
        }

        for(ISourceLocation dir : srcs){
            if(modulePath.startsWith(dir.getPath()) && moduleLoc.getScheme() == dir.getScheme()){
                return pathToModulename(modulePath, dir.getPath());
            }
        }

        for (ISourceLocation dir : libs) {
            if(modulePath.startsWith(dir.getPath()) && moduleLoc.getScheme() == dir.getScheme()){
                return pathToModulename(modulePath, dir.getPath());
            }
        }

        throw new IOException("No module name found for " + moduleLoc + "\n" + this);

    }

    @Deprecated
    private String pathToModulename(String modulePath, String folder) {
        String moduleName = modulePath.replaceFirst(folder, "").replace(".rsc", "");
        if(moduleName.startsWith("/")){
            moduleName = moduleName.substring(1, moduleName.length());
        }
        return moduleName.replace(Configuration.RASCAL_PATH_SEP, Configuration.RASCAL_MODULE_SEP);
    }

    @Deprecated
    private String moduleToDir(String module) {
        return module.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
    }

    @Deprecated
    private ISourceLocation getFullURI(String path, ISourceLocation dir) throws URISyntaxException {
        return URIUtil.getChildLocation(dir, path);
    }

    @Deprecated
    public List<String> listModuleEntries(String moduleRoot) {
        assert !moduleRoot.endsWith(Configuration.RASCAL_MODULE_SEP);
        final URIResolverRegistry reg = URIResolverRegistry.getInstance();
        try {
            String modulePath = moduleToDir(moduleRoot);
            List<String> result = new ArrayList<>();
            for (ISourceLocation dir : srcs) {
                ISourceLocation full = getFullURI(modulePath, dir);
                if (reg.exists(full)) {
                    try {
                        String[] entries = reg.listEntries(full);
                        if (entries == null) {
                            continue;
                        }
                        for (String module: entries ) {
                            if (module.endsWith(Configuration.RASCAL_FILE_EXT)) {
                                result.add(module.substring(0, module.length() - Configuration.RASCAL_FILE_EXT.length()));
                            }
                            else if (module.indexOf('.') == -1 && reg.isDirectory(getFullURI(module, full))) {
                                // a sub folder path
                                result.add(module + Configuration.RASCAL_MODULE_SEP);
                            }
                        }
                    }
                    catch (IOException e) {
                    }
                }
            }
            if (result.size() > 0) {
                return result;
            }
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    /**
     * Convert PathConfig Java object to pathConfig Rascal constructor for use
     * in Rascal code or for serialization and printing.
     */
    public IConstructor asConstructor() {
        Map<String, IValue> config = new HashMap<>();

        config.put("projectRoot", getProjectRoot());
        config.put("srcs", getSrcs());
        config.put("ignores", getIgnores());
        config.put("bin", getBin());
        config.put("libs", getLibs());
        config.put("resources", getResources());
        config.put("messages", getMessages());

        return vf.constructor(pathConfigConstructor, new IValue[0], config);
    }

    public void reportConfigurationInfo() {
        var pom = URIUtil.getChildLocation(projectRoot, "pom.xml");
        var sep = "\n  - ";

        messages.add(Messages.info("Project root is " + projectRoot, pom));
        messages.add(Messages.info("Bin folder   is " + bin, pom));

        if (!srcs.isEmpty()) {
            messages.add(Messages.info("Source module path is:" + sep
            + srcs.stream()
                .map(Object::toString)
                .collect(Collectors.joining(sep)), pom));
        }
        else {
            messages.add(Messages.info("Source path is empty", pom));
        }

        if (!ignores.isEmpty()) {
            messages.add(Messages.info("Ignored source files or folders are:" + sep
            + ignores.stream()
                .map(Object::toString)
                .collect(Collectors.joining(sep)), pom));
        }
       
        if (!libs.isEmpty()) {
            messages.add(Messages.info("Library module (and classes) path is:" + sep
            + libs.stream()
                .map(Object::toString)
                .collect(Collectors.joining(sep)), pom));
        }
        else {
            messages.add(Messages.info("Library path is empty", pom));
        }

        if (!resources.isEmpty()) {
            messages.add(Messages.info("Additional resources files or folders are:" + sep
            + resources.stream()
                .map(Object::toString)
                .collect(Collectors.joining(sep)), pom));
        }        
    }
    
    /**
     * Overview of the contents of the current configuration for debugging purposes.
     * Not necessarily for end-user UI, although it's better than nothing.
     */
    public String toString(){
      StringWriter w = new StringWriter();
      w.append("Path configuration items:").append("\n")
       .append("projectRoot:").append(getProjectRoot().toString()).append("\n")
       .append("srcs:       ").append(getSrcs().toString()).append("\n")
       .append("ignores:    ").append(getIgnores().toString()).append("\n")
       .append("libs:       ").append(getLibs().toString()).append("\n")
       .append("bin:        ").append(getBin().toString()).append("\n")
       .append("resources:  ").append(getResources().toString()).append("\n")
       .append("messages:   ").append(getMessages().toString()).append("\n")
       ;
       
      return w.toString();
    }
}
