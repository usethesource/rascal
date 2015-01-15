module demo::lang::turing::l2::check::Check

import demo::lang::turing::l2::ast::Turing;

import Message;
import Node;

  
public set[Message] check(Program p) {
 set[Message] errs = {};
 seen = {};
 for (/l:label(n) <- p) {
   if (n in seen) {
     errs += {error("Duplicate label", l.origin)};
   }
   else {
     seen += {n};
   }
 } 

 set[str] names(set[Statement] ss) = { s.name | s <- ss };
 jls = { j | /Statement j <- p, /^jump/ := getName(j), j has name };
 dls = { l | /l:label(n) <- p };
 
 errs += { error("Undefined label", j.origin) | j <- jls, j.name notin names(dls) };
 errs += { warning("Unused label", l.origin) | l <- dls, l.name notin names(jls) };
 
 set[Statement] l1s = { j | /Statement j <- p, /^jump/ := getName(j), j has line };
 errs += { warning("Level 1 jump", j.origin) | j <- l1s };
 
 return errs;
}
