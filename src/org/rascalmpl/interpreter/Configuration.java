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

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Path;

import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.interpreter.utils.Profiler.Fmt;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactParseError;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.UnexpectedTypeException;
import io.usethesource.vallang.impl.persistent.ValueFactory;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class Configuration {
	public static final String RASCAL_FILE_EXT = ".rsc";
	public static final String RASCAL_BIN_FILE_EXT = ".bin";
	public static final String SDF_EXT = ".sdf";
	public static final String RASCAL_MODULE_SEP = "::";
	public static final String RASCAL_PATH_SEP = "/";
	
	public final static String PROFILING_PROPERTY = "rascal.profiling";
	public final static String PROFILE_FMT_PROPERTY = "rascal.profileFmt";
	public final static String PROFILE_DIR_PROPERTY = "rascal.profileDir";
	public final static String GENERATOR_PROFILING_PROPERTY = "rascal.generatorProfiling";
	public final static String TRACING_PROPERTY = "rascal.tracing";
	public final static String ERRORS_PROPERTY = "rascal.errors";
	public final static String DEBUGGING_PROPERTY = "rascal.debugging";
	
  	private boolean profiling = getDefaultBoolean(PROFILING_PROPERTY, false);
  	private Fmt profileFmt = getDefaultProfileFmt(PROFILE_FMT_PROPERTY, Profiler.CPUPROFILE);
	private URI profileDir = getDefaultProfileDir(PROFILE_DIR_PROPERTY, Path.of("").toAbsolutePath().toUri()); // CWD
  	private boolean generatorProfiling = getDefaultBoolean(GENERATOR_PROFILING_PROPERTY, false);
  	private boolean tracing = getDefaultBoolean(TRACING_PROPERTY, false);
  	private boolean errors = getDefaultBoolean(ERRORS_PROPERTY, false);
	private boolean debugging = getDefaultBoolean(DEBUGGING_PROPERTY, false);

	private static boolean getDefaultBoolean(String property, boolean def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			return Boolean.parseBoolean("true");
		}
	}

	private static Fmt getDefaultProfileFmt(String property, Fmt def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			return toProfileFmt(parse(property, prop, TypeFactory.getInstance().stringType()));
		}
	}

	private static URI getDefaultProfileDir(String property, URI def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			return toProfileDir(parse(property, prop, TypeFactory.getInstance().sourceLocationType()));
		}
	}

    private static IValue parse(String key, String value, Type type) {
		value = value.trim();
		try {
            return new StandardTextReader().read(ValueFactory.getInstance(), type, new StringReader(value));
		} catch (FactParseError | FactTypeUseException e) {
			var expectedTypeHint = "`" + type.toString() + "`";
			if (expectedTypeHint.equals("`str`")) {
				expectedTypeHint += " (including quotation marks)";
			}

			var actualValueHint = "`" + value + "`";
			if (expectedTypeHint.startsWith("`str`") && (!value.startsWith("\"") || !value.endsWith("\""))) {
				actualValueHint += " (missing quotation marks?)";
			}

			var message = String.format(
				"Failed to parse the value of system property `%s`. Expected: Grammatical expression of type %s. Actual: %s.",
				key, expectedTypeHint, actualValueHint);
			
			throw new IllegalArgumentException(message, e);
		} catch (IOException e) { // Shouldn't happen
            throw new UncheckedIOException(e);
        }
    }

	public boolean getDebuggingProperty() {
		return debugging;
	}
	
	public boolean getProfilingProperty(){
		return profiling;
	}

	public Fmt getProfileFmtProperty() {
		return profileFmt;
	}

	public URI getProfileDirProperty() {
		return profileDir;
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

	public void setProfileFmt(Fmt profileFmt) {
		this.profileFmt = profileFmt;
	}

	public void setProfileDir(URI profileDir) {
		this.profileDir = profileDir;
	}
	
	public void setGeneratorProfiling(boolean profiling) {
		this.generatorProfiling = profiling;
	}
	
	public void setTracing(boolean tracing) {
	  this.tracing = tracing;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

    public static Fmt toProfileFmt(IValue v) {
        if (v instanceof IString) {
            switch (v.toString()) {
                case "\"folded\"":
                    return null;
                case "\"cpuprofile\"":
                    return Profiler.CPUPROFILE;
                default:
                    return null;
            }
        } else {
            throw new UnexpectedTypeException(TypeFactory.getInstance().stringType(), v.getType());
        }
    }

    public static URI toProfileDir(IValue v) {
        if (v instanceof ISourceLocation) {
            return URIResolverRegistry.getInstance().resolveIfLogical((ISourceLocation) v).getURI();
        } else {
            throw new UnexpectedTypeException(TypeFactory.getInstance().sourceLocationType(), v.getType());
        }
    }
}
