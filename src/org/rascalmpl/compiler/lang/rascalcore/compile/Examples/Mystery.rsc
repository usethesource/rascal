module lang::rascalcore::compile::Examples::Mystery
import ParseTree;
import IO;

data P = t() | f() | axiom(P mine = t());

data P(int size = 0);

// Analysis of the following test in lang::rascal::tests::functionality::Reification
//test bool allConstructorsHaveTheCommonKwParam()
//  =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label("size", \int()) in kws);

test bool allConstructorsHaveTheCommonKwParam(){
    iprintln(#P.definitions);
    // Alternative computation of the original test
    allKws = {kws | /choice(def, /cons(_,_,kws,_)) := #P.definitions};
    result1 = true;
    for(kws <- allKws){
        println("<kws>: <label("size", \int()) in kws>");
        result1 = result1 && label("size", \int()) in kws;
    }   
    // The original test:
    result2 =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions && label("size", \int()) in kws);

    println("result1, result2: <result1>, <result2>");
    return result1 && result2;
}

bool main() = allConstructorsHaveTheCommonKwParam();