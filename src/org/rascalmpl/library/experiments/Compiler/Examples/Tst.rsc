module experiments::Compiler::Examples::Tst

import Prelude;


int bug() {

return indexOf([<"String",{104}>,<"demo::common::WordReplacement",{158,71}>], <"demo::common::WordReplacement",{158,71}>);

//return indexOf([<"experiments::Compiler::Examples::Tst3",{66}>,<"experiments::Compiler::Examples::Tst3",{46,47}>,<"experiments::Compiler::Examples::Tst3",{47}>], <"experiments::Compiler::Examples::Tst3",{66}>);

}

value main(list[value] args) = bug();