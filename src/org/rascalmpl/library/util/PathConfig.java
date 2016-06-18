package org.rascalmpl.library.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class PathConfig {
	
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	List<ISourceLocation> srcPaths;		// List of directories to search for source files
	List<ISourceLocation> libPaths; 	// List of directories to search for derived files
	List<ISourceLocation> coursePaths; 	// List of directories to search for course source files
	ISourceLocation binDir; 			// Global directory for derived files outside projects
	ISourceLocation bootDir;			// Directory with Rascal boot files

	private RascalSearchPath rascalSearchPath;
	
	private static ISourceLocation defaultStdLocation;
	private static List<ISourceLocation> defaultCoursePaths;
	private static ISourceLocation defaultBootDir;
	
	static {
		try {
			defaultStdLocation =  vf.sourceLocation("std", "", "");
			defaultCoursePaths = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultBootDir = vf.sourceLocation("boot+compressed", "", "");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PathConfig() throws URISyntaxException{
		srcPaths = Arrays.asList(defaultStdLocation);
		coursePaths = defaultCoursePaths;
		binDir = vf.sourceLocation("home", "", "bin");
		libPaths = Arrays.asList(binDir);
		bootDir = defaultBootDir;
		makeRascalSearchPath();
	}
	
	public PathConfig(List<ISourceLocation> srcPaths, List<ISourceLocation> libPaths, ISourceLocation binDir) {
		this(srcPaths, libPaths, binDir, defaultBootDir, defaultCoursePaths);
	}
	
	public PathConfig(List<ISourceLocation> srcPaths, List<ISourceLocation> libPaths, ISourceLocation binDir, List<ISourceLocation> coursePaths) {
		this(srcPaths, libPaths, binDir, defaultBootDir, coursePaths);
	}
		
	public PathConfig(List<ISourceLocation> srcPaths, List<ISourceLocation> libPaths, ISourceLocation binDir, ISourceLocation bootDir) {
		this(srcPaths, libPaths, binDir, bootDir, defaultCoursePaths);
	}
	
	public PathConfig(List<ISourceLocation> srcPaths, List<ISourceLocation> libPaths, ISourceLocation binDir, ISourceLocation bootDir, List<ISourceLocation> coursePaths){
		this.srcPaths = srcPaths;
		this.coursePaths = coursePaths;
		this.libPaths = libPaths;
		this.binDir = binDir;
		this.bootDir = bootDir;
		makeRascalSearchPath();
	}
	
	public PathConfig(IList srcPaths, IList libPaths, ISourceLocation binDir, ISourceLocation bootDir){
        this.srcPaths = convertPaths(srcPaths);
        this.libPaths = convertPaths(libPaths);
        this.binDir = binDir;
        this.bootDir = bootDir;
        this.coursePaths = defaultCoursePaths;
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcPaths, IList libPaths, ISourceLocation binDir, IList coursePaths){
        this.srcPaths = convertPaths(srcPaths);
        this.libPaths = convertPaths(libPaths);
        this.binDir = binDir;
        this.bootDir = defaultBootDir;
        this.coursePaths = convertPaths(coursePaths);
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcPaths, IList libPaths, ISourceLocation binDir, ISourceLocation bootDir, IList coursePaths){
        this.srcPaths = convertPaths(srcPaths);
        this.libPaths = convertPaths(libPaths);
        this.binDir = binDir;
        this.bootDir = bootDir;
        this.coursePaths = convertPaths(coursePaths);
        makeRascalSearchPath();
    }
	
	List<ISourceLocation> convertPaths(IList path){
		List<ISourceLocation> result = new ArrayList<>();
		for(IValue p : path){
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
	
	public IList getSrcPaths() {
	    return vf.list(srcPaths.toArray(new IValue[0]));
	}
	
	public PathConfig addSourcePath(ISourceLocation dir) {
		List<ISourceLocation> extendedSrcPath = new ArrayList<ISourceLocation>(srcPaths);
		extendedSrcPath.add(dir);
		return new PathConfig(extendedSrcPath, libPaths, binDir, bootDir);
	}
	
	public IList getCoursePaths() {
	    return vf.list(coursePaths.toArray(new IValue[0]));
	}
	
	public PathConfig addCoursePath(ISourceLocation dir) {
		List<ISourceLocation> extendedCoursePaths = new ArrayList<ISourceLocation>(coursePaths);
		extendedCoursePaths.add(dir);
		return new PathConfig(srcPaths, libPaths, binDir, bootDir, extendedCoursePaths);
	}
	
	public ISourceLocation getCourseLocation(String courseName) throws URISyntaxException, IOException{
		for(ISourceLocation dir : coursePaths){
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
		ArrayList<String> courses = new ArrayList<>();
		for(ISourceLocation dir : coursePaths){
			if(reg.exists(dir)){
				for(ISourceLocation entry : reg.list(dir)){
					if(reg.isDirectory(entry)){
						String name = fileName(entry);
						if(isCourse(name)){
							courses.add(name);
						}
					}
				}
			}
		}
		return courses;
	}
	
	public IList getLibPaths() {
        return vf.list(libPaths.toArray(new IValue[0]));
    }
	
	public PathConfig addLibPath(ISourceLocation dir){
		List<ISourceLocation> extendedLibPath = new ArrayList<ISourceLocation>(libPaths);
		extendedLibPath.add(dir);
		return new PathConfig(srcPaths, extendedLibPath, binDir, bootDir);
	}
	
	public ISourceLocation getBootDir() {
        return bootDir;
    }
	
	public ISourceLocation getBinDir() {
        return binDir;
    }
	
	void makeRascalSearchPath(){
		this.rascalSearchPath = new RascalSearchPath();
		rascalSearchPath.addPathContributor(getSourcePathContributor());
	}

    public IRascalSearchPathContributor getSourcePathContributor() {
        return new PathContributor("srcPath", srcPaths);
    }
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	public RascalSearchPath getRascalSearchPath(){
		return rascalSearchPath;
	}

	ISourceLocation getModuleLocation(String qualifiedModuleName) throws IOException, URISyntaxException{
		String fileName = makeFileName(qualifiedModuleName);
		for(ISourceLocation dir : srcPaths){
			ISourceLocation fileLoc = vf.sourceLocation(dir.getScheme(), dir.getAuthority(), dir.getPath() + fileName);
			if(URIResolverRegistry.getInstance().exists(fileLoc)){
		    	return fileLoc;
		    }
		}
		throw new IOException("Module " + qualifiedModuleName + " not found");
	}
	
	public List<String> listModuleEntries(String qualifier) {
		return rascalSearchPath.listModuleEntries(qualifier);
	}
	
}

class PathContributor implements IRascalSearchPathContributor{
	
	private final String name;
	private final List<ISourceLocation> stdPath;

	PathContributor(String name, List<ISourceLocation> path){
		this.name = name;
		this.stdPath = path;
	}
	@Override
	public void contributePaths(List<ISourceLocation> path) {
		for(ISourceLocation p : stdPath){
			path.add(p);
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
}
