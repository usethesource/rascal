@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::TreeTraversals
import List;
import IO;

// Various examples of tree traversal and replacement

data NODE = i(int N)
          | f(NODE I, NODE J) 
          | g(NODE I, NODE J)
          | h(NODE I, NODE J) 
          | h(NODE I, NODE J, NODE K);


// Example cnt: Count the int nodes in a tree

//cnta: Collect all integers in the tree in 
// a list and return the size of the list

public int cnta(NODE T) {
    return size([N | /int N <- T]);
}

// cntb: alternative solution using a visit statement

public int cntb(NODE T) {
    int C = 0;
    visit(T) {
      case int N: C = C + 1;
    };
    return C;
}

// sumtree: Sum all leaves in a tree

// sumtreea: Collect all integers in the tree in 
// a list and use the library function sum on lists
// to add all list elements together.

public int sumtreea(NODE T) {
    int C = 0;
    for(/int N <- T){
      C += N;
    }
    return C;
}

// sumtreeb: same problem, now using visit statement

public int sumtreeb(NODE T) {
    int C = 0;
    visit(T) {
      case int N : C = C + N;
    };
    return C;
}

// inc: Increment all leaves in a tree

public NODE inc(NODE T) {
    return visit(T) {
      case int N: insert N + 1;
    };
}

// frepl: full replacement of g by h, i.e.
// replace all nodes g(_,_) by h(_,_)

// frepla: Using insert

public NODE frepla(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2):
           insert h(T1, T2);
    };
}

// freplb: Using replacement rule

public NODE freplb(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2) => h(T1, T2)
    };
}

// freplG3H3: Full replacement, replace all nodes g(_,_) by h(_,_,_)

// freplG3H3a: Using insert

public NODE freplG2H3a(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2):
           insert h(T1, T2, i(0));
    };
}

// freplG3H3b: Using replacement rule

public NODE freplG2H3b(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2) => h(T1, T2, i(0))
    };
}

// drepl: Deep replacement of g by h (i.e. only innermost 
// g's are replaced); 

public NODE drepl(NODE T) {
    return bottom-up-break visit (T) {
      case g(NODE T1, NODE T2) =>  h(T1, T2)
    };
}

// srepl: shallow replacement of g by h (i.e. only outermost 
// g's are replaced); 

public NODE srepl(NODE T) {
    return top-down-break visit (T) {
       case g(NODE T1, NODE T2) =>  h(T1, T2)
    };
}

// count_and_inc: accumulating transformer that increments integer leaves with 
// amount D and counts them as well.

public tuple[int, NODE] count_and_inc(NODE T, int D) {
    int C = 0;
    
    T = visit (T) {
        case int N: { C = C + 1; 
                     insert N + D;
                    }
        };
    return <C, T>;
}

// Tests

private NODE N = f(g(i(1),g(i(2),i(3))),i(4));
   
public test bool f1() = cnta(N) == 4;
public test bool f2() = cntb(N) == 4;
public test bool f3() = sumtreea(N) == 10;
public test bool f4() = sumtreeb(N) == 10;
   
public test bool f5() = inc(N) == f(g(i(2),g(i(3),i(4))),i(5));
   
public test bool f6() = frepla(N) == f(h(i(1),h(i(2),i(3))),i(4));
public test bool f7() = freplb(N) == f(h(i(1),h(i(2),i(3))),i(4));
   
public test bool f8() = freplG2H3a(N) == f(h(i(1),h(i(2),i(3),i(0)), i(0)),i(4));
public test bool f9() = freplG2H3b(N) == f(h(i(1),h(i(2),i(3),i(0)), i(0)),i(4));
   
public test bool f10() = drepl(N) == f(g(i(1),h(i(2),i(3))),i(4));
public test bool f11() = srepl(N) == f(h(i(1),g(i(2),i(3))),i(4));
   
public test bool f12() = count_and_inc(N,3) == <4, f(g(i(4),g(i(5),i(6))),i(7))>;
