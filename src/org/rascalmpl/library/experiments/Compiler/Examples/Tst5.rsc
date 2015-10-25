module experiments::Compiler::Examples::Tst5

value main() {
    return visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
                case set[node] s => s + { "d"(4,5) }
                case node n:str s(int x,int y) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
                case int i => i + 100
            } 
            //==
            //{ "a_been_here"(202,202),
            //  "b_been_here"(204,204),
            //  "d"(4,5),
            //  "c_been_here"(206,206)
            //}
            ;
}

//test bool visit11() {
//    return top-down visit({ "a"(1,1), "b"(2,2), "c"(3,3) }) {
//                case set[node] s => s + { "d"(4,5) }
//                case node n:str s(int x,int y) => { elem = ( 0 | it + i | int i <- n); (s + "_been_here")(elem,elem); }
//                case int i => i + 100
//            }
//            ==
//            { "a_been_here"(102,102),
//              "c_been_here"(106,106),
//              "b_been_here"(104,104),
//              "d_been_here"(109,109)
//            }
//            ;
//}