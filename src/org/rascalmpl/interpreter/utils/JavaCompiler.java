/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This code was originally taken from http://www.ibm.com/developerworks/java/library/j-jcomp
 *
 * Contributors:
 *   * David Biesak - IBM (initial version)
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.rascalmpl.uri.URIUtil;

/**
 * Compile a String or other {@link CharSequence}, returning a Java
 * {@link Class} instance that may be instantiated. This class is a Facade
 * around {@link JavaCompiler} for a narrower use case, but a bit easier to use.
 * <p>
 * To compile a String containing source for a Java class which implements
 * MyInterface:
 * 
 * <pre>
 * ClassLoader classLoader = MyClass.class.getClassLoader(); // optional; null is also OK 
 * List&lt;Diagnostic&gt; diagnostics = new ArrayList&lt;Diagnostic&gt;(); // optional; null is also OK
 * JavaStringCompiler&lt;Object&gt; compiler = new JavaStringCompiler&lt;MyInterface&gt;(classLoader,
 *       null);
 * try {
 *    Class&lt;MyInterface&gt; newClass = compiler.compile(&quot;com.mypackage.NewClass&quot;,
 *          stringContaininSourceForNewClass, diagnostics, MyInterface);
 *    MyInterface instance = newClass.newInstance();
 *    instance.someOperation(someArgs);
 * } catch (JavaStringCompilerException e) {
 *    handle(e);
 * } catch (IllegalAccessException e) {
 *    handle(e);
 * }
 * </pre>
 * 
 * The source can be in a String, {@link StringBuffer}, or your own class which
 * implements {@link CharSequence}. If you implement your own, it must be
 * thread safe (preferably, immutable.)
 * 
 * @author <a href="mailto:David.Biesack@sas.com">David J. Biesack</a>
 */
public class JavaCompiler<T> {
   // Compiler requires source files with a ".java" extension:
   static final String JAVA_EXTENSION = ".java";

   private final ClassLoaderImpl classLoader;

   // The compiler instance that this facade uses.
   private final javax.tools.JavaCompiler compiler;

   // The compiler options (such as "-target" "1.5").
   private final List<String> options;

   // collect compiler diagnostics in this instance.
   private DiagnosticCollector<JavaFileObject> diagnostics;

   // The FileManager which will store source and class "files".
   private final FileManagerImpl javaFileManager;

   /**
    * Construct a new instance which delegates to the named class loader.
    * 
    * @param loader
    *           the application ClassLoader. The compiler will look through to
    *           this // class loader for dependent classes
    * @param options
    *           The compiler options (such as "-target" "1.5"). See the usage
    *           for javac
    * @throws IllegalStateException
    *            if the Java compiler cannot be loaded.
    */
   public JavaCompiler(ClassLoader loader, JavaFileManager parentFileManager, Iterable<String> options) {
      compiler = ToolProvider.getSystemJavaCompiler();
      if (compiler == null) {
         throw new IllegalStateException("Cannot find the system Java compiler. "
               + "Check that your class path includes tools.jar");
      }
      classLoader = new ClassLoaderImpl(loader);
      diagnostics = new DiagnosticCollector<JavaFileObject>();
      final JavaFileManager fileManager;
      if (parentFileManager == null) {
    	  fileManager = compiler.getStandardFileManager(diagnostics, null, null);
      }
      else {
    	  fileManager = parentFileManager;
      }
      // create our FileManager which chains to the default file manager
      // and our ClassLoader
      javaFileManager = new FileManagerImpl(fileManager, classLoader);
      this.options = new ArrayList<String>();
      if (options != null) { // make a save copy of input options
         for (String option : options) {
            this.options.add(option);
         }
      }
   }

