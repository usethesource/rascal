module experiments::Compiler::Examples::Tst4

alias TUP = tuple[int \n,str \type];
 
value main() {  list[TUP] L = [<1, "a">, <2, "b">]; return L<\n> == [1, 2]; }