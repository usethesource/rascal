package org.rascalmpl.library.experiments.tutor3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import org.rascalmpl.library.experiments.tutor3.commonmark.ext.ConceptValidator;
import org.rascalmpl.library.experiments.tutor3.commonmark.ext.LinkExpander;
import org.rascalmpl.library.experiments.tutor3.commonmark.ext.RascalShellRunner;

public class CourseCompiler {
	
	static String courseName = "Test";
	static Path courseSrcDir = Paths.get("/Users/paulklint/git/rascal/src/org/rascalmpl/courses/Test/");
	static Path courseDestDir = Paths.get("/Users/paulklint/git/rascal/src/org/rascalmpl/courses/");
	
	
	private static void writeFile(String path, String content) throws IOException {
		FileWriter fout = new FileWriter(path);
		fout.write(content);
		fout.close();
	}
	
	public static void main(String[] args) {

		Onthology onthology = new Onthology(courseSrcDir, courseDestDir);
	
		List<Extension> extensions = Arrays.asList(TablesExtension.create());
		
		StringWriter sw = new StringWriter();
		PrintWriter err = new PrintWriter(sw);
		LinkExpander linkExpander = new LinkExpander(onthology, err);
		ConceptValidator validator = new ConceptValidator(err);
		Parser parser = Parser.builder().extensions(extensions).postProcessor(validator).postProcessor(linkExpander).build();
		RascalShellRunner rascalShellRunner = new RascalShellRunner(err);
		
		HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).escapeHtml(true).build();

		Map<String, Concept> conceptMap = onthology.getConceptMap();
		for(String conceptName : conceptMap.keySet()){
			System.err.println(conceptName);
			Concept concept = conceptMap.get(conceptName);
		
			rascalShellRunner.setConceptName(conceptName);
			linkExpander.setConceptName(conceptName);
			validator.setConceptName(conceptName);
			
			Node document = parser.parse(concept.getText());
			document.accept(rascalShellRunner);
			
//			CommonMarkRenderer cmrenderer = CommonMarkRenderer.builder().build();
//			System.err.println(cmrenderer.render(document));
			
			try {
				System.err.println("Writing to: " + concept.getHtmlFileName());
				writeFile(concept.getHtmlFileName(), renderer.render(document));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			System.err.println("\n------err:\n" + sw.toString());
			Node errors = parser.parse(sw.toString());
			writeFile(courseDestDir + "/" + courseName + "/" + "warnings1.html", renderer.render(errors));
			Node contents = parser.parse(onthology.toc());
			System.err.println("\n------toc:\n");
			writeFile(courseDestDir + "/" + courseName + "/" + "toc.html", renderer.render(contents));
			onthology.toc();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
