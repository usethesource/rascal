module demo::lang::Pico::ToDot
import demo::lang::Pico::ControlFlow;
import demo::lang::Pico::Load;
import demo::lang::Pico::Abstract;
import Relation;
import IO;
import Node;
import Set;
import lang::dot::Dot;

public DotGraph toDot(loc input) {
      str a = readFile(input);
      CFGraph c = cflowProgram(a);
      return buildGraph(c);
      }
      
public void toDot(loc input, loc output) {
    writeFile(output, lang::dot::Dot::toString(toDot(input)));
    }
      
map[loc, int] getMap(rel[CFNode,CFNode] c) {
   int i = 0;
   map[loc, int] idx = ();
   list[tuple[CFNode, CFNode]] c1 = toList(c);
   list[CFNode] c2 = [d[0]|d<-c1]+[d[1]|d<-c1];
   for (CFNode d<-c2) { 
      if (exp(EXP e):=d) {  
       loc l = e.origin;
       if (!(idx[l]?)) {
          idx[l] = i;
          i=i+1;
         }
       }
   }
   return idx;
}
      
DotGraph  buildGraph(CFGraph c) {
       rel[CFNode,CFNode] g = c.graph;
       map[loc, int] idx = getMap(g);
       Stms nodes = 
          [NODE( [<"style","filled">, <"fillcolor","cornsilk">,<"fontcolor","black">,<"shape","ellipse">])];
       Stms edges = [];
       list[tuple[CFNode, CFNode]] c1 = toList(g);
       list[CFNode] c2 = [d[0]|d<-c1]+[d[1]|d<-c1];
       for (CFNode d<-c2) { 
       if (exp(EXP e):=d) {
            Attrs attrs = [<"label", "<delAnnotationsRec(e)>">];
            for (q<-c.entry) 
               if (exp(EXP h):=q)
                 if (e.origin == h.origin)  {
                     attrs += <"fillcolor","lightsalmon">;
                     break;
                     }
             for (q<-c.exit) 
               if (exp(EXP h):=q)
                 if (e.origin == h.origin)  {
                     attrs += <"fillcolor","palegreen">;
                     break;
                     }
            nodes+=N("<idx[e.origin]>", attrs);
            }
        }
        for (<CFNode from, CFNode to> <-g) {
           if ((exp(EXP f):=from) && (exp(EXP t):=to)) {
            edges+=E("<idx[f.origin]>", "<idx[t.origin]>"); 
            }
        }
    return digraph("controlflow", nodes+edges);
    }