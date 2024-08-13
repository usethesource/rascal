module lang::rascalcore::compile::Examples::Tst4

 
import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+ ds;
//start syntax E = "e";
//start syntax ES = {E ","}+ args;

int cntDS(D+ ds) = size([d | d <- ds ]);

value main() //test bool cntDS1() 
    = cntDS(((DS) `d`).ds) == 1;
    
//test bool cntDS2() = cntDS(((DS) `dd`).ds) == 2;
//test bool cntDS3() = cntDS(((DS) `d d`).ds) == 2;
//
//int cntES({E ","}+ es) = size([e | e <- es ]);
//
//test bool cntES1() = cntES(((ES) `e`).args) == 1;
//test bool cntES2() = cntES(((ES) `e,e`).args) == 2;
//test bool cntES3() = cntES(((ES) `e ,e`).args) == 2;
//test bool cntES4() = cntES(((ES) `e, e`).args) == 2;
//test bool cntES5() = cntES(((ES) `e , e`).args) == 2;   


 
//int f(list[int] ds){
//    if([*a, xxx, *b]:= ds){
//        return 1;
//    } else {
//        return 2;
//    }
//}

//import lang::rascal::\syntax::Rascal;
//import lang::rascal::grammar::definition::Symbols;
//import lang::rascal::grammar::definition::Attributes;
//import lang::rascal::grammar::definition::Names;
//extend Grammar;
//extend ParseTree;   
//import util::Maybe;
//  
//private Production prod2prod(Symbol nt, Prod p) {
//    Production pp;
//  //switch(p) {
//    if(labeled(ProdModifier* ms, Name n, Sym* args) := p){ 
//      if ([Sym x] := args.args) {
//        return associativity(nt, \mods2assoc(ms), prod(label(unescape("<n>"),nt), [], mods2attrs(ms)));
//      }
//      else {
//        return associativity(nt, \mods2assoc(ms), prod(label(unescape("<n>"),nt), args2symbols(args), mods2attrs(ms)));
//      }
//    } else {
//      return pp;
//    }
//    //case unlabeled(ProdModifier* ms, Sym* args) :
//    //  if ([Sym x] := args.args, x is empty) {
//    //    return associativity(nt, mods2assoc(ms), prod(nt, [], mods2attrs(ms)));
//    //  }
//    //  else {
//    //    return associativity(nt, mods2assoc(ms), prod(nt,args2symbols(args),mods2attrs(ms)));
//    //  }     
//    //case \all(Prod l, Prod r) :
//    //  return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
//    //case \first(Prod l, Prod r) : 
//    //  return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
//    //case associativityGroup(\left(), Prod q) :
//    //  return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
//    //case associativityGroup(\right(), Prod q) :
//    //  return associativity(nt, Associativity::\right(), {prod2prod(nt, q)});
//    //case associativityGroup(\nonAssociative(), Prod q) :      
//    //  return associativity(nt, \non-assoc(), {prod2prod(nt, q)});
//    //case associativityGroup(\associative(), Prod q) :      
//    //  return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
//    //case reference(Name n): return \reference(nt, unescape("<n>"));
//   // default: throw "prod2prod, missed a case <p>"; 
// // } 
//  
//}
//
//
//
//private Production associativity(Symbol nt, nothing(), Production p) = p;
//private default Production associativity(Symbol nt, just(Associativity a), Production p) = associativity(nt, a, {p});
