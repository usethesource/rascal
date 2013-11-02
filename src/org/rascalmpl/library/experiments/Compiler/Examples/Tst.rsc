module experiments::Compiler::Examples::Tst

&T <: num neg(&T <: num x) = - x;

value main(list[value] args) = neg(3);