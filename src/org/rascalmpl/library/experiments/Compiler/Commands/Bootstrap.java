package org.rascalmpl.library.experiments.Compiler.Commands;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.rascalmpl.uri.URIUtil;

/**
 * This is experimental code.
 */
public class Bootstrap {
    private static final String BOOT_KERNEL_PATH = "lang/rascal/boot/Kernel.rvm.ser.gz";

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
    
    public static class NoFixedpoint extends BootstrapMessage {
		public NoFixedpoint(int phase) {
			super(phase);
		}

		private static final long serialVersionUID = -8257679193918528866L;

		@Override
    	public String getMessage() {
    		return "No fixed point reached after phase " + phase;
    	}
    }
    
    public static class Fixedpoint extends BootstrapMessage {
		private static final long serialVersionUID = -1L;

        public Fixedpoint(int phase) {
        	super(phase);
        }

        @Override
        public String getMessage() {
            return "Bootstrapped reached fixed point after phase " + phase;
        }
    }
    

    public static int main(String[] args) {
        if (args.length != 5) {
        	System.err.println("Usage: Bootstrap <classpath> <versionToBootstrapOff> <versionToBootstrapTo> <sourceFolder> <targetFolder>");
        	return -1;
        }
        
        int arg = 1;
        String classpath = args[arg++];
        String versionToUse = args[arg++];
        String versionToBuild = args[arg++];
        
        Path sourceFolder = new File(args[arg++]).toPath();
        if (!Files.exists(sourceFolder.resolve("org/rascalmpl/library/Prelude.rsc"))) {
        	throw new RuntimeException("source folder " + sourceFolder + " should point to source folder of standard library containing Prelude and the compiler");
        }
        
        Path targetFolder = new File(args[arg++]).toPath();
        if (!Files.exists(targetFolder.resolve("org/rascalmpl/library/Prelude.class"))) {	// PK: PreludeCompiled
        	throw new RuntimeException("target folder " + sourceFolder + " should point to source folder of compiler library and the RVM interpreter.");
        }
        
        Path tmpDir = new File(System.getProperty("java.io.tmpdir") + "/rascal-boot").toPath();
        System.err.println("INFO: bootstrap folder: " + tmpDir.toAbsolutePath());

        // bootstrappig does not do anything if the target version is equal to the currently released version.
        if (existsDeployedRuntime(tmpDir, versionToBuild)) {
            System.out.println("INFO: Got the kernel version: " + versionToBuild + " already from existing build.");
            return 0;
        }
        
        // Otherwise we have to bootstrap in three + one stages, generating a new Kernel file using an existing runtime:
        //    0. download a released runtime
        //    1. build new kernel with old jar using kernel inside the jar (creating K')
        //    2. build new kernel with old jar loading kernel K' (creating K'')
        //    3. build new kernel with new classes using kernel K'' (creating K'''')

        try {
            Path phase0Runtime = getDeployedRuntime(tmpDir, versionToUse);
            Path phase0Kernel = getDeployedKernel(tmpDir, versionToUse);
            Path phase1Kernel = compilePhase(1, phase0Runtime, tmpDir, phase0Kernel, sourceFolder);
            Path phase2Kernel = compilePhase(2, phase0Runtime, tmpDir, phase1Kernel, sourceFolder);
            Path phase3Kernel = compilePhase(3, classpath, phase2Kernel, tmpDir, phase2Kernel, sourceFolder, false);
            Path phase4Kernel = compilePhase(4, classpath, phase3Kernel, tmpDir, phase2Kernel, sourceFolder, true);

            Files.copy(phase4Kernel, targetFolder.resolve(BOOT_KERNEL_PATH));
        } 
        catch (Fixedpoint e) {
        	System.err.println(e.getMessage());
        }
        catch (BootstrapMessage | IOException | InterruptedException e) {
			e.printStackTrace();
			return 1;
		} 
       
        
        return 0;
    }
    
    private static boolean equalKernels(Path one, Path two) throws IOException {
    	return md5(one).equals(md5(two));
    }
    
    private static String md5(Path one) throws IOException {
    	MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
		}
		
