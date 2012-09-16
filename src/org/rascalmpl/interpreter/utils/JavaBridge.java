/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.JavaCompilationError;
import org.rascalmpl.interpreter.staticErrors.JavaMethodLinkError;
import org.rascalmpl.interpreter.staticErrors.MissingTagError;
import org.rascalmpl.interpreter.staticErrors.NonAbstractJavaFunctionError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredJavaMethodError;


public class JavaBridge {
	private static final String JAVA_CLASS_TAG = "javaClass";
	
	private final List<ClassLoader> loaders;
	
	private final static JavaClasses javaClasses = new JavaClasses();
	
	private final IValueFactory vf;
	
	private final HashMap<Class<?>, Object> instanceCache;
	
	private final HashMap<Class<?>, JavaFileManager> fileManagerCache;

	public JavaBridge(List<ClassLoader> classLoaders, IValueFactory valueFactory) {
		this.loaders = classLoaders;
		this.vf = valueFactory;
		this.instanceCache = new HashMap<Class<?>, Object>();
		this.fileManagerCache = new HashMap<Class<?>, JavaFileManager>();
		
		if (ToolProvider.getSystemJavaCompiler() == null) {
			throw new ImplementationError("Could not find an installed System Java Compiler, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
		}
	}

	public <T> Class<T> compileJava(URI loc, String className, String source) {
		return compileJava(loc, className, getClass(), source);
	}
	
	public <T> Class<T> compileJava(URI loc, String className, Class<?> parent, String source) {
		try {
			// watch out, if you start sharing this compiler, classes will not be able to reload
			List<String> commandline = Arrays.asList(new String[] {"-cp", Configuration.getRascalJavaClassPathProperty()});
			
			JavaCompiler<T> javaCompiler = new JavaCompiler<T>(parent.getClassLoader(), fileManagerCache.get(parent), commandline);
			Class<T> result = javaCompiler.compile(className, source, null, Object.class);
			fileManagerCache.put(result, javaCompiler.getFileManager());
			return result;
		} catch (ClassCastException e) {
			throw new JavaCompilationError(e.getMessage(), vf.sourceLocation(loc));
		} catch (JavaCompilerException e) {
			throw new JavaCompilationError(e.getDiagnostics().getDiagnostics().iterator().next().getMessage(null), vf.sourceLocation(loc));
		}
	}

	private String getClassName(FunctionDeclaration declaration) {
		Tags tags = declaration.getTags();
		
		if (tags.hasTags()) {
			for (Tag tag : tags.getTags()) {
				if (Names.name(tag.getName()).equals(JAVA_CLASS_TAG)) {
					String contents = ((TagString.Lexical) tag.getContents()).getString();
					
					if (contents.length() > 2 && contents.startsWith("{")) {
						contents = contents.substring(1, contents.length() - 1);
					}
					return contents;
				}
			}
		}
		
		return "";
	}
	

	private Class<?>[] getJavaTypes(Parameters parameters, Environment env, boolean hasReflectiveAccess) {
		List<Expression> formals = parameters.getFormals().getFormals();
		int arity = formals.size();
		Class<?>[] classes = new Class<?>[arity + (hasReflectiveAccess ? 1 : 0)];
		for (int i = 0; i < arity;) {
			Class<?> clazz;
			
			if (i == arity - 1 && parameters.isVarArgs()) {
				clazz = IList.class;
			}
			else {
				clazz = toJavaClass(formals.get(i), env);
			}
			
			if (clazz != null) {
			  classes[i++] = clazz;
			}
		}
		
		if (hasReflectiveAccess) {
			classes[arity] = IEvaluatorContext.class;
		}
		
		return classes;
	}
	
	private Class<?> toJavaClass(Expression formal, Environment env) {
		return toJavaClass(toValueType(formal, env));
	}

	private Class<?> toJavaClass(org.eclipse.imp.pdb.facts.type.Type type) {
		return type.accept(javaClasses);
	}
	
	private org.eclipse.imp.pdb.facts.type.Type toValueType(Expression formal, Environment env) {
		return formal.typeOf(env);
	}
	
	private static class JavaClasses implements ITypeVisitor<Class<?>> {

		public Class<?> visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return IBool.class;
		}

		public Class<?> visitReal(org.eclipse.imp.pdb.facts.type.Type type) {
			return IReal.class;
		}

		public Class<?> visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
			return IInteger.class;
		}
		
		public Class<?> visitRational(org.eclipse.imp.pdb.facts.type.Type type) {
			return IRational.class;
		}
		
		public Class<?> visitNumber(org.eclipse.imp.pdb.facts.type.Type type) {
			return INumber.class;
		}

		public Class<?> visitList(org.eclipse.imp.pdb.facts.type.Type type) {
			return IList.class;
		}

