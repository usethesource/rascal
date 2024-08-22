module lang::rascal::tests::recovery::PicoRecoveryTests

import lang::pico::\syntax::Main;

import ParseTree;
import IO;

Tree parsePico(str input, bool visualize=false) 
    = parser(#Program, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);

test bool picoOk() {
    t = parsePico("begin declare input : natural, 
              output : natural,           
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end");
    return !hasErrors(t);
}

test bool picoTypo() {
    t = parsePico("begin declare input : natural, 
              output : natural,           
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output x rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end");
    iprintln(findFirstError(t));
    return hasErrors(t) && size(findAllErrors(t)) == 1 && getErrorText(findFirstError(t)) == "x rep;
             repnr := repnr - 1
          od";
}

test bool picoMissingSemi() {
    t = parsePico("begin declare input : natural, 
              output : natural,           
              repnr : natural,
              rep : natural;
      input := 14;
      output := 0;
      while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od
          input := input - 1
      od
end");
    str errorText = getErrorText(findFirstError(t));
    println("error count: <size(findAllErrors(t))>");
    println("error text: <errorText>");

    for (error <- findAllErrors(t)) {
        println("   error: <getErrorText(error)>");
    }

    return hasErrors(t) && size(findAllErrors(t)) == 1 && getErrorText(findFirstError(t)) == "input := input - 1
      od";
}

test bool picoTypoMinimal() {
    t = parsePico(
"begin declare;
  while input do
    input x= 14;
    output := 0
  od
end", visualize=false);

    iprintln(t);

    for (error <- findAllErrors(t)) {
        println("   error: <getErrorText(error)>");
    }

    disambiguated = defaultErrorDisambiguationFilter(t);
    println("after disambiguation:");
    for (error <- findAllErrors(disambiguated)) {
        println("   error: <getErrorText(error)>");
    }

return hasErrors(t);
    /*str errorText = getErrorText(findFirstError(t));
    println("error text: <errorText>");
    return hasErrors(t) && size(findAllErrors(t)) == 1 && getErrorText(findFirstError(t)) == "input := input - 1
      od";
      */
}
test bool picoMissingSemiMinimal() {
    t = parsePico(
"begin declare;
  while input do
    input := 14
    output := 0
  od
end", visualize=true);

    for (error <- findAllErrors(t)) {
        println("   error: <getErrorText(error)>");
    }

return hasErrors(t);
    /*str errorText = getErrorText(findFirstError(t));
    println("error text: <errorText>");
    return hasErrors(t) && size(findAllErrors(t)) == 1 && getErrorText(findFirstError(t)) == "input := input - 1
      od";
      */
}