package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
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
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RascalExtraction.RascalExtraction;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
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
	
	public static Analyzer multiFieldAnalyzer(){
		Analyzer stdAnalyzer = new StandardAnalyzer();
		
		HashMap<String,Analyzer> analyzerMap = new HashMap<>();
		
		//analyzerMap.put("name", new SimpleAnalyzer());
		analyzerMap.put("index", new WhitespaceAnalyzer());
		analyzerMap.put("synopsis", stdAnalyzer);
		analyzerMap.put("signature", stdAnalyzer);
		analyzerMap.put("doc", stdAnalyzer);
		
		return new PerFieldAnalyzerWrapper(stdAnalyzer, analyzerMap);
	}

	public Onthology(Path srcDir, Path destDir, PrintWriter err) throws IOException, NoSuchRascalFunction, URISyntaxException{
		this.vf = ValueFactoryFactory.getValueFactory();
		this.rascalExtraction = new RascalExtraction(vf);
		this.srcDir = srcDir;
		this.destDir = destDir;

		this.courseName = this.srcDir.getName(this.srcDir.getNameCount() - 1);
		conceptMap = new HashMap<>();
		
		this.executor = new RascalCommandExecutor(err);
		
		Analyzer multiFieldAnalyzer = multiFieldAnalyzer();

		Directory directory = null;
		try {
			directory = FSDirectory.open(destDir);
			IndexWriterConfig config = new IndexWriterConfig(multiFieldAnalyzer);
			iwriter = new IndexWriter(directory, config);
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
				Concept concept = new Concept(conceptName, readFile(makeConceptFilePath(aDir).toString()), destDir);
				conceptMap.put(conceptName, concept);
				iwriter.addDocument(makeLuceneDocument(conceptName, concept.getIndex(), concept.getSynopsis(), concept.getText()));
			} else
				if(Files.exists(makeRemoteFilePath(aDir))){
						ISourceLocation remoteLoc = readLocFromFile(makeRemoteFilePath(aDir).toString());
						String parentName = aDir.getName(aDir.getNameCount()-2).toString();
						String remoteConceptName = makeConceptName(aDir);
						ITuple extracted = rascalExtraction.extractDoc(vf.string(parentName), remoteLoc, new KWParams(vf).build());
						IString remoteConceptText = (IString) extracted.get(0);
						IList declarationInfoList = (IList) extracted.get(1);
						System.err.println(remoteConceptText.getValue());
						Concept remoteConcept = new Concept(remoteConceptName, remoteConceptText.getValue(), destDir);
						conceptMap.put(remoteConceptName, remoteConcept);
						
						iwriter.addDocument(makeLuceneDocument(remoteConceptName, remoteConcept.getIndex(), remoteConcept.getSynopsis(), remoteConcept.getText()));
						for(IValue d : declarationInfoList){
							addDeclarationInfo(remoteConceptName, (IConstructor)d);
						}
				}
			return FileVisitResult.CONTINUE;
		}
	}
	
	private Document makeLuceneDocument(String name, String index, String synopsis, String doc){
		Document luceneDoc = new Document();
		
		Field nameField = new Field("name", name, TextField.TYPE_STORED);
		luceneDoc.add(nameField);
		
		Field indexField = new Field("index", index + " " + name.replaceAll("/", " ").toLowerCase(), TextField.TYPE_NOT_STORED);
		indexField.setBoost(2f);
		luceneDoc.add(indexField);
		
		Field synopsisField = new Field("synopsis", synopsis, TextField.TYPE_STORED);
		synopsisField.setBoost(2f);
		luceneDoc.add(synopsisField);
		
		Field docField = new Field("doc", doc, TextField.TYPE_NOT_STORED);
		luceneDoc.add(docField);
		return luceneDoc;
	}
	
	private void addDeclarationInfo(String remoteConceptName, IConstructor d) throws IOException{
		String consName = d.getName();
		String moduleName = remoteConceptName + "/" + ((IString) d.get("moduleName")).getValue().replaceAll("::",  "/");
		
		String doc = d.has("doc") ? ((IString) d.get("doc")).getValue() : "";
		String synopsis = d.has("synopsis") ? ((IString) d.get("synopsis")).getValue() : "";
		
		if(consName.equals("moduleInfo")){
			iwriter.addDocument(makeLuceneDocument(moduleName, "", synopsis, doc));
			return;
		}
		String name = ((IString) d.get("name")).getValue();
		String subConceptName = moduleName + "/" + name;
		Document luceneDoc = makeLuceneDocument(subConceptName, "", synopsis, doc);

		String signature = ((IString) d.get("signature")).getValue();
		Field signatureField = new Field("signature", signature, TextField.TYPE_STORED);
		luceneDoc.add(signatureField);
		
		iwriter.addDocument(luceneDoc);
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
