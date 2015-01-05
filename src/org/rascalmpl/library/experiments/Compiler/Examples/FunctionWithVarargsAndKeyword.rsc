module experiments::Compiler::Examples::FunctionWithVarargsAndKeyword

import List;

str fn(str s, int ints..., str kw = "keyword") = s + "-" + intercalate("-", ints) + "-" + kw;


value main(list[value] args) = 
	fn("a") + ";" + fn("b",kw="xxx") + ";" + fn("c",1,2,3) + ";" + fn("d",1,2,3,kw="xxx");