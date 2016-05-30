package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

//import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rascalmpl.library.experiments.tutor3.Onthology;

public class HelpManager {
	
	private final String COURSE_DIR = "/Users/paulklint/git/rascal/src/org/rascalmpl/courses/";
	private final String SEARCH_RESULT_FILE = "/Users/paulklint/search-result.html";
	
	private PrintWriter stdout;
	private PrintWriter stderr;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;
	}
	
	public void display(String link){
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("/usr/local/bin/lynx " + link);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = null;

		try {
			while ((line = input.readLine()) != null)
			{
				//stderr.println(line);
				stdout.println(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			int exitVal = p.waitFor();
			System.err.println("w3m exits with error code " + exitVal);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openInBrowser(String url)
	{
		URI uri = null;
		try {
			uri = new URL(url).toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				stderr.println("Cannout open " + url);
			}
		} else {
			stderr.println("Desktop not supported, cannout open browser automatically for " + url);
		}
	}
	
	String  makeURL(String conceptName){
		String[] parts = conceptName.split("/");
		int n = parts.length;
		String course = parts[0];
		if(n > 1){
			return "file://" + COURSE_DIR + course + "/" + course + ".html#" + parts[n-2] + "-" + parts[n-1];
		} else {
			return "file://" + COURSE_DIR + course + "/" + course + ".html#" + parts[n-1] + "-" + parts[n-1];
		}
	}
	
	private String escapeForQuery(String s){
		return s.toLowerCase().replaceAll("([+\\-!(){}\\[\\]\\^\"~*?:\\\\/]|(&&)|(\\|\\|))","\\\\$1");
	}
	
	private String escapeHtml(String s){
		return s;
	}
	
	public void printHelp(String[] words){
		//TODO Add here for example credits, copyright, license
		
		if(words.length <= 1){
			IntroHelp.print(stdout);
			return;
		}

		Path destDir = Paths.get(COURSE_DIR);
		Analyzer multiFieldAnalyzer = Onthology.multiFieldAnalyzer();
		
		try {
			Directory directory = FSDirectory.open(destDir);
			DirectoryReader ireader = DirectoryReader.open(directory); // read-only=true
			IndexSearcher isearcher = new IndexSearcher(ireader);
			
			String searchFields[] = {"index", "synopsis", "doc"};
			
			QueryParser parser  = new MultiFieldQueryParser(searchFields, multiFieldAnalyzer);
			
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < words.length; i++){
				sb.append(" ").append(escapeForQuery(words[i]));
			}
			Query query = parser.parse(sb.toString());
			    
			ScoreDoc[] hits = isearcher.search(query, 25).scoreDocs;
			
			// Iterate through the results:
			int nhits = hits.length;
			if(nhits == 0){
				stdout.println("No info found");
//			} else if (nhits == 1){
//				openInBrowser(makeURL(isearcher.doc(hits[0].doc).get("name")));
			} else {
				StringWriter w = new StringWriter();
				w.append("<title>Rascal Search Results</title>\n");
				w.append("<h1>Rascal Search Results</h1>\n");
				w.append("<ul>\n");
				for (int i = 0; i < Math.min(hits.length, 25); i++) {
					Document hitDoc = isearcher.doc(hits[i].doc);
					w.append("<li> ");
					String name = hitDoc.get("name");
					w.append("<a href=\"").append(makeURL(name)).append("\" target=\"_top\">").append(name).append("</a>:\n");
					w.append("<em>").append(escapeHtml(getInfo(hitDoc, "synopsis"))).append("</em>");
					String signature = getInfo(hitDoc, "signature");
					if(!signature.isEmpty()){
						w.append("<br>").append("<code>").append(escapeHtml(signature)).append("</code>");
					}
				}
				w.append("</ul>\n");
				FileWriter fout = new FileWriter(SEARCH_RESULT_FILE);
				fout.write(w.toString());
				fout.close();
		        openInBrowser("file://" + SEARCH_RESULT_FILE);
			}
			ireader.close();
			directory.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String getInfo(Document hitDoc, String field){
		String s = hitDoc.get(field);
		return (s == null || s.isEmpty()) ? "" : s;
	}
	
}
