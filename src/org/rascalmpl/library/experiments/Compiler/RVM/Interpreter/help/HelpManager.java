package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

//import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
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
	
	private URI coursesDir;
	private final int maxSearch = 25;
	private final PrintWriter stdout;
	private final PrintWriter stderr;
	private IndexSearcher indexSearcher;
	private final int port = 8000;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;
 
		URL c = this.getClass().getResource("/courses");
		if(c == null){
			stderr.println("Cannot find deployed (precompiled) courses");
		} else {
			try {
				coursesDir = c.toURI();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				new HelpServer(port, this, Paths.get(coursesDir));
				indexSearcher = makeIndexSearcher();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<IndexReader> getReaders() throws IOException{
		if(coursesDir.getScheme().equals("file")){
			Path destDir = Paths.get(coursesDir);
			ArrayList<IndexReader> readers = new ArrayList<>();
			for(Path p : Files.newDirectoryStream(destDir)){
				if(Files.isDirectory(p) && p.getFileName().toString().matches("^[A-Z].*")){
					Directory directory = FSDirectory.open(p);
					DirectoryReader ireader = DirectoryReader.open(directory);
					readers.add(ireader);
				}
			}
			return readers;
		}
		throw new IOException("Cannot yet handle non-file coursesDir");
	}
	
	IndexSearcher makeIndexSearcher() throws IOException{
		ArrayList<IndexReader> readers = getReaders();

		IndexReader[] ireaders = new IndexReader[readers.size()];
		for(int i = 0; i < readers.size(); i++){
			ireaders[i] = readers.get(i);
		}
		IndexReader ireader = new MultiReader(ireaders);
		return  new IndexSearcher(ireader);
	}
	
	private boolean indexAvailable(){
		if(indexSearcher != null){
			return true;
		}
		stderr.println("No deployed courses found; they are needed for 'help' or 'apropos'");
		return false;
	}
	
	public void openInBrowser(URI uri)
	{
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else {
			System.err.println("Desktop not supported, cannout open browser");
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
		w.append("<a href=\"http://localhost:");
		w.append(String.valueOf(port));
		appendURL(w, conceptName);
		w.append("\">").append(conceptName).append("</a>");
	}
	
	private String escapeForQuery(String s){
		return s.toLowerCase().replaceAll("([+\\-!(){}\\[\\]\\^\"~*?:\\\\/]|(&&)|(\\|\\|))","\\\\$1");
	}
	
	private String escapeHtml(String s){
		// TODO: switch to StringEscapeUtils when compiled inside Eclipse
		return s;
	}
	
	private URI makeSearchURI(String[] words) throws URISyntaxException, UnsupportedEncodingException{
		StringWriter w = new StringWriter();
		for(int i = 1; i < words.length; i++){
			w.append(words[i]);
			if(i < words.length - 1) w.append(" ");
		}
		String encoded = URLEncoder.encode(w.toString(), "UTF-8");
		return new URI("http", "localhost:" + port + "/Search?searchFor=" + encoded, null);
	}
	
	public void handleHelp(String[] words){
		if(words[0].equals("help")){
			try {
				openInBrowser(makeSearchURI(words));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			stdout.println(giveHelp(words));
		}
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

		Analyzer multiFieldAnalyzer = Onthology.multiFieldAnalyzer();
		
		try {
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
				return reportHelp(words, indexSearcher.search(query, maxSearch).scoreDocs);
			} else {
				return reportApropos(words, indexSearcher.search(query, maxSearch).scoreDocs);
			}
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
		w.append("<link rel=\"stylesheet\" href=\"css/style.css\"/>");
		w.append("<link rel=\"stylesheet\" href=\"css/font-awesome.min.css\"/>");
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
	
	String reportHelp(String[] words, ScoreDoc[] hits) throws IOException{
		int nhits = hits.length;
		
		StringWriter w = new StringWriter();
		if(nhits == 0){
			stdout.println("No info found");
			genPrelude(w);
			w.append("<h1 class=\"search-sect0\">No help found for: ");
			genSearchTerms(words, w);
			w.append("</h1>\n");
			w.append("<div class=\"search-ulist\">\n");
			w.append("<ul><li>Perhaps try <i>help</i>, <i>further reading</i> or <i>introduction</i> as search terms</li>");
			w.append("</ul>\n");
			w.append("</div>");
			w.append("</body>\n");
			return w.toString();
		} else {
			genPrelude(w);
			w.append("<h1 class=\"search-sect0\">Help for: ");
			genSearchTerms(words, w);
			w.append("</h1>\n");
			w.append("<ul>\n");
			for (int i = 0; i < Math.min(hits.length, maxSearch); i++) {
				Document hitDoc = indexSearcher.doc(hits[i].doc);
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
	
	String reportApropos(String[] words, ScoreDoc[] hits) throws IOException{
		StringWriter w = new StringWriter();
		for (int i = 0; i < Math.min(hits.length, maxSearch); i++) {
			Document hitDoc = indexSearcher.doc(hits[i].doc);
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
