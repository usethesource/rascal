module lang::rascalcore::compile::Examples::Origins

import String;
import util::Benchmark;
import util::Math;
import IO;

data StringWithOrigin
=   stringWithOrigin(
        str(str, loc) add,
        tuple[str, rel[int, int, loc]]() construct
);

StringWithOrigin newStringWithOrigin(str initial){
    str current = initial;
    rel[int,int,loc] toOrigin = {};
    int len = size(initial);

    str _add(str s, loc orgLoc){
        slen = size(s);
        toOrigin += <len, len + slen, orgLoc>;
        current += s;
        return s;
     }

     tuple[str, rel[int, int, loc]] _construct(){
        return <current, toOrigin>;
     }

     return stringWithOrigin(_add, _construct);
}

void main(){
    strings = [arbString(arbInt(50)) | int _ <- [0.. 100000]];
    begin1 = cpuTime();
    result1 = "";
    for(s <- strings) result1 += s;
    end1 = cpuTime();

    begin2 = cpuTime();
    nso = newStringWithOrigin("");
    l = |unknown:///|;
    for(s <- strings) nso.add(s, l);

    <result2, omap> =  nso.construct();
    end2 = cpuTime();
    t1 = (end1 - begin1)/1000000;
    t2 = (end2 - begin2)/1000000;
    println("<t1> versus <t2>: <t2/t1>");

}

