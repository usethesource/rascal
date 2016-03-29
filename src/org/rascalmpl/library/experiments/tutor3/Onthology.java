package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.KWParams;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Onthology {
	Path srcDir;
	Path destDir;
	
	Map<String,Concept> conceptMap;
	private IValueFactory vf;
	private RascalUtils rascalUtils;
	private Path courseName;

	public Onthology(Path srcDir, Path destDir){
		this.vf = ValueFactoryFactory.getValueFactory();
		this.rascalUtils = new RascalUtils(vf);
		this.srcDir = srcDir;
		this.destDir = destDir;
		this.courseName = this.srcDir.getName( this.srcDir.getNameCount() - 1);
		conceptMap = new HashMap<>();
		
		FileVisitor<Path> fileProcessor = new CollectConcepts();
		try {
			Files.walkFileTree(this.srcDir, fileProcessor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String conceptName : conceptMap.keySet()){
			Concept concept = conceptMap.get(conceptName);
			concept.finalize(getDetails(conceptName));
		}
	}
	
	public Map<String,Concept> getConceptMap(){
		return conceptMap;
	}
	
	private static String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( (line = reader.readLine()) != null ) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
	    }
	    reader.close();
	    return stringBuilder.toString();
	}
	
	static Path makeConceptFilePath(Path p){
		return Paths.get(p.toString(), p.getFileName().toString() + ".concept");
	}
	
	static Path makeRemoteFilePath(Path p){
		return Paths.get(p.toString(), p.getFileName().toString() + ".remote");
	}
	
	String makeConceptName(Path p){
		String s = p.toString();
		return courseName + s.substring(srcDir.toString().length(), s.length());
	}

	private final class CollectConcepts extends SimpleFileVisitor<Path> {
		
		@Override  public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
			if(Files.exists(makeConceptFilePath(aDir))){
				try {
					String conceptText = readFile(makeConceptFilePath(aDir).toString());
					String conceptName = makeConceptName(aDir);
					conceptMap.put(conceptName, new Concept(conceptName, conceptText, destDir));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
			if(Files.exists(makeRemoteFilePath(aDir))){
				try {
					String remote = readFile(makeRemoteFilePath(aDir).toString()).trim();
					String conceptName = makeConceptName(aDir);
					String moduleConceptName = conceptName + "/" + Concept.getConceptBaseName(conceptName);
					IMap result = rascalUtils.extractRemoteConcepts(vf.string(remote), vf.string(conceptName), new KWParams(vf).build());
					for(IValue key : result){
						String keyString = ((IString)key).getValue();
						String subConceptName = keyString.equals(moduleConceptName) ? conceptName : (conceptName + "/" + keyString);
						String conceptText = ((IString)result.get(key)).getValue();
						conceptMap.put(subConceptName, new Concept(subConceptName, conceptText, destDir, true));
					}
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
	
	int level(String s){
		return s.split("\\/").length - 1;
	}
	
	String indent(int n){
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < n; i++){
			s.append("  ");
		}
		return s.toString();
	}
	
	public String toc(){
		String[] keys = conceptMap.keySet().toArray(new String[conceptMap.size()]);
		Arrays.sort(keys);
		StringWriter result = new StringWriter();
		for(int i = 0; i < keys.length; i++){
			String conceptName = keys[i];
			Concept concept = conceptMap.get(conceptName);
			int newLevel = level(conceptName);
			result.append(indent(newLevel)).append("* ").append(concept.getConceptAsLink()).append("\n");
		}
		return result.toString();
	}
	
	public String getDetails(String conceptName){
		String[] keys = conceptMap.keySet().toArray(new String[conceptMap.size()]);
		Arrays.sort(keys);
		StringWriter result = new StringWriter();
		int start = level(conceptName);
		String sep = "";
		for(int i = 0; i < keys.length; i++){
			String cn = keys[i];
			Concept concept = conceptMap.get(cn);
			if(!conceptName.equals(cn) && level(cn) == start + 1){
				result.append(sep).append(concept.getConceptAsLink());
				sep = ", ";
			}
		}
		System.err.println("getDetails: " + result);
		return result.toString();
	}
}
