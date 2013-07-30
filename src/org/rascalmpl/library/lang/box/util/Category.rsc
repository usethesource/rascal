module lang::box::util::Category

extend lang::box::util::Box;

// Extension for highlights not supported by Box
data Box
  = CAT(str cat, list[Box] args)
  ;


