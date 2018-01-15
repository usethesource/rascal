package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.rascalmpl.library.util.Reflective;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.binary.stream.IValueInputStream;
import org.rascalmpl.values.ValueFactoryFactory;

import static org.rascalmpl.values.uptr.RascalValueFactory.TYPE_STORE_SUPPLIER;

/**
 * This program is intended to be executed directly from maven; it downloads a previous version of Rascal from a hard-wired location and uses 
 * this to start a bootstrap cycle to eventually arrive at a compiled Rascal compiler which is copied to the target folder of the maven build.
 * This same program also runs a number of tests with every stage of the bootstrap to try and fail if problems have been introduced.
 * 
 * In this manner the maven target always contained a fully tested and freshly bootstrapped binary compiler.
 */
public class Bootstrap {
    private static Process childProcess;
    private static boolean VERBOSE = false;
    private static final String[] testModules = {
            "lang::rascal::tests::basic::Booleans",
            "lang::rascal::tests::basic::Equality",
            "lang::rascal::tests::basic::Exceptions",
            "lang::rascal::tests::basic::Functions",
            "lang::rascal::tests::basic::Integers",
            "lang::rascal::tests::basic::IO",
            "lang::rascal::tests::basic::IsDefined",
            "lang::rascal::tests::basic::ListRelations",
            "lang::rascal::tests::basic::Lists",
            "lang::rascal::tests::basic::Locations",
            "lang::rascal::tests::basic::Maps",
            "lang::rascal::tests::basic::Overloading",
            "lang::rascal::tests::basic::Nodes",
            "lang::rascal::tests::basic::Memoization",
            "lang::rascal::tests::basic::Relations",
            "lang::rascal::tests::basic::Sets",
            "lang::rascal::tests::basic::Strings",
            "lang::rascal::tests::basic::Tuples" };
    
    private static final String[] syntaxTestModules = { 
            "lang::rascal::tests::basic::Matching",
            "lang::rascal::tests::functionality::ConcreteSyntaxTests1",
            "lang::rascal::tests::functionality::ConcreteSyntaxTests2",
            "lang::rascal::tests::functionality::ConcreteSyntaxTests3",
            "lang::rascal::tests::functionality::ConcreteSyntaxTests4",
            "lang::rascal::tests::functionality::ConcreteSyntaxTests5",
    };

    public static class BootstrapMessage extends Exception {
		private static final long serialVersionUID = -1L;
		protected int phase;

        public BootstrapMessage(int phase) {
            this.phase = phase;
        }

        @Override
        public String getMessage() {
            return "Failed during phase " + phase;
        }
    }
    
    private static class BootTiming {
        public final String message;
        public long duration;
        public BootTiming(String message) {
            this.message = message;
        }
    }
    private static final List<BootTiming> timings = new ArrayList<>();

    private static <T> T time(String message, ThrowingSupplier<T> target) throws Exception {
        BootTiming currentTimings = new BootTiming(message);
        timings.add(currentTimings); // reserve spot 
        long start = System.nanoTime();
        T result = target.throwingGet();
        currentTimings.duration = System.nanoTime() - start;
        return result;
    }

