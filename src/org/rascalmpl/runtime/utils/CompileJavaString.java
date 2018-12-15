package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;
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
