module lang::rascal::tests::basic::Functions

import List;
import Node;
import Exception;

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();

B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

value callDelAnnotations() = unset("f"(1,2,3));

test bool testCallWithTypeParameterBound() = callDelAnnotations() == "f"(1,2,3);

test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);

test bool normalizedVisit() =
  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };
  
B (B, B) giveOr() = or; 

test bool giveOr1() = giveOr()(t(), f()) == or(t(), f());

private test bool callKwp() {
  kwp(x = 2); // this would previously change the static type of the x argument of kwp to int
  return true;
}

private void kwp(value x = 1) {
  // this is a regression test for a bug;
  x = "node"(); // if the static type of the kwp is value, then we should be allowed to assign a node into it.
}

private str g(str s) = inter when str inter := s;

test bool functionWithWhen() {
    return g("Called!") == "Called!";
}

private str fn(str s, int ints..., str kw = "keyword") = s + "-" + intercalate("-", ints) + "-" + kw;
	
test bool functionWithVarargsAndKeyword1() = fn("a") == "a--keyword";
test bool functionWithVarargsAndKeyword2() = fn("b",kw="xxx") == "b--xxx";
test bool functionWithVarargsAndKeyword3() = fn("c",1,2,3)== "c-1-2-3-keyword";
test bool functionWithVarargsAndKeyword4() = fn("d",1,2,3,kw="xxx") == "d-1-2-3-xxx";

int fvarargs(str _, value _...) = 0;
int fvarargs(int _, str s, value _...) = 1;
int fvarargs(bool _, int _, str _, value _...) = 2;

test bool overloadedWithVarArgs1() = fvarargs("a") == 0;
test bool overloadedWithVarArgs2() = fvarargs("a", 1) == 0;
test bool overloadedWithVarArgs3() = fvarargs("a", 1, 2) == 0;

test bool overloadedWithVarArgs4() = fvarargs(10, "a") == 1;
test bool overloadedWithVarArgs5() = fvarargs(10, "a", 1) == 1;
test bool overloadedWithVarArgs6() = fvarargs(10, "a", 1, 2) == 1;

test bool overloadedWithVarArgs7() = fvarargs(true, 10, "a") == 2;
test bool overloadedWithVarArgs8() = fvarargs(true, 10, "a", 1) == 2;
test bool overloadedWithVarArgs9() = fvarargs(true, 10, "a", 1, 2) == 2;

int f1() {
    int g1() = 101;
    int () k1() {
        int () h1() = g1;
        return h1();
    };
    return k1()();
}

value nestedFunctions1() {
    return f1();
}

str f2(0) { res = g2("0"); return "f2(0); " + res; }
str f2(1) { res = g2("1"); fail;}
default str f2(int n) { res = g2("<n>"); return "default f2(1);" + res; }

str g2("0") = "g2(\"0\")";
str g2("1") = "g2(\"1\")";
default str g2(str s) = "default g2(<s>);";

test bool nestedFunctionCall1() = f2(0)  =="f2(0); g2(\"0\")";
test bool nestedFunctionCall2() = f2(1) == "default f2(1);g2(\"1\")";
test bool nestedFunctionCall3() = f2(5)  == "default f2(1);default g2(5);";

str f3(0) { res = g3("0"); return "f3(0); " + res; }
str f3(1) { 
    str res = "***init***"; 
    try { 
        res = g3("1"); 
    } catch str s: { 
        res = "catched(<s>); g3(\"1\")"; 
    }; 
    fail; 
}
default str f3(int n) { 
    str res = "***init***"; 
    try {
        res = g3("<n>");
    } catch str s: {
        res = "catched(<s>); g3(<n>)";
    }
    return "default f3(<n>); " + res; 
}

str g3("0") = "g3(\"0\")";
str g3("1") { throw "Try to catch me!!!"; }
default str g3(str s) = "default g3(<s>)";

