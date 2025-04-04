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
import java.util.jar.JarInputStream;
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
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;

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
      Map<String, Class<?>> compiled = compile(classes, diagnostics);
      @SuppressWarnings("unchecked")
      Class<T> newClass = (Class<T>) compiled.get(qualifiedClassName);
      return(Class<T>) castable((Class<T>) newClass, types);
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
   public synchronized Map<String, Class<?>> compile(final Map<String, CharSequence> classes, DiagnosticCollector<JavaFileObject> diagnostics) throws JavaCompilerException {
      if (diagnostics == null) {
         diagnostics = new DiagnosticCollector<>();
      }

      try {
         List<JavaFileObject> sources = registerSourceFiles(classes);

         // Get a CompliationTask from the compiler and compile the sources
         CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, options, null, sources);
         Boolean result = task.call();
         
         if (result == null || !result.booleanValue()) {
            throw new JavaCompilerException("Compilation failed.", classes.keySet(), diagnostics);
         }
      
         // For each class name in the inpput map, get its compiled
         // class and put it in the output map
         Map<String, Class<?>> compiled = new HashMap<String, Class<?>>();
         for (String qualifiedClassName : classes.keySet()) {
            final Class<T> newClass = loadClass(qualifiedClassName);
            compiled.put(qualifiedClassName, newClass);
         }
         
         return compiled;
      } 
      catch (ClassNotFoundException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      } 
      catch (IllegalArgumentException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      } 
      catch (SecurityException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      }
      catch (URISyntaxException e) {
         throw new JavaCompilerException(classes.keySet(), e, diagnostics);
      }
   }

   /**
    * Compile multiple Java source strings and return the error messages
    * <p>
    * Thread safety: this method is thread safe if the <var>classes</var> and
    * <var>diagnosticsList</var> are isolated to this thread.
    * 
    * @param classes
    *           A Map whose keys are source locations and whose values are
    *           the Java source strings containing the definition of the class.
    *           A map value may be null, indicating that compiled class is
    *           expected, although no source exists for it (it may be a
    *           non-public class contained in one of the other strings.)
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to
    *           this list.
    * @param target : where to write the class files to (jar or folder)
    * @return A mapping of qualified class names to their corresponding classes.
    *         The map has the same keys as the input <var>classes</var>; the
    *         values are the corresponding Class objects.
    * @throws JavaCompilerException
    *            if the source cannot be compiled
   * @throws URISyntaxException 
   */
   public synchronized void compileTo(ISet classes, ClassLoader loader, ISourceLocation target, DiagnosticCollector<JavaFileObject> diagnostics)
      throws JavaCompilerException, URISyntaxException {
      if (diagnostics == null) {
         diagnostics = new DiagnosticCollector<>();
      }
 
      List<JavaFileObject> sources = registerSourceFiles(classes);

      // Get a CompliationTask from the compiler and compile the sources
      final CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, options, null, sources);
      
      task.call();

      var reg = URIResolverRegistry.getInstance();

      // Write the bytes to their respective file URIs.
      // Notice that there are possible many more .class files then there were java source files,
      // because of nested and anonymous classes in their source code.
      for (Entry<JavaFileObjectImpl, byte[]> e : javaFileManager.getAllClassBytes().entrySet()) {
         var output = URIUtil.getChildLocation(target, e.getKey().toUri().getPath().replaceAll("\\.", "/"));
         output = URIUtil.changeExtension(output, "class");

         // this is where we implement support for any target scheme
         try (OutputStream out = reg.getOutputStream(output, false)) {
            out.write(e.getValue());
         }
         catch (IOException x) {
            throw new JavaCompilerException(java.util.Set.of(e.getKey().toUri().getPath()), x, diagnostics);
         }
      }

      return;
   }

   private List<JavaFileObject> registerSourceFiles(ISet classes) throws URISyntaxException {
      List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
      
      for (IValue entry : classes) {
         ISourceLocation sloc = (ISourceLocation) ((ITuple) entry).get(0);
         String qname = ((IString) ((ITuple) entry).get(1)).getValue();
         CharSequence javaSource = ((IString) ((ITuple) entry).get(2)).getValue();

         if (javaSource != null) {
            final JavaFileObjectImpl source = new JavaFileObjectImpl(sloc, qname.replaceAll("\\.", "/"), javaSource);
            sources.add(source);

            int dotPos = qname.lastIndexOf('.');
            String className = dotPos == -1 ? qname : qname.substring(dotPos + 1);
            String packageName = dotPos == -1 ? "" : qname.substring(0, dotPos);

            // Store the source file in the FileManager via package/class
            // name.
            // For source files, we add a .java extension
            javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION, source);
         }
      }

      return sources;
   }

   private List<JavaFileObject> registerSourceFiles(final Map<String, CharSequence> classes) throws URISyntaxException {
      List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
      
      for (Entry<String, CharSequence> entry : classes.entrySet()) {
         String qualifiedClassName = entry.getKey();
         CharSequence javaSource = entry.getValue();
         if (javaSource != null) {
            int dotPos = qualifiedClassName.lastIndexOf('.');
            String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring(dotPos + 1);
            String packageName = dotPos == -1 ? "" : qualifiedClassName.substring(0, dotPos);
            
            JavaFileObjectImpl source = new JavaFileObjectImpl(className, javaSource);
            sources.add(source);
            javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION, source);
         }
      }

      return sources;
   }

   /**
    * This is for the parser generator and saving the generated .class files into an opaqua jar
    * @param classBytes
    * @param qualifiedClassName
    * @param classSource
    * @param diagnostics
    * @throws JavaCompilerException
    */
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

   public Class<?> load(InputStream file) throws IOException, ClassNotFoundException, URISyntaxException {
      return classLoader.inputClassesFromJar(file);
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

   static URI toURI(ISourceLocation loc) {
      return loc.getURI();
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
    * Get access to all te generated bytecode for all classes in this FileManagerImpl
    * @return
    */
   public Map<JavaFileObjectImpl, byte[]> getAllClassBytes() {
      var result = new HashMap<JavaFileObjectImpl, byte[]>();

      for (JavaFileObjectImpl e : classLoader.files()) {
         if (e.getKind() == Kind.CLASS) {
            result.put(e, e.getByteCode());
         }
      }

      return result;
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
   public void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
      fileObjects.put(uri(location, packageName, relativeName), file);
   }

   /**
    * Convert a location and class name to a URI
    */
   private URI uri(Location location, String packageName, String relativeName) {
      return JavaCompiler.toURI(location.getName() + '/' + packageName + '/' + relativeName);
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
            try {
               JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
               classLoader.add(qualifiedName, file);
               return file;
            }
            catch (URISyntaxException e) {
               throw new IOException(e);
            }
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
 * A custom ClassLoader which maps class names to JavaFileObjectImpl instances.
 */
final class ClassLoaderImpl extends ClassLoader {
   private final Map<String, JavaFileObjectImpl> classes = new HashMap<String, JavaFileObjectImpl>();

   ClassLoaderImpl(final ClassLoader parentClassLoader) {
      super(parentClassLoader);
   }

   /**
    * This writes all the classes related to one generated parser class to a specific form
    * of jar file outputstream. The files are not organized in the normal way so this jar
    * can not be used as a normal jar file! The reason is that generated parser binary format
    * might change in the future and so the entire format is opaque.
    */
   public void outputClassesToJar(String qualifiedClassName, OutputStream output) throws IOException {
      Manifest manifest = new Manifest();
      manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
      manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, qualifiedClassName);
   
      try (JarOutputStream target = new JarOutputStream(output, manifest)) {
         for (Entry<String, JavaFileObjectImpl> entry : classes.entrySet()) {
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
    * This reconstructs an executable parser class from a jar that was created 
    * by {@see outputClassesToJar}.
    */
   public Class<?> inputClassesFromJar(InputStream in) throws IOException, ClassNotFoundException, URISyntaxException {
      try (JarInputStream jarIn = new JarInputStream(in)) {
         Manifest mf = jarIn.getManifest();
         String mainClass = (String) mf.getMainAttributes().get(Attributes.Name.MAIN_CLASS);
         JarEntry jarEntry;

         if (mainClass == null) {
            throw new IOException("missing Main-Class in jar manifest");
         }

         while ((jarEntry = jarIn.getNextJarEntry()) != null) {
            if (!jarEntry.isDirectory()) {
               var className = jarEntry.getName();
               var file = new JavaFileObjectImpl(className, JavaFileObject.Kind.CLASS);
               
               try (var fo = file.openOutputStream()) {
                  fo.write(Prelude.consumeInputStream(jarIn));
               }

               add(className, file);
            }
         }

         return loadClass(mainClass);
      }
   }

   /**
    * @return An collection of JavaFileObject instances for the classes in the
    *         class loader.
    */
   Collection<JavaFileObjectImpl> files() {
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
      classes.put(qualifiedClassName, (JavaFileObjectImpl) javaFile);
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
