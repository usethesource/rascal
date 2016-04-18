package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rascalmpl.value.IValue;

public class Concept {
	private final String name;
	private String text = null;
	private final Path destDir;
	private boolean remote;
	private String title;
	private String synopsis;

	public Concept(String name, String text, Path destDir, boolean remote){
		this.name = name;
		this.text = text;
		this.destDir = destDir;
		this.remote = remote;
		getTitleAndSynopsis();
	}
	
	public Concept(String name, String text, Path destDir){
		this(name, text, destDir, false);
	}
	
	public String getName(){
		return name;
	}
	
	public String getTitle(){
		return title == null ? getName() : title;
	}
	
	public String getSynopsis(){
		return synopsis;
	}
	
	public String getText(){
		return text;
	}
	
	private void getTitleAndSynopsis(){
		BufferedReader reader = new BufferedReader(new StringReader(text));
		String line = null;
		synopsis = "";
		try {
			while( (line = reader.readLine()) != null ) {
				if(line.startsWith("# ")){
					title = line.substring(2).trim();
				}
				if(line.matches("^.Synopsis")){
					line = reader.readLine();
					if(line == null){
						return;
					}
					line = line.trim();
					synopsis = line.endsWith(".") ? line : (line + ".");
					return;
				}
			}
		} catch (IOException e) {
			return;
		}
		return;
	}
	
	private String getConceptBaseName(){
		int i = name.lastIndexOf("/");
		return i < 0 ? name : name.substring(i + 1, name.length());
	}
	
	public String getAnchor(){
	  String[] parts = name.split("/");
	  int n = parts.length;
	  if(n >= 2){
		  return parts[n-2] + "-" + parts[n-1];
	  } else {
		  return parts[0];
	  }
	}
	
	public String getHtmlFileName(){
		return destDir.toString() + "/" + name + (remote ? "" : ("/" + getConceptBaseName())) + ".html";
	}
	
	private String getADocFileName(){
		return destDir.toString() + "/" + name + (remote ? "" : ("/" + getConceptBaseName())) + ".adoc";
	}
	
	public String getConceptAsInclude(){
		return "include::" + getConceptBaseName() + "/" + getConceptBaseName() + ".adoc" + "[" + getConceptBaseName() + "]\n";
	}
	
	private String complete(String line){
		return line + (line.endsWith(";") ? "\n" : ";\n");
	}
	
	String getAttr(String line, String attr, String defaultVal){
		Pattern p = Pattern.compile(attr + "=([^,\\]])");
		Matcher m = p.matcher(line); 
		
		return m.find() ? m.group(1) : defaultVal;
	}
	
	String makeRenderSave(String line, String width, String height, String file){
		Pattern p = Pattern.compile("render\\((.*)\\)");
		Matcher m = p.matcher(line); 
		if(m.find()){
			String arg = m.group(1);
			if(height.isEmpty()){
				return "renderSave(" + arg + "," + file + ");";
			} else {
				return  "renderSave(" + arg + "," + width + "," + height + "," + file + ");";
			}
		}
		return line;
	}
	
