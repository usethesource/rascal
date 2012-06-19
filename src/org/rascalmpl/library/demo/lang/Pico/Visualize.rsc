module demo::lang::Pico::Visualize

import Prelude;
import vis::Figure;

import demo::lang::Pico::Abstract;
import demo::lang::Pico::ControlFlow;

str make(natCon(int N)) = "<N>";
str make(strCon(str S)) = S;
str make(demo::lang::Pico::Abstract::id(PicoId Id)) = Id;
str make(add(EXP E1, EXP E2)) = "<make(E1)> + <make(E2)>";
str make(sub(EXP E1, EXP E2)) = "<make(E1)> - <make(E2)>";
str make(conc(EXP E1, EXP E2)) = "<make(E1)> || <make(E2)>";

Fproperty editIt(CFNode n) =
   (n has location) ? onClick(void () { edit(n.location,[]);})
                    : onClick(void () {;});
        
      
Figure visNode(n:entry(loc location)) = box(text("ENTRY"), id(getId(n)), fillColor("red"), gap(4));
Figure visNode(n:exit()) = box(text("EXIT"), id(getId(n)), fillColor("grey"), gap(4));
Figure visNode(n:choice(loc location, EXP exp)) = ellipse(text(make(exp)), id(getId(n)), fillColor("yellow"), gap(8), editIt(n));
Figure visNode(n:statement(loc location, asgStat(PicoId Id, EXP Exp))) = box(text("<Id> := <make(Exp)>"), id(getId(n)), gap(8), editIt(n));

str getId(entry(loc location)) = "ENTRY";
str getId(exit()) = "EXIT";
default str getId(CFNode n) = "<n.location>";

public Figure visCFG(rel[CFNode, CFNode] CFG){
       nodeSet = {};
       edges = [];
       for(< CFNode cf1, CFNode cf2> <- CFG){
           nodeSet += {cf1, cf2};
           edges += edge(getId(cf1), getId(cf2), toArrow(triangle(5, fillColor("black"))));
       }
       nodes = [visNode(n) | n <- nodeSet];
       return graph(nodes, edges, hint("layered"), gap(20));
}
