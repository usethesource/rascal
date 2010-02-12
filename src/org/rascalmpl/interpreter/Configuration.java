package org.rascalmpl.interpreter;

import java.io.File;
import java.io.IOException;

import org.rascalmpl.interpreter.asserts.ImplementationError;

public class Configuration {
	public static final String RASCAL_FILE_EXT = ".rsc";
	public static final String RASCAL_BIN_FILE_EXT = ".rsc.bin";
	public static final String SDF_EXT = ".sdf";
	
	private final static String DEFAULT_PARSETABLE_PROPERTY = "rascal.parsetable.default.file";
	private final static String HEADER_PARSETABLE_PROPERTY = "rascal.parsetable.header.file";
	private final static String CACHE_PATH_PROPERTY = "rascal.parsetable.cache.dir";
	private final static String RASCAL2TABLE_PROPERTY = "rascal.rascal2table.command";
	private final static String RASCAL2TABLE_BINDIR_PROPERTY = "rascal.rascal2table.dir";
	private final static String SDF_LIBRARY_PATH_PROPERTY = "rascal.sdf.library.dir";
	private static final String SDF2TABLE_PROPERTY = "rascal.sdf.sdf2table.command";
	private final static String PROFILING_PROPERTY = "rascal.profiling";
	
	private volatile static String basePath = null;
	
	public static void setBasePath(String path){
		if(path == null){
			basePath = null;
			return;
		}
			
		if(path.endsWith("/")) basePath = path;
		else basePath = path+"/";
	}

	public static String getDefaultParsetableProperty(){
		String defaultParsetableProperty = System.getProperty(DEFAULT_PARSETABLE_PROPERTY);
		if(defaultParsetableProperty != null){
			return defaultParsetableProperty;
		}
		
		try{
			if(basePath != null){
				defaultParsetableProperty = new File(basePath+"rascal-grammar/spec/rascal.tbl").getCanonicalPath();
			}else{
				defaultParsetableProperty = new File("../rascal-grammar/spec/rascal.tbl").getCanonicalPath();
			}
			
			System.setProperty(DEFAULT_PARSETABLE_PROPERTY, defaultParsetableProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return defaultParsetableProperty;
	}
	
	public static String getHeaderParsetableProperty(){
		String headerParsetableProperty = System.getProperty(HEADER_PARSETABLE_PROPERTY);
		if(headerParsetableProperty != null){
			return headerParsetableProperty;
		}
		
		try{
			if(basePath != null){
				headerParsetableProperty = new File(basePath+"rascal-grammar/spec/rascal-header.tbl").getCanonicalPath();
			}else{
				headerParsetableProperty = new File("../rascal-grammar/spec/rascal-header.tbl").getCanonicalPath();
			}
			
			System.setProperty(HEADER_PARSETABLE_PROPERTY, headerParsetableProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return headerParsetableProperty;
	}
	
	public static String getTableCacheDirectoryProperty(){
		return System.getProperty(CACHE_PATH_PROPERTY, System.getProperty("java.io.tmpdir"));
	}
	
	
	public static String getSdf2TableCommandProperty() {
		String sdf2TableCommandProperty = System.getProperty(SDF2TABLE_PROPERTY);
		if(sdf2TableCommandProperty != null){
			return sdf2TableCommandProperty;
		}
		
		try{
			if(basePath != null){
				sdf2TableCommandProperty = new File(basePath+"pgen/src/sdf2table").getCanonicalPath();
			}else{
				sdf2TableCommandProperty = new File("../pgen/src/sdf2table").getCanonicalPath();
			}
			
			System.setProperty(SDF2TABLE_PROPERTY, sdf2TableCommandProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return sdf2TableCommandProperty;
	}

	
	public static String getRascal2TableCommandProperty(){
		String rascal2TableCommandProperty = System.getProperty(RASCAL2TABLE_PROPERTY);
		if(rascal2TableCommandProperty != null){
			return rascal2TableCommandProperty;
		}
		
		try{
			if(basePath != null){
				rascal2TableCommandProperty = new File(basePath+"rascal-grammar/src/rascal2table").getCanonicalPath();
			}else{
				rascal2TableCommandProperty = new File("../rascal-grammar/src/rascal2table").getCanonicalPath();
			}
			
			System.setProperty(RASCAL2TABLE_PROPERTY, rascal2TableCommandProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return rascal2TableCommandProperty;
	}
	
	public static String getRascal2TableBinDirProperty(){
		String rascal2TableBinDirProperty = System.getProperty(RASCAL2TABLE_BINDIR_PROPERTY);
		if(rascal2TableBinDirProperty != null){
			return rascal2TableBinDirProperty;
		}
		
		try{
			if(basePath != null){
				rascal2TableBinDirProperty = new File(basePath+"rascal-grammar/src").getCanonicalPath();
			}else{
				rascal2TableBinDirProperty = new File("../rascal-grammar/src").getCanonicalPath();
			}
			
			System.setProperty(RASCAL2TABLE_BINDIR_PROPERTY, rascal2TableBinDirProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return rascal2TableBinDirProperty;
	}

	public static String getSDFExtensionProperty() {
		return SDF_EXT;
	}

	public static String getSdfLibraryPathProperty() {
		String sdfLibraryPathProperty = System.getProperty(SDF_LIBRARY_PATH_PROPERTY);
		if(sdfLibraryPathProperty != null){
			return sdfLibraryPathProperty;
		}
		
		try{
			if(basePath != null){
				sdfLibraryPathProperty = new File(basePath+"sdf-library/library").getCanonicalPath();
			}else{
				sdfLibraryPathProperty = new File("../sdf-library/library").getCanonicalPath();
			}
			
			System.setProperty(SDF_LIBRARY_PATH_PROPERTY, sdfLibraryPathProperty);
		}catch(IOException ioex){
			throw new ImplementationError("unexpected error in impl", ioex);
		}
		
		return sdfLibraryPathProperty;
	}
	
	public static boolean getProfilingProperty(){
		String profiling = System.getProperty(PROFILING_PROPERTY);
		if(profiling != null){
			return profiling.equals("true") ? true : false;
		}
		System.setProperty(PROFILING_PROPERTY, "false");
		return false;
	}

}
