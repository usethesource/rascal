module experiments::vis2::\data::Steden

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 

import Prelude;
import util::Math;
import lang::csv::IO;


loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/Steden.csv|;

//public rel[str name, int p2012, int p2013, int ext] v = readCSV(#rel[str name, int p2012, int p2013, int ext],
// location);
/*
public map[str, lrel[str, int, str]] exampleVegaBarData() {
   rel[str name, int p2012, int p2013, int ext] v = readCSV(#rel[str name, int p2012, int p2013, int ext], location, header=true);
   map[str, lrel[str, int, str]] r = ("Population":[<q, getOneFrom(v[q])[0], "2012">|q<-v.name]
     + [<q, getOneFrom(v[q])[1], "2013">|q<-v.name]);
   return r;
   }
*/
public map[str, lrel[str, int]] exampleSteden() {
   lrel[str, int, int , int] v = 
      sort(readCSV(#lrel[str name, int p2012, int p2013, int ext], location, header=true), 
        bool(tuple[str name, int v1, int v2, int v3]  a,  tuple[str name, int v1, int v2, int v3] b){ return a.name < b.name; }
      );  
   map[str, lrel[str, int]] r = ("P2013": [<q,  getOneFrom(v[q])[0]>|q<-domain(v)],
   "P2012": [<q,  getOneFrom(v[q])[1]>|q<-domain(v)]);
   return r;
   }

public void main() {   
   println(exampleSteden());
   }