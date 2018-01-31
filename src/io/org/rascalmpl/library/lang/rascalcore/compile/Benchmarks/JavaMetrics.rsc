module lang::rascalcore::compile::Benchmarks::JavaMetrics

import Prelude;
import util::Math;

import lang::java::m3::Core;
import lang::java::m3::AST;

bool initialized = false;
set[Declaration] allData = {};

private set[Declaration] getData() {
    if(!initialized){
        allData = readBinaryValueFile(#set[Declaration], |compressed+std:///experiments/Compiler/Benchmarks/pdb.values.bin.xz|);
    }
    return allData;
}       

void methodsPerClass(M3 model){
    for(c <- classes(model)){
        println("<c>: <size(methods(model, c))>");
    }  
} 

int numberOfClasses(M3 model) = size(classes(model));

set[loc] classesWithMostMethods(M3 model){
    methodCount = ( c : size(methods(model, c)) | c <- classes(model));
    largest = max(range(methodCount));
    return { c | c <- classes(model), methodCount[c] == largest };
}

// Metrics using the AST

int countCasts1(set[Declaration] decls){
    int cnt = 0;
    visit(decls){
        case \cast(_, _): cnt += 1;
    }
    return cnt;
}

int countCasts2(set[Declaration] decls) =
    size([c | /c: \cast(_, _) := decls]);


int countIfNesting(Statement stat){
    top-down-break visit(stat){
        case \if(_, Statement thenBranch): 
            return 1 + countIfNesting(thenBranch);
        case \if(_, Statement thenBranch, Statement elseBranch):
            return 1 + max(countIfNesting(thenBranch), countIfNesting(elseBranch));
    }
    return 0;
}

set[loc] ifNesting(set[Declaration] decls, int limit){
    results = {};
    visit(decls){
        case m: \method(_, _, _, _, Statement impl):
                if(countIfNesting(impl) > limit)
                    results += m@src;
        case c: \constructor(_, _,_, Statement impl):
                if(countIfNesting(impl) > limit)
                    results += c@src;
    }
    return results;
}