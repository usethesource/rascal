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
	
	public final static String PROFILING_PROPERTY = "rascal.profiling";
	public final static String GENERATOR_PROFILING_PROPERTY = "rascal.generatorProfiling";
	public final static String TRACING_PROPERTY = "rascal.tracing";
	public final static String ERRORS_PROPERTY = "rascal.errors";
	
  	private boolean profiling = getDefaultBoolean(PROFILING_PROPERTY, false);
  	private boolean generatorProfiling = getDefaultBoolean(GENERATOR_PROFILING_PROPERTY, false);
  	private boolean tracing = getDefaultBoolean(TRACING_PROPERTY, false);
  	private boolean errors = getDefaultBoolean(ERRORS_PROPERTY, false);
	
	private static boolean getDefaultBoolean(String property, boolean def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
		return prop.equals("true");
		}
  	}
	
	public boolean getProfilingProperty(){
		return profiling;
	}
	
	public boolean getGeneratorProfilingProperty(){
		return generatorProfiling;
	}
	
	public boolean getTracingProperty(){
		return tracing;
	}
	
	public boolean printErrors(){
		return errors;
	}
	
	public void setErrors(boolean errors) {
    	this.errors = errors;
  	}
	
	public void setProfiling(boolean profiling) {
	  	this.profiling = profiling;
	}
	
	public void setGeneratorProfiling(boolean profiling) {
		this.generatorProfiling = profiling;
	}
	
	public void setTracing(boolean tracing) {
	  this.tracing = tracing;
	}
}
