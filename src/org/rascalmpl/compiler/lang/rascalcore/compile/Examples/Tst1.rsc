module lang::rascalcore::compile::Examples::Tst1
   
public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
    <[t | <t,_> <- lst], [u | <_,u> <- lst]>;
  
// Make a triple of lists from a list of triples.
public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
    <[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;
        
public list[int] index(list[&T] lst) = [0];
         
@javaClass{org.rascalmpl.library.Prelude}
public java map[&K, set[&V]] index(lrel[&K, &V] R);
     
////test bool  dispatchTest3() { 
////    int f(/[a-z]+/) = 1;
////    int f(/[0-9]+/) = 2;
////    return f("abc") == 1 && f("123") == 2;
////}
// 
//test bool  dispatchTest4() { 
//    str f(/X<v:[a-z]+>Y/) = v;
//    str f(/X<v:[0-9]+>Y/) = v;
//    return f("XabcY") == "abc" && f("X123Y") == "123";
//}
// 
////str f(/X<v:[a-z]+>Y/) = v;
////str f(/X<v:[0-9]+>Y/) = v;
////value main() = f("XabcY");// == "abc" ;
//  
//data X = xx() | yy() | zz();    
//  
// // dispatchTest1
//  


test bool  dispatchTest1() { 
    int f(xx()) = 1;
    int f(yy()) = 2;
    int f(zz()) = 3;
    return [f(xx()),f(yy()),f(zz())] == [1,2,3];
}
  
            
public rel[&K,&V] toRel(map[&K,set[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
public rel[&K,&V] toRel(map[&K,list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]};
@javaClass{org.rascalmpl.library.Prelude}
public default java rel[&K, &V] toRel(map[&K, &V] M);

   
data RuntimeException = 
       IllegalArgument()                        // deprecated
     | IllegalArgument(value v)                 // deprecated
     | IllegalArgument(value v, str message)    // deprecated
     ;
          
test bool max2() { 
    int max(int a, int b) { return a > b ? a : b; } 
    real max(real a, real b) { return a > b ? a : b; }
    return max(3,4) == 4 && max(3.0,4.0) == 4.0;
}  
//        
@javaClass{org.rascalmpl.library.Prelude}
public java int size(list[&T] lst);
              
@javaClass{org.rascalmpl.library.Prelude}
public java int size(map[&K, &V] M);
    
            
int translateConstantCall(str name, list[value] args) =
    tcc(name, args);

private int tcc("value", []) = 0;
private int tcc("value", list[int] L) = 1 when size(L) == 1;
private int tcc("value", list[int] L) = 2 when size(L) == 2;

private default int tcc(str name, list[value] args) { return -1;}
       
                 
 data Symbol
     = \value()
     ;  
    
Symbol lub(Symbol s, s) = s;
default Symbol lub(Symbol s, Symbol t) = \value();
 
Symbol lub(\value(), Symbol t) = Symbol::\value();
 
        list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [\value()];
default list[Symbol] lub(list[Symbol] l, list[Symbol] r) = [];
 