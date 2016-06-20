package org.rascalmpl.library.experiments.tutor3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Concept {
	private final Path name;
	private String text = null;
	private final Path destPath;
	private boolean remote;
	private String title;
	private String synopsis;
	private String index;
	private Path libPath;

	public Concept(Path name, String text, Path destPath, Path libPath, boolean remote){
		this.name = name;
		this.text = text;
		this.destPath = destPath;
		this.libPath = libPath;
		this.remote = remote;
		title = extract(titlePat);
		synopsis = extractSynopsis();
		index = extractIndex();
	}
	
	public Concept(Path name, String text, Path destDir, Path libPath){
		this(name, text, destDir, libPath, false);
	}
	
	public Path getName(){
		return name;
	}
	
	public String getTitle(){
		return title == null ? getName().toString() : title;
	}
	
	public String getSynopsis(){
		return synopsis;
	}
	
	public String getText(){
		return text;
	}
	
	public String getIndex(){
		return index;
	}
	
	Pattern titlePat = Pattern.compile("(?ms)^# (.*?)$");
	Pattern indexPat = Pattern.compile("(?ms)^\\.Index$\\n(.*?)$");
	Pattern synopsisPat = Pattern.compile("(?ms)^\\.Synopsis$\\n(.*?)$");
	
	private String extractSynopsis(){
		String s = extract(synopsisPat);
		return s.isEmpty() ? "" : (s.endsWith(".") ? s : (s + "."));
	}
	
	private String extractIndex(){
		String s = extract(indexPat);
		if(!s.isEmpty()){
			Matcher matcher = indexPat.matcher(text);
			text = matcher.replaceFirst("");
		}
		return s;
	}

	private String extract(Pattern pat){
		Matcher matcher = pat.matcher(text);
		
		return matcher.find() ? matcher.group(1).trim() : "";
	}
	
	private String getConceptBaseName(){
		return name.getFileName().toString();
	}
	
	public String getAnchor(){
	  int n = name.getNameCount();
	  return n >= 2 ? name.getName(n-2) + "-" + name.getName(n-1) : name.getFileName().toString();
	}
	
	private String getADocFileName(){
		return destPath.toString() + "/" + name + (remote ? "" : ("/" + getConceptBaseName())) + ".adoc";
	}
	
	public String genInclude(){
		String baseName = getConceptBaseName();
		return "include::" + baseName + "/" + baseName + ".adoc" + "[" + baseName + "]\n";
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
				return  "renderSave(" + arg + ", |file://" + file + "|, width=" + width + ", height=" + height + ");";
			}
		}
		return line;
	}
	
	String commonDefs =	
		":icons:        font\n" + 
		":iconfont-remote!:\n" +
		//":iconfont-name: font-awesome.min\n" +
		":images:       ../images/\n" +
		":table-caption!:\n" +
		":docinfo1:\n";
	
	public static String getSearchForm(){
		return
		"<form class=\"search-form\" id=\"searchbox\" action=\"/Search\">\n" +
		"<input class=\"search-input\" id=\"search\" name=\"searchFor\" type=\"search\" placeholder=\"Search ...\">\n" +
		"<input class=\"search-submit\" id=\"submit\" type=\"submit\" value=\"&#10140;\" onkeypress=\"if(event.keyCode==13) {javascript:form.submit();}\">\n" +
		"</form>\n";
	}
	
	private final String prompt = "rascal>"; //"<i class=\"prompt\">::before</i>";
	private final String continuation = ">>>>>>>"; //"<i class=\"continuation\">::before</i>";
	
	public void preprocess(Onthology onthology, RascalCommandExecutor executor) throws IOException{
		System.err.println("Preprocessing: " + name);
		BufferedReader reader = new BufferedReader(new StringReader(text));

		StringWriter preprocessOut = new StringWriter();
		String line = null;
		String[] details = new String[0];
		int level = Onthology.level(name);

		while( (line = reader.readLine()) != null && !line.startsWith("#")){
			preprocessOut.append(line).append("\n");
		}
		if(line == null){
			preprocessOut.append("# ").append(name.toString()).append("\n");
		} else {
			title = line.substring(2).trim();
			if(level > 0){
				preprocessOut.append("\n[[").append(getAnchor()).append("]]\n");
				preprocessOut.append(line).append("\n");
			} else {
				line = line.replaceFirst("#",  "=");
				preprocessOut.append(line).append("\n");
				preprocessOut.append(commonDefs);
				preprocessOut.append(":LibDir: ").append(libPath.toString()).append("/\n");
			}
			
			preprocessOut.append(":concept: ").append(name.toString()).append("\n");
			
			if(level == 0){
				preprocessOut.append("\n++++\n");
				preprocessOut.append(getSearchForm());
				preprocessOut.append("++++\n");
			}
			while( (line = reader.readLine()) != null ) {
				if(line.startsWith(".Details")){
					line = reader.readLine();
					details = line.split("\\s");
				} else if(line.startsWith("```rascal-shell") || line.startsWith("[source,rascal-shell") || line.startsWith("[source,rascal-figure")) {
					boolean isContinue = line.contains("continue");
					boolean isFigure = line.contains("figure");
					String width = "100";
					String height = "100";
					String file = "/tmp/fig.png";
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
					
					boolean moreShellInput = true;
					while( moreShellInput && (line = reader.readLine()) != null ) {
						if(line.equals("```") || line.equals("----")){
							break;
						}
						if(isFigure){
							if(line.startsWith("render(")){
								preprocessOut.append(prompt).append(line).append("\n");
								line = makeRenderSave(line, height, width, file);
							}
						} else {
							preprocessOut.append(prompt).append(line).append("\n");
							String continuationLine = "";
							while(!executor.isStatementComplete(line)){
								 if((continuationLine = reader.readLine()) != null){ 
								     if(continuationLine.equals("```") || continuationLine.equals("----")){
								    	 moreShellInput = false;
								    	 break;
								     }
									 preprocessOut.append(continuation).append(continuationLine).append("\n");
								 } else {
									 break;
								 }
								 line += "\n" + continuationLine;
							}
							line += "\n";
						}
						String resultOutput = "";
						System.err.println(line);
						try {
//							if(!isFigure){
								resultOutput = executor.evalPrint(line);
//							}
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
							executor.error("* " + name + ":");
							executor.error(messages.trim());
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
					preprocessOut.append(onthology.genSubToc(name, depth, true, details));
				} else if(line.contains("image:")){
					Pattern p = Pattern.compile("(^.*)(image::?)([^\\[]+)(\\[.*$)");
					Matcher m = p.matcher(line);
					if(m.find()){
						String pre = m.group(1);
						String image = m.group(2);
						String link = m.group(3);
						String post = m.group(4);
						if(!link.contains("{") && !link.startsWith("/")){
							link = "/{concept}/" + link;
						}
						preprocessOut.append(pre).append(image).append(link).append(post).append("\n");
					} else {
						preprocessOut.append(line).append("\n");
					}
				} else {
					preprocessOut.append(line).append("\n");
				}
			}

			preprocessOut.append(onthology.genDetails(name, details));
		}
		
		Path parent = destPath.resolve(name);
		if(!Files.exists(parent)){
			Files.createDirectory(parent);
		}
		CourseCompiler.writeFile(getADocFileName(), preprocessOut.toString());
	}

}
