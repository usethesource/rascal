module experiments::tutor3::ParseQuestions

import experiments::tutor3::Questions;
import ParseTree;

public Questions parse(str src, loc l) = parse(#start[Questions], src, l).top;
public Questions parse(str src) = parse(#start[Questions], src).top;
public Questions parse(loc l) = parse(#start[Questions], l).top;