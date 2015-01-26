module experiments::Compiler::Examples::Tst1

bool b1 = true;
bool b2 = false;

value main(list[value] args) = (b1 ==> b2); // <==> !(b1 && !b2);