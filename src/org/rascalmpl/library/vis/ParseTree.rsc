@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::ParseTree

// Visualization of ParseTrees

import lang::rascal::format::Grammar;
import lang::rascal::format::Escape;
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
  
   case appl(Production prod, list[Tree] args) : {
	     FProperty p = popup(topProd2rascal(prod));
	     root = newId();
	     viewTrees(root, args);
	     nodes += ellipse(vis::Figure::id(root), size(4), p);
	     return root;
     }
     
     case amb(set[Tree] alternatives):{
         FProperty p = popup("Ambiguous: <size(alternatives)>");
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


