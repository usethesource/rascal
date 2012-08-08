package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class RecursiveTestSuite extends Suite {

	public RecursiveTestSuite(Class<?> setupClass)
			throws InitializationError {
		super(setupClass, getClasses(setupClass));
	}

	private static Class<?>[] getClasses(Class<?> setupClass) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		if (setupClass.isAnnotationPresent(RecursiveTest.class)) {
	        String packageName = setupClass.getPackage().getName();
	        String path = setupClass.getProtectionDomain().getCodeSource().getLocation().getFile();
	        path = path + packageName.replace('.', File.separatorChar);
			String[] directories = setupClass.getAnnotation(RecursiveTest.class).value();
			for (String dir: directories) {
				findTestClasses(setupClass.getPackage(), new File(path + File.separatorChar + dir), result);
			}
		}
		return result.toArray(new Class<?>[0]);
	}

	private static void findTestClasses(Package root, File path, List<Class<?>> result) {
		for (File f : path.listFiles()) {
			if (f.isDirectory()) {
				findTestClasses(root, f, result);
			}
			else if (f.getName().endsWith(".class")) {
				// check if the Class has @Test methods
				String className = f.getName().substring(0, f.getName().length() - 6);
				className = path.getPath().replace(File.separatorChar, '.') + '.' + className;
				className = className.substring(className.indexOf(root.getName())); // remove the part of the path which is just the subdir of the project.
				try {
					Class<?> currentClass = Class.forName(className);
					for (Method m: currentClass.getMethods()) {
						if (m.isAnnotationPresent(Test.class)) {
							result.add(currentClass);
							break;
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
