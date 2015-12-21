
module experiments::Compiler::Examples::Tst6

import Relation;
import IO;
import Map;

//// Load configs for the modules that we do not need to re-check
//    map[RName,Configuration] existingConfigs = ( wl : getCachedConfig(prettyPrintName(wl),pcfg) | wl <- carrier(ig), wl in moduleLocations, wl notin c.dirtyModules, <true, _> := cachedConfigReadLoc(prettyPrintName(wl),pcfg) );
//    
//    Configuration fullyCheckSingleModule(Configuration ci, RName itemName) {
//        // Using the checked type information, load in the info for each module
//        for (wl <- allImports[itemName], wl in moduleLocations) {
//            ci = loadConfigurationAndReset(ci, existingConfigs[wl], wl, importFilter[itemName][wl], allImports[itemName]);
//        }
        
tuple[bool, loc] cachedConfigReadLoc(str wl) = <true, |home:///<wl>|>;

    
 value f(){
 
    ig = {<"M1", "a">, <"M2", "a">, <"M2", "b">, <"M3", "d">};
    
    map[str, set[str]] allImports = ("M1" : {"a", "b"}, "M2" : {"c", "d"}, "a" : {}, "b" : {}, "c" : {}, "d" : {});
    allMods = {"M1", "M2", "a", "b", "c", "d"};
    dirtyModules = {"b"};
    map[str,rel[str,str]]  importFilter = ("M1" : {}, "M2" : {}, "a" : {}, "b" : {}, "c" : {}, "d" : {});
    
    println(importFilter);
    
    map[str,loc] moduleLocations = ("M1": |home:///M1|, "M2" : |home:///M2|,  "a" : |home:///a|,  "b" : |home:///b|,
     "c" : |home:///c|,  "d" : |home:///d|);
   

    existingConfigs = ( wl : wl
                      | wl <- allMods, 
                      wl in moduleLocations, 
                      wl notin dirtyModules, 
                      <true, _> := cachedConfigReadLoc(wl)
                      );
   println("existingConfigs = <existingConfigs>");
   
   void fn(str itemName){
      println("itemName = <itemName>");
      for (wl <- allImports[itemName], wl in moduleLocations) {
            println("wl = <wl>");
            println("existingConfigs[wl] = <existingConfigs[wl]>");
            println("importFilter[itemName] = <importFilter[itemName]>");
            println("importFilter[itemName][wl] = <importFilter[itemName][wl]>");
            println("allImports[itemName] = <allImports[itemName]>");
        }
   }
   
    for(itemName <- allMods){
       fn(itemName);
    }
 
 }       