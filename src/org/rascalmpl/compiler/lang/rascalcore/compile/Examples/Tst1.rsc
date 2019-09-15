module lang::rascalcore::compile::Examples::Tst1         
//                                                   
//data Symbol
//    = \void()
//    ;
//public bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);
//public default bool subtype(Symbol s, Symbol t) = false;
//    
//public bool subtype(\void(), Symbol _) = true;
   
         
//data Symbol;
//  
// data  Production
//    = \choice(Symbol def, set[Production] alternatives)  
//    ;
//           
//public Production choice(Symbol s, set[Production] choices){
//    fail;
//}           
// data Symbol
//   = \void();
//         
//public Symbol glb(Symbol s, s) = s;
//public default Symbol glb(Symbol s, Symbol t) = \void();
//
//public list[Symbol] glb(list[Symbol] l, list[Symbol] r) = [];
//public default list[Symbol] glb(list[Symbol] l, list[Symbol] r) = []; 
//  
// void main(){
//    glb([\void()], [\void()]);
// }
   
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java datetime incrementYears(datetime dt, int n);
//public datetime incrementYears(datetime dt) {
//  return incrementYears(dt,1);
//}

//                              
// public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
//	<[t | <t,_> <- lst], [u | <_,u> <- lst]>;
//
//// Make a triple of lists from a list of triples.
//public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
//	<[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;
 
	
@javaClass{org.rascalmpl.library.Prelude}
public java &T head(list[&T] lst);

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] head(list[&T] lst, int n);