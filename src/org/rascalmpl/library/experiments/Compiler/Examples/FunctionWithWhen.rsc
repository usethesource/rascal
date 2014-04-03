module experiments::Compiler::Examples::FunctionWithWhen

str g(str s) = inter when str inter := s;

value main(list[value] args) {
    return g("Called!");    
}