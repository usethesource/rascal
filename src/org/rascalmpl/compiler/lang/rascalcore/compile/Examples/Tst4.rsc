module lang::rascalcore::compile::Examples::Tst4

import IO;

value main(list[str] args){
    println(args);
    return "all" in args;
}