package org.rascalmpl.library.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.library.lang.rascal.boot.IJava2Rascal;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class PathConfig {
	
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final List<ISourceLocation> srcs;		// List of locations to search for source files
	private final List<ISourceLocation> libs;     // List of (library) locations to search for derived files
	private final List<ISourceLocation> courses; 	// List of (library) locations to search for course source files
	private final List<ISourceLocation> javaCompilerPath;     // List of (library) locations to search for course source files
	private final List<ISourceLocation> classloaders;     // List of (library) locations to search for course source files
    
	private ISourceLocation bin; 			// Global location for derived files outside projects or libraries
	private ISourceLocation boot;			// Location with Rascal boot files

	private static ISourceLocation defaultStd;
	private static List<ISourceLocation> defaultCourses;
	private static List<ISourceLocation> defaultJavaCompilerPath;
	private static List<ISourceLocation> defaultClassloaders;
	private static ISourceLocation defaultBin;
	private static ISourceLocation defaultBoot;
	
	static {
		try {
		    // Defaults should be in sync with util::Reflective
			defaultStd =  vf.sourceLocation("std", "", "");
			defaultBin = vf.sourceLocation("home", "", "bin");
			defaultBoot = vf.sourceLocation("boot", "", "");
			defaultCourses = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultJavaCompilerPath = computeDefaultJavaCompilerPath();
			defaultClassloaders = computeDefaultClassLoaders();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public PathConfig() throws URISyntaxException{
		srcs = Arrays.asList(defaultStd);
		courses = defaultCourses;
		bin = defaultBin;
		boot = defaultBoot;
		libs = Arrays.asList(bin, boot);
		javaCompilerPath = defaultJavaCompilerPath;
		classloaders = defaultClassloaders;
	}
	
	private static List<ISourceLocation> computeDefaultClassLoaders() {
	    List<ISourceLocation> result = new ArrayList<>();
        String javaClasspath = System.getProperty("java.class.path");
        if (javaClasspath != null) {
            for (String path : javaClasspath.split(":")) {
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
            for (String path : classPath.split(":")) {
                result.add(vf.sourceLocation(new File(path).getAbsolutePath()));
            }
        }
        
        return result;
    }

    public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin) {
		this(srcs, libs, bin, defaultBoot);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot) {
		this(srcs, libs, bin, boot, defaultCourses);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses) {
	    this(srcs, libs, bin, boot, courses, defaultJavaCompilerPath);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath) {
        this(srcs, libs, bin, boot, courses, javaCompilerPath, defaultClassloaders);
    }
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses, List<ISourceLocation> javaCompilerPath, List<ISourceLocation> classloaders){
		this.srcs = srcs;
		this.courses = courses;
		this.libs = libs;
		this.bin = bin;
		this.boot = boot;
		this.javaCompilerPath = javaCompilerPath;
		this.classloaders = classloaders;
	}
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = defaultCourses;
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = defaultBoot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = defaultJavaCompilerPath;
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses, IList javaCompilerPath){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = convertLocs(javaCompilerPath);
        this.classloaders = defaultClassloaders;
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses, IList javaCompilerPath, IList classloaders){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        this.javaCompilerPath = convertLocs(javaCompilerPath);
        this.classloaders = convertLocs(classloaders);
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
		return new PathConfig(extendedsrcs, libs, bin, boot, courses, javaCompilerPath, classloaders);
	}
	
	public PathConfig addJavaCompilerPath(ISourceLocation dir) {
	    List<ISourceLocation> extended = new ArrayList<ISourceLocation>(javaCompilerPath);
        extended.add(dir);
        return new PathConfig(srcs, libs, bin, boot, courses, extended, classloaders);
	}
	
	public PathConfig addClassloader(ISourceLocation dir) {
        List<ISourceLocation> extended = new ArrayList<ISourceLocation>(classloaders);
        extended.add(dir);
        return new PathConfig(srcs, libs, bin, boot, courses, javaCompilerPath, extended);
    }
	
	public IList getCourses() {
	    return vf.list(courses.toArray(new IValue[0]));
	}
	
	public PathConfig addCourseLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedcourses = new ArrayList<ISourceLocation>(courses);
		extendedcourses.add(dir);
		return new PathConfig(srcs, libs, bin, boot, extendedcourses, javaCompilerPath, classloaders);
	}
	
	public ISourceLocation getCourseLoc(String courseName) throws URISyntaxException, IOException{
		for(ISourceLocation dir : courses){
			ISourceLocation fileLoc = vf.sourceLocation(dir.getScheme(), dir.getAuthority(), dir.getPath() + courseName);
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
	
	public PathConfig addLibLoc(ISourceLocation dir){
		List<ISourceLocation> extendedlibs = new ArrayList<ISourceLocation>(libs);
		extendedlibs.add(dir);
		return new PathConfig(srcs, extendedlibs, bin, boot, courses, javaCompilerPath, classloaders);
	}
	
	public ISourceLocation getBoot() {
        return boot;
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
	
	String getModuleName(ISourceLocation moduleLoc) throws IOException{
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
	    
	    throw new IOException("No module name found for " + moduleLoc);
	        
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
      w.append("srcs:    ").append(getSrcs().toString()).append("\n")
       .append("libs:    ").append(getLibs().toString()).append("\n")
       .append("courses: ").append(getCourses().toString()).append("\n")
       .append("boot:    ").append(getBoot().toString()).append("\n")
       .append("bin:     ").append(getBin().toString()).append("\n")
       .append("clsspath:").append(getJavaCompilerPath().toString()).append("\n")
       .append("loaders: ").append(getClassloaders().toString()).append("\n")
       ;
       
      return w.toString();
    }
}
