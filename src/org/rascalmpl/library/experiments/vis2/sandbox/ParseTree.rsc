

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

Figure tip(str s) = // box(visibility="hidden", fillColor="green", fig=
      svg(grid(figArray=[
     [box(size=<10, 10>, fillColor="beige")
     ,box(size=<10, 15>, fillColor="antiquewhite")
     ]
     ,
     [box(size=<15, 10>, fillColor="pink")
     ,box(size=<10, 10>, fillColor="yellow")
     ]
     ]))
     // )
     ;
     

     

public void tree2(){
   render(tree(ellipse(size=<60,60>, fillColor="green", tooltip = "Ellipse A"),
       [ ellipse(size = <90,90>, id="aap", fillColor="red", tooltip = circle(r=20, fillColor="green"
          ,fig = emptyFigure())
          , event = on(["click"],  void(str e, str n, str v) {println("H");})
          ),
         ellipse(size = <120,120>, fillColor="blue", tooltip = tip("Ellipse C")),
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

Figure c(int r, str color) = circle(r=r, fillColor = color);
Figure qq1() = svg(tree(c(10,"red"),[c(20, "blue"), c(25, "green")], lineWidth = 1, lineColor = "black"
    ));
Figure qq2() = svg(tree(c(30,"magenta"),[c(10, "beige"), tree(c(25, "gold"), [box(size=<20, 20>, fillColor = "yellow")])]
, lineWidth = 1, lineColor = "black"
    ));

public Figure tst() { 
    Figure r = hcat(figs = [
        box(size=<150, 150> , fillColor = "antiquewhite", id = "aap"
        ,tooltip = qq1()      
             )
      
    
       ,
       box(size=<200, 200> , fillColor = "blue", id = "noot"
         ,tooltip = qq2()
          // ,event = on("mouseenter", void(str e, str n, str v) { println(e);})
        ) 
       ]);
    return r;
    }
    
public void ftst(loc l) { 
   writeFile(l, toHtmlString(tst()));
   }

public void ttst() = render(tst());

public Figure simple() =  box(size=<100, 100>, fillColor = "antiquewhite",
     tooltip = box(size=<100, 100>, fig = circle(r=30, fillColor = "red")));
     
public void tsimple() = render(simple());

void main() {
    Program p = parse(#Program, input);
    renderParsetree(p);
    // render(box(size=<100, 100>, fig=text("aap\<tspan x = \"5\"  y=\"10\"\> noot\</tspan\>")));
    }
