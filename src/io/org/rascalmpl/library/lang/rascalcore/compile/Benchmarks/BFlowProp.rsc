module lang::rascalcore::compile::Benchmarks::BFlowProp

import IO;
import ValueIO;
import Set;
import Relation;
import Map;
import util::Benchmark;

alias OFG = rel[loc from, loc to];

rel[loc,&T] propagateOriginal(OFG g, rel[loc,&T] gen, rel[loc,&T] kill, bool back) {
  rel[loc,&T] IN = { };
  rel[loc,&T] OUT = gen + (IN - kill);
  gi = g<to,from>;
  set[loc] pred(loc n) = gi[n];
  set[loc] succ(loc n) = g[n];

  solve (IN, OUT) {
    IN = { <n,\o> | n <- carrier(g), p <- (back ? pred(n) : succ(n)), \o <- OUT[p] };
    OUT = gen + (IN - kill);
  }

  return OUT;
}

rel[&G,&T] propagate1(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    succs = toMap(g);
    preds = toMap(g<1,0>);
    gall = carrier(g);

    solve (IN, OUT) {
        IN = { <n, \o> | n <- gall, p <- (back ? (preds[n]?{}) : (succs[n]?{})), \o <- OUT[p] };
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate2(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    succs = toMap(g);
    preds = toMap(g<1,0>);
    gall = carrier(g);

    solve (IN, OUT) {
        IN = { <n, \o> | n <- gall, \o <- OUT[(back ? (preds[n]?{}) : (succs[n]?{}))] };
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate3(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    succs = toMap(g);
    preds = toMap(g<1,0>);
    gall = carrier(g);

    solve (IN, OUT) {
        IN2 = { *({n} * (back ? (preds[n]?{}) : (succs[n]?{}))) | n <- gall };
        IN = IN2 o OUT;
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate4(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    nexts = back ? toMap(g<1,0>) : toMap(g);
    gall = carrier(g);

    solve (IN, OUT) {
        IN2 = { *({n} * (nexts[n]?{})) | n <- gall };
        IN = IN2 o OUT;
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate5(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    if (back) {
        g = g<1,0>;
    }
    gall = carrier(g);

    solve (IN, OUT) {
        IN2 = { t | t <- g, (t<0> in gall) }; 
        IN = IN2 o OUT;
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate6(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    if (back) {
        g = g<1,0>;
    }

    solve (IN, OUT) {
        IN = g o OUT;
        OUT = gen + (IN - kill);
    }

    return OUT;
}

rel[&G,&T] propagate7(rel[&G,&G] g, rel[&G,&T] gen, rel[&G,&T] kill, bool back) {
    rel[&G,&T] IN = { };
    rel[&G,&T] OUT = gen + (IN - kill);
    if (back) {
        g = g<1,0>;
    }
    
    // this is how we calculate compose in java side of things should be comparable in speed?
    solve (IN, OUT) {
        mOUT = toMap(OUT);
        IN = { <n,\o> | <n,p> <- g, \o <- (mOUT[p]?{})};
        OUT = gen + (IN - kill);
    }

    return OUT;
}

alias BenchData = tuple[rel[loc,loc] inputGraph, rel[loc,bool] genGraph];

BenchData getData() 
    = readBinaryValueFile(#BenchData, |compressed+std:///experiments/Compiler/Benchmarks/corebench.bin.xz|);

tuple[rel[loc,bool], int] getRef(BenchData bd)
    = <propagateOriginal(bd.inputGraph, bd.genGraph, {}, true), 
        cpuTime(() { propagateOriginal(bd.inputGraph, bd.genGraph, {}, true); }) / (1000*1000)>;
        
int calcFor(rel[&G,&T] (rel[&G,&G],rel[&G,&T],rel[&G,&T],bool) target, BenchData bd, rel[&G, &T] expected, int rounds) {
    if (expected != {}) {
        actual = target(bd.inputGraph, bd.genGraph, {}, true);
        if (expected != actual) {
            return -1;
        }
    }
    return (cpuTime(() { for (i <- [0..rounds]) target(bd.inputGraph, bd.genGraph, {}, true); }) / rounds) / (1000*1000);
}

int main(int rounds = 5) {
    println("Getting data");
    bd = getData();
    println("<size(bd.inputGraph)> : <size(bd.genGraph)>");
    println("Calculating reference");
    <ref, refTime> = getRef(bd);
    println("Reference graph took <refTime>ms to return <size(ref)> edges.");
    println("Prop1:\t<calcFor(propagate1, bd, ref, rounds)>ms");
    println("Prop2:\t<calcFor(propagate2, bd, ref, rounds)>ms");
    println("Prop3:\t<calcFor(propagate3, bd, ref, rounds)>ms");
    println("Prop4:\t<calcFor(propagate4, bd, ref, rounds)>ms");
    println("Prop5:\t<calcFor(propagate5, bd, ref, rounds)>ms");
    println("Prop6:\t<calcFor(propagate6, bd, ref, rounds)>ms");
    println("Prop7:\t<calcFor(propagate7, bd, ref, rounds)>ms");
    return 0;
}