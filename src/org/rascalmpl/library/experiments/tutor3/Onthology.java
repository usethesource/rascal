package org.rascalmpl.library.experiments.tutor3;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

/**
 * @author paulklint
 *
 */
public class Onthology {
	Path srcPath;
	Path destPath;
	
	static final String conceptExtension = "concept";
	
	Map<Path,Concept> conceptMap;
	private IValueFactory vf;
	private RascalExtraction rascalExtraction;
	private String courseName;
	
	private IndexWriter iwriter;
	private Path courseSrcPath;
	private Path courseDestPath;
	private Path libPath;
	
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

	public Onthology(Path srcPath, String courseName, Path destPath, Path libPath, RascalCommandExecutor executor) throws IOException, NoSuchRascalFunction, URISyntaxException{
		this.vf = ValueFactoryFactory.getValueFactory();
		this.srcPath = srcPath;
		this.courseSrcPath = srcPath.resolve(courseName);
		this.destPath = destPath;
		this.courseDestPath = destPath.resolve(courseName);
		
		this.libPath = libPath;

		this.courseName = courseName;
		conceptMap = new HashMap<>();
		
		Analyzer multiFieldAnalyzer = multiFieldAnalyzer();

		Directory directory = null;
	
		if(!Files.exists(courseDestPath)){
			Files.createDirectories(courseDestPath);
		}
		try {
			directory = FSDirectory.open(courseDestPath);
			IndexWriterConfig config = new IndexWriterConfig(multiFieldAnalyzer);
			config.setOpenMode( IndexWriterConfig.OpenMode.CREATE);
			iwriter = new IndexWriter(directory, config);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FileVisitor<Path> fileProcessor = new CollectConcepts();
		try {
			Files.walkFileTree(courseSrcPath, fileProcessor);
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Path conceptName : conceptMap.keySet()){
			Concept concept = conceptMap.get(conceptName);
			try {
				concept.preprocess(this, executor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Map<Path,Concept> getConceptMap(){
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
		return p.resolve(p.getFileName().toString() + "." + conceptExtension);
	}
	
	private Path makeRemoteFilePath(Path p){
		return p.resolve(p.getFileName().toString() + ".remote");
	}
	
	private Path makeConceptName(Path p){
		return  srcPath.relativize(p);
	}
	
	Path makeDestFilePath(Path path){
		return destPath.resolve(srcPath.relativize(path));
	}

	private class CollectConcepts extends SimpleFileVisitor<Path> {
		
		@Override public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs)
                  throws IOException{
			String fileName = file.getFileName().toString();
			
			if(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
				Path dest = makeDestFilePath(file);
				Path parent = dest.getParent();
				if(!Files.exists(parent)){
					Files.createDirectories(parent);
				}
				Files.copy(file, makeDestFilePath(file), REPLACE_EXISTING);
			}
			return FileVisitResult.CONTINUE;
		}
		
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
			
			//System.err.println(aDir);
			if(Files.exists(makeConceptFilePath(aDir))){
				Path conceptName = makeConceptName(aDir);
				if(!conceptName.equals(courseName)){
					Path conceptDestPath = destPath.resolve(conceptName);
					if(!Files.exists(conceptDestPath)){
						Files.createDirectories(conceptDestPath);
					}
				}
				Concept concept = new Concept(conceptName, readFile(makeConceptFilePath(aDir).toString()), destPath, libPath);
				conceptMap.put(conceptName, concept);
				iwriter.addDocument(makeLuceneDocument(conceptName.toString(), concept.getIndex(), concept.getSynopsis(), concept.getText()));
			} else
				if(Files.exists(makeRemoteFilePath(aDir))){
						ISourceLocation remoteLoc = readLocFromFile(makeRemoteFilePath(aDir).toString());
						String parentName = aDir.getName(aDir.getNameCount()-2).toString();
						Path remoteConceptName = makeConceptName(aDir);
						if(rascalExtraction == null){
							// Lazily load the RascalExtraction tool
							rascalExtraction = new RascalExtraction(vf);
						}
						ITuple extracted = rascalExtraction.extractDoc(vf.string(parentName), remoteLoc, new KWParams(vf).build());
						IString remoteConceptText = (IString) extracted.get(0);
						IList declarationInfoList = (IList) extracted.get(1);
						//System.err.println(remoteConceptText.getValue());
						Concept remoteConcept = new Concept(remoteConceptName, remoteConceptText.getValue(), destPath, libPath);
						conceptMap.put(remoteConceptName, remoteConcept);
						
						iwriter.addDocument(makeLuceneDocument(remoteConceptName.toString(), remoteConcept.getIndex(), remoteConcept.getSynopsis(), remoteConcept.getText()));
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
	
	private void addDeclarationInfo(Path remoteConceptName, IConstructor d) throws IOException{
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
	
	static int level(Path conceptName){
		return conceptName.getNameCount() - 1;
	}
	
	String bullets(int n){
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++){
			s.append("*");
		}
		s.append(" ");
		return s.toString();
	}
	
	private void genIncludeSubConcept(Path conceptName, Path subConceptName, StringWriter result){
		Concept concept = conceptMap.get(subConceptName);
		if(concept != null && subConceptName.startsWith(conceptName) && (level(subConceptName) == level(conceptName) + 1)){
			result.append(concept.genInclude()).append("\n");
		}
	}
	
	/**
	 * Generate "details" (i.e. a list of subconcepts), taking into account an ordered list of subconcepts that should come first
	 * @param conceptName	the root concept
	 * @param orderDetails subconcepts that should come first in generated list
	 * @return the list of details
	 */
	public String genDetails(Path conceptName, String[] orderDetails){
		Path[] keys = conceptMap.keySet().toArray(new Path[conceptMap.size()]);
		Arrays.sort(keys);
		StringWriter result = new StringWriter();
		HashSet<Path> seen = new HashSet<>();
		
		if(keys.length > 0){
			result.append("\n:leveloffset: +1\n");
		}
		for(String subConceptName : orderDetails){
			Path fullSubConceptName = conceptName.resolve(subConceptName);
			genIncludeSubConcept(conceptName, fullSubConceptName, result);
			seen.add(fullSubConceptName);
		}
		for(Path fullSubConceptName : keys){
			if(!seen.contains(fullSubConceptName)){
				genIncludeSubConcept(conceptName, fullSubConceptName, result);
			}
		}
		if(keys.length > 0){
			result.append("\n:leveloffset: -1");
		}
		return result.toString();
	}
	
	/**
	 * Generate a list item for a subconcept (if it is a subconcept within required depth)
	 * @param conceptName	the root concept
	 * @param subConceptName	subconcept
	 * @param start		 		start depth
	 * @param depth				maximal depth
	 * @param withSynopsis		add synopsis or not
	 * @param result			generated list item
	 */
	private void genListItemForSubConcept(Path conceptName, Path subConceptName, int start, int depth, boolean withSynopsis, StringWriter result){
		Concept subConcept = conceptMap.get(subConceptName);
		int newLevel = level(subConceptName);
		if(subConcept != null 
		   && !conceptName.equals(subConceptName) 
		   && subConceptName.startsWith(conceptName) 
		   && (newLevel - start <= depth)
		   ){
			result.append(bullets(newLevel)).append("<<").append(subConcept.getAnchor()).append(",").append(subConcept.getTitle()).append(">>");
			if(withSynopsis){
				result.append(": ").append(subConcept.getSynopsis());
			}
			result.append("\n");
		}
	}

	/**
	 * Generate a sub table-of-contents, i.e. a toc of all subconcepts upto certain depth
	 * @param conceptName	the root concept
	 * @param depth			depth of the toc
	 * @param withSynopsis	include a synopsis or not
	 * @param orderDetails	list of details that determines order
	 * @return the generated subtoc
	 */
	public String genSubToc(Path conceptName, int depth, boolean withSynopsis, String[] orderDetails) {
		int start = level(conceptName);
		Path[] keys = conceptMap.keySet().toArray(new Path[conceptMap.size()]);
		Arrays.sort(keys);
		HashSet<Path> seen = new HashSet<>();

		StringWriter result = new StringWriter().append("\n");
		for(String subConceptName : orderDetails){
			Path fullSubConceptName = conceptName.resolve(subConceptName);
			genListItemForSubConcept(conceptName, fullSubConceptName, start, start + depth, withSynopsis, result);
			seen.add(fullSubConceptName);
			for(Path fullSubSubConceptName : keys){
				if(!seen.contains(fullSubSubConceptName)){
					if(fullSubSubConceptName.startsWith(fullSubConceptName)){
						genListItemForSubConcept(fullSubConceptName, fullSubSubConceptName, start, start + depth, withSynopsis, result);
						seen.add(fullSubSubConceptName);
					}
				}
			}
		}
		for(Path fullSubConceptName : keys){
			if(!seen.contains(fullSubConceptName)){
				genListItemForSubConcept(conceptName, fullSubConceptName, start, start + depth, withSynopsis, result);
			}
		}
		return result.toString();
	}
}
