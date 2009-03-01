module SimpleExamples

import Rascal;


int cntModules(Rascal program)
@doc{Count the modules in a Rascal program}
{
    int cnt = 0;

    visit (Program){
      case Module M: cnt += 1;
    };
    if (x == 0) 
     if (x == 0) 
       print(x);
     else
       print(y);
    return cnt;
}

set[Name] extractNames(Rascal program)
@doc{Extract all names from a Rascal program}
{
   set[Name] names = {};

   visit (program){
      case Name Nm: names += Name;
    };
    return names;
}

Rascal rename(Rascal program)
@doc{Prefix all names in the program with "x"}
{
   set[Name] names = {};

   return visit (Program){
      case Name Nm: insert parseString("x" + toString(Nm));
    };
}

Rascal invertIf(Rascal program)
@doc{Switch the branches of if statements}
{
}

