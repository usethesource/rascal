module lang::rascal::tests::basic::Functions

import List;
import Node;

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();

B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

value callDelAnnotations() = delAnnotations("f"(1,2,3));

test bool testCallWithTypeParameterBound() = callDelAnnotations() == "f"(1,2,3);

test bool normalizedCall(B b1, B b2, B b3) = and(b1, and(b2, b3)) == and(and(b1, b2),b3);

test bool normalizedVisit() =
  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };
  
private test bool callKwp() {
  kwp(x = 2); // this would previously change the static type of the x argument of kwp to int
  return true;
}

private void kwp(value x = 1) {
  // this is a regression test for a bug;
  x = "node"(); // if the static type of the kwp is value, then we should be allowed to assign a node into it.
}

private str g(str s) = inter when str inter := s;

test bool finctionWithWhen() {
    return g("Called!") == "Called!";
}


private str fn(str s, int ints..., str kw = "keyword") = s + "-" + intercalate("-", ints) + "-" + kw;

test bool functionWithVarargsAndKeyword() = 
	fn("a") + ";" + fn("b",kw="xxx") + ";" + fn("c",1,2,3) + ";" + fn("d",1,2,3,kw="xxx")
	== "a--keyword;b--xxx;c-1-2-3-keyword;d-1-2-3-xxx";

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
str f2(1) { res = g2("1"); fail; return "f2(1); " + res; }
default str f2(int n) { res = g2("<n>"); return "default f2(1);" + res; }

str g2("0") = "g2(\"0\")";
str g2("1") = "g2(\"1\")";
default str g2(str s) = "default g2(<s>);";

test bool nestedFunctions2() {
    return f2(0) + ";; " + f2(1) + ";; " + f2(5) 
    == "f2(0); g2(\"0\");; default f2(1);g2(\"1\");; default f2(1);default g2(5);";
}

str f3(0) { res = g3("0"); return "f3(0); " + res; }
str f3(1) { 
    str res; 
    try { 
        res = g3("1"); 
    } catch str s: { 
        res = "catched(<s>); g3(\"1\")"; 
    }; 
    fail; 
    return "f3(1); " + res; 
}
default str f3(int n) { 
    str res; 
    try {
        res = g3("<n>");
    } catch str s: {
        res = "catched(<s>); g3(<n>)";
    }
    return "default f3(<n>); " + res; 
}

str g3("0") = "g3(\"0\")";
str g3("1") { throw "Try to catch me!!!"; return "g3(\"1\")"; }
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
                              } 
                          } /* renamed n to m*/
                          ( int (int n) { 
                                switch(n) { 
                                    case 0: return 0; 
                                    case 1: return 1; 
                                    case int m: return (m-1) + (m-2); 
                                } 
                            } /* renamed n to m*/ (i)
                           ) | int i <- inputs ]; 
    return outputs ==  [1,1,1,6,20,42,72,110,156,210];
}
