@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module experiments::Concept::Types
import Map;
import Set;
import List;
import String;
import IO;
import Relation;
import vis::Figure;
import vis::Render; 
import analysis::formalconcepts::FCA;

public FProperty tip(str S){ 
	return mouseOver(box(text(S, fontColor("green")), fillColor("lightgrey")));
}

alias property_table = tuple[map[str, set[str]] item, map[str, set[str]]feature ];

alias concept_t = tuple[set[str] item, set[str] feature];

rel[str, str] rinv1( map[str, set[str]] a) {
    return {<x,y>| str y <- domain(a), str x <- a[y]}; 
    }
  
map[str, set[str]]  rinv1(rel[str dom, str ran] input) {
   return ( i : input[i] | i <- input.dom );
}
 
public map[str, set[str]] rinv( map[str, set[str]] a) {
    return rinv1(rinv1(a)); 
    }
      
public set[&T] intersection(set[set[&T]] st)
{
  set[str] result = isEmpty(st)?{}:getOneFrom(st);
  for(set[str] elm <- st){
    result = result & elm;
  }
  return result;
}

public set[&T] union(set[set[&T]] st)
{
  set[&T] result = {};
  for(set[&T] elm <- st){
    result += elm;
  }
  return result;
}
   
set[str] pi(map[str, set[str]] a, set[str] b) {return isEmpty(b)?{a[i]|i<-domain(a)} : intersection({a[i]|str i<- b});}

public set[str] sigma(property_table t, set[str] b) {return pi(t.item, b);}

public set[str] tau(property_table t, set[str] b) {return pi(t.feature, b);}

public concept_t conceptFromItem(property_table t, set[str] b) {
    set[str] q = sigma(t, b);
    return <tau(t, q), q>;
    }
    
public concept_t conceptFromFeature(property_table t, set[str] b) {
    set[str] q = tau(t, b);
    return <q, sigma(t, q)>;
    }
 
bool isSubset(set[set[str]] candidate, set[str] s ) {
         for (set[str] c <- candidate) 
         if (s<c) return true;
         return false;
     }   

bool isConcept(property_table t, concept_t c) {
   return sigma(t, tau(t, c.feature))==c.feature;
   }
   
set[set[str]] maxincl(set[set[str]] c) {return {s |set[str] s <- c, !isSubset(c, s)};}

@doc{Return Attribute lattice.}  
public rel[set[str], set[str]] createAttributeLattice(property_table vb) {
     set[str] G = domain(vb[0]);
     set[str] M = domain(vb[1]);
     set[set[str]] layer = {M};
     set[set[str]] B = {sigma(vb, {g}) | g <- G};
     rel[set[str], set[str]] r = {};
     while (!isEmpty(layer)&& layer!={{}}) {
         set[set[str]] nextLayer = {};
         for (set[str] m<-layer) {
           set[set[str]] cover = maxincl({toSet([b&m])|set[str] b<-B,  (b&m)<m});
           for (set[str] cov<-cover) r+= {<m, cov>};
           nextLayer += cover;
           }
         layer = nextLayer;      
         }
     return r;
     }
 
    
 rel[concept_t, concept_t] createLattice(property_table vb, rel[set[str], set[str]] lat) {
     return {<<tau(vb, c1), c1>, <tau(vb, c2), c2>>|<set[str] c1, set[str] c2><-lat};
     }
 
  @doc{Return Concept Lattice.}     
 public rel[concept_t, concept_t] createLattice(property_table vb) {
     rel[set[str], set[str]] lat = createAttributeLattice(vb);
     return {<<tau(vb, c1), c1>, <tau(vb, c2), c2>>|<set[str] c1, set[str] c2><-lat};
     }
     
str lab(map[concept_t, int] z, concept_t c) {
     set[str] c0 = c[0];
     set[str] c1 = c[1];
     // str r = "n<z[c]>";
     str r = "";
     if (size(c0)==1) r = getOneFrom(c0);
     else if (size(c1)==1) r = getOneFrom(c1);
     // println(r);
     return r;
     }
          
 str baseName(loc s) {
     str r = s.path;
     if (/<q:\w+>\.dot$/:=r) return q;
     return r;
     }       
 
 @doc{Write relation in .dot format}    
 public int writeDot(loc output, rel[concept_t, concept_t] q) {
     map[concept_t, int] z = makeNodes(q);
     str r = "digraph <baseName(output)> {";
     r += "<for (x <- q) {><lab(z,x[0])>-\><lab(z, x[1])>;
     <}>";
     r+="}";
     println(r);
     writeFile(output, r);
     return 0;
     }
