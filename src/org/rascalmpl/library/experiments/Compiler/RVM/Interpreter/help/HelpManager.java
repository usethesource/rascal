package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.tutor3.Concept;
import org.rascalmpl.library.experiments.tutor3.Onthology;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

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
        HelpServer helpServer = null;

        for(int port = BASE_PORT; port < BASE_PORT+ATTEMPTS; port++){
            try {
                helpServer = new HelpServer(port, this, coursesDir);
                // success!
                break;
            } catch (IOException e) {
                // failure is expected if the port is taken
                continue;
            }
        }

        if (helpServer == null) {
            throw new IOException("Could not find port to run help server on");
        }

        stderr.println("HelpManager: using port " + port);
        return helpServer;
    }
    
    public void refreshIndex() throws IOException {
        indexSearcher = makeIndexSearcher();
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
	
	
	private static final Pattern BAD_QUERY_CHARS = Pattern.compile("([+\\-!(){}\\[\\]\\^\"~*?:\\\\/]|(&&)|(\\|\\|))");

	private static String escapeForQuery(String s){
	    return BAD_QUERY_CHARS.matcher(s.toLowerCase()).replaceAll("\\\\$1");
	}
	
	private String escapeHtml(String s){
		// TODO: switch to StringEscapeUtils when compiled inside Eclipse
		return s;
	}
	
	private URI makeSearchURI(String[] words) throws URISyntaxException, UnsupportedEncodingException{
		String encoded = URLEncoder.encode(Arrays.stream(words).skip(1).collect(Collectors.joining()), "UTF-8");
		return URIUtil.create("http", "localhost:" + getPort(), "/search-results.html", "searchFor=" + encoded, "");
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
		    printHelp(words, stdout);
		}
	}
	
	private ScoreDoc[] search(String[] words) {
		try {
            if (indexSearcher != null) {
                Analyzer multiFieldAnalyzer = Onthology.multiFieldAnalyzer();
                String query = Arrays.stream(words).map(HelpManager::escapeForQuery).collect(Collectors.joining(" "));
                QueryParser parser  = new MultiFieldQueryParser(new String[] {"index", "synopsis", "doc"}, multiFieldAnalyzer);
                return indexSearcher.search(parser.parse(query), maxSearch).scoreDocs;
            }
            return new ScoreDoc[0];
		} catch (ParseException | IOException e) {
		    stderr.println("Cannot parse/search query: " + Arrays.toString(words) + ", " + e.getMessage());
            return new ScoreDoc[0];
		}
	}
	
	public void printHelp(String[] words, PrintWriter target){
		//TODO Add here for example credits, copyright, license
		
		if(words.length <= 1){
			IntroHelp.print(target);
			return;
		}

		if(!indexAvailable()){
		    target.println();
			return;
		}
		
		try {
            reportApropos(search(words), target);
        }
        catch (IOException e) {
		    target.println("Search failed with: " + e.getMessage());
        }
	}

    public InputStream jsonHelp(String[] words) {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (JsonWriter w = new JsonWriter(new OutputStreamWriter(target, StandardCharsets.UTF_8))) {
            w.beginObject();
            w.name("results");
            w.beginArray();
            for (ScoreDoc r : search(words)) {
                appendJsonResult(findDocument(r.doc), w);
            }
            w.endArray();
            w.endObject();
        }
        catch (IOException e) {
        }
        return new ByteArrayInputStream(target.toByteArray());
    }
		
	private void appendJsonResult(Document hitDoc, JsonWriter w) throws IOException {
	    if (hitDoc != null) {
	        w.beginObject();
	        String name = hitDoc.get("name");
	        w.name("name");
	        w.value(name);
	        w.name("url");
	        w.value(makeURL(name));
	        w.name("text");
	        w.value(getField(hitDoc, "synopsis"));
	        String signature = getField(hitDoc, "signature");
	        if(!signature.isEmpty()){
	            w.name("code");
	            w.value(signature);
	        }
	        w.endObject();
	    }
    }

    private String getField(Document hitDoc, String field){
		String s = hitDoc.get(field);
		return s == null ? "" : s;
	}
	

	private Document findDocument(int needle) throws IOException {
	    return indexSearcher != null ? indexSearcher.doc(needle) : null;
	}
	
	private void reportApropos(ScoreDoc[] hits, PrintWriter target) throws IOException{
		for (int i = 0; i < Math.min(hits.length, maxSearch); i++) {
			Document hitDoc = findDocument(hits[i].doc);
			
			if (hitDoc != null) {
			    String name = hitDoc.get("name");
			    String signature = getField(hitDoc, "signature");
			    String synopsis = getField(hitDoc, "synopsis");
			    target.append(name).append(":\n\t").append(synopsis);
			    if(!signature.isEmpty()){
			        target.append("\n\t").append(signature);
			    }
			    target.append("\n");
			}
		}
	}

  public int getPort() {
    return port;
  }


}
