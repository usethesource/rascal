module WordReplacement

public str capOne(str S){
    return (/rascal/i ~= S) ? "Rascal" : S;
}

public str capAll1(str S)
{
 result = "";
 while (/^<before:\W*><word:\w+><after:.*$>/ ~= S) { 
    result = result + before + capOne(word);
    S = after;
  }
  return result;
}

public str capAll2(str S)
{
   return visit(S){
   	case /rascal/i => "Rascal"
   };
}