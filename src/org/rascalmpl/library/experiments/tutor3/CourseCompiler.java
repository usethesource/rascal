package org.rascalmpl.library.experiments.tutor3;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.rascalmpl.library.experiments.Compiler.Commands.CommandOptions;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * CourseCompiler compiles all courses to HTML in the following steps:
 * - each concept is translated to an AsciiDoc file. Note that the property rascal.asciidoctor
 *   can be used to override the default location for asciidoctor.
 * - all generated AsciiDoc files are transformed to a single HTML file
 * - the contributions to the Lucene index are computed and stored per course
 * - TODO Dependency on RascalExtraction
 */
public class CourseCompiler {
	private static final String ASCIIDOCTOR_DEFAULT = "/usr/local/bin/asciidoctor";
	
	static void writeFile(String path, String content) throws IOException {
		FileWriter fout = new FileWriter(path);
		fout.write(content);
		fout.close();
	}
	
	static void runAsciiDocter(Path srcPath, String courseName, Path destPath, PrintWriter err) throws IOException {
		Path courseDestDir = destPath.resolve(courseName);
		String asciidoctor = System.getProperty("rascal.asciidoctor");
		if(asciidoctor == null){
			asciidoctor = ASCIIDOCTOR_DEFAULT;
		}
		String cmd = 
			asciidoctor
			+ " -n"												// numbered sections
			+ " -v"												// verbose
			+ " -a toc-title=" + courseName						// table of contents
			+ " -a toc=left"									// at left side
		    //+ " -a toclevels=2"
			+ " -a linkcss"										// link the style sheet
			+ " -a stylesheet=" + "css/style.css"				// use our own style sheet
			+ " -d book"										// book style
			+ " -D " + courseDestDir							// destination directory
		    + " -B " + courseDestDir 							// base directory
			+ " " + courseDestDir.resolve(courseName + ".adoc")	// the adoc source file
			+ " -o " + courseDestDir + "/" + "index.html"		// the html output file
			;
		System.err.println(cmd);
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String line = null;

		while ((line = input.readLine()) != null)
		{
			System.err.println(line);
			err.println(line);
		}

		try {
			int exitVal = p.waitFor();
			if(exitVal != 0){
				System.err.println("asciidoctor exits with error code " + exitVal);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void compileCourse(Path srcPath, String courseName, Path destPath, Path libPath, RascalCommandExecutor executor) throws IOException, NoSuchRascalFunction, URISyntaxException {
		
		copyStandardFilesPerCourse(srcPath, courseName, destPath);
		new Onthology(srcPath, courseName, destPath, libPath, executor);
		
		try {
			runAsciiDocter(srcPath, courseName, destPath, executor.err);
		} catch (IOException e) {
			System.err.println("Cannot run asciidocter: " + e.getMessage());
		}
	}
	
	private static void copyStandardFilesPerCourse(Path srcPath, String courseName, Path destPath) throws IOException {
		ArrayList<String> files  = new ArrayList<>();
		
		files.add("docinfo.html");
		Path coursePath = destPath.resolve(courseName);
		if(!Files.exists(coursePath)){
			Files.createDirectories(coursePath);
		}
		for(String file : files){
			Path src = srcPath.resolve(file);
			Path dest = coursePath.resolve(file);
			Path parent = dest.getParent();
			if(!Files.exists(parent)){
				Files.createDirectories(parent);
			}
			Files.copy(src, dest, REPLACE_EXISTING);
		}
	}
	
	private static void copyStandardFiles(Path srcPath, Path destPath) throws IOException {
		
		System.err.println("Copying standard files");
		
		ArrayList<String> files  = new ArrayList<>();
		files.add("favicon.ico");
		files.add("css/style.css");
		files.add("docinfo.html");
		files.add("css/font-awesome.min.css");
		files.add("fonts/fontawesome-webfont.eot");
		files.add("fonts/fontawesome-webfont.svg");
		files.add("fonts/fontawesome-webfont.ttf");
		files.add("fonts/fontawesome-webfont.woff");
		files.add("fonts/fontawesome-webfont.woff2");
		files.add("images/rascal-tutor-small.png");
		for(int i = 1; i <= 15; i++){
			files.add("images/" + i + ".png");
		}
		
		for(String file : files){
			Path src = srcPath.resolve(file);
			Path dest = destPath.resolve(file);
			Path parent = dest.getParent();
			if(!Files.exists(parent)){
				Files.createDirectories(parent);
			}
			Files.copy(src, dest, REPLACE_EXISTING);
		}
	}
	
	private static class RemoveAdocs extends SimpleFileVisitor<Path> {

		@Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
			String fileName = file.getFileName().toString();

			if(fileName.endsWith(".adoc")){
				Files.delete(file);
			}
			return FileVisitResult.CONTINUE;
		}
	}
	
	/**
	 * CourseCompiler: compile and deploy all courses.
	 * 
	 * @param args array with command options and courses to be compiled
	 * @throws IOException
	 * @throws NoSuchRascalFunction
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, NoSuchRascalFunction, URISyntaxException {

		 IValueFactory vf = ValueFactoryFactory.getValueFactory();
		 CommandOptions cmdOpts = new CommandOptions("course-compiler");
         
         cmdOpts
         .locsOption("course")		
         .locsDefault(cmdOpts.getDefaultCourses().isEmpty() ? vf.list(cmdOpts.getDefaultCourses()) : cmdOpts.getDefaultCourses())
         .respectNoDefaults()
         .help("Add (absolute!) course location, use multiple --course arguments for multiple locations")
         
         .locsOption("src")		
         .locsDefault(cmdOpts.getDefaultStdlocs().isEmpty() ? vf.list(cmdOpts.getDefaultStdlocs()) : cmdOpts.getDefaultStdlocs())
         .respectNoDefaults()
         .help("Add (absolute!) source location, use multiple --src arguments for multiple locations")

         .locsOption("lib")		
         .locsDefault((co) -> vf.list(co.getCommandLocOption("bin")))
         .respectNoDefaults()
         .help("Add new lib location, use multiple --lib arguments for multiple locations")

         .locOption("bin") 		
         .respectNoDefaults()
         .help("Directory for Rascal binaries")
         
         .boolOption("all")
         .help("Compile available courses")

         .boolOption("help") 		
         .help("Print help message for this command")

         .boolOption("verbose")
         .help("Make the course compiler verbose")

         .rascalModules("Course modules to be compiled")

         .handleArgs(args);
		
		PathConfig pcfg = 
				new PathConfig(cmdOpts.getCommandlocsOption("src"),
							   cmdOpts.getCommandlocsOption("lib"),
					           cmdOpts.getCommandLocOption("bin"),
					           cmdOpts.getCommandlocsOption("course"));   
		
		Path coursesSrcPath = Paths.get(((ISourceLocation)pcfg.getcourses().get(0)).getPath());
		Path libPath = Paths.get(((ISourceLocation)pcfg.getLibs().get(0)).getPath());
		
		Path destPath = Paths.get(((ISourceLocation)pcfg.getBin()).getPath()).resolve("courses");
		copyStandardFiles(coursesSrcPath, destPath);
		
		StringWriter sw = new StringWriter();
		PrintWriter err = new PrintWriter(sw);
		RascalCommandExecutor executor = new RascalCommandExecutor(pcfg, err);
		
		if(cmdOpts.getCommandBoolOption("all")){
			IList givenCourses = cmdOpts.getRascalModules();
			if(!givenCourses.isEmpty()){
				System.err.println("--all conflicts with " + givenCourses);
			}
			for(String courseName : pcfg.listCourseEntries()){
				compileCourse(coursesSrcPath, courseName, destPath, libPath, executor);
			}
		} else {
			for(IValue iCourseName : cmdOpts.getRascalModules()){
				compileCourse(coursesSrcPath, ((IString)iCourseName).getValue(), destPath, libPath, executor);
			}
		}

//		compileCourse(coursesSrcPath, "ADocTest", destPath, libPath, executor);
//		
//		compileCourse(coursesSrcPath, "WhyRascal", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "GettingStarted", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "GettingHelp", destPath, libPath, executor);
//		
//		compileCourse(coursesSrcPath, "Errors", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "Rascal", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "RascalConcepts", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "Libraries", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "Rascalopedia", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "Recipes", destPath, libPath, executor);
//		compileCourse(coursesSrcPath, "TutorWebSite", destPath, libPath, executor);
		
		
		err.flush();
		writeFile(destPath + "/course-compilation-errors.txt", sw.toString());
		
//		System.err.println("Removing intermediate files");
//		
//		FileVisitor<Path> fileProcessor = new RemoveAdocs();
//		try {
//			Files.walkFileTree(destPath, fileProcessor);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		System.err.println("Course compilation done");
	}
}
