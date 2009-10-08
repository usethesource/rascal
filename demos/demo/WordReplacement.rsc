module demo::WordReplacement

import String;
import UnitTest;

// capitalize: convert first letter to uppercase

public str capitalize(str word)
{
   if(/^<letter:[a-z]><rest:.*$>/ := word){
     return toUpperCase(letter) + rest;
   } else {
     return word;
   }
}

// Capitalize all words in a string

// capAll1: using a while loop

public str capAll1(str S)
{
 result = "";
 while (/^<before:\W*><word:\w+><after:.*$>/ := S) { 
    result = result + before + capitalize(word);
    S = after;
  }
  return result;
}

// capAll2: using visit

public str capAll2(str S)
{
   return visit(S){
   	case /<word:\w+>/i => capitalize(word)
   };
}

public bool test()
{
  assertEqual(capitalize("1"), "1");
  assertEqual(capitalize("rascal"), "Rascal");
  assertEqual(capAll1("turn this into a title"), "Turn This Into A Title");
  assertEqual(capAll2("turn this into a title"), "Turn This Into A Title");
  
  return report("WordReplacement");
}