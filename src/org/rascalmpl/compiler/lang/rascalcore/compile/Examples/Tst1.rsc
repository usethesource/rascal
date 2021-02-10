module lang::rascalcore::compile::Examples::Tst1

public list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];

test bool tstMapper(list[int] L) {
  int incr(int x) { return x + 1; };
  return mapper(L, incr) == [x + 1 | x <- L];
}