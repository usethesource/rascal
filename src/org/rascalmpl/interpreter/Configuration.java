package org.rascalmpl.interpreter;


public class Configuration {
	public static final String RASCAL_FILE_EXT = ".rsc";
	public static final String RASCAL_BIN_FILE_EXT = ".bin";
	public static final String SDF_EXT = ".sdf";
	public static final String RASCAL_MODULE_SEP = "::";
	public static final String RASCAL_PATH_SEP = "/";
	
	private static final String RASCAL_JAVA_COMPILER_CLASSPATH = "rascal.java.classpath";
	private final static String PROFILING_PROPERTY = "rascal.profiling";
	private final static String TRACING_PROPERTY = "rascal.tracing";
	
	public static String getRascalJavaClassPathProperty() {
		String prop = System.getProperty(RASCAL_JAVA_COMPILER_CLASSPATH);
		if (prop == null) {
			prop = System.getProperty("java.class.path");
			System.setProperty(RASCAL_JAVA_COMPILER_CLASSPATH, prop);
		}
		return prop;
	}
	
	public static void setRascalJavaClassPathProperty(String path) {
		System.setProperty(RASCAL_JAVA_COMPILER_CLASSPATH, path);
	}
	
	public static boolean getProfilingProperty(){
		String profiling = System.getProperty(PROFILING_PROPERTY);
		if(profiling != null){
			return profiling.equals("true") ? true : false;
		}
		System.setProperty(PROFILING_PROPERTY, "false");
		return false;
	}
	
	public static boolean getTracingProperty(){
		String profiling = System.getProperty(TRACING_PROPERTY);
		if(profiling != null){
			return profiling.equals("true") ? true : false;
		}
		System.setProperty(TRACING_PROPERTY, "false");
		return false;
	}
}
