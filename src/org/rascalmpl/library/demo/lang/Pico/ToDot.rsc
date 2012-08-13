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
      CFSEGMENT c = cflowProgram(a);
      return buildGraph(c);
      }
      
public void toDot(loc input, loc output) {
    writeFile(output, toString(toDot(input)));
    }
      
map[loc, int] getMap(rel[CP,CP] c) {
   int i = 0;
   map[loc, int] idx = ();
   list[tuple[CP, CP]] c1 = toList(c);
   list[CP] c2 = [d[0]|d<-c1]+[d[1]|d<-c1];
   for (CP d<-c2) { 
      if (exp(EXP e):=d) {  
       loc l = e@location;
       if (!(idx[l]?)) {
          idx[l] = i;
          i=i+1;
         }
       }
   }
   return idx;
}
      
DotGraph  buildGraph(CFSEGMENT c) {
       rel[CP,CP] g = c.graph;
       map[loc, int] idx = getMap(g);
       Stms nodes = 
          [NODE( [<"style","filled">, <"fillcolor","cornsilk">,<"fontcolor","black">,<"shape","ellipse">])];
       Stms edges = [];
       list[tuple[CP, CP]] c1 = toList(g);
       list[CP] c2 = [d[0]|d<-c1]+[d[1]|d<-c1];
       for (CP d<-c2) { 
       if (exp(EXP e):=d) {
            Attrs attrs = [<"label", "<delAnnotationsRec(e)>">];
            for (q<-c.entry) 
               if (exp(EXP h):=q)
                 if (e@location == h@location)  {
                     attrs += <"fillcolor","lightsalmon">;
                     break;
                     }
             for (q<-c.exit) 
               if (exp(EXP h):=q)
                 if (e@location == h@location)  {
                     attrs += <"fillcolor","palegreen">;
                     break;
                     }
            nodes+=N("<idx[e@location]>", attrs);
            }
        }
        for (<CP from, CP to> <-g) {
           if ((exp(EXP f):=from) && (exp(EXP t):=to)) {
            edges+=E("<idx[f@location]>", "<idx[t@location]>"); 
            }
        }
    return digraph("controlflow", nodes+edges);
    }