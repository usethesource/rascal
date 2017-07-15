module experiments::Compiler::Examples::Tst3

import String;

public str functionPath(str fname, str namespace="") = 
    fname when size(namespace) == 0;
public str functionPath(str fname, str namespace="") = 
    namespace + "/" + fname when size(namespace) > 0;
    
value main() = functionPath("broken");