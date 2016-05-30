package
org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.io.PrintWriter;
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
	
	private PrintWriter stdout;
	private PrintWriter stderr;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;
	}
	
	private String escapeForQuery(String s){
		return s.toLowerCase().replaceAll("([+\\-!(){}\\[\\]\\^\"~*?:\\\\/]|(&&)|(\\|\\|))","\\\\$1");
	}
	
	public void printHelp(String[] words){
		//TODO Add here for example credits, copyright, license
		
		if(words.length <= 1){
			IntroHelp.print(stdout);
			return;
		}

		Path destDir = Paths.get("/Users/paulklint/git/rascal/src/org/rascalmpl/courses/");
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
			    
			ScoreDoc[] hits = isearcher.search(query, 20).scoreDocs;
			
			// Iterate through the results:
			for (int i = 0; i < Math.min(hits.length, 20); i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				System.err.println(i + ": " + hitDoc.get("name") 
				                            + getInfo(hitDoc, "synopsis") 
				                            + getInfo(hitDoc, "signature"));
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
		return (s == null || s.isEmpty()) ? "" : " -- " + s;
	}
	
}
