module lang::rascal::tutor::ParseQuestions

import lang::rascal::tutor::Questions;
import ParseTree;

public Questions parse(str src, loc l) = parse(#start[Questions], src, l).top;
public Questions parse(str src) = parse(#start[Questions], src).top;
public Questions parse(loc l) = parse(#start[Questions], l).top;