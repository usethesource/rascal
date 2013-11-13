module experiments::Compiler::Benchmarks::BDotVis

import Exception;
import vis::Figure;
//import vis::Render; 
import Set;
import IO;
import String;
import List;
import Map;

import experiments::Compiler::Benchmarks::dot::Dot;
import experiments::Compiler::Benchmarks::fca::CXTIO;
import experiments::Compiler::Benchmarks::fca::FCA;


Stms getStms(DotGraph g) {
  if (digraph(_, Stms stms):=g) {
      return stms;
      }
  return [];
}

list[FProperty] getFillColor(Attrs attrs) {
    return [fillColor(s)| <"fillcolor", s> <-attrs];
    }
    
list[FProperty] getFontColor(Attrs attrs) {
    return [std(fontColor(s)) | <"fontcolor", s> <-attrs];
    }
    
list[FProperty] getProps(Attrs attrs) {
    return getFillColor(attrs)+getFontColor(attrs);
    }

public Figure vertice(Id s, Attrs attrs) {
    list[str] labs = [s| <"label", s> <-attrs];
    str lab = (isEmpty(labs))?s:labs[0];
    list[str] r = [w | <"shape", w> <-attrs];
    println("Attrs: <attrs>");
    if (!isEmpty(r)) {
      switch(r[0]) {
       case "ellipse": return shapeEllipse(text(substr(lab)), getProps(attrs)+id(s));
       case "diamond": return shapeDiamond(text(substr(lab)), getProps(attrs)+id(s));
       }
    }
    return shapeBox(text(substr(lab)), getProps(attrs)+id(s));
    }

Figure vertice(Id s) { 
    return vertice(s, []);
    }

str substr(str s) {
   return substring(s,1, size(s)-1);
   }
   
Attrs getNodeAttributes(Stms stms) {
   Attrs a = [*attrs|NODE(Attrs attrs)<-stms];
   return a; 
   }
   
Attrs getEdgeAttributes(Stms stms) {
   Attrs a = [*attrs|NODE(Attrs attrs)<-stms];
   return a; 
   }

list[Figure] getNodes(Stms stms, Attrs nodeAttrs) {
   map[str, Figure] m = 
     (from:
   vertice(from, nodeAttrs)| Stm stm<-stms, (E(Id from, Id _):=stm || E(Id from, NodeId _):=stm || E(Id from, Stm _):=stm))
   + (to:
   vertice(to, nodeAttrs)| Stm stm<-stms, (E(Id _, Id to):=stm || E(NodeId _, Id to):=stm || E(Stm _, Id to):=stm))   
   + (s:
   vertice(s, nodeAttrs+attrs) | N(Id s, attrs)<-stms)
   + (s:
   vertice(s, nodeAttrs)| N(Id s)<-stms)
   ;
   return toList(range(m));
}

list[Edge] getEdges(Stms stms, Attrs edgeAttrs) {   
    return [edge(from, to, getProps(edgeAttrs)+toArrow(headNormal(fillColor("red"))))|E(Id from, Id to)<-stms];
}

public Figure dot2fig(DotGraph g) {
    Stms stms = getStms(g);
    Attrs nodeAttrs = getNodeAttributes(stms);
    Attrs edgeAttrs = getEdgeAttributes(stms);
    list[Figure] nodes = getNodes(stms, nodeAttrs);
    list[Edge] edges = getEdges(stms, edgeAttrs);
    return graph(nodes, edges, size(800),vgap(40), hgap(40), hint("layered"));
}
    
public value main(list[value] args) {
     ConceptLattice[str, str] cl = fca(readCxt(|rascal:///experiments/Compiler/Benchmarks/fca/examples/FCxt2.cxt|));
     DotGraph gr = toDot(cl);
     Figure g = dot2fig(gr);
     //render(g);
     return g;
}
