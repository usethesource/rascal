package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

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
	
	String[] helpText = {
			"Help for the compiler-based RascalShell.",
			"",
			"RascalShell commands:",
			"    quit or EOF            Quit this RascalShell",
			"    help <text>            Help about a specific Rascal, keyword, function or topic",
			"    declarations           List all declarations",
			"    modules                List all imported modules",
			"    undeclare <name>       Remove declaration of <name>",
			"    unimport <name>        Remove import of module <name>",
			"    set <option> <value>   Set RascalShell <option> to <value>",
			"    e.g. set profile true",
			"         set trace false",
			"         set coverage true",
			"",
			"Debugging commands:",
			"    break                  List current break points",
			"    break <name>           Breakpoint at start of function <name>",
			"    break <name> <lino>    Breakpoint in function <name> at line <lino>",
			"    cl(ear) <bpno>         Clear breakpoint with index <bpno>",
			"",
			"Keyboard essentials:",
			"    <UP>                   Previous command in history",
			"    <DOWN>                 Next command in history",
			"    <CTRL>r                Backward search in history",
			"    <CTRL>s                Forward search in history",
			"    <TAB>                  Complete previous word",
			"    <CTRL>a                Move cursor to begin of line",
			"    <CTRL>e                Move cursor to end of line",
			"    <CTRL>k                Kill remainder of line after cursor",
			"    <CTRL>l                Clear screen",
			"",
			"Further help: XXX"
			//":edit <modulename>         Opens an editor for that module",
			//":test                      Runs all unit tests currently loaded",
			//":unimport <modulename>     Undo an import",
			//":undeclare <name>          Undeclares a variable or function introduced in the shell",
			//":history                   Print the command history",
	};
	
	  static final TreeMap<String,String> keywordHelp;
	  
	  static {
		  keywordHelp = new TreeMap<>();
		  String bk = "Rascal/Statements/";
		  keywordHelp.put("if", 	bk + "If/If");
		  keywordHelp.put("then", 	bk + "If/If");
		  keywordHelp.put("else", 	bk + "If/If");
	  }
	  
	  static final TreeMap<String,String[]> operatorHelp;
	  
	  static {
		  operatorHelp = new TreeMap<>();
		  String bo = "Rascal/Expressions/Values/";
		  operatorHelp.put("+",  new String[] {bo + "Number/Addition/Addition",
				  							   bo + "String/Concatenation/Concatenation",
				  							   bo + "List/Concatenation/Concatenation",
				  							   bo + "Tuple/Concatenation/Concatenation",
				  							   bo + "Set/Union/Union",
				  							   bo + "Map/Union/Union",
				  							   bo + "Location/AddSegment/AddSegment",
				  							   
				  							   bo + "Relation/TransitiveClosure/TransitiveClosure",
                                               bo + "ListRelation/TransitiveClosure/TransitiveClosure"
				  							   });
		  
		  operatorHelp.put("/",  new String[] {bo +  "Number/Division/Division",
				  							   "Rascal/Patterns/Abstract/Descendant/Descendant"});
		  
		  operatorHelp.put("{",  new String[] {bo +  "Set/Set",
				  							   "Rascal/Expressions/Comprehensions/Comprehensions",
				  							   "Rascal/Statements/Block/Block"
		  										});
		  operatorHelp.put("}",  new String[] {bo +  "Set/Set",
			   								   "Rascal/Expressions/Comprehensions/Comprehensions",
			   								   "Rascal/Statements/Block/Block"
				  								});
		  }
	  
	HelpManager(PrintWriter stdout, PrintWriter stderr){
		this.stdout = stdout;
		this.stderr = stderr;
		vf = ValueFactoryFactory.getValueFactory();
		TypeStore store = new TypeStore();
		Type start = TypeFactory.getInstance().valueType();
		try {
			StdLibInfo = vf.sourceLocation("compressed+boot", "", "stdlib/StdLib.info.gz");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try (InputStream in = URIResolverRegistry.getInstance().getInputStream(StdLibInfo)) {
			declarationInfo = (ISet) new BinaryValueReader().read(vf, store, start, in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void printHelp(String[] words){
		//TODO Add here for example credits, copyright, license
		
		if(words.length == 1){
			for(String line : helpText){
				stdout.println(line);
			}
			return;  
		}
		if(words[1].equals("keywords")){
			for(String key : keywordHelp.keySet()){
				stdout.println(key);
			}
			return;
		}
		if(keywordHelp.containsKey(words[1])){
			printKeywordHelp(words[1]);
			return;
		}
		if(operatorHelp.containsKey(words[1])){
			printOperatorHelp(words);
			return;
		}
		stdout.print(infoFunction(words[1]));
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
		String conceptName = keywordHelp.get(keyword);
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
		String[] conceptNames = operatorHelp.get(op);
		if(words.length == 2 && conceptNames.length > 1){
			stdout.println("Operator " + words[1] + " is overloaded:");
			for(String conceptName : conceptNames){
				stdout.println("\t" + conceptName);
			}
			stdout.println("Type 'help " + words[1] + " substring' to disambiguate, e.g. 'help + num'");
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
		String[] lines = concept.split("\n");
		for(String line : lines){
			if(line.startsWith("Questions")){
				break;
			}
			stdout.println(line);
		}
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
		case "varInfo":         role =  "Variable:   "; break;
		}

		w.append("Module:       ").append(getField(declInfo, "moduleName")).append("\n")
		 .append(role).append(" ").append(getField(declInfo, "signature")).append("\n");
		if(showDoc){
			w.append(getField(declInfo, "doc"));
		} else {
			w.append("Synopsis:     ").append(getField(declInfo, "synopsis"));
		}
		w.append("\n\n");
		//'<showSource ? "Source:\n<readFile(di.src)>" : "">

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
