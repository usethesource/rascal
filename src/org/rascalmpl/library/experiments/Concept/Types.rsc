module experiments::Concept::Types
import Map;
import Set;
import List;
import String;
import IO;
import Relation;

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
    
@doc{Read object attribute in .cxt format.}   
public property_table readCxt(loc input)  {
    list[str] d = readFileLines(input);
    map[str, set[str]] vb1 = ();
    int nRows = toInt(d[2]);
    int nCols = toInt(d[3]);
    int start = 5+nRows+nCols;
    list[str] e = tail(d, size(d)-start);
    int idx = 5;
    for (str f <- e) {
         set[str] b = {d[5+nRows+i]|int i<-[0, 1..(size(f)-1)], charAt(f,i)==88};
         vb1[d[idx]] = b;
         idx = idx+1;
         }
    return <vb1, rinv(vb1)>;
    }
    
public set[str] intersection(set[set[str]] st)
{
  set[str] result = isEmpty(st)?{}:getOneFrom(st);
  for(set[str] elm <- st){
    result = result & elm;
  }
  return result;
}
   
set[str] pi(map[str, set[str]] a, set[str] b) {return isEmpty(b)?{a[i]|i<-domain(a)}:intersection({{a[i]}|str i<- b});}

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
   
set[set[str]] maxincl(set[set[str]] c) {return {{s}|set[str] s <- c, !isSubset(c, s)};}

@doc{Retrun Attribute lattice.}  
public rel[set[str], set[str]] createAttributeLattice(property_table vb) {
     set[str] G = domain(vb[0]);
     set[str] M = domain(vb[1]);
     set[set[str]] layer = {{M}};
     set[set[str]] B = {{sigma(vb, {g})} | g <- G};
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
     
str lab(map[concept_t, int] z, concept_t c) {
     set[str] c0 = c[0];
     set[str] c1 = c[1];
     set[str] r = {};
     if (size(c0)==1) return getOneFrom(c0);
     if (size(c1)==1) return getOneFrom(c1);
     return "n<z[c]>";
     }
 
 @doc{Write relation in .dot format}    
 public int writeDot(loc output, rel[concept_t, concept_t] q) {
     map[concept_t, int] z = makeNodes(q);
     str r = "digraph G {
     ";
     r += "<for (x <- q) {><lab(z,x[0])>-\><lab(z, x[1])>;
     <}>";
     r+="}";
     println(r);
     writeFile(output, r);
     return 0;
     }