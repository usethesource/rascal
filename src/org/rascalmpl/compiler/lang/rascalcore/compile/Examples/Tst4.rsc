module lang::rascalcore::compile::Examples::Tst4
 
set[int n] l = {};

//  import lang::rascalcore::check::AType;
// import IO;

//  void main(){
//     x = avoid();
//     y = \start(alt({},alabel=""));
//     println("alub(<x>, <y>) =\> <alub(x,y)>");
//     println("<alub(y,x)>");
//  }

// void f(int n){
//   [x | x <- [0..n], [x] := [x], x > 0];
// }
// list[int] index(list[&T] lst) = upTill(size(lst));

// @javaClass{org.rascalmpl.library.Prelude}
// java list[int] upTill(int n);

// @javaClass{org.rascalmpl.library.Prelude}
// java int size(list[&T] lst);

// int lastIndexOf(list[&T] lst, &T elt) {
// 	for(i <- [0..10]) {
// 		if(lst[i] == elt) return i;
// 	}
// 	return -1;
// }




//data D
//  = a(str s, D d)
//  | b()
//  ;
//
//void f() {
//  set[D] ds1 = {};
//  set[D] ds2 = {d
//    | D d: a(_, child: b()) <- ds1
//    , s2: "bar" := "foo" // Warning: "Unused patternvariable `s2`"
//    , /s2 := child
//  };
//}

//int sepInOthers(list[int] sep)
//    {
//      if([_] := sep) { return  1; }
//      else {
//        return 2;
//      }
//    }
//data Symbol = seq(list[Symbol] syms);
//
//bool sepInOthers2(Symbol sep, set[Symbol] others)    // TODO: factored out due to compiler issue
//     //= sep in others || (seq([a,_,b]) := sep && (a in others || b in others));
//    //= sep in others ? true
//    //                : (seq([a,_,b]) := sep) ? (a in others || b in others) : false;
//    { //if(sep in others) return true;
//      if(seq([a,_,b]) := sep) return (a in others || b in others);
//      else return false;
//    }
//    
//    //{ if(sep in others) return true;
//    //  if(seq([a,_,b]) := sep) return (a in others || b in others);
//    //  return false;
//    //}

//import IO;
//void main(set[value] y, set[value] z) {
//    //if(/int x := y || /int x := z) {
//    //    println(x);
//    //}
//
//    if ((/int x := y && /x := z) || (/int x := z && /x := y)) {
//        println(x);
//    }
//}

//import ParseTree;
//import String;
//
//public str squeeze(str src, type[&CharClass <: ![]] _) = visit(src) {
//    case /<c:.><c>+/ => c
//      when &CharClass _ := Tree::char(charAt(c, 0))
//};

//import util::Maybe;
//
//test bool nothingStrEqual(){
//    Maybe[str] x = nothing();
//    Maybe[str] y = nothing();
//    return x == nothing() && y == nothing() && x == y;
//}
//
//test bool just2nothing(){
//    x = just(3);
//    x = nothing();
//    return x == nothing();
//}
//
//test bool inc1(){
//    Maybe[int] x = just(3);
//    x.val += 1;
//    return x.val == 4;
//}
//
//test bool inc2(){
//    x = just(3);
//    x.val += 1;
//    return x.val == 4;
//}
//
//test bool inc3(){
//    x = just((1 : "a"));
//    x.val[1] ? "aaa" += "xxx";
//    return x.val == (1:"axxx");
//}
//
//test bool inc4(){
//    x = just((1 : "a"));
//    x.val[2] ? "aaa" += "xxx";
//    return x.val == (1:"a",2:"aaaxxx");
//}
//
//data X = contain(
//            Maybe[bool] p,
//            Maybe[int] kw1 = just(2),
//            Maybe[str] kw2 = nothing()
//        );
//
//test bool contain1(){
//    c = contain(nothing());
//    return c.p == nothing() && c.kw1.val == 2 && c.kw2 == nothing();
//}
//
//test bool contain2(){
//    c = contain(nothing(), kw1 = nothing(), kw2 = just("hi"));
//    return c.p == nothing() && c.kw1 == nothing() && c.kw2 == just("hi");
//}
//
//test bool contain3(){
//    c = contain(nothing());
//    c.kw1 = nothing();
//    c.kw2 = just("hi");
//    return c.p == nothing() && c.kw1 == nothing() && c.kw2 == just("hi");
//}

