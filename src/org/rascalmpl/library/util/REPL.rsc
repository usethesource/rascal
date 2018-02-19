module util::REPL

import String;
import Message;
import ParseTree;
import salix::App;
import util::Webserver;
import IO;

alias Completion
 = tuple[int offset, list[str] suggestions];

//alias CommandResult
//  = tuple[str result, list[Message] messages] 
//  ;
  
data CommandResult(list[Message] messages = [])
  = string(str result)
  | salix(SalixApp[&T] app)
  ; 
 
  
data REPL(SalixMultiplexer salix = noOp())
  = repl(str title, str welcome, str prompt, loc history, 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor)
  | repl( 
         CommandResult (str line) handler,
         Completion(str line, int cursor) completor
         )
  ;


alias SalixConsumer
  = void(SalixApp[value] app, str scope);

alias SalixMultiplexer
  = tuple[SalixConsumer consumer, void() stop, loc http];
  
SalixMultiplexer noOp() = <(SalixApp[value] x) {}, () {}, |http://localhost|>;

/*

public REPL myRepl = repl(

  , salix = makeSalixMultiplexedr(..., ...));



In java:

get the salix keyword param,
call the consumer function with the result of the salix(...) CommandResult
and the unique key.

*/


SalixMultiplexer makeSalixMultiplexer(loc http, loc static) {
  map[str, SalixApp[void]] apps = ();
  
  Response respondHttp(SalixResponse r)
    = response(("commands": r.cmds, "subs": r.subs, "patch": r.patch), ("Access-Control-Allow-Origin":"*","Access-Control-Allow-Headers":"Origin, X-Requested-With, Content-Type, Accept"));

  Response _handle(Request req) {
  
  scope = split("/", req.path)[1];
    switch (req) {
      case get("/<scope>/init"):
        return respondHttp(apps[scope](begin(), scope));
    
      case get("/<scope>/msg"): 
        return respondHttp(apps[scope](message(req.parameters), scope));
      
      case get(p:/\.<ext:[^.]*>$/):
        return fileResponse(static[path="<static.path>/<p>"], mimeTypes[ext], ());

      default: 
        return response(notFound(), "not handled: <req.path>");
    }
  }
  
  println("Serving: <http>"); 
  serve(http, _handle);
  
  return <void(SalixApp[void] app, str key) {
    apps[key] = app;
  }, () {
    shutdown(http);
  }, http>;
}

@javaClass{org.rascalmpl.library.util.TermREPL}
@reflect
java void startREPL(REPL repl);