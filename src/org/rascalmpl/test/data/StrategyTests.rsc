module StrategyTests
	
import IO;
import Strategy;
import TopologicalStrategy;

data A = f(B I, B J)
       | a()
       | d()
       | e()
       | aa()
       | dd()
       | ee()
       | h(A a);

       
data B = g(B I)
       | b()
       | c();

public &T(&T) rules = &T(&T t) {
    switch (t) {
	case g(b()): return b();
	default: return t;
    };
 };

public &T(&T) rules2 = &T(&T t) {
    switch (t) {
	case c(): return b();
	case g(c()): return c();
	default: return t;
    };
 };

public &T(&T) rules3 = &T(&T t) {
    switch (t) {
	case b(): return c();
	default: return t;
    };
 };



public B rules4(B t) {
   switch (t) {
	case b(): return c();
	default: return t;
   };
}


public &T rules5(&T t) {
   switch (t) {
	case a(): return aa();
	case d(): return dd();
	case e(): return ee();
	default: return t;
   };
}


public &T rules6(&T t) {
   switch (t) {
	case a(): return aa();
	case d(): return dd();
	case e(): return ee(); 
        case aa(): return h(aa());
	case dd(): return h(dd());
	case ee(): return h(ee());
        default: return t;
   };
}