//data D = d(int n);
//
//value main(){
//    x = d(1);
//    num y = 3;
//    x.n = y;
//    return 0;
//}

    
//import ParseTree;
//import lang::pico::\syntax::Main;
//
//value main() //test bool Pico1() 
// {t1 = (Program) `begin declare x: natural; x := 10 end`; return true;}

//import ParseTree;
//
//syntax A = a: "a";
//
//syntax As = as: A+ alist;
//
//syntax C = c: A a "x" As as;
//
//layout W = [\ ]*;
//
//value main() {//test bool testFieldSelectC(){
//    pt = parse(#C, "axaaa");
//    return c(A a, As _) := pt && pt.a == a;
//}
//import ParseTree;

//value main() // test bool ifThen3() 
//    {int n = 10; l:if(int i <- [1,2,3]){ if (i % 2 != 0) { n = n + 4; fail l; } n = n - 4;} return n == 10;}

//lexical Whitespace 
//  = 
//  [\u0009-\u000D \u0020 \u0085 \u00A0 \u1680 \u180E \u2000-\u200A \u2028 \u2029 \u202F \u205F \u3000]
//  ; 
//
//lexical Comment = "//" ![\n]* $;
//
//layout Standard 
//  = WhitespaceOrComment* !>> [\u0009-\u000D \u0020 \u0085 \u00A0 \u1680 \u180E \u2000-\u200A \u2028 \u2029 \u202F \u205F \u3000] !>> "//";
//  
//lexical WhitespaceOrComment 
//  = Whitespace
//  | comment: Comment
//  ; 

//syntax A = "a";
//syntax AA = a: A;
//
//value main() = 1;

//import IO;
//
//syntax As = "begin" A* as "end";
//
//syntax A = "a" | "b";
//
//value main() { //test bool issue1913() {
//    A* bs = (As)`begin b a b end`.as;
//
//    As prog = (As)`begin b a a end`;
//
//    prog = visit (prog) {
//        case (As)`begin <A* as1> b <A* as2> end` 
//            => (As)`begin <A* as1> <A* bs> <A* as2> end`
//    }
//
//    println(prog);
//    // don't loose a space
//    return "<prog>" == "begin b a b a a end";
//}
//syntax OptTestGrammar = A? a B b;
//
//syntax A = "a";
//syntax B = "b";
//
//layout L = " "*;
//
//value main() //test bool optionalNotPresentIsFalse() 
//    = !(int a <- []);
//    //= !((A)`a` <- ([OptTestGrammar] "b").a);
//    //= ([OptTestGrammar] "b").a;

//layout Layout = " "*;
//syntax AB = "a" | "b";
//syntax ABPlus = AB+ abs;
//syntax ABStar = AB* abs;
//syntax ABStarSep = {AB ","}* abs;
//value main() //test bool sizeABStar2() 
//    = size(([ABStar]"a a").abs);// == 2;
//
//int size({&E &S}* l) = (0 | it + 1 | _ <- l);
//int size(&E* l) = (0 | it + 1 | _ <- l);
//
//// ---- test1 ----
//
//test bool test11() { try return Exp::id(_) := implodeExp("a"); catch ImplodeError(_): return false;}
//
//@IgnoreCompiler{TODO}
//test bool test12() { try return Exp::number(Num::\int("0")) := implodeExp("0"); catch ImplodeError(_): return false;}
//
//test bool test13() { try return Exp::eq(Exp::id(_),Exp::id(_)) := implodeExp("a == b"); catch ImplodeError(_): return false;}
//
//@IgnoreCompiler{TODO}
//test bool test14() { try return Exp::eq(Exp::number(Num::\int("0")), Exp::number(Num::\int("1"))) := implodeExp("0 == 1"); catch ImplodeError(_): return false;}
//
//test bool test15() { try return  Expr::id(_) := implodeExpr("a"); catch ImplodeError(_): return false;}
//
//@IgnoreCompiler{TODO}
//test bool test16() { try return Expr::number(Number::\int("0")) := implodeExpr("0"); catch ImplodeError(_): return false;}
//
//test bool test17() { try return Expr::eq(Expr::id(_),Expr::id(_)) := implodeExpr("a == b"); catch ImplodeError(_): return false;}
//
//@IgnoreCompiler{TODO}
//test bool test18() { try return Expr::eq(Expr::number(Number::\int("0")), Expr::number(Number::\int("1"))) := implodeExpr("0 == 1"); catch ImplodeError(_): return false;}
//
//// ---- test2 ----
//
//test bool test21() { try return Exp::eq(Exp::id("a"),Exp::id("b")) := implodeExpLit1(); catch ImplodeError(_): return false;}
//
//test bool test22() { try return Exp::eq(Exp::id("a"),Exp::number(Num::\int("11"))) := implodeExpLit2(); catch ImplodeError(_): return false;}
//
//test bool test23() { try return Expr::eq(Expr::id("a"),Expr::id("b")) := implodeExprLit1(); catch ImplodeError(_): return false;}
//
//test bool test24() { try return  Expr::eq(Expr::id("a"),Expr::number(Number::\int("11"))) := implodeExprLit2(); catch ImplodeError(_): return false;}

