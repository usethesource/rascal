module experiments::Compiler::Examples::Tst5

syntax AS = {A ","}+;
syntax A = "a";

value main(list[value] args){

   return (AS) `<{A ","}+ as>` := parse(#AS, "a,a,a");

}