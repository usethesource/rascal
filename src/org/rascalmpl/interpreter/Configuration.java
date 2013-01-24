/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
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
	private final static String ERRORS_PROPERTY = "rascal.errors";
	
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
		String tracing = System.getProperty(TRACING_PROPERTY);
		if(tracing != null){
			return tracing.equals("true") ? true : false;
		}
		System.setProperty(TRACING_PROPERTY, "false");
		return false;
	}
	
	public static boolean printErrors(){
		String printErrors = System.getProperty(ERRORS_PROPERTY);
		if(printErrors != null){
			return printErrors.equals("true") ? true : false;
		}
		return false;
	}
}
