module experiments::Compiler::Examples::Tst2
import IO;
import Grammar;
import ParseTree;
import Node;
import String;
import Set;
import lang::rascal::grammar::ParserGenerator;

//str v2iX(value v) {	
//    switch (v) {
//        case item(p:prod(Symbol u,_,_), int i) : return "<v2iX(u)>.<v2iX(p)>_<v2iX(i)>";
//        case label(str x,Symbol u) : return escId(x) + "_" + v2iX(u);
//        case layouts(str x) : return "layouts_<escId(x)>";
//        case conditional(Symbol s,_) : return v2iX(s);
//        case sort(str s)   : return "<s>";
//        case \lex(str s)   : return "<s>";
//        case keywords(str s)   : return "<s>";
//        case \parameterized-sort(str s, list[Symbol] args) : return ("<s>_" | it + "_<v2iX(arg)>" | arg <- args);
//        case \parameterized-lex(str s, list[Symbol] args) : return ("<s>_" | it + "_<v2iX(arg)>" | arg <- args);
//        case cilit(/<s:^[A-Za-z0-9\-\_]+$>/)  : return "cilit_<escId(s)>";
//	    case lit(/<s:^[A-Za-z0-9\-\_]+$>/) : return "lit_<escId(s)>"; 
//        case int i         : return i < 0 ? "min_<-i>" : "<i>";
//        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)]);
//        case str s()       : return escId(s);
//        case node n        : return "<escId(getName(n))>_<("" | it + "_" + v2iX(c) | c <- getChildren(n))>";
//        case list[value] l : return ("" | it + "_" + v2iX(e) | e <- l);
//        case set[value] s  : return ("" | it + "_" + v2iX(e) | e <- sort(s));
//        default            : throw "value not supported <v>";
//    }
//}

 value main(list[value] args) =  generateNewItems(makeUnique(G0)) ==
 (
  sort("S")[
    @id=5
  ]:(item(
      prod(
        sort("S")[
          @id=5
        ],
        [lit("0")[
            @id=6
          ]],
        {}),
      0):<"new LiteralStackNode\<IConstructor\>(6, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, null)",6>),
  lit("0")[
    @id=9
  ]:(item(
      prod(
        lit("0")[
          @id=9
        ],
        [\char-class([range(48,48)])[
            @id=10
          ]],
        {}),
      0):<"new CharStackNode\<IConstructor\>(10, 0, new int[][]{{48,48}}, null, null)",10>)
);
	
	
	
	//v2iX(prod(lit("0")[@id=19],[\char-class([range(48,48)])[@id=20]],{}));

//data D = d() | d(int n) | d(str s, int m);
//
// value main(list[value] args) { 
//	value e = [1,2,3];
//	
//	switch(e){
//
//	case "abc": println("case 1");
//	case str s: /def/: println("case 2");
//	case 3: println("case 3");
//	case d(): println("case 1");
//	case d(z): println("case 2");
//	case str s(3): println("case 5");
//	case [1,2,3]: println("6");
//	case [1,2,3,4]: println("7");
//	
//	//case node q: d("abc",2): println("case 3");
//	default: println("default");
//	}
//	return true;
//}

//import ParseTree;
//layout Whitespace = [\ ]*;
//lexical IntegerLiteral = [0-9]+; 
//lexical Identifier = [a-z]+;
//
//syntax Exp 
//  = IntegerLiteral  
//  | Identifier        
//  | bracket "(" Exp ")"     
//  > left Exp "*" Exp        
//  > left Exp "+" Exp  
//  | Exp "==" Exp      
//  ;
//
//syntax Stat 
//   = Identifier ":=" Exp
//   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
//   ;
//
//
//value main(list[value] args) { 
//	e = [Exp] "1==2";
//	
//	switch(e){
//
//	case (Exp) `1`: println("case 1");
//	case (Exp) `1* 2`: println("case 2");
//	case (Exp) `1==2`: println("case 3");
//	default: println("default");
//	}
//	return true;
//}




//data D = d() | d(int n) | d(str s, int m);	
//
//value main(list[value] args) { 
//	x = d("abc",2);
//	switch(x){
//
//	case d(): println("case 1");
//	case d(z): println("case 2");
//	case node q: d("abc",2): println("case 3");
//	default: println("default");
//	}
//	return true;
//}