   /**
    * Compile Java source in <var>javaSource</name> and return the resulting
    * class.
    * <p>
    * Thread safety: this method is thread safe if the <var>javaSource</var>
    * and <var>diagnosticsList</var> are isolated to this thread.
    * 
    * @param qualifiedClassName
    *           The fully qualified class name.
    * @param javaSource
    *           Complete java source, including a package statement and a class,
    *           interface, or annotation declaration.
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to
    *           this collector.
    * @param types
    *           zero or more Class objects representing classes or interfaces
    *           that the resulting class must be assignable (castable) to.
    * @return a Class which is generated by compiling the source
    * @throws JavaCompilerException
    *            if the source cannot be compiled - for example, if it contains
    *            syntax or semantic errors or if dependent classes cannot be
    *            found.
    * @throws ClassCastException
    *            if the generated class is not assignable to all the optional
    *            <var>types</var>.
    */
   public synchronized Class<T> compile(final String qualifiedClassName,
         final CharSequence javaSource,
         final DiagnosticCollector<JavaFileObject> diagnosticsList,
         final Class<?>... types) throws JavaCompilerException,
         ClassCastException {
      if (diagnosticsList != null) {
         diagnostics = diagnosticsList;
      }
      else {
         diagnostics = new DiagnosticCollector<JavaFileObject>();
      }

      Map<String, CharSequence> classes = new HashMap<String, CharSequence>(1);
      classes.put(qualifiedClassName, javaSource);
      Map<String, Class<T>> compiled = compile(classes, diagnostics);
      Class<T> newClass = compiled.get(qualifiedClassName);
      return castable(newClass, types);
   }

