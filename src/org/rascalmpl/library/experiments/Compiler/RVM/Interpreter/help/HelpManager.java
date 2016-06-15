package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

//import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rascalmpl.library.experiments.tutor3.Concept;
import org.rascalmpl.library.experiments.tutor3.Onthology;

public class HelpManager {
	
	private String coursesDir;
	private String searchResultFile;
	private final int maxSearch = 25;
	
	private PrintWriter stdout;
	private PrintWriter stderr;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;

		coursesDir = System.getProperty("rascal.courses");
		if(coursesDir == null){
			stderr.println("Property rascal.courses should point to deployed courses");
		} else {
			searchResultFile = coursesDir + "/search-result.html";

			try {
				new HelpServer(this, Paths.get(coursesDir));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getSearchResultFile(){
		return searchResultFile;
	}
	
	private boolean indexAvailable(){
		if(coursesDir != null){
			return true;
		}
		stderr.println("Please specify -Drascal.courses=<courses-directory> to use 'help' or 'apropos'");
		return false;
	}
	
	public static void openInBrowser(String url)
	{
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else {
			System.err.println("Desktop not supported, cannout open browser automatically for: " + url);
		}
	}
	
	void appendURL(StringWriter w, String conceptName){
		String[] parts = conceptName.split("/");
		int n = parts.length;
		String course = parts[0];
		w.append("/").append(course).append("#").append(parts[n - (n > 1 ? 2 : 1)]).append("-").append(parts[n-1]);
	}
	
	String makeURL(String conceptName){
		StringWriter w = new StringWriter();
		appendURL(w, conceptName);
		return w.toString();
	}
	
	void appendHyperlink(StringWriter w, String conceptName){
		w.append("<a href=\"http://localhost:8000");
		appendURL(w, conceptName);
		w.append("\">").append(conceptName).append("</a>");
	}
	
	private String escapeForQuery(String s){
		return s.toLowerCase().replaceAll("([+\\-!(){}\\[\\]\\^\"~*?:\\\\/]|(&&)|(\\|\\|))","\\\\$1");
	}
	
	private String escapeHtml(String s){
		return s;
	}
	
	public String giveHelp(String[] words){
		//TODO Add here for example credits, copyright, license
		
		if(words.length <= 1){
			IntroHelp.print(stdout);
			return "";
		}
		
		if(!indexAvailable()){
			return "";
		}

		Path destDir = Paths.get(coursesDir);
		Analyzer multiFieldAnalyzer = Onthology.multiFieldAnalyzer();
		
		try {
			ArrayList<IndexReader> readers = new ArrayList<>();
			for(Path p : Files.newDirectoryStream(destDir)){
				if(Files.isDirectory(p) && p.getFileName().toString().matches("^[A-Z].*")){
					Directory directory = FSDirectory.open(p);
					DirectoryReader ireader = DirectoryReader.open(directory);
					readers.add(ireader);
				}
			}
			
			IndexReader[] ireaders = new IndexReader[readers.size()];
			for(int i = 0; i < readers.size(); i++){
				ireaders[i] = readers.get(i);
			}
			IndexReader ireader = new MultiReader(ireaders);
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
				return "";
			}

			if(words[0].equals("help")){
				return reportHelp(words, isearcher, isearcher.search(query, maxSearch).scoreDocs);
			} else {
				return reportApropos(words, isearcher, isearcher.search(query, maxSearch).scoreDocs);
			}
			//ireader.close();
			//directory.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
		
	String getField(Document hitDoc, String field){
		String s = hitDoc.get(field);
		return (s == null || s.isEmpty()) ? "" : s;
	}
	
	void genPrelude(StringWriter w){
		w.append("<head>\n");
		w.append("<title>Rascal Help</title>");
		w.append("<link rel=\"stylesheet\" href=\"style.css\"/>");
		w.append("<link rel=\"icon\" href=\"/favicon.ico\" type=\"image/x-icon\"/>");
		w.append("</head>\n");
		w.append("<body class=\"book toc2 toc-left\">");
		
		w.append(Concept.getSearchForm());
		
		w.append("<div id=\"toc\" class=\"toc2\">");
		w.append("</div>");
	}
	
	void genSearchTerms(String[] words, StringWriter w){
		w.append("<i>");
		for(int i = 1; i < words.length; i++){
			w.append(words[i]).append(" ");
		}
		w.append("</i>\n");
	}
	
	String reportHelp(String[] words, IndexSearcher isearcher, ScoreDoc[] hits) throws IOException{
		int nhits = hits.length;
		
		StringWriter w = new StringWriter();
		if(nhits == 0){
			stdout.println("No info found");
			genPrelude(w);
			w.append("<h1 class=\"search-sect0\">No help found for: ");
			genSearchTerms(words, w);
			w.append("</h1>\n");
			w.append("</body>\n");
			return w.toString();
//		} else if (nhits == 1){
//			openInBrowser(makeURL(isearcher.doc(hits[0].doc).get("name")));
		} else {
			genPrelude(w);

			w.append("<h1 class=\"search-sect0\">Help for: ");
			genSearchTerms(words, w);
			w.append("</h1>\n");
			w.append("<ul>\n");
			for (int i = 0; i < Math.min(hits.length, maxSearch); i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				w.append("<div class=\"search-ulist\">\n");
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
			w.append("</div>");
			w.append("</body>\n");
			return w.toString();
		}
	}
	
	String reportApropos(String[] words, IndexSearcher isearcher, ScoreDoc[] hits) throws IOException{
		StringWriter w = new StringWriter();
		for (int i = 0; i < Math.min(hits.length, maxSearch); i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			String name = hitDoc.get("name");
			String signature = getField(hitDoc, "signature");
			String synopsis = getField(hitDoc, "synopsis");
			w.append(name).append(":\n\t").append(synopsis);
			if(!signature.isEmpty()){
				w.append("\n\t").append(signature);
			}
			w.append("\n");
		}
		return w.toString();
	}
}
