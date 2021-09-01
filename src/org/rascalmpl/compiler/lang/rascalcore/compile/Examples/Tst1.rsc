module lang::rascalcore::compile::Examples::Tst1

//import lang::rascalcore::compile::Examples::Tst2;
//
//syntax Tag
//    = //@Folded @category="Comment" \default   : "@" Name name TagString contents 
//    //| @Folded @category="Comment" empty     : "@" Name name 
//    | @Folded @category="Comment" expression: "@" Name name "=" Expression expression !>> "@"
//    ;
//    
//lexical TagString
//    = "\\" !<< "{" ( ![{}] | ("\\" [{}]) | TagString)* contents "\\" !<< "}";
//
//syntax Expression = "\"MetaVariable\"";
//syntax Name = "category";
//
//syntax ProdModifier = \tag: Tag tag;
//
////data Attr 
////     = \tag(value \tag);
//
//
//value main() = //[Tag] "@category=\"MetaVariable\"";
//  \tag("category"("MetaVariable"));



data Tree = char(int character);
private bool check(type[&T] _, value x) = &T _ := x; //typeOf(x) == t.symbol;


value main() //test bool notSingleB() 
    = !check(#[A], char(66));

//value main(){ //test bool optionalPresentIsTrue() {
//    xxx = [];
//    return _ <- xxx;
//}    

//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//list[int] f([*int x, *int y]) { if(size(x) == size(y)) return x; fail; }
////default list[int] f(list[int] l) = l;
//
//value main(){ //test bool overloadingPlusBacktracking2(){
//    return f([1,2,3,4]);// == [1, 2];
//}