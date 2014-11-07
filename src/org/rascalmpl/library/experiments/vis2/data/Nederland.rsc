module experiments::vis2::\data::Nederland

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 

import Prelude;
import util::Math;
import lang::csv::IO;


loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/Nederland.csv|;

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
alias record = lrel[str year, 
         num p1900, num p1910,  num p1920, num p1930, num p1940,
         num p1950, num p1960, num p1970, num p1980, num p1990, num p2000,
         num p2010];
         
public map[str, lrel[str, num]] exampleNederland() {
   record v = 
      readCSV(#record, location, header=true);  
   map[str, lrel[str, num]] r =
   ("p1900": [<q,  getOneFrom(v[q])[0]>|q<-v<0>],
    "p1910": [<q,  getOneFrom(v[q])[1]>|q<-v<0>],
    "p1920": [<q,  getOneFrom(v[q])[2]>|q<-v<0>],
    "p1930": [<q,  getOneFrom(v[q])[3]>|q<-v<0>],
    "p1940": [<q,  getOneFrom(v[q])[4]>|q<-v<0>],
    "p1950": [<q,  getOneFrom(v[q])[5]>|q<-v<0>],
    "p1960": [<q,  getOneFrom(v[q])[6]>|q<-v<0>],
    "p1970": [<q,  getOneFrom(v[q])[7]>|q<-v<0>],
    "p1980": [<q,  getOneFrom(v[q])[8]>|q<-v<0>],
    "p1990": [<q,  getOneFrom(v[q])[9]>|q<-v<0>],
    "p2000": [<q,  getOneFrom(v[q])[10]>|q<-v<0>],
    "p2010": [<q,  getOneFrom(v[q])[11]>|q<-v<0>]
   );
   return r;
   }

public void main() {   
   println(exampleNederland());
   }