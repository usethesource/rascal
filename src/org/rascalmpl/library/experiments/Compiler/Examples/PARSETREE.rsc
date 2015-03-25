module experiments::Compiler::Examples::PARSETREE

extend experiments::Compiler::Examples::TYPE;


data SYM = C();

bool subtype(A(), C()) = true;

value main(list[value] args) = comparable(A(), C());