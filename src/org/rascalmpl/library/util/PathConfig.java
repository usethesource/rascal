package org.rascalmpl.library.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.file.MavenRepositoryURIResolver;
import org.rascalmpl.uri.jar.JarURIResolver;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
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
    private final TypeFactory tf = TypeFactory.getInstance();
    private final TypeStore store = new TypeStore();
    
    // WARNING: these definitions must reflect the definitions in `util::Reflective`
    private final Type PathConfigType = tf.abstractDataType(store, "PathConfig"); 
    private final Type pathConfigConstructor = tf.constructor(store, PathConfigType, "pathConfig");
    
    private final List<ISourceLocation> srcs;		
    private final List<ISourceLocation> libs;     
    private final ISourceLocation bin;  
    private final List<ISourceLocation> ignores; 	
    private final ISourceLocation generatedSources;     
    private final List<IConstructor> messages;     
    

    // defaults are shared here because they occur in different use places.
    private static final List<ISourceLocation> defaultIgnores = Collections.emptyList();
    private static final ISourceLocation defaultGeneratedSources = URIUtil.unknownLocation();
    private static final List<IConstructor> defaultMessages = Collections.emptyList();
    private static final ISourceLocation defaultBin = URIUtil.unknownLocation();
    private static final List<ISourceLocation> defaultLibs = Collections.emptyList();
    
    /** implementation detail of communicating with the `mvn` command */
    private static final String WINDOWS_ROOT_TRUSTSTORE_TYPE_DEFINITION = "-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT";

    public static enum RascalConfigMode {
        INTERPRETER,
        COMPILER
    }
    
    public PathConfig() {
        srcs = Collections.emptyList();
        ignores = defaultIgnores;
        bin = defaultBin;
        libs = Collections.emptyList();
        generatedSources = defaultGeneratedSources;
        messages = defaultMessages;
    }

    public PathConfig(IConstructor pcfg) throws IOException {
        this(
            srcs(pcfg), 
            libs(pcfg), 
            bin(pcfg), 
            ignores(pcfg), 
            generatedSources(pcfg), 
            messages(pcfg)
        );
    }

    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin) {
        this(srcs, libs, bin, defaultIgnores);
    }
    
    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> ignores) {
        this(srcs, libs, bin, ignores, defaultGeneratedSources);
    }
    
    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> ignores, ISourceLocation generatedSources) {
        this(srcs, libs, bin, ignores, generatedSources, defaultMessages);
    }
    
    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> ignores, ISourceLocation generatedSources, List<IConstructor> messages) {
        this.srcs = dedup(srcs);
        this.ignores = dedup(ignores);
        this.libs = dedup(libs);
        this.bin = bin;
        this.generatedSources = generatedSources;
        this.messages = messages;
    }
    
    public PathConfig(IList srcs, IList libs, ISourceLocation bin) {
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.ignores = defaultIgnores;
        this.generatedSources = defaultGeneratedSources;
        this.messages = defaultMessages;
    }
    
    public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList ignores) {
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.ignores = initializeLocList(ignores);
        this.generatedSources = defaultGeneratedSources;
        this.messages = defaultMessages;
    }
    
    public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList ignores, ISourceLocation generatedSources) {
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.ignores = initializeLocList(ignores);
        this.generatedSources = generatedSources;
        this.messages = defaultMessages;
    }
    
    public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList ignores, ISourceLocation generatedSources, IList messages) {
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.ignores = initializeLocList(ignores);
        this.generatedSources = generatedSources;
        this.messages = convertMessages(messages);
    }

    private static IList messages(IConstructor pcfg) {
        return getListValueFromConstructor(pcfg, defaultMessages, "messages");
    }

    private static ISourceLocation generatedSources(IConstructor pcfg) {
        ISourceLocation val = (ISourceLocation) pcfg.asWithKeywordParameters().getParameter("generatedSources");
        return val == null ? defaultGeneratedSources : val;
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

    private static List<ISourceLocation> initializeLocList(IList srcs) {
        return dedup(convertLocs(srcs));
    }
    
    private static List<ISourceLocation> dedup(List<ISourceLocation> list) {
        List<ISourceLocation> filtered = new ArrayList<>(list.size());
        
        for (ISourceLocation elem : list) {
            if (!filtered.contains(elem)) {
                filtered.add(elem);
            }
        }
        
        return filtered;
    }
    
    private static List<ISourceLocation> convertLocs(IList locs){
        List<ISourceLocation> result = new ArrayList<>();
        for(IValue p : locs){
            if(p instanceof ISourceLocation){
                result.add((ISourceLocation) p);
            } else {
                throw new RuntimeException("Path should contain source locations and not " + p.getClass().getName());
            }
        }
        
        return result;
    }

    private static List<IConstructor> convertMessages(IList locs){
        List<IConstructor> result = new ArrayList<>();
        for(IValue p : locs){
            if(p instanceof IConstructor){
                result.add((IConstructor) p);
            } else {
                throw new RuntimeException("Messages should contain message constructors and not " + p.getClass().getName());
            }
        }
        
        return result;
    }
    
    String makeFileName(String qualifiedModuleName) {
        return makeFileName(qualifiedModuleName, "rsc");
    }
    
    public static ISourceLocation getDefaultBin(){
        return defaultBin;
    }
    
    public static ISourceLocation getDefaultGeneratedSources() {
        return defaultGeneratedSources;
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
        return vf.list(srcs.toArray(new IValue[0]));
    }
    
    public ISourceLocation getGeneratedSources() {
        return generatedSources;
    }
    
    public IList getMessages() {
        return vf.list(messages.toArray(new IValue[messages.size()]));
    }
    
    public PathConfig addSourceLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedsrcs = new ArrayList<ISourceLocation>(srcs);
        extendedsrcs.add(dir);
        return new PathConfig(extendedsrcs, libs, bin, ignores, generatedSources, messages);
    }
    
    public PathConfig setGeneratedSources(ISourceLocation dir) throws IOException {
        return new PathConfig(srcs, libs, bin, ignores, dir, messages);
    }
    
    public IList getIgnores() {
        return vf.list(ignores.toArray(new IValue[0]));
    }
    
    public PathConfig addIgnoreLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedignores = new ArrayList<ISourceLocation>(ignores);
        extendedignores.add(dir);
        return new PathConfig(srcs, libs, bin, extendedignores, generatedSources, messages);
    }
    
    public IList getLibs() {
        return vf.list(libs.toArray(new IValue[0]));
    }

    public IList getLibsAndTarget() {
        return getLibs().append(getBin());
    }
    
    public PathConfig addLibLoc(ISourceLocation dir) throws IOException {
        List<ISourceLocation> extendedlibs = new ArrayList<ISourceLocation>(libs);
        extendedlibs.add(dir);
        return new PathConfig(srcs, extendedlibs, bin, ignores, generatedSources, messages);
    }
    
    /**
     * This will create a PathConfig by learning from the MANIFEST/RASCAL.MF file where the sources
     * are, which libraries to reference and which classpath entries to add. If this PathConfig is
     * for the interpreter it adds more folders to the source path than if its for the compiler.
     * 
     * If library dependencies exist for open projects in the same IDE, via the lib://libName, project://libName
     * correspondence, then the target and source folders of the projects are added rather then
     * the jar files. For compiler configs this works differently than for interpreter configs.
     * The latter adds source folders to the sources while the former adds target folders to the libraries.
     * 
     * @param manifest the source location of the folder which contains MANIFEST/RASCAL.MF.
     * @return
     * @throws URISyntaxException
     */
    public static PathConfig fromSourceProjectMemberRascalManifest(ISourceLocation projectMember, RascalConfigMode mode) throws IOException {
        if (!URIResolverRegistry.getInstance().isDirectory(projectMember)) {
            projectMember = URIUtil.getParentLocation(projectMember);
        }
        return fromSourceProjectRascalManifest(inferProjectRoot(projectMember), mode);
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

    private static ISourceLocation inferProjectRoot(ISourceLocation member) {
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

    public PathConfig parse(String pathConfigString) throws IOException {
        try {
            IConstructor cons = (IConstructor) new StandardTextReader().read(vf, store, PathConfigType, new StringReader(pathConfigString));
            IWithKeywordParameters<?> kwp = cons.asWithKeywordParameters();

            IList srcs = (IList) kwp.getParameter("srcs");
            IList libs =  (IList) kwp.getParameter("libs");
            IList ignores = (IList) kwp.getParameter("ignores");
            ISourceLocation generated = (ISourceLocation) kwp.getParameter("generatedSources");
            IList messages = (IList) kwp.getParameter("message");

            ISourceLocation bin = (ISourceLocation) kwp.getParameter("bin");

            PathConfig pcfg = new PathConfig(
                srcs != null ? srcs : vf.list(), 
                libs != null ? libs : vf.list(),
                bin != null ? bin : URIUtil.rootLocation("cwd"),
                ignores != null ? ignores : vf.list(),
                generated != null ? generated : null,
                messages != null ? messages : vf.list()
            );

            return pcfg;
        } 
        catch (FactTypeUseException e) {
            throw new IOException(e);
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
    public static PathConfig fromSourceProjectRascalManifest(ISourceLocation manifestRoot, RascalConfigMode mode)  {
        RascalManifest manifest = new RascalManifest();
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        IRascalValueFactory vf = IRascalValueFactory.getInstance();
        String projectName = manifest.getProjectName(manifestRoot);
        IListWriter libsWriter = vf.listWriter();
        IListWriter srcsWriter = vf.listWriter();
        IListWriter messages = vf.listWriter();
        
        if (!projectName.equals("rascal")) {
            // always add the standard library but not for the project named "rascal"
            // which contains the source of the standard library
            try {
                libsWriter.append(resolveCurrentRascalRuntimeJar());
            }
            catch (IOException e) {
                messages.append(Messages.error(e.getMessage(), manifestRoot));
            }
        }

        ISourceLocation target = URIUtil.correctLocation("project", projectName, "target/classes");
        ISourceLocation generatedSources = URIUtil.correctLocation("project", projectName, "target/generatedSources");

        // This later holds a location of the boot project rascal, in case we depend on that directly in the pom.xml 
        // Depending on which mode we are in, the configuration will run more (version) sanity checks and configure
        // the `libs` differently; all to avoid implicit duplicate and/or inconsistent entries on the `libs` path.
        ISourceLocation rascalProject = null;

        try {
            IList mavenClasspath = getPomXmlCompilerClasspath(manifestRoot);
        
            // This processes Rascal libraries we can find in maven dependencies,
            // adding them to libs or srcs depending on which mode we are in; interpreted or compiled.
            // 
            // * If a current project is open with the same name, we defer to its
            // srcs (interpreter mode only) and target folder (both modes) instead, 
            // for easy development of cross-project features in the IDE. 
            // * Only the rascal project itself is never used from source project, to avoid 
            // complex bootstrapping situations. 


            for (IValue elem : mavenClasspath) {
                ISourceLocation dep = (ISourceLocation) elem;
                String libProjectName = manifest.getManifestProjectName(manifest.manifest(dep));
                ISourceLocation projectLoc = URIUtil.correctLocation("project", libProjectName, "");
                
                if (libProjectName.equals("rascal")) {
                    rascalProject = dep;
                }

                // Rascal LSP is special because the VScode extension pre-loads it into the parametric DSL VM.
                // If the version is different, then the debugger may point to the wrong code, and also the Rascal
                // IDE features like "jump-to-definition" could be off.
                if (libProjectName.equals("rascal-lsp")) {
                    try {
                        var loadedRascalLsp = resolveProjectOnClasspath("rascal-lsp");

                        try (InputStream in = reg.getInputStream(loadedRascalLsp); InputStream in2 = reg.getInputStream(dep)) {
                            var version = new Manifest(in).getMainAttributes().getValue("Specification-Version");
                            var otherVersion = new Manifest(in2).getMainAttributes().getValue("Specification-Version");

                            if (version.equals(otherVersion)) {
                                messages.append(Messages.warning("Pom.xml dependency on rascal-lsp has version " + otherVersion + " while the effective version in the VScode extension is " + version + ". This can have funny effects in the IDE while debugging or code browsing.", getPomXmlLocation(manifestRoot)));
                            }
                        }
                    }
                    catch (FileNotFoundException e) {
                        // this is ok. there is not a duplicate presence of rascal-lsp.
                    }

                }

                if (libProjectName != null) {
                    if (reg.exists(projectLoc) && dep != rascalProject) {
                        // The project we depend on is available in the current workspace. 
                        // so we configure for using the current state of that project.
                        PathConfig childConfig = fromSourceProjectRascalManifest(projectLoc, mode);

                        switch (mode) {
                            case INTERPRETER:
                                srcsWriter.appendAll(childConfig.getSrcs());
                                libsWriter.append(childConfig.getBin());
                                break;
                            case COMPILER:
                                libsWriter.append(setTargetScheme(projectLoc));
                                break;
                        }

                        // libraries are transitively collected
                        libsWriter.appendAll(childConfig.getLibs());

                        // error messages are transitively collected
                        messages.appendAll(childConfig.getMessages());
                    }
                    else if (dep == rascalProject) {
                        // not adding it again (we added rascal already above)
                    }
                    else {
                        // just a pre-installed dependency in the local maven repository
                        if (!reg.exists(dep)) {
                            messages.append(Messages.error("Declared dependency does not exist: " + dep, getPomXmlLocation(manifestRoot)));
                        }
                        else {
                            switch (mode) {
                                case COMPILER:
                                    libsWriter.append(dep);
                                    break;
                                case INTERPRETER:
                                    libsWriter.append(dep);
                                    addLibraryToSourcePath(reg, srcsWriter, messages, dep);
                                    break;
                                default:
                                    messages.append(Messages.error("Can not recognize configuration mode (should be COMPILER or INTERPRETER):" + mode, getRascalMfLocation(manifestRoot)));
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            messages.append(Messages.warning(e.getMessage(), getPomXmlLocation(manifestRoot)));
        }

        try {
            if (!projectName.equals("rascal") && rascalProject == null) {
                // always add the standard library but not for the project named "rascal"
                // which contains the (source of) the standard library, and if we already
                // have a dependency on the rascal project we don't add it here either.
                var rascalLib = resolveCurrentRascalRuntimeJar();
                messages.append(Messages.info("Effective rascal library: " + rascalLib, getPomXmlLocation(manifestRoot)));
                libsWriter.append(rascalLib);
            }
            else if (projectName.equals("rascal")) {
                messages.append(Messages.info("detected rascal self-application", getPomXmlLocation(manifestRoot)));
            }
            else if (rascalProject != null) {
                // The Rascal interpreter can not escape its own classpath, whether
                // or not we configure a different version in the current project's 
                // pom.xml or not. So that pom dependency is always ignored!

                // We check this also in COMPILED mode, for the sake of consistency,
                // but it is not strictly necessary since the compiler can check and compile
                // against any standard library on the libs path, even if it's running
                // itself against a different rascal runtime and standard library.

                RascalManifest rmf = new RascalManifest();
                var builtinVersion = RascalManifest.getRascalVersionNumber();
                var dependentRascalProject = reg.logicalToPhysical(rascalProject);
                var dependentVersion = rmf.getManifestVersionNumber(dependentRascalProject);
                
                if (!builtinVersion.equals(dependentVersion)) {
                    messages.append(Messages.info("Effective rascal version: " + builtinVersion, getPomXmlLocation(manifestRoot)));
                    messages.append(Messages.warning("Different rascal dependency is not used: " + dependentVersion, getPomXmlLocation(manifestRoot)));
                }
            }

            ISourceLocation projectLoc = URIUtil.correctLocation("project", projectName, "");

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

        // The `rascal` project has two special source folders (`.../library`
        // and `.../typepal`). The versions of these two folders in the
        // "current" `rascal` (as per `resolveCurrentRascalRuntimeJar()`) are
        // always included in the module path. All remaining source folders of
        // the `rascal` project are always excluded, except when the current
        // project happens to be `rascal` itself (in which case they are
        // normally included).

        ISourceLocation currentRascal = URIUtil.unknownLocation();
        try {
            currentRascal = JarURIResolver.jarify(resolveCurrentRascalRuntimeJar());
        } catch (IOException e) {
            messages.append(Messages.error(e.getMessage(), manifestRoot));
        }

        var rascalSpecialSrcs = new LinkedHashMap<ISourceLocation, String>(); // Keep insertion order for predictable output
        rascalSpecialSrcs.put(URIUtil.rootLocation("std"),"src/org/rascalmpl/library");
        rascalSpecialSrcs.put(URIUtil.getChildLocation(currentRascal, "org/rascalmpl/typepal"), "src/org/rascalmpl/typepal");
        
        BiPredicate<String, String> isRascalSpecialSrc = (project, src) ->
            "rascal".equals(project) && rascalSpecialSrcs.values().contains(src);

        srcsWriter.appendAll(rascalSpecialSrcs.keySet());
        for (String srcName : manifest.getSourceRoots(manifestRoot)) {
            if (isRascalSpecialSrc.test(projectName, srcName)) {
                continue; // Don't append special source folders again
            }

            var srcFolder = URIUtil.getChildLocation(manifestRoot, srcName);
            
            if (!reg.exists(srcFolder) || !reg.isDirectory(srcFolder)) {
                messages.append(Messages.error("Source folder " + srcFolder + " does not exist.", getRascalMfLocation(rascalProject)));
            }

            srcsWriter.append(srcFolder);
        }
        
        return new PathConfig(
                srcsWriter.done(), 
                libsWriter.done(), 
                target, 
                vf.list(),
                generatedSources, 
                messages.done());
    }

    private static void addLibraryToSourcePath(URIResolverRegistry reg, IListWriter srcsWriter, IListWriter messages, ISourceLocation jar) {
        if (!reg.exists(URIUtil.getChildLocation(jar, RascalManifest.META_INF_RASCAL_MF))) {
            // skip all the non Rascal libraries
            return;
        }

       
        var manifest = new RascalManifest();

        // the rascal dependency leads to a dependency on the std:/// location, somewhere _inside_ of the rascal jar
        if (manifest.getProjectName(jar).equals("rascal")) {
            srcsWriter.append(URIUtil.rootLocation("std"));
            return;            
        }

        boolean foundSrc = false;

        for (String src : manifest.getSourceRoots(jar)) {
            ISourceLocation srcLib = URIUtil.getChildLocation(jar, src);
            if (reg.exists(srcLib)) {
                srcsWriter.append(srcLib);
                foundSrc = true;
            }
            else {
                messages.append(Messages.error(srcLib + " source folder does not exist.", URIUtil.getChildLocation(jar, RascalManifest.META_INF_RASCAL_MF)));
            }
        }

        if (!foundSrc) {
            // if we could not find source roots, we default to the jar root
            srcsWriter.append(jar);
        }
    }

    private static ISourceLocation setTargetScheme(ISourceLocation projectLoc) {
        try {
            return URIUtil.changeScheme(projectLoc, "target");
        }
        catch (URISyntaxException e) {
            // this never happens because "target" is valid
            return projectLoc;
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
     * @param manifestRoot
     * @return
     * @throws IOException 
     */
    private static IList getPomXmlCompilerClasspath(ISourceLocation manifestRoot) throws IOException {
        var pomxml = getPomXmlLocation(manifestRoot);
        manifestRoot = URIResolverRegistry.getInstance().logicalToPhysical(manifestRoot);

        if (!"file".equals(manifestRoot.getScheme())) {
            return vf.list();
        }

        if (!URIResolverRegistry.getInstance().exists(pomxml)) {
            return vf.list();
        }

        String mvnCommand = computeMavenCommandName();

        installNecessaryMavenPlugins(mvnCommand);

        // Note how we try to do this "offline" using the "-o" flag
        ProcessBuilder processBuilder = new ProcessBuilder(mvnCommand, 
            "--batch-mode", 
            "-o", 
            "dependency:build-classpath",
            "-DincludeScope=compile",
            trustStoreFix()
        );

        processBuilder.directory(new File(manifestRoot.getPath()));
        processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home", System.getenv("JAVA_HOME")));

        Process process = processBuilder.start();

        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return processOutputReader.lines()
                .filter(line -> !line.startsWith("["))
                .filter(line -> !line.contains("-----"))
                .flatMap(line -> Arrays.stream(line.split(File.pathSeparator)))
                .filter(fileName -> new File(fileName).exists())
                .map(elem -> {
                    try {
                        return MavenRepositoryURIResolver.mavenize(URIUtil.createFileLocation(elem));
                    }
                    catch (URISyntaxException e) {
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(vf.listWriter());
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static String computeMavenCommandName() {
        if (System.getProperty("os.name", "generic").startsWith("Windows")) {
            return "mvn.cmd";
        }
        else {
            return "mvn";
        }
    }

    /**
     * Prints what users need to know about how the interpreter is configured with this PathConfig
     */
    public void printInterpreterConfigurationStatus(PrintWriter out) {
        out.println("Module paths:");
        getSrcs().forEach(f -> {
            var l = (ISourceLocation) f;
            var s = " ".repeat(4) + l;
            if (l.getScheme().equals("std")) {
                try {
                    s += " at " + URIResolverRegistry.getInstance().logicalToPhysical(l);
                } catch (IOException e) {
                    s += " at unknown physical location";
                }
            }
            out.println(s);
        });
        out.println("JVM library classpath:");
        getLibsAndTarget().forEach((l) -> out.println(" ".repeat(4) + l));
        out.flush();
    }

    /**
     * Prints what users need to know about how the compiler is configured with this PathConfig
     */
    public void printCompilerConfigurationStatus(PrintWriter out) {
        out.println("Source paths:");
        getSrcs().forEach((f) -> out.println(" ".repeat(4) + f));
        out.println("Compiler target folder:");
        out.println(" ".repeat(4) + getBin());
        out.println("Compiler generated sources folder:");
        out.println(" ".repeat(4) + getGeneratedSources());
        out.println("Rascal and Java library classpath:");
        getLibsAndTarget().forEach((l) -> out.println(" ".repeat(4) + l));
        out.flush();
    }

    private static void installNecessaryMavenPlugins(String mvnCommand) throws IOException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(mvnCommand, 
                "-q", 
                "dependency:get", 
                "-DgroupId=org.apache.maven.plugins",
                "-DartifactId=maven-dependency-plugin", 
                "-Dversion=2.8",
                trustStoreFix());
            processBuilder.environment().put("JAVA_HOME", System.getProperty("java.home", System.getenv("JAVA_HOME")));

            Process process = processBuilder.start();
            if (process.waitFor() != 0) {
                throw new IOException("mvn dependency:get returned non-zero");
            } 
        }
        catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    private static String trustStoreFix() {
        return isWindows() ? WINDOWS_ROOT_TRUSTSTORE_TYPE_DEFINITION : "-Dnothing_to_see_here";
    }

    public ISourceLocation getBin() {
        return bin;
    }
    
    String makeFileName(String qualifiedModuleName, String extension) {
        return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
    }
    
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

    private String pathToModulename(String modulePath, String folder) {
        String moduleName = modulePath.replaceFirst(folder, "").replace(".rsc", "");
        if(moduleName.startsWith("/")){
            moduleName = moduleName.substring(1, moduleName.length());
        }
        return moduleName.replace(Configuration.RASCAL_PATH_SEP, Configuration.RASCAL_MODULE_SEP);
    }

    private String moduleToDir(String module) {
        return module.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
    }

    private ISourceLocation getFullURI(String path, ISourceLocation dir) throws URISyntaxException {
        return URIUtil.getChildLocation(dir, path);
    }

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

        config.put("srcs", getSrcs());
        config.put("ignores", getIgnores());
        config.put("bin", getBin());
        config.put("libs", getLibs());
        config.put("generatedSources", getGeneratedSources());
        config.put("messages", getMessages());

        return vf.constructor(pathConfigConstructor, new IValue[0], config);
    }
    
    /**
     * Overview of the contents of the current configuration for debugging purposes.
     * Not necessarily for end-user UI, although it's better than nothing.
     */
    public String toString(){
      StringWriter w = new StringWriter();
      w.append("Path configuration items:")
       .append("srcs:            ").append(getSrcs().toString()).append("\n")
       .append("ignores:         ").append(getIgnores().toString()).append("\n")
       .append("libs:            ").append(getLibs().toString()).append("\n")
       .append("bin:             ").append(getBin().toString()).append("\n")
       .append("generatedSources:").append(getGeneratedSources().toString()).append("\n")
       .append("messages:        ").append(getMessages().toString()).append("\n")
       ;
       
      return w.toString();
    }
}
