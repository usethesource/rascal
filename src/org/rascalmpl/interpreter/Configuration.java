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
import java.nio.file.Path;

import org.rascalmpl.interpreter.utils.profiler.CpuprofileFormatter;
import org.rascalmpl.interpreter.utils.profiler.DummyFormatter;
import org.rascalmpl.interpreter.utils.profiler.FoldedFormatter;
import org.rascalmpl.interpreter.utils.profiler.Formatter;
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
  	private Formatter profileFmt = getDefaultProfileFmt(PROFILE_FMT_PROPERTY, new CpuprofileFormatter());
	private Path profileDir = getDefaultProfileDir(PROFILE_DIR_PROPERTY, Path.of("").toAbsolutePath()); // CWD
  	private boolean generatorProfiling = getDefaultBoolean(GENERATOR_PROFILING_PROPERTY, false);
  	private boolean tracing = getDefaultBoolean(TRACING_PROPERTY, false);
  	private boolean errors = getDefaultBoolean(ERRORS_PROPERTY, false);
	private boolean debugging = getDefaultBoolean(DEBUGGING_PROPERTY, false);

	public static boolean getDefaultBoolean(String property, boolean def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			try {
				return toBoolean(parseIValue(TypeFactory.getInstance().boolType(), prop));
			} catch (FactParseError | FactTypeUseException e) {
				var message = "Failed to parse the value of system property `%s`. Expected: Grammatical expression of type `bool`. Actual: `%s`.";
				throw new IllegalArgumentException(String.format(message, property, prop), e);
			} catch (IOException e) { // Shouldn't happen
				throw new UncheckedIOException(e);
			}
		}
	}

	public static Formatter getDefaultProfileFmt(String property, Formatter def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			try {
				return toProfileFmt(parseIValue(TypeFactory.getInstance().stringType(), prop));
			} catch (FactParseError | FactTypeUseException e) {
				var message = "Failed to parse the value of system property `%s`. Expected: Grammatical expression of type `str` (including quotation marks). Actual: `%s`.";
				throw new IllegalArgumentException(String.format(message, property, prop), e);
			} catch (IOException e) { // Shouldn't happen
				throw new UncheckedIOException(e);
			}
		}
	}

	public static Path getDefaultProfileDir(String property, Path def) {
		String prop = System.getProperty(property);
		if (prop == null) {
			return def;
		}
		else {
			try {
				return toProfileDir(parseIValue(TypeFactory.getInstance().sourceLocationType(), prop));
			} catch (FactParseError | FactTypeUseException e) {
				var message = "Failed to parse the value of system property `%s`. Expected: Grammatical expression of type `loc`. Actual: `%s`.";
				throw new IllegalArgumentException(String.format(message, property, prop), e);
			} catch (IOException e) { // Shouldn't happen
				throw new UncheckedIOException(e);
			}
		}
	}

	private static IValue parseIValue(Type type, String value) throws FactParseError, FactTypeUseException, IOException {
		return new StandardTextReader().read(ValueFactory.getInstance(), type, new StringReader(value));
	} 

	public boolean getDebuggingProperty() {
		return debugging;
	}
	
	public boolean getProfilingProperty(){
		return profiling;
	}

	public Formatter getProfileFmtProperty() {
		return profileFmt;
	}

	public Path getProfileDirProperty() {
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

	public void setProfileFmt(Formatter profileFmt) {
		this.profileFmt = profileFmt;
	}

	public void setProfileDir(Path profileDir) {
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

	public static boolean toBoolean(IValue value) {
		return Boolean.parseBoolean(value.toString());
	}

    public static Formatter toProfileFmt(IValue v) {
        if (v instanceof IString) {
            switch (v.toString()) {
				case "\"none\"":
                    return new DummyFormatter();
                case "\"folded\"":
                    return new FoldedFormatter();
                case "\"cpuprofile\"":
                    return new CpuprofileFormatter();
                default:
                    return new DummyFormatter();
            }
        } else {
            throw new UnexpectedTypeException(TypeFactory.getInstance().stringType(), v.getType());
        }
    }

    public static Path toProfileDir(IValue v) {
        if (v instanceof ISourceLocation) {
            return Path.of(URIResolverRegistry.getInstance().resolveIfLogical((ISourceLocation) v).getURI());
        } else {
            throw new UnexpectedTypeException(TypeFactory.getInstance().sourceLocationType(), v.getType());
        }
    }
}
