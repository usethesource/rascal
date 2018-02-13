package org.rascalmpl.core.library.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.lang.rascal.boot.IJava2Rascal;

import org.rascalmpl.core.uri.URIResolverRegistry;
import org.rascalmpl.core.uri.URIUtil;
import org.rascalmpl.core.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.StandardTextReader;

public class PathConfig {
	
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final List<ISourceLocation> srcs;		// List of locations to search for source files
	private final List<ISourceLocation> libs;     // List of (library) locations to search for derived files
	private final List<ISourceLocation> courses; 	// List of (library) locations to search for course source files
	private final List<ISourceLocation> javaCompilerPath;     // List of (library) locations to search for course source files
	private final List<ISourceLocation> classloaders;     // List of (library) locations to search for course source files
    
	private final ISourceLocation bin;  // Global location for derived files outside projects or libraries
	private final ISourceLocation boot; // Location with Rascal boot files
	private final ISourceLocation repo; // Global location for finding installed Rascal packages

	private static ISourceLocation defaultStd;
	private static List<ISourceLocation> defaultCourses;
	private static List<ISourceLocation> defaultJavaCompilerPath;
	private static List<ISourceLocation> defaultClassloaders;
	private static ISourceLocation defaultBin;
	private static ISourceLocation defaultBoot;
	private static ISourceLocation defaultRepo;
    
	
	static {
		try {
		    // Defaults should be in sync with util::Reflective
			defaultStd =  vf.sourceLocation("std", "", "");
			defaultBin = vf.sourceLocation("home", "", "bin");
			defaultBoot = vf.sourceLocation("boot", "", "");
			defaultRepo = vf.sourceLocation("home","",".r2d2");
			defaultCourses = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultJavaCompilerPath = computeDefaultJavaCompilerPath();
			defaultClassloaders = computeDefaultClassLoaders();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public PathConfig() {
		srcs = Arrays.asList(defaultStd);
		courses = defaultCourses;
		bin = defaultBin;
		boot = defaultBoot;
		// TODO: this should be |std:///| and |bin:///| by default, after
		// the boot folder will not contain the library anymore. 
		libs = Arrays.asList(bin, boot);
		javaCompilerPath = defaultJavaCompilerPath;
		classloaders = defaultClassloaders;
		repo = defaultRepo;
	}
	
    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin) throws IOException {
		this(srcs, libs, bin, defaultBoot);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot) throws IOException {
		this(srcs, libs, bin, boot, defaultCourses);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses) throws IOException {
	    this(srcs, libs, bin, boot, courses, defaultJavaCompilerPath);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath) throws IOException {
        this(srcs, libs, bin, boot, courses, javaCompilerPath, defaultClassloaders);
    }
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath, List<ISourceLocation> classloaders) throws IOException{
	    this(srcs, libs, bin, boot, courses, javaCompilerPath, classloaders, defaultRepo);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath, List<ISourceLocation> classloaders, ISourceLocation repo) throws IOException{
		this.srcs = srcs;
		this.courses = courses;
		this.libs = transitiveClosure(libs, repo);
		this.bin = bin;
		this.boot = boot;
		this.javaCompilerPath = javaCompilerPath;
		this.classloaders = classloaders;
		this.repo = repo;
	}
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot) throws IOException{
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), defaultRepo);
        this.bin = bin;
        this.boot = boot;
        this.repo = defaultRepo;
        this.courses = defaultCourses;
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses, ISourceLocation repo) throws IOException{
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), repo);
        this.bin = bin;
        this.boot = defaultBoot;
        this.repo = repo;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses) throws IOException{
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), defaultRepo);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
        this.repo = defaultRepo;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses, IList javaCompilerPath) throws IOException{
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), defaultRepo);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = convertLocs(javaCompilerPath);
        this.classloaders = defaultClassloaders;
        this.repo = defaultRepo;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses, IList javaCompilerPath, IList classloaders) throws IOException {
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), defaultRepo);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = convertLocs(javaCompilerPath);
        this.classloaders = convertLocs(classloaders);
        this.repo = defaultRepo;
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
    
	/**
	 * This locates necessary libraries on the search path by taking the current libraries, finding the RASCAL.MF file and looking
	 * at the Required-Libraries field. The required libraries are then sought out in the {@see r2d2} installation folder and the process
	 * continues transitively until either a library is not found and an exception is raised, or until the process terminates because no new
	 * dependencies have been discovered.
	 * 
	 * @param seedLibraries are the root of the dependence hierarchy
	 * @param seedSources are source folders which may contain additional MANIFEST/RASCAL.MF files for library dependencies
	 * @param repo 
	 * @return the full list of transitively required libraries
	 * @throws IOException when a required library is not found.
	 */
	private List<ISourceLocation> transitiveClosure(List<ISourceLocation> seedLibraries, ISourceLocation repo) throws IOException {
        List<ISourceLocation> todo = new LinkedList<>();
        List<ISourceLocation> done = new LinkedList<>();
        
        todo.addAll(seedLibraries);
        
        while (!todo.isEmpty()) {
            List<ISourceLocation> more = new LinkedList<>();
            
            for (ISourceLocation lib : todo) {
                if (done.contains(lib)) {
                    continue;
                } else {
                    done.add(lib);
                }
                
                List<ISourceLocation> next = getMoreLibraries(repo, lib);
                next.removeAll(done);
                more.addAll(next);
            }
            
            todo.addAll(more);
            todo.removeAll(done);
        }
        

        // make all libraries look inside jar files where possible
        List<ISourceLocation> result = new LinkedList<>();
        for (ISourceLocation l : done) {
            ISourceLocation jarred = RascalManifest.jarify(l);
            if (!result.contains(jarred)) {
                result.add(jarred);
            }
        }
        
        return Collections.unmodifiableList(result);
	}

    private List<ISourceLocation> getMoreLibraries(ISourceLocation repo, ISourceLocation lib) throws IOException {
        List<ISourceLocation> result = new LinkedList<>();
        
        for (String recLib : new RascalManifest().getRequiredLibraries(lib)) {
            ISourceLocation libLoc = recLib.startsWith("|") ? parseSourceLocation(recLib) : findLibrary(recLib, repo);
            
            if (libLoc != null) {
                result.add(libLoc);
            }
            else {
                throw new IOException("Required Rascal library not found: " + recLib + ", needed by " + lib);
            }
        }
        
        return result;
    }

    private static ISourceLocation parseSourceLocation(String recLib) throws IOException {
        return (ISourceLocation) new StandardTextReader().read(vf, new StringReader(recLib));
    }
	
    private ISourceLocation findLibrary(String name, ISourceLocation repo) throws IOException {
        ISourceLocation found = null;
        
        for (ISourceLocation file : URIResolverRegistry.getInstance().list(repo)) {
            String path = file.getPath();
            path = path.substring(repo.getPath().length() + 1 /* for removing the / */);
            
            if (path.startsWith(name)) {
                String afterName = path.substring(name.length());
                
                if (afterName.length() > 0 && (afterName.equals(".jar") || (afterName.startsWith("-") && afterName.endsWith(".jar")))) {
                    if (found == null) {
                        found = file;
                    }
                    else {
                        throw new IOException("Ambiguous duplicate library entry named " + file + ", as indistuinguishable from " + found + " for library requirement " + name);
                    }
                }
            }
        }
        
        return found;
    }

    public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses, IList javaCompilerPath, IList classloaders, ISourceLocation repo) throws IOException{
        this.srcs = convertLocs(srcs);
        this.libs = transitiveClosure(convertLocs(libs), repo);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = convertLocs(javaCompilerPath);
        this.classloaders = convertLocs(classloaders);
        this.repo = repo;
    }
	
	List<ISourceLocation> convertLocs(IList locs){
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
	
	String makeFileName(String qualifiedModuleName) {
		return makeFileName(qualifiedModuleName, "rsc");
	}
	
	public static ISourceLocation getDefaultStd(){
	    return defaultStd;
	}
	
	public static ISourceLocation getDefaultBin(){
        return defaultBin;
    }
	
	public static ISourceLocation getDefaultBoot(){
        return defaultBoot;
    }
	
	public static ISourceLocation getDefaultRepo() {
        return defaultRepo;
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
            return new PathConfig(extendedsrcs, libs, bin, boot, courses, javaCompilerPath, classloaders);
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
            return new PathConfig(srcs, libs, bin, boot, courses, extended, classloaders);
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
            return new PathConfig(srcs, libs, bin, boot, courses, javaCompilerPath, extended);
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
            return new PathConfig(srcs, libs, bin, boot, extendedcourses, javaCompilerPath, classloaders);
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
		return new PathConfig(srcs, extendedlibs, bin, boot, courses, javaCompilerPath, classloaders);
	}
	
	/**
	 * This will _add_ the configuration parameters found (srcs, libs, etc.) as found in the given manifest file.
	 * 
	 * @param manifest the source location of the folder which contains MANIFEST/RASCAL.MF.
	 * @return
	 */
	public static PathConfig fromSourceProjectRascalManifest(ISourceLocation manifestRoot) throws IOException {
        RascalManifest manifest = new RascalManifest();
        Set<String> loaderSchemes = URIResolverRegistry.getInstance().getRegisteredClassloaderSchemes();
        
        IListWriter libsWriter = vf.listWriter();
        IListWriter srcsWriter = vf.listWriter();
        IListWriter classloaders = vf.listWriter();
        
        libsWriter.append(URIUtil.correctLocation("stdlib", "", ""));
        
        // These are jar files which make contain compiled Rascal code to link to:
        for (String lib : manifest.getRequiredLibraries(manifestRoot)) {
            ISourceLocation jar = lib.startsWith("|") ? parseSourceLocation(lib) : URIUtil.getChildLocation(manifestRoot, lib);
            libsWriter.append(jar);
            
            if (loaderSchemes.contains(jar.getScheme())) {
                classloaders.append(jar);
            }
        }
        
        for (String srcName : manifest.getSourceRoots(manifestRoot)) {
            srcsWriter.append(URIUtil.getChildLocation(manifestRoot, srcName));
        }
        
        ISourceLocation bin = URIUtil.getChildLocation(manifestRoot, "bin");
        ISourceLocation boot = URIUtil.correctLocation("boot", "", "");
        
        libsWriter.insert(bin);
      
        // for the Rascal run-time
        classloaders.append(URIUtil.correctLocation("system", "", ""));
        
        return new PathConfig(
                srcsWriter.done(), 
                libsWriter.done(), 
                bin, 
                boot, 
                vf.list(), 
                getDefaultJavaCompilerPathList(), 
                classloaders.done());
	}
	
	public ISourceLocation getBoot() {
        return boot;
    }
	
	public ISourceLocation getRepo() {
	    return repo;
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
	    for(ISourceLocation dir : srcs){
	        if(modulePath.startsWith(dir.getPath()) && moduleLoc.getScheme() == dir.getScheme()){
	            String moduleName = modulePath.replaceFirst(dir.getPath(), "").replace(".rsc", "");
	            if(moduleName.startsWith("/")){
	                moduleName = moduleName.substring(1, moduleName.length());
	            }
	            return moduleName.replace("/", "::");
	        }
	    }
	    
	    for (ISourceLocation dir : libs) {
	        if(modulePath.startsWith(dir.getPath()) && moduleLoc.getScheme() == dir.getScheme()){
                String moduleName = modulePath.replaceFirst(dir.getPath(), "").replace(".tc", "");
                if(moduleName.startsWith("/")){
                    moduleName = moduleName.substring(1, moduleName.length());
                }
                return moduleName.replace("/", "::");
            }
	    }
	    
	    throw new IOException("No module name found for " + moduleLoc + "\n" + this);
	        
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
	
	public IConstructor asConstructor(IJava2Rascal j2r){
	    return j2r.pathConfig(
	        j2r.kw_pathConfig()
	        .srcs(getSrcs())
	        .libs(getLibs())
	        .boot(getBoot())
	        .bin(getBin())
	        .courses(getCourses())
	        .javaCompilerPath(getJavaCompilerPath())
	        .classloaders(getClassloaders())
	        );
	  }
	
	public String toString(){
	  StringWriter w = new StringWriter();
      w.append("srcs:      ").append(getSrcs().toString()).append("\n")
       .append("libs:      ").append(getLibs().toString()).append("\n")
       .append("courses:   ").append(getCourses().toString()).append("\n")
       .append("boot:      ").append(getBoot().toString()).append("\n")
       .append("bin:       ").append(getBin().toString()).append("\n")
       .append("classpath: ").append(getJavaCompilerPath().toString()).append("\n")
       .append("loaders:   ").append(getClassloaders().toString()).append("\n")
       ;
       
      return w.toString();
    }
}
