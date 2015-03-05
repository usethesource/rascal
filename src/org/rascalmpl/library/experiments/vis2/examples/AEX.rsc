module experiments::vis2::examples::AEX


import Prelude;

import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 
import lang::csv::IO;

loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/AEX.csv|;

list[str] days = ["tue", "mon", "fri", "thu", "wed"];
void ex(str title, Figure f){
	render(title, f);
}
alias Trade = lrel[str date , num open, num high , num low, num close, num volume, num adjusted];


alias Header = lrel[value date , value open, value high , value low, value close, value volume, value adjusted];

BoxHeader t(Header r) {
    if (<str date , str open , str high, str low , str close , str volume, str adjusted>  := r[0]) {
          return <date, low,  open, close, high>;
          }
    return <"","","","","","">;
    }
    
public void aex() {
   Header w = readCSV(#Header, location, header=false);
   Trade v = readCSV(#Trade, location, header=true);  
   BoxLabeledData d = [<days[i%5], v[i].low, v[i].open, v[i].close, v[i].high
    ,"open: <v[i].open>\nclose: <v[i].close>\nlow:\t<v[i].low>\nhigh:\t<v[i].high>"
    >|i<-[0..size(v)]];
   ex("aex", candlestickchart(d, t(w)
   , options = chartOptions(candlestick=candlestick(risingColor=candlestickColor(fill="green")
   , fallingColor=candlestickColor(fill="red")))
   , width = 400, height = 400));
   }