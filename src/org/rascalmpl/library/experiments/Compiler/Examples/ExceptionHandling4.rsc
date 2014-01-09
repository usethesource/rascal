module experiments::Compiler::Examples::ExceptionHandling4

import Exception;
import List;

public bool main(list[value] args) { 
    try { 
        head([]); 
    } catch EmptyList():
        return true;
    return false; 
}