//import IO;
//
//lexical Id = [a-z]+ \ "type";
//
//layout Whitespace = [\ \t\n]*;
//
//start syntax Program = Stat+;
//
//syntax Stat
//    = "type" Id ";"
//    | Type Id ";"
//    | Exp ";"
//    ;
//
//syntax Type
//    = Id
//    | Id "*"
//    ;
//
//syntax Exp
//    = Id
//    | left Exp "*" Exp
//    ;
//
//start[Program] program(str input) {
//  // we always start with an empty symbol table
//  set[str] symbolTable = {};
//
//  // here we collect type declarations
//  Stat declareType(s:(Stat) `type <Id id>;`) {
//    println("declared <id>");
//    symbolTable += {"<id>"};
//    return s;
//  }
//
//  // here we remove type names used as expressions
//  Exp filterExp(e:(Exp) `<Id id>`) {
//    if ("<id>" in symbolTable) {
//        println("filtering <id> because it was declared as a type.");
//        filter;
//    }
//    else {
//        return e;
//    }
//  }
//
//  return parse(#start[Program], input, |demo:///|, filters={declareType, filterExp}, hasSideEffects=true);
//}
//
//value main(){
//    example = "type a; a * a;";
//    return program(example);
//}

//import ParseTree;
//
//@synopsis{Pretty prints parse trees using ASCII art lines for edges.}
//str prettyTree(Tree t, bool src=false, bool characters=true, bool \layout=false, bool literals=\layout) {
//  str nodeLabel(appl(prod(label(str l, Symbol nt), _, _), _)) = "<type(nt,())> = <l>: ";
//  //str nodeLabel(appl(prod(Symbol nt, as, _), _))              = "<type(nt,())> = <for (a <- as) {><type(a,())> <}>";
//  //str nodeLabel(appl(regular(Symbol nt), _))                  = "<type(nt,())>";
//  //str nodeLabel(char(32))                                     = "⎵";
//  //str nodeLabel(char(10))                                     = "\\r";
//  //str nodeLabel(char(13))                                     = "\\n"; 
//  //str nodeLabel(char(9))                                      = "\\t";
//  //str nodeLabel(amb(_) )                                      = "❖";
//  //str nodeLabel(loc src)                                      = "<src>";
//  default str nodeLabel(Tree v)                               = "<v>";
//
//  //lrel[str,value] edges(Tree t:appl(_,  list[Tree] args)) = [<"src", t@\loc> | src, t@\loc?] + [<"", k> | Tree k <- args, include(k)];
//  //lrel[str,value] edges(amb(set[Tree] alts))              = [<"", a> | Tree a <- alts];
//  //lrel[str,value] edges(loc _)                            = [];
//  //default lrel[str,value] edges(Tree _)                   = [];
//  //  
//  return ppvalue(t, nodeLabel/*, edges*/);
//}
//
//@synopsis{Pretty prints nodes and ADTs using ASCII art for the edges.}
//str prettyNode(node n, bool keywords=true) {
//  //str nodeLabel(list[value] _)       = "[…]";
//  //str nodeLabel(set[value] _)        = "{…}";
//  //str nodeLabel(map[value, value] _) = "(…)";
//  //str nodeLabel(value t)             = "\<…\>" when typeOf(t) is \tuple;
//  //str nodeLabel(node k)              = getName(k);
//  default str nodeLabel(value v)     = "<v>";
//  
//  //lrel[str,value] edges(list[value] l)       = [<"", x> | value x <- l];
//  //lrel[str,value] edges(value t)             = [<"", x> | value x <- carrier([t])] when typeOf(t) is \tuple;
//  //lrel[str,value] edges(set[value] s)        = [<"", x> | value x <- s];
//  //lrel[str,value] edges(map[str, value] m)   = [<"<x>", m[x]> | value x <- m];  
//  //lrel[str,value] edges(map[num, value] m)   = [<"<x>", m[x]> | value x <- m];  
//  //lrel[str,value] edges(map[loc, value] m)   = [<"<x>", m[x]> | value x <- m];  
//  //lrel[str,value] edges(map[node, value] m)  = [<"key", x>, <"value", m[x]> | value x <- m];  
//  //lrel[str,value] edges(node k)              = [<"", kid> | value kid <- getChildren(k)] + [<l, m[l]> | keywords, map[str,value] m := getKeywordParameters(k), str l <- m];
//  //default lrel[str,value] edges(value _)     = [];
//    
//  return ppvalue(n, nodeLabel/*, edges*/);
//}
//
//private str ppvalue(value e, str(value) nodeLabel/*, lrel[str,value](value) edges*/) 
//  = ""; //" <nodeLabel(e)>
//    //'<ppvalue_(e, nodeLabel, edges)>";
//
////private str ppvalue_(value e, str(value) nodeLabel, lrel[str,value](value) edges, str indent = "") {
////  lrel[str, value] kids = edges(e);
////  int i = 0;
////
////  str indented(str last, str other, bool doSpace) 
////    = "<indent> <if (i == size(kids) - 1) {><last><} else {><other><}><if (doSpace) {> <}>";
////    
////  return "<for (<str l, value sub> <- kids) {><indented("└─", "├─", l == "")><if (l != "") {>─<l>─→<}><nodeLabel(sub)>
////         '<ppvalue_(sub, nodeLabel, edges, indent = indented(" ", "│", true))><i +=1; }>";
////}

