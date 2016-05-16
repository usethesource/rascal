package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;

public class CourseCompiler {
	
	
	static String courseName;
	static Path courseSrcDir;
	static Path courseDestDir;
	static String theme = "flask";
	//static String theme = "volnitsky";
	
	
	static void writeFile(String path, String content) throws IOException {
		FileWriter fout = new FileWriter(path);
		fout.write(content);
		fout.close();
	}
	
	static void asciiDoctorConvert(String name, PrintWriter err) throws IOException{
		Process p = Runtime.getRuntime().exec(
			"/usr/local/bin/asciidoctor "
			+ "-n "										// numbered sections
			+ "-v "										// verbose
			+ "-a toc-title=" + name + " "			// table of contents
			+ "-a toc=left "							
		    //+ "-a toclevels=2 "
			+ "-D / "									// destination directory
		    + "-B " + courseSrcDir + " " 				// base directory
			+ courseSrcDir + "/" + courseName + ".adoc" // the adoc source file
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
			System.err.println("asciidoctor exits with error code " + exitVal);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void compileCourse(String name) throws IOException, NoSuchRascalFunction {
		courseName = name;
		courseSrcDir = Paths.get("/Users/paulklint/git/rascal/src/org/rascalmpl/courses/" + courseName + "/");
		courseDestDir = Paths.get("/Users/paulklint/git/rascal/src/org/rascalmpl/courses/");

		StringWriter sw = new StringWriter();
		PrintWriter err = new PrintWriter(sw);
		
		err.println("# Errors in Course " + courseName);
		
		long start = Timing.getCpuTime();
		
		Onthology onthology = new Onthology(courseSrcDir, courseDestDir, err);
		
		long ontComplete = Timing.getCpuTime();
		
		
		try {
			asciiDoctorConvert(name, err);
			long asciiComplete = Timing.getCpuTime();
			System.out.println("Onthology + preprocessing: " + (ontComplete - start)/1000000000 + "sec");
			System.out.println("asciidoctor: " + (asciiComplete - ontComplete)/1000000000 + "sec");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		try {
			err.flush();
			System.err.println("\n------err:\n" + sw.toString());
			writeFile(courseDestDir + "/" + courseName + "/" + "errors.adoc", sw.toString());
//			String toc = onthology.getSubToc(courseName, 0, true);
//			System.err.println(toc);
//			writeFile(courseDestDir + "/" + courseName + "/" + "toc.adoc", toc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException, NoSuchRascalFunction {
//		compileCourse("ADocTest");
//		compileCourse("CompareWithOtherParadigms");
//		compileCourse("EASY");
//		compileCourse("Errors");
//		compileCourse("Rascal");
//		compileCourse("Libraries");
//		compileCourse("Rascalopedia");
//		compileCourse("Recipes");
		compileCourse("SolutionStrategies");
//		compileCourse("TutorWebSite");
		
	}
}
