

@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::vis2::sandbox::ParseTree

// Visualization of ParseTrees

import lang::rascal::format::Grammar;
import lang::rascal::format::Escape;
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::FigureServer;

import ParseTree;
import IO;
import String;
import ValueIO;
import Set;
import Prelude;

public void renderParsetree(Tree t){
    render(space(visParsetree(t),std(gap(4,15)),std(resizable(false))));
}

public Figure visParsetree(Tree t){
  switch(t){
  
   case b:appl(Production prod, list[Tree] args) : {
        if (skipped() == prod) {
            return box(text("skipped"),size = <5,5>,tooltip = unparse(b));
        }
        if(\lex(_) := prod.def){
            return box(text(unparse(b)),size = <5,5>);
        }
        if(prod.def has string){
            return box(text(prod.def.string),size = <5,5>);
        }
        if(\layouts(_) := prod.def){
            return box(size = <5,5>,fillColor="grey",tooltip=unparse(b));
        }
         //Event p = popup(topProd2rascal(prod));
         return tree(ellipse(size = <5,5>,tooltip = topProd2rascal(prod)),
                     [visParsetree(c) | c <- args]);
     }
     
     case amb(set[Tree] alternatives):{
         //Event p = popup("Ambiguous: <size = <alternatives)>");
        // viewTrees(root, toList(alternatives));
         return tree(ellipse(size = <10,10>, fillColor="red", tooltip = "Ambiguous: <size(alternatives)>"),
                     [visParsetree(c) | c <- alternatives]);
      }
     
    case char(int c) : {
        return  box(text(escape(stringChar(c)), fontColor("blue")));
    }

    case cycle(Symbol symbol, int cycleLength) : {
         //Event p = popup("Cycle-<cycleLength> of <symbol>");
         return tree(ellipse(size = <10,10>, fillColor="yellow", tooltip = "Cycle-<cycleLength> of <symbol>"), []);
    }
  }
  throw "viewTree1: missing case for: <t>";
}

//private Event popup(str s){
//    return tooltip(s); //mouseOver(box(text(s), grow(1.2), resizable(false),fillColor("yellow")));
//}

private bool allChars(list[Tree] trees){
  return all(char(_) <- trees);
}

private str getChars(list[Tree] trees){
  list[int] chars = [ c | t <- trees, char(int c) := t];
  return stringChars(chars);
}

//public FProperty popup(str s){
//return mouseOver(box(text(s), grow(1.2), resizable(false), fillColor("yellow")));
//}

public void tree2(){
   render(tree(ellipse(size=<60,60>, fillColor="green", tooltip = "Ellipse A"),
       [ ellipse(size = <90,90>, fillColor="red", tooltip = "Ellipse B"),
         ellipse(size = <120,120>, fillColor="blue", tooltip = "Ellipse C"),
         ellipse(size = <150,150>, fillColor="purple", tooltip = "Ellipse D"),
         ellipse(size = <180,180>, fillColor="lightblue", tooltip = "Ellipse E"),
         box(size = <60,60>, fillColor="orange", tooltip = "Box F"),
         box(size = <60,60>, fillColor="brown", tooltip = "Box G"),
         box(size = <60,60>, fillColor="black", tooltip = "Box H"),
         box(size = <60,60>, fillColor="grey", tooltip = "Box I"),
         ellipse(size = <60,60>, fillColor="white", tooltip = "Ellipse J")
       ],  gap=<30,30>,  lineWidth=2, fillColor="yellow", cityblock=true)); 
}


