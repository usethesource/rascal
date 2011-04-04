@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::TestStrategy
	
import IO;
import Strategy;
import TopologicalStrategy;
import UnitTest;

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

&T(&T) rules = &T(&T t) {
    switch (t) {
	case g(b()): return b();
	default: return t;
    };
 };

&T(&T) rules2 = &T(&T t) {
    switch (t) {
	case c(): return b();
	case g(c()): return c();
	default: return t;
    };
 };

&T(&T) rules3 = &T(&T t) {
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

public void main() {
     A t = f(g(g(b())),g(g(b())));
     assertEqual(top_down(rules)(t), f(g(b()),g(b())));
     assertEqual(bottom_up(rules)(t), f(b(),b()));
	
     B t2 = g(c());
     assertEqual(once_top_down(rules2)(t2), c());
     assertEqual(once_bottom_up(rules2)(t2), g(b()));
 
     list[B] l = [g(c()),c()];
     assertEqual(makeAll(rules2)(l),[c(),b()]);
 
     tuple[A,B] t3 = <a(),c()>;
     assertEqual(top_down(rules2)(t3),<a(),b()>);
	
     rel[A, B] r = {<a(), b()>, <f(b(),b()), c()>};
     assertEqual(top_down(rules3)(r),{<a(), c()>, <f(c(),c()), c()>});

     A t4 = f(g(b()),g(b()));
     assertEqual(top_down(functionToStrategy(rules4))(t4),f(g(c()),g(c())));

     assertEqual(innermost(rules)(t), f(b(),b()));
     assertEqual(outermost(rules)(t), f(b(),b()));

     rel[A, A] r2 = {<a(), d()>, <a(), e()>, <d(), e()>};
     assertEqual(topological_once_bottom_up(rules5)(r2), {<a(),ee()>,<a(),d()>,<d(),ee()>});
     assertEqual(topological_once_top_down(rules5)(r2), {<aa(),e()>,<aa(),d()>,<d(),e()>});

     assertEqual(topological_top_down(rules5)(r2), {<aa(), dd()>, <aa(), ee()>, <dd(),ee()>}); 
  
     assertEqual(topological_bottom_up(rules5)(r2), {<aa(), dd()>, <aa(), ee()>, <dd(),ee()>});
     assertEqual(topological_innermost(rules6)(r2), {<h(aa()), h(dd())>, <h(aa()), h(ee())>, <h(dd()),h(ee())>});

     assertEqual(topological_outermost(rules6)(r2), {<h(aa()), h(dd())>, <h(aa()), h(ee())>, <h(dd()),h(ee())>});
     
     // test Top-Down in a cyclic relation
     rel[A, A] r3 = {<a(), d()>, <d(), e()>, <e(), d()>};
     list[value] elts = [];
     &T(&T) collect = &T(&T t) { 
      elts += [t];
      return t;
     };
     topological_top_down(collect)(r3);
     assertEqual(elts,[{<e(),d()>,<d(),e()>,<a(),d()>},a(),d(),e(),d()]);
     report("Strategies");

}