   /**
    * Compile multiple Java source strings and return a Map containing the
    * resulting classes.
    * <p>
    * Thread safety: this method is thread safe if the <var>classes</var> and
    * <var>diagnosticsList</var> are isolated to this thread.
    * 
    * @param classes
    *           A Map whose keys are qualified class names and whose values are
    *           the Java source strings containing the definition of the class.
    *           A map value may be null, indicating that compiled class is
    *           expected, although no source exists for it (it may be a
    *           non-public class contained in one of the other strings.)
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to
    *           this list.
    * @return A mapping of qualified class names to their corresponding classes.
    *         The map has the same keys as the input <var>classes</var>; the
    *         values are the corresponding Class objects.
    * @throws JavaCompilerException
    *            if the source cannot be compiled
    */
   public synchronized Map<String, Class<T>> compile(
         final Map<String, CharSequence> classes,
         final DiagnosticCollector<JavaFileObject> diagnostics)
         throws JavaCompilerException {
      List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
      for (Entry<String, CharSequence> entry : classes.entrySet()) {
         String qualifiedClassName = entry.getKey();
         CharSequence javaSource = entry.getValue();
         if (javaSource != null) {
            final int dotPos = qualifiedClassName.lastIndexOf('.');
            final String className = dotPos == -1 ? qualifiedClassName
                  : qualifiedClassName.substring(dotPos + 1);
            final String packageName = dotPos == -1 ? "" : qualifiedClassName
                  .substring(0, dotPos);
            final JavaFileObjectImpl source = new JavaFileObjectImpl(className,
                  javaSource);
            sources.add(source);
            // Store the source file in the FileManager via package/class
            // name.
            // For source files, we add a .java extension
            javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName,
                  className + JAVA_EXTENSION, source);
         }
      }
      // Get a CompliationTask from the compiler and compile the sources
      final CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics,
            options, null, sources);
      final Boolean result = task.call();
      if (result == null || !result.booleanValue()) {
         throw new JavaCompilerException("Compilation failed.", classes
               .keySet(), diagnostics);
      }
      try {
         // For each class name in the inpput map, get its compiled
         // class and put it in the output map
         Map<String, Class<T>> compiled = new HashMap<String, Class<T>>();
         for (String qualifiedClassName : classes.keySet()) {
            final Class<T> newClass = loadClass(qualifiedClassName);
            compiled.put(qualifiedClassName, newClass);
         }
         return compiled;
      } catch (ClassNotFoundException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      } catch (IllegalArgumentException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      } catch (SecurityException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      }
   }

   public void compile(OutputStream classBytes, String qualifiedClassName, CharSequence classSource, final DiagnosticCollector<JavaFileObject> diagnostics) throws JavaCompilerException {
      Map<String, CharSequence> fileMap = new HashMap<>();

      try {
         fileMap.put(qualifiedClassName, classSource);

         // ignoring return class here
         compile(fileMap, diagnostics);

         // side-effect alert:
         //     now the local classloader contains the .class file
         classLoader.outputClassesToJar(qualifiedClassName, classBytes);
      }
      catch (IOException e) {
         throw new JavaCompilerException(fileMap.keySet(), e, diagnostics);
      }
   }

   /**
    * Load a class that was generated by this instance or accessible from its
    * parent class loader. Use this method if you need access to additional
    * classes compiled by
    * {@link #compile(String, CharSequence, DiagnosticCollector, Class...) compile()},
    * for example if the primary class contained nested classes or additional
    * non-public classes.
    * 
    * @param qualifiedClassName
    *           the name of the compiled class you wish to load
    * @return a Class instance named by <var>qualifiedClassName</var>
    * @throws ClassNotFoundException
    *            if no such class is found.
    */
   @SuppressWarnings("unchecked")
   public Class<T> loadClass(final String qualifiedClassName)
         throws ClassNotFoundException {
      return (Class<T>) classLoader.loadClass(qualifiedClassName);
   }

   /**
    * Check that the <var>newClass</var> is a subtype of all the type
    * parameters and throw a ClassCastException if not.
    * 
    * @param types
    *           zero of more classes or interfaces that the <var>newClass</var>
    *           must be castable to.
    * @return <var>newClass</var> if it is castable to all the types
    * @throws ClassCastException
    *            if <var>newClass</var> is not castable to all the types.
    */
   private Class<T> castable(Class<T> newClass, Class<?>... types)
         throws ClassCastException {
      for (Class<?> type : types)
         if (!type.isAssignableFrom(newClass)) {
            throw new ClassCastException(type.getName());
         }
      return newClass;
   }

   /**
    * COnverts a String to a URI.
    * 
    * @param name
    *           a file name
    * @return a URI
    */
   static URI toURI(String name) {
      try {
         return URIUtil.createFromEncoded(name);
      } catch (URISyntaxException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * @return This compiler's class loader.
    */
   public ClassLoader getClassLoader() {
      return javaFileManager.getClassLoader();
   }

   public JavaFileManager getFileManager() {
	  return javaFileManager;
   }

   
}

/**
 * A JavaFileManager which manages Java source and classes. This FileManager
 * delegates to the JavaFileManager and the ClassLoaderImpl provided in the
 * constructor. The sources are all in memory CharSequence instances and the
 * classes are all in memory byte arrays.
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
   // the delegating class loader (passed to the constructor)
   private final ClassLoaderImpl classLoader;

   // Internal map of filename URIs to JavaFileObjects.
   private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

   /**
    * Construct a new FileManager which forwards to the <var>fileManager</var>
    * for source and to the <var>classLoader</var> for classes
    * 
    * @param fileManager
    *           another FileManager that this instance delegates to for
    *           additional source.
    * @param classLoader
    *           a ClassLoader which contains dependent classes that the compiled
    *           classes will require when compiling them.
    */
   public FileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
      super(fileManager);
      this.classLoader = classLoader;
   }

   /**
    * @return the class loader which this file manager delegates to
    */
   public ClassLoader getClassLoader() {
      return classLoader;
   }

   /**
    * For a given file <var>location</var>, return a FileObject from which the
    * compiler can obtain source or byte code.
    * 
    * @param location
    *           an abstract file location
    * @param packageName
    *           the package name for the file
    * @param relativeName
    *           the file's relative name
    * @return a FileObject from this or the delegated FileManager
    * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location,
    *      java.lang.String, java.lang.String)
    */
   @Override
   public FileObject getFileForInput(Location location, String packageName,
         String relativeName) throws IOException {
      FileObject o = fileObjects.get(uri(location, packageName, relativeName));
      if (o != null)
         return o;
      return super.getFileForInput(location, packageName, relativeName);
   }

   /**
    * Store a file that may be retrieved later with
    * {@link #getFileForInput(javax.tools.JavaFileManager.Location, String, String)}
    * 
    * @param location
    *           the file location
    * @param packageName
    *           the Java class' package name
    * @param relativeName
    *           the relative name
    * @param file
    *           the file object to store for later retrieval
    */
   public void putFileForInput(StandardLocation location, String packageName,
         String relativeName, JavaFileObject file) {
      fileObjects.put(uri(location, packageName, relativeName), file);
   }

   /**
    * Convert a location and class name to a URI
    */
   private URI uri(Location location, String packageName, String relativeName) {
      return JavaCompiler.toURI(location.getName() + '/' + packageName + '/'
            + relativeName);
   }

   /**
    * Create a JavaFileImpl for an output class file and store it in the
    * classloader.
    * 
    * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location,
    *      java.lang.String, javax.tools.JavaFileObject.Kind,
    *      javax.tools.FileObject)
    */
   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName,
         Kind kind, FileObject outputFile) throws IOException {
      JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
      classLoader.add(qualifiedName, file);
      return file;
   }

   @Override
   public ClassLoader getClassLoader(JavaFileManager.Location location) {
      return classLoader;
   }

   @Override
   public String inferBinaryName(Location loc, JavaFileObject file) {
      String result;
      // For our JavaFileImpl instances, return the file's name, else
      // simply run the default implementation
      if (file instanceof JavaFileObjectImpl)
         result = file.getName();
      else
         result = super.inferBinaryName(loc, file);
      return result;
   }

   @Override
   public Iterable<JavaFileObject> list(Location location, String packageName,
         Set<Kind> kinds, boolean recurse) throws IOException {
      Iterable<JavaFileObject> result = super.list(location, packageName, kinds,
            recurse);
      ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();
      if (location == StandardLocation.CLASS_PATH
            && kinds.contains(JavaFileObject.Kind.CLASS)) {
         for (JavaFileObject file : fileObjects.values()) {
            if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName))
               files.add(file);
         }
         files.addAll(classLoader.files());
      } else if (location == StandardLocation.SOURCE_PATH
            && kinds.contains(JavaFileObject.Kind.SOURCE)) {
         for (JavaFileObject file : fileObjects.values()) {
            if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName))
               files.add(file);
         }
      }
      for (JavaFileObject file : result) {
         files.add(file);
      }
      return files;
   }
}

