module experiments::VisitorMigration::Extract

import IO;
import List;
import Map;

loc evalSrc = |cwd:///src/org/rascalmpl/interpreter/Evaluator.java|;

// Extract all visit methods from Evaluator.java
// Returns a map from method name to its source code.

public map[str, str] extract(){
  lines = readFileLines(evalSrc);
  mapping = ();
  N = size(lines);  // length of source text
  I = 0;			// index in source code
  while(I < N){
     switch(lines[I]){
     
       case /^\s*\/\//: I += 1;   // Skip commented lines
       
       case /public\s+Result.*visit<name:[A-Za-z\-\_]+>\s*\(/: {
         nesting = 0;
         txt = "";
         inBody = false;
         // Read the body until balanced brackets are found
         // inBody == false as long as we are still in the method header
         do {
           nesting += nestingDiff(lines[I]);
           if(!inBody && nesting > 0)
              inBody = true;
           txt += lines[I] + "\n";
           I += 1;
         } while ( I < N && (!inBody || nesting > 0));
         mapping[name] = txt;
         I += 1;
       }
       default: I += 1;
     }
  }
  println("Found <size(mapping)> visit methods");
  return mapping;
}

// Determine the effect of a line on the { } nesting level.
// Caveat: brackets in comments and strings are also taken into account
int nestingDiff(str txt){
  int n = 0;
  visit(txt){
    case /^\{/: {n += 1;}
    case /^\}/: {n -= 1;}
  }
  return n;
}
