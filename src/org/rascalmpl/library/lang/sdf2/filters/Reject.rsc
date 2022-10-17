module lang::sdf2::filters::Reject

import ParseTree;

@doc{ Import his module if you want SDF2 style reject filtering enabled for your grammar. Use @reject to
label one alternative and the whole non-terminal will be filtered if it matches.
}
&T <: Tree rejectFilter(amb(set[&T <: Tree] alts)) {
  if (appl(prod(_,_,{*_,\tag("reject"())}),_) <- alts) 
    filter;
  fail;
} 
