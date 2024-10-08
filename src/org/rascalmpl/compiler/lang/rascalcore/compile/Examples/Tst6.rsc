module lang::rascalcore::compile::Examples::Tst6

import Grammar;

import util::Monitor;
  
data Symbol(int id = 0, str prefix = "");

public str newGenerate(str _package, str _name, Grammar gr) { 
    return job("Generating parser", str (void (str m, int w) _worked) { 
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };

    Production rewrite(Production p) = 
      visit (p) { 
        case Symbol s => s[id=newItem()] 
      };
    beforeUniqueGr = gr;   
    gr.rules = (s : rewrite(gr.rules[s]) | s <- gr.rules);
    
    return "";
    }, totalWork=9);      
}  
