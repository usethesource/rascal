module experiments::Compiler::Examples::Tst2
import IO;
//import Grammar;
//import ParseTree;
//import Node;
//import String;
//import Set;
////import lang::rascal::grammar::ParserGenerator;
//import util::Reflective;
//
//anno int Symbol@id;
//
//map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");
//
//map[str,str] javaIdEscapes = javaStringEscapes + ("-":"_", "_": "__");
//
//public str escId(str s){
//    res = escape(s, javaIdEscapes);
//    println("escId(<s>) =\> <res>");
//    return res;
//}
//
//str v2iX(value v) {	
//    switch (v) {
//        case item(p:prod(Symbol u,_,_), int i) : return "<v2iY(u)>.<v2iY(p)>_<v2iY(i)>";
//        case label(str x,Symbol u) : return escId(x) + "_" + v2iY(u);
//        case layouts(str x) : return "layouts_<escId(x)>";
//        case conditional(Symbol s,_) : return v2iY(s);
//        case sort(str s)   : return "<s>";
//        case \lex(str s)   : return "<s>";
//        case keywords(str s)   : return "<s>";
//        case \parameterized-sort(str s, list[Symbol] args) : return ("<s>_" | it + "_<v2iY(arg)>" | arg <- args);
//        case \parameterized-lex(str s, list[Symbol] args) : return ("<s>_" | it + "_<v2iY(arg)>" | arg <- args);
//        case cilit(/<s:^[A-Za-z0-9\-\_]+$>/)  : return "cilit_<escId(s)>";
//	    case lit(/<s:^[A-Za-z0-9\-\_]+$>/) : return "lit_<escId(s)>";
//        case int i         : return i < 0 ? "min_<-i>" : "<i>";
//        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)]);
//        case str s()       : return escId(s);
//        case node n        : return "<escId(getName(n))>_<("" | it + "_" + v2iY(c) | c <- getChildren(n))>";
//        case list[value] l : return ("" | it + "_" + v2iY(e) | e <- l);
//        case set[value] s  : return ("" | it + "_" + v2iY(e) | e <- sort(s));
//        default            : throw "value not supported <v>";
//    }
//}
//
//str v2iY(value v){
//	println("v2iX(<v>)");
//	res = v2iX(v);
//	println("v2iX(<v>) =\> <res>");
//	return res;
//}
//
//value main(list[value] args) = v2iY(prod(lit("0")[@id=9],[\char-class([range(48,48)])[@id=10]],{}));

// value main(list[value] args) =  diff(generateNewItems(makeUnique(G0)),
// (
//  sort("S")[
//    @id=5
//  ]:(item(
//      prod(
//        sort("S")[
//          @id=5
//        ],
//        [lit("0")[
//            @id=6
//          ]],
//        {}),
//      0):<"new LiteralStackNode\<IConstructor\>(6, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, null)",6>),
//  lit("0")[
//    @id=9
//  ]:(item(
//      prod(
//        lit("0")[
//          @id=9
//        ],
//        [\char-class([range(48,48)])[
//            @id=10
//          ]],
//        {}),
//      0):<"new CharStackNode\<IConstructor\>(10, 0, new int[][]{{48,48}}, null, null)",10>)
//)
//);
	
	
	
	//v2iX(prod(lit("0")[@id=19],[\char-class([range(48,48)])[@id=20]],{}));

data D = d(int i) | d();
 
int sw4(value e){ 	
 	int n = 0;
 	switch(e){
 		case "abc": 		n = 1;
		case str s: /def/: 	n = 2;
		case 3: 			n = 3;
		case d(): 			n = 4;
		case d(z): 			n = 5;
		case str s(3): 		n = 6;
		case [1,2,3]: 		n = 7;
		case [1,2,3,4]: 	n = 8;
		default: 			n = 9;
 	}
 	return n;
 }
 
 //test bool testSwitch4a() = sw4("abc") 		== 1;
 //test bool testSwitch4b() = sw4("def") 		== 2;
 //test bool testSwitch4c() = sw4(3)     		== 3;
 //test bool testSwitch4d() = sw4(d())   		== 4;
 //test bool testSwitch4e() = sw4(d(2))  		== 5;
 //test bool testSwitch4f() = sw4("abc"(3))	== 6;
 //test bool testSwitch4g() = sw4([1,2,3]) 	== 7;
 //test bool testSwitch4h() = sw4([1,2,3,4]) 	== 8;
 //test bool testSwitch4i() = sw4(<-1,-1>) 	== 9;
 // 	
  	value main(list[value] args) = sw4("def");

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