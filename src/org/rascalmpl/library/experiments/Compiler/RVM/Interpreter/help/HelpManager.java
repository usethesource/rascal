package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.commonmark.CommonMarkRenderer;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.highlighter.RascalHighlighter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.UndeclaredFieldException;
import org.rascalmpl.value.io.BinaryValueReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class HelpManager {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	private IValueFactory vf;
	private ISourceLocation StdLibInfo;
	private ISet declarationInfo = null;
	private static final int FILE_BUFFER_SIZE = 8 * 1024;
	private Parser cmparser;
	private CommonMarkRenderer cmrenderer;

	public HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;
		vf = ValueFactoryFactory.getValueFactory();
		
		cmparser = Parser.builder().build();
		
		RascalHighlighter highlighter = new RascalHighlighter()
				.setKeywordMarkup(Ansi.ansi().reset().bold().toString(), 
							      Ansi.ansi().reset().boldOff().toString())
				.setCommentMarkup(Ansi.ansi().reset().fg(Ansi.Color.GREEN).toString(), 
								  Ansi.ansi().reset().fg(Ansi.Color.BLACK).toString());
		cmrenderer =  CommonMarkRenderer.builder().setHighlighter(highlighter)
				.setEmphasisMarkup(Ansi.ansi().a(Attribute.ITALIC).toString(), Ansi.ansi().a(Attribute.ITALIC_OFF).toString())
				.setStrongMarkup(Ansi.ansi().a(Attribute.INTENSITY_BOLD).toString(), Ansi.ansi().a(Attribute.INTENSITY_BOLD_OFF).toString())
				.build();
	}
	
	public void printHelp(String[] words){
		//TODO Add here for example credits, copyright, license
		
		readDeclarationInfo();
		
		if(words.length == 1){
			IntroHelp.print(stdout);
			return;  
		}
		if(words[1].equals("keywords")){
			KeywordHelp.printKeywords(stdout);
			return;
		}
		if(KeywordHelp.containsKey(words[1])){
			printKeywordHelp(words[1]);
			return;
		}
		
		if(words[1].equals("operators")){
			OperatorHelp.printOperators(stdout);
			return;
		}
		
		if(OperatorHelp.containsKey(words[1])){
			printOperatorHelp(words);
			return;
		}
		stdout.print(infoFunction(words[1]));
	}
	
	private void readDeclarationInfo(){
		if(declarationInfo == null){
			TypeStore store = new TypeStore();
			Type start = TypeFactory.getInstance().valueType();
			try {
				StdLibInfo = vf.sourceLocation("compressed+boot", "", "stdlib/StdLib.info.gz");
			} catch (URISyntaxException e) {
				stderr.println("Cannot create location for help info");
			} 
			
			try (InputStream in = URIResolverRegistry.getInstance().getInputStream(StdLibInfo)) {
				declarationInfo = (ISet) new BinaryValueReader().read(vf, store, start, in);
			} catch (IOException e) {
				stderr.println("Cannot read help info");
			}
		}
	}
	
	private String consumeInputStream(Reader in) throws IOException {
		StringBuilder res = new StringBuilder();
		char[] chunk = new char[FILE_BUFFER_SIZE];
		int read;
		while ((read = in.read(chunk, 0, chunk.length)) != -1) {
		    res.append(chunk, 0, read);
		}
		return res.toString();
	}
	
	void printKeywordHelp(String keyword){
		String conceptName = KeywordHelp.get(keyword)[0];
		ISourceLocation conceptLoc;
		try {
			conceptLoc = vf.sourceLocation("courses", "", conceptName + ".concept");
		} catch (URISyntaxException e) {
			stderr.println("Cannot create location for " + conceptName);
			return;
		} 
		try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(conceptLoc);){
			printConcept(consumeInputStream(reader));
		} catch (IOException e) {
			stderr.println("Cannot read: " + conceptLoc);
			return;
		}
	}
	
	void printOperatorHelp(String[] words){
		String op = words[1];
		String[] conceptNames = OperatorHelp.get(op);
		if(words.length == 2 && conceptNames.length > 1){
			stdout.println("Operator " + words[1] + " is overloaded:\n");
			for(String conceptName : conceptNames){
				stdout.println("\t" + conceptName);
			}
			stdout.println("\nType 'help " + words[1] + " word1 word2 ...' to disambiguate, e.g. 'help + num'");
			stdout.println("or   'help " + words[1] + " all' to show all versions");
			return;
		}
		AllNames:
		for(String conceptName : conceptNames){
			if(words.length > 2 && !words[2].equals("all")){
				for(int i = 2; i < words.length; i++){
					if(conceptName.toLowerCase().indexOf(words[i].toLowerCase()) < 0){
						continue AllNames;
					}
				}
			}
			ISourceLocation conceptLoc;
			try {
				conceptLoc = vf.sourceLocation("courses", "", conceptName + ".concept");
			} catch (URISyntaxException e) {
				stderr.println("Cannot create location for " + conceptName);
				return;
			} 
			try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(conceptLoc);){
				printConcept(consumeInputStream(reader));
			} catch (IOException e) {
				stderr.println("Cannot read: " + conceptLoc);
				return;
			}
		}
	}
	
	void printConcept(String concept){
		Node document = cmparser.parse(concept);
		stdout.println(cmrenderer.render(document));
//		String[] lines = concept.split("\n");
//		for(String line : lines){
//			if(line.startsWith("Questions")){
//				break;
//			}
//			stdout.println(line);
//		}
	}
	
	String getField(IConstructor cons, String fieldName){
		try {
			return ((IString)cons.get(fieldName)).getValue();
		}
		catch (UndeclaredFieldException e){
			return "";
		}
	}
	
	void report(IConstructor declInfo, StringWriter w, boolean showDoc, boolean showSource){
		String constructorName = declInfo.getName();
		String role = constructorName;
		switch(constructorName){
		case "functionInfo":    role = "Function:    "; break;
		case "constructorInfo": role = "Constructor: "; break;
		case "dataInfo":        role = "Data:        "; break;
		case "moduleInfo":      role = "Module:      "; break;
		case "varInfo":         role = "Variable:    "; break;
		}

		w.append("Module:       ").append(getField(declInfo, "moduleName")).append("\n")
		 .append(role).append(" ").append(getField(declInfo, "signature")).append("\n");
		if(showDoc){
			w.append(getField(declInfo, "doc"));
		} else {
			String synopsis = getField(declInfo, "synopsis");
			if(!synopsis.isEmpty()){
			   w.append("Synopsis:     ").append(synopsis).append("\n");
		    }
		}
		w.append("\n");
		//'<showSource ? "Source:\n<readFile(di.src)>" : ""
	}
	
	String infoFunction(String name){
		StringWriter w = new StringWriter();
		for(IValue elem : declarationInfo){
			IConstructor declInfo = (IConstructor) elem;
			String consName = declInfo.getName();
			if(consName.equals("moduleInfo")) continue;
			if(getField(declInfo, "name").indexOf(name) >= 0){
				report(declInfo, w, false, false);
			}
		}
		return w.toString();
	}
}
