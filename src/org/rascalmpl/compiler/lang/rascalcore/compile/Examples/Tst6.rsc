module lang::rascalcore::compile::Examples::Tst6

import util::Math;
public map[&T <: num, int] distribution(map[&U event, &T <: num bucket] input, &T <: num bucketSize) {
  map[&T <: num,int] result = ();
  for (&U event <- input) {
    result[round(input[event], bucketSize)]?0 += 1;
  }
  return result;
}