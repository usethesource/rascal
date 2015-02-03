module experiments::Compiler::Examples::Tst2

import lang::rascal::grammar::definition::Symbols;
import Message;

//int f(int n){
//	if(true){
//		int n = 0;
//	}
//	return n;
//}

int h(int n){
    int h() = n;
    int h(int  x) = x;
    return h(n);
}

value main(list[value] args) = h(13);