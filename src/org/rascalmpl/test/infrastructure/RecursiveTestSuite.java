/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.infrastructure;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import junit.framework.TestCase;

public class RecursiveTestSuite extends Suite {

	public RecursiveTestSuite(Class<?> setupClass)
			throws InitializationError {
		super(setupClass, getClasses(setupClass));
	}

	private static Class<?>[] getClasses(Class<?> setupClass) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		if (setupClass.isAnnotationPresent(RecursiveTest.class)) {
		    String[] directories = setupClass.getAnnotation(RecursiveTest.class).value();
	        addClassesFromDirectories(setupClass, result, directories, true);
		}
		if (setupClass.isAnnotationPresent(RecursiveJavaOnlyTest.class)) {
		    String[] directories = setupClass.getAnnotation(RecursiveJavaOnlyTest.class).value();
	        addClassesFromDirectories(setupClass, result, directories, false);
		}
		return result.toArray(new Class<?>[0]);
	}

    private static void addClassesFromDirectories(Class<?> setupClass, List<Class<?>> result, String[] directories,
            boolean includeRascalTests) {
        String packageName = setupClass.getPackage().getName();
        String path = setupClass.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = path + packageName.replace('.', File.separatorChar);
        for (String dir: directories) {
        	findTestClasses(setupClass.getPackage(), new File(path + File.separatorChar + dir), includeRascalTests, result);
        }
    }

	private static void findTestClasses(Package root, File path, boolean includeRascalTests, List<Class<?>> result) {
		for (File f : path.listFiles()) {
			if (f.isDirectory()) {
				findTestClasses(root, f, includeRascalTests, result);
			}
			else if (f.getName().endsWith(".class")) {
				// check if the Class has @Test methods
				String className = f.getName().substring(0, f.getName().length() - 6);
				className = path.getPath().replace(File.separatorChar, '.') + '.' + className;
				className = className.substring(className.indexOf(root.getName())); // remove the part of the path which is just the subdir of the project.
				try {
					Class<?> currentClass = Class.forName(className);
					
					if (currentClass.isAnnotationPresent(RascalJUnitTestPrefix.class) || currentClass.isAnnotationPresent(RecursiveRascalParallelTest.class)) {
					    if (includeRascalTests) {
					        result.add(currentClass);
					    }
					}
					else if (TestCase.class.isAssignableFrom(currentClass)) {
						result.add(currentClass);
					}
					else {
						for (Method m: currentClass.getMethods()) {
							if (m.isAnnotationPresent(Test.class)) {
								result.add(currentClass);
								break;
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
