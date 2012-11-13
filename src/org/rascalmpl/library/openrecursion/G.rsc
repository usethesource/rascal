module openrecursion::G

import openrecursion::TraversalStrategies;
import IO;

data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2) | h(value V1, value V2, value V3);

public int cntA(NODE1 T) {
	int C = 0;
	visit(T) {
		case int N: C = C + 1;
	};
	return C;
}

public int cntB(NODE1 T) {
    int D = 0;   
    (bu(int (int n) { D = D + 1; return n; }) o fvisit)(T);
    return D;
}

public test bool test1() {
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
					 
	list[int] output = [ cntA(i) - cntB(i) | NODE1 i <- input ];
	return output == [0,0,0,0,0,0,0];
} 

public NODE1 cnt1A(NODE1 T) {
	return visit(T) {
		case int N => x when int x := N * 2, x >= 1
	};
}

public NODE1 cnt1B(NODE1 T) {
	int id(int N) = x when int x := N * 2, x >= 1;
	return (bu(id) o fvisit)(T);
}

public test bool test2() {
	list[NODE1] input = [f(3), f(1,2,3), f(1,g(2,3)), f(1,g(2,[3,4,5])), f(-1,g(2,[3,-4,5]))];
	list[NODE1] output = [f(6), f(2,4,6), f(2, g(4, 6)), f(2, g(4, [6, 8, 10])), f(-1,g(4,[6,-4,10]))];
	
	list[NODE1] output1 = [ cnt1A(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ cnt1B(i) | NODE1 i <- input ];
	
	return output1 == output && output2 == output;
}

data T = knot(int i, T l, T r) | tip(int i);

public T cnt2A(T t) {
	return visit(t) { 
		case tip(int i) => tip(i+1) 
		case knot(int i, T l, T r) => knot(i + l.i + r.i, l, r) 
	} 
}

public T cnt2B(T t) {
	T c(tip(int i)) = tip(i+1);
	T c(knot(int i, T l, T r)) = knot(i + l.i + r.i, l, r);
	return (bu(c) o fvisit)(t);
}

public T cnt2C(T t) {
	T c1(tip(int i)) = tip(i+1);
	T c2(knot(int i, T l, T r)) = knot(i + l.i + r.i, l, r);
	return (bu(c1) o bu(c2) o fvisit)(t);
}

public test bool test3() {
	T input = knot(0,tip(0),tip(0));
	T output = knot(2,tip(1),tip(1));
	return output == cnt2A(input) && output == cnt2B(input) && output == cnt2C(input);
}

public NODE1 frepaA(NODE1 T) {
    return visit (T) {
      case g(value T1, value T2): insert h(T1, T2);
    };
}

public NODE1 frepaB(NODE1 T) {
	NODE1 c(g(value T1, value T2)) = h(T1, T2);
	return (bu(c) o fvisit)(T);
}

public test bool test4() {
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,{3,4,5})),
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
	list[NODE1] output = [f(3),
						  f(1,2,3),
						  f(1,h(2,3)),
						  f(1,h(2,[3,4,5])),
						  f(1,h(2,{3,4,5})),
						  f(1,h(2,<3,4,5>)),
						  f(1,h(2,{<1,10>,<2,20>})),
						  f(1,h(2,(1:10,2:20)))];
	list[NODE1] output1 = [ frepaA(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ frepaB(i) | NODE1 i <- input ];
	return output == output1 && output == output2;
}

public NODE1 frepbA(NODE1 T) {
    return visit (T) {
      case g(value T1, value T2) => h(T1, T2)
      case int i => 0
    };
}

public NODE1 frepbB(NODE1 T) {
	NODE1 c1(g(value T1, value T2)) = h(T1, T2);
	int c2(int i) = 0;
	return (bu(c1) o bu(c2) o fvisit)(T);
}

public test bool test5() {
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,{3,4,5})),
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
	list[NODE1] output = [f(0),
						  f(0,0,0),
						  f(0,h(0,0)),
						  f(0,h(0,[0,0,0])),
						  f(0,h(0,{0,0,0})),
						  f(0,h(0,<0,0,0>)),
						  f(0,h(0,{<0,0>})),
						  f(0,h(0,(0:0)))];
	list[NODE1] output1 = [ frepbA(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ frepbB(i) | NODE1 i <- input ];
	return output == output1 && output == output2;
}

public NODE1 frepb1A(NODE1 T) {
    return visit (T) {
      case g(value T1, value T2) => h(T1, T2)
      case h(0,0) => h(500,500)
      case int i => 0
    };
}

public NODE1 frepb1B(NODE1 T) {
	NODE1 c1(g(value T1, value T2)) = h(T1, T2);
	NODE1 c1(h(0,0)) = h(500,500);
	int c2(int i) = 0;
	return (bu(c1) o bu(c2) o fvisit)(T);
}

