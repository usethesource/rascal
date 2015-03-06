module experiments::Compiler::Examples::Tst3

import lang::rascal::types::AbstractType;
import Message;

value main(list[value] args) {
	rt = failure({error("Type String not declared",|unknown:///|(2,6,<1,2>,<1,8>))});
	return isFailType(rt);
}	