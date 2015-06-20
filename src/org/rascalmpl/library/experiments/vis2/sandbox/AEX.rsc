module experiments::vis2::sandbox::AEX


import Prelude;
import Node;

import experiments::vis2::sandbox::Figure;
import lang::csv::IO;
import lang::json::IO;

// loc location = |std://experiments/vis2/data/AEX.csv|;

loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/AEX.csv|;

void ex(str title, Figure f){
	render(title, f);
}
alias Trade = lrel[str date , num open, num high , num low, num close, num volume, num adjusted];


// alias Header = lrel[value date , value open, value high , value low, value close, value volume, value adjusted];

//BoxHeader t(Header r) {
//    if (<str date , str open , str high, str low , str close , str volume, str adjusted>  := r[0]) {
//          return <date, low,  open, close, high>;
//          }
//    return <"","","","","","">;
//    }
    
public Figure aex() {
   // Header w = readCSV(#Header, location, header=false);
   Trade v = readCSV(#Trade, location, header=true); 
     GoogleData d = [["a","aex","c","d", "e"]]+
         [[printDate(h,"E:\tMM-dd"), e.low, e.open, e.close, e.high]|e<-v  
          , datetime h := parseDate(e.date,  "yyyy-MM-dd")
          ];
   return candlestickchart(googleData = d 
   , options = chartOptions(candlestick=candlestick(
     risingColor= candlestickColor(fill="green")
   , fallingColor=candlestickColor(fill="red")), hAxis = axis(direction=-1))
   , width = 400, height = 400);
   }
   
//map[str, value] adt2map(node t) {
//   map[str, value] r = getKeywordParameters(t);
//   for (d<-r) {
//        if (node n := r[d]) {
//           r[d] = getKeywordParameters(n);
//        }
//      }
//   return r;
//   }
   
//public void main() {
//   println(adt2json((candlestick(
//     risingColor=candlestickColor(fill="green")
//   , fallingColor=candlestickColor()))));
//    }