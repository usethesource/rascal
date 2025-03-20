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
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
public class CompileJavaString {
	public static void main(String[] args) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String program = "class Test{" + "   public static void main (String [] args){"
				+ "      System.out.println (\"Hello, World\");"
				+ "      System.out.println (args.length);" + "   }" + "}";

		Iterable<? extends JavaFileObject> fileObjects;
		fileObjects = getJavaSourceFromString(program);

		compiler.getTask(null, null, null, null, null, fileObjects).call();

		Class<?> clazz = Class.forName("Test");
		Method m = clazz.getMethod("main", new Class[] { String[].class });
		Object[] _args = new Object[] { new String[0] };
		m.invoke(null, _args);
	}

	static Iterable<JavaSourceFromString> getJavaSourceFromString(String code) {
		final JavaSourceFromString jsfs;
		jsfs = new JavaSourceFromString("code", code);
		return new Iterable<JavaSourceFromString>() {
			public Iterator<JavaSourceFromString> iterator() {
				return new Iterator<JavaSourceFromString>() {
					boolean isNext = true;

					public boolean hasNext() {
						return isNext;
					}

					public JavaSourceFromString next() {
						if (!isNext)
							throw new NoSuchElementException();
						isNext = false;
						return jsfs;
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}

class JavaSourceFromString extends SimpleJavaFileObject {
	final String code;

	JavaSourceFromString(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
