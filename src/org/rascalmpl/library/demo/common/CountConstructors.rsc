// tag::module[]
module demo::common::CountConstructors

import Node;
import Map;

// Define a ColoredTree data type

data ColoredTree = leaf(int N)      
                 | red(ColoredTree left, ColoredTree right) 
                 | black(ColoredTree left, ColoredTree right);
                 
public ColoredTree CT = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));

// Define a Card data type.
             
data Suite = hearts() | diamonds() | clubs() | spades();

data Card =  two(Suite s) | three(Suite s) | four(Suite s) | five(Suite s) |
             six(Suite s) | seven(Suite s) | eight(Suite s) | nine(Suite s) | ten(Suite s) |
             jack(Suite s) | queen(Suite s) | king(Suite s) | ace(Suite s);
             
data Hand = hand(list[Card] cards);

public Hand H = hand([two(hearts()), jack(diamonds()), six(hearts()), ace(spades())]);

// Count frequencies of constructors

map[str,int] count(node N){ // <1>
  freq = (); // <2>
  visit(N){  // <3>
    case node M: { name = getName(M); // <4>
                   freq[name] ? 0 += 1; 
                 }
  }
  return freq; // <5>
}

map[str,int] countRelevant(node N, set[str] relevant) = domainR(count(N), relevant); // <6>
// end::module[]

test bool tstCount() =  count(CT) == ("red":2, "leaf":5, "black":2);
test bool tstCountRelevant() = countRelevant(CT, {"leaf"}) == ("leaf" : 5);
