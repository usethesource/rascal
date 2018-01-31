module lang::rascalcore::compile::Benchmarks::BMarriage

import Exception;
import List;
import Map;
import Relation;
import Set;
import IO;

// Stable Marriage algorithm

map[str,list[str]] male_preferences = (
   "abe":  ["abi", "eve", "cath", "ivy", "jan", "dee", "fay", "bea", "hope", "gay"],
   "bob":  ["cath", "hope", "abi", "dee", "eve", "fay", "bea", "jan", "ivy", "gay"],
   "col":  ["hope", "eve", "abi", "dee", "bea", "fay", "ivy", "gay", "cath", "jan"],
   "dan":  ["ivy", "fay", "dee", "gay", "hope", "eve", "jan", "bea", "cath", "abi"],
   "ed":   ["jan", "dee", "bea", "cath", "fay", "eve", "abi", "ivy", "hope", "gay"],
   "fred": ["bea", "abi", "dee", "gay", "eve", "ivy", "cath", "jan", "hope", "fay"],
   "gav":  ["gay", "eve", "ivy", "bea", "cath", "abi", "dee", "hope", "jan", "fay"],
   "hal":  ["abi", "eve", "hope", "fay", "ivy", "cath", "jan", "bea", "gay", "dee"],
   "ian":  ["hope", "cath", "dee", "gay", "bea", "abi", "fay", "ivy", "jan", "eve"],
   "jon":  ["abi", "fay", "jan", "gay", "eve", "bea", "dee", "cath", "ivy", "hope"]
   );

map[str, list[str]] female_preferences = (
   "abi":  ["bob", "fred", "jon", "gav", "ian", "abe", "dan", "ed", "col", "hal"],
   "bea":  ["bob", "abe", "col", "fred", "gav", "dan", "ian", "ed", "jon", "hal"],
   "cath": ["fred", "bob", "ed", "gav", "hal", "col", "ian", "abe", "dan", "jon"],
   "dee":  ["fred", "jon", "col", "abe", "ian", "hal", "gav", "dan", "bob", "ed"],
   "eve":  ["jon", "hal", "fred", "dan", "abe", "gav", "col", "ed", "ian", "bob"],
   "fay":  ["bob", "abe", "ed", "ian", "jon", "dan", "fred", "gav", "col", "hal"],
   "gay":  ["jon", "gav", "hal", "fred", "bob", "abe", "col", "ed", "dan", "ian"],
   "hope": ["gav", "jon", "bob", "abe", "ian", "dan", "hal", "ed", "col", "fred"],
   "ivy":  ["ian", "col", "hal", "gav", "fred", "bob", "abe", "ed", "jon", "dan"],
   "jan":  ["ed", "hal", "gav", "abe", "bob", "jon", "col", "ian", "fred", "dan"]
   );
  
data ENGAGED = engaged(str man, str woman);

public set[ENGAGED] stableMarriage(map[str,list[str]] male_preferences, map[str,list[str]] female_preferences){
  engagements = {};
  freeMen = domain(male_preferences);
  while (size(freeMen) > 0){
     <m, freeMen> = takeOneFrom(freeMen);
     w = head(male_preferences[m]);
     if(size(male_preferences[m]) == 0) return engagements;
     male_preferences[m] = tail(male_preferences[m]);
     if({engaged(str m1, w), *_} := engagements){
        if(indexOf(female_preferences[w], m) < indexOf(female_preferences[w], m1)){
           engagements1 = engagements - {engaged(m1, w)} + {engaged(m, w)};
           engagements = engagements1;
           freeMen += m1;
         } else {
           freeMen += m;
         }
      } else {
      	engagements += {engaged(m, w)};
      } 
  }
  return engagements;    
}

value main(){
  
   for(i <- [1 .. 200]){
     stableMarriage(male_preferences, female_preferences);
  }
  return 0;
}