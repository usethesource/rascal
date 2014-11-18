module experiments::Compiler::Examples::Tst2

// Recently introduced in List.rsc
public list[&T] concat(list[list[&T]] xxs) =
  ([] | it + xs | xs <- xxs);

 value main(list[value] args) = concat([]);