public NODE1 frepb1C(NODE1 T) {
	NODE1 c1(g(value T1, value T2)) = h(T1, T2);
	NODE1 c1(h(0,0)) = h(500,500);
	int c2(int i) = 0;
	return (bu(c2) o bu(c1) o fvisit)(T);
}

public NODE1 frepb1D(NODE1 T) {
	NODE1 c1(g(value T1, value T2)) = h(T1, T2);
	NODE1 c1(h(0,0)) = h(500,500);
	int c2(int i) = 0;
	return (bu(c1) o (bu(c2) o fvisit))(T);
}

public test bool test6() {
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,h(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,{3,4,5})),
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
	list[NODE1] output = [f(0),
						  f(0,0,0),
						  f(0,h(0,0)),
						  f(0,h(500,500)), 
						  f(0,h(0,[0,0,0])),
						  f(0,h(0,{0})),
						  f(0,h(0,<0,0,0>)),
						  f(0,h(0,{<0,0>})),
						  f(0,h(0,(0:0)))];
	list[NODE1] output1 = [ frepb1A(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ frepb1B(i) | NODE1 i <- input ];
	list[NODE1] output3 = [ frepb1C(i) | NODE1 i <- input ];
	list[NODE1] output4 = [ frepb1D(i) | NODE1 i <- input ];
	return output == output1 && output == output2 && output == output3 && output == output4;
}

public NODE1 frepG2H3aA(NODE1 T) {
    return visit (T) {
      case g(value T1, value T2): insert h(T1, T2, 0);
    };
}

public NODE1 frepG2H3aB(NODE1 T) {
	NODE1 c(g(value T1, value T2)) = h(T1, T2, 0);
	return (bu(c) o fvisit)(T);
}

public test bool test7() {
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,{3,4,5})),
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
	list[NODE1] output = [f(3),
						  f(1,2,3),
						  f(1,h(2,3,0)),
						  f(1,h(2,[3,4,5],0)), 
						  f(1,h(2,{3,4,5},0)),
						  f(1,h(2,<3,4,5>,0)),
						  f(1,h(2,{<1,10>,<2,20>},0)),
						  f(1,h(2,(1:10,2:20),0))];
	list[NODE1] output1 = [ frepG2H3aA(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ frepG2H3aB(i) | NODE1 i <- input ];
	return output == output1 && output == output2;
}

public tuple[int, NODE1] inc_and_countA(NODE1 T, int D) {
    int C = 0;
    T = visit (T) {
        case int N: { C = C + 1; 
                      insert N + D;
                    }
        };
    return <C, T>;
}

public tuple[int, NODE1] inc_and_countB(NODE1 T, int D) {
    int C = 0;
    int c(int N) { C = C + 1; return N + D; };
    T = (bu(c) o fvisit)(T);
    return <C, T>;
}

public test bool test8() {
	int N = 10;
	list[NODE1] input = [f(3), 
					 	 f(1,2,3), 
					 	 f(1,g(2,3)), 
					 	 f(1,g(2,[3,4,5])), 
					 	 f(1,g(2,{3,4,5})),
					 	 f(1,g(2,<3,4,5>)), 
					 	 f(1,g(2,{<1,10>,<2,20>})), 
					 	 f(1,g(2,(1:10,2:20)))];
	list[tuple[int,NODE1]] output = [<1,f(13)>,
						  			 <3,f(11,12,13)>,
						  			 <3,f(11,g(12,13))>, 
					 	 			 <5,f(11,g(12,[13,14,15]))>, 
					 	 			 <5,f(11,g(12,{13,14,15}))>,
					 	 			 <5,f(11,g(12,<13,14,15>))>, 
					 	 			 <6,f(11,g(12,{<11,20>,<12,30>}))>, 
					 	 			 <6,f(11,g(12,(11:20,12:30)))>];
	list[tuple[int,NODE1]] output1 = [ inc_and_countA(i,N) | NODE1 i <- input ];
	list[tuple[int,NODE1]] output2 = [ inc_and_countB(i,N) | NODE1 i <- input ];
	return output == output1 && output == output2;
}

public NODE1 sreplA(NODE1 T) {
    return top-down-break visit (T) {
       case g(value T1, value T2) =>  h(T1, T2)
    };
}

public NODE1 sreplB(NODE1 T) {
	NODE1 c(g(value T1, value T2)) = h(T1, T2);
	return (tdb(c) o fvisit)(T);
}

public test bool test9() {
	list[NODE1] input = [f(3), 
					 	 g(1,2), 
					 	 g(1,f(g(2,3))),
					 	 g(1,f([g(2,3),4,5]))];
	list[NODE1] output = [f(3),
						  			 h(1,2),
						  			 h(1,f(g(2,3))), 
					 	 			 h(1,f([g(2,3),4,5]))];
	list[NODE1] output1 = [ sreplA(i) | NODE1 i <- input ];
	list[NODE1] output2 = [ sreplB(i) | NODE1 i <- input ];
	return output == output1 && output == output2;
}

