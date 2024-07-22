/*******************************************************************************
 * Copyright (c) 2009-2017 CWI
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.JavaCompilation;
import org.rascalmpl.exceptions.JavaMethodLink;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.MissingTag;
import org.rascalmpl.interpreter.staticErrors.NonAbstractJavaFunction;
import org.rascalmpl.interpreter.staticErrors.UndeclaredJavaMethod;
import org.rascalmpl.types.DefaultRascalTypeVisitor;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.util.ListClassLoader;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;


public class JavaBridge {
	private static final String JAVA_CLASS_TAG = "javaClass";
	
	private final List<ClassLoader> loaders;
	
	private final static JavaClasses javaClasses = new JavaClasses();
	
	private final IValueFactory vf;
	
	private final Map<Class<?>, Object> instanceCache;
	

	private final Configuration config;

	public JavaBridge(List<ClassLoader> classLoaders, IValueFactory valueFactory, Configuration config) {
		this.loaders = classLoaders;
		this.vf = valueFactory;
		this.instanceCache = new HashMap<Class<?>, Object>();
		this.config = config;
		
		if (ToolProvider.getSystemJavaCompiler() == null) {
			throw new ImplementationError("Could not find an installed System Java Compiler, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
		}
	}

	public <T> Class<T> compileJava(ISourceLocation loc, String className, String source) {
		return compileJava(loc, className, getClass(), source);
	}

	public void compileJava(ISourceLocation loc, String className, String source, OutputStream classBytes) {
		compileJava(loc, className, getClass(), source, classBytes);
	}
	
	public <T> Class<T> compileJava(ISourceLocation loc, String className, Class<?> parent, String source) {
		try {
			// watch out, if you start sharing this compiler, classes will not be able to reload
			List<String> commandline = Arrays.asList(new String[] {"-proc:none", "--release", "11", "-cp", config.getRascalJavaClassPathProperty()});
			JavaCompiler<T> javaCompiler = new JavaCompiler<T>(parent.getClassLoader(), null, commandline);
			Class<T> result = javaCompiler.compile(className, source, null, Object.class);
			return result;
		} 
		catch (ClassCastException e) {
			throw new JavaCompilation(e.getMessage(), 1, 0, source, config.getRascalJavaClassPathProperty(), e);
		} 
		catch (JavaCompilerException e) {
		    if (!e.getDiagnostics().getDiagnostics().isEmpty()) {
		        Diagnostic<? extends JavaFileObject> msg = e.getDiagnostics().getDiagnostics().iterator().next();
		        throw new JavaCompilation(msg.getMessage(null), msg.getLineNumber(), msg.getColumnNumber(), source, config.getRascalJavaClassPathProperty(), e);
		    }
		    else {
		        throw new JavaCompilation(e.getMessage(), 1, 0, source, config.getRascalJavaClassPathProperty(), e);
		    }
		}
	}

	public Class<?> loadClass(InputStream in) throws IOException, ClassNotFoundException {
		List<String> commandline = Arrays.asList(new String[] {"-proc:none", "-cp", config.getRascalJavaClassPathProperty()});
		JavaCompiler<?> javaCompiler = new JavaCompiler<Object>(getClass().getClassLoader(), null, commandline);
		return javaCompiler.load(in);
	}

	public <T> void compileJava(ISourceLocation loc, String className, Class<?> parent, String source, OutputStream classBytes) {
		try {
			// watch out, if you start sharing this compiler, classes will not be able to reload
			List<String> commandline = Arrays.asList(new String[] {"-proc:none", "-cp", config.getRascalJavaClassPathProperty()});
			JavaCompiler<T> javaCompiler = new JavaCompiler<T>(parent.getClassLoader(), null, commandline);
			javaCompiler.compile(classBytes, className, source, null);
		} 
		catch (ClassCastException e) {
			throw new JavaCompilation(
				e.getMessage(), 
				1, 0,
				source, 
				config.getRascalJavaClassPathProperty(),
				e
			);
		} 
		catch (JavaCompilerException e) {
		    if (!e.getDiagnostics().getDiagnostics().isEmpty()) {
		        Diagnostic<? extends JavaFileObject> msg = e.getDiagnostics().getDiagnostics().iterator().next();
		        throw new JavaCompilation(
					msg.getMessage(null), msg.getLineNumber(), msg.getColumnNumber(), 
					source, 
					config.getRascalJavaClassPathProperty(),
					e
				);
		    }
		    else {
		        throw new JavaCompilation(
					e.getMessage(), 
					1,  0,
					source, 
					config.getRascalJavaClassPathProperty(),
					e
				);
		    }
		}
	}

	private String getClassName(FunctionDeclaration declaration) {
		Tags tags = declaration.getTags();
		
		if (tags.hasTags()) {
			for (Tag tag : tags.getTags()) {
				if (Names.name(tag.getName()).equals(JAVA_CLASS_TAG)) {
					if(tag.hasContents()){
						String contents = ((TagString.Lexical) tag.getContents()).getString();

						if (contents.length() > 2 && contents.startsWith("{")) {
							contents = contents.substring(1, contents.length() - 1);
						}
						return contents;
					}
				}
			}
		}
		
		return "";
	}
	

	private Class<?>[] getJavaTypes(Parameters parameters, Environment env, boolean hasReflectiveAccess) {
		List<Expression> formals = parameters.getFormals().getFormals();
		int arity = formals.size();
		int kwArity = 0;
		List<KeywordFormal> keywordFormals = null;
		if(parameters.getKeywordFormals().isDefault()){
			keywordFormals = parameters.getKeywordFormals().getKeywordFormalList();
			kwArity = keywordFormals.size();
		}		
		
		Class<?>[] classes = new Class<?>[arity + kwArity + (hasReflectiveAccess ? 1 : 0)];
		int i = 0;
		while (i < arity) {
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
		
		while(i < arity + kwArity){
			Class<?> clazz = toJavaClass(keywordFormals.get(i - arity).getType(), env);
			if (clazz != null) {
				  classes[i++] = clazz;
				}
		}
		
		if (hasReflectiveAccess) {
			classes[arity + kwArity] = IEvaluatorContext.class;
		}
		
		return classes;
	}
	
	private Class<?> toJavaClass(Expression formal, Environment env) {
		return toJavaClass(toValueType(formal, env));
	}
	
	private Class<?> toJavaClass(org.rascalmpl.ast.Type tp, Environment env) {
		return toJavaClass(tp.typeOf(env, null, false));
	}

	private Class<?> toJavaClass(io.usethesource.vallang.type.Type type) {
		return type.accept(javaClasses);
	}
	
	private io.usethesource.vallang.type.Type toValueType(Expression formal, Environment env) {
		return formal.typeOf(env, null, false);
	}
	
	private static class JavaClasses extends DefaultRascalTypeVisitor<Class<?>, RuntimeException> {

		public JavaClasses() {
			super(IValue.class);
		}

		@Override
		public Class<?> visitBool(io.usethesource.vallang.type.Type boolType) {
			return IBool.class;
		}

		@Override
		public Class<?> visitReal(io.usethesource.vallang.type.Type type) {
			return IReal.class;
		}

		@Override
		public Class<?> visitInteger(io.usethesource.vallang.type.Type type) {
			return IInteger.class;
		}
		
		@Override
		public Class<?> visitRational(io.usethesource.vallang.type.Type type) {
			return IRational.class;
		}
		
		@Override
		public Class<?> visitNumber(io.usethesource.vallang.type.Type type) {
			return INumber.class;
		}

		@Override
		public Class<?> visitList(io.usethesource.vallang.type.Type type) {
			return IList.class;
		}

		@Override
		public Class<?> visitMap(io.usethesource.vallang.type.Type type) {
			return IMap.class;
		}

		@Override
		public Class<?> visitAlias(io.usethesource.vallang.type.Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public Class<?> visitAbstractData(io.usethesource.vallang.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitSet(io.usethesource.vallang.type.Type type) {
			return ISet.class;
		}

		@Override
		public Class<?> visitSourceLocation(io.usethesource.vallang.type.Type type) {
			return ISourceLocation.class;
		}

		@Override
		public Class<?> visitString(io.usethesource.vallang.type.Type type) {
			return IString.class;
		}

		@Override
		public Class<?> visitNode(io.usethesource.vallang.type.Type type) {
			return INode.class;
		}

		@Override
		public Class<?> visitConstructor(io.usethesource.vallang.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitTuple(io.usethesource.vallang.type.Type type) {
			return ITuple.class;
		}

		@Override
		public Class<?> visitValue(io.usethesource.vallang.type.Type type) {
			return IValue.class;
		}

		@Override
		public Class<?> visitVoid(io.usethesource.vallang.type.Type type) {
			return null;
		}

		@Override
		public Class<?> visitParameter(io.usethesource.vallang.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
		
		@Override
		public Class<?> visitNonTerminal(RascalType type)
				throws RuntimeException {
			return ITree.class;
		}
		
		@Override
		public Class<?> visitFunction(Type type) throws RuntimeException {
		    return IFunction.class;
		}
	}
	
	public synchronized Object getJavaClassInstance(FunctionDeclaration func, IRascalMonitor monitor, TypeStore store, PrintWriter out, PrintWriter err, OutputStream rawOut, OutputStream rawErr, InputStream in, IEvaluatorContext ctx) {
		String className = getClassName(func);
		
		PrintWriter[] outputs = new PrintWriter[] { out, err };
		int writers = 0;
        
		OutputStream[] rawOutputs = new OutputStream[] { rawOut, rawErr };
		int rawWriters = 0;

		try {
			for(ClassLoader loader : loaders){
				try{
					Class<?> clazz = loader.loadClass(className);

					Object instance = instanceCache.get(clazz);
					if(instance != null){
						return instance;
					}

					if (clazz.getConstructors().length > 1) {
					    throw new IllegalArgumentException("Rascal JavaBridge can only deal with one constructor. This class has multiple: " + clazz);
					}
					
					Constructor<?>[] constructors = clazz.getConstructors();

					if (constructors.length < 1) {
						throw new JavaMethodLink(className, "no public constructors found", new IllegalArgumentException(className));
					}
					else if (constructors.length != 1) {
						throw new JavaMethodLink(className, "more than one public constructor found", new IllegalArgumentException(className));
					}

					Constructor<?> constructor = constructors[0];

					Object[] args = new Object[constructor.getParameterCount()];
					Class<?>[] formals = constructor.getParameterTypes();

					for (int i = 0; i < constructor.getParameterCount(); i++) {
					    if (formals[i].isAssignableFrom(IValueFactory.class)) {
					        args[i] = vf;
					    }
					    else if (formals[i].isAssignableFrom(TypeStore.class)) {
					        args[i] = store;
					    }
					    else if (formals[i].isAssignableFrom(TypeFactory.class)) {
					        args[i] = TypeFactory.getInstance();
					    }
					    else if (formals[i].isAssignableFrom(PrintWriter.class)) {
					        args[i] = outputs[writers++ % 2];
					    }
					    else if (formals[i].isAssignableFrom(OutputStream.class)) {
					        args[i] = rawOutputs[rawWriters++ %2];
					    }
					    else if (formals[i].isAssignableFrom(InputStream.class)) {
					        args[i] = in;
					    }
					    else if (formals[i].isAssignableFrom(IRascalMonitor.class)) {
					        args[i] = monitor;
					    }
					    else if (formals[i].isAssignableFrom(ClassLoader.class)) {
					        args[i] = new ListClassLoader(loaders, getClass().getClassLoader()); 
					    }
					    else if (formals[i].isAssignableFrom(IRascalValueFactory.class)) {
					        args[i] = ctx.getFunctionValueFactory();
					    }
						else if (formals[i].isAssignableFrom(IDEServices.class)) {
							if (monitor instanceof IDEServices) {
								args[i] = (IDEServices) monitor;
							}
							else {
								throw new IllegalArgumentException("no IDE services are available in this environment");
							}
						}
						else if (formals[i].isAssignableFrom(IResourceLocationProvider.class)) {
							args[i] = new IResourceLocationProvider() {
								@Override
								public Set<ISourceLocation> findResources(String fileName) {
									Set<ISourceLocation> result = new HashSet<>();
									URIResolverRegistry reg = URIResolverRegistry.getInstance();
									
									for (ISourceLocation dir : ctx.getEvaluator().getRascalResolver().collect()) {
										ISourceLocation full = URIUtil.getChildLocation(dir, fileName);
										if (reg.exists(full)) {
											result.add(full);
										}
									}
										
									return result;
								}
							};
						}
					    else {
					        throw new IllegalArgumentException(constructor + " has unknown kinds of arguments.");
					    }
					}

					instance = constructor.newInstance(args);
					instanceCache.put(clazz, instance);
					return instance;
				}
				catch(ClassNotFoundException e){
					continue;
				} 
			}
		} 
		catch(NoClassDefFoundError e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		}
		catch (IllegalArgumentException e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		} 
		catch (InstantiationException e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		} 
		catch (IllegalAccessException e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		} 
		catch (InvocationTargetException e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		} 
		catch (SecurityException e) {
			throw new JavaMethodLink(className, e.getMessage(), e);
		} 
		
		throw new JavaMethodLink(className, "class not found", null);
	}

	public Method lookupJavaMethod(IEvaluator<Result<IValue>> eval, FunctionDeclaration func, Environment env, boolean hasReflectiveAccess){
		if(!func.isAbstract()){
			throw new NonAbstractJavaFunction(func);
		}
		
		String className = getClassName(func);
		String name = Names.name(func.getSignature().getName());
		
		if(className.length() == 0){	// TODO: Can this ever be thrown since the Class instance has 
										// already been identified via the javaClass tag.
			throw new MissingTag(JAVA_CLASS_TAG, func);
		}
		
		for (ClassLoader loader : loaders) {
			try {
				Class<?> clazz = loader.loadClass(className);
				Parameters parameters = func.getSignature().getParameters();
				Class<?>[] javaTypes = getJavaTypes(parameters, env, hasReflectiveAccess);

				try { 
					Method m;
					
					if(javaTypes.length > 0){ // non-void
						m = clazz.getMethod(name, javaTypes);
					}else{
						m = clazz.getMethod(name);
					}

					return m;
				} catch(SecurityException e) {
					throw RuntimeExceptionFactory.permissionDenied(vf.string(e.getMessage()), eval.getCurrentAST(), eval.getStackTrace());
				} catch(NoSuchMethodException e) {
					throw new UndeclaredJavaMethod(e.getMessage(), func);
				}
			} catch(ClassNotFoundException e) {
				continue;
			}
		}
		
		throw new UndeclaredJavaMethod(className + "." + name, func);
	}
}
