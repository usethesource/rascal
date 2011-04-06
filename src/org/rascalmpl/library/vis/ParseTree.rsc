@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::ParseTree

// Visualization of ParseTrees

import vis::Figure;
import vis::Render;

import ParseTree;
import IO;
import String;
import ValueIO;
import Set;

private int idGen = 0;

private list[Figure] nodes = [];
private list[Edge] edges = [];

private str newId(){
  idGen += 1;
  return "<idGen>";
}

private void reset(){
  idGen = 0;
  nodes = [];
  edges = [];
}

public Figure parsetree(Tree p){
  reset();
  root = viewTree1(p);
  return tree(nodes, edges, gap(4));
}

private FProperty popup(str s){
	return mouseOver(box(text(s), gap(3,1), lineWidth(0), fillColor("yellow")));
}

private str viewTree1(Tree t){
  //println("viewTree1:"); rawPrintln(t);
  switch(t){
  
   case appl(Production prod, list[Tree] args):
    if(\layouts(_) := prod.rhs){
        root = newId();
        nodes += ellipse(size(4), vis::Figure::id(root), fillColor("grey"), popup("LAYOUTLIST"));
        return root;
     } else {
	     FProperty p = popup(viewProduction(prod));
	     root = newId();
	     viewTrees(root, args);
	     nodes += ellipse(vis::Figure::id(root), size(4), p);
	     return root;
     }
     
     case amb(set[Tree] alternatives):{
         FProperty p = popup("Ambiguous");
         root = newId();
         viewTrees(root, toList(alternatives));
         nodes += ellipse(vis::Figure::id(root), size(10), fillColor("red"), p);
	     return root; 
      }
     
    case char(int c) : {
        root = newId();
        nodes += box(text(escape(stringChar(c))), vis::Figure::id(root), gap(1), fontColor("blue"));
        return root;
    }
    
    case str s: {
      root = newId();
      nodes += box([id(root), gap(1), fontColor("blue")], text(s));
    }
  }
  throw "viewTree1: missing case for: <t>";
}

private bool allChars(list[Tree] trees){
  return all(char(_) <- trees);
}

private str escape(str input){
  return 
    visit(input){
      case /^\</ => "\\\<"
      case /^\>/ => "\\\>"
      case /^"/  => "\\\""
      case /^'/  => "\\\'"
      case /^\\/ => "\\\\"
      case /^ /  => "\\ "
      case /^\t/ => "\\t"
      case /^\n/ => "\\n"
      case /^\r/ => "\\r"
    };
}

private str getChars(list[Tree] trees){
  chars = [ c | t <- trees, char(int c) := t];
  return stringChars(chars);
}

private void viewTrees(str root, list[Tree] trees){
  if(allChars(trees)){
    this = newId();
    chars = getChars(trees);
    nodes += box(text(chars), vis::Figure::id(this), gap(1), fontColor("blue"));
    edges += edge(root, this);
  } else {
    for(a <- trees)
	  edges += edge(root, viewTree1(a));
  }
}

private str viewProduction(Production p){
  //println("viewProduction:"); rawPrintln(p);
  switch(p){
    case prod(list[Symbol] lhs, Symbol rhs, Attributes attributes):
       return "<for(s <- lhs){><viewSymbol(s)> <}> -\> <viewSymbol(rhs)>";
    case \regular(Symbol s, Attributes attributes): return viewSymbol(s);
  }
  throw "viewProduction: missing case for: <p>"; 
}

private str viewSymbol(Symbol sym){
  //println("viewSymbol(<sym>)");
  switch(sym){
    case \start(Symbol s): return "start(<viewSymbol(s)>)";
    case \label(str name, Symbol s): return "<name>:<viewSymbol(s)>";
    case \lit(str s) : return "\"<s>\"";
    case \cilit(str s) : return "\"<s>\"";
    case \opt(Symbol s): return viewSymbol(s) + "?";
    case \sort(str s): return s;
    case \layout(): return "LAYOUT";
    case \layouts(str s): return s;
    case \iter(Symbol s): return viewSymbol(s) + "+";
    case \iter-star(Symbol s): return viewSymbol(s) + "*";
    case \iter-seps(Symbol s, list[Symbol] seps): 
		return "{<viewSymbol(s)> <for(sep <- seps){><viewSymbol(sep)><}>}+";
 
    case \iter-star-seps(Symbol s, list[Symbol] seps): 
		return "{<viewSymbol(s)> <for(sep <- seps){><viewSymbol(sep)><}>}*";
 	case \parameterized-sort(str sort, list[Symbol] parameters):
 		return "<viewSymbol(s)>[<for(sep <- seps){><viewSymbol(sep)><}>]";
    case \parameter(str name): return "param: <name>";
    case \char-class(list[CharRange] ranges): return "[<for(r <- ranges){><viewCharRange(r)><}>]";
    case \at-column(int column): return "@<column>";
    case \start-of-line(): return "^";
    case \end-of-line(): return "$";
  }
  throw "viewSymbol: missing case for: <sym>";
}

private str viewCharRange(CharRange crange){
  switch(crange){
    case single(int c): return escape(stringChar(c));
    case range(int start, int end): return escape(stringChar(start)) + "-" + escape(stringChar(end));
  }
}