/**
 * A JavaFileObject which contains either the source text or the compiler
 * generated class. This class is used in two cases.
 * <ol>
 * <li>This instance uses it to store the source which is passed to the
 * compiler. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, CharSequence)}
 * constructor.
 * <li>The Java compiler also creates instances (indirectly through the
 * FileManagerImplFileManager) when it wants to create a JavaFileObject for the
 * .class output. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, JavaFileObject.Kind)}
 * constructor.
 * </ol>
 * This class does not attempt to reuse instances (there does not seem to be a
 * need, as it would require adding a Map for the purpose, and this would also
 * prevent garbage collection of class byte code.)
 */
final class JavaFileObjectImpl extends SimpleJavaFileObject {
   // If kind == CLASS, this stores byte code from openOutputStream
   private ByteArrayOutputStream byteCode;

   // if kind == SOURCE, this contains the source text
   private final CharSequence source;

   /**
    * Construct a new instance which stores source
    * 
    * @param baseName
    *           the base name
    * @param source
    *           the source code
    */
   JavaFileObjectImpl(final String baseName, final CharSequence source) {
      super(JavaCompiler.toURI(baseName + JavaCompiler.JAVA_EXTENSION),
            Kind.SOURCE);
      this.source = source;
   }

   /**
    * Construct a new instance
    * 
    * @param name
    *           the file name
    * @param kind
    *           the kind of file
    */
   JavaFileObjectImpl(final String name, final Kind kind) {
      super(JavaCompiler.toURI(name), kind);
      source = null;
   }

