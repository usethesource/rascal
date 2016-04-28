package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Onthology {
	Path srcDir;
	Path destDir;
	
	static final String conceptExtension = "concept2";
	
	Map<String,Concept> conceptMap;
	private IValueFactory vf;
	private RascalUtils rascalUtils;
	private Path courseName;
	
	private RascalCommandExecutor executor;

	public Onthology(Path srcDir, Path destDir, PrintWriter err){
		this.vf = ValueFactoryFactory.getValueFactory();
		this.rascalUtils = new RascalUtils(vf);
		this.srcDir = srcDir;
		this.destDir = destDir;

		this.courseName = this.srcDir.getName(this.srcDir.getNameCount() - 1);
		conceptMap = new HashMap<>();
		
		this.executor = new RascalCommandExecutor(err);
		
		FileVisitor<Path> fileProcessor = new CollectConcepts();
		try {
			Files.walkFileTree(this.srcDir, fileProcessor);
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
	
	private static ISourceLocation readLocFromFile(String file) throws IOException{
		String s = readFile(file);
		return ValueFactoryFactory.getValueFactory().sourceLocation(s);
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
			} else
				if(Files.exists(makeRemoteFilePath(aDir))){
					try {
						String remote = readFile(makeRemoteFilePath(aDir).toString());
						if(remote.endsWith("\n")){
							remote = remote.substring(0,  remote.length()-1);
						}
						remote = remote.trim();
						String parentName = aDir.getName(aDir.getNameCount()-2).toString();
						String remoteConceptName = makeConceptName(aDir);
						
						System.err.println(remote + ": " + remoteConceptName);
						IString remoteConceptText = rascalUtils.extractRemoteConcepts(vf.string(parentName), vf.string(remote), new KWParams(vf).build());
						System.err.println(remoteConceptText.getValue());
						conceptMap.put(remoteConceptName, new Concept(remoteConceptName, remoteConceptText.getValue(), destDir));
					} catch (IOException e) {
						e.printStackTrace();
					}
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
