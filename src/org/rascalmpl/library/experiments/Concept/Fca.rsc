@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::Concept::Fca
import IO;
import String;
import List;
import Set;
import Map;
import Relation;
import experiments::Concept::Types;
import vis::Figure;


@doc{Read object attribute in .cxt format.}   
public property_table readCxt(loc input)  {
    list[str] d = readFileLines(input);
    map[str, set[str]] vb1 = ();
    int nRows = toInt(d[2]);
    int nCols = toInt(d[3]);
    int theStart = 5+nRows+nCols;
    list[str] e = tail(d, size(d)-theStart);
    int idx = 5;
    for (str f <- e) {
         set[str] b = {d[5+nRows+i]|int i<-[0, 1..(size(f)-1)], charAt(f,i)==88};
         vb1[d[idx]] = b;
         idx = idx+1;
         }
    return <vb1, rinv(vb1)>;
    }
    
    
    @doc{Create Lattice Figure from Property Table}
 public Figure createLatticeFigure(property_table vb, str layout_alg) {
     int bound = 10;
     rel[concept_t, concept_t] q = createLattice(vb);
     map[concept_t, int] z = makeNodes(q);
     set[concept_t] d = domain(z);
     Figures nodes = [];
     bool big = (size(d)<bound);
     for (concept_t c <- d) {
       set[str] a0 = newAdded0(q, c);
       set[str] a1 = newAdded1(q, c);
       str s0 = (isEmpty(a0)?"":((size(a0)==1)?getOneFrom(a0):toString(a0)));
       str s1 = (isEmpty(a1)?"":((size(a1)==1)?getOneFrom(a1):toString(a1)));
       nodes += compose(s0, s1, c, z, big);
     }  
     list[Edge] edges =   [ edge("<z[x[0]]>", "<z[x[1]]>", [lineWidth(1)]) | x<-q];  
     return graph( nodes, edges, [hint(layout_alg), gap(50)]);
     }
     
// Auxilary functions
   
set[str] newAdded1(rel[concept_t, concept_t] q, concept_t c) {
     set[concept_t] parents = range(domainR(q, {c}));
     return c[1] - union({{{p[1]|concept_t p <-parents}}});
     }
 
 set[str] newAdded0(rel[concept_t, concept_t] q, concept_t c) {
     set[concept_t] parents = domain(rangeR(q, {c}));
     return c[0] - union({{{p[0]|concept_t p <-parents}}});
     }  
     
 map[concept_t, int] makeNodes(rel[concept_t, concept_t] q) {
     set[concept_t] c = carrier(q);
     int i = 0;
     map[concept_t, int] r = ();
     for (concept_t b<-c) {
          if (!(r[b])?) {
              r[b] = i;
              i=i+1;
              }
          }
     return r;
     }
   
 Figure compose(str s1, str s2, concept_t c, map[concept_t, int] z, bool big) {
     int w = 70, h = 20;
     FProperty mOver = mouseOver(box(
      text("<c[0]> <c[1]>", [fontColor("green")]), 
         [fillColor("lightgrey")]));
     list[FProperty] l = [width(w), height(h), fontSize(9), lineColor("black")];
     if (big)
     return vcat([box(text(s1), l+fillColor("lightgrey")),
             box(text(s2), l+fillColor("lightyellow"))],[id("<z[c]>"),mOver]
         );
     else
     return ellipse([width(10), height(10),fillColor("lightyellow"), id("<z[c]>"), mOver]);
     } 