//test bool everyTypeCanBeReifiedWithoutExceptions(&T u) = _ := typeOf(u);
//
//test bool allConstructorsAreDefined() 
//  = (0 | it + 1 | /cons(_,_,_,_) := #P.definitions) == 7;
//
//test bool allConstructorsForAnAlternativeDefineTheSameSort() 
//  = !(/choice(def, /cons(label(_,def),_,_,_)) !:= #P.definitions);
//  
//test bool typeParameterReificationIsStatic1(&F _) = #&F.symbol == \parameter("F",\value());
//test bool typeParameterReificationIsStatic2(list[&F] _) = #list[&F].symbol == \list(\parameter("F",\value()));
//
//@ignore{issue #1007}
//test bool typeParameterReificationIsStatic3(&T <: list[&F] f) = #&T.symbol == \parameter("T", \list(\parameter("F",\value())));
//
//test bool dynamicTypesAreAlwaysGeneric(value v) = !(type[value] _ !:= type(typeOf(v),()));
//
//// New tests which can be enabled after succesful bootstrap
//data P(int size = 0);
//
//@ignore{Does not work after changed TypeReifier in compiler}
//test bool allConstructorsHaveTheCommonKwParam()
//  =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label("size", \int()) in kws);
//   
//@ignoreCompiler{Does not work after changed TypeReifier in compiler}  
//test bool axiomHasItsKwParam()
//  =  /cons(label("axiom",_),_,kws,_) := #P.definitions && label("mine", \adt("P",[])) in kws;  
//
//@ignore{Does not work after changed TypeReifier in compiler}  
//test bool axiomsKwParamIsExclusive()
//  =  all(/cons(label(!"axiom",_),_,kws,_) := #P.definitions, label("mine", \adt("P",[])) notin kws);
//  
  
  


//import List;
//test bool listCount1(list[int] L){
//   int cnt(list[int] L){
//    int count = 0;
//    while ([int _, *int _] := L) { 
//           count = count + 1;
//           L = tail(L);
//    }
//    return count;
//  }
//  return cnt(L) == size(L);
//}
//
//value main()= listCount1([-8,1121836232,-5,0,1692910390]);


//test bool testSimple1() 
//    = int i <- [1,4] && int j <- [2,1] && int k := i + j && k >= 5;
//
//value main() = testSimple1();

//int f(list[int] ds){
//    if([int xxx]:= ds, xxx > 0){
//        return 1;
//    } else {
//        return 2;
//    }
//}
//value main() = /*testSimple1() && */f([1]) == 1;
