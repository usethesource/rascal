module lang::rascalcore::compile::Examples::Tst1

import Set;
import Exception;

value main(){ //test bool emptySetException1() { 
    try { 
        getOneFrom({}); 
    } catch CallFailed([{}]): 
        return true; 
    return false; 
}