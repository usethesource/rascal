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
    Tree error = findFirstError(defaultErrorDisambiguationFilter(t));
    return getErrorText(error) == "output x rep";
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
    Tree error = findFirstError(defaultErrorDisambiguationFilter(t));
    return getErrorText(error) == "input := input - 1\n      od";
}

test bool picoTypoSmall() {
    t = parsePico(
"begin declare;
  while input do
    input x= 14;
    output := 0
  od
end");

    Tree error = findFirstError(defaultErrorDisambiguationFilter(t));
    return getErrorText(error) == "x= 14";
}

test bool picoMissingSemiSmall() {
    t = parsePico(
"begin declare;
  while input do
    input := 14
    output := 0
  od
end");

    Tree error = findFirstError(defaultErrorDisambiguationFilter(t));
    return getErrorText(error) == "output := 0\n  od";
}