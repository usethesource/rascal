package org.rascalmpl.library.lang.rascal.docs;

import java.io.*;
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
	private Path libSrcPath;
	private String toc;
	private long timestamp;

	public Concept(Path name, String text, Path destPath, Path libSrcPath, long timestamp) {
		this.timestamp = timestamp;
		this.name = name;
		this.text = text;
		this.destPath = destPath;
		this.libSrcPath = libSrcPath;
		title = extract(titlePat);
		synopsis = extractSynopsis();
		index = extractIndex();
	}

	public void setRemote(String toc) {
		this.toc = toc;
		remote = true;
	}

	public void setQuestions() {
	}

	public Path getName() {
		return name;
	}

	public String getTitle() {
		return title == null ? getName().toString() : title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public String getText() {
		return text;
	}

	public String getIndex() {
		return index;
	}

	Pattern titlePat = Pattern.compile("(?ms)^# (.*?)$");
	Pattern indexPat = Pattern.compile("(?ms)^\\.Index$\\n(.*?)$");
	Pattern synopsisPat = Pattern.compile("(?ms)^\\.Synopsis$\\n(.*?)$");

	private String extractSynopsis() {
		String s = extract(synopsisPat);
		return s.isEmpty() ? "" : (s.endsWith(".") ? s : (s + "."));
	}

	private String extractIndex() {
		String s = extract(indexPat);
		if (!s.isEmpty()) {
			Matcher matcher = indexPat.matcher(text);
			text = matcher.replaceFirst("");
		}
		return s;
	}

	private String extract(Pattern pat) {
		Matcher matcher = pat.matcher(text);

		return matcher.find() ? matcher.group(1).trim() : "";
	}

	private String getConceptBaseName() {
		return name.getFileName().toString();
	}

	public String getAnchor() {
		int n = name.getNameCount();
		return n >= 2 ? name.getName(n - 2) + "-" + name.getName(n - 1) : name.getFileName().toString();
	}

	private String getADocFileName() {
		return destPath.toString() + "/" + name + ("/" + getConceptBaseName()) + ".adoc";
	}

	private String getADocFileFolder() {
		return destPath.toString() + "/" + name;
	}

	public String genInclude() {
		String baseName = getConceptBaseName();
		return "include::" + baseName + "/" + baseName + ".adoc" + "[" + baseName + "]\n";
	}

	String commonDefs =
		":icons:        font\n" +
		":iconfont-remote!:\n" +
		//":iconfont-name: font-awesome.min\n" +
		":images:       ../images/\n" +
		":table-caption!:\n" +
		":prewrap!:\n" +
		":docinfo1:\n" +
		":experimental:\n";

	private String makeRed(String result) {
		// this is tricky since # syntax is parsed using a line-by-line tokenizer.
		// there are many many many corner cases where this might go wrong.
		// the nbsp is there to normalize these case a bit, such that # is never directly
		// after a \r or \n character to break the asciidoctor parser.
		return "[error]#" + result + "&nbsp;#\n";
	}

	public void preprocess(Onthology onthology, TutorCommandExecutor repl) throws IOException {
		assert onthology != null && repl != null;
		File adocOut = new File(getADocFileName());

		if (adocOut.exists() && adocOut.lastModified() > timestamp) {
			return; // we don't have to do the work if the source hasn't changed.
		}

		StringWriter preprocessOut = new StringWriter();
		String line = null;
		String[] details = new String[0];
		int level = Onthology.level(name);
		BufferedReader reader = new BufferedReader(new StringReader(text));

		while ((line = reader.readLine()) != null && !line.startsWith("#")) {
			preprocessOut.append(line).append("\n");
		}

		if (line == null) {
			preprocessOut.append("# ").append(name.toString()).append("\n");
		} else {
			title = line.substring(2).trim();
			if (level > 0) {
				preprocessOut.append("\n[[").append(getAnchor()).append("]]\n");
				preprocessOut.append(line).append("\n");
			} else {
				line = line.replaceFirst("#", "=");
				preprocessOut.append(line).append("\n");
				preprocessOut.append(commonDefs);
				preprocessOut.append(":keywords: ").append(index).append("\n");
				preprocessOut.append(":LibDir: ").append(libSrcPath.toString()).append("/\n");
			}

			preprocessOut.append(":concept: ").append(name.toString()).append("\n");

			while ((line = reader.readLine()) != null) {
				if (line.startsWith(".Details")) {
					details = registerDetails(reader);
				}
				//				else if(line.startsWith("```rascal-shell") || line.startsWith("[source,rascal-shell")) {
				//				    executeRascalShellScript(repl, reader, preprocessOut, line);
				//				}
				else if (line.startsWith("```") || line.startsWith("[source")) {
					preprocessCodeBlock(reader, preprocessOut, line);
				} else if (line.startsWith("loctoc::[")) {
					generateSubTableOfContents(onthology, preprocessOut, line, details);
				} else if (line.contains("image:")) {
					preprocessImage(preprocessOut, line);
				} else {
					preprocessOut.append(line).append("\n");
				}
			}

			preprocessOut.append(onthology.genDetails(name, details));
		}

		Path parent = destPath.resolve(name);
		if (!Files.exists(parent)) {
			Files.createDirectory(parent);
		}
		CourseCompiler.writeFile(getADocFileName(), preprocessOut.toString());
	}

	private void executeRascalShellScript(TutorCommandExecutor repl,
										  BufferedReader reader,
										  StringWriter preprocessOut,
										  String line) throws IOException {
		boolean isContinue = line.contains("continue");
		boolean mayHaveErrors = line.contains("error");

		if (line.startsWith("[")) {
			line = reader.readLine();   // skip ----
			if (line == null) {
				// missing ---- ? what to do?
				return;
			}
		}

		if (!isContinue) {
			repl.reset();
		}

		startREPL(preprocessOut, mayHaveErrors);
		boolean printWarning = false;

		OUTER:
		while ((line = reader.readLine()) != null) {
			if (line.equals("```") || line.equals("----")) {
				break;
			}

			if (line.trim().startsWith("//")) {
				endREPL(preprocessOut);
				preprocessOut.append(line.trim().substring(2).trim() + "\n");
				while ((line = reader.readLine()) != null && line.trim().startsWith("//")) {
					preprocessOut.append(line.trim().substring(2).trim() + "\n");
					if (line.equals("```") || line.equals("----")) {
						break OUTER;
					}
				}
				startREPL(preprocessOut, mayHaveErrors);
			}

			preprocessOut.append(repl.getPrompt()).append(escapeForADOC(line)).append("\n");

			String resultOutput = escapeForADOC(repl.eval(line, getADocFileFolder()));
			String htmlOutput = repl.getHTMLOutput();
			String errorOutput = escapeForADOC(repl.getErrorOutput());
			String printedOutput = escapeForADOC(repl.getPrintedOutput());

			if (!printedOutput.isEmpty()) {
				preprocessOut.append(printedOutput);
			}

			if (!errorOutput.isEmpty()) {
				if (!mayHaveErrors) {
					printWarning = true;
				}
				preprocessOut.append(mayHaveErrors ? makeRed(errorOutput) : errorOutput);
			}

			if (!htmlOutput.isEmpty()) {
				endREPL(preprocessOut);
				preprocessOut.append("[example]\n====\n++++\n");
				preprocessOut.append(htmlOutput);
				preprocessOut.append("\n++++\n====\n");
				startREPL(preprocessOut, mayHaveErrors);
			}

			if (!resultOutput.isEmpty()) {
				preprocessOut.append(resultOutput);
			}
		}

		endREPL(preprocessOut);

		if (printWarning) {
			// note that the trailing space after the second # is important for the ADOC parser.
			preprocessOut.append(
				"[error]#WARNING: unexpected errors in the above SHELL example. Documentation author please fix!# ");
		}
	}

	private String[] registerDetails(BufferedReader reader) throws IOException {
		String line;
		String[] details;
		line = reader.readLine();
		details = line.split("\\s");
		return details;
	}

	private void preprocessImage(StringWriter preprocessOut, String line) {
		Pattern p = Pattern.compile("(^.*)(image::?)([^\\[]+)(\\[.*$)");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String pre = m.group(1);
			String image = m.group(2);
			String link = m.group(3);
			String post = m.group(4);
			if (!link.contains("{") && !link.startsWith("/")) {
				link = "/{concept}/" + link;
			}
			preprocessOut.append(pre).append(image).append(link).append(post).append("\n");
		} else {
			preprocessOut.append(line).append("\n");
		}
	}

	private void preprocessCodeBlock(BufferedReader reader, StringWriter preprocessOut, String line)
		throws IOException {
		preprocessOut.append(line).append("\n");
		boolean inCode = false;
		while ((line = reader.readLine()) != null) {
			preprocessOut.append(line).append("\n");
			if (line.equals("```") || line.equals("----")) {
				if (inCode) {
					break;
				} else {
					inCode = true;
				}
			}
		}
	}

	private void generateSubTableOfContents(Onthology onthology, StringWriter preprocessOut, String line,
											String[] details) {
		Pattern p = Pattern.compile("loctoc::\\[(\\d*)\\]");
		Matcher m = p.matcher(line);
		int depth = 1;
		if (m.find()) {
			String intStr = m.group(1);
			depth = intStr.equals("") ? 1 : Integer.parseInt(intStr.substring(0, intStr.length()));
		}
		if (remote) {
			preprocessOut.append(toc);
		} else {
			preprocessOut.append(onthology.genSubToc(name, depth, true, details));
		}
	}

	private String escapeForADOC(String s) {
		StringBuilder out = new StringBuilder(Math.max(16, s.length()));
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 127) {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			} else {
				switch (c) {
					case '\\':
					case '"':
					case '<':
					case '>':
					case '&':
					case '*':
					case '#':
					case '`':
					case '+':
						out.append("&#");
						out.append((int) c);
						out.append(';');
						break;
					default:
						out.append(c);
				}
			}
		}

		return out.toString();
	}

	private void endREPL(StringWriter preprocessOut) {
		preprocessOut.append("----\n");
	}

	private void startREPL(StringWriter preprocessOut, boolean mayHaveErrors) {
		preprocessOut.append("[source,rascal-shell");
		preprocessOut.append(",subs=\"normal\"");
		preprocessOut.append("]\n").append("----\n");
	}

}
