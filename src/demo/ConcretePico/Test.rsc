module demo::ConcretePico::Test

import languages::pico::syntax::Identifiers;

PICOID P = [|x|];  //HACK: force introduction of type PICOID
 
alias PICO_ID = PICOID;
