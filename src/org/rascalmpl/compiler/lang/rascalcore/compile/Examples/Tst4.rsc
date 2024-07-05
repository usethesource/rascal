module lang::rascalcore::compile::Examples::Tst4

//alias T = tuple[int, str];
void main() {
  //set[tuple[int, str]] r = {};

  // Error
  // `Initialization of `ts1` should be subtype of `rel[int, str]`, found `rel[value, value]``
  //set[tuple[int, str]] ts1 = {t | t:<int _, _> <- r};
  
  set[str] a = {};
  set[str] b = { x| t: x <- a};

  //// No errors
  //set[T] ts2 = {t | t <- r};
  //set[T] ts3 = {t | T t:<_, _> <- r};
  //set[T] ts4 = {<i, s> | <i, s> <- r};
  //set[T] ts5 = {t | t:<int _, str _> <- r};
}

//value main() = _f(3);

//data Tree;
//anno set[int] Tree@messages;
//
//data TModel(list[int] messages = []);
//
//list[int] f(TModel tm) = tm.messages;