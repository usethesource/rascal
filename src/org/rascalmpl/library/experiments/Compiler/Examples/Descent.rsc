module experiments::Compiler::Examples::Descent

data NODE = nd(NODE lhs, NODE rhs) | leaf(int n);

value main(list[value] args) {
	nd1 = "leaf"(1);
	nd2 = "nd"(nd1,"leaf"(2));
	nd3 = "nd"("leaf"(3),"leaf"(4));
	nd4 = "nd"(nd2,nd3);
	
	cnd1 = leaf(1);
	cnd2 = nd(cnd1,leaf(2));
	cnd3 = nd(leaf(3),leaf(4));
	cnd4 = nd(cnd2,cnd3);
	
	return <  { v | /v:"nd"(node _, "leaf"(int _)) <- "nd"(nd4,"leaf"(0)) }
			+ { v | /v: nd(NODE _, leaf(int _))    <-  nd(cnd4,leaf(0)) },
			  [ v | /v:"nd"(node _, "leaf"(int _)) <- "nd"(nd4,"leaf"(0)) ]
			+ [ v | /v: nd(NODE _, leaf(int _))    <-  nd(cnd4,leaf(0)) ]
		   >;
}