    private static void time(String message, ThrowingSideEffectOnly target) throws Exception {
        time(message, () -> target.call());
    }
    @FunctionalInterface
    public interface ThrowingSideEffectOnly {
        default Void call() throws Exception {
            actualCall();
            return null;
        }
        void actualCall() throws Exception;
    }
    
    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T throwingGet() throws Exception;
    }
    
    private static void printTimings() {
        Optional<Integer> maxLength = timings.stream().map(b -> b.message).map(s -> s.length()).max(Comparator.naturalOrder());
        int labelWidth = maxLength.orElse(1) + 4;
        System.err.println("---------------------");
        System.err.println("Bootstrapping time:");
        System.err.println("---------------------");
        for (BootTiming bt: timings) {
            System.err.println(String.format("%-"+labelWidth+"s : %,d ms", bt.message, TimeUnit.NANOSECONDS.toMillis(bt.duration)));
        }
        System.err.println("---------------------");
    }
    
    public static void main(String[] args) throws Exception {
        initializeShutdownhook();

        if (args.length < 5) {
        	System.err.println("Usage: Bootstrap <classpath> <versionToBootstrapOff> <versionToBootstrapTo> <sourceFolder> <targetFolder> [--verbose] [--clean] [--basic] [--download] [--validating] (you provided " + args.length + " arguments instead)");
        	System.exit(1);
        	return;
        }
        
        int arg = 0;
        String classpath = args[arg++];
        String versionToUse = args[arg++];
        String versionToBuild = args[arg++];
        
        if (versionToUse.equals("unstable")) {
            info("YOU ARE NOT SUPPOSED TO BOOTSTRAP OFF AN UNSTABLE VERSION! ***ONLY FOR DEBUGGING PURPOSES***");
        }
        
        if (versionToUse.equals("latest")) {
            info("YOU ARE BOOTSTRAPPING OFF THE LATEST RELEASE");
        }
        
        Path sourceFolder = new File(args[arg++]).toPath();
        if (!Files.exists(sourceFolder.resolve("org/rascalmpl/library/Prelude.rsc"))) {
        	throw new RuntimeException("source folder " + sourceFolder + " should point to source folder of standard library containing Prelude and the compiler");
        }
        
        info("sourceFolder: " + sourceFolder);
        
        String librarySource = sourceFolder.resolve("org/rascalmpl/library").toAbsolutePath().toString();
        
        Path targetFolder = new File(args[arg++]).toPath();
        if (!Files.exists(targetFolder.resolve("org/rascalmpl/library/Prelude.class"))) {	// PK: PreludeCompiled
        	throw new RuntimeException("target folder " + sourceFolder + " should point to source folder of compiler library and the RVM interpreter.");
        }
        
        Path tmpFolder = new File(args[arg++]).toPath();
        Path bootstrapMarker = targetFolder.resolve("META-INF/bootstrapped.version");
        if (Files.exists(bootstrapMarker)) {
            System.err.println("Not bootstrapping, since " + bootstrapMarker + " already exists");
            System.exit(0);
        }
        
        boolean cleanTempDir = false;
        boolean basicOption = false;
        boolean validatingOption = false;
        
        for (;arg < args.length; arg++) {
            switch (args[arg]) {
                case "--verbose": VERBOSE=true; break;
                case "--clean": cleanTempDir = true; break;
                case "--basic" : basicOption = true; break;
                case "--download" : basicOption = false; break;
                case "--validating" : validatingOption = true; break;
                default: 
                    System.err.println(args[arg] + " is not a supported argument.");
                    System.exit(1);
                    return;
            }
        }
        
        final boolean realBootstrap = basicOption || validatingOption;
        final boolean validatingBootstrap = validatingOption;
        
        Path tmpDir = initializeTemporaryFolder(tmpFolder, cleanTempDir, versionToUse);
        
        if (existsDeployedVersion(versionToBuild)) {
            info("Got the kernel version to compile: " + versionToBuild + " already from existing deployed build.");
        }
        
        // We bootstrap in several stages, in each step generating a new Kernel file using an existing version:
        //    -1. targetFolder contains what is found online already:
        //        - compiled classes for new RVM classes (newRVMClasses)
        //        - copied new source files of the Rascal compiler (newRascalCompilerSources) and library (newLibrarySources)
        //    0. download a released version (phase0)
        //       contains:
        //       - class files of compiled RVM Java code (OldRVMClasses)
        //       - linked Kernel (OldKernel = Kernel.rvm.ser.gz, muLibrary.rvm.gz, ParserGenerator.rvm.ser.gz + all compiled Rascal libraries)
        //       - old source code of the Rascal compiler (OldCompilerSources)
        //    1. build new kernel with old jar (OldRVMClasses) using OldKernel from the newCompilerSources (from librarySource), creating phase1
        //       phase1 consists of:
        //       - linked Kernel (newKernel1)
        //    2. build newKernel2 using newKernel1 and OldRVMClasses, because newKernel1 was still compiled with the old compiler)
        //    3. build newKernel3 using newKernel2 with newRVMClasses using newKernel2,  because newKernel2 was compiled with new compiler which may depend on changes in the RVM.
       
        time("Bootstrap:", () -> {
            try { 
                
                String[] rvm    = new String[] { 
                        getDeployedVersion(tmpDir, versionToUse).toAbsolutePath().toString(), // this is the released jar
                        targetFolder + File.pathSeparator + /*deps*/ classpath /* this is the pre-compiled target folder with the new RVM implementation */  
                };
                
                
                String[] kernel = new String[] {
                        "|boot:///|",  // This is retrieved from the released jar
                        phaseFolderString(1, tmpDir), 
                        phaseFolderString(2, tmpDir),  
                        phaseFolderString(3, tmpDir)
                };
                  
                if (!realBootstrap) {
                  time("Copying downloaded files", () -> copyJar(jarFileSystem(rvm[0]).getPath("boot"), targetFolder));
                  System.exit(0);
                }
                
                /*------------------------------------------,-CODE---------,-RVM---,-KERNEL---,-TESTS--*/
                time("Phase 1", () -> compilePhase(tmpDir, 1, librarySource, rvm[0], kernel[0], rvm[1], "|noreloc:///|", targetFolder));
                
                // Identify jars that contain new names and should be included in the classpath
                String renamedJars = "";// System.getProperty("user.home") + "/.m2/repository/io/usethesource/vallang/0.7.0-SNAPSHOT/vallang-0.7.0-SNAPSHOT.jar";
                if(!renamedJars.isEmpty()){
                    rvm[0] += File.pathSeparator + renamedJars;
                    rvm[1] += File.pathSeparator + renamedJars;
                }
                
                time("Phase 2", () -> compilePhase(tmpDir, 2, librarySource, rvm[0], kernel[1], rvm[1], "|std:///|", targetFolder));
                
                if(validatingBootstrap){
                  time("Phase 3", () -> compilePhase(tmpDir, 3, librarySource, rvm[1], kernel[2], rvm[1], "|std:///|", targetFolder));
                  try {
                    time("Validation", () -> {
                      long nfiles = compareGeneratedRVMCode(Paths.get(kernel[2]), Paths.get(kernel[3]));
                      progress("VALIDATION: All " + nfiles + " *.rvm files in Phase 2 and Phase 3 are identical");
                    });
                  }
                  catch (Exception e) {
                    error("Comparison between Phase 2 and Phase 3 failed: " + e.getMessage());
                    System.exit(1);
                  }
                }
                
                // The result of the final compilation phase is copied to the bin folder such that it can be deployed with the other compiled (class) files
                time("Copying bootstrapped files", () -> copyResult(new File(kernel[2]).toPath(), targetFolder.resolve("boot")));

                Files.write(bootstrapMarker, versionToUse.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
            } 
            catch (BootstrapMessage | IOException | InterruptedException e) {
                error(e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } 
        });
        printTimings();
    }

    private static FileSystem jarFileSystem(String jarFile) throws IOException, URISyntaxException {
        return FileSystems.newFileSystem(new URI("jar", new File(jarFile).toURI().toString(), null), Collections.singletonMap("create", true));
    }

    private static void initializeShutdownhook() {
        Thread destroyChild = new Thread() {
            public void run() {
                synchronized (Bootstrap.class) {
                    if (childProcess != null && childProcess.isAlive()) {
                        childProcess.destroy();
                    }
                }
            }
        };
        
        Runtime.getRuntime().addShutdownHook(destroyChild);
    }

    private static Path initializeTemporaryFolder(Path tmpDir, boolean cleanTempDir, String versionToUse) {
        if (cleanTempDir && Files.exists(tmpDir)) {
            info("Removing files in " + tmpDir.toString());
            try {
                Files.walkFileTree(tmpDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                      try {
                        Files.delete(dir);
                      }
                      catch (DirectoryNotEmptyException e) {
                        // that's ok when we've left the jar file in it.
                      }
                      
                      return super.postVisitDirectory(dir, exc);
                    }
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                      if (!file.equals(cachedDeployedVersion(tmpDir, versionToUse))) {
                        // we don't delete downloaded versions automatically to make sure we can
                        // keep running bootstrap while offline in trains and airplanes
                        Files.delete(file);
                      }
                      
                      return super.visitFile(file, attrs);
                    }

                });
            } catch (IOException e) {
                System.err.println(e);
                System.err.println("Error cleaning temp directory");
                System.exit(1);
                return null;
            }
        }
        tmpDir.toFile().mkdir();
        info("bootstrap folder: " + tmpDir.toAbsolutePath());
        return tmpDir;
    }
    
    // TODO: merge code with copyResult; should be possible imho, but ZipPath.relativize throws exceptions on Paths in the jar
	private static void copyJar(Path sourcePath, Path targetPath) throws IOException {
	    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult preVisitDirectory(final Path dir,  final BasicFileAttributes attrs) throws IOException {
	            Files.createDirectories(Paths.get(targetPath.toString(), dir.toString()));
	            if (dir.toString().contains("courses")) {
	                return FileVisitResult.SKIP_SUBTREE;
	            }
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
	            info("Copying " + file);
	            Files.copy(file, Paths.get(targetPath.toString(), file.toString()), StandardCopyOption.REPLACE_EXISTING);
	            return FileVisitResult.CONTINUE;
	        }
	    });
    }
	
	private static void copyResult(Path sourcePath, Path targetPath) throws IOException {
      Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult preVisitDirectory(final Path dir,  final BasicFileAttributes attrs) throws IOException {
              Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
              //info("Copying " + file + " to " + targetPath.resolve(sourcePath.relativize(file)));
              Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
              return FileVisitResult.CONTINUE;
          }
      });
  }

    private static boolean existsDeployedVersion(String version) {	
		try (InputStream s = deployedVersion(version).toURL().openStream()) {
			return s != null;
		} catch (IOException e) {
			return false;
		} 
	}
    
    /**
     * Either download or get the jar of the deployed version of Rascal from a previously downloaded instance.
     */
    private static Path getDeployedVersion(Path tmp, String version) throws IOException {
        Path cached = cachedDeployedVersion(tmp, version);

        if (!cached.toFile().exists() || "unstable".equals(version)) {
            if (cached.toFile().exists()) {
                cached.toFile().delete();
            }
            URI deployedVersion = deployedVersion(version);
            info("downloading " + deployedVersion);
            Files.copy(deployedVersion.toURL().openStream(), cached);
        }

        info("deployed version ready: " + cached);
        return cached;
    }

    private static Path cachedDeployedVersion(Path tmpFolder, String version) {
    	return tmpFolder.resolve("rascal-" + version + ".jar");
	}

	private static URI deployedVersion(String version) {
	    if ("unstable".equals(version)) {
	        return unstableVersion();
	    }
	    else if ("latest".equals(version)) {
	        return latestReleasedVersion();
	    }

	    return URIUtil.assumeCorrect("https", "update.rascal-mpl.org", "/console/rascal-" + version + ".jar");
	}
	
	private static URI unstableVersion() {
        return URIUtil.assumeCorrect("https", "update.rascal-mpl.org", "/console/rascal-shell-unstable.jar");
    }
	
	private static URI latestReleasedVersion() {
        return URIUtil.assumeCorrect("http", "nexus.usethesource.io", "/service/local/artifact/maven/content", "g=org.rascalmpl&a=rascal&r=releases&v=LATEST");
    }
	
	private static String phaseFolderString(int phase, Path tmp) {
        Path result = tmp.resolve("phase" + phase);
        result.toFile().mkdir();
        return result.toAbsolutePath().toString();
    }
	
    private static Path phaseFolder(int phase, Path tmp) {
        Path result = tmp.resolve("phase" + phase);
        result.toFile().mkdir();
        return result;
    }
    
    private static Path phaseTestFolder(int phase, Path tmp) {
        Path result = tmp.resolve("phase-test" + phase);
        result.toFile().mkdir();
        return result;
    }
    
    private static Path compilePhase(Path tmp, int phase, String sourcePath, String classPath, String bootPath, String testClassPath, String reloc, Path targetFolder) throws Exception {
      Path phaseResult = phaseFolder(phase, tmp);
      Path testResults = phaseTestFolder(phase, tmp);
      progress("phase " + phase + ": " + phaseResult);

      time("- compile MuLibrary",       () -> compileMuLibrary(phase, classPath, bootPath, sourcePath, phaseResult, reloc));
      time("- compile Kernel",          () -> compileModule   (phase, classPath, bootPath, sourcePath, phaseResult, "lang::rascal::boot::Kernel", reloc));

      if (phase == 1) {
          // the new parser generator would refer to classes which may not exist yet. Until stage 2 we still run against this old version
          // we now copy an old version of the generator to be used in phase 2.
          copyParserGenerator(jarFileSystem(classPath).getRootDirectories().iterator().next(), phaseResult);
      }
      else {
          time("- compile ParserGenerator", () -> compileModule   (phase, classPath, bootPath, sourcePath, phaseResult, "lang::rascal::grammar::ParserGenerator", reloc));
      }
      
      if(phase == 2){
          time("- generate and compile RascalParser", () -> generateAndCompileRascalParser(phase, classPath, sourcePath, bootPath, phaseResult, targetFolder));
      }

      if (phase >= 2) {
          // phase 1 tests often fail for no other reason than an incompatibility.
          time("- compile simple tests",           () -> compileTests    (phase, classPath, phaseResult.toAbsolutePath().toString(), sourcePath, testResults, testModules));
          time("- run simple tests",               () -> runTests        (phase, testClassPath, phaseResult.toAbsolutePath().toString(), sourcePath, testResults, testModules));
      }
      
      if (phase > 2) {
          // phase 2 tests can not succeed in case the parser generator changed, so we can only test this after phase 3 has completed
          time("- compile syntax tests",           () -> compileTests    (phase, classPath, phaseResult.toAbsolutePath().toString(), sourcePath, testResults, syntaxTestModules));
          time("- run syntax tests",               () -> runTests        (phase, testClassPath, phaseResult.toAbsolutePath().toString(), sourcePath, testResults, syntaxTestModules));
      }
      
      return phaseResult;
    }

    private static void generateAndCompileRascalParser(int phase, String classPath, String sourcePath, String bootPath, Path phaseResult, Path targetFolder) throws IOException, InterruptedException, BootstrapMessage {
//        if (
            bootstrapRascalParser(classPath, sourcePath, bootPath, phaseResult); 
//            != 0) {
//            throw new BootstrapMessage(phase);
//        }
        
        String[] paths = new String [] { sourcePath + "/lang/rascal/syntax/RascalParser.java" };
        if (runJavaCompiler(classPath, targetFolder.toAbsolutePath().toString(), concat(paths)) != 0) {
            throw new BootstrapMessage(phase);
        }
    }
    
    private static int bootstrapRascalParser(String classPath, String srcPath, String bootPath, Path phaseResult) throws IOException, InterruptedException {
        String[] javaCmd = new String[] {"java", "-cp", classPath, "-Xmx2G", "-Dfile.encoding=UTF-8", "org.rascalmpl.library.experiments.Compiler.Commands.BootstrapRascalParser"};
        String[] paths = new String[] {"--src", srcPath, "--bin", phaseResult.toAbsolutePath().toString(), "--boot", bootPath };
        return runChildProcess(concat(javaCmd, paths));
    }

    private static void copyParserGenerator(Path jar, Path phaseResult) throws IOException {
        Files.copy(jar.resolve("boot/lang/rascal/grammar/ParserGenerator.rvmx"), phaseResult.resolve("lang/rascal/grammar/ParserGenerator.rvmx"));
    }

    private static String[] concat(String[]... arrays) {
        return Stream.of(arrays).flatMap(Stream::of).toArray(sz -> new String[sz]);
    }
    
    private static void compileTests(int phase, String classPath, String boot, String sourcePath, Path phaseResult, String[] testModules) throws IOException, InterruptedException, BootstrapMessage {
        progress("\tcompiling tests (phase " + phase +")");
        String[] paths = new String [] { "--bin", phaseResult.toAbsolutePath().toString(), "--src", sourcePath, "--boot", boot };
        String[] otherArgs = VERBOSE? new String[] {"--verbose"} : new String[] {};

        if (runRascalCompiler(classPath, concat(concat(paths, otherArgs), testModules)) != 0) {
            throw new BootstrapMessage(phase);
        }
    }
    
    private static void compileModule(int phase, String classPath, String boot, String sourcePath, Path phaseResult,
            String module, String reloc) throws IOException, InterruptedException, BootstrapMessage {
        progress("\tcompiling " + module + " (phase " + phase +")");
       
        String[] paths;
        paths = phase >= 2 ? new String [] { "--bin", phaseResult.toAbsolutePath().toString(), "--src", sourcePath, "--boot", boot , "--reloc", reloc }
                           : new String [] { "--bin", phaseResult.toAbsolutePath().toString(), "--src", sourcePath, "--boot", boot};
        String[] otherArgs = VERBOSE? new String[] {"--verbose", module} : new String[] {module};

        if (runRascalCompiler(classPath, concat(paths, otherArgs)) != 0) {
            throw new BootstrapMessage(phase);
        }
    }
    
    private static void compileMuLibrary(int phase, String classPath, String bootDLoc, String sourcePath, Path phaseResult, String reloc) throws IOException, InterruptedException, BootstrapMessage {
        progress("\tcompiling MuLibrary (phase " + phase +")");
        
        String[] paths = new String [] { "--bin", phaseResult.toAbsolutePath().toString(), "--src", sourcePath, "--boot", bootDLoc, "--reloc", reloc };
        String[] otherArgs = VERBOSE? new String[] {"--verbose"} : new String[0];

        if (runMuLibraryCompiler(classPath, concat(paths, otherArgs)) != 0) {
            throw new BootstrapMessage(phase);
        }
    }
    
    private static void runTests(int phase, String classPath, String boot, String sourcePath, Path phaseResult, String[] testModules) throws IOException, InterruptedException, BootstrapMessage {
        progress("Running tests with the results of " + phase);
        if (phase == 1) return;
        String[] javaCmd = new String[] {"java", "-ea", "-cp", classPath, "-Xmx2G", "-Dfile.encoding=UTF-8", "-Dfile.encoding=UTF-8", "org.rascalmpl.library.experiments.Compiler.Commands.RascalTests" };
        String[] paths = new String [] { "--bin", phaseResult.toAbsolutePath().toString(), "--src", sourcePath, "--boot", boot };
        String[] otherArgs = VERBOSE? new String[] {"--verbose"} : new String[0];

        if (runChildProcess(concat(javaCmd, paths, otherArgs, testModules)) != 0) { 
            throw new BootstrapMessage(phase);
        }
    }
    
    private static int runJavaCompiler(String classPath, String targetFolder, String... arguments) throws IOException, InterruptedException {
        String[] javaCmd = new String[] {"javac", "-cp", classPath, "-encoding", "UTF-8", "-d", targetFolder };
        return runChildProcess(concat(javaCmd, arguments));
    }

    private static int runRascalCompiler(String classPath, String... arguments) throws IOException, InterruptedException {
        /*
         * Remote Debugging Example:
         *     -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n
         * 
         * Flags: 
         *     suspend=n - starts up and does not wait for attaching a debugger
         *     suspend=y - waits until a debugger is attached before to proceed
         */
        String[] javaCmd = new String[] {"java", "-cp", classPath, "-Xmx2G", "-Dfile.encoding=UTF-8", /*"-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n",*/ "org.rascalmpl.library.experiments.Compiler.Commands.RascalC" };
        return runChildProcess(concat(javaCmd, arguments));
    }

    private static int runMuLibraryCompiler(String classPath, String... arguments) throws IOException, InterruptedException {
        String[] javaCmd = new String[] {"java", "-cp", classPath, "-Xmx2G", "-Dfile.encoding=UTF-8", "org.rascalmpl.library.experiments.Compiler.Commands.CompileMuLibrary" };
    	return runChildProcess(concat(javaCmd, arguments));
    }
    
    private static int runChildProcess(String[] command) throws IOException, InterruptedException {
        synchronized (Bootstrap.class) {
            info("command: " + Arrays.stream(command).reduce("", (x,y) -> x + " " + y));
            childProcess = new ProcessBuilder(command).inheritIO().start();
            childProcess.waitFor();
            int exitValue = childProcess.exitValue();
            if (exitValue != 0) {
                error("Command failed: " + Arrays.stream(command).reduce("", ((s,e) -> s + (safeString(e.toString()) + " "))));
            }
            return exitValue;
        }
    }
    
    private static String safeString(String x) {
        x = x.trim();
        if (x.startsWith("|")) {
            return "\"" + x + "\"";
        }
        
        return x;
    }
    private static void progress(String msg) {
         System.err.println("BOOTSTRAP:" + msg);
    }
    private static void info(String msg) {
        if (VERBOSE) {
            System.err.println("BOOTSTRAP:" + msg);
        }
    }
    private static void error(String msg) {
        System.err.println("BOOTSTRAP:" + msg);
    }
    
    /**
     * Assert that two directories are recursively "equal" by checking that
     * - corresponding (sub)directories and files exist in expected and actual directory
     * - of .rvm files actual content is compared.
     * 
     * If they are not, an {@link RuntimeException} is thrown with the given message.<br/>
     * Missing or additional files are considered an error.<br/>
     * 
     * @param expected
     *            Path expected directory
     * @param actual
     *            Path actual directory
     */
    public static final long compareGeneratedRVMCode(final Path expected, final Path actual) {
       
        class RVMFileCompareAndCount implements FileVisitor<Path> {

          final Path absoluteExpected;
          final Path absoluteActual;
          int nfiles = 0;
          IValueFactory vf;

          RVMFileCompareAndCount(final Path expected, final Path actual){
            absoluteExpected = expected.toAbsolutePath();
            absoluteActual = actual.toAbsolutePath();
            vf = ValueFactoryFactory.getValueFactory();
          }

          int getCount() { return nfiles; }
          
          private IValue read(Path path) throws IOException {
              ISourceLocation loc = null;
              try {
                  loc = vf.sourceLocation("file", "", path.toString());
              }
              catch (URISyntaxException e1) {
                  throw new IOException("Cannot create location |file://" + path.toString() + "|");
              }
              try (IValueInputStream in = new IValueInputStream(URIResolverRegistry.getInstance().getInputStream(loc), vf, TYPE_STORE_SUPPLIER)) {
                  return in.read();
              }
          }

          @Override
          public FileVisitResult preVisitDirectory(Path expectedDir, BasicFileAttributes attrs)
              throws IOException {
            Path relativeExpectedDir = absoluteExpected.relativize(expectedDir.toAbsolutePath());
            Path actualDir = absoluteActual.resolve(relativeExpectedDir);

            if (!Files.exists(actualDir)) {
              throw new RuntimeException(String.format("Directory \'%s\' missing in actual.", expectedDir.getFileName()));
            }

            if(expectedDir.toFile().list().length != actualDir.toFile().list().length){
              throw new RuntimeException(String.format("Directory sizes differ: \'%s\' and \'%s\'.", expectedDir, actualDir));
            }

            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path expectedFile, BasicFileAttributes attrs) throws IOException {
              Path relativeExpectedFile = absoluteExpected.relativize(expectedFile.toAbsolutePath());
              Path actualFile = absoluteActual.resolve(relativeExpectedFile);

              if (!Files.exists(actualFile)) {
                  throw new RuntimeException(String.format("File \'%s\' missing in actual.", expectedFile.getFileName()));
              }
              if(actualFile.toString().endsWith(".rvm")){
                  nfiles += 1;
                  if(actualFile.toString().endsWith("_imports.rvm")){  // Skip since base directories will always differ
                      return FileVisitResult.CONTINUE;
                  }

                  IValue expectedValue = read(expectedFile);
                  IValue actualValue = read(actualFile);
                  if(!expectedValue.isEqual(actualValue)){
                      Reflective refl = new Reflective(vf);
                      throw new RuntimeException(String.format("File content differs: \'%s\' and \'%s\':\n%s", expectedFile, actualFile, refl.diff(expectedValue, actualValue).getValue()));
                  }
              }

              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            throw new RuntimeException(exc.getMessage());
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
          }
        }

        try {
          RVMFileCompareAndCount fileVisitor = new RVMFileCompareAndCount(expected, actual); 

          Files.walkFileTree(expected, fileVisitor);
          return fileVisitor.getCount();

        } catch (IOException e) {
          throw new RuntimeException(e.getMessage());
        }
    }
}
