module TTL::tests::Patterns

import Type;
import IO;
import TTL::tests::Library;
import util::Eval;

test bool tst(&T x){
    if(result(v) := eval("<escape(x)> := (<escape(x)>);") && true := v)
       return true;
    return false;

}