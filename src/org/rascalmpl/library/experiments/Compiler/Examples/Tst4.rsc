
module experiments::Compiler::Examples::Tst4

import Message;
import IO;

value main(list[value] args) {

	messages = [];
    if(any(Message msg <- messages, error(_,_) := msg)){
    	println("yes");
    } else {
    
    	println ("no");
    }
    return "OK";
   } 	