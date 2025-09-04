module lang::rascalcore::compile::Examples::Intercalate

import util::Benchmark;
import util::Math;
import IO;
import List;
import String;

str intercalate1a(str sep, list[value] l) = 
	(isEmpty(l)) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );

str intercalate1b(str sep, list[value] l) = 
	(isEmpty(l)) ? "" : ( "<l[0]>" | it + "<sep><x>" | x <- l[1..] );

str intercalate2(str sep, list[value] l) = 
  "<for(int i <- index(l)){><i == 0 ? "" : sep><l[i]><}>";

str intercalate3(str sep, list[value] l) {
   str result = "";
   first = true;
   for (v <- l) {
       if (!first) { result += sep; }
       result += "<v>";
       first = false;
   }
   return result;
}

str intercalate4(str sep, list[value] l) { 
  int i = 0;
  int n = size(l);
  return "<while(i < n){><i == 0 ? "" : sep><l[i]>i+=1;<}>";
}


list[str] arbList(int len)
  = [ arbString(20) | int _ <- [0 .. len]];

void main() {
  pairs = [ <arbList(arbInt(200)), arbString(arbInt(30)) > | int _ <- [0 .. 1000] ];

  begin1a = cpuTime();
  for(<lst, sep> <- pairs){
    intercalate1a(sep, lst);
  }
  time1a = cpuTime() - begin1a;

  begin1b = cpuTime();
  for(<lst, sep> <- pairs){
    intercalate1a(sep, lst);
  }
  time1b = cpuTime() - begin1b;

  begin2 = cpuTime();
  for(<lst, sep> <- pairs){
    intercalate2(sep, lst);
  }
  time2 = cpuTime() - begin2;

  begin3 = cpuTime();
  for(<lst, sep> <- pairs){
    intercalate3(sep, lst);
  }
  time3 = cpuTime() - begin3;

  // begin4 = cpuTime();
  // for(<lst, sep> <- pairs){
  //   intercalate4(sep, lst);
  // }
  // time4 = cpuTime() - begin4;

  println("time1a <time1a/1000000>, 
          'time1b <time1b/1000000>, 
          'time2 <time2/1000000>, 
          'time3 <time3/1000000>, 
          
          'ratio <time1b * 1.0 /time2>");


}