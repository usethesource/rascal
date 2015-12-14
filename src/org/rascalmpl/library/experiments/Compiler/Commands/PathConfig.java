package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class PathConfig {
	
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	List<ISourceLocation> srcPath;		// List of directories to search for source files
	List<ISourceLocation> libPath; 		// List of directories to search source for derived files
	ISourceLocation binDir; 			// Global directory for derived files outside projects
	ISourceLocation bootDir;			// Directory with Rascal boot files

	private RascalSearchPath rascalSearchPath;
	
	
	public PathConfig(){

		try {
			ISourceLocation std = vf.sourceLocation("std", "", "");
			//ISourceLocation bootStdLib = vf.sourceLocation("boot", "", "stdlib");

			srcPath = Arrays.asList(std);
			binDir = vf.sourceLocation("home", "", "c1bin/stdlib");
			libPath = Arrays.asList(binDir);
			bootDir = vf.sourceLocation("boot+compressed", "", "");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		makeRascalSearchPath();
	}
	
	public PathConfig(List<ISourceLocation> srcPath, List<ISourceLocation> libPath, ISourceLocation binDir){
		this(srcPath, libPath, binDir, vf.sourceLocation("|boot+compressed:///|"));
	}
		
	public PathConfig(List<ISourceLocation> srcPath, List<ISourceLocation> libPath, ISourceLocation binDir, ISourceLocation bootDir){
		this.srcPath = srcPath;
		this.libPath = libPath;
		this.binDir = binDir;
		this.bootDir = bootDir;
		makeRascalSearchPath();
	}
	
	String makeFileName(String qualifiedModuleName) {
		return makeFileName(qualifiedModuleName, "rsc");
	}
	
	void makeRascalSearchPath(){
		this.rascalSearchPath = new RascalSearchPath();
		rascalSearchPath.addPathContributor(new PathContributor("srcPath", srcPath));
//		rascalSearchPath.addPathContributor(URIUtil.rootLocation("test-modules"));
//		rascalSearchPath.addPathContributor(StandardLibraryContributor.getInstance());
	}
	
	String makeFileName(String qualifiedModuleName, String extension) {
		return qualifiedModuleName.replaceAll("::", "/") + "." + extension;
	}
	
	public RascalSearchPath getRascalResolver(){
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