test bool nestedFunctions3() {
    return f3(0) + " ;; " + f3(1) + " ;; " + f3(5)
    == "f3(0); g3(\"0\") ;; default f3(1); catched(Try to catch me!!!); g3(1) ;; default f3(5); default g3(5)";
}

int () g4() {
    int k4() = 666;
    int h4() = k4();
    return h4;
}

test bool nestedFunctions4() {
    return g4()() == 666;
}

public int (int) f5() { int n = 100; return int (int i) { return i + n; }; }

public int (int) h5(int n1) { int n2 = 50; int k5(int i) { return n1 + n2 + i; } return k5; } 

test bool capture1() {
	g5 = f5();
	res1 = g5(11);
	
	l5 = h5(1);
	res2 = l5(2);
	
	return res1 + res2 == 111 + 53;
}

test bool closure1() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
    list[int] outputs = [ int (int n) { 
                              switch(n) { 
                                  case 0: return 1; 
                                  case 1: return 1; 
                                  case int m: return m*(m-1); 
                                  default: return -1;
                              } 
                          } /* renamed n to m*/
                          ( int (int n) { 
                                switch(n) { 
                                    case 0: return 0; 
                                    case 1: return 1; 
                                    case int m: return (m-1) + (m-2); 
                                    default: return -1;
                                } 
                            } /* renamed n to m*/ (i)
                           ) | int i <- inputs ]; 
    return outputs ==  [1,1,1,6,20,42,72,110,156,210];
}


private &T something(set[&T] x) {
   if (e <- x) 
     return e;

   fail;
}

private default value something({}) = "hello";

test bool parameterizedFunctionsDoNotReturnVoid() {
  set[value] emptySet = {};
  return "hello" == something(emptySet);
}

test bool functionTypeArgumentVariance1() =
  int (value _) _ := int (int x ) { return 1; };
  
test bool functionTypeArgumentVariance2() =
  int (int _) _ := int (value x ) { return 1; };
  
test bool functionTypeArgumentVariance3() {
  value f = int (str x ) { return 1; };
  return int (int _) _ !:= f;
} 

test bool higherOrderFunctionCompatibility1() {
   // the parameter function is specific to int
   int parameter(int _) { return 0; }
   
   // the higher order function expects to call the
   // parameter function with other things too
   int hof(int (value) p, value i) { return p(i); }
   
   // still this is ok, since functions in Rascal
   // are partial. This call should simply succeed:
   if (hof(parameter, 1) != 0) {
     return false;
   }
   
   // but the next call produces a CallFailed, since
   // the parameter function is not defined on strings:
   try {
     // statically allowed! but dynamically failing
     hof(parameter, "string");
     return false;
   } 
   catch CallFailed(_):
     return true; 
}

test bool higherOrderFunctionCompatibility2() {
   // the parameter function is very generic
   int parameter(value _) { return 0; }
   
   // the higher order function expects to call the
   // parameter function with only integers
   int hof(int (int) p, value i) { return p(i); }
   
   // this is ok, a more generic function should be
   // able to handle ints. This call should simply succeed:
   if (hof(parameter, 1) != 0) {
     return false;
   }
   
   return true;
}

@ignore{this fails also in the interpreter because the algorithm
for binding type parameter uses `match` in two directions which 
implements comparability rather than intersectability}
test bool higherOrderFunctionCompatibility3() {
   // the parameter function is specific for tuple[int, value]
   int parameter(tuple[int, value] _) { return 0; }
   
   // the higher order function expects to call the
   // parameter function with tuple[value, int]
   int hof(int (tuple[value, int]) p, tuple[value,int] i) { return p(i); }
   
   // this is ok, the parameter function's type has a non-empty
   // intersection at tuple[int, int], so at least for such 
   // tuples the function should succeed
   if (hof(parameter, <1,1>) != 0) {
     return false;
   }
   
   // however, when called with other tuples the parameter fails
   // at run-time:
   try {
     // statically allowed! but dynamically failing
     hof(parameter, <"1", 1>);
     return false;
   } 
   catch CallFailed(_):
     return true; 
     
   return false;
}

