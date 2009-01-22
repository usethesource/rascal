module WordReplacement

import String;

public str capAll1(str S)
{
 result = "";
 while (/^<before:\W*><word:\w+><after:.*$>/ := S) { 
    result = result + before + capitalize(word);
    S = after;
  }
  return result;
}

public str capitalize(str word)
{
   if(/^<letter:[a-z]><rest:.*$>/ := word){
   		return toUpperCase(letter) + rest;
   } else {
   		return word;
   }
}

public str capAll2(str S)
{
   return visit(S){
   	case /<word:\w+>/i => capitalize(word)
   };
}

public bool test()
{
	return
	    capitalize("1") == "1" &&
	    capitalize("rascal") == "Rascal" &&
		capAll1("turn this into a title") == "Turn This Into A Title"  &&
		capAll2("turn this into a title") == "Turn This Into A Title" ;
}