package org.meta_environment.rascal.interpreter;

public class Configuration {
	public static final String RASCAL_FILE_EXT = ".rsc";
	public static final String RASCAL_BIN_FILE_EXT = ".rsc.bin";
	public static final String SDF_EXT = ".sdf";
	
	private final static String DEFAULT_PARSETABLE_PROPERTY = "rascal.parsetable.default.file";
	private final static String HEADER_PARSETABLE_PROPERTY = "rascal.parsetable.header.file";
	private final static String CACHE_PATH_PROPERTY = "rascal.parsetable.cache.dir";
	private final static String RASCAL2TABLE_PROPERTY = "rascal.rascal2table.command";
	private final static String RASCAL2TABLE_BINDIR_PROPERTY = "rascal.rascal2table.dir";

	public static String getDefaultParsetableProperty() {
		return System.getProperty(DEFAULT_PARSETABLE_PROPERTY, "../rascal-grammar/spec/rascal.tbl");
	}
	
	public static String getHeaderParsetableProperty() {
		return System.getProperty(HEADER_PARSETABLE_PROPERTY, "../rascal-grammar/spec/rascal-header.tbl");
	}
	
	public static String getTableCacheDirectoryProperty() {
		return System.getProperty(CACHE_PATH_PROPERTY, System.getProperty("java.io.tmpdir"));
	}
	
	public static String getRascal2TableCommandProperty() {
		return System.getProperty(RASCAL2TABLE_PROPERTY, "../rascal-grammar/src/rascal2table");
	}
	
	public static String getRascal2TableBinDirProperty() {
		return System.getProperty(RASCAL2TABLE_BINDIR_PROPERTY, "../rascal-grammar/src");
	}

	public static String getSDFExtensionProperty() {
		return SDF_EXT;
	}
}
