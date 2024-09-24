module lang::rascal::tests::concrete::recovery::PicoMassRecoveryTests

import lang::pico::\syntax::Main;
import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

str picoFac = "begin declare input : natural,
              output : natural,
              repnr : natural,
              rep : natural;
      input := 14;
      output := 1;
      % check this out %
      while input - 1 do
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end";

bool testFacDeletions() {
    TestStats stats = testSingleCharDeletions(#Program, picoFac);
    printStats(stats);
    return true;
}