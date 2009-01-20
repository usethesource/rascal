module Tree-examples
import List;

data NODE int N | f(NODE I, NODE J) | g(NODE I, NODE J) |  h(NODE I, NODE J)  | h(NODE I, NODE J, NODE K);


// Example 1: Count the int nodes in a tree

// Ex1a: Collect all integers in the tree in 
// a list and return the size of the list

public int cnta(tree T) {
    return size([N | int N : T]);
}

// Ex1b: alternative solution using a visit statement

public int cntb(tree T) {
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

public int sumtreea(tree T) {
    return sum([n | int N : T]);
}

// Ex2b: using visit statement

public int sumtreeb(tree T) {
    int C = 0;
    visit(T) {
      case int N : C = C + N;
    };
    return C;
}

// Ex3: Increment all leaves in a tree

public tree inc(tree T) {
    return visit(T) {
      case int N: insert N + 1;
    };
}

// Ex4: full replacement of g by h, i.e.
// replace all nodes g(_,_) by h(_,_)

// Ex4a Using insert

public tree frepa(tree T) {
    return visit (T) {
      case g(value T1, value T2):
           insert h(T1, T2);
    };
}

// Ex4a Using replacement rule

public tree frepb(tree T) {
    return visit (T) {
      case g(value T1, value T2) => h(T1, T2)
    };
}

// Ex5 Replace all nodes g(_,_) by h(_,_,_)

// Ex5a Using insert

public tree frepG2H3a(tree T) {
    return visit (T) {
      case g(value T1, value T2):
           insert h(T1, T2, 0);
    };
}

// Ex5b Using replacement rule

public tree frepG2H3b(tree T) {
    return visit (T) {
      case g(value T1, value T2) => h(T1, T2, 0)
    };
}

// Ex6: Deep replacement of g by h (i.e. only innermost 
// g's are replaced); 

public tree drepl(tree T) {
    return bottom-up-break visit (T) {
      case g(value T1, value T2) =>  h(T1, T2)
    };
}

// Ex7: shallow replacement of g by h (i.e. only outermost 
// g's are replaced); 

public tree srepl(tree T) {
    return top-down-break visit (T) {
       case g(value T1, value T2) =>  h(T1, T2)
    };
}


// Ex8: accumulating transformer that increments integer leaves with 
// amount D and counts them as well.

public tuple[int, tree] count_and_inc(tree T, int D) {
    int C = 0;
    
    T = visit (T) {
        case int N: { C = C + 1; 
                     insert N + D;
                    }
        };
    return <C, T>;
}

// Add an element to an array

public tree array(tree T) {
    return visit (T) {
      case list[value] L => L + [11]
    };
}
