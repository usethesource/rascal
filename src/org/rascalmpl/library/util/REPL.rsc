module util::REPL

import Message;
import ParseTree;

alias Completion
 = tuple[int offset, list[str] suggestions];

data CommandResult(list[Message] messages = [])
  = commandResult(str result)
  ;  
 
data REPL
  = repl(str title, str welcome, str prompt, loc history, 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor)
  | repl( 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor)         
  ;

@javaClass{org.rascalmpl.library.util.TermREPL}
@reflect
java void startREPL(REPL repl);