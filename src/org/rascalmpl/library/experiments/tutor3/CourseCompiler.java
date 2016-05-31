package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;

/**
 * CourseCompiler compiles all courses to HTML in the following steps:
 * - 
 *
 */
public class CourseCompiler {
	private static final String ASCIIDOCTER = "/usr/local/bin/asciidoctor";
	
	static void writeFile(String path, String content) throws IOException {
		FileWriter fout = new FileWriter(path);
		fout.write(content);
		fout.close();
	}
	
	static void runAsciiDocter(String coursesDir, String courseName, PrintWriter err) throws IOException {
		Path courseSrcDir = Paths.get(coursesDir + courseName + "/");
		Process p = Runtime.getRuntime().exec(
			ASCIIDOCTER + " "
			+ "-n "										// numbered sections
			+ "-v "										// verbose
			+ "-a toc-title=" + courseName + " "		// table of contents
			+ "-a toc=left "							
		    //+ "-a toclevels=2 "
			+ "-D / "									// destination directory
		    + "-B " + courseSrcDir + " " 					// base directory
			+ courseSrcDir + "/" + courseName + ".adoc"	// the adoc source file
			);
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
	
	public static void compileCourse(String coursesDir, String courseName) throws IOException, NoSuchRascalFunction, URISyntaxException {
		Path srcDir = Paths.get(coursesDir + courseName + "/");
		Path destDir = Paths.get(coursesDir);

		StringWriter sw = new StringWriter();
		PrintWriter err = new PrintWriter(sw);
		
		err.println("# Errors in Course " + courseName);
		
		new Onthology(srcDir, destDir, err);
		
		try {
			runAsciiDocter(coursesDir, courseName, err);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		try {
			err.flush();
//			System.err.println("\n------err:\n" + sw.toString());
			writeFile(destDir + "/" + courseName + "/" + "errors.adoc", sw.toString());
//			String toc = onthology.getSubToc(courseName, 0, true);
//			System.err.println(toc);
//			writeFile(courseDestDir + "/" + courseName + "/" + "toc.adoc", toc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * CourseCompiler: compile all courses to HTML
	 * 
	 * @param args	a single element string array that contains the path to the courses directory
	 * @throws IOException
	 * @throws NoSuchRascalFunction
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException, NoSuchRascalFunction, URISyntaxException {

		if(args.length != 1){
			System.err.println("CourseCompiler needs a courses directory as argument");
			System.exit(1);
		}
		String coursesDir = args[0];
		if(!coursesDir.endsWith("/")){
			coursesDir = coursesDir + "/";
		}

//		compileCourse("ADocTest");
		compileCourse(coursesDir, "CompareWithOtherParadigms");
//		compileCourse(coursesDir, "EASY");
//		compileCourse(coursesDir, "Errors");
//		compileCourse(coursesDir, "Rascal");
//		compileCourse(coursesDir, "Libraries");
//		compileCourse(coursesDir, "Rascalopedia");
//		compileCourse(coursesDir, "Recipes");
//		compileCourse(coursesDir, "SolutionStrategies");
//		compileCourse(coursesDir, "TutorWebSite");
	}
}
