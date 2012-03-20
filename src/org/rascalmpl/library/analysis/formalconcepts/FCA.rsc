@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}

module analysis::formalconcepts::FCA

import Set;
import Map;
import Relation;
import util::Dot;

@doc{Data Types belonging to Formal Concept Analysis }
public alias FormalContext[&Object, &Attribute] = rel[&Object, &Attribute];
public alias Concept[&Object, &Attribute] = tuple[set[&Object], set[&Attribute]];
public alias ConceptLattice[&Object, &Attribute] = rel[Concept[&Object], Concept[&Attribute]];

public alias Object2Attributes[&Object, &Attribute] = map[&Object, set[&Attribute]];
public alias Attribute2Objects[&Attribute, &Object] = map[&Attribute, set[&Object]];
                                                     
@doc{Computes Concept Lattice given the Object Attribute Relation }
public ConceptLattice[&Object, &Attribute] fca (FormalContext[&Object, &Attribute] fc) {
    rel[set[&Attribute], set[&Attribute]] lat = createAttributeLattice(fc);
    return {<<tau(fc, c1), c1>, <tau(fc, c2), c2>>|<set[&Attribute] c1, set[&Attribute] c2><-lat};
}

@doc{Computes Dot Graph from Concept Lattice  }
public DotGraph toDot(ConceptLattice[&Object, &Attribute] cl) {
     map[Concept[&Object, &Attribute], int] z = makeNodes(cl);
     set[Concept[&Object, &Attribute]] d = domain(z);
     Stms nodes = [];
     for (Concept[&Object, &Attribute] c <- d) {
       /*
       set[&Object] a0 = newAdded0(q, c);
       set[&Attribute] a1 = newAdded1(q, c);
       &Object s0 = (isEmpty(a0)?"":((size(a0)==1)?getOneFrom(a0):toString(a0)));
       &Attribute s1 = (isEmpty(a1)?"":((size(a1)==1)?getOneFrom(a1):toString(a1)));
       */
       nodes += compose(c, z);
     }  
     Stms edges =   [ E("\"<z[x[0]]>\"", "\"<z[x[1]]>\"") | x<-cl]; 
     return digraph("fca", nodes+edges); 
     }
     
public FormalContext[&Object, &Attribute] toFormalContext(Object2Attributes objects) {
    return {<object, attribute>  | &Object object <- domain(objects), 
            &Attribute attribute <- objects[object]}; 
    }

public FormalContext[&Object, &Attribute] toFormalContext(Attribute2Objects attributes) {
    return {<object, attribute>  | &Attribute attribute <- domain(attributes), 
            &Object object <- attributes[attribute]}; 
    }     
/*---------------------------------------------------------------------------------------------*/

set[&T] intersection(set[set[&T]] st)
{
  set[str] result = isEmpty(st)?{}:getOneFrom(st);
  for(set[str] elm <- st){
    result = result & elm;
  }
  return result;
}

set[&T] union(set[set[&T]] st)
{
  set[&T] result = {};
  for(set[&T] elm <- st){
    result += elm;
  }
  return result;
} 

bool isSubset(set[set[&T]] candidate, set[&T] s ) {
         for (set[&T] c <- candidate) 
         if (s<c) return true;
         return false;
     }   

set[&Attribute] sigma(FormalContext[&Object, &Attribute] fc, set[&Object] objects) {
      return {y|<x,y><-fc, x <- objects};
      }
      
set[&Object] tau(FormalContext[&Object, &Attribute] fc, set[&Attributes] attributes) {
      return {x|<x,y><-fc, y <- attributes};
      }
      
set[set[&T]] maxincl(set[set[&T]] c) {return {s|set[&T] s <- c, !isSubset(c, s)};}

rel[set[&Attribute], set[&Attribute]] createAttributeLattice(FormalContext[&Object, &Attribute] fc) {
     set[&Object] G = domain(fc);
     set[&Attribute] M = range(fc);
     set[set[&Attribute]] layer = {M};
     set[set[&Attribute]] B = {sigma(fc, {g}) | g <- G};
     rel[set[&Attribute], set[&Attribute]] r = {};
     while (!isEmpty(layer)&& layer!={{}}) {
         set[set[&Attribute]] nextLayer = {};
         for (set[&Attribute] m<-layer) {
           set[set[&Attribute]] cover = maxincl({b&m|set[&Attribute] b<-B,  (b&m)<m});
           for (set[&Attribute] cov<-cover) r+= {<m, cov>};
           nextLayer += cover;
           }
         layer = nextLayer;      
         }
     return r;
     }
     
 /*-----------------------------------------------------------------------------------*/ 
    
map[Concept[&Object, &Attribute], int] makeNodes(ConceptLattice[&Object, &Attribute] q) {
     set[Concept[&Object, &Attribute]] c = carrier(q);
     int i = 0;
     map[Concept[&Object, &Attribute], int] r = ();
     for (Concept[&Object, &Attribute] b<-c) {
          if (!(r[b])?) {
              r[b] = i;
              i=i+1;
              }
          }
     return r;
     }
     
set[&Attribute] newAdded1(ConceptLattice[&Object, &Attribute] q,  Concept[&Object, &Attribute] c) {
     set[Concept[&Object, &Attribute]] parents = range(domainR(q, {c}));
     return c[1] - union({p[1]|Concept[&Object, &Attribute] p <-parents});
     }
 
set[&Concept] newAdded0(ConceptLattice[&Object, &Attribute] q, Concept[&Object, &Attribute] c) {
     set[concept_t] parents = domain(rangeR(q, {c}));
     return c[0] - union({p[0]|Concept[&Object, &Attribute] p <-parents});
     }  

Stm compose(Concept[&Object, &Attribute] c, map[Concept[&Object, &Attribute], int] z) {
     return N("\"<z[c]>\"", [<"style","filled">, <"fillcolor","cornsilk">]);
     }     
