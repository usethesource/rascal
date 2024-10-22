module lang::rascal::tests::concrete::recovery::bugs::MultiErrorPico

import lang::pico::\syntax::Main;
import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

bool multiErrorPico() {
    return checkRecovery(#start[Program], "begin
  declare;
  i := #1;
  j := #2;
  k := 3
end" , ["#1", "#2"]);
}
