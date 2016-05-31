package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

//import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Desktop;
import java.io.FileWriter;
import java.io.IOException;
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
import org.rascalmpl.uri.URIResolverRegistry;

public class HelpManager {
	
	private final String COURSE_DIR = "/Users/paulklint/git/rascal/src/org/rascalmpl/courses/";
	private final String SEARCH_RESULT_FILE = "/Users/paulklint/search-result.html";
	private final int MAX_SEARCH = 25;
	
	private PrintWriter stdout;
	private PrintWriter stderr;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		String u = System.getProperty("rascal.courses");
		this.stdout = stdout;
		this.stderr = stderr;
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
				stderr.println("Cannout open in browser: " + url);
			}
		} else {
			stderr.println("Desktop not supported, cannout open browser automatically for: " + url);
		}
	}
	
	void appendURL(StringWriter w, String conceptName){
		String[] parts = conceptName.split("/");
		int n = parts.length;
		String course = parts[0];
		w.append(COURSE_DIR).append(course).append("/").append(course).append(".html")
		 .append("#").append(parts[n - (n > 1 ? 2 : 1)]).append("-").append(parts[n-1]);
	}
	
	String makeURL(String conceptName){
		StringWriter w = new StringWriter();
		appendURL(w, conceptName);
		return w.toString();
	}
	
	void appendHyperlink(StringWriter w, String conceptName){
		w.append("<a href=\"file://");
		appendURL(w, conceptName);
		w.append("\">").append(conceptName).append("</a>");
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
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			
			String searchFields[] = {"index", "synopsis", "doc"};
			
			QueryParser parser  = new MultiFieldQueryParser(searchFields, multiFieldAnalyzer);
			
			StringBuilder sb = new StringBuilder();
			for(int i = 1; i < words.length; i++){
				sb.append(" ").append(escapeForQuery(words[i]));
			}
			Query query;
			try {
				query = parser.parse(sb.toString());
			} catch (ParseException e) {
				stderr.println("Cannot parse query: " + sb + ", " + e.getMessage());
				return;
			}

			if(words[0].equals("help")){
				reportHelp(isearcher, isearcher.search(query, MAX_SEARCH).scoreDocs);
			} else {
				reportApropos(isearcher, isearcher.search(query, MAX_SEARCH).scoreDocs);
			}
			ireader.close();
			directory.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
		
	String getField(Document hitDoc, String field){
		String s = hitDoc.get(field);
		return (s == null || s.isEmpty()) ? "" : s;
	}
	
	void reportHelp(IndexSearcher isearcher, ScoreDoc[] hits) throws IOException{
		// Iterate through the results:
		int nhits = hits.length;

		if(nhits == 0){
			stdout.println("No info found");
		} else if (nhits == 1){
			openInBrowser(makeURL(isearcher.doc(hits[0].doc).get("name")));
		} else {
			StringWriter w = new StringWriter();
			w.append("<title>Rascal Search Results</title>\n");
			w.append("<h1>Rascal Search Results</h1>\n");
			w.append("<ul>\n");
			for (int i = 0; i < Math.min(hits.length, MAX_SEARCH); i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				w.append("<li> ");
				String name = hitDoc.get("name");
				appendHyperlink(w, name);
				w.append(": <em>").append(escapeHtml(getField(hitDoc, "synopsis"))).append("</em>");
				String signature = getField(hitDoc, "signature");
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
	}
	
	void reportApropos(IndexSearcher isearcher, ScoreDoc[] hits) throws IOException{
		for (int i = 0; i < Math.min(hits.length, MAX_SEARCH); i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			String name = hitDoc.get("name");
			String signature = getField(hitDoc, "signature");
			String synopsis = getField(hitDoc, "synopsis");
			if(signature.isEmpty()){
				stdout.println(name + ":\n\t" + synopsis);
			} else {
				stdout.println(name + ":\n\t" + synopsis + "\n\t" + signature);
			}
		}
	}
}
