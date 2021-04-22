package org.rascalmpl.library.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
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
	
	// WARNING: these definitions must reflect the definitions in util::Reflective.rsc
	private final Type PathConfigType = tf.abstractDataType(store, "PathConfig"); 
	private final Type pathConfigConstructor = tf.constructor(store, PathConfigType, "pathConfig");
	
	private final List<ISourceLocation> srcs;		// List of locations to search for source files
	private final List<ISourceLocation> libs;     // List of (library) locations to search for derived files
	private final List<ISourceLocation> courses; 	// List of (library) locations to search for course source files
	private final List<ISourceLocation> javaCompilerPath;     // List of (library) locations to use for the compiler path of generated parsers
	private final List<ISourceLocation> classloaders;     // List of (library) locations to use to bootstrap classloaders from
    
	private final ISourceLocation bin;  // Global location for derived files outside projects or libraries

	private static ISourceLocation defaultStd;
	private static List<ISourceLocation> defaultCourses;
	private static List<ISourceLocation> defaultJavaCompilerPath;
	private static List<ISourceLocation> defaultClassloaders;
	private static ISourceLocation defaultBin;
    
	
	static {
		try {
		    // Defaults should be in sync with util::Reflective
			defaultStd =  vf.sourceLocation("lib", "rascal", "");
			defaultBin = vf.sourceLocation("tmp", "", "default-rascal-bin");
			defaultCourses = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultJavaCompilerPath = computeDefaultJavaCompilerPath();
			defaultClassloaders = computeDefaultClassLoaders();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public PathConfig() {
		srcs = Collections.emptyList();
		courses = defaultCourses;
		bin = defaultBin;
		libs = Arrays.asList(defaultStd);
		javaCompilerPath = defaultJavaCompilerPath;
		classloaders = defaultClassloaders;
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin) throws IOException {
		this(srcs, libs, bin, defaultCourses);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> courses) throws IOException {
	    this(srcs, libs, bin, courses, defaultJavaCompilerPath);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath) throws IOException {
        this(srcs, libs, bin, courses, javaCompilerPath, defaultClassloaders);
    }
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath, List<ISourceLocation> classloaders) throws IOException {
		this.srcs = dedup(srcs);
		this.courses = dedup(courses);
		this.libs = dedup(libs);
		this.bin = bin;
		this.javaCompilerPath = dedup(javaCompilerPath);
		this.classloaders = dedup(classloaders);
	}
	
    public PathConfig(IList srcs, IList libs, ISourceLocation bin) throws IOException{
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.courses = defaultCourses;
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses) throws IOException{
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.courses = initializeLocList(courses);
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses, IList javaCompilerPath) throws IOException{
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.courses = initializeLocList(courses);
        this.javaCompilerPath = initializeLocList(javaCompilerPath);
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses, IList javaCompilerPath, IList classloaders) throws IOException {
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.courses = initializeLocList(courses);
        this.javaCompilerPath = initializeLocList(javaCompilerPath);
        this.classloaders = initializeLocList(classloaders);
    }
	
    private static ISourceLocation parseSourceLocation(String recLib) throws IOException {
        return (ISourceLocation) new StandardTextReader().read(vf, new StringReader(recLib));
    }
	
    public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses, IList javaCompilerPath, IList classloaders, ISourceLocation repo) throws IOException{
        this.srcs = initializeLocList(srcs);
        this.libs = initializeLocList(libs);
        this.bin = bin;
        this.courses = initializeLocList(courses);
        this.javaCompilerPath = initializeLocList(javaCompilerPath);
        this.classloaders = initializeLocList(classloaders);
    }

    public PathConfig parse(String pathConfigString) throws IOException {
        try {
            IConstructor cons = (IConstructor) new StandardTextReader().read(vf, store, PathConfigType, new StringReader(pathConfigString));
            IWithKeywordParameters<?> kwp = cons.asWithKeywordParameters();

            IList srcs = (IList) kwp.getParameter("srcs");
            IList libs =  (IList) kwp.getParameter("libs");
            ISourceLocation bin = (ISourceLocation) kwp.getParameter("bin");

            return new PathConfig(
                srcs != null ? srcs : vf.list(), 
                libs != null ? libs : vf.list(),
                bin != null ? bin : URIUtil.rootLocation("cwd") 
            );
        } 
        catch (FactTypeUseException e) {
            throw new IOException(e);
        }
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
	
	private static List<ISourceLocation> computeDefaultClassLoaders() {
        List<ISourceLocation> result = new ArrayList<>();
        String javaClasspath = System.getProperty("java.class.path");
        if (javaClasspath != null) {
            for (String path : javaClasspath.split(File.pathSeparator)) {
                result.add(vf.sourceLocation(new File(path).getAbsolutePath()));
            }
        }
        else {
            result.add(URIUtil.correctLocation("system", "", ""));
        }
        return result;
    }

    private static List<ISourceLocation> computeDefaultJavaCompilerPath() {
        List<ISourceLocation> result = new ArrayList<>();
        String classPath = System.getProperty("java.class.path");
        
        if (classPath != null) {
            for (String path : classPath.split(File.pathSeparator)) {
                result.add(vf.sourceLocation(new File(path).getAbsolutePath()));
            }
        }
        
        return result;
    }
	
	String makeFileName(String qualifiedModuleName) {
		return makeFileName(qualifiedModuleName, "rsc");
	}
	
	public static ISourceLocation getDefaultStd(){
	    return defaultStd;
	}
	
	public static ISourceLocation getDefaultBin(){
        return defaultBin;
    }
	
	public static List<ISourceLocation> getDefaultJavaCompilerPath() {
	    return  Collections.unmodifiableList(defaultJavaCompilerPath);
	}
	
	public static IList getDefaultJavaCompilerPathList() {
        return  convertLocs(defaultJavaCompilerPath);
    }
	
	public static IList getDefaultCoursesList() {
	    return convertLocs(defaultCourses);
	}
	
	public static IList getDefaultClassloadersList() {
	    return convertLocs(defaultClassloaders);
	}
	
	private static IList convertLocs(List<ISourceLocation> locs) {
	    IListWriter w = vf.listWriter();
	    w.appendAll(locs);
	    return w.done();
    }

    public static List<ISourceLocation> getDefaultCourses(){
	    return  Collections.unmodifiableList(defaultCourses);
	}
	
	public static List<ISourceLocation> getDefaultClassloaders() {
        return Collections.unmodifiableList(defaultClassloaders);
    }
	
	public IValueFactory getValueFactory() {
	    return vf;
	}
	
	public IList getSrcs() {
	    return vf.list(srcs.toArray(new IValue[0]));
	}
	
	public IList getJavaCompilerPath() {
	    return vf.list(javaCompilerPath.toArray(new IValue[0]));
	}
	
	public IList getClassloaders() {
	    return vf.list(classloaders.toArray(new IValue[classloaders.size()]));
	}
	
	public PathConfig addSourceLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedsrcs = new ArrayList<ISourceLocation>(srcs);
		extendedsrcs.add(dir);
		try {
            return new PathConfig(extendedsrcs, libs, bin, courses, javaCompilerPath, classloaders);
        }
        catch (IOException e) {
            assert false;
            return this;
        }
	}
	
	public PathConfig addJavaCompilerPath(ISourceLocation dir) {
	    List<ISourceLocation> extended = new ArrayList<ISourceLocation>(javaCompilerPath);
        extended.add(dir);
        try {
            return new PathConfig(srcs, libs, bin, courses, extended, classloaders);
        }
        catch (IOException e) {
            assert false;
            return this;
        }
	}
	
	public PathConfig addClassloader(ISourceLocation dir) {
        List<ISourceLocation> extended = new ArrayList<ISourceLocation>(classloaders);
        extended.add(dir);
        try {
            return new PathConfig(srcs, libs, bin, courses, javaCompilerPath, extended);
        }
        catch (IOException e) {
            assert false;
            return this;
        }
    }
	
	public IList getCourses() {
	    return vf.list(courses.toArray(new IValue[0]));
	}
	
	public PathConfig addCourseLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedcourses = new ArrayList<ISourceLocation>(courses);
		extendedcourses.add(dir);
		try {
            return new PathConfig(srcs, libs, bin, extendedcourses, javaCompilerPath, classloaders);
        }
        catch (IOException e) {
            assert false;
            return this;
        }
	}
	
	public ISourceLocation getCourseLoc(String courseName) throws URISyntaxException, IOException{
		for(ISourceLocation dir : courses){
			ISourceLocation fileLoc = vf.sourceLocation(dir.getScheme(), dir.getAuthority(), dir.getPath() + "/" + courseName);
			if(URIResolverRegistry.getInstance().exists(fileLoc)){
		    	return fileLoc;
		    }
		}
		throw new IOException("Course " + courseName + " not found");
	}
	
	private boolean isCourse(String name){
		return name.matches("[A-Z][A-Za-z0-9]*$");
	}
	
	private String fileName(ISourceLocation loc){
		String[] parts = loc.getPath().split("/");
		return parts[parts.length - 1];
	}
	
	public List<String> listCourseEntries() throws IOException{
		URIResolverRegistry reg = URIResolverRegistry.getInstance();
		ArrayList<String> courseList = new ArrayList<>();
		for(ISourceLocation dir : courses){
			if(reg.exists(dir)){
				for(ISourceLocation entry : reg.list(dir)){
					if(reg.isDirectory(entry)){
						String name = fileName(entry);
						if(isCourse(name)){
							courseList.add(name);
						}
					}
				}
			}
		}
		return courseList;
	}
	
	public IList getLibs() {
        return vf.list(libs.toArray(new IValue[0]));
    }
	
	public PathConfig addLibLoc(ISourceLocation dir) throws IOException {
		List<ISourceLocation> extendedlibs = new ArrayList<ISourceLocation>(libs);
		extendedlibs.add(dir);
		return new PathConfig(srcs, extendedlibs, bin, courses, javaCompilerPath, classloaders);
	}
	
	/**
	 * Construct a path config necessary to resolve links between depending libraries. This
	 * can be used for browsing library files in IDEs or to construct a valid classpath for running
	 * compiled library modules.
	 *  
	 * @param library   the name of a library
	 * @return          a pathConfig with a proper library path and run-time classpath
	 *  
	 * @throws IOException if the RASCAL.MF files found contain errors
	 */
	public static PathConfig fromLibraryRascalManifest(String library) throws IOException {
	    ISourceLocation libraryLoc = URIUtil.correctLocation("lib", library, "");
	    
	    RascalManifest manifest = new RascalManifest();
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        Set<String> loaderSchemes = reg.getRegisteredClassloaderSchemes();
        
        IListWriter libsWriter = vf.listWriter();
        IListWriter classloaders = vf.listWriter();
        
        // always add the standard library on the lib path
        if (!library.equals("rascal")) {
            libsWriter.append(URIUtil.correctLocation("lib", "rascal", ""));
        }
        
        // add the current library as well, for resolving references within the library
        libsWriter.append(libraryLoc);
        
        for (String lib : manifest.getRequiredLibraries(libraryLoc)) {
            ISourceLocation jar = lib.startsWith("|") ? parseSourceLocation(lib) : URIUtil.getChildLocation(libraryLoc, lib);
            
            if (jar != null && reg.exists(jar)) {
                libsWriter.append(jar);
            }
            else {
                System.err.println("WARNING: could not resolve required library: " + lib);
            }
            
            if (loaderSchemes.contains(jar.getScheme())) {
                classloaders.append(jar);
            }
        }
        
        ISourceLocation bin = URIUtil.correctLocation("unknown", "", "");
        
        // for the Rascal run-time
        classloaders.append(URIUtil.correctLocation("system", "", ""));
        
        return new PathConfig(
                vf.list(), // a library has no sources to compile 
                libsWriter.done(), 
                bin, 
                vf.list(), 
                getDefaultJavaCompilerPathList(), 
                classloaders.done());
	}
    
    /**
	 * This will _add_ the configuration parameters found (srcs, libs, etc.) as found in the given manifest file.
	 * 
	 * @param a file or folder in a project that has a META-INF/RASCAL.MF file somewhere close.
	 * @return
	 */
	public static PathConfig fromSourceProjectMemberRascalManifest(ISourceLocation projectMember) throws IOException {
        if (!URIResolverRegistry.getInstance().isDirectory(projectMember)) {
            projectMember = URIUtil.getParentLocation(projectMember);
        }
        return fromSourceProjectRascalManifest(inferProjectRoot(projectMember));
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
	/**
	 * This will _add_ the configuration parameters found (srcs, libs, etc.) as found in the given manifest file.
	 * 
	 * @param manifest the source location of the folder which contains MANIFEST/RASCAL.MF.
	 * @return
	 */
	public static PathConfig fromSourceProjectRascalManifest(ISourceLocation manifestRoot) throws IOException {
        RascalManifest manifest = new RascalManifest();
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        Set<String> loaderSchemes = reg.getRegisteredClassloaderSchemes();
        
        IListWriter libsWriter = vf.listWriter();
        IListWriter srcsWriter = vf.listWriter();
        IListWriter classloaders = vf.listWriter();
        
        libsWriter.append(URIUtil.correctLocation("lib", "rascal", ""));
        
        // These are jar files which make contain compiled Rascal code to link to:
        for (String lib : manifest.getRequiredLibraries(manifestRoot)) {
            ISourceLocation jar = lib.startsWith("|") ? parseSourceLocation(lib) : URIUtil.getChildLocation(manifestRoot, lib);
            
            if (jar != null && reg.exists(jar)) {
                libsWriter.append(jar);
            }
            else {
                System.err.println("WARNING: could not resolve required library: " + lib);
            }
            
            if (loaderSchemes.contains(jar.getScheme())) {
                classloaders.append(jar);
            }
        }
        
        for (String srcName : manifest.getSourceRoots(manifestRoot)) {
            srcsWriter.append(URIUtil.getChildLocation(manifestRoot, srcName));
        }
        
        ISourceLocation bin = URIUtil.getChildLocation(manifestRoot, "bin");
        ISourceLocation target = URIUtil.getChildLocation(manifestRoot, "target/classes");
       

        if (reg.exists(bin)) {
            classloaders.append(bin);
        }
        else if (reg.exists(target)) {
            classloaders.append(target);
        }

        // for the Rascal run-time
        classloaders.append(URIUtil.correctLocation("system", "", ""));

        classloaders.appendAll(getPomXmlCompilerClasspath(manifestRoot));
        
        return new PathConfig(
                srcsWriter.done(), 
                libsWriter.done(), 
                bin, 
                vf.list(), 
                getDefaultJavaCompilerPathList(), 
                classloaders.done());
	}
	
    /**
     * See if there is a pom.xml and extract the compile-time classpath from a mvn run
     * if there is such a file.
     * @param manifestRoot
     * @return
     */
	private static IList getPomXmlCompilerClasspath(ISourceLocation manifestRoot) {
        ISourceLocation pomxml = URIUtil.getChildLocation(manifestRoot, "pom.xml");

        if (!"file".equals(manifestRoot.getScheme())) {
            return vf.list();
        }

        if (!URIResolverRegistry.getInstance().exists(pomxml)) {
            return vf.list();
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "-q", "-o", "exec:exec",
                "-DExec.classpathScope=compile", "-Dexec.executable=echo", "-Dexec.args=%classpath");
            processBuilder.directory(new File(manifestRoot.getPath()));

            Process process = processBuilder.start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                process.waitFor();

                return processOutputReader.lines()
                    .filter(line -> !line.contains("-----"))
                    .flatMap(line -> Arrays.stream(line.split(File.pathSeparator)))
                    .map(elem -> {
                        try {
                            return URIUtil.createFileLocation(elem);
                        }
                        catch (URISyntaxException e) {
                            return null;
                        }
                    })
                    .filter(e -> e != null)
                    .collect(vf.listWriter());
            }
        }
        catch (IOException | InterruptedException e) {
            return vf.list();
        }
    }

    public ISourceLocation getBin() {
        return bin;
    }
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	ISourceLocation getModuleLoc(String qualifiedModuleName) throws IOException {
		ISourceLocation result = resolveModule(qualifiedModuleName);
		if(result == null){
		    throw new IOException("Module " + qualifiedModuleName + " not found");
		}
		return result;
	}
	
	public ISourceLocation resolveModule(String qualifiedModuleName) {
        String fileName = makeFileName(qualifiedModuleName);
        for(ISourceLocation dir : srcs){
            ISourceLocation fileLoc;
            try {
                getFullURI(fileName, dir);
                fileLoc = getFullURI(fileName, dir);
                if(URIResolverRegistry.getInstance().exists(fileLoc)){
                    return fileLoc;
                }
            }
            catch (URISyntaxException e) {
                return null;
            }
        }
        return null;
    }
	
	public String getModuleName(ISourceLocation moduleLoc) throws IOException{
	    String modulePath = moduleLoc.getPath();
	    if(!modulePath.endsWith(".rsc")){
	        throw new IOException("Not a Rascal source file: " + moduleLoc);
	    }
	    
	    if (moduleLoc.getScheme().equals("std") || moduleLoc.getScheme().equals("lib")) {
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
        return moduleName.replace("/", "::");
    }
	
	private String moduleToDir(String module) {
        return module.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
    }
    
    private ISourceLocation getFullURI(String path, ISourceLocation dir) throws URISyntaxException {
        return URIUtil.getChildLocation(dir, path);
    }
    
	public List<String> listModuleEntries(String moduleRoot) {
        assert !moduleRoot.endsWith("::");
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
                                result.add(module + "::");
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
	
	public IConstructor asConstructor() {
	    Map<String, IValue> config = new HashMap<>();

	    config.put("srcs", getSrcs());
	    config.put("courses", getCourses());
	    config.put("bin", getBin());
	    config.put("libs", getLibs());
	    config.put("javaCompilerPath", getJavaCompilerPath());
	    config.put("classloaders", getClassloaders());

	    return vf.constructor(pathConfigConstructor, new IValue[0], config);
	}
	
	public String toString(){
	  StringWriter w = new StringWriter();
      w.append("srcs:      ").append(getSrcs().toString()).append("\n")
       .append("libs:      ").append(getLibs().toString()).append("\n")
       .append("courses:   ").append(getCourses().toString()).append("\n")
       .append("bin:       ").append(getBin().toString()).append("\n")
       .append("classpath: ").append(getJavaCompilerPath().toString()).append("\n")
       .append("loaders:   ").append(getClassloaders().toString()).append("\n")
       ;
       
      return w.toString();
    }
}
