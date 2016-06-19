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
	
	List<ISourceLocation> srcLocs;		// List of locations to search for source files
	List<ISourceLocation> libLocs; 		// List of locations to search for derived files
	List<ISourceLocation> courseLocs; 	// List of locations to search for course source files
	ISourceLocation binLoc; 			// Global location for derived files outside projects
	ISourceLocation bootLoc;			// Location with Rascal boot files

	private RascalSearchPath rascalSearchPath;
	
	private static ISourceLocation defaultStdLoc;
	private static List<ISourceLocation> defaultCourseLocs;
	private static ISourceLocation defaultBootLoc;
	
	static {
		try {
			defaultStdLoc =  vf.sourceLocation("std", "", "");
			defaultCourseLocs = Arrays.asList(vf.sourceLocation("courses", "", ""));
			defaultBootLoc = vf.sourceLocation("boot+compressed", "", "");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PathConfig() throws URISyntaxException{
		srcLocs = Arrays.asList(defaultStdLoc);
		courseLocs = defaultCourseLocs;
		binLoc = vf.sourceLocation("home", "", "bin");
		libLocs = Arrays.asList(binLoc);
		bootLoc = defaultBootLoc;
		makeRascalSearchPath();
	}
	
	public PathConfig(List<ISourceLocation> srcLocs, List<ISourceLocation> libLocs, ISourceLocation binLoc) {
		this(srcLocs, libLocs, binLoc, defaultBootLoc, defaultCourseLocs);
	}
	
	public PathConfig(List<ISourceLocation> srcLocs, List<ISourceLocation> libLocs, ISourceLocation binLoc, List<ISourceLocation> courseLocs) {
		this(srcLocs, libLocs, binLoc, defaultBootLoc, courseLocs);
	}
		
	public PathConfig(List<ISourceLocation> srcLocs, List<ISourceLocation> libLocs, ISourceLocation binLoc, ISourceLocation bootLoc) {
		this(srcLocs, libLocs, binLoc, bootLoc, defaultCourseLocs);
	}
	
	public PathConfig(List<ISourceLocation> srcLocs, List<ISourceLocation> libLocs, ISourceLocation binLoc, ISourceLocation bootLoc, List<ISourceLocation> courseLocs){
		this.srcLocs = srcLocs;
		this.courseLocs = courseLocs;
		this.libLocs = libLocs;
		this.binLoc = binLoc;
		this.bootLoc = bootLoc;
		makeRascalSearchPath();
	}
	
	public PathConfig(IList srcLocs, IList libLocs, ISourceLocation binLoc, ISourceLocation bootLoc){
        this.srcLocs = convertLocs(srcLocs);
        this.libLocs = convertLocs(libLocs);
        this.binLoc = binLoc;
        this.bootLoc = bootLoc;
        this.courseLocs = defaultCourseLocs;
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcLocs, IList libLocs, ISourceLocation binLoc, IList courseLocs){
        this.srcLocs = convertLocs(srcLocs);
        this.libLocs = convertLocs(libLocs);
        this.binLoc = binLoc;
        this.bootLoc = defaultBootLoc;
        this.courseLocs = convertLocs(courseLocs);
        makeRascalSearchPath();
    }
	
	public PathConfig(IList srcLocs, IList libLocs, ISourceLocation binLoc, ISourceLocation bootLoc, IList courseLocs){
        this.srcLocs = convertLocs(srcLocs);
        this.libLocs = convertLocs(libLocs);
        this.binLoc = binLoc;
        this.bootLoc = bootLoc;
        this.courseLocs = convertLocs(courseLocs);
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
	
	public IList getSrcLocs() {
	    return vf.list(srcLocs.toArray(new IValue[0]));
	}
	
	public PathConfig addSourceLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedSrcPath = new ArrayList<ISourceLocation>(srcLocs);
		extendedSrcPath.add(dir);
		return new PathConfig(extendedSrcPath, libLocs, binLoc, bootLoc);
	}
	
	public IList getCourseLocs() {
	    return vf.list(courseLocs.toArray(new IValue[0]));
	}
	
	public PathConfig addCourseLoc(ISourceLocation dir) {
		List<ISourceLocation> extendedCoursePaths = new ArrayList<ISourceLocation>(courseLocs);
		extendedCoursePaths.add(dir);
		return new PathConfig(srcLocs, libLocs, binLoc, bootLoc, extendedCoursePaths);
	}
	
	public ISourceLocation getCourseLoc(String courseName) throws URISyntaxException, IOException{
		for(ISourceLocation dir : courseLocs){
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
		for(ISourceLocation dir : courseLocs){
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
	
	public IList getLibLocs() {
        return vf.list(libLocs.toArray(new IValue[0]));
    }
	
	public PathConfig addLibLoc(ISourceLocation dir){
		List<ISourceLocation> extendedlibLocs = new ArrayList<ISourceLocation>(libLocs);
		extendedlibLocs.add(dir);
		return new PathConfig(srcLocs, extendedlibLocs, binLoc, bootLoc);
	}
	
	public ISourceLocation getBootLoc() {
        return bootLoc;
    }
	
	public ISourceLocation getBinLoc() {
        return binLoc;
    }
	
	void makeRascalSearchPath(){
		this.rascalSearchPath = new RascalSearchPath();
		rascalSearchPath.addPathContributor(getSourcePathContributor());
	}

    public IRascalSearchPathContributor getSourcePathContributor() {
        return new PathContributor("srcPath", srcLocs);
    }
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	public RascalSearchPath getRascalSearchPath(){
		return rascalSearchPath;
	}

	ISourceLocation getModuleLoc(String qualifiedModuleName) throws IOException, URISyntaxException{
		String fileName = makeFileName(qualifiedModuleName);
		for(ISourceLocation dir : srcLocs){
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
