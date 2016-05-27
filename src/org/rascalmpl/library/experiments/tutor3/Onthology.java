package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RascalExtraction.RascalExtraction;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;


public class Onthology {
	Path srcDir;
	Path destDir;
	
	static final String conceptExtension = "concept2";
	
	Map<String,Concept> conceptMap;
	private IValueFactory vf;
	private RascalExtraction rascalExtraction;
	private Path courseName;
	
	private RascalCommandExecutor executor;
	private IndexWriter iwriter;

	public Onthology(Path srcDir, Path destDir, PrintWriter err) throws IOException, NoSuchRascalFunction{
		this.vf = ValueFactoryFactory.getValueFactory();
		this.rascalExtraction = new RascalExtraction(vf);
		this.srcDir = srcDir;
		this.destDir = destDir;

		this.courseName = this.srcDir.getName(this.srcDir.getNameCount() - 1);
		conceptMap = new HashMap<>();
		
		this.executor = new RascalCommandExecutor(err);
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);

		// Store the index in memory:
		// Directory directory = new RAMDirectory();
		// To store an index on disk, use this instead:
		Directory directory = null;
		try {
			directory = FSDirectory.open(destDir.toFile());
			iwriter = new IndexWriter(directory, analyzer, true, new IndexWriter.MaxFieldLength(25000));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FileVisitor<Path> fileProcessor = new CollectConcepts();
		try {
			Files.walkFileTree(this.srcDir, fileProcessor);
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String conceptName : conceptMap.keySet()){
			Concept concept = conceptMap.get(conceptName);
			try {
				concept.preprocess(this, err, executor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			IndexReader ireader = IndexReader.open(directory); // read-only=true
			IndexSearcher isearcher = new IndexSearcher(ireader);
			// Parse a simple query that searches for "text":
			QueryParser parser = new QueryParser(Version.LUCENE_35, "fieldname", analyzer);
			Query query = parser.parse("on-the-fly");
			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
			
			// Iterate through the results:
			for (int i = 0; i < hits.length; i++) {
				System.err.println("**** HIT " + i);
				Document hitDoc = isearcher.doc(hits[i].doc);
				System.err.println(hitDoc.get("title"));
			}
			isearcher.close();
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
	
	public Map<String,Concept> getConceptMap(){
		return conceptMap;
	}
	
	private static String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    StringWriter  result = new StringWriter();
	    String line = null;

	    while( (line = reader.readLine()) != null ) {
	    	result.append(line).append("\n");
	    }
	    reader.close();
	    return result.toString();	
	}
	
	ISourceLocation readLocFromFile(String file) {
		TypeStore store = new TypeStore();
		
		Type start = TypeFactory.getInstance().sourceLocationType();
		
		try (StringReader in = new StringReader(readFile(file))) {
			return (ISourceLocation) new StandardTextReader().read(vf, store, start, in);
		}
		catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
		} 
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
		}
	}
	
	private Path makeConceptFilePath(Path p){
		return Paths.get(p.toString(), p.getFileName().toString() + "." + conceptExtension);
	}
	
	private Path makeRemoteFilePath(Path p){
		return Paths.get(p.toString(), p.getFileName().toString() + ".remote");
	}
	
	private String makeConceptName(Path p){
		String s = p.toString();
		return courseName + s.substring(srcDir.toString().length(), s.length());
	}
	
	String makeConceptNameInCourse(String conceptName){
		int n = conceptName.indexOf("/");
		return n < 0 ? "" : conceptName.substring(n + 1, conceptName.length());
	}

	private final class CollectConcepts extends SimpleFileVisitor<Path> {

		@Override  public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
			String cpf = makeConceptFilePath(aDir).toString();
			if(cpf.contains("/ValueUI") 
			   || cpf.contains("/Vis/") 
			   || cpf.contains("/SyntaxHighlightingTemplates")
			   || cpf.contains("/ShellExec")
			   || cpf.contains("/Resources")
			 
			   ){
				return FileVisitResult.CONTINUE;
			}
			
			System.err.println(aDir);
			if(Files.exists(makeConceptFilePath(aDir))){
				String conceptName = makeConceptName(aDir);
				conceptMap.put(conceptName, new Concept(conceptName, readFile(makeConceptFilePath(aDir).toString()), destDir));
				Document doc = new Document();
				doc.add(new Field("title", conceptName, Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field("fieldname", conceptMap.get(conceptName).getText(), Field.Store.YES, Field.Index.ANALYZED));
				iwriter.addDocument(doc);
			} else
				if(Files.exists(makeRemoteFilePath(aDir))){
						ISourceLocation remoteLoc = readLocFromFile(makeRemoteFilePath(aDir).toString());
						String parentName = aDir.getName(aDir.getNameCount()-2).toString();
						String remoteConceptName = makeConceptName(aDir);
						IString remoteConceptText = rascalExtraction.extractDoc(vf.string(parentName), remoteLoc, new KWParams(vf).build());
						System.err.println(remoteConceptText.getValue());
						conceptMap.put(remoteConceptName, new Concept(remoteConceptName, remoteConceptText.getValue(), destDir));
						
						Document doc = new Document();
						doc.add(new Field("title", remoteConceptName, Field.Store.YES, Field.Index.ANALYZED));
						doc.add(new Field("fieldname", conceptMap.get(remoteConceptName).getText(), Field.Store.YES, Field.Index.ANALYZED));
						iwriter.addDocument(doc);
				}
			return FileVisitResult.CONTINUE;
		}
	}
	
	public ArrayList<Concept> resolve(String conceptRef){
		ArrayList<Concept> result = new ArrayList<>();
		for(String cn : conceptMap.keySet()){
			if(cn.endsWith(conceptRef)){
				result.add(conceptMap.get(cn));
			}
		}
		return result;
	}
	
	static int level(String s){
		return s.split("\\/").length - 1;
	}
	
	String indent(int n){
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++){
			s.append("  ");
		}
		return s.toString();
	}
	
