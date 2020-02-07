module lang::rascalcore::compile::Examples::Tst1

/*
    Import this module to Disambiguate the ambiguity cause by the prefix operators +/- and infix operators +/-.
    An example of this ambiguity is (A) + (B) . This could be (A)(+ (B)) or ((A)) + ((B)).
    We need to have a symbol table to decide if A is a type and thus a TypeCast, or it is a field/variable access.
    
    Java lacks operator overloading, therefore, prefix operators only work on numeric types.
    Moreover, there is no support for custom covariance and contravariance.
    Therefore, only if (A) is a primary/boxed numeric type can it be a prefix expression.
    
    We therefore have added this complete but not sound disambiguation as a separate module.
    
    These following cases will result in a incorrect parse tree:
    
    - Shadowing of Integer/Double/Float
    - An invalid type cast: (String)+(A) where A has a numeric type
      (This expression would be an uncompilable, and we would disambiguate it as a infix expression) 
*/
import ParseTree;
import List;
import lang::java::\syntax::Java15;

default bool isNumeric(RefType r) = false;
default bool isPrefix(Expr x) = false;

Tree amb(set[Tree] alts) {
    
        counts = [<size(casts), a> | Tree a <- alts, casts := [ isNumeric(t) | /(Expr)`(<RefType t>) <Expr e>` := a, isPrefix(e)]];
    
    fail amb;
}

//data Tree 
//     = char(int character) // <4>
//     ;
//@doc{
//.Synopsis
//Return character in a string by its index position.
//
//.Description
//Return the character at position `i` in string `s` as integer character code.
//Also see <<String-stringChar>> that converts character codes back to string.
//
//.Examples
//[source,rascal-shell]
//----
//import String;
//charAt("abc", 0);
//stringChar(charAt("abc", 0));
//----
//}
//@javaClass{org.rascalmpl.library.Prelude}
//public java int charAt(str s, int i);
//
//
//@doc{
//.Synopsis
//Determine length of a string value.
//
//.Description
//Returns the length (number of characters) in string `s`.
//
//.Examples
//[source,rascal-shell]
//----
//import String;
//size("abc");
//size("");
//----
//}
//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(str s);
//
//
//@doc{
//.Synopsis
//Return characters of a string.
//.Description
//Return a list of the characters of `s` as integer character codes.
//Also see <<String-stringChars>> that converts character codes back to string.
//
//.Examples
//[source,rascal-shell]
//----
//import String;
//chars("abc");
//stringChars(chars("abc")) == "abc";
//----
//}
//public list[int] chars(str s) = [ charAt(s,i) | i <- [0..size(s)]];
//
//private list[![]] characters(str x) = [char(i) | i <- chars(x)];
  
  
  
  
  
  
  
  
//import Type;
//
////import lang::rascal::checker::TTL::Library;
////extend lang::rascal::checker::TTL::TTLsyntax;
//import util::Math;
//
//alias SymbolPair = tuple[Symbol l, Symbol r];
//alias BinarySig = tuple[str operator, Symbol left, Symbol right];
//alias UnarySig = tuple[str operator, Symbol left];
//
//
//set[SymbolPair] rlub(\real()) = {/* <\void(), \real()>,<\real(), \void()>,*/ <Symbol::\int(), Symbol::\real()>, <Symbol::\real(), Symbol::\int()>, <Symbol::\rat(), Symbol::\real()>, <Symbol::\real(), Symbol::\rat()>, <Symbol::\real(), Symbol::\real()>};
//set[SymbolPair] rlub(Symbol::\rat()) = {/* <Symbol::\void(), Symbol::\rat()>,<Symbol::\rat(), Symbol::\void()>,*/ <Symbol::\int(), Symbol::\rat()>, <Symbol::\rat(), Symbol::\int()>, <Symbol::\rat(), Symbol::\rat()>};
//
//set[SymbolPair] rlub(Symbol::\num()) = {/*<Symbol::\void(), Symbol::\num()>,<Symbol::\num(), Symbol::\void()>,*/ <Symbol::\int(), Symbol::\num()>, <Symbol::\rat(), Symbol::\num()>, <Symbol::\real(), Symbol::\num()>, <Symbol::\num(), Symbol::\int()>, <Symbol::\num(), Symbol::\rat()>, <Symbol::\num(), Symbol::\real()>, <Symbol::\num(), Symbol::\num()>};
//
//set[SymbolPair] rlub(Symbol::\set(Symbol s)) = /*{ <Symbol::\void(), Symbol::\set(s)>, <Symbol::\set(s), Symbol::\void()>} + */ {<Symbol::\set(l), Symbol::\set(r)> | <l, r> <- rlub(s)};
//set[SymbolPair] rlub(Symbol::\rel(ts)) = /*{<Symbol::\void(), Symbol::\rel(ts)>, <Symbol::\rel(ts), Symbol::\void()>} + */ {<Symbol::\rel([l]), Symbol::\rel([r])> | a <- rlub(ts), <l, r> <- a };
//
//set[SymbolPair] rlub(Symbol::\list(Symbol s)) = /* {<Symbol::\void(), Symbol::\list(s)>, <Symbol::\list(s), Symbol::\void()>} + */{<Symbol::\list(l), Symbol::\list(r)> | <l, r> <- rlub(s)};
//set[SymbolPair] rlub(Symbol::\lrel(list[Symbol] ls)) = /* {<Symbol::\void(), Symbol::\lrel(s)>, <Symbol::\lrel(s), Symbol::\void()>} + */ {<Symbol::\lrel([l]), Symbol::\lrel([r])> | a <- rlub(ls), <Symbol l, Symbol r> <- a};
//
//set[SymbolPair] rlub(Symbol::\map(Symbol k, Symbol v)) = /* {<Symbol::\void(), Symbol::\map(k, v)>, <Symbol::\map(k, v), Symbol::\void()>} + */ {<Symbol::\map(kl, vl), Symbol::\map(kr, vr)> | <kl, kr> <- rlub(k), <vl, vr> <- rlub(v) };
//
//set[SymbolPair] rlub(Symbol::\tuple(list[Symbol] ts)) = /* {<Symbol::\void(), Symbol::\tuple(ts)>, <Symbol::\tuple(ts), Symbol::\void()>} + */ {<Symbol::\tuple([l]), Symbol::\tuple([r])> | a <- rlub(ts), <l, r> <- a};
//
//default set[SymbolPair] rlub(Symbol s) = {<s, s>};
//
//list[set[SymbolPair]] rlub([]) = [];
//list[set[SymbolPair]] rlub([Symbol s]) = [rlub(s)];
//list[set[SymbolPair]] rlub([Symbol s, *Symbol sl]) = [rlub(s), *rlub(sl)];
//

