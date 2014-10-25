module experiments::Compiler::Examples::ExceptionHandling4

import List;
import Exception;

public bool main(list[value] args) { 
    try { 
        head([]); 
    } catch EmptyList():
        return true;
    return false; 
}