package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.help;

import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeMap;

public class KeywordHelp {
	 static final TreeMap<String,String[]> keywordHelp;
	  
	  static {
		  keywordHelp = new TreeMap<>();
		  String stat = "Rascal/Statements/";
		  String val = "Rascal/Expressions/Values/";
		  keywordHelp.put("if", 	new String[] {stat + "If/If"});
		  keywordHelp.put("then", 	new String[] {stat + "If/If"});
		  keywordHelp.put("else", 	new String[] {stat + "If/If"});
		  
		  keywordHelp.put("true", 	new String[] {val + "Boolean/Boolean"});
		  keywordHelp.put("false", 	new String[] {val + "Boolean/Boolean"});
		  keywordHelp.put("all", 	new String[] {val + "Boolean/All/All"});
		  keywordHelp.put("any", 	new String[] {val + "Boolean/Any/Any"});
		  
		  /*
		  = "o"
					| "syntax"
					| "keyword"
					| "lexical"
					| "int"
					| "break"
					| "continue"
					| "rat" 
					| "bag" 
					| "num" 
					| "node" 
					| "finally" 
					| "private" 
					| "real" 
					| "list" 
					| "fail" 
					| "filter" 
					| "if" 
					| "tag" 
					| BasicType
					| "extend" 
					| "append" 
					| "rel" 
					| "lrel"
					| "void" 
					| "non-assoc" 
					| "assoc" 
					| "test" 
					| "anno" 
					| "layout" 
					| "data" 
					| "join" 
					| "it" 
					| "bracket" 
					| "in" 
					| "import" 
			 
					| "dynamic" 
					| "solve" 
					| "type" 
					| "try" 
					| "catch" 
					| "notin" 
					| "else" 
					| "insert" 
					| "switch" 
					| "return" 
					| "case" 
					| "while" 
					| "str" 
					| "throws" 
					| "visit" 
					| "tuple" 
					| "for" 
					| "assert" 
					| "loc" 
					| "default" 
					| "map" 
					| "alias" 
					| "module" 
					| "mod"
					| "bool" 
					| "public" 
					| "one" 
					| "throw" 
					| "set" 
					| "start"
					| "datetime" 
					| "value" 
					*/
	  }
	  
	  static boolean containsKey(String key){
		  return keywordHelp.containsKey(key);
	  }
	  
	  static String[] get(String key){
		  return keywordHelp.get(key);
	  }
	  
	  public static void printKeywords(PrintWriter stdout) {
		  Set<String> keys = keywordHelp.keySet();
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
