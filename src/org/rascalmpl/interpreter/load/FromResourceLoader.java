package org.rascalmpl.interpreter.load;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.io.PBFWriter;

public class FromResourceLoader implements IModuleFileLoader{
	private final String sourceFolder;
	private final Class<?> clazz;

	/**
	 * Creates a loader that will find modules in the resources
	 * of the given class
	 * 
	 * @param clazz
	 */
	public FromResourceLoader(Class<?> clazz) {
		this(clazz, "");
	}
	
	/**
	 * Creates a loader that will find modules in the resources
	 * of the given class. The given sourceFolder is where the search
	 * for modules in the class's resources will start.
	 * 
	 * @param clazz
	 */
	public FromResourceLoader(Class<?> clazz, String sourceFolder) {
		this.clazz = clazz;
		
		if (!sourceFolder.startsWith("/")) {
			sourceFolder = "/" + sourceFolder;
		}
		if (!sourceFolder.endsWith("/")) {
			sourceFolder = sourceFolder + "/";
		}
		this.sourceFolder = sourceFolder;
	}
	
	public boolean fileExists(String filename){
		try{
			URL url = clazz.getResource(sourceFolder + filename);
			return (url != null);
		}catch(RuntimeException rex){
			return false;
		}
	}

	public InputStream getInputStream(String filename){
		try{
			URL url = clazz.getResource(sourceFolder + filename);
			if(url != null){
				return url.openStream();
			}
		}catch(IOException ioex){
			// Ignore, this is fine.
		}
		
		return null;
	}
	
	public boolean supportsLoadingBinaries(){
		return true;
	}
	
	public boolean tryWriteBinary(String filename, String binaryName, IConstructor tree){
		// Temporary workaround for generating binaries.
		// Generating binaries should not be supported by resource loaders.
		File binFile;
		URL url = clazz.getResource(sourceFolder + filename);
		if(url == null) return false;
		
		String binFileLocation = url.getFile();
		if(binFileLocation.length() == 0) return false;
		
		binFile = new File(binFileLocation+".bin");
		try{
			binFile.createNewFile();
		}catch(IOException ioex){
			return false;
		}
		if(!binFile.canWrite()) return false;
		
		BufferedOutputStream outputStream = null;
		
		PBFWriter pbfWriter = new PBFWriter();
		try{
			outputStream = new BufferedOutputStream(new FileOutputStream(binFile));
			pbfWriter.write(tree, outputStream);
			return true;
		}catch(IOException ioex){
			ioex.printStackTrace();
		}finally{
			if(outputStream != null){
				try{
					outputStream.flush();
				}catch(IOException ioex){
					ioex.printStackTrace();
				}
				
				try{
					outputStream.close();
				}catch(IOException ioex){
					ioex.printStackTrace();
				}
			}
		}
		return false;
	}
}
