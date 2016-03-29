package org.rascalmpl.library.experiments.tutor3;

import java.io.StringWriter;
import java.nio.file.Path;

public class Concept {
	private final String name;
	private String text;
	private final Path destDir;
	
	private final boolean isRemote;

	public Concept(String name, String text, Path destDir, boolean isRemote){
		this.name = name;
		this.text = text;
		this.isRemote = isRemote;
		this.destDir = destDir;
	}
	
	public Concept(String name, String text, Path destDir){
		this(name, text, destDir, false);
	}
	
	public void finalize(String details){
		text = "# Name\n" + getNameAsSegmentedLinks() + "\n" +
	           (details.isEmpty() ? "" : ("# Details\n" + details + "\n")) +
	           text;
	}
	
	public String getName(){
		return name;
	}
	
	public String getText(){
		return text;
	}
	
	String cleanup(String text){
		return "# Name\n" + getNameAsSegmentedLinks() + "\n" + text;
	}
	
	static String getConceptBaseName(String name){
		int i = name.lastIndexOf("/");
		return i < 0 ? name : name.substring(i + 1, name.length());
	}
	
	public String getHtmlFileName(){
		return destDir.toString() + "/" + name + (isRemote ? "" : ("/" + getConceptBaseName(name))) + ".html";
	}
	
	public String getConceptAsLink(){
		return "[" + Concept.getConceptBaseName(name) + "](" + name + ")";
	}
	
	public String getNameAsSegmentedLinks(){
		StringWriter result = new StringWriter();
		int to = name.indexOf("/");
		while(to > 0){
			String sub = name.substring(0, to);
			result.append("[").append(getConceptBaseName(sub)).append("](").append(sub).append(")/");
			to = name.indexOf("/", to+1);
		}
		result.append("[").append(getConceptBaseName(name)).append("](").append(name).append(")");
		return result.toString();
	}
}
