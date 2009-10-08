module demo::McCabe
import Graph;
import Set;
import Relation;
import UnitTest;

// McCabe Complexity (also Cyclomatic Complexity) is a measure for source code
// complexity, see http://en.wikipedia.org/wiki/Cyclomatic_complexity
// or McCabe's original article:
// T.J. McCabe, A Complexity Measure, IEEE Transactions on Software Engineering,
// Vol. 2, No. 4, p. 308 (1976)
// It is defined as a metric on the control flow graph:
//   number_of_edges - number_of_nodes + 2

public int cyclomaticComplexity(graph[&T] CFG){
    return size(CFG) - size(carrier(CFG)) + 2;
}

graph[int] G1 = {<1,2>, <2,3>};
graph[int] G3 = {<1,2>, <1,3>, <2,6>, <3,4>, <3,5>, <4,7>, <5,8>, <6,7>, <7,8>};
graph[int] G5 = {<1,2>, <2,3>, <2,4>, <3,6>, <4,2>, <4,5>, <5, 10>, <6, 7>, 
                 <7, 8>, <7,9>, <8,9>, <9, 7>, <9,10>};

public bool test(){
  assertEqual(cyclomaticComplexity(G1), 1);
  assertEqual(cyclomaticComplexity(G3), 3);
  assertEqual(cyclomaticComplexity(G5), 5);
  return report("McCabe");
}