		public Class<?> visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
			return IMap.class;
		}

		public Class<?> visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
			return type.getAliased().accept(this);
		}

		public Class<?> visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		public Class<?> visitRelationType(org.eclipse.imp.pdb.facts.type.Type type) {
			return IRelation.class;
		}

		public Class<?> visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISet.class;
		}

		public Class<?> visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISourceLocation.class;
		}

		public Class<?> visitString(org.eclipse.imp.pdb.facts.type.Type type) {
			return IString.class;
		}

		public Class<?> visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
			return INode.class;
		}

		public Class<?> visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		public Class<?> visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
			return ITuple.class;
		}

		public Class<?> visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
			return IValue.class;
		}

		public Class<?> visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
			return null;
		}

		public Class<?> visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		public Class<?> visitExternal(
				org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}

		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}
	
	public Object getJavaClassInstance(Class<?> clazz){
		Object instance = instanceCache.get(clazz);
		if(instance != null){
			return instance;
		}
		
		try{
			Constructor<?> constructor = clazz.getConstructor(IValueFactory.class);
			instance = constructor.newInstance(vf);
			instanceCache.put(clazz, instance);
			return instance;
		} catch (IllegalArgumentException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ImplementationError(e.getMessage(), e);
		} 
	}
	
	public Object getJavaClassInstance(FunctionDeclaration func){
		String className = getClassName(func);

		try {
			for(ClassLoader loader : loaders){
				try{
					Class<?> clazz = loader.loadClass(className);

					Object instance = instanceCache.get(clazz);
					if(instance != null){
						return instance;
					}

					Constructor<?> constructor = clazz.getConstructor(IValueFactory.class);
					instance = constructor.newInstance(vf);
					instanceCache.put(clazz, instance);
					return instance;
				}
				catch(ClassNotFoundException e){
					continue;
				} 
			}
		} 
		catch(NoClassDefFoundError e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		}
		catch (IllegalArgumentException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		} catch (InstantiationException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		} catch (IllegalAccessException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		} catch (InvocationTargetException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		} catch (SecurityException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		} catch (NoSuchMethodException e) {
			throw new JavaMethodLinkError(className, e.getMessage(), func, e);
		}
		
		throw new JavaMethodLinkError(className, "class not found", func, null);
	}

	public Method lookupJavaMethod(IEvaluator<Result<IValue>> eval, FunctionDeclaration func, Environment env, boolean hasReflectiveAccess){
		if(!func.isAbstract()){
			throw new NonAbstractJavaFunctionError(func);
		}
		
		String className = getClassName(func);
		String name = Names.name(func.getSignature().getName());
		
		if(className.length() == 0){
			throw new MissingTagError(JAVA_CLASS_TAG, func);
		}
		
		for(ClassLoader loader : loaders){
			try{
				Class<?> clazz = loader.loadClass(className);
				Parameters parameters = func.getSignature().getParameters();
				Class<?>[] javaTypes = getJavaTypes(parameters, env, hasReflectiveAccess);

				try{
					Method m;
					
					if(javaTypes.length > 0){ // non-void
						m = clazz.getMethod(name, javaTypes);
					}else{
						m = clazz.getMethod(name);
					}

					return m;
				}catch(SecurityException e){
					throw RuntimeExceptionFactory.permissionDenied(vf.string(e.getMessage()), eval.getCurrentAST(), eval.getStackTrace());
				}catch(NoSuchMethodException e){
					throw new UndeclaredJavaMethodError(e.getMessage(), func);
				}
			}catch(ClassNotFoundException e){
				continue;
			}
		}
		
		throw new UndeclaredJavaMethodError(className + "." + name, func);
	}

	/**
	 * Same as saveToJar("", clazz, outPath, false);
	 */
	public void saveToJar(Class<?> clazz, String outPath) throws IOException {
		saveToJar("", clazz, outPath, false);
	}
	
	/**
	 * Save a compiled class and associated classes to a jar file.
	 *  
	 * With a packageName = "" and recursive = false, it will save clazz and any classes
	 * compiled from the same source (I think); this is probably what you want.
	 * 
	 * @param packageName package name prefix to search for classes, or "" for all 
	 * @param clazz a class that has been previously compiled by this bridge
	 * @param outPath name of output jar file
	 * @param recursive whether to retrieve classes from rest of the the JavaFileManager hierarchy
	 * 	
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveToJar(String packageName, Class<?> clazz, String outPath,
			boolean recursive) throws IOException {
		JavaFileManager manager = fileManagerCache.get(clazz);
		Iterable<JavaFileObject> list = null;
		list = manager.list(StandardLocation.CLASS_PATH, packageName,
			Collections.singleton(JavaFileObject.Kind.CLASS), false);

		if (list.iterator().hasNext()) {
			Manifest manifest = new Manifest();
			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,
					"1.0");
			JarOutputStream target = new JarOutputStream(new FileOutputStream(outPath), manifest);
			JarEntry entry = new JarEntry("META-INF/");
			target.putNextEntry(entry);
			Collection<String> dirs = new ArrayList<String>();

			for (JavaFileObject o : list) {
				String path = o.toUri().getPath().replace(".", "/");
				String dir = path.substring(0, path.lastIndexOf('/'));
				StringBuilder dirTmp = new StringBuilder(dir.length());
				for (String d : dir.split("/")) {
					dirTmp.append(d);
					dirTmp.append("/");
					String tmp = dirTmp.toString();
					if (!dirs.contains(tmp)) {
						dirs.add(tmp);
						entry = new JarEntry(tmp);
						target.putNextEntry(entry);
					}
				}
				entry = new JarEntry(path + ".class");
				entry.setTime(o.getLastModified());
				target.putNextEntry(entry);
				InputStream stream = null;
				try {
					stream = o.openInputStream();

					byte[] buffer = new byte[8192];
					int c = stream.read(buffer);
					while (c > -1) {
						target.write(buffer, 0, c);
						c = stream.read(buffer);
					}
				} finally {
					if (stream != null)
						stream.close();
				}
				target.closeEntry();

			}
			target.close();
		}

	}
}
