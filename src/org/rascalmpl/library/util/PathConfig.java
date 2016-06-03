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
	
	List<ISourceLocation> srcPath;		// List of directories to search for source files
	List<ISourceLocation> libPath; 		// List of directories to search source for derived files
	ISourceLocation binDir; 			// Global directory for derived files outside projects
	ISourceLocation bootDir;			// Directory with Rascal boot files

	private RascalSearchPath rascalSearchPath;
	
	public PathConfig() throws URISyntaxException{
		ISourceLocation std = vf.sourceLocation("std", "", "");

		srcPath = Arrays.asList(std);
		binDir = vf.sourceLocation("home", "", "bin");
		libPath = Arrays.asList(binDir);
		bootDir = vf.sourceLocation("boot+compressed", "", "");
	
		makeRascalSearchPath();
	}
	
	public PathConfig(List<ISourceLocation> srcPath, List<ISourceLocation> libPath, ISourceLocation binDir) throws URISyntaxException{
		this(srcPath, libPath, binDir, vf.sourceLocation("boot+compressed", "", ""));
	}
		
	public PathConfig(List<ISourceLocation> srcPath, List<ISourceLocation> libPath, ISourceLocation binDir, ISourceLocation bootDir){
		this.srcPath = srcPath;
		this.libPath = libPath;
		this.binDir = binDir;
		this.bootDir = bootDir;
		makeRascalSearchPath();
	}
	
	public PathConfig(IList srcPath, IList libPath, ISourceLocation binDir, ISourceLocation bootDir){
        this.srcPath = convertPath(srcPath);
        this.libPath = convertPath(libPath);
        this.binDir = binDir;
        this.bootDir = bootDir;
        makeRascalSearchPath();
    }
	
	List<ISourceLocation> convertPath(IList path){
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
	
	public IList getSrcPath() {
	    return vf.list(srcPath.toArray(new IValue[0]));
	}
	
	public PathConfig addSourcePath(ISourceLocation dir) {
		List<ISourceLocation> extendedSrcPath = new ArrayList<ISourceLocation>(srcPath);
		extendedSrcPath.add(dir);
		return new PathConfig(extendedSrcPath, libPath, binDir, bootDir);
	}
	
	
	public IList getLibPath() {
        return vf.list(libPath.toArray(new IValue[0]));
    }
	
	public PathConfig addLibPath(ISourceLocation dir){
		List<ISourceLocation> extendedLibPath = new ArrayList<ISourceLocation>(libPath);
		extendedLibPath.add(dir);
		return new PathConfig(srcPath, extendedLibPath, binDir, bootDir);
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
        return new PathContributor("srcPath", srcPath);
    }
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	public RascalSearchPath getRascalSearchPath(){
		return rascalSearchPath;
	}

	ISourceLocation getModuleLocation(String qualifiedModuleName) throws IOException, URISyntaxException{
		String fileName = makeFileName(qualifiedModuleName);
		for(ISourceLocation dir : srcPath){
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
