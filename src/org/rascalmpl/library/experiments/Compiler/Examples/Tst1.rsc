module experiments::Compiler::Examples::Tst1

//import Grammar;
//import ParseTree;
//  
//alias Items = map[Symbol,map[Item item, tuple[str new, int itemId] new]];
//
//map[Symbol,map[Item,tuple[str new, int itemId]]] generateNewItems(Grammar g) {
//  map[Symbol,map[Item,tuple[str new, int itemId]]] items = ();
// 
//  visit (g) {
//    case Production p:regular(Symbol s) : {
//         
//      switch(s) {
//        case \iter(Symbol elem) : 
//          123; //items[s]?fresh += (); //(item(p,0):sym2newitem(g, elem, 0));
//      }
//     }
//  }
//  
//  return items;
//}

//data D = d(int i) | d();
//  
//D d(int i) { if (i % 2 == 0) fail d; else return d();}
//
//int sw4(value e){   
//    int n = 0;
//    switch(e){
//        case [1,2,3,4]:     n = 8;
//        case [1,2,3]:       n = 7;
//        case [1,2,3,4,5]:   n = 80;
//        default:            n = 9;
//    }
//    return n;
// }
// 
// value main() = sw4([1,2,3]) ;
// 
//value main(){
//    visit([1,2,3]){
//        case 3 => 30
//    }
//}


//value main(){
//  x = 7;
//  switch(1){case 0: x = 0; case 1: x = 1;}
//  return x;
//}

//value main(){
//  x = 7;
//  switch(1){case 0: x = 0;};
//}

//value main(){
//  x = 7;
//  switch(5){case 0: x = 0; case 1: x = 1; default: x = 2;}
//  return x;
//}

value main(){
  x = 7;
  switch(5){case 0: x = 0; case 1: x = 1; default: x = 2;}
}

//value main(){
//  x = 7;
//  switch(5){case 0: 0; case 1: 1; default: 2;}
//}

//int sw(int n) { switch(n){case 0: return 0; case 1: return 1; default: return 2;} }
//
//test bool tst() = run("x = 7" , "switch(1){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(1);
//
//test bool tst() = run("x = 7" , "switch(2){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(2);
//
//test bool tst() = run("x = 7" , "switch(0){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(0);

//value main()
//{ int n = 0; 
//  switch([1,2,3,4,5,6]) { 
//  case [*int x, *int y]: { n += 1; fail; } 
//  case list[int] _ : { n += 100; } 
//  } 
//  n;
//}
//
//data E = e() | e(int n) | e(str s, int m);
// 
// int sw5(value v){
//    int n = 0;
//    switch(v){
//        case "abc":                             n = 1;
//        case e(/<s:^[A-Za-z0-9\-\_]+$>/, 2):    n = 2;
//        case e(/<s:^[A-Za-z0-9\-\_]+$>/, 3):    n = 3;
//        case 4:                                 n = 4;
//        case e():                               n = 5;
//        case e(int x):                          n = 6;
//        case str s(7):                          n = 7;
//        case [1,2,3]:                           n = 8;
//        case [1,2,3,4]:                         n = 9;
//        case e("abc", 10):                      n = 10;
//        case e("abc", int x):                   n = 11;
//        case node nd:                           n = 12;
//        default:                                n = 13;
//    }
//    return n;
//}

//value main() = sw5([1,2,3, 4]);//   == 9;
//value main() =  sw5(e("abc",10));
//value main() = sw5(e("abc",11)) == 11;
//value main() = sw5(e("abc",3)) == 3;

//test bool testSwitch5a() = sw5("abc")       == 1;
//test bool testSwitch5b() = sw5(e("abc",2))  == 2;
//test bool testSwitch5b() = sw5(e("abc",3))    == 3;
//test bool testSwitch5e() = sw5(4)           == 4;
//test bool testSwitch5f() = sw5(e())         == 5;
//test bool testSwitch5g() = sw5(e(6))        == 6;
//test bool testSwitch5h() = sw5(e(7))        == 6;
//test bool testSwitch5i() = sw5("f"(7))      == 7;
//test bool testSwitch5j() = sw5([1,2,3])     == 8;
//test bool testSwitch5k() = sw5([1,2,3,4])   == 9;
//test bool testSwitch5l() = sw5(e("abc",10)) == 10;
//test bool testSwitch5m() = sw5(e("abc",11)) == 11;
//test bool testSwitch5n() = sw5("f"(12))     == 12;
//test bool testSwitch5o() = sw5(13)          == 13;


//int sw6(value v){
//    int n = 0;
//    switch(v){
//        case {1,2,3}:                           n = 10;
//        case {<1,2,3>}:                         n = 11;
//       
//        default:                                n = 13;
//    }
//    return n;
//}
//value main() = sw6({<1,2,3>});//   == 11;

// int fn(int n) { 
//  switch(n) { 
//      case 0: return 1; 
//      case 1: return 1; 
//      case int m: return m*(m-1); 
//  } }
//                              
//value main() = fn(0);                       
   
   
//public bool hasDuplicateElement(list[int] L)
//  {
//    switch(L){
//    
//    case [*int L1, int I, *int L2, int J, *int L3]:
//        if(I == J){
//            return true;
//        } else {
//            fail;
//        }
//    default:
//        return false;
//      }
//  }
//value main() = hasDuplicateElement([]);      


// public bool isDuo2(list[int] L)
//  {
//    switch(L){
//    case [*int L1, *L1]:
//            return true;
//    default:
//        return false;
//      }
//  }  
//  
//value main() = isDuo2([1]);       


//value main(){
//  x = -1;
//  visit([1,2,3]){
//    case 1: x += 1;
//    case 2: x += 2;
//    case int n: 
//          switch(n){case 0: x += 1; case 1: x += 1;}
//    }
//  return x;
//}

//data NODE = nd(NODE left, NODE right) | leaf(int n);
//anno int NODE@pos;
//NODE N1 = nd(leaf(0)[@pos=0], leaf(1)[@pos=1])[@pos=2];
//
//value main() {
//    return visit(N1){
//        case leaf(0) => leaf(0)
//        case leaf(1) => leaf(10)
//        default:;
//    }
//    //==
//    //nd(leaf(0), leaf(10))[@pos=2];
//}

//import lang::rascal::\syntax::Rascal;
//
//value main(){
//    n = 0;
//    r = (RegExp) `\\\\`;
//    switch(r){
//      case (RegExp) `\<<Name name>\>`:
//        n = 1;
//      case (RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`: {
//       n = 2;
//        }
//      default:
//        n = 3;
//    }
//    return n;
//} 

//str deescape(str s) = visit(s) { case /\\<c: [\" \' \< \> \\ b f n r t]>/m => c };
//
//test bool StringVisit71() = deescape("abc") == "abc";
//test bool StringVisit72() = deescape("\\") == "\\";
//test bool StringVisit73() = deescape("\\\\") == "\\";
//test bool StringVisit74() = deescape("\\\<") == "\<";
//test bool StringVisit75() = deescape("\\\>") == "\>";
//test bool StringVisit76() = deescape("\\n") == "n";
       