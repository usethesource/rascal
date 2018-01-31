module lang::rascalcore::check::Test3

import lang::rascalcore::check::Test2;

str module_name = "a";

value f(){
    module_name = 3;
    return module_name;
    }

