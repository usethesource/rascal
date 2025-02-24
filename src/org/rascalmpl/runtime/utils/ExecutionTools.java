/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.runtime.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.rascalmpl.exceptions.JavaCompilation;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.classloaders.PathConfigClassLoader;

import io.usethesource.vallang.IValue;

public class ExecutionTools<T> {
	private JavaCompiler<T> javaCompiler;
	private Map<String, Class<T>> classCache;

	ExecutionTools(org.rascalmpl.library.util.PathConfig pcfg){
		PathConfigClassLoader loader = null; //new PathConfigClassLoader(pcfg, null);
		this.javaCompiler = new JavaCompiler<T>(loader, null, null);
		classCache = new HashMap<>();
	}
	
	String interfaceName(String className) {
		int idx = className.lastIndexOf(".");
		if(idx < 0) return "$" + className;
		return className.substring(0, idx) + ".$" + className.substring(idx+1);
	}
	
	public Class<T> compile(String className, String interfaceModule, String classModule, final DiagnosticCollector<JavaFileObject> diagnostics){
		try {
			Map<String, CharSequence> classes = new HashMap<>();
			String interfaceName = interfaceName(className);
			classes.put(interfaceName, interfaceModule);
			classes.put(className,  classModule);
			Map<String, Class<T>> clazzes = javaCompiler.compile(classes, diagnostics);
			Class<T> the_class = clazzes.get(className);
			Class<T> the_interface = clazzes.get(interfaceName);
			classCache.put(className,  the_class);
			classCache.put(interfaceName, the_interface);
			return the_class;
		} catch (JavaCompilerException e) {
			if (!e.getDiagnostics().getDiagnostics().isEmpty()) {
		        Diagnostic<? extends JavaFileObject> msg = e.getDiagnostics().getDiagnostics().iterator().next();
		        throw new JavaCompilation(msg.getMessage(null), msg.getLineNumber(), msg.getColumnNumber(), interfaceModule, classModule, e);
		    }
		    else {
		        throw new JavaCompilation(e.getMessage(), 0, 0, interfaceModule, classModule, e);
		    }
		}
	}
	
	public IValue executeProgram(String className, Map<String, IValue> keywordArguments){
		Class<?> clazz = classCache.get(className);
		if(clazz == null) {
			throw new RuntimeException("Not available: " + className);
		}
		Method m;
		try {
			m = clazz.getMethod("main", new Class[] { String[].class });
			Object[] _args = new Object[] { new String[0] };
			try {
				return (IValue) m.invoke(null, _args);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		String iface =   "public interface $Test { }";
		String program = "public class Test implements $Test {" + "   public static void main (String [] args){"
				       + "      System.out.println (\"Hello, World\");"
				       + "      System.out.println (args.length);" + "   }" 
				       + "}";

		org.rascalmpl.library.util.PathConfig pcfg = new PathConfig();
		
		ExecutionTools<?> exec = new ExecutionTools<>(pcfg);

		exec.compile("Test", iface, program, null);
		
		exec.executeProgram("Test", null);
	}
}
