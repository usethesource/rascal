@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::McCabe
import  analysis::graphs::Graph;
import Set;
import Relation;


@synopsis{Compute the cyclomatic complexity of a graph}
@description{

McCabe Complexity (also Cyclomatic Complexity) is a measure for source code
complexity, see <http://en.wikipedia.org/wiki/Cyclomatic_complexity>
or McCabe's original article:
> T.J. McCabe, A Complexity Measure, IEEE Transactions on Software Engineering,
> Vol. 2, No. 4, p. 308 (1976)

It is defined as a metric on the control flow graph:
  `number_of_edges - number_of_nodes + 2`

}
public int cyclomaticComplexity(Graph[&T] CFG){
    return size(CFG) - size(carrier(CFG)) + 2;
}

// Tests

Graph[int] G1 = {<1,2>, <2,3>};
Graph[int] G3 = {<1,2>, <1,3>, <2,6>, <3,4>, <3,5>, <4,7>, <5,8>, <6,7>, <7,8>};
Graph[int] G5 = {<1,2>, <2,3>, <2,4>, <3,6>, <4,2>, <4,5>, <5, 10>, <6, 7>, 
                 <7, 8>, <7,9>, <8,9>, <9, 7>, <9,10>};

test bool tstCyclomaticComplexity1() = cyclomaticComplexity(G1) == 1;
test bool tstCyclomaticComplexity2() = cyclomaticComplexity(G3) == 3;
test bool tstCyclomaticComplexity3() = cyclomaticComplexity(G5) == 5;
