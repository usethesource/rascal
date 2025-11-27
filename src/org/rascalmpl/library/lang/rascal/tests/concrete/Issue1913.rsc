module lang::rascal::tests::concrete::Issue1913

extend lang::std::Layout;
import IO;

syntax As = "begin" A* as "end";

syntax A = "a" | "b";

test bool issue1913() {
    A* bs = (As)`begin b a b end`.as;

    As prog = (As)`begin b a a end`;

    prog = visit (prog) {
        case (As)`begin <A* as1> b <A* as2> end` 
            => (As)`begin <A* as1> <A* bs> <A* as2> end`
    }

    println(prog);
    // don't loose a space
    return "<prog>" == "begin b a b a a end";
}