test bool higherOrderVoidFunctionCompatibility() {
   bool hof (void(int s) g) { g(0); return true; }
   void ff(int _) { return; }
   
   try {
     return hof(ff);
   }  
   catch CallFailed(_): 
     return false;
}

test bool returnOfAnInstantiatedGenericFunction() {
    &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    int add(int i, int j) = i + j;

    // issue #1467 would not allow this assignment because the 
    // returned closure was not instantiated properly from `&S (&U)` to `int(int)`
    int (int) f = curry(add, 1); 

    return f(1) == 2 && (f o f)(1) == 3;
}

test bool curryAConstructor() {
    &S(&U) c(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    B (B) f = c(and, t());

    return f(t()) == and(t(), t());
}

test bool selfApplyCurry() {
    &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    int addition(int i, int j) = i + j;

    func = curry(curry, addition);

    assert int(int)(int) _ := func;

    func2 = func(1);

    assert int(int) _ := func2;

    return func2(1) == 2;
}

test bool accessParameterInClosure(){

    int() make(int x) = int() { return x; };
    
    int () use(int n)  = make(n);
    
    return use(3)() == 3;

}

test bool assignVariableInNestedFunctions(){
    int X = 0;
    int Y = 0;
    
    int incX() { X += 1; return X; }
    int incY() { Y += 1; return Y; }
    
    int incXY() = incX() + incY();

    incX(); incX(); incX();
    incY(); incY();
    
    return incXY() == 7;
}

test bool assignVariableInNestedFunctionWithVisit() {  
    int uniqueItem = 1;
    int newItem() { uniqueItem += 1; return uniqueItem; };
    
    list[str] rewrite(list[str] p) = 
      visit (p) { 
        case "a": newItem();
        case "b": newItem();
      };
    return rewrite(["a", "b", "c"]) == ["a", "b", "c"] && uniqueItem == 3;
} 

int container(int n){

    int f() = g();
    
    int g() = h();
    
    int h() = n + 1;
    
    return f();
}

test bool sharedFormal() = container(12) == 13;

test bool assignVariableInNestedFunctions1() {
    int x = 1;
    
    void setX() { x = 10; }
    
    setX();
    return x == 10;
}

test bool assignVariableInVisit1() {
    int x = 1;
    
    visit([1,2,3]){ case 1: x = 10; }
    return x == 10;
}

test bool assignVariableInNestedFunctions2(int x) {
    
    void setX() { x = 10; }
    
    setX();
    return x == 10;
}

test bool assignVariableInVisit2(int x) {
    
    visit([1,2,3]){ case 1: x = 10; }
   
    return x == 10;
}


test bool assignVariableInNestedFunctions3() {
    int x = 1;
    int y = 100;
    
    void setX() { x = 10; }
    void setY() { y = 20; }
    
    x1 = x; y1 = y;
    setX();
    if(x != 10 || y != y1) return false;
    
    x1 = x; y1 = y;
    setY();
    if(x != x1 || y != 20 ) return false;
    
    return true;
}

test bool assignVariableInVisit3() {
    int x = 1;
    int y = 100;
    
    x1 = x; y1 = y;
    visit([1,2,3]){ case 1: x = 10; }
    if(x != 10 || y != y1) return false;
    
    x1 = x; y1 = y;
    visit([1,2,3]){ case 1: y = 20; }
    if(x != x1 || y != 20 ) return false;
    
    return true;
}

test bool assignVariableInNestedFunctions4() {
    int x = 1;
    int y = x;
    
    void setX() { x = 10; }
    void setY() { y = 20; }
    
    x1 = x; y1 = y;
    setX();
    if(x != 10 || y != y1) return false;
    
    x1 = x; y1 = y;
    setY();
    if(x != x1 || y != 20 ) return false;
    
    return true;
}

test bool assignVariableInVisit4() {
    int x = 1;
    int y = x;
    
    x1 = x; y1 = y;
    visit([1,2,3]){ case 1: x = 10; }
    if(x != 10 || y != y1) return false;
    
    x1 = x; y1 = y;
    visit([1,2,3]){ case 1: y = 20; }
    if(x != x1 || y != 20 ) return false;
    
    return true;
}
    
test bool assignVariableInNestedFunctions5(int x) {
    int y = x;
    int z = 3;
    
    void setX() { x = 10; }
    void setY() { y = 20; }
    void setZ() { z = 30; }
    
    x1 = x; y1 = y; z1 = z;
    setX();
    if(x != 10 || y != y1 || z != z1) return false;
    
    x1 = x; y1 = y; z1 = z;
     
    setY();
    if(x != x1 || y != 20 || z != z1) return false;
    x1 = x; y1 = y; z1 = z;
    setZ();
     
    if(x != x1 || y != y1 || z != 30) return false;
    
    return true;
}

test bool assignVariableInVisit5(int x) {
    int y = x;
    int z = 3;
    
    x1 = x; y1 = y; z1 = z;
    visit([1,2,3]){ case 1: x = 10; }
    if(x != 10 || y != y1 || z != z1) return false;
    
    x1 = x; y1 = y; z1 = z;
     
    visit([1,2,3]){ case 1: y = 20; }
    if(x != x1 || y != 20 || z != z1) return false;
    x1 = x; y1 = y; z1 = z;
    visit([1,2,3]){ case 1: z = 30; }
     
    if(x != x1 || y != y1 || z != 30) return false;
    
    return true;
}

data D = one_d(int(int a) n);

test bool assignVariableViaFunctionValue(){
    set[int] x = {};
    
    int add (int n) { x += n; return n;}

    D make() {return one_d(add);}
    
    m = make(); 
    m.n(3);
    m.n(4);
    return x == {3, 4};
}

test bool namedParameterInClosure(){             
    int collect(current: int n){
        return  int () { return current; }();
    }
    return collect(5) == 5;
}

data E = e(int n);

int useExtraFormalInConstructor(e(int n)){
    int g() { return n; }
    return g();
} 

test bool useExtraFormalInConstructor1() = useExtraFormalInConstructor(e(42)) == 42;

data F = f(list[int] ints);

int useExtraFormalInListPattern(f([*int ints])){
    int g() { return ints[0]; }
    return g();
} 

test bool useExtraFormalInListPattern1() = useExtraFormalInListPattern(f([42])) == 42;

int _f(int n) = n;

test bool functionNameStartsWithUnderscore() = _f(13) == 13;

test bool innerAndOuterFunctionUseSameParameterName1(){
    int outer(int t) {
        int inner(t:3) = t;
        default int inner(int t) = 10*t;
        return inner(t);
    }
    
    return outer(3) == 3 && outer(5) == 50;
}

test bool innerAndOuterFunctionUseSameParameterName2(){
    int outer(int t) {
        int inner(int t:3) = t;
        default int inner(int t) = 10*t;
        return inner(t);
    }
    
    return outer(3) == 3 && outer(5) == 50;
}

@ignoreCompiler{"Return type `int` expected, found `str`"}
test bool innerAndOuterFunctionUseSameParameterName3(){
    int outer(str t) {
        int inner(t:3) = t;
        default int inner(int t) = 10*t;
        return inner(3);
    }
    
    return outer("a") == 30;
}

test bool innerAndOuterFunctionUseSameParameterName4(){
    int outer(str t) {
        int inner(int t:3) = t;
        default int inner(int t) = 10*t;
        return inner(3);
    }
    
    return outer("a") == 3;
}

test bool stackoverflow() {
    int f(int i) = f(i);

    try {
        f(1);
        return false;
    }
    catch StackOverflow():
        return true;
}