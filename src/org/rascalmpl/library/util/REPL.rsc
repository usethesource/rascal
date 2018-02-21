module util::REPL

import String;
import Message;
import ParseTree;
import util::Webserver;
import IO;

alias Completion
 = tuple[int offset, list[str] suggestions];

//alias CommandResult
//  = tuple[str result, list[Message] messages] 
//  ;
  
data CommandResult(list[Message] messages = [])
  = string(str result)
  ; 
 
  
data REPL
  = repl(str title, str welcome, str prompt, loc history, 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor)
  | repl( 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor
         )
  ;


@javaClass{org.rascalmpl.library.util.TermREPL}
@reflect
java void startREPL(REPL repl);