	String bullets(int n){
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++){
			s.append("*");
		}
		s.append(" ");
		return s.toString();
	}
	
	private void includeSubConcept(String conceptName, String subConceptName, StringWriter result){
		Concept concept = conceptMap.get(subConceptName);
		if(concept != null && subConceptName.startsWith(conceptName) && (level(subConceptName) == level(conceptName) + 1)){
			result.append(concept.getConceptAsInclude()).append("\n");
		}
	}
	
	public String getDetails(String conceptName, String[] orderDetails){
		String[] keys = conceptMap.keySet().toArray(new String[conceptMap.size()]);
		Arrays.sort(keys);
		StringWriter result = new StringWriter();
		HashSet<String> seen = new HashSet<>();
		
		if(keys.length > 0){
			result.append("\n:leveloffset: +1\n");
//			result.append("\n:leveloffset: " + (level(conceptName) + 1)).append("\n");
		}
		for(String subConceptName : orderDetails){
			String fullSubConceptName = conceptName + "/" + subConceptName;
			includeSubConcept(conceptName, fullSubConceptName, result);
			seen.add(fullSubConceptName);
		}
		for(String fullSubConceptName : keys){
			if(!seen.contains(fullSubConceptName)){
				includeSubConcept(conceptName, fullSubConceptName, result);
			}
		}
		if(keys.length > 0){
			result.append("\n:leveloffset: -1");
//			result.append("\n:leveloffset: " + (level(conceptName))).append("\n");
		}
		return result.toString();
	}
	
	private void listItemSubConcept(String conceptName, String subConceptName, int start, int depth, boolean withSynopsis, StringWriter result){
		Concept subConcept = conceptMap.get(subConceptName);
		int newLevel = level(subConceptName);
		if(subConcept != null 
		   && !conceptName.equals(subConceptName) 
		   && subConceptName.startsWith(conceptName + "/") 
		   && (newLevel - start <= depth)
		   ){
			result.append(bullets(newLevel)).append("<<").append(subConcept.getAnchor()).append(",").append(subConcept.getTitle()).append(">>");
			if(withSynopsis){
				result.append(": ").append(subConcept.getSynopsis());
			}
			result.append("\n");
		}
	}

	public String getSubToc(String conceptName, int depth, boolean withSynopsis, String[] orderDetails) {
		int start = level(conceptName);
		String[] keys = conceptMap.keySet().toArray(new String[conceptMap.size()]);
		Arrays.sort(keys);
		HashSet<String> seen = new HashSet<>();

		StringWriter result = new StringWriter().append("\n");
		for(String subConceptName : orderDetails){
			String fullSubConceptName = conceptName + "/" + subConceptName;
			listItemSubConcept(conceptName, fullSubConceptName, start, start + depth, withSynopsis, result);
			seen.add(fullSubConceptName);
			for(String fullSubSubConceptName : keys){
				if(!seen.contains(fullSubSubConceptName)){
					if(fullSubSubConceptName.startsWith(fullSubConceptName)){
						listItemSubConcept(fullSubConceptName, fullSubSubConceptName, start, start + depth, withSynopsis, result);
						seen.add(fullSubSubConceptName);
					}
				}
			}
		}
		for(String fullSubConceptName : keys){
			if(!seen.contains(fullSubConceptName)){
				listItemSubConcept(conceptName, fullSubConceptName, start, start + depth, withSynopsis, result);
			}
		}
		return result.toString();
	}
}
