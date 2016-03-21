package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.help;

import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeMap;

public class OperatorHelp {
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
	  static Set<String> keySet() {
		  return operatorHelp.keySet();
	  }
	  
	  static boolean containsKey(String key){
		  return operatorHelp.containsKey(key);
	  }
	  
	  static String[] get(String key){
		  return operatorHelp.get(key);
	  }

	public static void printOperators(PrintWriter stdout) {
		Set<String> keys = operatorHelp.keySet();
		int MAXLINE = 80;
	
		int col = 0;
		for(String key : keys){
			stdout.print(String.format("%1$-" + 10 + "s", key));
			col += 10;
			if(col > MAXLINE){
				col = 0;
				stdout.println();
			}
		}
		stdout.println();
	}
}
