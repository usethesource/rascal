module demo::lang::Lisra::Console

import Prelude;
import demo::lang::Lisra::Runtime;
import demo::lang::Lisra::Syntax;
import demo::lang::Lisra::Parse;
import demo::lang::Lisra::Eval;
import demo::lang::Lisra::Pretty;
import util::IDE;

// Parse and evaluate an expression.

public Result eval(str txt, Env env) = eval(parse(txt), env); /*1*/

// Create an interactive console.

public void console(){                                        /*2*/
   env = emptyEnv;
   createConsole("Lisra Console",                             /*3*/
                 "Welcome to the Awesome Lisra Interpreter\nLisra\> ", 
                 str (str inp) { <val, env> = eval(inp, env); 
                                 return "<pretty(val)>\nLisra\> ";
                               });
}
