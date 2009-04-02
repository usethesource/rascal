module demo::TreeTraversals
import List;
import UnitTest;

data NODE = i(int N) | f(NODE I, NODE J) | g(NODE I, NODE J) |  h(NODE I, NODE J)  | h(NODE I, NODE J, NODE K);


// Example 1: Count the int nodes in a tree

// Ex1a: Collect all integers in the tree in 
// a list and return the size of the list

public int cnta(NODE T) {
    return size([N | int N <- T]);
}

// Ex1b: alternative solution using a visit statement

public int cntb(NODE T) {
    int C = 0;
    visit(T) {
      case int N: C = C + 1;
    };
    return C;
}

// Ex2: Sum all leaves in a tree

// Ex2a: Collect all integers in the tree in 
// a list and use the library function sum on lists
// to add all list elements together.

public int sumtreea(NODE T) {
    return sum([N | int N <- T], 0);
}

// Ex2b: using visit statement

public int sumtreeb(NODE T) {
    int C = 0;
    visit(T) {
      case int N : C = C + N;
    };
    return C;
}

// Ex3: Increment all leaves in a tree

public NODE inc(NODE T) {
    return visit(T) {
      case int N: insert N + 1;
    };
}

// Ex4: full replacement of g by h, i.e.
// replace all nodes g(_,_) by h(_,_)

// Ex4a Using insert

public NODE frepa(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2):
           insert h(T1, T2);
    };
}

// Ex4a Using replacement rule

public NODE frepb(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2) => h(T1, T2)
    };
}

// Ex5 Replace all nodes g(_,_) by h(_,_,_)

// Ex5a Using insert

public NODE frepG2H3a(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2):
           insert h(T1, T2, i(0));
    };
}

// Ex5b Using replacement rule

public NODE frepG2H3b(NODE T) {
    return visit (T) {
      case g(NODE T1, NODE T2) => h(T1, T2, i(0))
    };
}

// Ex6: Deep replacement of g by h (i.e. only innermost 
// g's are replaced); 

public NODE drepl(NODE T) {
    return bottom-up-break visit (T) {
      case g(NODE T1, NODE T2) =>  h(T1, T2)
    };
}

// Ex7: shallow replacement of g by h (i.e. only outermost 
// g's are replaced); 

public NODE srepl(NODE T) {
    return top-down-break visit (T) {
       case g(NODE T1, NODE T2) =>  h(T1, T2)
    };
}

// Ex8: accumulating transformer that increments integer leaves with 
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

public bool test(){
   NODE N = f(g(i(1),g(i(2),i(3))),i(4));
   
   assertTrue(cnta(N) == 4);
   assertTrue(cntb(N) == 4);
   assertTrue(sumtreea(N) == 10);
   assertTrue(sumtreeb(N) == 10);
   
   assertTrue(inc(N) ==  f(g(i(2),g(i(3),i(4))),i(5)));
   
   assertTrue(frepa(N) ==   f(h(i(1),h(i(2),i(3))),i(4)));
   assertTrue(frepb(N) ==   f(h(i(1),h(i(2),i(3))),i(4)));
   
   assertTrue(frepG2H3a(N) ==   f(h(i(1),h(i(2),i(3),i(0)), i(0)),i(4)));
   assertTrue(frepG2H3b(N) ==   f(h(i(1),h(i(2),i(3),i(0)), i(0)),i(4)));
   
   assertTrue(drepl(N) ==  f(g(i(1),h(i(2),i(3))),i(4)));
   assertTrue(srepl(N) ==  f(h(i(1),g(i(2),i(3))),i(4)));
   
   assertTrue(count_and_inc(N,3) == <4, f(g(i(4),g(i(5),i(6))),i(7))>);
   
   return report();
}
