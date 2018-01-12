package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.tutor3.Concept;
import org.rascalmpl.library.experiments.tutor3.Onthology;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

public class HelpManager {
	
	private final ISourceLocation coursesDir;
	private final PathConfig pcfg;
	private final int maxSearch = 25;
	private final PrintWriter stdout;
	private final PrintWriter stderr;
	private IndexSearcher indexSearcher;
	
	private final int BASE_PORT = 8750;
	private final int ATTEMPTS = 100;
	private final int port;
	
    private final HelpServer helpServer;
    private final IDEServices ideServices;

    public HelpManager(ISourceLocation compiledCourses, PathConfig pcfg, PrintWriter stdout, PrintWriter stderr, IDEServices ideServices) throws IOException {
        this.pcfg = pcfg;
        this.stdout = stdout;
        this.stderr = stderr;
        this.ideServices = ideServices;

        coursesDir = compiledCourses;

        helpServer = startServer(stderr);
        port = helpServer.getPort();
    }

    public HelpManager(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr, IDEServices ideServices) throws IOException {
      this.pcfg = pcfg;
      this.stdout = stdout;
      this.stderr = stderr;
      this.ideServices = ideServices;
     
      coursesDir = URIUtil.correctLocation("boot", "", "/courses");

      helpServer = startServer(stderr);
      port = helpServer.getPort();
    }

    private HelpServer startServer(PrintWriter stderr) throws IOException {
        for(int port = BASE_PORT; port < BASE_PORT+ATTEMPTS; port++){
              try {
                  HelpServer helpServer = new HelpServer(port, this, coursesDir);
                  indexSearcher = makeIndexSearcher();
                  stderr.println("HelpManager: using port " + port);
                  return helpServer;
              } catch (IOException e) {
                  // this is expected if the port is taken
              }
          }
          
          throw new IOException("Could not find port to run help server on");
    }
    
    public void stopServer() {
        helpServer.stop();
    }
    
    PathConfig getPathConfig(){
      return pcfg;
    }
	
	Path copyToTmp(ISourceLocation fromDir) throws IOException{
	  Path targetDir = Files.createTempDirectory(URIUtil.getLocationName(fromDir));
	  targetDir.toFile().deleteOnExit();
	  URIResolverRegistry reg = URIResolverRegistry.getInstance();
	  for(ISourceLocation file : reg.list(fromDir)){
	    if(!reg.isDirectory(file)){
	      String p = file.getPath();
	      int n = p.lastIndexOf("/");
	      String fileName = n >= 0 ? p.substring(n+1) : p;
	      // Only copy _* (index files) and segments* (defines number of segments)
	      if(fileName.startsWith("_") || fileName.startsWith("segments")){
	        Path targetFile = targetDir.resolve(fileName);
	        //System.out.println("copy " + file + " to " + toDir.resolve(fileName));
	        Files.copy(reg.getInputStream(file), targetFile); 
	        targetFile.toFile().deleteOnExit();
	      }
	    }
	  }
	  return targetDir;
	}
	
	private ArrayList<IndexReader> getReaders() throws IOException{
	  ArrayList<IndexReader> readers = new ArrayList<>();
	  URIResolverRegistry reg = URIResolverRegistry.getInstance();
	  for(ISourceLocation p : reg.list(coursesDir)){
	    if(reg.isDirectory(p) && URIUtil.getLocationName(p).toString().matches("^[A-Z].*")){
	      Path p1 = copyToTmp(p);
	      Directory directory = FSDirectory.open(p1);
	      try {
	        DirectoryReader ireader = DirectoryReader.open(directory);
	        readers.add(ireader);
	      } catch (IOException e){
	        stderr.println("Skipping index " + directory);
	      }
	    }
	  }
	  return readers;
	}
	
	IndexSearcher makeIndexSearcher() throws IOException {
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
		w.append(String.valueOf(getPort()));
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
		return URIUtil.create("http", "localhost:" + getPort(), "/Search", "searchFor=" + encoded, "");
	}
	
	public void handleHelp(String[] words){
		if(words[0].equals("help") && words.length > 1){
			try {
				ideServices.browse(makeSearchURI(words));
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
				return reportApropos(indexSearcher.search(query, maxSearch).scoreDocs);
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
		
		w.append(Concept.getHomeLink());
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
	
	String reportApropos(ScoreDoc[] hits) throws IOException{
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

  public int getPort() {
    return port;
  }

}
