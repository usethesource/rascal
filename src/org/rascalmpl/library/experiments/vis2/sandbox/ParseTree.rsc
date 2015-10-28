

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
import demo::lang::Pico::Load;
import demo::lang::Pico::Syntax;

import ParseTree;
import IO;
import String;
import ValueIO;
import Set;
import Prelude;

str input = 
"begin 
     declare input : natural,  
             output : natural,           
             repnr : natural,
             rep : natural;
     input := 14;
     output := 1;
     while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
            output := output + rep;
            repnr := repnr - 1
          od;
          input := input - 1
     od
end"
;


public void renderParsetree(Tree t){
    Figure f = visParsetree(t);
    f.manhattan = true;
    f.ySep = 10;
    f.rasterHeight = 500;
    // println(f);
    render(f);
}


int w  =20;

Figure bx(str s, str color)  = box(size=<10, 10>, tooltip = s, fillColor = color, lineWidth = 0);


public Figure visParsetree(Tree t){
  // println(t);
  switch(t){
  
   case b:appl(Production prod, list[Tree] args) : {
        if (skipped() == prod) {
            return bx("skipped","pink");
        }
        if(\lex(_) := prod.def){
            return bx(unparse(b),"yellow");
        }
        if(prod.def has string){
            return bx(prod.def.string,"yellow");
        }
        if(\layouts(_) := prod.def){
            return bx(unparse(b), "grey");
        }
         return tree(ellipse(size = <w,w>
                     ,tooltip = topProd2rascal(prod)
                      //, tooltip = "A"
                      )
                     ,[visParsetree(c) | c <- args]);
     }
     
     case amb(set[Tree] alternatives):{
         //Event p = popup("Ambiguous: <size = <alternatives)>");
        // viewTrees(root, toList(alternatives));
         return tree(ellipse(size = <w,w>, fillColor="red", tooltip = "Ambiguous: <size(alternatives)>"),
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
         box(size = <60,60>, fillColor="orange", tooltip = "Box
                                                            F"),
         box(size = <60,60>, fillColor="brown", tooltip = "Box\nG"),
         box(size = <60,60>, fillColor="black", tooltip = "Box H"),
         box(size = <60,60>, fillColor="grey", tooltip = "Box I"),
         ellipse(size = <60,60>, fillColor="white", tooltip = "Ellipse J")
       ],  gap=<30,30>,  lineWidth=2, fillColor="antiqueWhite", rasterHeight = 250, manhattan=true, ySep = 30)); 
}

void main() {
    Program p = parse(#Program, input);
    renderParsetree(p);
    // render(box(size=<100, 100>, fig=text("aap\<tspan x = \"5\"  y=\"10\"\> noot\</tspan\>")));
    }