	public void preprocess(Onthology onthology, PrintWriter err, RascalCommandExecutor executor) throws IOException{
		//executor = new RascalCommandExecutor(err);
		BufferedReader reader = new BufferedReader(new StringReader(text));

		StringWriter preprocessOut = new StringWriter();
		String line = null;
		String[] details = new String[0];

		preprocessOut.append("[[").append(getAnchor()).append("]]\n");

		while( (line = reader.readLine()) != null && !line.startsWith("#")){
			preprocessOut.append(line).append("\n");
		}
		if(line == null){
			preprocessOut.append("# ").append(name).append("\n");
		} else {
			title = line.substring(2).trim();
			preprocessOut.append(line).append("\n");
			if(Onthology.level(name)==0){
				preprocessOut.append("include::../CommonDefs.adoc[]\n");
				preprocessOut.append(":concept: " + name + "\n");
			} else {
				preprocessOut.append(":concept: ").append(onthology.makeConceptNameInCourse(name)).append("\n");
			}
			while( (line = reader.readLine()) != null ) {
				if(line.startsWith(".Details")){
					line = reader.readLine();
					details = line.split("\\s");
				} else if(line.startsWith("```rascal-shell") || line.startsWith("[source,rascal-shell") || line.startsWith("[source,rascal-figure")) {
					boolean isContinue = line.contains("continue");
					boolean isFigure = line.contains("figure");
					String width = "";
					String height = "";
					String file = "";
					if(isFigure){
						height = getAttr(line, "height", height);
						width = getAttr(line, "width", width);
						file = getAttr(line,"file", file);
					}
					boolean mayHaveErrors = line.contains("error");
					if(line.startsWith("[")){
						line = reader.readLine();	// skip ----
						if(line == null){
							break;
						}
					}
					if(!isContinue){
						executor.reset();
					}

					executor.resetOutput();
					preprocessOut.append("[source,rascal-shell");
					if(mayHaveErrors){
						preprocessOut.append("-error");
					}
					preprocessOut.append("]\n").append("----\n");
					
					if(isFigure){
//						rascalShell.eval("import vis::Figure;");
//						rascalShell.eval("import vis::Render;");
					}
					boolean moreShellInput = true;
					while( moreShellInput && (line = reader.readLine()) != null ) {
						if(line.equals("```") || line.equals("----")){
							break;
						}
						if(isFigure && line.startsWith("render(")){
							preprocessOut.append("rascal>").append(line).append("\n");
							line = makeRenderSave(line, height, width, file);
						}
						if(!isFigure){
							preprocessOut.append("rascal>").append(line).append("\n");
							String continuationLine = "";
							while(!executor.isStatementComplete(line)){
								 if((continuationLine = reader.readLine()) != null){ 
								     if(continuationLine.equals("```") || continuationLine.equals("----")){
								    	 moreShellInput = false;
								    	 break;
								     }
									 preprocessOut.append(">>>>>>>").append(continuationLine).append("\n");
								 } else {
									 break;
								 }
								 line += "\n" + continuationLine;
							}
							line += "\n";
						}
						String resultOutput = "";
						try {
							if(!isFigure){
								resultOutput = executor.evalPrint(line);
							}
						} catch (Exception e){
							String msg = "While parsing '" + complete(line) + "': " + e.getMessage();
							System.err.println(msg);
							executor.error("* __" + name + "__:");
							executor.error(msg);
						}

						String messages = executor.getMessages();
						executor.resetOutput();
						preprocessOut.append(messages);
						if(!messages.contains("[error]")){
						   preprocessOut.append(resultOutput);
						}
//						if(!isFigure){
//							if(result == null){
//								preprocessOut.append("ok\n");
//							} else{
//									preprocessOut.append(result.getType().toString()).append(": ").append(result.toString()).append("\n");
//							}
//						}
						if(!mayHaveErrors && (messages.contains("[error]") || messages.contains("Exception")) ){
							executor.error("* __" + name + "__:");
							executor.error("```");
							executor.error(messages.trim());
							executor.error("```");
						}
					}
					preprocessOut.append("----\n");
				} else if(line.startsWith("subtoc::[")){
					Pattern p = Pattern.compile("subtoc::\\[(\\d*)\\]");
					Matcher m = p.matcher(line); 
					int depth = 0;
					if(m.find()){
						String intStr = m.group(1);
						depth = intStr.equals("") ? 0 : Integer.parseInt(intStr.substring(0,intStr.length()));
					}
					preprocessOut.append(onthology.getSubToc(name, depth, true, details));
				} else if(line.contains("image:")){
					Pattern p = Pattern.compile("(^.*)(image::?)([^\\[]+)(\\[.*$)");
					Matcher m = p.matcher(line);
					if(m.find()){
						String pre = m.group(1);
						String image = m.group(2);
						String link = m.group(3);
						String post = m.group(4);
						if(!link.contains("{") && Onthology.level(name) > 0){
							link = "{concept}/" + link;
						}
						preprocessOut.append(pre).append(image).append(link).append(post).append("\n");
					} else {
						preprocessOut.append(line).append("\n");
					}
				} else {
					preprocessOut.append(line).append("\n");
				}
			}

			preprocessOut.append(onthology.getDetails(name, details));
		}
		CourseCompiler.writeFile(getADocFileName(), preprocessOut.toString());
	}

}
