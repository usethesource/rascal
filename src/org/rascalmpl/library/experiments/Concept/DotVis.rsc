@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module experiments::Concept::DotVis
import lang::dot::Dot;
import vis::Figure;
import vis::Render; 
import Set;
import IO;
import String;
import List;
import Map;
import analysis::formalconcepts::CXTIO;

import analysis::formalconcepts::FCA;


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
    if (!isEmpty(r)) {
      switch(r[0]) {
       case "ellipse": return shapeEllipse(text(substr(lab)), getProps(attrs)+id(s));
       case "diamond": return shapeDiamand(text(substr(lab)), getProps(attrs)+id(s));
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
   vertice(from, nodeAttrs)| E(Id from, _)<-stms)
   + (to:
   vertice(to, nodeAttrs)| E(_, Id to)<-stms)   
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
    // println(nodes);
    list[Edge] edges = getEdges(stms, edgeAttrs);
    // println(edges);
    return graph(nodes, edges, size(800),vgap(40), hgap(40), hint("layered"));
    }
    
public void main() {
     ConceptLattice[str, str] legacy0 = fca(readCxt(|file:///ufs/bertl/cxt/legacy.cxt|));
     DotGraph legacy = toDot(legacy0);
     ConceptLattice[str, str] tealady0 = fca(readCxt(|file:///ufs/bertl/cxt/tealady.cxt|));
     DotGraph tealady = toDot(tealady0);
     ConceptLattice[str, str] liveinwater0 = fca(readCxt(|file:///ufs/bertl/cxt/liveinwater.cxt|));
     DotGraph liveinwater = toDot(liveinwater0);
     ConceptLattice[str, str] gewaesser0 = fca(readCxt(|file:///ufs/bertl/cxt/gewaesser.cxt|));
     DotGraph gewaesser = toDot(gewaesser0);
     Figure g = dot2fig(gewaesser);
     render(g);
     }
