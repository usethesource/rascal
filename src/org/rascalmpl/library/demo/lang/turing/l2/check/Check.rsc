module demo::lang::turing::l2::check::Check

import demo::lang::turing::l2::ast::Turing;

import Message;
import Node;

  
public set[Message] check(Program p) {
 jls = { j | /Statement j <- p, /^jump/ := getName(j), j has name };
 dls = { l | /l:label(n) <- p };
 
 set[str] names(set[Statement] ss) = { s.name | s <- ss };
 
 set[Statement] l1s = { j | /Statement j <- p, /^jump/ := getName(j), j has line };
 
 return { error("Undefined label", j@location) | j <- jls, j.name notin names(dls) }
   + { warning("Unused label", l@location) | l <- dls, l.name notin names(jls) }
   + { warning("Level 1 jump", j@location) | j <- l1s };
}
