module experiments::Compiler::Examples::PARSETREE

extend experiments::Compiler::Examples::TYPE;


data SYM = C() | D();

bool subtype(A(), C()) = true;
bool subtype(A(), D()) = true;

value main(list[value] args) = comparable(A(), D());