package org.meta_environment.rascal.interpreter;

import java.io.File;
import java.io.IOException;

import org.meta_environment.rascal.interpreter.asserts.ImplementationError;

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

	public static String getDefaultParsetableProperty() {
		try {
			return System.getProperty(DEFAULT_PARSETABLE_PROPERTY, new File("../rascal-grammar/spec/rascal.tbl").getCanonicalPath());
		} catch (IOException e) {
			throw new ImplementationError("unexpected error in impl");
		}
	}
	
	public static String getHeaderParsetableProperty() {
		try {
			return System.getProperty(HEADER_PARSETABLE_PROPERTY, new File("../rascal-grammar/spec/rascal-header.tbl").getCanonicalPath());
		} catch (IOException e) {
			throw new ImplementationError("unexpected error in impl");
		}
	}
	
	public static String getTableCacheDirectoryProperty() {
		return System.getProperty(CACHE_PATH_PROPERTY, System.getProperty("java.io.tmpdir"));
	}
	
	public static String getRascal2TableCommandProperty() {
		try {
			return System.getProperty(RASCAL2TABLE_PROPERTY, new File("../rascal-grammar/src/rascal2table").getCanonicalPath());
		} catch (IOException e) {
			throw new ImplementationError("unexpected error in impl");
		}
	}
	
	public static String getRascal2TableBinDirProperty() {
		try {
			return System.getProperty(RASCAL2TABLE_BINDIR_PROPERTY, new File("../rascal-grammar/src").getCanonicalPath());
		} catch (IOException e) {
			throw new ImplementationError("unexpected error in impl");
		}
	}

	public static String getSDFExtensionProperty() {
		return SDF_EXT;
	}

	public static String getSdfLibraryPathProperty() {
		try {
			return System.getProperty(SDF_LIBRARY_PATH_PROPERTY,new File("../sdf-library/library").getCanonicalPath());
		} catch (IOException e) {
			throw new ImplementationError("unexpected error in impl");
		}
	}
}
