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
	
	List<ISourceLocation> srcs;		// List of locations to search for source files
	List<ISourceLocation> libs; 		// List of locations to search for derived files
	List<ISourceLocation> courses; 	// List of locations to search for course source files
	ISourceLocation bin; 			// Global location for derived files outside projects
	ISourceLocation boot;			// Location with Rascal boot files

	private RascalSearchPath rascalSearchPath;
	
	private static ISourceLocation defaultStdLoc;
	private static List<ISourceLocation> defaultcourses;
	private static ISourceLocation defaultboot;
	
	static {
		try {
			defaultStdLoc =  vf.sourceLocation("std", "", "");
			defaultcourses = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultboot = vf.sourceLocation("boot+compressed", "", "");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PathConfig() throws URISyntaxException{
		srcs = Arrays.asList(defaultStdLoc);
		courses = defaultcourses;
		bin = vf.sourceLocation("home", "", "bin");
		libs = Arrays.asList(bin);
		boot = defaultboot;
		makeRascalSearchPath();
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin) {
		this(srcs, libs, bin, defaultboot, defaultcourses);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, List<ISourceLocation> courses) {
		this(srcs, libs, bin, defaultboot, courses);
	}
		
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot) {
		this(srcs, libs, bin, boot, defaultcourses);
	}
	
	public PathConfig(List<ISourceLocation> srcs, List<ISourceLocation> libs, ISourceLocation bin, ISourceLocation boot, List<ISourceLocation> courses){
		this.srcs = srcs;
		this.courses = courses;
		this.libs = libs;
		this.bin = bin;
		this.boot = boot;
		makeRascalSearchPath();
	}
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = defaultcourses;
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, IList courses){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = defaultboot;
        this.courses = convertLocs(courses);
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcs, IList libs, ISourceLocation bin, ISourceLocation boot, IList courses){
        this.srcs = convertLocs(srcs);
        this.libs = convertLocs(libs);
        this.bin = bin;
        this.boot = boot;
        this.courses = convertLocs(courses);
        makeRascalSearchPath();
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
	
	public IList getSrcs() {
	    return vf.list(srcs.toArray(new IValue[0]));
	}
	
	public PathConfig addSourceLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedsrcs = new ArrayList<ISourceLocation>(srcs);
		extendedsrcs.add(dir);
		return new PathConfig(extendedsrcs, libs, bin, boot);
	}
	
	public IList getcourses() {
	    return vf.list(courses.toArray(new IValue[0]));
	}
	
	public PathConfig addCourseLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedcourses = new ArrayList<ISourceLocation>(courses);
		extendedcourses.add(dir);
		return new PathConfig(srcs, libs, bin, boot, extendedcourses);
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
		return new PathConfig(srcs, extendedlibs, bin, boot);
	}
	
	public ISourceLocation getboot() {
        return boot;
    }
	
	public ISourceLocation getBin() {
        return bin;
    }
	
	void makeRascalSearchPath(){
		this.rascalSearchPath = new RascalSearchPath();
		rascalSearchPath.addPathContributor(getSourcePathContributor());
	}

    public IRascalSearchPathContributor getSourcePathContributor() {
        return new PathContributor("srcs", srcs);
    }
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	public RascalSearchPath getRascalSearchPath(){
		return rascalSearchPath;
	}

	ISourceLocation getModuleLoc(String qualifiedModuleName) throws IOException, URISyntaxException{
		String fileName = makeFileName(qualifiedModuleName);
		for(ISourceLocation dir : srcs){
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
