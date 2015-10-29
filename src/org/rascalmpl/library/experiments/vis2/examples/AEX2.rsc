module experiments::vis2::examples::AEX2
import Prelude;
import util::Math;
import experiments::vis2::FigureServer;
import experiments::vis2::Figure; 
import lang::csv::IO;
import lang::json::IO;

alias AexData =  lrel[str date , num open, num high , num low, num close, num volume, num adjusted];
// alias AexData = lrel[value date , value open, value high , value low, value close, value volume, value adjusted];

// loc location = |std://experiments/vis2/data/YAHOO-INDEX_AEX.csv|;
loc location = |project://rascal/src/org/rascalmpl/library/experiments/vis2/data/YAHOO-INDEX_AEX.csv|;

int n = 8;
int m = 10;
GoogleData points = [["n","open","close"]]+[ [i, floor(m*arbReal()), floor(m*arbReal())]|int i<-[0..n]];

void ex(str title, Figure f){
	render(title, f);
}

datetime nextMonth(datetime dt, map[datetime , tuple[num open, num close]] d2v) {
    datetime dt1 = incrementMonths(dt);
    while (!d2v[dt1]?) dt1 = incrementDays(dt1);
    return dt1;
    }
    
datetime firstMonth(int year, int month, map[datetime, tuple[num open, num close]] d2v) {
    datetime dt = createDate(year, month, 1);
    while (!d2v[dt]?) dt = incrementDays(dt);
    // println(dt);
    // println(d2v[dt]);
    return dt;
    }
    
public void aex2() {
     AexData v = readCSV(#AexData, location, header=true); 
     // RawData d = [["date", "open", "close"]]+[[e.date, e.close, e.open]|e<-v, datetime h := parseDate(e.date,  "yyyy-MM-dd"), h.year in [2012, 2013, 2014],
     // printDate(h,"E")=="Mon" ];
     map[datetime d, tuple[num open, num close] v] d2v = (
          h:<e.open, e.close>|e<-v, datetime h := parseDate(e.date,  "yyyy-MM-dd"), h.year in [2012, 2013, 2014, 2015]);
     // list[datetime] idx = sort([h|h<-d2v, h.year in [2012, 2013, 2014],  printDate(h,"E")=="Mon", d2v[incrementDays(h, 4)]?]);
     list[datetime] idx = sort([h|h<-d2v, h.year in [2012, 2013, 2014]]);
     GoogleData d = [["date", "close", "open"]]+
     //      [[printDate(h,"Y-w"), d2v[h].open, d2v[incrementDays(h, 4)].close] |h<-idx]; 
     [*[[printDate(h,"Y/M"), d2v[nextMonth(h, d2v)].close, d2v[h].open]|int i <-[1..13], datetime h :=firstMonth(j, i, d2v)]|j<-[2012, 2013, 2014]];
     // [*[["a" , d2v[nextMonth(h, d2v)].close, d2v[h].open]|int i <-[1..13], datetime h :=firstMonth(j, i, d2v)]|j<-[2012, 2013]];
     ex("aex2", areachart(d,
      options = chartOptions(
            width = 800, height = 400, hAxis = axis(direction=1), pointSize = 1)));
     }