   /**
    * Return the source code content
    * 
    * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
    */
   @Override
   public CharSequence getCharContent(final boolean ignoreEncodingErrors)
         throws UnsupportedOperationException {
      if (source == null)
         throw new UnsupportedOperationException("getCharContent()");
      return source;
   }

   /**
    * Return an input stream for reading the byte code
    * 
    * @see javax.tools.SimpleJavaFileObject#openInputStream()
    */
   @Override
   public InputStream openInputStream() {
      return new ByteArrayInputStream(getByteCode());
   }

   /**
    * Return an output stream for writing the bytecode
    * 
    * @see javax.tools.SimpleJavaFileObject#openOutputStream()
    */
   @Override
   public OutputStream openOutputStream() {
      byteCode = new ByteArrayOutputStream();
      return byteCode;
   }

   /**
    * @return the byte code generated by the compiler
    */
   byte[] getByteCode() {
      return byteCode.toByteArray();
   }
}

/**
 * A custom ClassLoader which maps class names to JavaFileObjectImpl instances.
 */
final class ClassLoaderImpl extends ClassLoader {
   private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

   ClassLoaderImpl(final ClassLoader parentClassLoader) {
      super(parentClassLoader);
   }

   public void outputClassesToJar(String qualifiedClassName, OutputStream output) throws IOException {
      Manifest manifest = new Manifest();
      manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
      manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, qualifiedClassName);
   
      try (JarOutputStream target = new JarOutputStream(output, manifest)) {
         for (Entry<String, JavaFileObject> entry : classes.entrySet()) {
            String className = entry.getKey();
            JavaFileObjectImpl file = (JavaFileObjectImpl) entry.getValue();
            JarEntry jarEntry = new JarEntry(className);
            jarEntry.setTime(file.getLastModified());
            target.putNextEntry(jarEntry);
            target.write(file.getByteCode());
            target.closeEntry();
         }
      }  
   }

   /**
    * @return An collection of JavaFileObject instances for the classes in the
    *         class loader.
    */
   Collection<JavaFileObject> files() {
      return Collections.unmodifiableCollection(classes.values());
   }

   @Override
   protected Class<?> findClass(final String qualifiedClassName)
         throws ClassNotFoundException {
      JavaFileObject file = classes.get(qualifiedClassName);
      if (file != null) {
         byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
         return defineClass(qualifiedClassName, bytes, 0, bytes.length);
      }
      // Workaround for "feature" in Java 6
      // see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
      try {
         Class<?> c = Class.forName(qualifiedClassName);
         return c;
      } catch (ClassNotFoundException nf) {
         // Ignore and fall through
      }
      return super.findClass(qualifiedClassName);
   }

   /**
    * Add a class name/JavaFileObject mapping
    * 
    * @param qualifiedClassName
    *           the name
    * @param javaFile
    *           the file associated with the name
    */
   void add(final String qualifiedClassName, final JavaFileObject javaFile) {
      classes.put(qualifiedClassName, javaFile);
   }

   @Override
   protected synchronized Class<?> loadClass(final String name, final boolean resolve)
         throws ClassNotFoundException {
      return super.loadClass(name, resolve);
   }

   @Override
   public InputStream getResourceAsStream(final String name) {
      if (name.endsWith(".class")) {
         String qualifiedClassName = name.substring(0,
               name.length() - ".class".length()).replace('/', '.');
         JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
         if (file != null) {
            return new ByteArrayInputStream(file.getByteCode());
         }
      }
      return super.getResourceAsStream(name);
   }
}