    	try (InputStream streamOne = Files.newInputStream(one);
    			DigestInputStream digestOne = new DigestInputStream(streamOne, md);
    			DataInputStream d = new DataInputStream(digestOne))
    	{
    		byte[] bytes = new byte[md.getDigestLength()];
    		d.readFully(bytes);
    		return new String(md.digest(), "UTF8");
    	}
    }
    
    // PK: I don't get "Runtime" here and in other names, would "Version" or "Rascal" not be better?
    // - existsDeployedRascal / existsDeployedVersion
    // - kernelInRascalLocation / kernelInVersionLocation
    // - getDeployedRascal / getDeployedVersion
    // etc.

	private static boolean existsDeployedRuntime(Path folder, String version) {	
		try (InputStream s = deployedRuntime(version).toURL().openStream()) {
			return s != null;
		} catch (IOException e) {
			return false;
		} 
	}
    
    private static Path getDeployedKernel(Path tmp, String version) throws IOException {
    	Path cachedKernel = cachedKernel(tmp, version);
    	
		if (!cachedKernel.toFile().exists()) {
    		Path runtime = getDeployedRuntime(tmp, version);
    		Path kernelInJar = kernelInRuntimeLocation(runtime);
    		Files.copy(kernelInJar, cachedKernel);
    	}
    	
    	return cachedKernel;
    }

	private static Path kernelInRuntimeLocation(Path runtime) throws IOException {
		FileSystem fs = FileSystems.newFileSystem(runtime, System.class.getClassLoader());
		return fs.getPath(BOOT_KERNEL_PATH);
	}
	
    /**
     * Either download or get the jar of the deployed version of Rascal from a previously downloaded instance.
     */
    private static Path getDeployedRuntime(Path tmp, String version) throws IOException {
		Path cached = cachedRuntime(tmp, version);
		if (!cached.toFile().exists()) {
    		URI deployedRuntime = deployedRuntime(version);
			Files.copy(deployedRuntime.toURL().openStream(), cached);
    	}
		
		return cached;
    }

    private static Path cachedRuntime(Path tmpFolder, String version) {
    	return tmpFolder.resolve("rascal-" + version + ".jar");
	}

	private static URI deployedRuntime(String version) {
		return URIUtil.assumeCorrect("http", "update.rascal-mpl.org", "/console/rascal-" + version + ".jar");
	}
	
	private static Path cachedKernel(Path tmpFolder, String version) {
		return tmpFolder.resolve("Kernel.rvm.ser.gz");
	}

	// PK: Observations:
	// - The MuLibrary has to be compiled as well. There is a function for that: compileMuLibrary
	// - Where are the files of the Rascal library compiled?
	
    private static Path compilePhase(int phase, Path workingCompiler, Path tmp, Path kernel, Path sourcePath) throws BootstrapMessage, Fixedpoint, IOException, InterruptedException {
        Path result = tmp.resolve("phase" + phase);
        result.toFile().mkdir();
        
        if (executeJarRuntime(workingCompiler, 
        		"-rascalc", 
        		"--noDefaults", 
                "--binDir", result.toAbsolutePath().toString(),
                "--srcPath", sourcePath.toAbsolutePath().toString(), 
                "lang::rascal::boot::Kernel", "lang::rascal::grammar::ParserGenerator") != 0) {
        	
        	throw new BootstrapMessage(phase);
        }
      
        Path newKernel = result.resolve(BOOT_KERNEL_PATH);
        
        if (equalKernels(kernel, newKernel)) {
        	throw new Fixedpoint(phase);
        }
      
		return newKernel;
    }
    
    private static Path compilePhase(int phase, String classPath, Path workingCompiler, Path tmp, Path kernel, Path sourcePath, boolean expectFixedpoint) throws BootstrapMessage, IOException, InterruptedException, Fixedpoint {
        Path result = tmp.resolve("phase" + phase);
        result.toFile().mkdir();
        
        if (executeFolderRuntime(classPath, workingCompiler, 
        		"-rascalc", 
        		"--noDefaults", 
                "--binDir", result.toAbsolutePath().toString(),
                "--srcPath", sourcePath.toAbsolutePath().toString(), 
                "lang::rascal::boot::Kernel", "lang::rascal::grammar::ParserGenerator") != 0) {
        	
        	throw new BootstrapMessage(phase);
        }
        
        Path newKernel = result.resolve(BOOT_KERNEL_PATH);
        
        if (expectFixedpoint) {
        	if (equalKernels(kernel, newKernel)) {
        		return newKernel;
        	}
        	else {
            	throw new NoFixedpoint(phase);        		
        	}
        }
        else if (equalKernels(kernel, newKernel)) {
        	throw new Fixedpoint(phase);
        }
        
        return newKernel;
    }

    private static int executeJarRuntime(Path workingCompiler, String... arguments) throws IOException, InterruptedException {
    	String[] command = new String[arguments.length + 2];
    	command[0] = "java";
    	command[1] = "-jar";
    	command[2] = workingCompiler.toAbsolutePath().toString();
    	System.arraycopy(arguments, 0, command, 3, arguments.length);;
    	Process runtime = new ProcessBuilder(command).start();
    	runtime.waitFor();
    	return runtime.exitValue();
    }
    
    private static int executeFolderRuntime(String classPath, Path workingCompiler, String... arguments) throws IOException, InterruptedException {
    	String[] command = new String[arguments.length + 2];
    	command[0] = "java";
    	command[1] = "-cp";
    	command[2] = classPath;
    	command[3] = "org.rascalmpl.shell.RascalShell";
    	command[2] = workingCompiler.toAbsolutePath().toString();
    	System.arraycopy(arguments, 0, command, 3, arguments.length);;
    	Process runtime = new ProcessBuilder(command).start();
    	runtime.waitFor();
    	return runtime.exitValue();
    }
}
